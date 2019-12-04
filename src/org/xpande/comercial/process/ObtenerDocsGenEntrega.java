package org.xpande.comercial.process;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.xpande.comercial.model.MZGeneraEntrega;

/**
 * Proceso para obtener documentos a considerar en el proceso de generación de entregas a clientes.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 12/4/19.
 */
public class ObtenerDocsGenEntrega extends SvrProcess {

    private MZGeneraEntrega generaEntrega = null;
    private String tipoAccion = "NUEVOS";

    @Override
    protected void prepare() {

        ProcessInfoParameter[] para = getParameter();

        for (int i = 0; i < para.length; i++){

            String name = para[i].getParameterName();

            if (name != null){

                if (para[i].getParameter() != null){

                    if (name.trim().equalsIgnoreCase("SelDocGenPagoAction")){
                        this.tipoAccion = (String)para[i].getParameter();
                    }
                }
            }
        }

        this.generaEntrega = new MZGeneraEntrega(getCtx(), this.getRecord_ID(), get_TrxName());

    }

    @Override
    protected String doIt() throws Exception {

        String message = this.generaEntrega.getDocumentos(tipoAccion);

        if (message != null){
            return "@Error@ " + message;
        }

        return "OK";
    }

}
