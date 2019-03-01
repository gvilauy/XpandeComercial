package org.xpande.comercial.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.SvrProcess;
import org.xpande.comercial.model.MZLoadInvoice;

/**
 * Proceso de carga de comprobantes comerciales.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 2/28/19.
 */
public class InterfaceCargaInvoice extends SvrProcess {

    MZLoadInvoice loadInvoice = null;

    @Override
    protected void prepare() {

        this.loadInvoice = new MZLoadInvoice(getCtx(), this.getRecord_ID(), get_TrxName());
    }

    @Override
    protected String doIt() throws Exception {

        try{

            if ((this.loadInvoice.getFileName() == null) || (this.loadInvoice.getFileName().trim().equalsIgnoreCase(""))){
                return "@Error@ Debe indicar archivo a procesar ";
            }

            this.loadInvoice.executeInterface();

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return "OK";
    }
}
