package org.xpande.comercial.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.acct.Doc;
import org.compiere.model.*;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.xpande.comercial.model.MZComercialConfig;
import org.xpande.comercial.utils.ComercialUtils;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;


/**
 * Proceso para generar facturas recibidas en una Recepcion de productos en Retail.
 * Se generan en estado Borrador para su posterior verificación.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 7/4/17.
 */
public class GenerarFacturasRecibidas extends SvrProcess {

    private MInOut mInOut = null;

    @Override
    protected void prepare() {

        this.mInOut = new MInOut(getCtx(), this.getRecord_ID(), get_TrxName());
    }

    @Override
    protected String doIt() throws Exception {

        String message = null;
        boolean tieneConstancia = false;
        String action = "";

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            // Obtengo documento a utilizar para generar facturas del proveedor recibidas
            MDocType[] docTypes = MDocType.getOfDocBaseType(getCtx(), Doc.DOCTYPE_APInvoice);
            MDocType docType = docTypes[0];
            if ((docType == null) || (docType.get_ID() <= 0)){
                return "@Error@ " + "No se pudo obtener Documento de Factura a considerar";
            }

            // Instancio modelos necesarios
            MBPartner bp = (MBPartner)mInOut.getC_BPartner();

            Timestamp fechaHoy = TimeUtil.trunc(new Timestamp(System.currentTimeMillis()), TimeUtil.TRUNC_DAY);

            sql = " select * from z_recepcionprodfact where m_inout_id =" + this.mInOut.get_ID();

        	pstmt = DB.prepareStatement(sql, get_TrxName());
        	rs = pstmt.executeQuery();

        	while(rs.next()){

        	    String manualDocumentNo = rs.getString("ManualDocumentNo").trim();
        	    String serie = rs.getString("DocumentSerie");

        	    if (serie != null) serie = serie.trim();

        	    int cCurrencyID = rs.getInt("C_Currency_ID");

                // Verifico si no existe un comprobante con la misma: serie, numero, tipo de documento y socio de negocio.
                // En caso de existir, no genero de nuevo este comprobante.
                MInvoice invoiceAux = ComercialUtils.getInvoiceByDocPartner(getCtx(), docType.get_ID(), serie, manualDocumentNo, mInOut.getC_BPartner_ID(), get_TrxName());
                if ((invoiceAux != null) && (invoiceAux.get_ID() > 0)){
                    continue;
                }

                if (cCurrencyID <= 0){
                    return "@Error@ " + "Falta indicar Moneda en Factura : " + manualDocumentNo;
                }

                Timestamp dateInvoiced = TimeUtil.trunc(rs.getTimestamp("DateDoc"), TimeUtil.TRUNC_DAY);

                // Seteo cabezal de nueva factura
                MInvoice invoice = new MInvoice(mInOut, dateInvoiced);
                invoice.setDateInvoiced(dateInvoiced);
                invoice.setDateAcct(dateInvoiced);
                invoice.setC_DocTypeTarget_ID(docType.get_ID());
                invoice.setC_DocType_ID(docType.get_ID());
                invoice.set_ValueOfColumn("DocumentSerie", rs.getString("DocumentSerie").trim());
                invoice.setDocumentNo(manualDocumentNo);
                invoice.setC_BPartner_ID(mInOut.getC_BPartner_ID());
                invoice.setC_BPartner_Location_ID(mInOut.getC_BPartner_Location_ID());
                invoice.setC_Currency_ID(cCurrencyID);
                invoice.setAD_Org_ID(mInOut.getAD_Org_ID());
                invoice.set_ValueOfColumn("SubDocBaseType", "RET");

                if (bp.getPaymentRulePO() != null){
                    invoice.setPaymentRule(bp.getPaymentRulePO());
                }
                if (bp.getPO_PaymentTerm_ID() > 0){
                    invoice.setC_PaymentTerm_ID(bp.getPO_PaymentTerm_ID());
                }

                if (cCurrencyID == 142){
                    invoice.setM_PriceList_ID(1000000);
                }
                else if (cCurrencyID == 100){
                    invoice.setM_PriceList_ID(1000371);
                }

                MPriceList priceList = new MPriceList(getCtx(), invoice.getM_PriceList_ID(), null);

                // Seteo impuestos incluidos segun lista de precios
                invoice.setIsTaxIncluded(priceList.isTaxIncluded());

                invoice.saveEx();

                // Dejo asociada la invoice creada con la relación de factura - inout
                action = " update z_recepcionprodfact set c_invoice_id =" + invoice.get_ID() +
                        " where z_recepcionprodfact_id =" + rs.getInt("z_recepcionprodfact_id");
                DB.executeUpdateEx(action, get_TrxName());

                this.setInvoiceLines(invoice, rs.getInt("z_recepcionprodfact_id"));
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
        	rs = null; pstmt = null;
        }

        return "OK";
    }

