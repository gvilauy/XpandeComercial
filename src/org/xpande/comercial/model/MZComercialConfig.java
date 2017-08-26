package org.xpande.comercial.model;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoicePaySchedule;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para configuraciones comerciales del Core.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 8/25/17.
 */
public class MZComercialConfig extends X_Z_ComercialConfig {

    public MZComercialConfig(Properties ctx, int Z_ComercialConfig_ID, String trxName) {
        super(ctx, Z_ComercialConfig_ID, trxName);
    }

    public MZComercialConfig(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }


    /***
     * Obtiene modelo único de configuración de modulo comercial.
     * Xpande. Created by Gabriel Vila on 8/22/17.
     * @param ctx
     * @param trxName
     * @return
     */
    public static MZComercialConfig getDefault(Properties ctx, String trxName){

        MZComercialConfig model = new Query(ctx, I_Z_ComercialConfig.Table_Name, "", trxName).first();

        return model;
    }

    /***
     * Valida vencimientos manuales ingresados en una invoice de compra/venta.
     * Xpande. Created by Gabriel Vila on 8/25/17.
     * @param invoice
     * @return
     */
    public String validateInvoiceVencimientoManual(MInvoice invoice){

        String message = null;

        try{

            MInvoicePaySchedule[] schedule = MInvoicePaySchedule.getInvoicePaySchedule(getCtx(), invoice.get_ID(), 0, get_TrxName());
            if (schedule.length == 0){
                return "Debe indicar vencimientos del Comprobante.";
            }

            boolean validDueDate = true;

            BigDecimal total = Env.ZERO;
            for (int i = 0; i < schedule.length; i++)
            {
                BigDecimal due = schedule[i].getDueAmt();
                if (due != null){
                    total = total.add(due);
                }

                if (schedule[i].getDueDate().before(invoice.getDateInvoiced())){
                    validDueDate = false;
                }

            }

            if (!validDueDate){
                return "Fecha de vencimiento ingresada es anterior a la fecha del comprobante.";
            }

            boolean validAmt = invoice.getGrandTotal().compareTo(total) == 0;
            invoice.setIsPayScheduleValid(validAmt);
            invoice.saveEx();

            //	Schedule
            for (int i = 0; i < schedule.length; i++)
            {
                if (schedule[i].isValid() != validAmt)
                {
                    schedule[i].setIsValid(validAmt);
                    schedule[i].saveEx();
                }
            }

            if (!validAmt){
                message = "La suma de importes de vencimientos no coincide con el Total del comprobante: \n" +
                        " Total Comprobante = " + invoice.getGrandTotal() + "\n" +
                        " Total Vencimientos = " + total + "\n" +
                        " Diferencia = " + invoice.getGrandTotal().subtract(total);
                return message;
            }

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return message;
    }
}
