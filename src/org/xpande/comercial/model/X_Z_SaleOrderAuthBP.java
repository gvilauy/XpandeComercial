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
import org.compiere.util.KeyNamePair;

/** Generated Model for Z_SaleOrderAuthBP
 *  @author Adempiere (generated) 
 *  @version Release 3.9.1 - $Id$ */
public class X_Z_SaleOrderAuthBP extends PO implements I_Z_SaleOrderAuthBP, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210602L;

    /** Standard Constructor */
    public X_Z_SaleOrderAuthBP (Properties ctx, int Z_SaleOrderAuthBP_ID, String trxName)
    {
      super (ctx, Z_SaleOrderAuthBP_ID, trxName);
      /** if (Z_SaleOrderAuthBP_ID == 0)
        {
			setC_BPartner_ID (0);
			setStatusApprovalSO (null);
// N
			setZ_SaleOrderAuthBP_ID (0);
			setZ_SaleOrderAuth_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_SaleOrderAuthBP (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_SaleOrderAuthBP[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Approval Amount.
		@param AmtApproval 
		The approval amount limit for this role
	  */
	public void setAmtApproval (BigDecimal AmtApproval)
	{
		set_Value (COLUMNNAME_AmtApproval, AmtApproval);
	}

	/** Get Approval Amount.
		@return The approval amount limit for this role
	  */
	public BigDecimal getAmtApproval () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmtApproval);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_BPartner_ID()));
    }

	/** Set Amount due.
		@param DueAmt 
		Amount of the payment due
	  */
	public void setDueAmt (BigDecimal DueAmt)
	{
		set_Value (COLUMNNAME_DueAmt, DueAmt);
	}

	/** Get Amount due.
		@return Amount of the payment due
	  */
	public BigDecimal getDueAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DueAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Credit Limit.
		@param SO_CreditLimit 
		Total outstanding invoice amounts allowed
	  */
	public void setSO_CreditLimit (BigDecimal SO_CreditLimit)
	{
		set_Value (COLUMNNAME_SO_CreditLimit, SO_CreditLimit);
	}

	/** Get Credit Limit.
		@return Total outstanding invoice amounts allowed
	  */
	public BigDecimal getSO_CreditLimit () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SO_CreditLimit);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Credit Used.
		@param SO_CreditUsed 
		Current open balance
	  */
	public void setSO_CreditUsed (BigDecimal SO_CreditUsed)
	{
		set_Value (COLUMNNAME_SO_CreditUsed, SO_CreditUsed);
	}

	/** Get Credit Used.
		@return Current open balance
	  */
	public BigDecimal getSO_CreditUsed () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SO_CreditUsed);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** StatusApprovalSO AD_Reference_ID=1000067 */
	public static final int STATUSAPPROVALSO_AD_Reference_ID=1000067;
	/** SIN APROBACION = N */
	public static final String STATUSAPPROVALSO_SINAPROBACION = "N";
	/** APROBACION PARCIAL = P */
	public static final String STATUSAPPROVALSO_APROBACIONPARCIAL = "P";
	/** APROBACION TOTAL = T */
	public static final String STATUSAPPROVALSO_APROBACIONTOTAL = "T";
	/** Set StatusApprovalSO.
		@param StatusApprovalSO 
		Estado Aprobación Credito
	  */
	public void setStatusApprovalSO (String StatusApprovalSO)
	{

		set_Value (COLUMNNAME_StatusApprovalSO, StatusApprovalSO);
	}

	/** Get StatusApprovalSO.
		@return Estado Aprobación Credito
	  */
	public String getStatusApprovalSO () 
	{
		return (String)get_Value(COLUMNNAME_StatusApprovalSO);
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

	/** Set Z_SaleOrderAuthBP ID.
		@param Z_SaleOrderAuthBP_ID Z_SaleOrderAuthBP ID	  */
	public void setZ_SaleOrderAuthBP_ID (int Z_SaleOrderAuthBP_ID)
	{
		if (Z_SaleOrderAuthBP_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_SaleOrderAuthBP_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_SaleOrderAuthBP_ID, Integer.valueOf(Z_SaleOrderAuthBP_ID));
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
}