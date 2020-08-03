package org.xpande.comercial.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para lineas del documento de Reservas de Venta.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 8/2/20.
 */
public class MZReservaVtaLin extends X_Z_ReservaVtaLin{

    public MZReservaVtaLin(Properties ctx, int Z_ReservaVtaLin_ID, String trxName) {
        super(ctx, Z_ReservaVtaLin_ID, trxName);
    }

    public MZReservaVtaLin(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
