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

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for Z_ComercialConfigPList
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_ComercialConfigPList extends PO implements I_Z_ComercialConfigPList, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170927L;

    /** Standard Constructor */
    public X_Z_ComercialConfigPList (Properties ctx, int Z_ComercialConfigPList_ID, String trxName)
    {
      super (ctx, Z_ComercialConfigPList_ID, trxName);
      /** if (Z_ComercialConfigPList_ID == 0)
        {
			setC_Currency_ID (0);
			setIsSOPriceList (false);
// N
			setM_PriceList_ID (0);
			setZ_ComercialConfig_ID (0);
			setZ_ComercialConfigPList_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_ComercialConfigPList (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_ComercialConfigPList[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_Currency getC_Currency() throws RuntimeException
    {
		return (I_C_Currency)MTable.get(getCtx(), I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Sales Price list.
		@param IsSOPriceList 
		This is a Sales Price List
	  */
	public void setIsSOPriceList (boolean IsSOPriceList)
	{
		set_Value (COLUMNNAME_IsSOPriceList, Boolean.valueOf(IsSOPriceList));
	}

	/** Get Sales Price list.
		@return This is a Sales Price List
	  */
	public boolean isSOPriceList () 
	{
		Object oo = get_Value(COLUMNNAME_IsSOPriceList);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_M_PriceList getM_PriceList() throws RuntimeException
    {
		return (I_M_PriceList)MTable.get(getCtx(), I_M_PriceList.Table_Name)
			.getPO(getM_PriceList_ID(), get_TrxName());	}

	/** Set Price List.
		@param M_PriceList_ID 
		Unique identifier of a Price List
	  */
	public void setM_PriceList_ID (int M_PriceList_ID)
	{
		if (M_PriceList_ID < 1) 
			set_Value (COLUMNNAME_M_PriceList_ID, null);
		else 
			set_Value (COLUMNNAME_M_PriceList_ID, Integer.valueOf(M_PriceList_ID));
	}

	/** Get Price List.
		@return Unique identifier of a Price List
	  */
	public int getM_PriceList_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_PriceList_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_ComercialConfig getZ_ComercialConfig() throws RuntimeException
    {
		return (I_Z_ComercialConfig)MTable.get(getCtx(), I_Z_ComercialConfig.Table_Name)
			.getPO(getZ_ComercialConfig_ID(), get_TrxName());	}

	/** Set Z_ComercialConfig ID.
		@param Z_ComercialConfig_ID Z_ComercialConfig ID	  */
	public void setZ_ComercialConfig_ID (int Z_ComercialConfig_ID)
	{
		if (Z_ComercialConfig_ID < 1) 
			set_Value (COLUMNNAME_Z_ComercialConfig_ID, null);
		else 
			set_Value (COLUMNNAME_Z_ComercialConfig_ID, Integer.valueOf(Z_ComercialConfig_ID));
	}

	/** Get Z_ComercialConfig ID.
		@return Z_ComercialConfig ID	  */
	public int getZ_ComercialConfig_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ComercialConfig_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_ComercialConfigPList ID.
		@param Z_ComercialConfigPList_ID Z_ComercialConfigPList ID	  */
	public void setZ_ComercialConfigPList_ID (int Z_ComercialConfigPList_ID)
	{
		if (Z_ComercialConfigPList_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_ComercialConfigPList_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_ComercialConfigPList_ID, Integer.valueOf(Z_ComercialConfigPList_ID));
	}

	/** Get Z_ComercialConfigPList ID.
		@return Z_ComercialConfigPList ID	  */
	public int getZ_ComercialConfigPList_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ComercialConfigPList_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}