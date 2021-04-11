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

/** Generated Model for Z_PautaComVtaProd
 *  @author Adempiere (generated) 
 *  @version Release 3.9.1 - $Id$ */
public class X_Z_PautaComVtaProd extends PO implements I_Z_PautaComVtaProd, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210411L;

    /** Standard Constructor */
    public X_Z_PautaComVtaProd (Properties ctx, int Z_PautaComVtaProd_ID, String trxName)
    {
      super (ctx, Z_PautaComVtaProd_ID, trxName);
      /** if (Z_PautaComVtaProd_ID == 0)
        {
			setM_Product_ID (0);
			setZ_PautaComVtaProd_ID (0);
			setZ_PautaComVtaSeg_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_PautaComVtaProd (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_PautaComVtaProd[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Discount %.
		@param Discount 
		Discount in percent
	  */
	public void setDiscount (BigDecimal Discount)
	{
		set_Value (COLUMNNAME_Discount, Discount);
	}

	/** Get Discount %.
		@return Discount in percent
	  */
	public BigDecimal getDiscount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Discount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_M_Product_Category getM_Product_Category() throws RuntimeException
    {
		return (I_M_Product_Category)MTable.get(getCtx(), I_M_Product_Category.Table_Name)
			.getPO(getM_Product_Category_ID(), get_TrxName());	}

	/** Set Product Category.
		@param M_Product_Category_ID 
		Category of a Product
	  */
	public void setM_Product_Category_ID (int M_Product_Category_ID)
	{
		if (M_Product_Category_ID < 1) 
			set_Value (COLUMNNAME_M_Product_Category_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_Category_ID, Integer.valueOf(M_Product_Category_ID));
	}

	/** Get Product Category.
		@return Category of a Product
	  */
	public int getM_Product_Category_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_Category_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_Z_FamiliaProd getZ_FamiliaProd() throws RuntimeException
    {
		return (I_Z_FamiliaProd)MTable.get(getCtx(), I_Z_FamiliaProd.Table_Name)
			.getPO(getZ_FamiliaProd_ID(), get_TrxName());	}

	/** Set Z_FamiliaProd ID.
		@param Z_FamiliaProd_ID Z_FamiliaProd ID	  */
	public void setZ_FamiliaProd_ID (int Z_FamiliaProd_ID)
	{
		if (Z_FamiliaProd_ID < 1) 
			set_Value (COLUMNNAME_Z_FamiliaProd_ID, null);
		else 
			set_Value (COLUMNNAME_Z_FamiliaProd_ID, Integer.valueOf(Z_FamiliaProd_ID));
	}

	/** Get Z_FamiliaProd ID.
		@return Z_FamiliaProd ID	  */
	public int getZ_FamiliaProd_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_FamiliaProd_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_PautaComVtaProd ID.
		@param Z_PautaComVtaProd_ID Z_PautaComVtaProd ID	  */
	public void setZ_PautaComVtaProd_ID (int Z_PautaComVtaProd_ID)
	{
		if (Z_PautaComVtaProd_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_PautaComVtaProd_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_PautaComVtaProd_ID, Integer.valueOf(Z_PautaComVtaProd_ID));
	}

	/** Get Z_PautaComVtaProd ID.
		@return Z_PautaComVtaProd ID	  */
	public int getZ_PautaComVtaProd_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_PautaComVtaProd_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_PautaComVtaSeg getZ_PautaComVtaSeg() throws RuntimeException
    {
		return (I_Z_PautaComVtaSeg)MTable.get(getCtx(), I_Z_PautaComVtaSeg.Table_Name)
			.getPO(getZ_PautaComVtaSeg_ID(), get_TrxName());	}

	/** Set Z_PautaComVtaSeg ID.
		@param Z_PautaComVtaSeg_ID Z_PautaComVtaSeg ID	  */
	public void setZ_PautaComVtaSeg_ID (int Z_PautaComVtaSeg_ID)
	{
		if (Z_PautaComVtaSeg_ID < 1) 
			set_Value (COLUMNNAME_Z_PautaComVtaSeg_ID, null);
		else 
			set_Value (COLUMNNAME_Z_PautaComVtaSeg_ID, Integer.valueOf(Z_PautaComVtaSeg_ID));
	}

	/** Get Z_PautaComVtaSeg ID.
		@return Z_PautaComVtaSeg ID	  */
	public int getZ_PautaComVtaSeg_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_PautaComVtaSeg_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_SubfamiliaProd getZ_SubfamiliaProd() throws RuntimeException
    {
		return (I_Z_SubfamiliaProd)MTable.get(getCtx(), I_Z_SubfamiliaProd.Table_Name)
			.getPO(getZ_SubfamiliaProd_ID(), get_TrxName());	}

	/** Set Z_SubfamiliaProd ID.
		@param Z_SubfamiliaProd_ID Z_SubfamiliaProd ID	  */
	public void setZ_SubfamiliaProd_ID (int Z_SubfamiliaProd_ID)
	{
		if (Z_SubfamiliaProd_ID < 1) 
			set_Value (COLUMNNAME_Z_SubfamiliaProd_ID, null);
		else 
			set_Value (COLUMNNAME_Z_SubfamiliaProd_ID, Integer.valueOf(Z_SubfamiliaProd_ID));
	}

	/** Get Z_SubfamiliaProd ID.
		@return Z_SubfamiliaProd ID	  */
	public int getZ_SubfamiliaProd_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_SubfamiliaProd_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}