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
package org.xpande.comercial.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for Z_PautaComVtaSeg
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_Z_PautaComVtaSeg 
{

    /** TableName=Z_PautaComVtaSeg */
    public static final String Table_Name = "Z_PautaComVtaSeg";

    /** AD_Table_ID=1000223 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID(int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription(String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name EndDate */
    public static final String COLUMNNAME_EndDate = "EndDate";

	/** Set End Date.
	  * Last effective date (inclusive)
	  */
	public void setEndDate(Timestamp EndDate);

	/** Get End Date.
	  * Last effective date (inclusive)
	  */
	public Timestamp getEndDate();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive(boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsGeneral */
    public static final String COLUMNNAME_IsGeneral = "IsGeneral";

	/** Set IsGeneral	  */
	public void setIsGeneral(boolean IsGeneral);

	/** Get IsGeneral	  */
	public boolean isGeneral();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName(String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name ProcessButton */
    public static final String COLUMNNAME_ProcessButton = "ProcessButton";

	/** Set ProcessButton	  */
	public void setProcessButton(String ProcessButton);

	/** Get ProcessButton	  */
	public String getProcessButton();

    /** Column name StartDate */
    public static final String COLUMNNAME_StartDate = "StartDate";

	/** Set Start Date.
	  * First effective day (inclusive)
	  */
	public void setStartDate(Timestamp StartDate);

	/** Get Start Date.
	  * First effective day (inclusive)
	  */
	public Timestamp getStartDate();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name UUID */
    public static final String COLUMNNAME_UUID = "UUID";

	/** Set Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public void setUUID(String UUID);

	/** Get Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public String getUUID();

    /** Column name Z_PautaComVta_ID */
    public static final String COLUMNNAME_Z_PautaComVta_ID = "Z_PautaComVta_ID";

	/** Set Z_PautaComVta ID	  */
	public void setZ_PautaComVta_ID(int Z_PautaComVta_ID);

	/** Get Z_PautaComVta ID	  */
	public int getZ_PautaComVta_ID();

	public I_Z_PautaComVta getZ_PautaComVta() throws RuntimeException;

    /** Column name Z_PautaComVtaSeg_ID */
    public static final String COLUMNNAME_Z_PautaComVtaSeg_ID = "Z_PautaComVtaSeg_ID";

	/** Set Z_PautaComVtaSeg ID	  */
	public void setZ_PautaComVtaSeg_ID(int Z_PautaComVtaSeg_ID);

	/** Get Z_PautaComVtaSeg ID	  */
	public int getZ_PautaComVtaSeg_ID();

    /** Column name Z_ProductoRubro_ID */
    public static final String COLUMNNAME_Z_ProductoRubro_ID = "Z_ProductoRubro_ID";

	/** Set Z_ProductoRubro ID	  */
	public void setZ_ProductoRubro_ID(int Z_ProductoRubro_ID);

	/** Get Z_ProductoRubro ID	  */
	public int getZ_ProductoRubro_ID();

    /** Column name Z_ProductoSeccion_ID */
    public static final String COLUMNNAME_Z_ProductoSeccion_ID = "Z_ProductoSeccion_ID";

	/** Set Z_ProductoSeccion ID	  */
	public void setZ_ProductoSeccion_ID(int Z_ProductoSeccion_ID);

	/** Get Z_ProductoSeccion ID	  */
	public int getZ_ProductoSeccion_ID();
}
