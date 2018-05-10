package org.xpande.comercial.model;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.Query;
import org.compiere.util.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para guardar información de ultima compra por produto.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 5/7/18.
 */
public class MZProductoUltCpra extends X_Z_ProductoUltCpra {

    public MZProductoUltCpra(Properties ctx, int Z_ProductoUltCpra_ID, String trxName) {
        super(ctx, Z_ProductoUltCpra_ID, trxName);
    }

    public MZProductoUltCpra(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }


    /***
     * Obtiene modelo según ID de producto recibido.
     * Xpande. Created by Gabriel Vila on 5/7/18.
     * @param ctx
     * @param mProductID
     * @param trxName
     * @return
     */
    public static MZProductoUltCpra getByProduct(Properties ctx, int mProductID, String trxName){

        String whereClause = X_Z_ProductoUltCpra.COLUMNNAME_M_Product_ID + " =" + mProductID;

        MZProductoUltCpra model = new Query(ctx, I_Z_ProductoUltCpra.Table_Name, whereClause, trxName).first();

        return model;
    }


    /***
     * Actualizo información de este modelo.
     * Xpande. Created by Gabriel Vila on 5/7/18.
     * @param mProductID
     */
    public void updateInfo(int mProductID){

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            sql = " select a.c_invoice_id, a.c_invoiceline_id, a.dateinvoiced, a.c_bpartner_id, bp.name " +
            " from zv_historicocompras a " +
            " inner join c_invoice inv on a.c_invoice_id = inv.c_invoice_id " +
            " inner join c_bpartner bp on a.c_bpartner_id = bp.c_bpartner_id " +
            " where a.m_product_id =" + mProductID +
            " and inv.docbasetype='API' " +
            " order by a.dateinvoiced desc ";

            pstmt = DB.prepareStatement(sql, get_TrxName());
        	rs = pstmt.executeQuery();

        	if (rs.next()){
                this.setC_BPartner_ID(rs.getInt("c_bpartner_id"));
                this.setC_Invoice_ID(rs.getInt("c_invoice_id"));
                this.setC_InvoiceLine_ID(rs.getInt("c_invoiceline_id"));
                this.setDateInvoiced(rs.getTimestamp("dateinvoiced"));
                this.setNombreSocio(rs.getString("name"));
                this.saveEx();
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

}
