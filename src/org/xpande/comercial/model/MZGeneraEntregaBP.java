package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para socios de negocio a considerar en el proceso de generación de entregas a clientes.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 12/4/19.
 */
public class MZGeneraEntregaBP extends X_Z_GeneraEntregaBP {

    public MZGeneraEntregaBP(Properties ctx, int Z_GeneraEntregaBP_ID, String trxName) {
        super(ctx, Z_GeneraEntregaBP_ID, trxName);
    }

    public MZGeneraEntregaBP(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
