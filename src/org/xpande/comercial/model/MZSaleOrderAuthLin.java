package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 6/2/21.
 */
public class MZSaleOrderAuthLin extends X_Z_SaleOrderAuthLin{

    public MZSaleOrderAuthLin(Properties ctx, int Z_SaleOrderAuthLin_ID, String trxName) {
        super(ctx, Z_SaleOrderAuthLin_ID, trxName);
    }

    public MZSaleOrderAuthLin(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
