package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 6/2/21.
 */
public class MZSaleOrderAuthBP extends X_Z_SaleOrderAuthBP{

    public MZSaleOrderAuthBP(Properties ctx, int Z_SaleOrderAuthBP_ID, String trxName) {
        super(ctx, Z_SaleOrderAuthBP_ID, trxName);
    }

    public MZSaleOrderAuthBP(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
