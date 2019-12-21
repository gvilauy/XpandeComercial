package org.xpande.comercial.callout;

import org.compiere.model.*;
import org.compiere.util.DB;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Properties;

/**
 * Callouts Comerciales.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 8/23/17.
 */
public class CalloutComercial extends CalloutEngine {


    /***
     * Setea DocBaseType según ID de documento recibido.
     * Xpande. Created by Gabriel Vila on 8/23/17.
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     * @return
     */
    public String docBaseTypeByDocType(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {

        if ((value == null) || (((Integer) value).intValue() <= 0)){
            return "";
        }

        int cDocTypeID = ((Integer) value).intValue();

        MDocType docType = new MDocType(ctx, cDocTypeID, null);
        mTab.setValue("DocBaseType", docType.getDocBaseType());

        return "";
    }


    /***
     * En una vencimiento de una invoice, al digitar fecha de vencimiento, setea monto vencimiento con lo pendiente a vencer de la invoice.
     * Xpande. Created by Gabriel Vila on 8/28/17.
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     * @return
     */
    public String dueAmtByInvoiceDueDate(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {

        if (value == null){
            return "";
        }

        Timestamp dueDate = (Timestamp) value;

        int cInvoiceID = Env.getContextAsInt(ctx, WindowNo, "C_Invoice_ID");
        MInvoice invoice = new MInvoice(ctx, cInvoiceID, null);

        // Total montos vencimientos de este comprobante
        String sql = " select sum(dueamt) as totalvenc from c_invoicepayschedule where c_invoice_id =" + cInvoiceID;
        BigDecimal totalVenc = DB.getSQLValueBDEx(null, sql);
        if (totalVenc == null){
            totalVenc = Env.ZERO;
        }

        BigDecimal montoVenc = invoice.getGrandTotal().subtract(totalVenc).setScale(2, RoundingMode.HALF_UP);
        mTab.setValue(X_C_InvoicePaySchedule.COLUMNNAME_DueAmt, montoVenc);
        mTab.setValue(X_C_InvoicePaySchedule.COLUMNNAME_IsValid, true);

        return "";
    }

    /***
     * Setea ID de socio de negocio, según número de identificación recibido (RUT, CI, etc)
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     * @return
     */
    public String partnerByTaxID(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {

        if (value == null){
            mTab.setValue(X_C_BPartner.COLUMNNAME_C_BPartner_ID, null);
            return "";
        }

        String taxID = ((String)value).trim();
        if (taxID.equalsIgnoreCase( "")){
            mTab.setValue(X_C_BPartner.COLUMNNAME_C_BPartner_ID, null);
            return "";
        }

        String whereClause = X_C_BPartner.COLUMNNAME_TaxID + " ='" + taxID + "' ";
        int[] partnerIDs = MBPartner.getAllIDs(I_C_BPartner.Table_Name, whereClause, null);

        if (partnerIDs.length > 0){
            MBPartner partner = new MBPartner(ctx, partnerIDs[0], null);
            mTab.setValue(X_C_BPartner.COLUMNNAME_C_BPartner_ID, partner.get_ID());
        }
        else{
            mTab.setValue(X_C_BPartner.COLUMNNAME_C_BPartner_ID, null);
        }

        return "";
    }


    /***
     * Setea ID localización de socio de negocio, según ID de socio de negocio recibido.
     * Xpande. Created by Gabriel Vila on 7/25/19.
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     * @return
     */
    public String partnerLocationByPartnerID(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {

        Integer C_BPartner_ID = (Integer)value;
        if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0){
            return "";
        }

        MBPartnerLocation[] arrayLocations = MBPartnerLocation.getForBPartner(ctx, C_BPartner_ID, null);
        if (arrayLocations.length > 0){
            mTab.setValue(X_C_Invoice.COLUMNNAME_C_BPartner_Location_ID, arrayLocations[0].get_ID());
        }
        else {
            mTab.setValue(X_C_Invoice.COLUMNNAME_C_BPartner_Location_ID, null);
        }

        return "";
    }

    /***
     * Al ingresar organización se setea un almacen asociado a dicha organizacion.
     * Xpande. Created by Gabriel Vila on 10/17/19.
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     * @return
     */
    public String setWarehouseByOrg(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {

        if (isCalloutActive()) return "";

        if (value == null){
            mTab.setValue("M_Warehouse_ID", null);
            return "";
        }

        int adOrgID = (Integer) value;
        if (adOrgID <= 0){
            mTab.setValue("M_Warehouse_ID", null);
            return "";
        }

        // Busco almacen asociada a la organización ingresada
        MWarehouse[] warehouseList = MWarehouse.getForOrg(ctx, adOrgID);
        if (warehouseList.length > 0){
            mTab.setValue("M_Warehouse_ID", warehouseList[0].get_ID());
        }
        else {
            mTab.setValue("M_Warehouse_ID", null);
        }

        return "";
    }

}
