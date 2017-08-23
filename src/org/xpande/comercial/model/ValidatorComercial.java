package org.xpande.comercial.model;

import org.compiere.acct.Doc;
import org.compiere.model.*;
import org.compiere.util.DB;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 8/23/17.
 */
public class ValidatorComercial implements ModelValidator {

    private int adClientID = 0;

    @Override
    public void initialize(ModelValidationEngine engine, MClient client) {

        // Guardo compañia
        if (client != null){
            this.adClientID = client.get_ID();
        }

        // DB Validations
        engine.addModelChange(I_C_Invoice.Table_Name, this);

        // Document Validations
        engine.addDocValidate(I_C_Invoice.Table_Name, this);

    }

    @Override
    public int getAD_Client_ID() {
        return this.adClientID;
    }

    @Override
    public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
        return null;
    }

    @Override
    public String modelChange(PO po, int type) throws Exception {

        if (po.get_TableName().equalsIgnoreCase(I_C_Invoice.Table_Name)){
            return modelChange((MInvoice) po, type);
        }

        return null;
    }

    @Override
    public String docValidate(PO po, int timing) {

        if (po.get_TableName().equalsIgnoreCase(I_C_Invoice.Table_Name)){
            return docValidate((MInvoice) po, timing);
        }

        return null;
    }


    /***
     * Validaciones para el modelo de Invoices en compras.
     * Xpande. Created by Gabriel Vila on 8/8/17.
     * @param model
     * @param type
     * @return
     * @throws Exception
     */
    public String modelChange(MInvoice model, int type) throws Exception {

        String mensaje = null, action = "";

        if (type == ModelValidator.TYPE_BEFORE_DELETE) {

            // Orignalmente adempiere no permite borrar cabezales en estado BORRADOR.
            // Lo que hace es permitir anular un borrador o en progreso.
            // Esto genera mucho dato basura y por lo tanto es deseable poder eliminar cabezales.
            // En este validator de invoice se copia funcionlidad que se hace al anular una invoice.
            if (!model.isSOTrx())
            {
                MMatchInv[] mInv = MMatchInv.getInvoice(model.getCtx(), model.get_ID(), model.get_TrxName());
                for (int i = 0; i < mInv.length; i++)
                    mInv[i].delete(true);
                MMatchPO[] mPO = MMatchPO.getInvoice(model.getCtx(), model.get_ID(), model.get_TrxName());
                for (int i = 0; i < mPO.length; i++)
                {
                    if (mPO[i].getM_InOutLine_ID() == 0)
                        mPO[i].delete(true);
                    else
                    {
                        mPO[i].setC_InvoiceLine_ID(null);
                        mPO[i].saveEx();
                    }
                }
            }
        }

        return mensaje;
    }


    /***
     * Validaciones para documentos de la tabla C_Invoice en gestión financiera.
     * Xpande. Created by Gabriel Vila on 8/8/17.
     * @param model
     * @param timing
     * @return
     */
    private String docValidate(MInvoice model, int timing) {

        String message = null, sql = "";

        if (timing == TIMING_BEFORE_COMPLETE){

            // Para comprobantes de venta, valido datos para CFE.
            if (model.isSOTrx()){

                // Valido que la localización de este comprobante de venta tenga departamento, ciudad y dirección.
                MBPartnerLocation partnerLocation = (MBPartnerLocation) model.getC_BPartner_Location();
                MLocation location = (MLocation) partnerLocation.getC_Location();
                if ((location.getAddress1() == null) || (location.getAddress1().trim().equalsIgnoreCase(""))){
                    message = "Es obligatorio indicar Dirección en la Localización del Socio de Negocio de este Comprobante.";
                    return message;
                }
                if ((location.getRegionName() == null) || (location.getRegionName().trim().equalsIgnoreCase(""))){
                    message = "Es obligatorio indicar Departamento en la Localización del Socio de Negocio de este Comprobante.";
                    return message;
                }
                if ((location.getCity() == null) || (location.getCity().trim().equalsIgnoreCase(""))){
                    message = "Es obligatorio indicar Ciudad en la Localización del Socio de Negocio de este Comprobante.";
                    return message;
                }

                // Valido que el socio de negocio de este comprobante tenga número de identificación
                MBPartner partner = (MBPartner) model.getC_BPartner();
                if ((partner.getTaxID() == null) || (partner.getTaxID().trim().equalsIgnoreCase(""))){
                    message = "Es obligatorio indicar Número de Identificación en el Socio de Negocio.";
                    return message;
                }

                // Para notas de credito, valido referencia de facturas (necesarias para CFE)
                MDocType docType = (MDocType) model.getC_DocTypeTarget();
                if (docType.getDocBaseType().equalsIgnoreCase(Doc.DOCTYPE_ARCredit)){
                    // Es obligatorio referenciar al menos una factura
                    sql = " select count(*) from z_invoiceref where c_invoice_id =" + model.get_ID();
                    int contador = DB.getSQLValueEx(model.get_TrxName(), sql);
                    if (contador <= 0){
                        message = "Es obligatorio indicar Facturas afectadas por este comprobante.";
                        return message;
                    }
                }
            }

        }

        return null;
    }

}
