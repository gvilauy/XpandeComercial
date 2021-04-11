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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for Z_PautaComVtaSeg
 *  @author Adempiere (generated) 
 *  @version Release 3.9.1 - $Id$ */
public class X_Z_PautaComVtaSeg extends PO implements I_Z_PautaComVtaSeg, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210411L;

    /** Standard Constructor */
    public X_Z_PautaComVtaSeg (Properties ctx, int Z_PautaComVtaSeg_ID, String trxName)
    {
      super (ctx, Z_PautaComVtaSeg_ID, trxName);
      /** if (Z_PautaComVtaSeg_ID == 0)
        {
			setIsGeneral (false);
// N
			setName (null);
			setStartDate (new Timestamp( System.currentTimeMillis() ));
			setZ_PautaComVta_ID (0);
			setZ_PautaComVtaSeg_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_PautaComVtaSeg (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_PautaComVtaSeg[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set End Date.
		@param EndDate 
		Last effective date (inclusive)
	  */
	public void setEndDate (Timestamp EndDate)
	{
		set_Value (COLUMNNAME_EndDate, EndDate);
	}

	/** Get End Date.
		@return Last effective date (inclusive)
	  */
	public Timestamp getEndDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndDate);
	}

	/** Set IsGeneral.
		@param IsGeneral IsGeneral	  */
	public void setIsGeneral (boolean IsGeneral)
	{
		set_Value (COLUMNNAME_IsGeneral, Boolean.valueOf(IsGeneral));
	}

	/** Get IsGeneral.
		@return IsGeneral	  */
	public boolean isGeneral () 
	{
		Object oo = get_Value(COLUMNNAME_IsGeneral);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
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

	/** Set Start Date.
		@param StartDate 
		First effective day (inclusive)
	  */
	public void setStartDate (Timestamp StartDate)
	{
		set_Value (COLUMNNAME_StartDate, StartDate);
	}

	/** Get Start Date.
		@return First effective day (inclusive)
	  */
	public Timestamp getStartDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartDate);
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

	/** Set Z_PautaComVtaSeg ID.
		@param Z_PautaComVtaSeg_ID Z_PautaComVtaSeg ID	  */
	public void setZ_PautaComVtaSeg_ID (int Z_PautaComVtaSeg_ID)
	{
		if (Z_PautaComVtaSeg_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_PautaComVtaSeg_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_PautaComVtaSeg_ID, Integer.valueOf(Z_PautaComVtaSeg_ID));
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