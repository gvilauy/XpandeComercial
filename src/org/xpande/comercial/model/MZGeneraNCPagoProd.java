package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para detalle por producto en el proceso de generación de notas de crédito por descuentos al pago.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/25/17.
 */
public class MZGeneraNCPagoProd extends X_Z_GeneraNCPagoProd {

    public MZGeneraNCPagoProd(Properties ctx, int Z_GeneraNCPagoProd_ID, String trxName) {
        super(ctx, Z_GeneraNCPagoProd_ID, trxName);
    }

    public MZGeneraNCPagoProd(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
