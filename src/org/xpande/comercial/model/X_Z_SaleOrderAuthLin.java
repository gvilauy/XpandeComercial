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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for Z_SaleOrderAuthLin
 *  @author Adempiere (generated) 
 *  @version Release 3.9.1 - $Id$ */
public class X_Z_SaleOrderAuthLin extends PO implements I_Z_SaleOrderAuthLin, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210602L;

    /** Standard Constructor */
    public X_Z_SaleOrderAuthLin (Properties ctx, int Z_SaleOrderAuthLin_ID, String trxName)
    {
      super (ctx, Z_SaleOrderAuthLin_ID, trxName);
      /** if (Z_SaleOrderAuthLin_ID == 0)
        {
			setC_BPartner_ID (0);
			setC_BPartner_Location_ID (0);
			setC_Order_ID (0);
			setDateOrdered (new Timestamp( System.currentTimeMillis() ));
			setIsSelected (false);
// N
			setTotalAmt (Env.ZERO);
			setZ_SaleOrderAuthBP_ID (0);
			setZ_SaleOrderAuth_ID (0);
			setZ_SaleOrderAuthLin_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_SaleOrderAuthLin (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_SaleOrderAuthLin[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException
    {
		return (I_C_BPartner_Location)MTable.get(getCtx(), I_C_BPartner_Location.Table_Name)
			.getPO(getC_BPartner_Location_ID(), get_TrxName());	}

	/** Set Partner Location.
		@param C_BPartner_Location_ID 
		Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
	{
		if (C_BPartner_Location_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, Integer.valueOf(C_BPartner_Location_ID));
	}

	/** Get Partner Location.
		@return Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Order getC_Order() throws RuntimeException
    {
		return (I_C_Order)MTable.get(getCtx(), I_C_Order.Table_Name)
			.getPO(getC_Order_ID(), get_TrxName());	}

	/** Set Order.
		@param C_Order_ID 
		Order
	  */
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1) 
			set_Value (COLUMNNAME_C_Order_ID, null);
		else 
			set_Value (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Order_ID()));
    }

	/** Set Date Ordered.
		@param DateOrdered 
		Date of Order
	  */
	public void setDateOrdered (Timestamp DateOrdered)
	{
		set_Value (COLUMNNAME_DateOrdered, DateOrdered);
	}

	/** Get Date Ordered.
		@return Date of Order
	  */
	public Timestamp getDateOrdered () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateOrdered);
	}

	/** Set Date Promised.
		@param DatePromised 
		Date Order was promised
	  */
	public void setDatePromised (Timestamp DatePromised)
	{
		set_Value (COLUMNNAME_DatePromised, DatePromised);
	}

	/** Get Date Promised.
		@return Date Order was promised
	  */
	public Timestamp getDatePromised () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DatePromised);
	}

	/** Set Selected.
		@param IsSelected Selected	  */
	public void setIsSelected (boolean IsSelected)
	{
		set_Value (COLUMNNAME_IsSelected, Boolean.valueOf(IsSelected));
	}

	/** Get Selected.
		@return Selected	  */
	public boolean isSelected () 
	{
		Object oo = get_Value(COLUMNNAME_IsSelected);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_AD_User getSalesRep() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getSalesRep_ID(), get_TrxName());	}

	/** Set Sales Representative.
		@param SalesRep_ID 
		Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID)
	{
		if (SalesRep_ID < 1) 
			set_Value (COLUMNNAME_SalesRep_ID, null);
		else 
			set_Value (COLUMNNAME_SalesRep_ID, Integer.valueOf(SalesRep_ID));
	}

	/** Get Sales Representative.
		@return Sales Representative or Company Agent
	  */
	public int getSalesRep_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SalesRep_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Total Amount.
		@param TotalAmt 
		Total Amount
	  */
	public void setTotalAmt (BigDecimal TotalAmt)
	{
		set_Value (COLUMNNAME_TotalAmt, TotalAmt);
	}

	/** Get Total Amount.
		@return Total Amount
	  */
	public BigDecimal getTotalAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalAmt);
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

	public I_Z_SaleOrderAuthBP getZ_SaleOrderAuthBP() throws RuntimeException
    {
		return (I_Z_SaleOrderAuthBP)MTable.get(getCtx(), I_Z_SaleOrderAuthBP.Table_Name)
			.getPO(getZ_SaleOrderAuthBP_ID(), get_TrxName());	}

	/** Set Z_SaleOrderAuthBP ID.
		@param Z_SaleOrderAuthBP_ID Z_SaleOrderAuthBP ID	  */
	public void setZ_SaleOrderAuthBP_ID (int Z_SaleOrderAuthBP_ID)
	{
		if (Z_SaleOrderAuthBP_ID < 1) 
			set_Value (COLUMNNAME_Z_SaleOrderAuthBP_ID, null);
		else 
			set_Value (COLUMNNAME_Z_SaleOrderAuthBP_ID, Integer.valueOf(Z_SaleOrderAuthBP_ID));
	}

	/** Get Z_SaleOrderAuthBP ID.
		@return Z_SaleOrderAuthBP ID	  */
	public int getZ_SaleOrderAuthBP_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_SaleOrderAuthBP_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_SaleOrderAuth getZ_SaleOrderAuth() throws RuntimeException
    {
		return (I_Z_SaleOrderAuth)MTable.get(getCtx(), I_Z_SaleOrderAuth.Table_Name)
			.getPO(getZ_SaleOrderAuth_ID(), get_TrxName());	}

	/** Set Z_SaleOrderAuth ID.
		@param Z_SaleOrderAuth_ID Z_SaleOrderAuth ID	  */
	public void setZ_SaleOrderAuth_ID (int Z_SaleOrderAuth_ID)
	{
		if (Z_SaleOrderAuth_ID < 1) 
			set_Value (COLUMNNAME_Z_SaleOrderAuth_ID, null);
		else 
			set_Value (COLUMNNAME_Z_SaleOrderAuth_ID, Integer.valueOf(Z_SaleOrderAuth_ID));
	}

	/** Get Z_SaleOrderAuth ID.
		@return Z_SaleOrderAuth ID	  */
	public int getZ_SaleOrderAuth_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_SaleOrderAuth_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_SaleOrderAuthLin ID.
		@param Z_SaleOrderAuthLin_ID Z_SaleOrderAuthLin ID	  */
	public void setZ_SaleOrderAuthLin_ID (int Z_SaleOrderAuthLin_ID)
	{
		if (Z_SaleOrderAuthLin_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_SaleOrderAuthLin_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_SaleOrderAuthLin_ID, Integer.valueOf(Z_SaleOrderAuthLin_ID));
	}

	/** Get Z_SaleOrderAuthLin ID.
		@return Z_SaleOrderAuthLin ID	  */
	public int getZ_SaleOrderAuthLin_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_SaleOrderAuthLin_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}