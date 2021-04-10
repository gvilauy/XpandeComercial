package org.xpande.comercial.model;

import org.compiere.model.MBPartner;

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

    @Override
    protected boolean beforeSave(boolean newRecord) {

        // Seteo ingformacion del socio de negocio
        MBPartner partner = (MBPartner) this.getC_BPartner();
        if (partner.get_ValueAsInt("Z_CanalVenta_ID") > 0){
            this.setZ_CanalVenta_ID(partner.get_ValueAsInt("Z_CanalVenta_ID"));
        }
        if (partner.getSalesRep_ID() > 0){
            this.setSalesRep_ID(partner.getSalesRep_ID());
        }

        return true;
    }
}
