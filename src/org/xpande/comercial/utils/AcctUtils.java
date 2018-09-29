package org.xpande.comercial.utils;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.DB;

import java.util.Properties;

/**
 * Clase de métodos staticos referidos a funcionalidades contables para el core Comercial.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 8/28/18.
 */
public final class AcctUtils {

    public static int getIDCargoRedondeo(Properties ctx, String trxName){

        int ID = -1;
        String sql = "";

        try{

            sql = " select Charge_Rounding_ID " +
                    " from z_AcctConfig " +
                    " where lower(value) ='general' ";
            ID = DB.getSQLValueEx(trxName, sql);

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return ID;
    }

}
