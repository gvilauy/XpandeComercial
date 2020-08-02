package org.xpande.comercial.model;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MUOM;
import org.compiere.util.DB;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para linea en el proceso de generación de entregas a clientes.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 12/4/19.
 */
public class MZGeneraEntregaLin extends X_Z_GeneraEntregaLin {

    public MZGeneraEntregaLin(Properties ctx, int Z_GeneraEntregaLin_ID, String trxName) {
        super(ctx, Z_GeneraEntregaLin_ID, trxName);
    }

    public MZGeneraEntregaLin(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean beforeSave(boolean newRecord) {

        if ((!newRecord) && (is_ValueChanged(X_Z_GeneraEntregaLin.COLUMNNAME_QtyToDeliver))){

            // Valido que la cantidad a procesar no super la cantidad pendiente
            if (this.getQtyToDeliver().compareTo(this.getQtyOpen()) > 0){
                log.saveError("ATENCIÓN", "La cantidad a procesar supera la cantidad pendiente");
                return false;
            }

            // Obtengo modelo de generación de producto
            MZGeneraEntProd entregaProd = (MZGeneraEntProd) this.getZ_GeneraEntProd();

            // Obtengo suma de unidades asignadas para este producto excluyendo esta linea
            String sql = " select sum(qtytodeliver / uommultiplyrate) as total " +
                    " from z_generaentregalin " +
                    " where z_generaentprod_id = " + entregaProd.get_ID() +
                    " and z_generaentregalin_id !=" + this.get_ID();
            BigDecimal sumQtyToDeliver = DB.getSQLValueBDEx(get_TrxName(), sql);
            if (sumQtyToDeliver == null) sumQtyToDeliver = Env.ZERO;

            // Precision según unidad de medida del producto
            MUOM uomProd = (MUOM) entregaProd.getC_UOM();
            sumQtyToDeliver = sumQtyToDeliver.setScale(uomProd.getStdPrecision(), RoundingMode.HALF_UP);

            // Si tengo cantidad a asignar
            BigDecimal qtyToDeliver = Env.ZERO;
            if (this.getQtyToDeliver().compareTo(Env.ZERO) > 0){

                // Obtengo cantidad disponible aun del producto para asignar
                BigDecimal qtyAvailable = entregaProd.getQtyAvailable().subtract(sumQtyToDeliver);
                qtyAvailable = qtyAvailable.setScale(uomProd.getStdPrecision(), RoundingMode.HALF_UP);

                // Valido que aún tengo disponibilidad para este linea
                qtyToDeliver = this.getQtyToDeliver().divide(this.getUomMultiplyRate(), uomProd.getStdPrecision(), RoundingMode.HALF_UP);
                qtyToDeliver = qtyToDeliver.setScale(uomProd.getStdPrecision(), RoundingMode.HALF_UP);

                if (qtyToDeliver.compareTo(qtyAvailable) > 0){
                    log.saveError("ATENCIÓN", "La cantidad a procesar supera la cantidad disponible del producto : " + qtyAvailable);
                    return false;
                }
            }

            // Actualizo totales de generacion del producto
            entregaProd.setQtyToDeliver(sumQtyToDeliver.add(qtyToDeliver));
            entregaProd.setQtyOnHand(entregaProd.getQtyAvailable().subtract(entregaProd.getQtyToDeliver()));
            entregaProd.saveEx();
        }

        return true;
    }

    @Override
    protected boolean afterDelete(boolean success) {

        if (!success) return false;

        try{
            // Obtengo modelo de generación de producto
            MZGeneraEntProd entregaProd = (MZGeneraEntProd) this.getZ_GeneraEntProd();

            // Obtengo suma de unidades asignadas para este producto excluyendo esta linea
            String sql = " select sum(qtytodeliver / uommultiplyrate) as total " +
                    " from z_generaentregalin " +
                    " where z_generaentprod_id = " + entregaProd.get_ID() +
                    " and z_generaentregalin_id !=" + this.get_ID();
            BigDecimal sumQtyToDeliver = DB.getSQLValueBDEx(get_TrxName(), sql);
            if (sumQtyToDeliver == null) sumQtyToDeliver = Env.ZERO;

            // Precision según unidad de medida del producto
            MUOM uomProd = (MUOM) entregaProd.getC_UOM();
            sumQtyToDeliver = sumQtyToDeliver.setScale(uomProd.getStdPrecision(), RoundingMode.HALF_UP);

            // Actualizo totales de generacion del producto
            entregaProd.setQtyToDeliver(sumQtyToDeliver);
            entregaProd.setQtyOnHand(entregaProd.getQtyAvailable().subtract(entregaProd.getQtyToDeliver()));
            entregaProd.saveEx();
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return true;
    }
}
