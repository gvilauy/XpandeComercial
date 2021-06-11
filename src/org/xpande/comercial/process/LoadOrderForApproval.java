package org.xpande.comercial.process;

import org.compiere.process.SvrProcess;
import org.xpande.comercial.model.MZSaleOrderAuth;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 6/9/21.
 */
public class LoadOrderForApproval extends SvrProcess {

    MZSaleOrderAuth orderAuth = null;

    @Override
    protected void prepare() {
        this.orderAuth = new MZSaleOrderAuth(getCtx(), this.getRecord_ID(), get_TrxName());
    }

    @Override
    protected String doIt() throws Exception {
        String message = this.orderAuth.getOrders();
        if (message != null){
            return "@Error@ " + message;
        }
        return "OK";
    }
}
