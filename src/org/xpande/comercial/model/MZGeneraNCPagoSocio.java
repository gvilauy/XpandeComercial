package org.xpande.comercial.model;

import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 * Modelo de socio de negocio a considerar en el proceso de generación de notas de crédito por descuentos al pago
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/25/17.
 */
public class MZGeneraNCPagoSocio extends X_Z_GeneraNCPagoSocio {

    public MZGeneraNCPagoSocio(Properties ctx, int Z_GeneraNCPagoSocio_ID, String trxName) {
        super(ctx, Z_GeneraNCPagoSocio_ID, trxName);
    }

    public MZGeneraNCPagoSocio(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }


    /***
     * Obtiene y retorna lista de documentos seleccionados para este socio de negocio.
     * Xpande. Created by Gabriel Vila on 10/25/17.
     * @return
     */
    public List<MZGeneraNCPagoLin> getSelectedDocuments() {

        String whereClause = X_Z_GeneraNCPagoLin.COLUMNNAME_Z_GeneraNCPagoSocio_ID + " =" + this.get_ID() +
                " AND " + X_Z_GeneraNCPagoLin.COLUMNNAME_IsSelected + " ='Y' ";

        List<MZGeneraNCPagoLin> lines = new Query(getCtx(), I_Z_GeneraNCPagoLin.Table_Name, whereClause, get_TrxName()).list();

        return lines;
    }

}
