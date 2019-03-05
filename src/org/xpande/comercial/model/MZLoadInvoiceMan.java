package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para carga manual de comprobantes comerciales.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 3/4/19.
 */
public class MZLoadInvoiceMan extends X_Z_LoadInvoiceMan {

    public MZLoadInvoiceMan(Properties ctx, int Z_LoadInvoiceMan_ID, String trxName) {
        super(ctx, Z_LoadInvoiceMan_ID, trxName);
    }

    public MZLoadInvoiceMan(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
