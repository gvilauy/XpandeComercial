package org.xpande.comercial.model;

import org.compiere.model.MBOMProduct;
import org.compiere.model.MPriceList;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para parametrizacion de listas de precios en parametros generales del modulo comercial.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 9/27/17.
 */
public class MZComercialConfigPList extends X_Z_ComercialConfigPList {

    public MZComercialConfigPList(Properties ctx, int Z_ComercialConfigPList_ID, String trxName) {
        super(ctx, Z_ComercialConfigPList_ID, trxName);
    }

    public MZComercialConfigPList(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean beforeSave(boolean newRecord) {

        // Seteo atributos de la lista de precios de este modelo
        if ((newRecord) || (is_ValueChanged(X_Z_ComercialConfigPList.COLUMNNAME_M_PriceList_ID))){
            MPriceList priceList = (MPriceList) this.getM_PriceList();
            this.setC_Currency_ID(priceList.getC_Currency_ID());
            this.setIsSOPriceList(priceList.isSOPriceList());
        }

        return true;
    }
}
