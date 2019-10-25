package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/24/19.
 */
public class MZPautaComVtaDtos extends X_Z_PautaComVtaDtos {

    public MZPautaComVtaDtos(Properties ctx, int Z_PautaComVtaDtos_ID, String trxName) {
        super(ctx, Z_PautaComVtaDtos_ID, trxName);
    }

    public MZPautaComVtaDtos(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
