package org.xpande.comercial.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInvoice;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.xpande.comercial.model.MZGeneraNCPago;
import org.xpande.comercial.utils.ComercialUtils;

import java.math.BigDecimal;

/**
 * Proceso para generar un documento de InOut según documento Invoice.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 5/17/19.
 */
public class GenerarInOutFromInvoice extends SvrProcess {

    private int cDocTypeID = -1;
    private int cBPartnerID = -1;
    private String documentNo = "";
    private MInvoice invoiceFrom = null;

    @Override
    protected void prepare() {

        ProcessInfoParameter[] para = getParameter();

        for (int i = 0; i < para.length; i++){

            String name = para[i].getParameterName();

            if (name != null){

                if (para[i].getParameter() != null){

                    if (name.trim().equalsIgnoreCase("DocumentNoRef")){
                        this.documentNo = ((String)para[i].getParameter()).trim();
                    }
                    else if (name.trim().equalsIgnoreCase("C_BPartner_ID")){
                        this.cBPartnerID = ((BigDecimal)para[i].getParameter()).intValue();
                    }
                    else if (name.trim().equalsIgnoreCase("C_DocType_ID")){
                        this.cDocTypeID = ((BigDecimal)para[i].getParameter()).intValue();
                    }
                }
            }
        }

        this.invoiceFrom = ComercialUtils.getInvoiceByDocPartner(getCtx(), this.cDocTypeID, null, this.documentNo, this.cBPartnerID, get_TrxName());
    }

    @Override
    protected String doIt() throws Exception {

        try{

            if ((this.invoiceFrom == null) || (this.invoiceFrom.get_ID() <= 0)){
                return "@Error@ " + "No existe Comprobante para ese Socio de Negocio con el Número :" + this.documentNo;
            }

            String message = this.invoiceFrom.generateInOutFromInvoice(false, true);

            if (message != null){
                return "@Error@ " + message;
            }

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return "OK";
    }
}
