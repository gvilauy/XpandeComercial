package org.xpande.comercial.model;

import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 * Modelo de linea para el proceso de generación de notas de crédito por descuentos al pago.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/25/17.
 */
public class MZGeneraNCPagoLin extends X_Z_GeneraNCPagoLin {

    public MZGeneraNCPagoLin(Properties ctx, int Z_GeneraNCPagoLin_ID, String trxName) {
        super(ctx, Z_GeneraNCPagoLin_ID, trxName);
    }

    public MZGeneraNCPagoLin(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }


    /***
     * Obtiene y retorna lista de productos asociados al documento de esta linea.
     * Xpande. Created by Gabriel Vila on 10/27/17.
     * @return
     */
    public List<MZGeneraNCPagoProd> getProductos(){

        String whereClause = X_Z_GeneraNCPagoProd.COLUMNNAME_Z_GeneraNCPagoLin_ID + " =" + this.get_ID();

        List<MZGeneraNCPagoProd> lines = new Query(getCtx(), I_Z_GeneraNCPagoProd.Table_Name, whereClause, get_TrxName()).list();

        return lines;
    }

}
