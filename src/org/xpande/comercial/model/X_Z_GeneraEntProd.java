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

/** Generated Model for Z_GeneraEntProd
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_GeneraEntProd extends PO implements I_Z_GeneraEntProd, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200801L;

    /** Standard Constructor */
    public X_Z_GeneraEntProd (Properties ctx, int Z_GeneraEntProd_ID, String trxName)
    {
      super (ctx, Z_GeneraEntProd_ID, trxName);
      /** if (Z_GeneraEntProd_ID == 0)
        {
			setC_UOM_ID (0);
			setEstadoGenEnt (null);
// NINGUNA
			setM_Product_ID (0);
			setQtyAvailable (Env.ZERO);
			setQtyDelivered (Env.ZERO);
			setQtyOnHand (Env.ZERO);
			setQtyOrdered (Env.ZERO);
			setQtyReserved (Env.ZERO);
			setQtyToDeliver (Env.ZERO);
			setZ_GeneraEntProd_ID (0);
			setZ_GeneraEntrega_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_GeneraEntProd (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_GeneraEntProd[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** EstadoGenEnt AD_Reference_ID=1000048 */
	public static final int ESTADOGENENT_AD_Reference_ID=1000048;
	/** NINGUNA = NINGUNA */
	public static final String ESTADOGENENT_NINGUNA = "NINGUNA";
	/** PARCIAL = PARCIAL */
	public static final String ESTADOGENENT_PARCIAL = "PARCIAL";
	/** TOTAL = TOTAL */
	public static final String ESTADOGENENT_TOTAL = "TOTAL";
	/** Set EstadoGenEnt.
		@param EstadoGenEnt 
		Estados de generación de entregas / reservas de mercaderias
	  */
	public void setEstadoGenEnt (String EstadoGenEnt)
	{

		set_Value (COLUMNNAME_EstadoGenEnt, EstadoGenEnt);
	}

	/** Get EstadoGenEnt.
		@return Estados de generación de entregas / reservas de mercaderias
	  */
	public String getEstadoGenEnt () 
	{
		return (String)get_Value(COLUMNNAME_EstadoGenEnt);
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

	/** Set Delivered Quantity.
		@param QtyDelivered 
		Delivered Quantity
	  */
	public void setQtyDelivered (BigDecimal QtyDelivered)
	{
		set_Value (COLUMNNAME_QtyDelivered, QtyDelivered);
	}

	/** Get Delivered Quantity.
		@return Delivered Quantity
	  */
	public BigDecimal getQtyDelivered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyDelivered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set On Hand Quantity.
		@param QtyOnHand 
		On Hand Quantity
	  */
	public void setQtyOnHand (BigDecimal QtyOnHand)
	{
		set_Value (COLUMNNAME_QtyOnHand, QtyOnHand);
	}

	/** Get On Hand Quantity.
		@return On Hand Quantity
	  */
	public BigDecimal getQtyOnHand () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyOnHand);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set QtyOpen.
		@param QtyOpen 
		Cantidad pendiente del documento o linea
	  */
	public void setQtyOpen (BigDecimal QtyOpen)
	{
		set_Value (COLUMNNAME_QtyOpen, QtyOpen);
	}

	/** Get QtyOpen.
		@return Cantidad pendiente del documento o linea
	  */
	public BigDecimal getQtyOpen () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyOpen);
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

	/** Set Qty to deliver.
		@param QtyToDeliver Qty to deliver	  */
	public void setQtyToDeliver (BigDecimal QtyToDeliver)
	{
		set_Value (COLUMNNAME_QtyToDeliver, QtyToDeliver);
	}

	/** Get Qty to deliver.
		@return Qty to deliver	  */
	public BigDecimal getQtyToDeliver () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyToDeliver);
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

	/** Set Z_GeneraEntProd ID.
		@param Z_GeneraEntProd_ID Z_GeneraEntProd ID	  */
	public void setZ_GeneraEntProd_ID (int Z_GeneraEntProd_ID)
	{
		if (Z_GeneraEntProd_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_GeneraEntProd_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_GeneraEntProd_ID, Integer.valueOf(Z_GeneraEntProd_ID));
	}

	/** Get Z_GeneraEntProd ID.
		@return Z_GeneraEntProd ID	  */
	public int getZ_GeneraEntProd_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_GeneraEntProd_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_GeneraEntrega getZ_GeneraEntrega() throws RuntimeException
    {
		return (I_Z_GeneraEntrega)MTable.get(getCtx(), I_Z_GeneraEntrega.Table_Name)
			.getPO(getZ_GeneraEntrega_ID(), get_TrxName());	}

	/** Set Z_GeneraEntrega ID.
		@param Z_GeneraEntrega_ID Z_GeneraEntrega ID	  */
	public void setZ_GeneraEntrega_ID (int Z_GeneraEntrega_ID)
	{
		if (Z_GeneraEntrega_ID < 1) 
			set_Value (COLUMNNAME_Z_GeneraEntrega_ID, null);
		else 
			set_Value (COLUMNNAME_Z_GeneraEntrega_ID, Integer.valueOf(Z_GeneraEntrega_ID));
	}

	/** Get Z_GeneraEntrega ID.
		@return Z_GeneraEntrega ID	  */
	public int getZ_GeneraEntrega_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_GeneraEntrega_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}