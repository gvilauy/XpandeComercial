package org.xpande.comercial.model;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.acct.Doc;
import org.compiere.model.*;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.eevolution.model.X_C_TaxGroup;
import org.xpande.cfe.model.MZCFEConfig;
import org.xpande.cfe.model.MZCFEConfigDocSend;
import org.xpande.comercial.utils.AcctUtils;
import org.xpande.comercial.utils.ComercialUtils;
import org.xpande.core.model.MZProductoUPC;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

/**
 * Validador de modelos para el modulo Comercial.
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
        engine.addModelChange(I_M_InOutLine.Table_Name, this);
        engine.addModelChange(I_C_OrderLine.Table_Name, this);
        engine.addModelChange(I_M_InOut.Table_Name, this);

        // Document Validations
        engine.addDocValidate(I_C_Invoice.Table_Name, this);
        engine.addDocValidate(I_M_InOut.Table_Name, this);
        engine.addDocValidate(I_C_Order.Table_Name, this);
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
        else if (po.get_TableName().equalsIgnoreCase(I_C_InvoiceLine.Table_Name)){
            return modelChange((MInvoiceLine) po, type);
        }
        else if (po.get_TableName().equalsIgnoreCase(I_M_InOutLine.Table_Name)){
            return modelChange((MInOutLine) po, type);
        }
        else if (po.get_TableName().equalsIgnoreCase(I_C_OrderLine.Table_Name)){
            return modelChange((MOrderLine) po, type);
        }
        else if (po.get_TableName().equalsIgnoreCase(I_M_InOut.Table_Name)){
            return modelChange((MInOut) po, type);
        }

        return null;
    }

    @Override
    public String docValidate(PO po, int timing) {

        if (po.get_TableName().equalsIgnoreCase(I_C_Invoice.Table_Name)){
            return docValidate((MInvoice) po, timing);
        }
        else if (po.get_TableName().equalsIgnoreCase(I_C_Order.Table_Name)){
            return docValidate((MOrder) po, timing);
        }

        return null;
    }


    /***
     * Validaciones para el modelo de Invoices.
     * Xpande. Created by Gabriel Vila on 8/8/17.
     * @param model
     * @param type
     * @return
     * @throws Exception
     */
    public String modelChange(MInvoice model, int type) throws Exception {

        String action;
        String sql;

        if (type == ModelValidator.TYPE_BEFORE_DELETE) {

            // Orignalmente adempiere no permite borrar cabezales en estado BORRADOR.
            // Lo que hace es permitir anular un borrador o en progreso.
            // Esto genera mucho dato basura y por lo tanto es deseable poder eliminar cabezales.
            // En este validator de invoice se copia funcionlidad que se hace al anular una invoice.
            if (!model.isSOTrx())
            {
                // Me aseguro eliminar lineas en cashline asociados a esta invoice
                action = " delete from c_cashline where c_invoice_id =" + model.get_ID();
                DB.executeUpdateEx(action, model.get_TrxName());

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

        else if ((type == ModelValidator.TYPE_BEFORE_NEW) || (type == ModelValidator.TYPE_BEFORE_CHANGE)){

            // Para cualquier comprobante de la tabla invoice, me aseguro de que se indique Organización distinta de *.
            if (model.getAD_Org_ID() <= 0){
                return "Debe indicar Organización para este Documento";
            }

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
        else if ((type == ModelValidator.TYPE_AFTER_NEW) || (type == ModelValidator.TYPE_AFTER_CHANGE)){

            // Debo considerar la posibilidad de que el usuario haya ingresado de manera manual un monto de Redondeo para el comprobante.
            // Si es asi, debo reflejarlo en el total del comprobante.
            if ((model.is_ValueChanged("AmtRounding") || (model.is_ValueChanged("AmtSubtotal"))
                    || (model.is_ValueChanged("Grandtotal")))){

                // Obtengo suma de impuestos manuales con el comportamiento de SUMAR AL SUBTOTAL
                sql = " select coalesce(sum(a.taxamt), 0) as total " +
                        "from z_invoicetaxmanual a " +
                        "inner join c_tax t on a.c_tax_id = t.c_tax_id " +
                        "where c_invoice_id =" + model.get_ID() +
                        "and ManualTaxAction = 'SUMAR' ";

                BigDecimal sumar = DB.getSQLValueBDEx(model.get_TrxName(), sql);
                if (sumar == null) sumar = Env.ZERO;

                // Obtengo suma de impuestos manuales con el comportamiento de RESTAR AL SUBTOTAL
                sql = " select coalesce(sum(a.taxamt), 0) as total " +
                        "from z_invoicetaxmanual a " +
                        "inner join c_tax t on a.c_tax_id = t.c_tax_id " +
                        "where c_invoice_id =" + model.get_ID() +
                        "and ManualTaxAction = 'RESTAR' ";

                BigDecimal restar = DB.getSQLValueBDEx(model.get_TrxName(), sql);
                if (restar == null) restar = Env.ZERO;

                BigDecimal importeTaxManuales = sumar.subtract(restar);

                // Actualizo total de la invoice para considerar impuestos manuales y redondeo
                BigDecimal amtRounding = (BigDecimal) model.get_Value("AmtRounding");
                if (amtRounding == null) amtRounding = Env.ZERO;

                BigDecimal amtSubtotal = (BigDecimal) model.get_Value("AmtSubtotal");
                if (amtSubtotal == null) amtSubtotal = Env.ZERO;

                BigDecimal taxAmt = (BigDecimal) model.get_Value("TaxAmt");
                if (taxAmt == null){
                    sql = " select sum(coalesce(taxamt,0)) as taxamt from c_invoicetax where c_invoice_id =" + model.get_ID();
                    taxAmt = DB.getSQLValueBDEx(model.get_TrxName(), sql);
                }
                if (taxAmt == null) taxAmt = Env.ZERO;

                BigDecimal grandTotal = amtSubtotal.add(taxAmt).add(importeTaxManuales).add(amtRounding);

                action = " update c_invoice set grandtotal =" +  grandTotal +
                         " where c_invoice_id =" + model.get_ID();

                DB.executeUpdateEx(action, model.get_TrxName());
            }

            // Guardo RUT en el comprobante si esta en null
            if (model.get_ValueAsString("TaxID") == null){
                if (model.getC_BPartner_ID() > 0){

                    MBPartner partner = (MBPartner) model.getC_BPartner();
                    if (partner.getTaxID() != null){
                        action = " update c_invoice set taxid =" + partner.getTaxID() +
                                " where c_invoice_id =" + model.get_ID();
                        DB.executeUpdateEx(action, model.get_TrxName());
                    }
                }
            }

            // Me aseguro que si el medio de pago no es efectivo, el campo caja destino quede en blanco
            if ((model.getPaymentRule() != null) && (!model.getPaymentRule().equalsIgnoreCase(X_C_Invoice.PAYMENTRULE_Cash))){
                if (model.get_ValueAsInt("C_CashBook_ID") > 0){
                    action = " update c_invoice set C_CashBook_ID = null " +
                            " where c_invoice_id =" + model.get_ID();
                    DB.executeUpdateEx(action, model.get_TrxName());
                }
            }

            // Me aseguro que si no esta marcada la opcion de Transferencia de Saldo, el campo de socio de negocio relacionado quede en blanco
            if (!model.get_ValueAsBoolean("TransferSaldo")){
                if (model.get_ValueAsInt("C_BPartnerRelation_ID") > 0){
                    action = " update c_invoice set C_BPartnerRelation_ID = null " +
                            " where c_invoice_id =" + model.get_ID();
                    DB.executeUpdateEx(action, model.get_TrxName());
                }
            }
        }

        return null;
    }

    /***
     * Validaciones para el modelo de InOut en modulo comercial.
     * Xpande. Created by Gabriel Vila on 9/29/18.
     * @param model
     * @param type
     * @return
     * @throws Exception
     */
    public String modelChange(MInOut model, int type) throws Exception {

        String mensaje = null, action = "";

        if ((type == ModelValidator.TYPE_BEFORE_NEW) || (type == ModelValidator.TYPE_BEFORE_CHANGE)){

            // Para cualquier comprobante de la tabla inout, me aseguro de que se indique Organización distinta de *.
            if (model.getAD_Org_ID() <= 0){
                return "Debe indicar Organización para este Documento";
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
        MZComercialConfig comercialConfig = MZComercialConfig.getDefault(model.getCtx(), model.get_TrxName());

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
                MBPartner partner = (MBPartner) model.getC_BPartner();
                MBPartnerLocation partnerLocation = (MBPartnerLocation) model.getC_BPartner_Location();
                MLocation location = (MLocation) partnerLocation.getC_Location();
                if ((location.getAddress1() == null) || (location.getAddress1().trim().equalsIgnoreCase(""))){
                    message = "Es obligatorio indicar Dirección en la Localización del Socio de Negocio de este Comprobante " +
                            "(código : " + partner.getValue() + ")";
                    return message;
                }
                if ((location.getRegionName() == null) || (location.getRegionName().trim().equalsIgnoreCase(""))){
                    message = "Es obligatorio indicar Departamento en la Localización del Socio de Negocio de este Comprobante " +
                            "(código : " + partner.getValue() + ")";
                    return message;
                }
                if ((location.getCity() == null) || (location.getCity().trim().equalsIgnoreCase(""))){
                    message = "Es obligatorio indicar Ciudad en la Localización del Socio de Negocio de este Comprobante " +
                            "(código : " + partner.getValue() + ")";
                    return message;
                }

                // Valido que el socio de negocio de este comprobante tenga número de identificación según el Tipo de Identificación

                X_C_TaxGroup taxGroup = (X_C_TaxGroup) partner.getC_TaxGroup();
                if (taxGroup.getValue() != null){
                    if (!taxGroup.getValue().equalsIgnoreCase("OTRO")){
                        if ((partner.getTaxID() == null) || (partner.getTaxID().trim().equalsIgnoreCase(""))){
                            message = "Es obligatorio indicar Número de Identificación en el Socio de Negocio " +
                                    "(código : " + partner.getValue() + ")";
                            return message;
                        }
                    }
                }

                // Para notas de credito, valido referencia de facturas (necesarias para CFE)
                if (docType.getDocBaseType().equalsIgnoreCase(Doc.DOCTYPE_ARCredit)){
                    // Es obligatorio referenciar al menos una factura
                    String referenciaCFE = model.get_ValueAsString("ReferenciaCFE");
                    if ((referenciaCFE == null) || (referenciaCFE.trim().equalsIgnoreCase(""))){
                        sql = " select count(*) from c_invoiceline where c_invoice_id =" + model.get_ID() +
                                " AND Ref_InvoiceLine_ID > 0 ";
                        int contador = DB.getSQLValueEx(model.get_TrxName(), sql);
                        if (contador <= 0){
                            message = "Es obligatorio indicar Facturas afectadas por este comprobante: " + model.getDocumentNo();
                            return message;
                        }
                    }
                }

                // Para comprobantes de venta del tipo ARI, seteo monto total de descuentos (en caso que hubiera).
                if (docType.getDocBaseType().equalsIgnoreCase(Doc.DOCTYPE_ARInvoice)){
                    sql = " select round(sum(pricelist * qtyentered) - sum(priceentered * qtyentered),2) as discountamt from c_invoiceline where c_invoice_id =" + model.get_ID();
                    BigDecimal discountAmt = DB.getSQLValueBDEx(model.get_TrxName(), sql);
                    if (discountAmt == null) discountAmt = Env.ZERO;
                    model.set_ValueOfColumn("DiscountAmt", discountAmt);
                }
            }

            // Para comprobantes de compra y venta, valido ingreso manual de vencimientos cuando el termino de pago asi lo requiere.
            if (model.get_ValueAsBoolean("VencimientoManual")){
                message = comercialConfig.validateInvoiceVencimientoManual(model);
                if (message != null){
                    return message;
                }
            }

            model.setIsPayScheduleValid(true);

            // Contabilidad. Seteo cargo contable para el redondeo en caso de no tener ninguno asociado en este momento.
            MClient client = new MClient(model.getCtx(), model.getAD_Client_ID(), null);
            if (client.isClientAccounting()){
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

        }
        else if (timing == TIMING_AFTER_COMPLETE){

            // Si tengo impuestos manuales ingresados en este documento, debo llevarlos a la tabla c_invoicetax para poder tener la suma de
            // impuestos acorde con el trunk.
            // Para ellos simplemente copio impuestos manuales en esta tabla.
            List<MZInvoiceTaxManual> invoiceTaxManuals = MZInvoiceTaxManual.getManualTaxes(model.getCtx(), model.get_ID(), model.get_TrxName());
            for (MZInvoiceTaxManual invoiceTaxManual: invoiceTaxManuals){
                MInvoiceTax invoiceTax = new MInvoiceTax(model.getCtx(), 0, model.get_TrxName());
                invoiceTax.setC_Invoice_ID(model.get_ID());
                invoiceTax.setAD_Org_ID(model.getAD_Org_ID());
                invoiceTax.setC_Tax_ID(invoiceTaxManual.getC_Tax_ID());
                invoiceTax.setTaxAmt(invoiceTaxManual.getTaxAmt());
                invoiceTax.set_ValueOfColumn("IsManual", true);
                invoiceTax.saveEx();
            }

            // Si en la configuración comercial se indica que en las ventas se debe generar entrega
            // de manera automática, lo hago ahora.
            if (comercialConfig.isVtaGeneraInOut()){
                message = model.generateInOutFromInvoice(false, true);
                if (message != null){
                    return message;
                }
            }

            // Proceso comprobantes marcados con Asiento Manual Contable
            if (model.get_ValueAsBoolean("AsientoManualInvoice")){

                /*
                // Elimino impuestos y genero los mismos según datos ingresados en la grilla de Asiento Manual.
                message = this.setInvoiceTaxAsientoManual(model);
                if (message != null){
                    return message;
                }
                */
            }


        }
        else if (timing == TIMING_BEFORE_REACTIVATE){

            // Si estoy reactivando un comprobante de venta, me aseguro que el documento no este configurado para CFE.
            if (model.isSOTrx()){

                // Obtengo configuracion de envío de CFE para documento referenciado, si tengo aviso que no se puede reactivar..
                MZCFEConfig cfeConfig = MZCFEConfig.getDefault(model.getCtx(), null);
                if ((cfeConfig != null) && (cfeConfig.get_ID() > 0)){
                    MZCFEConfigDocSend configDocRefSend = cfeConfig.getConfigDocumentoCFE(model.getAD_Org_ID(), model.getC_DocTypeTarget_ID());
                    if ((configDocRefSend != null) && (configDocRefSend.get_ID() > 0)){
                        if (configDocRefSend.isActive()){
                            return "No es posible Reactivar este Documento ya que fue enviado electrónicamente a la DGI.";
                        }
                    }
                }
            }

            // Al reactivar una invoice me aseguro de dejar en nulo el monto original auxiliar.
            BigDecimal amtAuxiliar = (BigDecimal) model.get_Value("AmtAuxiliar");
            if ((amtAuxiliar != null) && (amtAuxiliar.compareTo(Env.ZERO) != 0)){
                model.set_ValueOfColumn("AmtAuxiliar", null);
            }

            // Cuando reactivo un documento, me aseguro de eliminar de la tabla c_invoicetax, aquellos impuestos manuales.
            String action = " delete from c_invoicetax where c_invoice_id =" + model.get_ID() + " and ismanual ='Y'";
            DB.executeUpdateEx(action, model.get_TrxName());

        }

        return null;
    }


    /***
     * Validaciones para acciones de documento de la tabla M_InOut.
     * Xpande. Created by Gabriel Vila on 10/21/18.
     * @param model
     * @param timing
     * @return
     */
    private String docValidate(MInOut model, int timing) {

        String message = null;
        String action = "";

        if (timing == TIMING_BEFORE_COMPLETE){

            // En recepciones de productos
            if (model.getMovementType().equalsIgnoreCase(X_M_InOut.MOVEMENTTYPE_VendorReceipts)){

                // Obtengo y recorro lineas
                MInOutLine[] mInOutLines = model.getLines();
                for (int i = 0; i < mInOutLines.length; i++){
                    MInOutLine mInOutLine = mInOutLines[i];

                    // Asocio posibles nuevos códigos de barra a los productos del socio de negocio
                    if (mInOutLine.get_Value("UPC") != null){
                        String upc = mInOutLine.get_ValueAsString("UPC").toString().trim();
                        if (!upc.equalsIgnoreCase("")){
                            MZProductoUPC pupc = MZProductoUPC.getByUPC(model.getCtx(), upc, model.get_TrxName());
                            if ((pupc == null) || (pupc.get_ID() <= 0)){
                                // Asocio nuevo UPC a este producto
                                pupc = new MZProductoUPC(model.getCtx(), 0, model.get_TrxName());
                                pupc.setUPC(upc);
                                pupc.setM_Product_ID(mInOutLine.getM_Product_ID());
                                pupc.saveEx();
                            }
                            else{
                                if (pupc.getM_Product_ID() != mInOutLine.getM_Product_ID()){
                                    MProduct prod = (MProduct)pupc.getM_Product();
                                    return "El Código de Barras ingresado (" + upc + ") esta asociado a otro Producto : " + prod.getValue() + " - " + prod.getName();
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (timing == TIMING_BEFORE_REACTIVATE){

            // En recepciones de productos
            if (model.getMovementType().equalsIgnoreCase(X_M_InOut.MOVEMENTTYPE_VendorReceipts)){

                // Valido que esta recepcion no tenga facturas asociadas que NO esten completas o cerradas.
                // Obtengo lista de facturas que pueden estar asociadas
                List<MInvoice> invoiceList = ComercialUtils.getInvoicesByInOut(model.getCtx(), model.get_ID(), false, model.get_TrxName());
                if (invoiceList.size() > 0){

                    message = "No se puede reactivar este documento ya que tiene las siguientes facturas NO COMPLETAS y asociadas : ";
                    for (MInvoice invoice: invoiceList){
                        message += invoice.getDocumentNo() + ", ";
                    }
                    message += " debe eliminarlas para poder continuar.";
                    return message;
                }

                // Refreso estado de marca de invoiced en lineas de esta recepcion, esto es para no permitir editar lineas que estan facturadas
                action = " update m_inoutline set isinvoiced ='N', processed='N' where m_inout_id =" + model.get_ID();
                DB.executeUpdateEx(action, model.get_TrxName());

                action = " update m_inoutline set isinvoiced ='Y' where m_inout_id =" + model.get_ID() +
                        " and m_inoutline_id in (select m_inoutline_id from c_invoiceline where c_invoice_id in " +
                        " (select a.c_invoice_id from z_recepcionprodfact a " +
                        " inner join c_invoice b on a.c_invoice_id = b.c_invoice_id " +
                        " where a.m_inout_id =" + model.get_ID() + " and b.docstatus='CO')) ";
                DB.executeUpdateEx(action, model.get_TrxName());
            }
        }

        return null;
    }


    /***
     * Validaciones para acciones de documento de la tabla C_Order.
     * Xpande. Created by Gabriel Vila on 11/15/18.
     * @param model
     * @param timing
     * @return
     */
    private String docValidate(MOrder model, int timing) {

        String message = null, sql = "";

        if (timing == TIMING_AFTER_COMPLETE){

            // Si es una orden de venta
            if (model.isSOTrx()){
                // Si tengo indicado un numero de remito manual
                String nroRemitoManual = model.get_ValueAsString("NroRemito");
                if ((nroRemitoManual != null) && (!nroRemitoManual.trim().equalsIgnoreCase(""))){
                    // Si tengo una documento de Entrega de cliente asociado
                    MInOut inOut = ComercialUtils.getInOutByOrder(model.getCtx(), model.get_ID(), model.get_TrxName());
                    if ((inOut != null) && (inOut.get_ID() > 0)){
                        // Si es una Entrega de cliente, le seteo numero de documento = numero de remito manual ingresado
                        // por el usuario al momento de hacer la orden de venta.
                        if (inOut.getMovementType().equalsIgnoreCase(X_M_InOut.MOVEMENTTYPE_CustomerShipment)){
                            inOut.setDocumentNo(nroRemitoManual);
                            inOut.saveEx();
                        }
                    }
                }
            }
        }
        else if (timing == TIMING_BEFORE_REACTIVATE){

            // Si es una orden de venta
            if (model.isSOTrx()){

            }
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
        String action = "";

        if ((type == ModelValidator.TYPE_AFTER_NEW) || (type == ModelValidator.TYPE_AFTER_CHANGE)
                || (type == ModelValidator.TYPE_AFTER_DELETE)){

            MInvoice invoice = (MInvoice)model.getC_Invoice();

            // Cuando modifico linea de comprobante, me aseguro que se calcule bien el campo del cabezal
            // para subtotal. Esto es porque Adempiere de fábrica, cuando maneja lista de precios con
            // impuestos incluídos, me muestra el total de lineas = grand total en el cabezal del comprobante.
            // Para ello no se modifico el comportamiento original de ADempiere y se mantuvo el campo: TotalLines.
            // Pero se agrego campo nuevo para desplegarse con el calculo requerido.

            BigDecimal grandTotal = invoice.getGrandTotal();
            BigDecimal sumImpuestos = Env.ZERO, amtSubTotal = Env.ZERO;
            BigDecimal amtRounding = Env.ZERO;
            if (amtRounding == null) amtRounding = Env.ZERO;

            if ((grandTotal != null) && (grandTotal.compareTo(Env.ZERO) > 0)){
                // Obtengo suma de impuestos para esta invoice
                String sql = " select sum(coalesce(taxamt,0)) as taxamt from c_invoicetax where c_invoice_id =" + invoice.get_ID();
                sumImpuestos = DB.getSQLValueBDEx(model.get_TrxName(), sql);
                if (sumImpuestos == null){
                    sumImpuestos = Env.ZERO;
                }
                else{
                    sumImpuestos = sumImpuestos.setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                amtSubTotal = grandTotal.subtract(sumImpuestos);

                // Para comprobantes de venta, proceso importe de redondeo automático si así esta parametrizado el sistema.
                // Ademas este redondeo automático solo aplica cuando la moneda del comprobante es la del esquema comtable.
                if (invoice.isSOTrx()){

                    // Si moneda del comprobante es igual a la moneda del esquema contable
                    MAcctSchema acctSchema = MClient.get(model.getCtx(), invoice.getAD_Client_ID()).getAcctSchema();
                    if (acctSchema.getC_Currency_ID() == invoice.getC_Currency_ID()){

                        // Obtengo configuracion comercial
                        MZComercialConfig comercialConfig = MZComercialConfig.getDefault(model.getCtx(), null);
                        if ((comercialConfig == null) || (comercialConfig.get_ID() <= 0)){
                            return "No se pudo obtener información de Configuración Comercial";
                        }
                        // Si se aplica redondeo automatico
                        if (comercialConfig.isRedondeoAutoVta()){
                            // Redondeo = redondeo (Subtotal + impuestos, 0) - (subtotal + impuestos)
                            BigDecimal totalInvoice = amtSubTotal.add(sumImpuestos);
                            BigDecimal totalPrecisionCero = totalInvoice.setScale(0, RoundingMode.HALF_UP);
                            amtRounding = totalPrecisionCero.subtract(totalInvoice);
                        }
                    }
                }
            }
            invoice.set_ValueOfColumn("AmtSubtotal", amtSubTotal);
            invoice.set_ValueOfColumn("TaxAmt", sumImpuestos);
            invoice.set_ValueOfColumn("AmtRounding", amtRounding);
            invoice.saveEx();

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

        }
        else if ((type == ModelValidator.TYPE_BEFORE_NEW) || (type == ModelValidator.TYPE_BEFORE_CHANGE)
                || (type == ModelValidator.TYPE_BEFORE_DELETE)){

            MInvoice invoice = (MInvoice)model.getC_Invoice();
            MDocType docType = (MDocType) invoice.getC_DocTypeTarget();

            // Para comprobantes de compra
            if (!invoice.isSOTrx()){

                if (type != ModelValidator.TYPE_BEFORE_DELETE){
                    // Obtengo configuracion comercial
                    MZComercialConfig comercialConfig = MZComercialConfig.getDefault(model.getCtx(), null);
                    if ((comercialConfig == null) || (comercialConfig.get_ID() <= 0)){
                        return "No se pudo obtener información de Configuración Comercial";
                    }
                    // Si esta linea pertenece a un documento interno
                    if (comercialConfig.getDefaultDocInterno_ID() == docType.get_ID()){
                        // Valido que la tasa de impuesto sea CERO, no se permite otra tasa
                        if (model.getC_Tax_ID() > 0){
                            MTax tax = (MTax) model.getC_Tax();
                            if (tax.getRate() != null){
                                if (tax.getRate().compareTo(Env.ZERO) != 0){
                                    return "No es posible ingresar lineas que muevan impuestos en comprobantes del tipo DOCUMENTO INTERNO";
                                }
                            }
                        }
                    }
                }
            }

            // Me aseguro que esta linea tenga producto o cargo
            if ((model.getM_Product_ID() <= 0) && (model.getC_Charge_ID() <= 0)){
                return "Debe indicar producto o cargo para esta linea.";
            }

            // Me aseguro que esta linea tenga precio de lista
            if ((model.getPriceList() == null) || (model.getPriceList().compareTo(Env.ZERO) == 0)){
                model.setPriceList(model.getPriceActual());
            }

            // Siguiendo el mismo concepto que el cabezal, se actualiza subtotal de esta linea.
            // Nuevo campo de subtotal, no se toca el original de ADempiere.
            BigDecimal lineTotal = model.getLineTotalAmt();
            if (lineTotal != null){
                BigDecimal lineTaxAmt = model.getTaxAmt();
                if (lineTaxAmt != null){
                    model.set_ValueOfColumn("AmtSubtotal", lineTotal.subtract(lineTaxAmt));
                }
                else{
                    model.set_ValueOfColumn("AmtSubtotal", lineTotal);
                }
            }

            if (type == ModelValidator.TYPE_BEFORE_DELETE){

                // Me aseguro de eliminar referencias en tabla M_MatchInv
                action = " delete from m_matchinv where c_invoiceline_id =" + model.get_ID();
                DB.executeUpdateEx(action, model.get_TrxName());
            }

        }

        return null;
    }

    /***
     * Validaciones para el modelo de Lineas de Orders.
     * Xpande. Created by Gabriel Vila on 10/17/19.
     * @param model
     * @param type
     * @return
     * @throws Exception
     */
    public String modelChange(MOrderLine model, int type) throws Exception {

        String action = "";

        if ((type == ModelValidator.TYPE_AFTER_NEW) || (type == ModelValidator.TYPE_AFTER_CHANGE)
                || (type == ModelValidator.TYPE_AFTER_DELETE)){

            MOrder order = (MOrder)model.getC_Order();

            // Cuando modifico linea de orden, me aseguro que se calcule bien el campo del cabezal
            // para subtotal. Esto es porque Adempiere de fábrica, cuando maneja lista de precios con
            // impuestos incluídos, me muestra el total de lineas = grand total en el cabezal del comprobante.
            // Se tiene mostrar subtotal = total - impuestos.
            // Para ello no se modifico el comportamiento original de ADempiere y se mantuvo el campo: TotalLines.
            // Pero se agrego campo nuevo para desplegarse con el calculo requerido.

            BigDecimal grandTotal = order.getGrandTotal();
            if ((grandTotal == null) || (grandTotal.compareTo(Env.ZERO) <= 0)){
                order.set_ValueOfColumn("AmtSubtotal", Env.ZERO);
                order.saveEx();
            }
            else{
                // Obtengo suma de impuestos para esta order
                String sql = " select sum(coalesce(taxamt,0)) as taxamt from c_ordertax where c_order_id =" + order.get_ID();
                BigDecimal sumImpuestos = DB.getSQLValueBDEx(model.get_TrxName(), sql);
                if (sumImpuestos == null){
                    sumImpuestos = Env.ZERO;
                }
                else{
                    sumImpuestos = sumImpuestos.setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                order.set_ValueOfColumn("AmtSubtotal", grandTotal.subtract(sumImpuestos));
                order.saveEx();
            }

        }
        else if ((type == ModelValidator.TYPE_BEFORE_NEW) || (type == ModelValidator.TYPE_BEFORE_CHANGE)){

            // Me aseguro que esta linea tenga producto o cargo
            if ((model.getM_Product_ID() <= 0) && (model.getC_Charge_ID() <= 0)){
                return "Debe indicar producto o cargo para esta linea.";
            }

            MOrder order = (MOrder)model.getC_Order();

            // Para ordenes de venta
            if (order.isSOTrx()){
                // Obtengo multiply rate para convertir desde unidad de medida de esta linea,
                // a la unidad de medida del producto
                if (model.getM_Product_ID() > 0){
                    String sql = " select c_uom_id from m_product where m_product_id =" + model.getM_Product_ID();
                    int cUomProdID = DB.getSQLValueEx(null, sql);
                    BigDecimal multiplyRate = Env.ONE;
                    if (cUomProdID != model.getC_UOM_ID()){
                        multiplyRate = MUOMConversion.getProductRateTo(model.getCtx(), model.getM_Product_ID(), model.getC_UOM_ID());
                        if ((multiplyRate == null) || (multiplyRate.compareTo(Env.ZERO) <= 0)){
                            return "No se pudo obtener el factor de conversión entre unidad de medida del producto, " +
                                    "y la unidad de medida de esta linea.";
                        }
                    }
                    model.set_ValueOfColumn("UomMultiplyRate", multiplyRate.setScale(4, RoundingMode.HALF_UP));
                }
            }
        }

        return null;
    }

    /***
     * Validaciones para el modelo de Lineas de InOut.
     * Xpande. Created by Gabriel Vila on 6/13/19.
     * @param model
     * @param type
     * @return
     * @throws Exception
     */
    public String modelChange(MInOutLine model, int type) throws Exception {

        String mensaje = null;
        String action = "";

        if (type == ModelValidator.TYPE_BEFORE_DELETE){

            // Valido que no se pueda eliminar una linea marcada como facturada
            if (model.isInvoiced()){
                return "No es posible eliminar esta linea ya que esta asociada a un comprobante comercial.";
            }

            // Me aseguro de eliminar referencias en tabla M_CostDetails
            action = " delete from m_costdetail where m_inoutline_id =" + model.get_ID();
            DB.executeUpdateEx(action, model.get_TrxName());
        }


        return mensaje;
    }

    /***
     * Setea impuestos para invoices marcadas con asiento manual contable.
     * Xpande. Created by Gabriel Vila on 5/10/19.
     * @param model
     * @return
     */
    private String setInvoiceTaxAsientoManual(MInvoice model) {

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String message = null;
        String action = "";

        try{

            // Primero elimino impuestos actuales
            // Elimino impuestos de este comprobante, ya que se cargaran a mano.
            action = " delete from c_invoicetax " +
                    "  where c_invoice_id =" + model.get_ID();
            DB.executeUpdateEx(action, model.get_TrxName());

            // Obtengo lineas de asientos manuales que se correspondan a impuestos
            sql = " select c_tax_id, sum(amtacctdr - amtacctcr) as amttax " +
                    " from z_invoiceastomanual " +
                    " where c_invoice_id =" + model.get_ID() +
                    " and c_tax_id > 0 " +
                    " group by c_tax_id ";

            pstmt = DB.prepareStatement(sql, model.get_TrxName());
            rs = pstmt.executeQuery();

            while(rs.next()){

                BigDecimal amtTax = rs.getBigDecimal("amttax");
                if ((amtTax != null) && (amtTax.compareTo(Env.ZERO) != 0)){

                    // Me aseguro signo positivo
                    if (amtTax.compareTo(Env.ZERO) < 0) amtTax = amtTax.negate();

                    // Genero linea para este impuesto
                    MInvoiceTax invoiceTax = new MInvoiceTax(model.getCtx(), 0, model.get_TrxName());
                    invoiceTax.setC_Invoice_ID(model.get_ID());
                    invoiceTax.setAD_Org_ID(model.getAD_Org_ID());
                    invoiceTax.setC_Tax_ID(rs.getInt("c_tax_id"));
                    invoiceTax.setTaxBaseAmt(Env.ZERO);
                    invoiceTax.setTaxAmt(amtTax);
                    invoiceTax.set_ValueOfColumn("IsManual", true);
                    invoiceTax.saveEx();
                }
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
            rs = null; pstmt = null;
        }

        return message;
    }

}
