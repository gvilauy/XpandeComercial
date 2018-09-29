package org.xpande.comercial.model;

import org.compiere.acct.Doc;
import org.compiere.model.*;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.xpande.comercial.utils.AcctUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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
        engine.addModelChange(I_C_InvoiceLine.Table_Name, this);

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
            else{
                // Al eliminar una invoice de venta me aseguro que las lineas inout asociadas a las lineas de esta invoice, queden marcadas
                // como no facturadas.
                action = " update m_inoutline set isinvoiced='N' where m_inoutline_id in " +
                        " (select m_inoutline_id from c_invoiceline where c_invoice_id =" + model.get_ID() + ") ";
                DB.executeUpdateEx(action, model.get_TrxName());
            }
        }

        if ((type == ModelValidator.TYPE_BEFORE_NEW) || (type == ModelValidator.TYPE_BEFORE_CHANGE)){


            if ((type == ModelValidator.TYPE_BEFORE_NEW) || (model.is_ValueChanged(X_C_Invoice.COLUMNNAME_C_DocTypeTarget_ID))){

                MDocType docType = (MDocType) model.getC_DocTypeTarget();

                // Me aseguro de dejar bien seteado el valor de DocBaseType en la Invoice
                model.set_ValueOfColumn("DocBaseType", docType.getDocBaseType());

                // Cuando se trata de notas de credito, el termino de pago es siempre AL DIA
                // Este termino de pago AL DIA se toma de la configuracion comercial
                if ((docType.getDocBaseType().equalsIgnoreCase(Doc.DOCTYPE_APCredit)) || (docType.getDocBaseType().equalsIgnoreCase(Doc.DOCTYPE_ARCredit))){

                    MZComercialConfig comercialConfig = MZComercialConfig.getDefault(model.getCtx(), model.get_TrxName());
                    if (comercialConfig.getC_PaymentTerm_ID() <= 0){
                        return "Falta configuracion comercial: no se indica Termino de Pago AL DIA";
                    }
                    model.setC_PaymentTerm_ID(comercialConfig.getC_PaymentTerm_ID());
                    model.set_ValueOfColumn("VencimientoManual", false);
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

        MDocType docType = (MDocType) model.getC_DocTypeTarget();

        if (timing == TIMING_BEFORE_COMPLETE){

            // Valido fecha de comprobante menor o igual al día de hoy
            Timestamp fechaHoy = TimeUtil.trunc(new Timestamp(System.currentTimeMillis()), TimeUtil.TRUNC_DAY);
            if (model.getDateInvoiced().after(fechaHoy)){
                message = "Fecha de comprobante incorrecta. Debe ser menor o igual a la fecha de hoy.";
                return message;
            }

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
                if (docType.getDocBaseType().equalsIgnoreCase(Doc.DOCTYPE_ARCredit)){
                    // Es obligatorio referenciar al menos una factura
                    sql = " select count(*) from z_invoiceref where c_invoice_id =" + model.get_ID();
                    int contador = DB.getSQLValueEx(model.get_TrxName(), sql);
                    if (contador <= 0){
                        message = "Es obligatorio indicar Facturas afectadas por este comprobante.";
                        return message;
                    }

                    // No puedo seleccionar productos en esta Nota de Credito, que no pertenezcan a las facturas refereciadas por esta NC.
                    sql = " select count(l.*) from c_invoiceline l " +
                            " where l.c_invoice_id =" + model.get_ID() +
                            " and l.m_product_id is not null " +
                            " and l.m_product_id not in (select m_product_id from z_invoiceref where c_invoice_id =" + model.get_ID() + ") ";
                    contador = DB.getSQLValueEx(model.get_TrxName(), sql);
                    if (contador > 0){
                        message = "Este comprobante tiene productos que no figuran en las Facturas afectadas.";
                        return message;
                    }
                }
            }

            // Instancio configurador comercial
            MZComercialConfig comercialConfig = MZComercialConfig.getDefault(model.getCtx(), model.get_TrxName());

            // Para comprobantes de compra y venta, valido ingreso manual de vencimientos cuando el termino de pago asi lo requiere.
            if (model.get_ValueAsBoolean("VencimientoManual")){
                message = comercialConfig.validateInvoiceVencimientoManual(model);
                if (message != null){
                    return message;
                }
            }

            model.setIsPayScheduleValid(true);

            // Contabilidad. Seteo cargo contable para el redondeo en caso de no tener ninguno asociado en este momento.
            if (model.getC_Charge_ID() <= 0){

                int chargeRoundingID = AcctUtils.getIDCargoRedondeo(model.getCtx(), null);

                if (chargeRoundingID <= 0){
                    message = "Falta parametrizar Cargo por Redondeo en Configuración Contable.";
                    return message;
                }
                model.setC_Charge_ID(chargeRoundingID);
            }
            // Contabilidad. Seteo monto de cargo contable de redondeo según monto digitado por este concepto.
            BigDecimal amtRounding = (BigDecimal) model.get_Value("AmtRounding");
            if (amtRounding == null) amtRounding = Env.ZERO;
            model.setChargeAmt(amtRounding);

        }
        else if (timing == TIMING_AFTER_COMPLETE){

            // Si tengo impuestos manuales ingresados en este documento, debo llevarlos a la tabla c_invoicetax para poder tener la suma de
            // impuestos acorde con el trunk.
            // Para ellos simplemente copio impuestos manuales en esta tabla.
            List<MZInvoiceTaxManual> invoiceTaxManuals = MZInvoiceTaxManual.getManualTaxes(model.getCtx(), model.get_ID(), model.get_TrxName());
            for (MZInvoiceTaxManual invoiceTaxManual: invoiceTaxManuals){
                MInvoiceTax invoiceTax = new MInvoiceTax(model.getCtx(), 0, model.get_TrxName());
                invoiceTax.setC_Invoice_ID(model.get_ID());
                invoiceTax.setC_Tax_ID(invoiceTaxManual.getC_Tax_ID());
                invoiceTax.setTaxAmt(invoiceTaxManual.getTaxAmt());
                invoiceTax.set_ValueOfColumn("IsManual", true);
                invoiceTax.saveEx();
            }

        }
        else if (timing == TIMING_BEFORE_REACTIVATE){
            // Cuando reactivo un documento, me aseguro de eliminar de la tabla c_invoicetax, aquellos impuestos manuales.
            String action = " delete from c_invoicetax where c_invoice_id =" + model.get_ID() + " and ismanual ='Y'";
            DB.executeUpdateEx(action, model.get_TrxName());
        }

        return null;
    }

    /***
     * Validaciones para el modelo de Lineas de Invoices.
     * Xpande. Created by Gabriel Vila on 6/30/17.
     * @param model
     * @param type
     * @return
     * @throws Exception
     */
    public String modelChange(MInvoiceLine model, int type) throws Exception {

        String mensaje = null;

        if (type == ModelValidator.TYPE_AFTER_DELETE){

            // Al eliminar linea de factura, me aseguro que si hay linea de inout referenciando, la misma quede marcada como no facturada.
            if (model.getM_InOutLine_ID() > 0){
                MInOutLine inOutLine = (MInOutLine) model.getM_InOutLine();
                if ((inOutLine != null) && (inOutLine.get_ID() > 0)){
                    inOutLine.setIsInvoiced(false);
                    inOutLine.saveEx();
                }
            }

        }


        return mensaje;
    }

}
