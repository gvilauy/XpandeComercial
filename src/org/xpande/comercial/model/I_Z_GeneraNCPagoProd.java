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

/** Generated Interface for Z_GeneraNCPagoProd
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_Z_GeneraNCPagoProd 
{

    /** TableName=Z_GeneraNCPagoProd */
    public static final String Table_Name = "Z_GeneraNCPagoProd";

    /** AD_Table_ID=1000160 */
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

    /** Column name AmtDtoNC */
    public static final String COLUMNNAME_AmtDtoNC = "AmtDtoNC";

	/** Set AmtDtoNC.
	  * Monto de linea de comprobante considerando precio con descuentos de Notas de Credito al Pago
	  */
	public void setAmtDtoNC(BigDecimal AmtDtoNC);

	/** Get AmtDtoNC.
	  * Monto de linea de comprobante considerando precio con descuentos de Notas de Credito al Pago
	  */
	public BigDecimal getAmtDtoNC();

    /** Column name C_InvoiceLine_ID */
    public static final String COLUMNNAME_C_InvoiceLine_ID = "C_InvoiceLine_ID";

	/** Set Invoice Line.
	  * Invoice Detail Line
	  */
	public void setC_InvoiceLine_ID(int C_InvoiceLine_ID);

	/** Get Invoice Line.
	  * Invoice Detail Line
	  */
	public int getC_InvoiceLine_ID();

	public I_C_InvoiceLine getC_InvoiceLine() throws RuntimeException;

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

    /** Column name LineNetAmt */
    public static final String COLUMNNAME_LineNetAmt = "LineNetAmt";

	/** Set Line Amount.
	  * Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public void setLineNetAmt(BigDecimal LineNetAmt);

	/** Get Line Amount.
	  * Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public BigDecimal getLineNetAmt();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID(int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public I_M_Product getM_Product() throws RuntimeException;

    /** Column name PorcDtoNC */
    public static final String COLUMNNAME_PorcDtoNC = "PorcDtoNC";

	/** Set PorcDtoNC.
	  * Porcentaje de descuentos por Nota de Credito al pago
	  */
	public void setPorcDtoNC(BigDecimal PorcDtoNC);

	/** Get PorcDtoNC.
	  * Porcentaje de descuentos por Nota de Credito al pago
	  */
	public BigDecimal getPorcDtoNC();

    /** Column name PriceDtoNC */
    public static final String COLUMNNAME_PriceDtoNC = "PriceDtoNC";

	/** Set PriceDtoNC.
	  * Precio incluyendo descuentos por motivos de Notas de Cŕedito al Pago
	  */
	public void setPriceDtoNC(BigDecimal PriceDtoNC);

	/** Get PriceDtoNC.
	  * Precio incluyendo descuentos por motivos de Notas de Cŕedito al Pago
	  */
	public BigDecimal getPriceDtoNC();

    /** Column name PriceEntered */
    public static final String COLUMNNAME_PriceEntered = "PriceEntered";

	/** Set Price.
	  * Price Entered - the price based on the selected/base UoM
	  */
	public void setPriceEntered(BigDecimal PriceEntered);

	/** Get Price.
	  * Price Entered - the price based on the selected/base UoM
	  */
	public BigDecimal getPriceEntered();

    /** Column name QtyEntered */
    public static final String COLUMNNAME_QtyEntered = "QtyEntered";

	/** Set Quantity.
	  * The Quantity Entered is based on the selected UoM
	  */
	public void setQtyEntered(BigDecimal QtyEntered);

	/** Get Quantity.
	  * The Quantity Entered is based on the selected UoM
	  */
	public BigDecimal getQtyEntered();

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

    /** Column name Z_GeneraNCPagoLin_ID */
    public static final String COLUMNNAME_Z_GeneraNCPagoLin_ID = "Z_GeneraNCPagoLin_ID";

	/** Set Z_GeneraNCPagoLin ID	  */
	public void setZ_GeneraNCPagoLin_ID(int Z_GeneraNCPagoLin_ID);

	/** Get Z_GeneraNCPagoLin ID	  */
	public int getZ_GeneraNCPagoLin_ID();

	public I_Z_GeneraNCPagoLin getZ_GeneraNCPagoLin() throws RuntimeException;

    /** Column name Z_GeneraNCPagoProd_ID */
    public static final String COLUMNNAME_Z_GeneraNCPagoProd_ID = "Z_GeneraNCPagoProd_ID";

	/** Set Z_GeneraNCPagoProd ID	  */
	public void setZ_GeneraNCPagoProd_ID(int Z_GeneraNCPagoProd_ID);

	/** Get Z_GeneraNCPagoProd ID	  */
	public int getZ_GeneraNCPagoProd_ID();
}
