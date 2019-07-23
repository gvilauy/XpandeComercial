package org.xpande.comercial.callout;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Env;
import org.xpande.core.model.MZProductoUPC;


import java.util.Properties;

/**
 * Callouts para entregas/rececpiones/devoluciones en el módulo comercial.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 7/23/19.
 */
public class CalloutInOut extends CalloutEngine {

    /***
     * Al ingresar código de barras o el producto directamente, se deben setear los otros valores asociados.
     * Xpande. Created by Gabriel Vila on 7/23/19.
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     * @return
     */
    public String upcProduct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {

        if (isCalloutActive()) return "";

        if ((value == null) || (value.toString().trim().equalsIgnoreCase(""))){
            mTab.setValue("UPC", null);
            mTab.setValue("VendorProductNo", null);
            mTab.setValue("M_Product_ID", null);
            return "";
        }

        int cBPartnerID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");

        String column = mField.getColumnName();

        if (column.equalsIgnoreCase("UPC")){
            MZProductoUPC pupc = MZProductoUPC.getByUPC(ctx, value.toString().trim(), null);
            if ((pupc != null) && (pupc.get_ID() > 0)){
                mTab.setValue("M_Product_ID", pupc.getM_Product_ID());
            }
        }
        else if (column.equalsIgnoreCase("M_Product_ID")){
            int mProductID = ((Integer) value).intValue();
            if ((mTab.getValue("UPC") == null) || (mTab.getValue("UPC").toString().trim().equalsIgnoreCase(""))){
                MZProductoUPC pupc = MZProductoUPC.getByProduct(ctx, mProductID, null);
                if ((pupc != null) && (pupc.get_ID() > 0)){
                    mTab.setValue("UPC", pupc.getUPC());
                }
            }
            else{
                // El usuario ingreso un UPC y ademas selecciono un producto.
                // Puede pasar que quiera asociar un nuevo UPC al producto seleccionado o que cambió el producto y el UPC es de otro.
                // En el segundo caso, tengo que cargar el UPC correcto del nuevo producto seleccionado.
                String upc = mTab.getValue("UPC").toString().trim();
                MZProductoUPC pupc = MZProductoUPC.getByUPC(ctx, upc, null);
                if ((pupc != null) && (pupc.get_ID() > 0)){
                    if (pupc.getM_Product_ID() != mProductID){
                        MZProductoUPC pupcProd = MZProductoUPC.getByProduct(ctx, mProductID, null);
                        if ((pupcProd != null) && (pupcProd.get_ID() > 0)){
                            mTab.setValue("UPC", pupcProd.getUPC());
                        }
                        else{
                            mTab.setValue("UPC", null);
                        }
                    }
                }
            }
        }

        return "";
    }

}
