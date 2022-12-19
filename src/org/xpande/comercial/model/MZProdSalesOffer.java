package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 12/19/22.
 */
public class MZProdSalesOffer extends X_Z_ProdSalesOffer {

    public MZProdSalesOffer(Properties ctx, int Z_ProdSalesOffer_ID, String trxName) {
        super(ctx, Z_ProdSalesOffer_ID, trxName);
    }

    public MZProdSalesOffer(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
