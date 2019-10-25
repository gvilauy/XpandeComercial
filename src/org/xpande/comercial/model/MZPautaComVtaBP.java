package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para socio de negocio asociado a una pauta comercial de venta
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/24/19.
 */
public class MZPautaComVtaBP extends X_Z_PautaComVtaBP {

    public MZPautaComVtaBP(Properties ctx, int Z_PautaComVtaBP_ID, String trxName) {
        super(ctx, Z_PautaComVtaBP_ID, trxName);
    }

    public MZPautaComVtaBP(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
