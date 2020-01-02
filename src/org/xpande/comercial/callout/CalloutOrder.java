package org.xpande.comercial.callout;

import org.compiere.model.*;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.xpande.comercial.utils.DiscountUtils;
import org.xpande.comercial.utils.ProductPricesInfo;
import org.xpande.core.model.MZProductoUPC;
import org.xpande.core.utils.TaxUtils;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Callouts para ordenes de venta y compra en el módulo comercial.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/17/19.
 */
public class CalloutOrder extends CalloutEngine {

    /**	Debug Steps			*/
    private boolean steps = false;


    /*************************************************************************
     *	Order Line - Product.
     *		- reset C_Charge_ID / M_AttributeSetInstance_ID
     *		- PriceList, PriceStd, PriceLimit, C_Currency_ID, EnforcePriceLimit
     *		- UOM
     *	Calls Tax
     *
     *  @param ctx context
     *  @param WindowNo current Window No
     *  @param mTab Grid Tab
     *  @param mField Grid Field
     *  @param value New Value
     *  @return null or error message
     */
    public String product (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {

        // Xpande. Gabriel Vila.
        // Llamo a callout para setear los datos : codigo de barras - codigo prod.prov - producto
        String message = upcVendProdNoProduct(ctx, WindowNo, mTab, mField, value);
        if (!message.equalsIgnoreCase("")){
            return  message;
        }
        // Xpande.


        Integer M_Product_ID = (Integer)value;
        Integer M_AttributeSetInstance_ID = 0;
        //
        if (M_Product_ID == null || M_Product_ID.intValue() == 0)
        {
            //  If the product information is deleted, zero the other items as well
            mTab.setValue("M_AttributeSetInstance_ID", null);
            mTab.setValue("PriceList", new BigDecimal(0));
            mTab.setValue("PriceLimit", new BigDecimal(0));
            mTab.setValue("PriceActual", new BigDecimal(0));
            mTab.setValue("PriceEntered", new BigDecimal(0));
            mTab.setValue("C_Currency_ID", null);
            mTab.setValue("Discount", new BigDecimal(0));
            mTab.setValue("Discount2", new BigDecimal(0));
            mTab.setValue("C_UOM_ID", null);
            return "";
        }
        if (steps) log.warning("init");

        int adOrgID = Env.getContextAsInt(ctx, WindowNo, "AD_Org_ID");
        int cOrderID = ((Integer)mTab.getValue("C_Order_ID")).intValue();
        if (cOrderID <= 0) {
            return "No se obtuvo ID interno de Orden";
        }

        MOrder order = new MOrder(ctx, cOrderID, null);
        int M_PriceList_ID = order.getM_PriceList_ID();
        if (M_PriceList_ID <= 0) {
            return "No se obtuvo ID interno de Lista de Precios de la Orden";
        }

        int M_PriceList_Version_ID = ((MPriceList)order.getM_PriceList()).getPriceListVersion(null).get_ID();

        int StdPrecision = MPriceList.getPricePrecision(ctx, M_PriceList_ID);

        MProduct product = MProduct.get (ctx, M_Product_ID.intValue());
        I_M_AttributeSetInstance asi = product.getM_AttributeSetInstance();
        //
        mTab.setValue("C_Charge_ID", null);
        //	Set Attribute from context or, if null, from the Product
        //	Get Model and check the Attribute Set Instance from the context
        MProduct m_product = MProduct.get(Env.getCtx(), M_Product_ID);
        mTab.setValue("M_AttributeSetInstance_ID", m_product.getEnvAttributeSetInstance(ctx, WindowNo));
        if (Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
                && Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
            mTab.setValue("M_AttributeSetInstance_ID", Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_AttributeSetInstance_ID"));
        else {
            mTab.setValue("M_AttributeSetInstance_ID", asi.getM_AttributeSetInstance_ID());
        }
        /*****	Price Calculation see also qty	****/
        int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
        BigDecimal Qty = (BigDecimal)mTab.getValue("QtyOrdered");
        boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
        org.xpande.comercial.model.MProductPricing pp = new org.xpande.comercial.model.MProductPricing (M_Product_ID.intValue(), C_BPartner_ID, adOrgID,
                order.getDateOrdered(), Qty, IsSOTrx, null);
        //
        pp.setM_PriceList_ID(M_PriceList_ID);
        pp.setForcedPrecision(StdPrecision);
        Timestamp orderDate = (Timestamp)mTab.getValue("DateOrdered");

        /** PLV is only accurate if PL selected in header */
        if ( M_PriceList_Version_ID == 0 && M_PriceList_ID > 0)
        {
            String sql = "SELECT plv.M_PriceList_Version_ID "
                    + "FROM M_PriceList_Version plv "
                    + "WHERE plv.M_PriceList_ID=? "						//	1
                    + " AND plv.ValidFrom <= ? "
                    + "ORDER BY plv.ValidFrom DESC";
            //	Use newest price list - may not be future

            M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, M_PriceList_ID, orderDate);
            if ( M_PriceList_Version_ID > 0 )
                Env.setContext(ctx, WindowNo, "M_PriceList_Version_ID", M_PriceList_Version_ID );
        }
        pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
        pp.setPriceDate(orderDate);

        BigDecimal manualDiscount = (BigDecimal)mTab.getValue("DiscountMan");
        if (manualDiscount == null) manualDiscount = Env.ZERO;

        // Xpande. Gabriel Vila. 02/01/2020.
        // Comento y cambio seteos de precios

        /*
        //
        mTab.setValue("PriceList", pp.getPriceList());
        mTab.setValue("PriceLimit", pp.getPriceLimit());
        mTab.setValue("PriceActual", pp.getPriceStd());
        mTab.setValue("PriceEntered", pp.getPriceStd());
        */

        mTab.setValue("PriceActual", pp.getPriceStd());
        mTab.setValue("PriceEntered", pp.getPriceStd());
        mTab.setValue("PriceList", pp.getPriceStd());
        mTab.setValue("PriceLimit", pp.getPriceStd());

        // Fin Xpande

        mTab.setValue("C_Currency_ID", pp.getC_Currency_ID());
        mTab.setValue("Discount", pp.getDiscount());
        mTab.setValue("C_UOM_ID", product.getC_UOM_ID());
        mTab.setValue("QtyOrdered", mTab.getValue("QtyEntered"));
        Env.setContext(ctx, WindowNo, "EnforcePriceLimit", pp.isEnforcePriceLimit() ? "Y" : "N");
        Env.setContext(ctx, WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");

        // Xpande. Gabriel Vila. 02/01/2010.
        // Calculo descuentos para esta fecha documento - socio de negocio - producto.
        ProductPricesInfo ppi = DiscountUtils.setDiscountPrices(ctx, orderDate, order.getAD_Client_ID(), order.getAD_Org_ID(), C_BPartner_ID, M_Product_ID,
                manualDiscount, (BigDecimal) mTab.getValue("PriceList"), (BigDecimal) mTab.getValue("PriceActual"), StdPrecision, null);

        if (ppi != null){
            mTab.setValue("PriceActual", ppi.getPriceFinal());
            mTab.setValue("PriceEntered", ppi.getPriceFinal());
            mTab.setValue("FlatDiscount", ppi.getFlatDiscount());
            mTab.setValue("DiscountPauta", ppi.getTotalPautaDiscounts());
            //mTab.setValue("Discount", ppi.getTotalDiscounts());
        }
        // Fin Xpande.

        // Xpande.Gabriel Vila.
        // Si estoy en orden de compra y tengo tasa de cambio que aplicar, la aplico.
        if (mTab.getValue("C_Order_ID") != null){
            order = new MOrder(ctx, cOrderID, null);
            BigDecimal multiplyRate = (BigDecimal) order.get_Value("MultiplyRate");
            if (!order.isSOTrx()){
                if ((multiplyRate != null) && (multiplyRate.compareTo(Env.ZERO) != 0)){
                    if (!mTab.getValueAsBoolean("IsConverted")){
                        BigDecimal priceList = ((BigDecimal)mTab.getValue("PriceList")).multiply(multiplyRate).setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
                        BigDecimal priceActual = ((BigDecimal)mTab.getValue("PriceActual")).multiply(multiplyRate).setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
                        BigDecimal priceLimit = ((BigDecimal)mTab.getValue("PriceLimit")).multiply(multiplyRate).setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
                        BigDecimal priceEntered = ((BigDecimal)mTab.getValue("PriceEntered")).multiply(multiplyRate).setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
                        mTab.setValue("PriceList", priceList);
                        mTab.setValue("PriceLimit", priceLimit);
                        mTab.setValue("PriceActual", priceActual);
                        mTab.setValue("PriceEntered", priceEntered);
                        mTab.setValue("IsConverted", true);
                    }
                }
            }
        }
        // Xpande.

        //	Check/Update Warehouse Setting
        //	int M_Warehouse_ID = Env.getContextAsInt(ctx, WindowNo, "M_Warehouse_ID");
        //	Integer wh = (Integer)mTab.getValue("M_Warehouse_ID");
        //	if (wh.intValue() != M_Warehouse_ID)
        //	{
        //		mTab.setValue("M_Warehouse_ID", new Integer(M_Warehouse_ID));
        //		ADialog.warn(,WindowNo, "WarehouseChanged");
        //	}


        if (Env.isSOTrx(ctx, WindowNo))
        {
            if (product.isStocked())
            {

                // Xpande. Gabriel Vila. 07/05/2018.
                // Comento avisos de stock en ordenes de venta

                /*
                BigDecimal QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
                int M_Warehouse_ID = Env.getContextAsInt(ctx, WindowNo, "M_Warehouse_ID");
                M_AttributeSetInstance_ID = Env.getContextAsInt(ctx, WindowNo, "M_AttributeSetInstance_ID");
                BigDecimal available = MStorage.getQtyAvailable
                        (M_Warehouse_ID, M_Product_ID.intValue(), M_AttributeSetInstance_ID, null);
                if (available == null)
                    available = Env.ZERO;
                if (available.signum() == 0)
                    mTab.fireDataStatusEEvent ("NoQtyAvailable", "0", false);
                else if (available.compareTo(QtyOrdered) < 0)
                    mTab.fireDataStatusEEvent ("InsufficientQtyAvailable", available.toString(), false);
                else
                {
                    Integer C_OrderLine_ID = (Integer)mTab.getValue("C_OrderLine_ID");
                    if (C_OrderLine_ID == null)
                        C_OrderLine_ID = new Integer(0);
                    BigDecimal notReserved = MOrderLine.getNotReserved(ctx,
                            M_Warehouse_ID, M_Product_ID, M_AttributeSetInstance_ID,
                            C_OrderLine_ID.intValue());
                    if (notReserved == null)
                        notReserved = Env.ZERO;
                    BigDecimal total = available.subtract(notReserved);
                    if (total.compareTo(QtyOrdered) < 0)
                    {
                        String info = Msg.parseTranslation(ctx, "@QtyAvailable@=" + available
                                + " - @QtyNotReserved@=" + notReserved + " = " + total);
                        mTab.fireDataStatusEEvent ("InsufficientQtyAvailable",
                                info, false);
                    }
                }
                */
                // Fin Xpande
            }
        }
        //
        if (steps) log.warning("fini");
        return tax (ctx, WindowNo, mTab, mField, value);
    }	//	product


    /**
     *	Order Line - Charge.
     * 		- updates PriceActual from Charge
     * 		- sets PriceLimit, PriceList to zero
     * 	Calls tax
     *  @param ctx context
     *  @param WindowNo current Window No
     *  @param mTab Grid Tab
     *  @param mField Grid Field
     *  @param value New Value
     *  @return null or error message
     */
    public String charge (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
        Integer C_Charge_ID = (Integer)value;
        if (C_Charge_ID == null || C_Charge_ID.intValue() == 0)
            return "";
        //	No Product defined
        if (mTab.getValue("M_Product_ID") != null)
        {
            mTab.setValue("C_Charge_ID", null);
            return "ChargeExclusively";
        }
        mTab.setValue("M_AttributeSetInstance_ID", null);
        mTab.setValue("S_ResourceAssignment_ID", null);
        mTab.setValue("C_UOM_ID", 100);	//	EA

        Env.setContext(ctx, WindowNo, "DiscountSchema", "N");
        String sql = "SELECT ChargeAmt FROM C_Charge WHERE C_Charge_ID=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, C_Charge_ID.intValue());
            rs = pstmt.executeQuery();
            if (rs.next())
            {
                mTab.setValue ("PriceEntered", rs.getBigDecimal (1));
                mTab.setValue ("PriceActual", rs.getBigDecimal (1));
                mTab.setValue ("PriceLimit", Env.ZERO);
                mTab.setValue ("PriceList", Env.ZERO);
                mTab.setValue ("Discount", Env.ZERO);
                mTab.setValue ("Discount2", Env.ZERO);
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
            rs = null; pstmt = null;
        }

        //
        return tax (ctx, WindowNo, mTab, mField, value);
    }	//	charge


    /**
     *	Order Line - Tax.
     *		- basis: Product, Charge, BPartner Location
     *		- sets C_Tax_ID
     *  Calls Amount
     *  @param ctx context
     *  @param WindowNo current Window No
     *  @param mTab Grid Tab
     *  @param mField Grid Field
     *  @param value New Value
     *  @return null or error message
     */
    public String tax (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
        String column = mField.getColumnName();
        if (value == null)
            return "";
        if (steps) log.warning("init");

        //	Check Product
        int M_Product_ID = 0;
        if (column.equals("M_Product_ID"))
            M_Product_ID = ((Integer)value).intValue();
        else
            M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
        int C_Charge_ID = 0;
        if (column.equals("C_Charge_ID"))
            C_Charge_ID = ((Integer)value).intValue();
        else
            C_Charge_ID = Env.getContextAsInt(ctx, WindowNo, "C_Charge_ID");
        log.fine("Product=" + M_Product_ID + ", C_Charge_ID=" + C_Charge_ID);
        if (M_Product_ID == 0 && C_Charge_ID == 0)
            return amt(ctx, WindowNo, mTab, mField, value);		//

        //	Check Partner Location
        int shipC_BPartner_Location_ID = 0;
        if (column.equals("C_BPartner_Location_ID"))
            shipC_BPartner_Location_ID = ((Integer)value).intValue();
        else
            shipC_BPartner_Location_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_Location_ID");
        if (shipC_BPartner_Location_ID == 0)
            return amt(ctx, WindowNo, mTab, mField, value);		//
        log.fine("Ship BP_Location=" + shipC_BPartner_Location_ID);

        //
        Timestamp billDate = Env.getContextAsDate(ctx, WindowNo, "DateOrdered");
        log.fine("Bill Date=" + billDate);

        Timestamp shipDate = Env.getContextAsDate(ctx, WindowNo, "DatePromised");
        log.fine("Ship Date=" + shipDate);

        int AD_Org_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Org_ID");
        log.fine("Org=" + AD_Org_ID);

        int M_Warehouse_ID = Env.getContextAsInt(ctx, WindowNo, "M_Warehouse_ID");
        log.fine("Warehouse=" + M_Warehouse_ID);

        int billC_BPartner_Location_ID = Env.getContextAsInt(ctx, WindowNo, "Bill_Location_ID");
        if (billC_BPartner_Location_ID == 0)
            billC_BPartner_Location_ID = shipC_BPartner_Location_ID;
        log.fine("Bill BP_Location=" + billC_BPartner_Location_ID);

        //
        int C_Tax_ID = Tax.get (ctx, M_Product_ID, C_Charge_ID, billDate, shipDate,
                AD_Org_ID, M_Warehouse_ID, billC_BPartner_Location_ID, shipC_BPartner_Location_ID,
                "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx")));

        // Xpande. Gabriel Vila.
        // Seteos de tasa de impuesto segun condiciones.
        // Si el socio de negocio es literal E, entonces todos sus productos deben ir con la tasa de impuesto para Literal E
        boolean esLiteralE = false;
        int cBPartnerID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
        if (cBPartnerID > 0){
            MBPartner partner = new MBPartner(ctx, cBPartnerID, null);
            if (partner.get_ValueAsBoolean("LiteralE")){
                esLiteralE = true;
                // Obtengo ID de tasa de impuesto para Literal E desde coniguración comercial
                String sql = " select coalesce(literale_tax_id,0) as literale_tax_id from z_comercialconfig where value ='General' ";
                int taxLiteralE_ID = DB.getSQLValueEx(null, sql);
                if (taxLiteralE_ID > 0){
                    C_Tax_ID = taxLiteralE_ID;
                }
            }
        }
        // Si no es Literal E, para invoices compra/venta en Retail, puede suceder que el producto tenga un impuesto especial de compra/venta.
        if (!esLiteralE){
            // Para ordenes de compra en Retail, puede suceder que el producto tenga un impuesto especial de compra.
            // Por lo tanto aca considero esta posibilidad.
            if ("N".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"))){
                MProduct product = new MProduct(ctx, M_Product_ID, null);
                if (product.get_ValueAsInt("C_TaxCategory_ID_2") > 0){
                    //MTax taxAux = TaxUtils.getLastTaxByCategory(ctx, product.get_ValueAsInt("C_TaxCategory_ID_2"), null);
                    MTax taxAux = TaxUtils.getDefaultTaxByCategory(ctx, product.get_ValueAsInt("C_TaxCategory_ID_2"), null);
                    if ((taxAux != null) && (taxAux.get_ID() > 0)){
                        C_Tax_ID = taxAux.get_ID();
                    }
                }
            }
        }
        // Xpande

        log.info("Tax ID=" + C_Tax_ID);
        //
        if (C_Tax_ID == 0)
            mTab.fireDataStatusEEvent(CLogger.retrieveError());
        else
            mTab.setValue("C_Tax_ID", new Integer(C_Tax_ID));
        //
        if (steps) log.warning("fini");
        return amt(ctx, WindowNo, mTab, mField, value);
    }	//	tax


    /**
     *	Order Line - Amount.
     *		- called from QtyOrdered, Discount and PriceActual
     *		- calculates Discount or Actual Amount
     *		- calculates LineNetAmt
     *		- enforces PriceLimit
     *  @param ctx context
     *  @param WindowNo current Window No
     *  @param mTab Grid Tab
     *  @param mField Grid Field
     *  @param value New Value
     *  @return null or error message
     */
    public String amt (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
        if (isCalloutActive() || value == null)
            return "";

        if (steps) log.warning("init");
        int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
        int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
        int adOrgID = Env.getContextAsInt(ctx, WindowNo, "AD_Org_ID");

        int cOrderID = ((Integer)mTab.getValue("C_Order_ID")).intValue();
        if (cOrderID <= 0) {
            return "No se obtuvo ID interno de Orden";
        }

        MOrder order = new MOrder(ctx, cOrderID, null);
        int M_PriceList_ID = order.getM_PriceList_ID();
        int StdPrecision = MPriceList.getPricePrecision(ctx, M_PriceList_ID);

        BigDecimal QtyEntered, QtyOrdered, PriceEntered, PriceActual, PriceLimit, Discount, PriceList, Discount2;
        //	get values
        QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
        QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
        log.fine("QtyEntered=" + QtyEntered + ", Ordered=" + QtyOrdered + ", UOM=" + C_UOM_To_ID);
        //
        PriceEntered = (BigDecimal)mTab.getValue("PriceEntered");
        PriceActual = (BigDecimal)mTab.getValue("PriceActual");
        Discount = (BigDecimal)mTab.getValue("Discount");
        Discount2 = (BigDecimal)mTab.getValue("Discount2");
        PriceLimit = (BigDecimal)mTab.getValue("PriceLimit");
        PriceList = (BigDecimal)mTab.getValue("PriceList");
        log.fine("PriceList=" + PriceList + ", Limit=" + PriceLimit + ", Precision=" + StdPrecision);
        log.fine("PriceEntered=" + PriceEntered + ", Actual=" + PriceActual + ", Discount=" + Discount  + ", Discount2=" + Discount2);


        // Xpande.Gabriel Vila.
        // Obtengo tasa multiplicadora por conversión entre moneda de lista y moneda de compra
        BigDecimal multiplyRate = null;
        if (mTab.getValue("C_Order_ID") != null){
            multiplyRate = (BigDecimal) order.get_Value("MultiplyRate");
            if (!order.isSOTrx()){
                if ((multiplyRate != null) && (multiplyRate.compareTo(Env.ZERO) != 0)){
                    if (!mTab.getValueAsBoolean("IsConverted")){
                        BigDecimal priceLimit = ((BigDecimal)mTab.getValue("PriceLimit")).multiply(multiplyRate).setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
                        PriceLimit = priceLimit;
                        mTab.setValue("IsConverted", true);
                    }
                }
            }
        }
        // Xpande.

        //		No Product
        if (M_Product_ID == 0)
        {
            // if price change sync price actual and entered
            // else ignore
            if (mField.getColumnName().equals("PriceActual"))
            {
                PriceEntered = (BigDecimal) value;
                mTab.setValue("PriceEntered", value);
            }
            else if (mField.getColumnName().equals("PriceEntered"))
            {
                PriceActual = (BigDecimal) value;
                mTab.setValue("PriceActual", value);
            }
        }
        //	Product Qty changed - recalc price
        else if ((mField.getColumnName().equals("QtyOrdered")
                || mField.getColumnName().equals("QtyEntered")
                || mField.getColumnName().equals("C_UOM_ID")
                || mField.getColumnName().equals("M_Product_ID"))
                    && !"N".equals(Env.getContext(ctx, WindowNo, "DiscountSchema")))
        {
            int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
            if (mField.getColumnName().equals("QtyEntered"))
                QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
                        C_UOM_To_ID, QtyEntered);
            if (QtyOrdered == null)
                QtyOrdered = QtyEntered;
            boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
            org.xpande.comercial.model.MProductPricing pp = new org.xpande.comercial.model.MProductPricing (M_Product_ID, C_BPartner_ID, adOrgID,
                    order.getDateOrdered(), QtyOrdered, IsSOTrx, null);
            pp.setM_PriceList_ID(M_PriceList_ID);
            pp.setForcedPrecision(StdPrecision);
            int M_PriceList_Version_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_Version_ID");
            pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
            Timestamp date = (Timestamp)mTab.getValue("DateOrdered");
            pp.setPriceDate(date);

            BigDecimal manualDiscount = (BigDecimal)mTab.getValue("DiscountMan");
            if (manualDiscount == null) manualDiscount = Env.ZERO;

            //
            PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
                    C_UOM_To_ID, pp.getPriceStd());
            if (PriceEntered == null)
                PriceEntered = pp.getPriceStd();
            //
            log.fine("QtyChanged -> PriceActual=" + pp.getPriceStd()
                    + ", PriceEntered=" + PriceEntered + ", Discount=" + pp.getDiscount());
            PriceActual = pp.getPriceStd();
            mTab.setValue("PriceActual", pp.getPriceStd());
            mTab.setValue("Discount", pp.getDiscount());
            mTab.setValue("PriceEntered", PriceEntered);
            Env.setContext(ctx, WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");

            /*
            // Xpande. Gabriel Vila. 02/01/2010.
            // Calculo descuentos para esta fecha documento - socio de negocio - producto.
            ProductPricesInfo ppi = DiscountUtils.setDiscountPrices(ctx, order.getDateOrdered(), order.getAD_Client_ID(), order.getAD_Org_ID(), C_BPartner_ID, M_Product_ID,
                    manualDiscount, (BigDecimal) mTab.getValue("PriceList"), (BigDecimal) mTab.getValue("PriceList"), StdPrecision, null);

            if (ppi != null){
                mTab.setValue("PriceActual", ppi.getPriceFinal());
                //mTab.setValue("PriceEntered", ppi.getPriceFinal());
                mTab.setValue("FlatDiscount", ppi.getFlatDiscount());
                mTab.setValue("DiscountPauta", ppi.getTotalPautaDiscounts());
                mTab.setValue("Discount", ppi.getTotalDiscounts());
            }

            PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
                    C_UOM_To_ID, ppi.getPriceFinal());
            if (PriceEntered == null)
                PriceEntered = ppi.getPriceFinal();

            mTab.setValue("PriceEntered", PriceEntered);
            // Fin Xpande.
             */

            if ((order != null) && (!order.isSOTrx())){
                if ((multiplyRate != null) && (multiplyRate.compareTo(Env.ZERO) != 0)){
                    if (!mTab.getValueAsBoolean("IsConverted")){
                        BigDecimal priceActual = ((BigDecimal)mTab.getValue("PriceActual")).multiply(multiplyRate).setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
                        BigDecimal priceEntered = ((BigDecimal)mTab.getValue("PriceEntered")).multiply(multiplyRate).setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
                        mTab.setValue("PriceActual", priceActual);
                        mTab.setValue("PriceEntered", priceEntered);
                        mTab.setValue("IsConverted", true);
                    }
                }
            }
        }
        else if (mField.getColumnName().equals("PriceActual"))
        {
            PriceActual = (BigDecimal)value;
            PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
                    C_UOM_To_ID, PriceActual);
            if (PriceEntered == null)
                PriceEntered = PriceActual;
            //
            log.fine("PriceActual=" + PriceActual
                    + " -> PriceEntered=" + PriceEntered);
            mTab.setValue("PriceEntered", PriceEntered);

            if ((order != null) && (!order.isSOTrx())){
                if ((multiplyRate != null) && (multiplyRate.compareTo(Env.ZERO) != 0)){
                    if (!mTab.getValueAsBoolean("IsConverted")){
                        BigDecimal priceEntered = ((BigDecimal)mTab.getValue("PriceEntered")).multiply(multiplyRate).setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
                        mTab.setValue("PriceEntered", priceEntered);
                        mTab.setValue("IsConverted", true);
                    }
                }
            }


        }
        else if (mField.getColumnName().equals("PriceEntered"))
        {
            PriceEntered = (BigDecimal)value;
            PriceActual = MUOMConversion.convertProductTo (ctx, M_Product_ID,
                    C_UOM_To_ID, PriceEntered);
            if (PriceActual == null)
                PriceActual = PriceEntered;
            //
            log.fine("PriceEntered=" + PriceEntered
                    + " -> PriceActual=" + PriceActual);
            mTab.setValue("PriceActual", PriceActual);

            if ((order != null) && (!order.isSOTrx())){
                if ((multiplyRate != null) && (multiplyRate.compareTo(Env.ZERO) != 0)){
                    if (!mTab.getValueAsBoolean("IsConverted")){
                        BigDecimal priceActual = ((BigDecimal)mTab.getValue("PriceActual")).multiply(multiplyRate).setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
                        mTab.setValue("PriceActual", priceActual);
                        mTab.setValue("IsConverted", true);
                    }
                }
            }

        }
        // Xpande. Gabriel Vila. 02/01/2020.
        // Aplico descuento manual.
        else if (mField.getColumnName().equalsIgnoreCase("DiscountMan")){

            BigDecimal manualDiscount = (BigDecimal) value;
            if (manualDiscount == null) manualDiscount = Env.ZERO;

            // Calculo descuentos para esta fecha documento - socio de negocio - producto.
            ProductPricesInfo ppi = DiscountUtils.setDiscountPrices(ctx, order.getDateOrdered(), order.getAD_Client_ID(), order.getAD_Org_ID(), order.getC_BPartner_ID(), M_Product_ID,
                    manualDiscount, (BigDecimal) mTab.getValue("PriceList"), (BigDecimal) mTab.getValue("PriceList"), StdPrecision, null);

            if (ppi != null){
                mTab.setValue("PriceActual", ppi.getPriceFinal());
                mTab.setValue("FlatDiscount", ppi.getFlatDiscount());
                mTab.setValue("DiscountPauta", ppi.getTotalPautaDiscounts());
                //mTab.setValue("Discount", ppi.getTotalDiscounts());
            }

            PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
                    C_UOM_To_ID, ppi.getPriceFinal());
            if (PriceEntered == null)
                PriceEntered = ppi.getPriceFinal();

