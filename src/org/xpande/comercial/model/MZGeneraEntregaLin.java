package org.xpande.comercial.model;

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
}
