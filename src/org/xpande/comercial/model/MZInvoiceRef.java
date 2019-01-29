package org.xpande.comercial.model;

import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 * Modelo para referenciar invoices desde una invoice padre.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 8/23/17.
 */
public class MZInvoiceRef extends X_Z_InvoiceRef {

    public MZInvoiceRef(Properties ctx, int Z_InvoiceRef_ID, String trxName) {
        super(ctx, Z_InvoiceRef_ID, trxName);
    }

    public MZInvoiceRef(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }


    /***
     * Retorna lista de invoices referenciadas por una determinada invoice recibida.
     * Xpande. Created by Gabriel Vila on 1/29/19.
     * @param ctx
     * @param cInvoiceID
     * @param trxName
     * @return
     */
    public static List<MZInvoiceRef> getByInvoice(Properties ctx, int cInvoiceID, String trxName){

        String whereClause = X_Z_InvoiceRef.COLUMNNAME_C_Invoice_ID + " =" + cInvoiceID;

        List<MZInvoiceRef> lines = new Query(ctx, I_Z_InvoiceRef.Table_Name, whereClause, trxName).list();

        return lines;
    }

}
