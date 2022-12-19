package org.xpande.comercial.model;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.DB;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 12/19/22.
 */
public class MZProdPurchaseOffer extends X_Z_ProdPurchaseOffer {

    public MZProdPurchaseOffer(Properties ctx, int Z_ProdPurchaseOffer_ID, String trxName) {
        super(ctx, Z_ProdPurchaseOffer_ID, trxName);
    }

    public MZProdPurchaseOffer(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Obtiene y retorna precio de oferta de compra según parametros recibidos. Si no tiene retorna null.
     * Tanane. Created by Gabriel Vila on 2022-12-08
     * @param ctx
     * @param adClientID
     * @param adOrgID
     * @param mProductID
     * @param dateTrx
     * @param trxName
     * @return
     */
    public static BigDecimal getOfferPrice(Properties ctx, int adClientID, int adOrgID, int mProductID, Timestamp dateTrx, String trxName) {

        BigDecimal price = null;
        try {
            String sql = " select max(z_prodpurchaseoffer_id) as z_prodpurchaseoffer_id " +
                    " from z_prodpurchaseoffer " +
                    " where ad_client_id =" + adClientID +
                    " and ad_org_id =" + adOrgID +
                    " and m_product_id =" + mProductID +
                    " and enddate >= '" + dateTrx + "' ";
            int offerID = DB.getSQLValueEx(null, sql);
            if (offerID > 0) {
                MZProdPurchaseOffer purchaseOffer = new MZProdPurchaseOffer(ctx, offerID, null);
                price = purchaseOffer.getPrice();
            }
        }
        catch (Exception e) {
            throw new AdempiereException(e);
        }
        return price;
    }
}
