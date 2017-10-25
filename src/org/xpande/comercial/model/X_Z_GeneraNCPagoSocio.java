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

/** Generated Model for Z_GeneraNCPagoSocio
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_GeneraNCPagoSocio extends PO implements I_Z_GeneraNCPagoSocio, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171025L;

    /** Standard Constructor */
    public X_Z_GeneraNCPagoSocio (Properties ctx, int Z_GeneraNCPagoSocio_ID, String trxName)
    {
      super (ctx, Z_GeneraNCPagoSocio_ID, trxName);
      /** if (Z_GeneraNCPagoSocio_ID == 0)
        {
			setC_BPartner_ID (0);
			setC_Currency_ID (0);
			setIsSelected (false);
// N
			setTotalAmt (Env.ZERO);
			setZ_GeneraNCPago_ID (0);
			setZ_GeneraNCPagoSocio_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_GeneraNCPagoSocio (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_GeneraNCPagoSocio[")
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

	/** Set ProcessButton.
		@param ProcessButton ProcessButton	  */
	public void setProcessButton (String ProcessButton)
	{
		set_Value (COLUMNNAME_ProcessButton, ProcessButton);
	}

	/** Get ProcessButton.
		@return ProcessButton	  */
	public String getProcessButton () 
	{
		return (String)get_Value(COLUMNNAME_ProcessButton);
	}

	/** Set Tax ID.
		@param TaxID 
		Tax Identification
	  */
	public void setTaxID (String TaxID)
	{
		set_Value (COLUMNNAME_TaxID, TaxID);
	}

	/** Get Tax ID.
		@return Tax Identification
	  */
	public String getTaxID () 
	{
		return (String)get_Value(COLUMNNAME_TaxID);
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

	public I_Z_GeneraNCPago getZ_GeneraNCPago() throws RuntimeException
    {
		return (I_Z_GeneraNCPago)MTable.get(getCtx(), I_Z_GeneraNCPago.Table_Name)
			.getPO(getZ_GeneraNCPago_ID(), get_TrxName());	}

	/** Set Z_GeneraNCPago ID.
		@param Z_GeneraNCPago_ID Z_GeneraNCPago ID	  */
	public void setZ_GeneraNCPago_ID (int Z_GeneraNCPago_ID)
	{
		if (Z_GeneraNCPago_ID < 1) 
			set_Value (COLUMNNAME_Z_GeneraNCPago_ID, null);
		else 
			set_Value (COLUMNNAME_Z_GeneraNCPago_ID, Integer.valueOf(Z_GeneraNCPago_ID));
	}

	/** Get Z_GeneraNCPago ID.
		@return Z_GeneraNCPago ID	  */
	public int getZ_GeneraNCPago_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_GeneraNCPago_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_GeneraNCPagoSocio ID.
		@param Z_GeneraNCPagoSocio_ID Z_GeneraNCPagoSocio ID	  */
	public void setZ_GeneraNCPagoSocio_ID (int Z_GeneraNCPagoSocio_ID)
	{
		if (Z_GeneraNCPagoSocio_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_GeneraNCPagoSocio_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_GeneraNCPagoSocio_ID, Integer.valueOf(Z_GeneraNCPagoSocio_ID));
	}

	/** Get Z_GeneraNCPagoSocio ID.
		@return Z_GeneraNCPagoSocio ID	  */
	public int getZ_GeneraNCPagoSocio_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_GeneraNCPagoSocio_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}