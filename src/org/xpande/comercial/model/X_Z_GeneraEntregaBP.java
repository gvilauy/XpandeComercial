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

/** Generated Model for Z_GeneraEntregaBP
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_GeneraEntregaBP extends PO implements I_Z_GeneraEntregaBP, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200801L;

    /** Standard Constructor */
    public X_Z_GeneraEntregaBP (Properties ctx, int Z_GeneraEntregaBP_ID, String trxName)
    {
      super (ctx, Z_GeneraEntregaBP_ID, trxName);
      /** if (Z_GeneraEntregaBP_ID == 0)
        {
			setC_BPartner_ID (0);
			setZ_GeneraEntregaBP_ID (0);
			setZ_GeneraEntrega_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_GeneraEntregaBP (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_GeneraEntregaBP[")
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

	public I_Z_CanalVenta getZ_CanalVenta() throws RuntimeException
    {
		return (I_Z_CanalVenta)MTable.get(getCtx(), I_Z_CanalVenta.Table_Name)
			.getPO(getZ_CanalVenta_ID(), get_TrxName());	}

	/** Set Z_CanalVenta ID.
		@param Z_CanalVenta_ID Z_CanalVenta ID	  */
	public void setZ_CanalVenta_ID (int Z_CanalVenta_ID)
	{
		if (Z_CanalVenta_ID < 1) 
			set_Value (COLUMNNAME_Z_CanalVenta_ID, null);
		else 
			set_Value (COLUMNNAME_Z_CanalVenta_ID, Integer.valueOf(Z_CanalVenta_ID));
	}

	/** Get Z_CanalVenta ID.
		@return Z_CanalVenta ID	  */
	public int getZ_CanalVenta_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_CanalVenta_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_GeneraEntregaBP ID.
		@param Z_GeneraEntregaBP_ID Z_GeneraEntregaBP ID	  */
	public void setZ_GeneraEntregaBP_ID (int Z_GeneraEntregaBP_ID)
	{
		if (Z_GeneraEntregaBP_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_GeneraEntregaBP_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_GeneraEntregaBP_ID, Integer.valueOf(Z_GeneraEntregaBP_ID));
	}

	/** Get Z_GeneraEntregaBP ID.
		@return Z_GeneraEntregaBP ID	  */
	public int getZ_GeneraEntregaBP_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_GeneraEntregaBP_ID);
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