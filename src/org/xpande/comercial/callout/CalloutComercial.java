package org.xpande.comercial.callout;

import org.compiere.model.*;

import java.util.Properties;

/**
 * Callouts Comerciales.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 8/23/17.
 */
public class CalloutComercial extends CalloutEngine {


    /***
     * Setea DocBaseType según ID de documento recibido.
     * Xpande. Created by Gabriel Vila on 8/23/17.
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     * @return
     */
    public String docBaseTypeByDocType(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {

        if ((value == null) || (((Integer) value).intValue() <= 0)){
            return "";
        }

        int cDocTypeID = ((Integer) value).intValue();

        MDocType docType = new MDocType(ctx, cDocTypeID, null);
        mTab.setValue("DocBaseType", docType.getDocBaseType());

        return "";
    }

}
