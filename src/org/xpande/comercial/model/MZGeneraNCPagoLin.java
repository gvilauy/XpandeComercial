package org.xpande.comercial.model;

import java.sql.ResultSet;
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
}
