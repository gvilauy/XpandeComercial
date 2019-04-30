package org.xpande.comercial.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.xpande.comercial.utils.ComercialUtils;
import org.xpande.core.utils.CurrencyUtils;

import java.math.BigDecimal;

/**
 * Proceso para copiar lineas de una invoice orgien en una invoice destino.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 2/26/19.
 */
public class CopiarInvoice extends SvrProcess {

    private int cBPartnerID = 0;
    private int cDocTypeID = 0;
    private String documentSerie = "";
    private String documentNo = "";
    private MInvoice invoiceFrom = null;
    private MInvoice invoiceTo = null;

    @Override
    protected void prepare() {

        ProcessInfoParameter[] para = getParameter();

        for (int i = 0; i < para.length; i++){

            String name = para[i].getParameterName();

            if (name != null){
                if (para[i].getParameter() != null){
                    if (name.trim().equalsIgnoreCase("DocumentNoRef")){
                        this.documentNo = para[i].getParameter().toString().trim();
                    }
                    else if (name.trim().equalsIgnoreCase("C_BPartner_ID")){
                        this.cBPartnerID = ((BigDecimal)para[i].getParameter()).intValue();
                    }
                    else if (name.trim().equalsIgnoreCase("C_DocType_ID")){
                        this.cDocTypeID = ((BigDecimal)para[i].getParameter()).intValue();
                    }
                    else if (name.trim().equalsIgnoreCase("DocumentSerie")){
                        this.documentSerie = para[i].getParameter().toString().trim();
                    }
                }
            }
        }

        this.invoiceFrom = ComercialUtils.getInvoiceByDocPartner(getCtx(), this.cDocTypeID, this.documentNo, this.cBPartnerID, get_TrxName());
        this.invoiceTo = new MInvoice(this.getCtx(), this.getRecord_ID(), this.get_TrxName());

    }

    @Override
    protected String doIt() throws Exception {

        try{

            if ((this.invoiceFrom == null) || (this.invoiceFrom.get_ID() <= 0)){
                return "@Error@ " + "No existe Comprobante para ese Socio de Negocio con el Número :" + this.documentNo;
            }

            // Onbtengo lineas del documento origen y las recorro.
            MInvoiceLine[] lines = this.invoiceFrom.getLines(true);
            for (int i = 0; i < lines.length; i++){

                MInvoiceLine invoiceLineFrom = lines[i];
                MInvoiceLine invoiceLineTo = new MInvoiceLine(this.invoiceTo);
                PO.copyValues (invoiceLineFrom, invoiceLineTo);
                invoiceLineTo.set_ValueOfColumn("AD_Client_ID", invoiceLineTo.getAD_Client_ID());
                invoiceLineTo.setAD_Org_ID(invoiceLineTo.getAD_Org_ID());
                invoiceLineTo.setC_Invoice_ID(this.invoiceTo.get_ID());
                invoiceLineTo.setInvoice(this.invoiceTo);
                invoiceLineTo.setProcessed(false);
                invoiceLineTo.saveEx();
            }

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return "OK";
    }

}
