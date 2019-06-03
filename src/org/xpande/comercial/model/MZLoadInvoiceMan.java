package org.xpande.comercial.model;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MDocType;
import org.xpande.cfe.model.MZCFEConfig;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para carga manual de comprobantes comerciales.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 3/4/19.
 */
public class MZLoadInvoiceMan extends X_Z_LoadInvoiceMan {

    public MZLoadInvoiceMan(Properties ctx, int Z_LoadInvoiceMan_ID, String trxName) {
        super(ctx, Z_LoadInvoiceMan_ID, trxName);
    }

    public MZLoadInvoiceMan(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean beforeSave(boolean newRecord) {

        if ((newRecord) || (is_ValueChanged(COLUMNNAME_C_DocType_ID))){

            MZLoadInvoice loadInvoice = (MZLoadInvoice) this.getZ_LoadInvoice();

            // Para comprobantes de venta
            if (loadInvoice.isSOTrx()){

                MZCFEConfig cfeConfig = MZCFEConfig.getDefault(getCtx(), null);
                if ((cfeConfig == null) || (cfeConfig.get_ID() <= 0)){
                    log.saveError("ATENCIÓN", "Falta parametrización en el sistema para conceptos de CFE");
                    return false;
                }

                MDocType docType = new MDocType(getCtx(), this.getC_DocType_ID(), null);
                if ((loadInvoice.isSOTrx() && !docType.isSOTrx()) || (!loadInvoice.isSOTrx() && docType.isSOTrx())){
                    log.saveError("ATENCIÓN", "El Tipo de Documento ingresado no es igual a la opción Compra/Venta de este Documento");
                    return false;
                }

                // Me aseguro que el documento recibido no EMITA CFE !!.
                if (cfeConfig.isDocSendCFE(this.getAD_OrgTrx_ID(), docType.get_ID())){
                    log.saveError("ATENCIÓN", "El Tipo de Documento ingresado NO debe emitir Facturación Electrónica (CFE)");
                    return false;
                }
            }

        }

        return true;
    }

}
