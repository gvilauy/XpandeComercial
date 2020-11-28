package org.xpande.comercial.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInvoice;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Trx;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Proceso para completar de manera masiva comprobantes de venta, según filros indicados por el usuario.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 11/27/20.
 */
public class CompleteMasivoInvoices extends SvrProcess {

    private int adOrgID = -1;
    private int cDocTypeID = -1;
    private int cCurrencyID = -1;
    private int cBPartnerID = -1;
    private Timestamp startDate = null;
    private Timestamp endDate = null;

    @Override
    protected void prepare() {

        ProcessInfoParameter[] para = getParameter();

        for (int i = 0; i < para.length; i++){

            String name = para[i].getParameterName();

            if (name != null){
                if (name.trim().equalsIgnoreCase("AD_Org_ID")){
                    this.adOrgID = ((BigDecimal)para[i].getParameter()).intValueExact();
                }
                else if (name.trim().equalsIgnoreCase("C_DocType_ID")){
                    if (para[i].getParameter() != null){
                        this.cDocTypeID = ((BigDecimal)para[i].getParameter()).intValueExact();
                    }
                }
                else if (name.trim().equalsIgnoreCase("C_Currency_ID")){
                    if (para[i].getParameter() != null){
                        this.cCurrencyID = ((BigDecimal)para[i].getParameter()).intValueExact();
                    }
                }
                else if (name.trim().equalsIgnoreCase("C_BPartner_ID")){
                    if (para[i].getParameter() != null){
                        this.cBPartnerID = ((BigDecimal)para[i].getParameter()).intValueExact();
                    }
                }
                else if (name.trim().equalsIgnoreCase("DateInvoiced")){
                    this.startDate = (Timestamp)para[i].getParameter();
                    this.endDate = (Timestamp)para[i].getParameter_To();
                }
            }
        }
    }

    @Override
    protected String doIt() throws Exception {

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Trx trans = null;
        int contador = 0;

        try{

            String whereClause = " and h.ad_org_id =" + this.adOrgID + " and h.dateinvoiced between '" + this.startDate + "' and '" + this.endDate + "' ";

            if (this.cDocTypeID > 0){
                whereClause += " and h.c_doctypetarget_id =" + this.cDocTypeID;
            }
            if (this.cBPartnerID > 0){
                whereClause += " and h.c_bpartner_id =" + this.cBPartnerID;
            }
            if (this.cCurrencyID > 0){
                whereClause += " and h.c_currency_id =" + this.cCurrencyID;
            }

            sql = " select c_invoice_id " +
                    " from c_invoice h " +
                    " where h.docstatus in ('DR', 'IP') " +
                    " and h.issotrx='Y' " + whereClause;

        	pstmt = DB.prepareStatement(sql, null);
        	rs = pstmt.executeQuery();

        	while(rs.next()){

                // Genero nueva transaccion
                String trxNameAux = Trx.createTrxName();
                trans = Trx.get(trxNameAux, true);

                MInvoice invoice = new MInvoice(getCtx(), rs.getInt("c_invoice_id"), trxNameAux);

                if (!invoice.processIt(DocAction.ACTION_Complete)) {
                    throw new Exception("No se pudo completar Comprobante Número : " + invoice.getDocumentNo() + "\n" + invoice.getProcessMsg());
                }
                invoice.saveEx(trxNameAux);

                trans.close();

                contador++;
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
        	rs = null; pstmt = null;
        }

        return "Proceso Finalizado. Cantidad de Comprobantes de Venta procesados : " + contador;
    }
}
