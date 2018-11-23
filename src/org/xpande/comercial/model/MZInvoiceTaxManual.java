package org.xpande.comercial.model;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 * Modelo para gestión de impuestos manuales en comprobantes comerciales.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/13/17.
 */
public class MZInvoiceTaxManual extends X_Z_InvoiceTaxManual {

    public MZInvoiceTaxManual(Properties ctx, int Z_InvoiceTaxManual_ID, String trxName) {
        super(ctx, Z_InvoiceTaxManual_ID, trxName);
    }

    public MZInvoiceTaxManual(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean afterSave(boolean newRecord, boolean success) {

        if (!success) return success;

        // Actualizo total del comprobante considerando impuestos manuales
        this.updateTotalDocumento();

        return true;
    }

    @Override
    protected boolean afterDelete(boolean success) {

        if (!success) return success;

        // Actualizo total del comprobante considerando impuestos manuales
        this.updateTotalDocumento();

        return true;
    }


    /***
     * Actualiza total de la invoice asociada a este impuesto manual.
     * Xpande. Created by Gabriel Vila on 10/13/17.
     */
    private void updateTotalDocumento() {

        String action = "", sql = "";

        try{
            // Obtengo suma de impuestos manuales con el comportamiento de SUMAR AL SUBTOTAL
            sql = " select coalesce(sum(a.taxamt), 0) as total " +
                    "from z_invoicetaxmanual a " +
                    "inner join c_tax t on a.c_tax_id = t.c_tax_id " +
                    "where c_invoice_id =" + this.getC_Invoice_ID() +
                    "and ManualTaxAction = 'SUMAR' ";

            BigDecimal sumar = DB.getSQLValueBDEx(get_TrxName(), sql);
            if (sumar == null) sumar = Env.ZERO;

            // Obtengo suma de impuestos manuales con el comportamiento de RESTAR AL SUBTOTAL
            sql = " select coalesce(sum(a.taxamt), 0) as total " +
                    "from z_invoicetaxmanual a " +
                    "inner join c_tax t on a.c_tax_id = t.c_tax_id " +
                    "where c_invoice_id =" + this.getC_Invoice_ID() +
                    "and ManualTaxAction = 'RESTAR' ";

            BigDecimal restar = DB.getSQLValueBDEx(get_TrxName(), sql);
            if (restar == null) restar = Env.ZERO;

            BigDecimal importeTaxManuales = sumar.subtract(restar);

            // Actualizo total de la invoice
            action = " update c_invoice set grandtotal = totallines + (coalesce(amtrounding,0)) + " + importeTaxManuales +
                    " where c_invoice_id =" + this.getC_Invoice_ID();
            DB.executeUpdateEx(action, get_TrxName());

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

    }


    /***
     * Obtiene y retorna lista de impuestos manuales para determinada invoice recibida.
     * Xpande. Created by Gabriel Vila on 10/13/17.
     * @param ctx
     * @param cInvoiceID
     * @param trxName
     * @return
     */
    public static List<MZInvoiceTaxManual> getManualTaxes(Properties ctx, int cInvoiceID, String trxName){

        String whereClause = X_Z_InvoiceTaxManual.COLUMNNAME_C_Invoice_ID + " =" + cInvoiceID;

        List<MZInvoiceTaxManual> lines = new Query(ctx, I_Z_InvoiceTaxManual.Table_Name, whereClause, trxName).list();

        return lines;

    }
}
