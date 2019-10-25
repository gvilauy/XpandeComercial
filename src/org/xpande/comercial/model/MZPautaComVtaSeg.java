package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/24/19.
 */
public class MZPautaComVtaSeg extends X_Z_PautaComVtaSeg {

    public MZPautaComVtaSeg(Properties ctx, int Z_PautaComVtaSeg_ID, String trxName) {
        super(ctx, Z_PautaComVtaSeg_ID, trxName);
    }

    public MZPautaComVtaSeg(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
