package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/24/19.
 */
public class MZPautaComVtaProd extends X_Z_PautaComVtaProd {

    public MZPautaComVtaProd(Properties ctx, int Z_PautaComVtaProd_ID, String trxName) {
        super(ctx, Z_PautaComVtaProd_ID, trxName);
    }

    public MZPautaComVtaProd(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
