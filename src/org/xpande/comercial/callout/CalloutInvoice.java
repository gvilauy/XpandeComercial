package org.xpande.comercial.callout;

import org.compiere.model.*;
import org.compiere.util.Env;
import org.xpande.core.model.MZProductoUPC;
import org.xpande.core.utils.AcctUtils;
import org.xpande.core.utils.CurrencyUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Properties;

/**
 * Callouts para Invoices en modulo comercial
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/2/19.
 */
public class CalloutInvoice extends CalloutEngine {

    /***
     * Al ingresar código de barras o el producto directamente se deben setear el otro valores asociado.
     * Xpande. Created by Gabriel Vila on 6/25/17.
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
            else{
                mTab.setValue("M_Product_ID", null);
                mTab.fireDataStatusEEvent ("Error", "No existe Producto con código de barras ingresado", true);
            }
        }
        else if (column.equalsIgnoreCase("M_Product_ID")){
            int mProductID = ((Integer) value).intValue();
            MZProductoUPC pupc = MZProductoUPC.getByProduct(ctx, mProductID, null);
            if ((pupc != null) && (pupc.get_ID() > 0)){
                mTab.setValue("UPC", pupc.getUPC());
            }
            else{
                mTab.setValue("UPC", null);
            }
        }

        return "";
    }

    /***
     * Setea tasa de cambio al modificar la moneda en un comprobante de venta.
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     * @return
     */
    public String setCurrencyRate(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {

        // Por ahora solo para comprobante de venta.
        String isSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx");
        if (isSOTrx != null){
            if (isSOTrx.equalsIgnoreCase("N")){
                return "";
            }
        }

        if (value == null){
            mTab.setValue("CurrencyRate", Env.ZERO);
            return "";
        }

        int adClientID = Env.getContextAsInt(ctx, WindowNo, "AD_Client_ID");
        MAcctSchema schema = MClient.get(ctx, adClientID).getAcctSchema();
        int  currencyID = Env.getContextAsInt(ctx, WindowNo, "C_Currency_ID");
        Timestamp dateRate = Env.getContextAsDate(ctx, WindowNo, "DateInvoiced");

        String column = mField.getColumnName();

        if (column.equalsIgnoreCase("C_Currency_ID")){
            currencyID = (Integer) value;
        }
        else if (column.equalsIgnoreCase("DateInvoiced")){
            dateRate = (Timestamp) value;
        }

        if (currencyID == schema.getC_Currency_ID()){
            mTab.setValue("CurrencyRate", Env.ONE);
            return "";
        }

        BigDecimal curencyRate = CurrencyUtils.getCurrencyRate(ctx, adClientID, 0, schema.getC_Currency_ID(), currencyID, 114, dateRate, null);

        if (curencyRate == null){
            curencyRate = Env.ZERO;
        }
        else {
            mTab.setValue("CurrencyRate", curencyRate);
        }

        return "";
    }

}
