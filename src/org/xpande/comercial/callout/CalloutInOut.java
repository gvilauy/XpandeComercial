package org.xpande.comercial.callout;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.xpande.core.model.MZProductoUPC;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Callouts para entregas/rececpiones/devoluciones en el módulo comercial.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 7/23/19.
 */
public class CalloutInOut extends CalloutEngine {

    /***
     * Al ingresar código de barras o el producto directamente, se deben setear los otros valores asociados.
     * Xpande. Created by Gabriel Vila on 7/23/19.
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     * @return
     */
    public String upcProduct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {

        if (isCalloutActive()) return "";

        if ((value == null) || (value.toString().trim().equalsIgnoreCase(""))){
            mTab.setValue("UPC", null);
            mTab.setValue("VendorProductNo", null);
            mTab.setValue("M_Product_ID", null);
            return "";
        }

        int cBPartnerID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");

        String column = mField.getColumnName();

        if (column.equalsIgnoreCase("UPC")){
            MZProductoUPC pupc = MZProductoUPC.getByUPC(ctx, value.toString().trim(), null);
            if ((pupc != null) && (pupc.get_ID() > 0)){
                mTab.setValue("M_Product_ID", pupc.getM_Product_ID());
            }
        }
        else if (column.equalsIgnoreCase("M_Product_ID")){
            int mProductID = ((Integer) value).intValue();
            if ((mTab.getValue("UPC") == null) || (mTab.getValue("UPC").toString().trim().equalsIgnoreCase(""))){
                MZProductoUPC pupc = MZProductoUPC.getByProduct(ctx, mProductID, null);
                if ((pupc != null) && (pupc.get_ID() > 0)){
                    mTab.setValue("UPC", pupc.getUPC());
                }
            }
            else{
                // El usuario ingreso un UPC y ademas selecciono un producto.
                // Puede pasar que quiera asociar un nuevo UPC al producto seleccionado o que cambió el producto y el UPC es de otro.
                // En el segundo caso, tengo que cargar el UPC correcto del nuevo producto seleccionado.
                String upc = mTab.getValue("UPC").toString().trim();
                MZProductoUPC pupc = MZProductoUPC.getByUPC(ctx, upc, null);
                if ((pupc != null) && (pupc.get_ID() > 0)){
                    if (pupc.getM_Product_ID() != mProductID){
                        MZProductoUPC pupcProd = MZProductoUPC.getByProduct(ctx, mProductID, null);
                        if ((pupcProd != null) && (pupcProd.get_ID() > 0)){
                            mTab.setValue("UPC", pupcProd.getUPC());
                        }
                        else{
                            mTab.setValue("UPC", null);
                        }
                    }
                }
            }
        }

        return "";
    }

    /**
     *	M_InOut - Defaults for BPartner.
     *			- Location
     *			- Contact
     *	@param ctx
     *	@param WindowNo
     *	@param mTab
     *	@param mField
     *	@param value
     *	@return error message or ""
     */
    public String bpartner (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
        Integer C_BPartner_ID = (Integer)value;
        if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0)
            return "";

        String sql = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
                + "p.M_PriceList_ID,p.PaymentRule,p.POReference,"
                + "p.SO_Description,p.IsDiscountPrinted,"
                + "p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
                + "l.C_BPartner_Location_ID,c.AD_User_ID "
                + "FROM C_BPartner p, C_BPartner_Location l, AD_User c "
                + "WHERE l.IsActive='Y' AND p.C_BPartner_ID=l.C_BPartner_ID(+)"
                + " AND p.C_BPartner_ID=c.C_BPartner_ID(+)"
                + " AND p.C_BPartner_ID=?";		//	1

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, C_BPartner_ID.intValue());
            rs = pstmt.executeQuery();
            if (rs.next())
            {
                //[ 1867464 ]
                boolean IsSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));
                if (!IsSOTrx)
                {
                    //	Location
                    Integer ii = new Integer(rs.getInt("C_BPartner_Location_ID"));
                    if (rs.wasNull())
                        mTab.setValue("C_BPartner_Location_ID", null);
                    else
                        mTab.setValue("C_BPartner_Location_ID", ii);
                    //	Contact
                    ii = new Integer(rs.getInt("AD_User_ID"));
                    if (rs.wasNull())
                        mTab.setValue("AD_User_ID", null);
                    else
                        mTab.setValue("AD_User_ID", ii);
                }

                //Bugs item #1679818: checking for SOTrx only
                if (IsSOTrx)
                {
                    //	Location
                    Integer ii = new Integer(rs.getInt("C_BPartner_Location_ID"));
                    if (rs.wasNull())
                        mTab.setValue("C_BPartner_Location_ID", null);
                    else
                        mTab.setValue("C_BPartner_Location_ID", ii);

                    //	CreditAvailable
                    double CreditAvailable = rs.getDouble("CreditAvailable");
                    if (!rs.wasNull() && CreditAvailable < 0)
                        mTab.fireDataStatusEEvent("CreditLimitOver",
                                DisplayType.getNumberFormat(DisplayType.Amount).format(CreditAvailable),
                                false);
                }//
            }
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, sql, e);
            return e.getLocalizedMessage();
        }
        finally
        {
            DB.close(rs, pstmt);
        }

        return "";
    }	//	bpartner

}
