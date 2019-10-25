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

/** Generated Model for Z_CanalVenta
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_CanalVenta extends PO implements I_Z_CanalVenta, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20191024L;

    /** Standard Constructor */
    public X_Z_CanalVenta (Properties ctx, int Z_CanalVenta_ID, String trxName)
    {
      super (ctx, Z_CanalVenta_ID, trxName);
      /** if (Z_CanalVenta_ID == 0)
        {
			setName (null);
			setZ_CanalVenta_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_CanalVenta (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_CanalVenta[")
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

	/** Set Z_CanalVenta ID.
		@param Z_CanalVenta_ID Z_CanalVenta ID	  */
	public void setZ_CanalVenta_ID (int Z_CanalVenta_ID)
	{
		if (Z_CanalVenta_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_CanalVenta_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_CanalVenta_ID, Integer.valueOf(Z_CanalVenta_ID));
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
}