package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para generación de entregas / reservas por Producto.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 8/1/20.
 */
public class MZGeneraEntProd extends X_Z_GeneraEntProd{

    public MZGeneraEntProd(Properties ctx, int Z_GeneraEntProd_ID, String trxName) {
        super(ctx, Z_GeneraEntProd_ID, trxName);
    }

    public MZGeneraEntProd(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
