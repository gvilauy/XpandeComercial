package org.xpande.comercial.utils;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.MInvoice;
import org.compiere.model.Query;
import org.compiere.model.X_C_Invoice;
import org.compiere.util.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Clase de métodos staticos referidos a funcionalidades comerciales del core.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 5/28/18.
 */
public final class ComercialUtils {

    /***
     * Verifica si una determinada invoice se encuentra referenciada en la tabla z_invoiceref.
     * Xpande. Created by Gabriel Vila on 5/28/18.
     * @param ctx
     * @param cInvoiceID
     * @param trxName
     * @return
     */
    public static boolean isInvoiceReferenced(Properties ctx, int cInvoiceID, String trxName){

        boolean value = false;

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            sql = " select z.c_invoice_id " +
                    "from z_invoiceref z " +
                    "inner join c_invoice inv on z.c_invoice_id = inv.c_invoice_id " +
                    "where z.c_invoice_to_id =" + cInvoiceID +
                    "and inv.docstatus='CO'";

            pstmt = DB.prepareStatement(sql, trxName);
            rs = pstmt.executeQuery();

            if (rs.next()){
                value = true;
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
            rs = null; pstmt = null;
        }

        return value;
    }


    /***
     * Metodo que obtiene y retorna una invoice según tipo de documento, número y socio de negocio recibidos.
     * Xpande. Created by Gabriel Vila on 8/8/18.
     * @param ctx
     * @param cDocTypeID
     * @param documentNo
     * @param cBPartnerID
     * @param trxName
     * @return
     */
    public static MInvoice getInvoiceByDocPartner(Properties ctx, int cDocTypeID, String documentNo, int cBPartnerID, String trxName){

        String whereClause = X_C_Invoice.COLUMNNAME_C_DocTypeTarget_ID + " =" + cDocTypeID +
                " AND " + X_C_Invoice.COLUMNNAME_DocumentNo + " ='" + documentNo + "' " +
                " AND " + X_C_Invoice.COLUMNNAME_C_BPartner_ID + " =" + cBPartnerID;

        MInvoice model = new Query(ctx, I_C_Invoice.Table_Name, whereClause, trxName).first();

        return model;
    }


    /***
     * Metodo que obtiene invoices que esten asociadas a un determinado inout.
     * Xpande. Created by Gabriel Vila on 10/9/18.
     * @param ctx
     * @param mInOutID
     * @param onlyCompleted
     * @param trxName
     * @return
     */
    public static List<MInvoice> getInvoicesByInOut(Properties ctx, int mInOutID, boolean onlyCompleted, String trxName){

        List<MInvoice> invoiceList = new ArrayList<>();

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            String whereStatus = "";
            if (onlyCompleted){
                whereStatus = " and inv.docstatus in ('CO','CL')";
            }

            sql = " select distinct il.c_invoice_id " +
                    "from c_invoiceline il " +
                    "inner join c_invoice inv on il.c_invoice_id = inv.c_invoice_id " +
                    "where c_invoiceline_id in  " +
                    " (select c_invoiceline_id from m_matchinv where m_inoutline_id in  " +
                    " (select m_inoutline_id from m_inoutline where m_inout_id =" + mInOutID + ")) " +
                    whereStatus;

        	pstmt = DB.prepareStatement(sql, trxName);
        	rs = pstmt.executeQuery();

        	while(rs.next()){
                MInvoice invoice = new MInvoice(ctx, rs.getInt("c_invoice_id"), trxName);
                invoiceList.add(invoice);
        	}
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
        	rs = null; pstmt = null;
        }

        return invoiceList;
    }

}
