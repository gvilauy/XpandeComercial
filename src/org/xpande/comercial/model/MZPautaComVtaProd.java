package org.xpande.comercial.model;

import org.compiere.model.MProduct;

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

    @Override
    protected boolean beforeSave(boolean newRecord) {

        MProduct product = (MProduct) this.getM_Product();
        if (product.getM_Product_Category_ID() > 0){
            this.setM_Product_Category_ID(product.getM_Product_Category_ID());
        }

        if (product.get_ValueAsInt("Z_FamiliaProd_ID") > 0){
            this.setZ_FamiliaProd_ID (product.get_ValueAsInt("Z_FamiliaProd_ID"));
        }

        if (product.get_ValueAsInt("Z_SubfamiliaProd_ID") > 0){
            this.setZ_SubfamiliaProd_ID(product.get_ValueAsInt("Z_SubfamiliaProd_ID"));
        }

        return super.beforeSave(newRecord);
    }
}
