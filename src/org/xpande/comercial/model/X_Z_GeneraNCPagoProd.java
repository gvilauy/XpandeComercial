/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.										*
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.xpande.comercial.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for Z_GeneraNCPagoProd
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_GeneraNCPagoProd extends PO implements I_Z_GeneraNCPagoProd, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171025L;

    /** Standard Constructor */
    public X_Z_GeneraNCPagoProd (Properties ctx, int Z_GeneraNCPagoProd_ID, String trxName)
    {
      super (ctx, Z_GeneraNCPagoProd_ID, trxName);
      /** if (Z_GeneraNCPagoProd_ID == 0)
        {
			setAmtDtoNC (Env.ZERO);
			setC_InvoiceLine_ID (0);
			setLineNetAmt (Env.ZERO);
			setM_Product_ID (0);
			setZ_GeneraNCPagoLin_ID (0);
			setZ_GeneraNCPagoProd_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_GeneraNCPagoProd (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_Z_GeneraNCPagoProd[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AmtDtoNC.
		@param AmtDtoNC 
		Monto de linea de comprobante considerando precio con descuentos de Notas de Credito al Pago
	  */
	public void setAmtDtoNC (BigDecimal AmtDtoNC)
	{
		set_Value (COLUMNNAME_AmtDtoNC, AmtDtoNC);
	}

	/** Get AmtDtoNC.
		@return Monto de linea de comprobante considerando precio con descuentos de Notas de Credito al Pago
	  */
	public BigDecimal getAmtDtoNC () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmtDtoNC);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_InvoiceLine getC_InvoiceLine() throws RuntimeException
    {
		return (I_C_InvoiceLine)MTable.get(getCtx(), I_C_InvoiceLine.Table_Name)
			.getPO(getC_InvoiceLine_ID(), get_TrxName());	}

	/** Set Invoice Line.
		@param C_InvoiceLine_ID 
		Invoice Detail Line
	  */
	public void setC_InvoiceLine_ID (int C_InvoiceLine_ID)
	{
		if (C_InvoiceLine_ID < 1) 
			set_Value (COLUMNNAME_C_InvoiceLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_InvoiceLine_ID, Integer.valueOf(C_InvoiceLine_ID));
	}

	/** Get Invoice Line.
		@return Invoice Detail Line
	  */
	public int getC_InvoiceLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_InvoiceLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Line Amount.
		@param LineNetAmt 
		Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public void setLineNetAmt (BigDecimal LineNetAmt)
	{
		set_Value (COLUMNNAME_LineNetAmt, LineNetAmt);
	}

	/** Get Line Amount.
		@return Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public BigDecimal getLineNetAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LineNetAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_M_Product getM_Product() throws RuntimeException
    {
		return (I_M_Product)MTable.get(getCtx(), I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set PorcDtoNC.
		@param PorcDtoNC 
		Porcentaje de descuentos por Nota de Credito al pago
	  */
	public void setPorcDtoNC (BigDecimal PorcDtoNC)
	{
		set_Value (COLUMNNAME_PorcDtoNC, PorcDtoNC);
	}

	/** Get PorcDtoNC.
		@return Porcentaje de descuentos por Nota de Credito al pago
	  */
	public BigDecimal getPorcDtoNC () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PorcDtoNC);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set PriceDtoNC.
		@param PriceDtoNC 
		Precio incluyendo descuentos por motivos de Notas de Cŕedito al Pago
	  */
	public void setPriceDtoNC (BigDecimal PriceDtoNC)
	{
		set_Value (COLUMNNAME_PriceDtoNC, PriceDtoNC);
	}

	/** Get PriceDtoNC.
		@return Precio incluyendo descuentos por motivos de Notas de Cŕedito al Pago
	  */
	public BigDecimal getPriceDtoNC () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PriceDtoNC);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Price.
		@param PriceEntered 
		Price Entered - the price based on the selected/base UoM
	  */
	public void setPriceEntered (BigDecimal PriceEntered)
	{
		set_Value (COLUMNNAME_PriceEntered, PriceEntered);
	}

	/** Get Price.
		@return Price Entered - the price based on the selected/base UoM
	  */
	public BigDecimal getPriceEntered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PriceEntered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Quantity.
		@param QtyEntered 
		The Quantity Entered is based on the selected UoM
	  */
	public void setQtyEntered (BigDecimal QtyEntered)
	{
		set_Value (COLUMNNAME_QtyEntered, QtyEntered);
	}

	/** Get Quantity.
		@return The Quantity Entered is based on the selected UoM
	  */
	public BigDecimal getQtyEntered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyEntered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_Z_GeneraNCPagoLin getZ_GeneraNCPagoLin() throws RuntimeException
    {
		return (I_Z_GeneraNCPagoLin)MTable.get(getCtx(), I_Z_GeneraNCPagoLin.Table_Name)
			.getPO(getZ_GeneraNCPagoLin_ID(), get_TrxName());	}

	/** Set Z_GeneraNCPagoLin ID.
		@param Z_GeneraNCPagoLin_ID Z_GeneraNCPagoLin ID	  */
	public void setZ_GeneraNCPagoLin_ID (int Z_GeneraNCPagoLin_ID)
	{
		if (Z_GeneraNCPagoLin_ID < 1) 
			set_Value (COLUMNNAME_Z_GeneraNCPagoLin_ID, null);
		else 
			set_Value (COLUMNNAME_Z_GeneraNCPagoLin_ID, Integer.valueOf(Z_GeneraNCPagoLin_ID));
	}

	/** Get Z_GeneraNCPagoLin ID.
		@return Z_GeneraNCPagoLin ID	  */
	public int getZ_GeneraNCPagoLin_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_GeneraNCPagoLin_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_GeneraNCPagoProd ID.
		@param Z_GeneraNCPagoProd_ID Z_GeneraNCPagoProd ID	  */
	public void setZ_GeneraNCPagoProd_ID (int Z_GeneraNCPagoProd_ID)
	{
		if (Z_GeneraNCPagoProd_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_GeneraNCPagoProd_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_GeneraNCPagoProd_ID, Integer.valueOf(Z_GeneraNCPagoProd_ID));
	}

	/** Get Z_GeneraNCPagoProd ID.
		@return Z_GeneraNCPagoProd ID	  */
	public int getZ_GeneraNCPagoProd_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_GeneraNCPagoProd_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}