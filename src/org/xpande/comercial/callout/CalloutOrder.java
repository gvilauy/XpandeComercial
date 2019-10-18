package org.xpande.comercial.callout;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MWarehouse;
import org.compiere.util.Env;
import org.xpande.core.model.MZProductoUPC;

import java.util.Properties;

/**
 * Callouts para ordenes de venta y compra en el módulo comercial.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/17/19.
 */
public class CalloutOrder extends CalloutEngine {

    /***
     * Al ingresar organización en una orden de venta, se setea un almacen asociado a dicha organizacion.
     * Xpande. Created by Gabriel Vila on 10/17/19.
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     * @return
     */
    public String setWarehouseByOrg(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {

        if (isCalloutActive()) return "";

        if (value == null){
            mTab.setValue("M_Warehouse_ID", null);
            return "";
        }

        int adOrgID = (Integer) value;
        if (adOrgID <= 0){
            mTab.setValue("M_Warehouse_ID", null);
            return "";
        }

        // Busco almacen asociada a la organización de esta orden
        MWarehouse[] warehouseList = MWarehouse.getForOrg(ctx, adOrgID);
        if (warehouseList.length > 0){
            mTab.setValue("M_Warehouse_ID", warehouseList[0].get_ID());
        }
        else {
            mTab.setValue("M_Warehouse_ID", null);
        }

        return "";
    }

}
