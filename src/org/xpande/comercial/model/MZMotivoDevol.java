package org.xpande.comercial.model;

import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo de motivo de devolucion de mercaderia.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 3/28/21.
 */
public class MZMotivoDevol extends  X_Z_MotivoDevol{

    public MZMotivoDevol(Properties ctx, int Z_MotivoDevol_ID, String trxName) {
        super(ctx, Z_MotivoDevol_ID, trxName);
    }

    public MZMotivoDevol(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }


    /***
     * Obtiene y retorna modelo según value recibido.
     * Xpande. Created by Gabriel Vila on 3/28/21.
     * @param ctx
     * @param value
     * @param trxName
     * @return
     */
    public static MZMotivoDevol getByValue(Properties ctx, String value, String trxName){

        String whereClause = X_Z_MotivoDevol.COLUMNNAME_Value + " ='" + value + "' ";

        MZMotivoDevol model = new Query(ctx, I_Z_MotivoDevol.Table_Name, whereClause, trxName).first();

        return model;
    }

}
