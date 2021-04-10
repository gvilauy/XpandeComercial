package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 4/10/21.
 */
public class MZFamiliaProd extends X_Z_FamiliaProd{

    public MZFamiliaProd(Properties ctx, int Z_FamiliaProd_ID, String trxName) {
        super(ctx, Z_FamiliaProd_ID, trxName);
    }

    public MZFamiliaProd(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
