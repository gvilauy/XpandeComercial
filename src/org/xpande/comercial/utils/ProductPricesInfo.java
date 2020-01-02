package org.xpande.comercial.utils;

import org.compiere.util.Env;

import java.math.BigDecimal;

/**
 * Clase para manejo de información de precios de un producto.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 1/1/20.
 */
public class ProductPricesInfo {

    private int M_Product_ID = 0;
    private int C_BPartner_ID = 0;

    private BigDecimal PriceList = Env.ZERO;
    private BigDecimal PriceFinal = Env.ZERO;
    private int PrecisionDecimal = 2;

    private BigDecimal flatDiscount = Env.ZERO;
    private BigDecimal totalManualDiscount = Env.ZERO;
    private BigDecimal totalPautaDiscounts = Env.ZERO;
    private BigDecimal totalDiscounts = Env.ZERO;

    private boolean CascadeDiscounts = false;

    public int getM_Product_ID() {
        return M_Product_ID;
    }

    public void setM_Product_ID(int m_Product_ID) {
        M_Product_ID = m_Product_ID;
    }

    public int getC_BPartner_ID() {
        return C_BPartner_ID;
    }

    public void setC_BPartner_ID(int c_BPartner_ID) {
        C_BPartner_ID = c_BPartner_ID;
    }

    public BigDecimal getPriceList() {
        return PriceList;
    }

    public void setPriceList(BigDecimal priceList) {
        PriceList = priceList;
    }

    public BigDecimal getPriceFinal() {
        return PriceFinal;
    }

    public void setPriceFinal(BigDecimal priceFinal) {
        PriceFinal = priceFinal;
    }

    public int getPrecisionDecimal() {
        return PrecisionDecimal;
    }

    public void setPrecisionDecimal(int precisionDecimal) {
        PrecisionDecimal = precisionDecimal;
    }


    public boolean isCascadeDiscounts() {
        return CascadeDiscounts;
    }

    public void setCascadeDiscounts(boolean cascadeDiscounts) {
        CascadeDiscounts = cascadeDiscounts;
    }

    public BigDecimal getFlatDiscount() {
        return flatDiscount;
    }

    public void setFlatDiscount(BigDecimal flatDiscount) {
        this.flatDiscount = flatDiscount;
    }

    public BigDecimal getTotalManualDiscount() {
        return totalManualDiscount;
    }

    public void setTotalManualDiscount(BigDecimal totalManualDiscount) {
        this.totalManualDiscount = totalManualDiscount;
    }

    public BigDecimal getTotalPautaDiscounts() {
        return totalPautaDiscounts;
    }

    public void setTotalPautaDiscounts(BigDecimal totalPautaDiscounts) {
        this.totalPautaDiscounts = totalPautaDiscounts;
    }

    public BigDecimal getTotalDiscounts() {
        return totalDiscounts;
    }

    public void setTotalDiscounts(BigDecimal totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }
}
