package org.xpande.comercial.model;

import java.sql.ResultSet;
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
}