    private void setInvoiceLines(MInvoice invoice, int zRecepcionProdFactID) {

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            sql = " select * " +
                    " from m_inoutline " +
                    " where m_inout_id =" + this.mInOut.get_ID() +
                    " and z_recepcionprodfact_id =" + zRecepcionProdFactID +
                    " order by m_inoutline_id";

            pstmt = DB.prepareStatement(sql, get_TrxName());
            rs = pstmt.executeQuery();

        	while(rs.next()) {

                MInvoiceLine invLine = new MInvoiceLine(invoice);

                invLine.setC_Invoice_ID(invoice.get_ID());
                invLine.setM_Product_ID(rs.getInt("M_Product_ID"));
                invLine.setC_UOM_ID(rs.getInt("C_UOM_ID"));

                if (rs.getBigDecimal("QtyEnteredInvoice") != null) {
                    invLine.setQtyEntered((BigDecimal) rs.getBigDecimal("QtyEnteredInvoice"));
                    invLine.setQtyInvoiced(invLine.getQtyEntered());
                } else {
                    invLine.setQtyEntered(Env.ZERO);
                    invLine.setQtyInvoiced(Env.ZERO);
                }

                MProduct prod = new MProduct(getCtx(), rs.getInt("m_product_id"), null);

                // Seteos de tasa de impuesto segun condiciones.
                // Si el socio de negocio es literal E, entonces todos sus productos deben ir con la tasa de impuesto para Literal E
                boolean esLiteralE = false;
                MBPartner partner = (MBPartner) invoice.getC_BPartner();
                if (partner.get_ValueAsBoolean("LiteralE")) {
                    esLiteralE = true;
                    // Obtengo ID de tasa de impuesto para Literal E desde coniguración comercial
                    MZComercialConfig comercialConfig = MZComercialConfig.getDefault(getCtx(), get_TrxName());
                    if (comercialConfig.getLiteralE_Tax_ID() > 0) {
                        invLine.setC_Tax_ID(comercialConfig.getLiteralE_Tax_ID());
                    }
                }
                // Si no es Literal E, para invoices compra/venta en Retail, puede suceder que el producto tenga un impuesto especial de compra/venta.
                if (!esLiteralE) {
                    // Impuesto del producto (primero impuesto especial de compra, y si no tiene, entonces el impuesto normal
                    if (prod.get_ValueAsInt("C_TaxCategory_ID_2") > 0) {
                        MTaxCategory taxCat = new MTaxCategory(getCtx(), prod.get_ValueAsInt("C_TaxCategory_ID_2"), null);
                        MTax tax = taxCat.getDefaultTax();
                        if (tax != null) {
                            if (tax.get_ID() > 0) {
                                invLine.setC_Tax_ID(tax.get_ID());
                            }
                        }
                    } else {
                        if (prod.getC_TaxCategory_ID() > 0) {
                            MTaxCategory taxCat = (MTaxCategory) prod.getC_TaxCategory();
                            MTax tax = taxCat.getDefaultTax();
                            if (tax != null) {
                                if (tax.get_ID() > 0) {
                                    invLine.setC_Tax_ID(tax.get_ID());
                                }
                            }
                        }
                    }
                }

                // Precios
                org.xpande.comercial.model.MProductPricing productPricing = this.getProductPricing(invLine, invoice);
                if (productPricing == null) {
                    throw new AdempiereException("No se pudo calcular precios y montos para el producto : " + prod.getValue() + " - " + prod.getName());
                }
                invLine.setPriceActual(productPricing.getPriceStd());
                invLine.setPriceList(productPricing.getPriceList());
                invLine.setPriceLimit(productPricing.getPriceLimit());
                invLine.setPriceEntered(invLine.getPriceActual());

                if (productPricing.isCostoHistorico()) {
                    invLine.set_ValueOfColumn("PricePO", productPricing.getPricePO());
                    invLine.set_ValueOfColumn("PricePONoDto", productPricing.getPricePO());
                } else {
                    invLine.set_ValueOfColumn("PricePO", invLine.getPriceEntered());
                    invLine.set_ValueOfColumn("PricePONoDto", invLine.getPriceEntered());
                }

                invLine.setLineNetAmt();
                invLine.setM_InOutLine_ID(rs.getInt("m_inoutline_id"));

                invLine.saveEx();
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
        	rs = null; pstmt = null;
        }
    }


    /**
     * 	Get and calculate Product Pricing
     *	@param invoiceLine
     *  @param invoice
     *	@return product pricing
     */
    private org.xpande.comercial.model.MProductPricing getProductPricing (MInvoiceLine invoiceLine, MInvoice invoice)
    {
        org.xpande.comercial.model.MProductPricing productPricing = null;

        try{
            productPricing = new org.xpande.comercial.model.MProductPricing (invoiceLine.getM_Product_ID(), invoice.getC_BPartner_ID(), invoice.getAD_Org_ID(),
                    invoice.getDateInvoiced(), invoiceLine.getQtyEntered(), false, get_TrxName());
            productPricing.setM_PriceList_ID(invoice.getM_PriceList_ID());
            productPricing.setPriceDate(invoice.getDateInvoiced());

            productPricing.calculatePrice();

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        return productPricing;
    }

}
