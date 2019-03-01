package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo de linea de archivo leída en interface de carga de comprobantes comerciales.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 2/28/19.
 */
public class MZLoadInvoiceFile extends X_Z_LoadInvoiceFile {

    public MZLoadInvoiceFile(Properties ctx, int Z_LoadInvoiceFile_ID, String trxName) {
        super(ctx, Z_LoadInvoiceFile_ID, trxName);
    }

    public MZLoadInvoiceFile(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
