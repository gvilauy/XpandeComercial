package org.xpande.comercial.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInvoice;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Proceso para generar documentos de InOut según invoices.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 3/29/21.
 */
public class GenerarInOutsFromInvoices extends SvrProcess {

    private int adOrgID = -1;
    private int cDocTypeID = -1;
    private int cBPartnerID = -1;
    private String documentNo = "";
    private Timestamp startDate = null;
    private Timestamp endDate = null;

    @Override
    protected void prepare() {

        ProcessInfoParameter[] para = getParameter();

        for (int i = 0; i < para.length; i++){

            String name = para[i].getParameterName();

            if (name != null){

                if (para[i].getParameter() != null){

                    if (name.trim().equalsIgnoreCase("DocumentNoRef")){
                        this.documentNo = ((String)para[i].getParameter()).trim();
                    }
                    else if (name.trim().equalsIgnoreCase("C_BPartner_ID")){
                        this.cBPartnerID = ((BigDecimal)para[i].getParameter()).intValue();
                    }
                    else if (name.trim().equalsIgnoreCase("AD_Org_ID")){
                        this.adOrgID = ((BigDecimal)para[i].getParameter()).intValue();
                    }
                    else if (name.trim().equalsIgnoreCase("C_DocType_ID")){
                        this.cDocTypeID = ((BigDecimal)para[i].getParameter()).intValue();
                    }
                    else if (name.trim().equalsIgnoreCase("StartDate")){
                        if (para[i].getParameter() != null){
                            this.startDate = (Timestamp)para[i].getParameter();
                        }
                    }
                    else if (name.trim().equalsIgnoreCase("EndDate")){
                        this.endDate = (Timestamp)para[i].getParameter();
                    }
                }
            }
        }
    }

    @Override
    protected String doIt() throws Exception {

        String message;

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            String whereClause ="";

            if (this.adOrgID > 0){
                whereClause += " and a.ad_org_id =" + this.adOrgID;
            }
            if (this.cBPartnerID > 0){
                whereClause += " and a.c_bpartner_id =" + this.cBPartnerID;
            }
            if (this.startDate != null){
                whereClause += " and a.dateinvoiced >='" + this.startDate + "' ";
            }
            if (this.endDate != null){
                whereClause += " and a.dateinvoiced <='" + this.endDate + "' ";
            }
            if (this.cDocTypeID > 0){
                whereClause += " and a.c_doctypetarget_id =" + this.cDocTypeID;
            }
            if (this.documentNo != null){
                if (!this.documentNo.trim().equalsIgnoreCase("")){
                    whereClause += " and a.documentno ='" + this.documentNo.trim() + "' ";
                }
            }

            sql = " select a.c_invoice_id " +
                    " from c_invoice a " +
                    " where docstatus ='CO' " + whereClause;

        	pstmt = DB.prepareStatement(sql, get_TrxName());
        	rs = pstmt.executeQuery();

        	while(rs.next()){

        	    MInvoice invoice = new MInvoice(getCtx(), rs.getInt("c_invoice_id"), get_TrxName());

                message = invoice.generateInOutFromInvoice(false, true);

                if (message != null){
                    return "@Error@ " + message;
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

        return "OK";
    }
}
