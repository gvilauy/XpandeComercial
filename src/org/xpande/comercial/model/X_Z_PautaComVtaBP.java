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

/** Generated Model for Z_PautaComVtaBP
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_PautaComVtaBP extends PO implements I_Z_PautaComVtaBP, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20191024L;

    /** Standard Constructor */
    public X_Z_PautaComVtaBP (Properties ctx, int Z_PautaComVtaBP_ID, String trxName)
    {
      super (ctx, Z_PautaComVtaBP_ID, trxName);
      /** if (Z_PautaComVtaBP_ID == 0)
        {
			setC_BPartner_ID (0);
			setZ_PautaComVtaBP_ID (0);
			setZ_PautaComVta_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_PautaComVtaBP (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_PautaComVtaBP[")
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

	public org.xpande.comercial.model.I_Z_CanalVenta getZ_CanalVenta() throws RuntimeException
    {
		return (org.xpande.comercial.model.I_Z_CanalVenta)MTable.get(getCtx(), org.xpande.comercial.model.I_Z_CanalVenta.Table_Name)
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

	/** Set Z_PautaComVtaBP ID.
		@param Z_PautaComVtaBP_ID Z_PautaComVtaBP ID	  */
	public void setZ_PautaComVtaBP_ID (int Z_PautaComVtaBP_ID)
	{
		if (Z_PautaComVtaBP_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_PautaComVtaBP_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_PautaComVtaBP_ID, Integer.valueOf(Z_PautaComVtaBP_ID));
	}

	/** Get Z_PautaComVtaBP ID.
		@return Z_PautaComVtaBP ID	  */
	public int getZ_PautaComVtaBP_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_PautaComVtaBP_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_PautaComVta getZ_PautaComVta() throws RuntimeException
    {
		return (I_Z_PautaComVta)MTable.get(getCtx(), I_Z_PautaComVta.Table_Name)
			.getPO(getZ_PautaComVta_ID(), get_TrxName());	}

	/** Set Z_PautaComVta ID.
		@param Z_PautaComVta_ID Z_PautaComVta ID	  */
	public void setZ_PautaComVta_ID (int Z_PautaComVta_ID)
	{
		if (Z_PautaComVta_ID < 1) 
			set_Value (COLUMNNAME_Z_PautaComVta_ID, null);
		else 
			set_Value (COLUMNNAME_Z_PautaComVta_ID, Integer.valueOf(Z_PautaComVta_ID));
	}

	/** Get Z_PautaComVta ID.
		@return Z_PautaComVta ID	  */
	public int getZ_PautaComVta_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_PautaComVta_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}