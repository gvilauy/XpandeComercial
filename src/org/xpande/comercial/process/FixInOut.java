package org.xpande.comercial.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 6/25/21.
 */
public class FixInOut extends SvrProcess {

    @Override
    protected void prepare() {

    }

    @Override
    protected String doIt() throws Exception {

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String message = null;

        try{

            sql = " select m_inout_id from aux_pp";

            pstmt = DB.prepareStatement(sql, get_TrxName());
            rs = pstmt.executeQuery();

            while(rs.next()){

                MInOut inOut = new MInOut(getCtx(), rs.getInt("m_inout_id"), get_TrxName());
                if (!inOut.reActivateIt()){
                    message = "No se pudo reactivar inout: " + inOut.getDocumentNo();
                    if (inOut.getProcessMsg() != null){
                        message += "\n" + inOut.getProcessMsg();
                        throw new AdempiereException(message);
                    }
                }
                inOut.saveEx();
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
            rs = null; pstmt = null;
        }

        return "OK.";

    }
}
