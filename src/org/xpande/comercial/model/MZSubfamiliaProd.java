package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 4/10/21.
 */
public class MZSubfamiliaProd extends X_Z_SubfamiliaProd{

    public MZSubfamiliaProd(Properties ctx, int Z_SubfamiliaProd_ID, String trxName) {
        super(ctx, Z_SubfamiliaProd_ID, trxName);
    }

    public MZSubfamiliaProd(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
