package org.xpande.comercial.utils;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.util.Env;
import org.xpande.comercial.model.MZPautaComVta;
import org.xpande.comercial.model.MZPautaComVtaDtos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

/**
 * Clase de métodos staticos referidos a calculo de descuentos comerciales.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 1/2/20.
 */
public final class DiscountUtils {

    /***
     * Obtiene descuentos y setea precio final.
     * @param ctx
     * @param fechaDoc
     * @param adClientID
     * @param adOrgID
     * @param cBpartnerID
     * @param mProductID
     * @param dtoManual
     * @param priceList
     * @param basePrice
     * @param precision
     * @param trxName
     * @return
     */
    public static ProductPricesInfo setDiscountPrices(Properties ctx, Timestamp fechaDoc, int adClientID, int adOrgID, int cBpartnerID, int mProductID,
                                                      BigDecimal dtoManual, BigDecimal priceList, BigDecimal basePrice, int precision, String trxName){

        ProductPricesInfo ppi = new ProductPricesInfo();

        try{

            ppi.setC_BPartner_ID(cBpartnerID);
            ppi.setM_Product_ID(mProductID);
            ppi.setPriceList(priceList);
            ppi.setPriceFinal(basePrice);
            ppi.setPrecisionDecimal(precision);

            BigDecimal priceFinal = basePrice;

            // Modelo de socio de negocio
            MBPartner partner = new MBPartner(ctx, cBpartnerID, null);

            // Si el socio de negocio tiene seteado un porcentaje de decuento global
            if (partner.getFlatDiscount() != null){
                if (partner.getFlatDiscount().compareTo(Env.ZERO) > 0){

                    // Calculo precio final considerando este descuento global
                    priceFinal = priceFinal.multiply(Env.ONE.subtract(partner.getFlatDiscount().divide(Env.ONEHUNDRED, 6, BigDecimal.ROUND_HALF_UP))).setScale(ppi.getPrecisionDecimal(), BigDecimal.ROUND_HALF_UP);

                    ppi.setFlatDiscount(partner.getFlatDiscount());
                    ppi.setTotalDiscounts(ppi.getTotalDiscounts().add(partner.getFlatDiscount()));
                }
            }

            // Obtengo lista de descuentos vigentes en factura en pautas comerciales para este socio de negocio - producto.
            List<MZPautaComVtaDtos> dtosList = MZPautaComVta.getInvoiceDiscounts(ctx, fechaDoc, adClientID, adOrgID, cBpartnerID, mProductID, trxName);
            for (MZPautaComVtaDtos dto: dtosList){
                priceFinal = priceFinal.multiply(Env.ONE.subtract(dto.getDiscount().divide(Env.ONEHUNDRED, 6, BigDecimal.ROUND_HALF_UP))).setScale(ppi.getPrecisionDecimal(), BigDecimal.ROUND_HALF_UP);

                ppi.setTotalPautaDiscounts(ppi.getTotalPautaDiscounts().add(dto.getDiscount()));
                ppi.setTotalDiscounts(ppi.getTotalDiscounts().add(dto.getDiscount()));
            }

            // Si tengo descuento manual lo aplico
            if (dtoManual != null){
                if (dtoManual.compareTo(Env.ZERO) > 0){

                    // Calculo precio final considerando este descuento manual
                    priceFinal = priceFinal.multiply(Env.ONE.subtract(dtoManual.divide(Env.ONEHUNDRED, 6, BigDecimal.ROUND_HALF_UP))).setScale(ppi.getPrecisionDecimal(), BigDecimal.ROUND_HALF_UP);

                    ppi.setTotalManualDiscount(dtoManual);
                    ppi.setTotalDiscounts(ppi.getTotalDiscounts().add(dtoManual));
                }
            }

            ppi.setPriceFinal(priceFinal);
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return ppi;
    }

}
