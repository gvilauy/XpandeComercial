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

/** Generated Model for Z_ReservaVtaLin
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_ReservaVtaLin extends PO implements I_Z_ReservaVtaLin, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200802L;

    /** Standard Constructor */
    public X_Z_ReservaVtaLin (Properties ctx, int Z_ReservaVtaLin_ID, String trxName)
    {
      super (ctx, Z_ReservaVtaLin_ID, trxName);
      /** if (Z_ReservaVtaLin_ID == 0)
        {
			setC_OrderLine_ID (0);
			setC_UOM_ID (0);
			setM_Product_ID (0);
			setQtyAvailable (Env.ZERO);
			setQtyAvailableEnt (Env.ZERO);
			setQtyEntered (Env.ZERO);
			setQtyOrdered (Env.ZERO);
			setQtyReserved (Env.ZERO);
			setQtyReservedEnt (Env.ZERO);
			setUomMultiplyRate (Env.ZERO);
			setZ_ReservaVta_ID (0);
			setZ_ReservaVtaLin_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_ReservaVtaLin (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_ReservaVtaLin[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_OrderLine getC_OrderLine() throws RuntimeException
    {
		return (I_C_OrderLine)MTable.get(getCtx(), I_C_OrderLine.Table_Name)
			.getPO(getC_OrderLine_ID(), get_TrxName());	}

	/** Set Sales Order Line.
		@param C_OrderLine_ID 
		Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID)
	{
		if (C_OrderLine_ID < 1) 
			set_Value (COLUMNNAME_C_OrderLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_OrderLine_ID, Integer.valueOf(C_OrderLine_ID));
	}

	/** Get Sales Order Line.
		@return Sales Order Line
	  */
	public int getC_OrderLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_OrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_UOM getC_UOM() throws RuntimeException
    {
		return (I_C_UOM)MTable.get(getCtx(), I_C_UOM.Table_Name)
			.getPO(getC_UOM_ID(), get_TrxName());	}

	/** Set UOM.
		@param C_UOM_ID 
		Unit of Measure
	  */
	public void setC_UOM_ID (int C_UOM_ID)
	{
		if (C_UOM_ID < 1) 
			set_Value (COLUMNNAME_C_UOM_ID, null);
		else 
			set_Value (COLUMNNAME_C_UOM_ID, Integer.valueOf(C_UOM_ID));
	}

	/** Get UOM.
		@return Unit of Measure
	  */
	public int getC_UOM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_UOM_ID);
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

	/** Set Available Quantity.
		@param QtyAvailable 
		Available Quantity (On Hand - Reserved)
	  */
	public void setQtyAvailable (BigDecimal QtyAvailable)
	{
		set_Value (COLUMNNAME_QtyAvailable, QtyAvailable);
	}

	/** Get Available Quantity.
		@return Available Quantity (On Hand - Reserved)
	  */
	public BigDecimal getQtyAvailable () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyAvailable);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set QtyAvailableEnt.
		@param QtyAvailableEnt 
		Cantidad de unidades disponibles expresadas en unidad de medida del registro
	  */
	public void setQtyAvailableEnt (BigDecimal QtyAvailableEnt)
	{
		set_Value (COLUMNNAME_QtyAvailableEnt, QtyAvailableEnt);
	}

	/** Get QtyAvailableEnt.
		@return Cantidad de unidades disponibles expresadas en unidad de medida del registro
	  */
	public BigDecimal getQtyAvailableEnt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyAvailableEnt);
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

	/** Set Ordered Quantity.
		@param QtyOrdered 
		Ordered Quantity
	  */
	public void setQtyOrdered (BigDecimal QtyOrdered)
	{
		set_Value (COLUMNNAME_QtyOrdered, QtyOrdered);
	}

	/** Get Ordered Quantity.
		@return Ordered Quantity
	  */
	public BigDecimal getQtyOrdered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyOrdered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Reserved Quantity.
		@param QtyReserved 
		Reserved Quantity
	  */
	public void setQtyReserved (BigDecimal QtyReserved)
	{
		set_Value (COLUMNNAME_QtyReserved, QtyReserved);
	}

	/** Get Reserved Quantity.
		@return Reserved Quantity
	  */
	public BigDecimal getQtyReserved () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyReserved);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set QtyReservedEnt.
		@param QtyReservedEnt 
		Cantidad de unidades reservadas expresadas en unidad de medida del registro
	  */
	public void setQtyReservedEnt (BigDecimal QtyReservedEnt)
	{
		set_Value (COLUMNNAME_QtyReservedEnt, QtyReservedEnt);
	}

	/** Get QtyReservedEnt.
		@return Cantidad de unidades reservadas expresadas en unidad de medida del registro
	  */
	public BigDecimal getQtyReservedEnt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyReservedEnt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set UomMultiplyRate.
		@param UomMultiplyRate 
		Factor de conversión de multiplicación entre unidades de medida
	  */
	public void setUomMultiplyRate (BigDecimal UomMultiplyRate)
	{
		set_Value (COLUMNNAME_UomMultiplyRate, UomMultiplyRate);
	}

	/** Get UomMultiplyRate.
		@return Factor de conversión de multiplicación entre unidades de medida
	  */
	public BigDecimal getUomMultiplyRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_UomMultiplyRate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Immutable Universally Unique Identifier.
		@param UUID 
		Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID)
	{
		set_Value (COLUMNNAME_UUID, UUID);
	}

	/** Get Immutable Universally Unique Identifier.
		@return Immutable Universally Unique Identifier
	  */
	public String getUUID () 
	{
		return (String)get_Value(COLUMNNAME_UUID);
	}

	public I_Z_ReservaVta getZ_ReservaVta() throws RuntimeException
    {
		return (I_Z_ReservaVta)MTable.get(getCtx(), I_Z_ReservaVta.Table_Name)
			.getPO(getZ_ReservaVta_ID(), get_TrxName());	}

	/** Set Z_ReservaVta ID.
		@param Z_ReservaVta_ID Z_ReservaVta ID	  */
	public void setZ_ReservaVta_ID (int Z_ReservaVta_ID)
	{
		if (Z_ReservaVta_ID < 1) 
			set_Value (COLUMNNAME_Z_ReservaVta_ID, null);
		else 
			set_Value (COLUMNNAME_Z_ReservaVta_ID, Integer.valueOf(Z_ReservaVta_ID));
	}

	/** Get Z_ReservaVta ID.
		@return Z_ReservaVta ID	  */
	public int getZ_ReservaVta_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ReservaVta_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_ReservaVtaLin ID.
		@param Z_ReservaVtaLin_ID Z_ReservaVtaLin ID	  */
	public void setZ_ReservaVtaLin_ID (int Z_ReservaVtaLin_ID)
	{
		if (Z_ReservaVtaLin_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_ReservaVtaLin_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_ReservaVtaLin_ID, Integer.valueOf(Z_ReservaVtaLin_ID));
	}

	/** Get Z_ReservaVtaLin ID.
		@return Z_ReservaVtaLin ID	  */
	public int getZ_ReservaVtaLin_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ReservaVtaLin_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}