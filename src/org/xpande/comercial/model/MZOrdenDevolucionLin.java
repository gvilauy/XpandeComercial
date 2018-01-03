package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Lineas asociadas a una orden de devolución a socio de negocio.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 1/3/18.
 */
public class MZOrdenDevolucionLin extends X_Z_OrdenDevolucionLin {

    public MZOrdenDevolucionLin(Properties ctx, int Z_OrdenDevolucionLin_ID, String trxName) {
        super(ctx, Z_OrdenDevolucionLin_ID, trxName);
    }

    public MZOrdenDevolucionLin(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
