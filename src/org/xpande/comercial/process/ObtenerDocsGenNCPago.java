package org.xpande.comercial.process;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.xpande.comercial.model.MZGeneraNCPago;

/**
 * Proceso para obtener documentos a considerar en el proceso de generación de notas de crédito por descuentos al pago.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/25/17.
 */
public class ObtenerDocsGenNCPago extends SvrProcess {

    private MZGeneraNCPago generaNCPago = null;
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

        this.generaNCPago = new MZGeneraNCPago(getCtx(), this.getRecord_ID(), get_TrxName());
    }

    @Override
    protected String doIt() throws Exception {

        String message = this.generaNCPago.getDocumentos(tipoAccion);

        if (message != null){
            return "@Error@ " + message;
        }

        return "OK";
    }

}
