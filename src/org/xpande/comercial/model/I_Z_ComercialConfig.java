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

/** Generated Interface for Z_ComercialConfig
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_Z_ComercialConfig 
{

    /** TableName=Z_ComercialConfig */
    public static final String Table_Name = "Z_ComercialConfig";

    /** AD_Table_ID=1000133 */
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

    /** Column name C_PaymentTerm_ID */
    public static final String COLUMNNAME_C_PaymentTerm_ID = "C_PaymentTerm_ID";

	/** Set Payment Term.
	  * The terms of Payment (timing, discount)
	  */
	public void setC_PaymentTerm_ID(int C_PaymentTerm_ID);

	/** Get Payment Term.
	  * The terms of Payment (timing, discount)
	  */
	public int getC_PaymentTerm_ID();

	public I_C_PaymentTerm getC_PaymentTerm() throws RuntimeException;

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

    /** Column name DefaultDocAPCDif_ID */
    public static final String COLUMNNAME_DefaultDocAPCDif_ID = "DefaultDocAPCDif_ID";

	/** Set DefaultDocAPCDif_ID.
	  * Documento por defecto para Notas de Crédito por Diferencias en Facturación de Proveedores
	  */
	public void setDefaultDocAPCDif_ID(int DefaultDocAPCDif_ID);

	/** Get DefaultDocAPCDif_ID.
	  * Documento por defecto para Notas de Crédito por Diferencias en Facturación de Proveedores
	  */
	public int getDefaultDocAPCDif_ID();

    /** Column name DefaultDocAPI_ID */
    public static final String COLUMNNAME_DefaultDocAPI_ID = "DefaultDocAPI_ID";

	/** Set DefaultDocAPI_ID.
	  * Documento por defecto para Factura de Proveedores
	  */
	public void setDefaultDocAPI_ID(int DefaultDocAPI_ID);

	/** Get DefaultDocAPI_ID.
	  * Documento por defecto para Factura de Proveedores
	  */
	public int getDefaultDocAPI_ID();

    /** Column name DefaultDocMMR_ID */
    public static final String COLUMNNAME_DefaultDocMMR_ID = "DefaultDocMMR_ID";

	/** Set DefaultDocMMR_ID.
	  * Documento por defecto para Recepción de Productos de Proveedores
	  */
	public void setDefaultDocMMR_ID(int DefaultDocMMR_ID);

	/** Get DefaultDocMMR_ID.
	  * Documento por defecto para Recepción de Productos de Proveedores
	  */
	public int getDefaultDocMMR_ID();

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

    /** Column name MarginTolerance */
    public static final String COLUMNNAME_MarginTolerance = "MarginTolerance";

	/** Set MarginTolerance.
	  * Porcentaje de tolerancia para márgenes
	  */
	public void setMarginTolerance(BigDecimal MarginTolerance);

	/** Get MarginTolerance.
	  * Porcentaje de tolerancia para márgenes
	  */
	public BigDecimal getMarginTolerance();

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

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue(String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();

    /** Column name Z_ComercialConfig_ID */
    public static final String COLUMNNAME_Z_ComercialConfig_ID = "Z_ComercialConfig_ID";

	/** Set Z_ComercialConfig ID	  */
	public void setZ_ComercialConfig_ID(int Z_ComercialConfig_ID);

	/** Get Z_ComercialConfig ID	  */
	public int getZ_ComercialConfig_ID();
}