            mTab.setValue("PriceEntered", PriceEntered);
        }

        //  Discount entered - Calculate Actual/Entered
        if (mField.getColumnName().equals("Discount"))
        {
            if ( PriceList.doubleValue() != 0 )
                PriceActual = new BigDecimal ((100.0 - Discount.doubleValue()) / 100.0 * PriceList.doubleValue());
            if (PriceActual.scale() > StdPrecision)
                PriceActual = PriceActual.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
            PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
                    C_UOM_To_ID, PriceActual);
            if (PriceEntered == null)
                PriceEntered = PriceActual;
            mTab.setValue("PriceActual", PriceActual);
            mTab.setValue("PriceEntered", PriceEntered);

            if ((order != null) && (!order.isSOTrx())){
                if ((multiplyRate != null) && (multiplyRate.compareTo(Env.ZERO) != 0)){
                    if (!mTab.getValueAsBoolean("IsConverted")){
                        BigDecimal priceActual = ((BigDecimal)mTab.getValue("PriceActual")).multiply(multiplyRate).setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
                        BigDecimal priceEntered = ((BigDecimal)mTab.getValue("PriceEntered")).multiply(multiplyRate).setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
                        mTab.setValue("PriceActual", priceActual);
                        mTab.setValue("PriceEntered", priceEntered);
                        mTab.setValue("IsConverted", true);
                    }
                }
            }
        }
        //	calculate Discount
        else
        {
            if (PriceList.intValue() == 0)
                Discount = Env.ZERO;
            else
                Discount = new BigDecimal ((PriceList.doubleValue() - PriceActual.doubleValue()) / PriceList.doubleValue() * 100.0);
            if (Discount.scale() > 2)
                Discount = Discount.setScale(2, BigDecimal.ROUND_HALF_UP);
            mTab.setValue("Discount", Discount);
        }
        log.fine("PriceEntered=" + PriceEntered + ", Actual=" + PriceActual + ", Discount=" + Discount  + ", Discount2=" + Discount2);

        //	Check PriceLimit
        String epl = Env.getContext(ctx, WindowNo, "EnforcePriceLimit");
        boolean enforce = Env.isSOTrx(ctx, WindowNo) && epl != null && epl.equals("Y");
        if (enforce && MRole.getDefault().isOverwritePriceLimit())
            enforce = false;
        //	Check Price Limit?
        if (enforce && PriceLimit.doubleValue() != 0.0
                && PriceActual.compareTo(PriceLimit) < 0)
        {
            PriceActual = PriceLimit;
            PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
                    C_UOM_To_ID, PriceLimit);
            if (PriceEntered == null)
                PriceEntered = PriceLimit;
            log.fine("(under) PriceEntered=" + PriceEntered + ", Actual" + PriceLimit);
            mTab.setValue ("PriceActual", PriceLimit);
            mTab.setValue ("PriceEntered", PriceEntered);
            mTab.fireDataStatusEEvent ("UnderLimitPrice", "", false);
            //	Repeat Discount calc
            if (PriceList.intValue() != 0)
            {
                Discount = new BigDecimal ((PriceList.doubleValue () - PriceActual.doubleValue ()) / PriceList.doubleValue () * 100.0);
                if (Discount.scale () > 2)
                    Discount = Discount.setScale (2, BigDecimal.ROUND_HALF_UP);
                mTab.setValue ("Discount", Discount);
            }
        }

        //	Line Net Amt
        BigDecimal LineNetAmt = QtyOrdered.multiply(PriceActual);
        if (LineNetAmt.scale() > StdPrecision)
            LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
        log.info("LineNetAmt=" + LineNetAmt);
        mTab.setValue("LineNetAmt", LineNetAmt);


        // XPande. Gabriel Vila. Seteo un campo con el valor del subtotal de la linea, contemplando listas con impuesto incluído.
        Integer taxID = (Integer)mTab.getValue("C_Tax_ID");
        BigDecimal taxAmt = Env.ZERO;
        if (taxID != null)
        {
            int C_Tax_ID = taxID.intValue();
            MTax tax = new MTax (ctx, C_Tax_ID, null);
            taxAmt = tax.calculateTax(LineNetAmt, order.isTaxIncluded(), StdPrecision);
            mTab.setValue("TaxAmt", taxAmt);
        }
        if (!order.isTaxIncluded()){
            //	Add it up
            //mTab.setValue("LineTotalAmt", LineNetAmt.add(taxAmt));
            mTab.setValue("AmtSubtotal", LineNetAmt);
        }
        else{
            //mTab.setValue("LineTotalAmt", LineNetAmt);
            mTab.setValue("AmtSubtotal", LineNetAmt.subtract(taxAmt));
        }
        // Fin Xpande.


        //
        return "";
    }	//	amt

    /**
     *	Order Line - Quantity.
     *		- called from C_UOM_ID, QtyEntered, QtyOrdered
     *		- enforces qty UOM relationship
     *  @param ctx context
     *  @param WindowNo current Window No
     *  @param mTab Grid Tab
     *  @param mField Grid Field
     *  @param value New Value
     *  @return null or error message
     */
    public String qty (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
        if (isCalloutActive() || value == null)
            return "";
        int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
        if (steps) log.warning("init - M_Product_ID=" + M_Product_ID + " - " );
        BigDecimal QtyOrdered = Env.ZERO;
        BigDecimal QtyEntered, PriceActual, PriceEntered;

        //	No Product
        if (M_Product_ID == 0)
        {
            QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
            QtyOrdered = QtyEntered;
            mTab.setValue("QtyOrdered", QtyOrdered);
        }
        //	UOM Changed - convert from Entered -> Product
        else if (mField.getColumnName().equals("C_UOM_ID"))
        {
            int C_UOM_To_ID = ((Integer)value).intValue();
            QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
            BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
            if (QtyEntered.compareTo(QtyEntered1) != 0)
            {
                log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
                        + "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
                QtyEntered = QtyEntered1;
                mTab.setValue("QtyEntered", QtyEntered);
            }
            QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
                    C_UOM_To_ID, QtyEntered);
            if (QtyOrdered == null)
                QtyOrdered = QtyEntered;
            boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
            PriceActual = (BigDecimal)mTab.getValue("PriceActual");
            PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
                    C_UOM_To_ID, PriceActual);
            if (PriceEntered == null)
                PriceEntered = PriceActual;
            log.fine("UOM=" + C_UOM_To_ID
                    + ", QtyEntered/PriceActual=" + QtyEntered + "/" + PriceActual
                    + " -> " + conversion
                    + " QtyOrdered/PriceEntered=" + QtyOrdered + "/" + PriceEntered);
            Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
            mTab.setValue("QtyOrdered", QtyOrdered);
            mTab.setValue("PriceEntered", PriceEntered);
        }
        //	QtyEntered changed - calculate QtyOrdered
        else if (mField.getColumnName().equals("QtyEntered"))
        {
            int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
            QtyEntered = (BigDecimal)value;
            BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
            if (QtyEntered.compareTo(QtyEntered1) != 0)
            {
                log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
                        + "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
                QtyEntered = QtyEntered1;
                mTab.setValue("QtyEntered", QtyEntered);
            }
            QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
                    C_UOM_To_ID, QtyEntered);
            if (QtyOrdered == null)
                QtyOrdered = QtyEntered;
            boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
            log.fine("UOM=" + C_UOM_To_ID
                    + ", QtyEntered=" + QtyEntered
                    + " -> " + conversion
                    + " QtyOrdered=" + QtyOrdered);
            Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
            mTab.setValue("QtyOrdered", QtyOrdered);
        }
        //	QtyOrdered changed - calculate QtyEntered (should not happen)
        else if (mField.getColumnName().equals("QtyOrdered"))
        {
            int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
            QtyOrdered = (BigDecimal)value;
            int precision = MProduct.get(ctx, M_Product_ID).getUOMPrecision();
            BigDecimal QtyOrdered1 = QtyOrdered.setScale(precision, BigDecimal.ROUND_HALF_UP);
            if (QtyOrdered.compareTo(QtyOrdered1) != 0)
            {
                log.fine("Corrected QtyOrdered Scale "
                        + QtyOrdered + "->" + QtyOrdered1);
                QtyOrdered = QtyOrdered1;
                mTab.setValue("QtyOrdered", QtyOrdered);
            }
            QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID,
                    C_UOM_To_ID, QtyOrdered);
            if (QtyEntered == null)
                QtyEntered = QtyOrdered;
            boolean conversion = QtyOrdered.compareTo(QtyEntered) != 0;
            log.fine("UOM=" + C_UOM_To_ID
                    + ", QtyOrdered=" + QtyOrdered
                    + " -> " + conversion
                    + " QtyEntered=" + QtyEntered);
            Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
            mTab.setValue("QtyEntered", QtyEntered);
        }
        else
        {
            //	QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
            QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
        }

        //	Storage
        if (M_Product_ID != 0
                && Env.isSOTrx(ctx, WindowNo)
                && QtyOrdered.signum() > 0)		//	no negative (returns)
        {
            MProduct product = MProduct.get (ctx, M_Product_ID);
            if (product.isStocked())
            {
                int M_Warehouse_ID = Env.getContextAsInt(ctx, WindowNo, "M_Warehouse_ID");
                int M_AttributeSetInstance_ID = Env.getContextAsInt(ctx, WindowNo, "M_AttributeSetInstance_ID");
                BigDecimal available = MStorage.getQtyAvailable
                        (M_Warehouse_ID, M_Product_ID, M_AttributeSetInstance_ID, null);
                if (available == null)
                    available = Env.ZERO;

                // Xpande. Gabriel Vila. 30/09/2019.
                // Comento warnings por stock
                if (available.signum() == 0){
                    // mTab.fireDataStatusEEvent ("NoQtyAvailable", "0", false);
                }
                else if (available.compareTo(QtyOrdered) < 0){
                    // mTab.fireDataStatusEEvent ("InsufficientQtyAvailable", available.toString(), false);
                }
                // Fin Xpande.
                else
                {
                    Integer C_OrderLine_ID = (Integer)mTab.getValue("C_OrderLine_ID");
                    if (C_OrderLine_ID == null)
                        C_OrderLine_ID = new Integer(0);
                    BigDecimal notReserved = MOrderLine.getNotReserved(ctx,
                            M_Warehouse_ID, M_Product_ID, M_AttributeSetInstance_ID,
                            C_OrderLine_ID.intValue());
                    if (notReserved == null)
                        notReserved = Env.ZERO;
                    BigDecimal total = available.subtract(notReserved);
                    if (total.compareTo(QtyOrdered) < 0)
                    {
                        // Xpande. Gabriel Vila. 30/09/2019.
                        // Comento warnings por stock

                        /*
                        String info = Msg.parseTranslation(ctx, "@QtyAvailable@=" + available
                                + "  -  @QtyNotReserved@=" + notReserved + "  =  " + total);
                        mTab.fireDataStatusEEvent ("InsufficientQtyAvailable",
                                info, false);
                        */

                        // Fin Xpande
                    }
                }
            }
        }
        //
        return "";
    }	//	qty


    /***
     * Al ingresar código de barras, o codigo de producto del proveedor, o el producto directamente,
     * se deben setear los otros dos valores asociados.
     * Xpande. Created by Gabriel Vila on 6/25/17.
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     * @return
     */
    public String upcVendProdNoProduct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {

        if (isCalloutActive()) return "";

        if ((value == null) || (value.toString().trim().equalsIgnoreCase(""))){
            mTab.setValue("UPC", null);
            mTab.setValue("VendorProductNo", null);
            mTab.setValue("M_Product_ID", null);
            return "";
        }

        boolean isSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));

        int cBPartnerID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");

        String column = mField.getColumnName();

        if (column.equalsIgnoreCase("UPC")){
            MZProductoUPC pupc = MZProductoUPC.getByUPC(ctx, value.toString().trim(), null);
            if ((pupc != null) && (pupc.get_ID() > 0)){

                MProduct prod = (MProduct) pupc.getM_Product();
                mTab.setValue("M_Product_ID", prod.get_ID());
            }
            else{
                mTab.setValue("VendorProductNo", null);
                mTab.setValue("M_Product_ID", null);
                mTab.fireDataStatusEEvent ("Error", "No existe Producto con código de barras ingresado", true);
            }
        }
        else if (column.equalsIgnoreCase("VendorProductNo")){
            mTab.setValue("UPC", null);
            mTab.setValue("M_Product_ID", null);
            mTab.fireDataStatusEEvent ("Error", "No existe Producto para el código ingresado de producto del proveedor", true);
        }
        else if (column.equalsIgnoreCase("M_Product_ID")){

            int mProductID = ((Integer) value).intValue();

            MZProductoUPC pupc = MZProductoUPC.getByProduct(ctx, mProductID, null);
            if ((pupc != null) && (pupc.get_ID() > 0)){
                mTab.setValue("UPC", pupc.getUPC());
            }
            else{
                mTab.setValue("UPC", null);
            }
        }

        return "";
    }

    /**
     *	Order Header - PriceList.
     *	(used also in Invoice)
     *		- C_Currency_ID
     *		- IsTaxIncluded
     *	Window Context:
     *		- EnforcePriceLimit
     *		- StdPrecision
     *		- M_PriceList_Version_ID
     *  @param ctx context
     *  @param WindowNo current Window No
     *  @param mTab Grid Tab
     *  @param mField Grid Field
     *  @param value New Value
     *  @return null or error message
     */
    public String priceList (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
        if (isCalloutActive()) return "";

        Integer M_PriceList_ID = (Integer) mTab.getValue("M_PriceList_ID");
        if (M_PriceList_ID == null || M_PriceList_ID.intValue()== 0)
            return "";
        if (steps) log.warning("init");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT pl.IsTaxIncluded,pl.EnforcePriceLimit,pl.C_Currency_ID,c.StdPrecision,"
                + "plv.M_PriceList_Version_ID,plv.ValidFrom "
                + "FROM M_PriceList pl,C_Currency c,M_PriceList_Version plv "
                + "WHERE pl.C_Currency_ID=c.C_Currency_ID"
                + " AND pl.M_PriceList_ID=plv.M_PriceList_ID"
                + " AND pl.M_PriceList_ID=? "						//	1
                + " AND plv.ValidFrom <= ? "
                + "ORDER BY plv.ValidFrom DESC";
        //	Use newest price list - may not be future
        try
        {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, M_PriceList_ID.intValue());
            Timestamp date = new Timestamp(System.currentTimeMillis());
            if (mTab.getAD_Table_ID() == I_C_Order.Table_ID)
                date = Env.getContextAsDate(ctx, WindowNo, "DateOrdered");
            else if (mTab.getAD_Table_ID() == I_C_Invoice.Table_ID)
                date = Env.getContextAsDate(ctx, WindowNo, "DateInvoiced");
            pstmt.setTimestamp(2, date);

            rs = pstmt.executeQuery();
            if (rs.next())
            {
                //	Tax Included
                mTab.setValue("IsTaxIncluded", new Boolean("Y".equals(rs.getString(1))));
                //	Price Limit Enforce
                Env.setContext(ctx, WindowNo, "EnforcePriceLimit", rs.getString(2));
                //	Currency
                Integer ii = new Integer(rs.getInt(3));
                mTab.setValue("C_Currency_ID", ii);

                // Xpande. Gabriel Vila.
                // Seteo moneda de lista de precio.
                // Esto se debe a que puede haber compras de productos en lista UYU pero en moneda de OC USD.
                // La moneda OC se mantiene en el campo c_currency_id, y se mantiene la moneda de la lista en este
                // nuevo campo.
                mTab.setValue("C_Currency_PriceList_ID", ii);
                // Xpande.

                //	PriceList Version
                Env.setContext(ctx, WindowNo, "M_PriceList_Version_ID", rs.getInt(5));
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
            rs = null; pstmt = null;
        }
        if (steps) log.warning("fini");

        return "";
    }	//	priceList

    /**
     *	Order Header - BPartner.
     *		- M_PriceList_ID (+ Context)
     *		- C_BPartner_Location_ID
     *		- Bill_BPartner_ID/Bill_Location_ID
     *		- AD_User_ID
     *		- POReference
     *		- SO_Description
     *		- IsDiscountPrinted
     *		- InvoiceRule/DeliveryRule/PaymentRule/FreightCost/DeliveryViaRule
     *		- C_PaymentTerm_ID
     *  @param ctx      Context
     *  @param WindowNo current Window No
     *  @param mTab     Model Tab
     *  @param mField   Model Field
     *  @param value    The new value
     *  @return Error message or ""
     */
    public String bPartner (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
        Integer C_BPartner_ID = (Integer)value;
        if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0)
            return "";
        String sql = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
                + " COALESCE(p.M_PriceList_ID,g.M_PriceList_ID) AS M_PriceList_ID, p.PaymentRule,p.POReference,"
                + " p.SO_Description,p.IsDiscountPrinted,"
                + " p.InvoiceRule,p.DeliveryRule,p.FreightCostRule,DeliveryViaRule,"
                + " p.SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
                + " lship.C_BPartner_Location_ID,c.AD_User_ID,"
                + " COALESCE(p.PO_PriceList_ID,g.PO_PriceList_ID) AS PO_PriceList_ID, p.PaymentRulePO,p.PO_PaymentTerm_ID,"
                + " lbill.C_BPartner_Location_ID AS Bill_Location_ID, p.SOCreditStatus, "
                + " p.SalesRep_ID "
                + "FROM C_BPartner p"
                + " INNER JOIN C_BP_Group g ON (p.C_BP_Group_ID=g.C_BP_Group_ID)"
                + " LEFT OUTER JOIN C_BPartner_Location lbill ON (p.C_BPartner_ID=lbill.C_BPartner_ID AND lbill.IsBillTo='Y' AND lbill.IsActive='Y')"
                + " LEFT OUTER JOIN C_BPartner_Location lship ON (p.C_BPartner_ID=lship.C_BPartner_ID AND lship.IsShipTo='Y' AND lship.IsActive='Y')"
                + " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID) "
                + "WHERE p.C_BPartner_ID=? AND p.IsActive='Y'";		//	#1

        boolean IsSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, C_BPartner_ID.intValue());
            rs = pstmt.executeQuery();
            if (rs.next())
            {
                // Sales Rep - If BP has a default SalesRep then default it
                Integer salesRep = rs.getInt("SalesRep_ID");
                if (IsSOTrx && salesRep != 0 )
                {
                    mTab.setValue("SalesRep_ID", salesRep);
                }

                //	PriceList (indirect: IsTaxIncluded & Currency)
                Integer ii = new Integer(rs.getInt(IsSOTrx ? "M_PriceList_ID" : "PO_PriceList_ID"));
                if (!rs.wasNull())
                    mTab.setValue("M_PriceList_ID", ii);
                else
                {	//	get default PriceList
                    int i = Env.getContextAsInt(ctx, "#M_PriceList_ID");
                    if (i != 0)
                        mTab.setValue("M_PriceList_ID", new Integer(i));
                }

                //	Bill-To
                mTab.setValue("Bill_BPartner_ID", C_BPartner_ID);
                int bill_Location_ID = rs.getInt("Bill_Location_ID");
                if (bill_Location_ID == 0)
                    mTab.setValue("Bill_Location_ID", null);
                else
                    mTab.setValue("Bill_Location_ID", new Integer(bill_Location_ID));
                // Ship-To Location
                int shipTo_ID = rs.getInt("C_BPartner_Location_ID");
                //	overwritten by InfoBP selection - works only if InfoWindow
                //	was used otherwise creates error (uses last value, may belong to different BP)
                if (C_BPartner_ID.toString().equals(Env.getContext(ctx, WindowNo, Env.TAB_INFO, "C_BPartner_ID")))
                {
                    String loc = Env.getContext(ctx, WindowNo, Env.TAB_INFO, "C_BPartner_Location_ID");
                    if (loc.length() > 0){
                        if ((Integer.parseInt(loc)) > 0 ){
                            shipTo_ID = Integer.parseInt(loc);
                        }
                    }
                }
                if (shipTo_ID == 0)
                    mTab.setValue("C_BPartner_Location_ID", null);
                else
                    mTab.setValue("C_BPartner_Location_ID", new Integer(shipTo_ID));

                //	Contact - overwritten by InfoBP selection
                int contID = rs.getInt("AD_User_ID");
                if (C_BPartner_ID.toString().equals(Env.getContext(ctx, WindowNo, Env.TAB_INFO, "C_BPartner_ID")))
                {
                    String cont = Env.getContext(ctx, WindowNo, Env.TAB_INFO, "AD_User_ID");
                    if (cont.length() > 0)
                        contID = Integer.parseInt(cont);
                }
                if (contID == 0)
                    mTab.setValue("AD_User_ID", null);
                else
                {
                    mTab.setValue("AD_User_ID", new Integer(contID));
                    mTab.setValue("Bill_User_ID", new Integer(contID));
                }

                //	CreditAvailable
                if (IsSOTrx)
                {
                    double CreditLimit = rs.getDouble("SO_CreditLimit");
                    String SOCreditStatus = rs.getString("SOCreditStatus");
                    if (CreditLimit != 0)
                    {
                        double CreditAvailable = rs.getDouble("CreditAvailable");
                        if (!rs.wasNull() && CreditAvailable < 0)
                            mTab.fireDataStatusEEvent("CreditLimitOver",
                                    DisplayType.getNumberFormat(DisplayType.Amount).format(CreditAvailable),
                                    false);
                    }
                }

                //	PO Reference
                String s = rs.getString("POReference");
                if (s != null && s.length() != 0)
                    mTab.setValue("POReference", s);
                // should not be reset to null if we entered already value! VHARCQ, accepted YS makes sense that way
                // TODO: should get checked and removed if no longer needed!
				/*else
					mTab.setValue("POReference", null);*/

                //	SO Description
                s = rs.getString("SO_Description");
                if (s != null && s.trim().length() != 0)
                    mTab.setValue("Description", s);
                //	IsDiscountPrinted
                s = rs.getString("IsDiscountPrinted");
                if (s != null && s.length() != 0)
                    mTab.setValue("IsDiscountPrinted", s);
                else
                    mTab.setValue("IsDiscountPrinted", "N");

                //	Defaults, if not Walkin Receipt or Walkin Invoice
                String OrderType = Env.getContext(ctx, WindowNo, "OrderType");
                mTab.setValue("InvoiceRule", X_C_Order.INVOICERULE_AfterDelivery);
                mTab.setValue("DeliveryRule", X_C_Order.DELIVERYRULE_Availability);
                mTab.setValue("PaymentRule", X_C_Order.PAYMENTRULE_OnCredit);
                if (OrderType.equals(MOrder.DocSubTypeSO_Prepay))
                {
                    mTab.setValue("InvoiceRule", X_C_Order.INVOICERULE_Immediate);
                    mTab.setValue("DeliveryRule", X_C_Order.DELIVERYRULE_AfterReceipt);
                }
                else if (OrderType.equals(MOrder.DocSubTypeSO_POS))	//  for POS
                    mTab.setValue("PaymentRule", X_C_Order.PAYMENTRULE_Cash);
                else
                {
                    //	PaymentRule
                    s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
                    if (s != null && s.length() != 0)
                    {
                        if (s.equals("B"))				//	No Cache in Non POS
                            s = "P";
                        if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
                            s = "P";										//  Payment Term
                        mTab.setValue("PaymentRule", s);
                    }
                    //	Payment Term
                    ii = new Integer(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
                    if (!rs.wasNull())
                        mTab.setValue("C_PaymentTerm_ID", ii);
                    //	InvoiceRule
                    s = rs.getString("InvoiceRule");
                    if (s != null && s.length() != 0)
                        mTab.setValue("InvoiceRule", s);
                    //	DeliveryRule
                    s = rs.getString("DeliveryRule");
                    if (s != null && s.length() != 0)
                        mTab.setValue("DeliveryRule", s);
                    //	FreightCostRule
                    s = rs.getString("FreightCostRule");
                    if (s != null && s.length() != 0)
                        mTab.setValue("FreightCostRule", s);
                    //	DeliveryViaRule
                    s = rs.getString("DeliveryViaRule");
                    if (s != null && s.length() != 0)
                        mTab.setValue("DeliveryViaRule", s);
                }
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
            rs = null; pstmt = null;
        }
        return "";
    }	//	bPartner

}
