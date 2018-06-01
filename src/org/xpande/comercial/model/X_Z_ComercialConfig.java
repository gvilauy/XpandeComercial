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

/** Generated Model for Z_ComercialConfig
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_ComercialConfig extends PO implements I_Z_ComercialConfig, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180601L;

    /** Standard Constructor */
    public X_Z_ComercialConfig (Properties ctx, int Z_ComercialConfig_ID, String trxName)
    {
      super (ctx, Z_ComercialConfig_ID, trxName);
      /** if (Z_ComercialConfig_ID == 0)
        {
			setValue (null);
			setZ_ComercialConfig_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_ComercialConfig (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_ComercialConfig[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_PaymentTerm getC_PaymentTerm() throws RuntimeException
    {
		return (I_C_PaymentTerm)MTable.get(getCtx(), I_C_PaymentTerm.Table_Name)
			.getPO(getC_PaymentTerm_ID(), get_TrxName());	}

	/** Set Payment Term.
		@param C_PaymentTerm_ID 
		The terms of Payment (timing, discount)
	  */
	public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
	{
		if (C_PaymentTerm_ID < 1) 
			set_Value (COLUMNNAME_C_PaymentTerm_ID, null);
		else 
			set_Value (COLUMNNAME_C_PaymentTerm_ID, Integer.valueOf(C_PaymentTerm_ID));
	}

	/** Get Payment Term.
		@return The terms of Payment (timing, discount)
	  */
	public int getC_PaymentTerm_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentTerm_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DefaultDocAPCDif_ID.
		@param DefaultDocAPCDif_ID 
		Documento por defecto para Notas de Crédito por Diferencias en Facturación de Proveedores
	  */
	public void setDefaultDocAPCDif_ID (int DefaultDocAPCDif_ID)
	{
		if (DefaultDocAPCDif_ID < 1) 
			set_Value (COLUMNNAME_DefaultDocAPCDif_ID, null);
		else 
			set_Value (COLUMNNAME_DefaultDocAPCDif_ID, Integer.valueOf(DefaultDocAPCDif_ID));
	}

	/** Get DefaultDocAPCDif_ID.
		@return Documento por defecto para Notas de Crédito por Diferencias en Facturación de Proveedores
	  */
	public int getDefaultDocAPCDif_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DefaultDocAPCDif_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DefaultDocAPCDtoPag_ID.
		@param DefaultDocAPCDtoPag_ID 
		Documento por defecto para Notas de Crédito por descuentos al Pago de Proveedores
	  */
	public void setDefaultDocAPCDtoPag_ID (int DefaultDocAPCDtoPag_ID)
	{
		if (DefaultDocAPCDtoPag_ID < 1) 
			set_Value (COLUMNNAME_DefaultDocAPCDtoPag_ID, null);
		else 
			set_Value (COLUMNNAME_DefaultDocAPCDtoPag_ID, Integer.valueOf(DefaultDocAPCDtoPag_ID));
	}

	/** Get DefaultDocAPCDtoPag_ID.
		@return Documento por defecto para Notas de Crédito por descuentos al Pago de Proveedores
	  */
	public int getDefaultDocAPCDtoPag_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DefaultDocAPCDtoPag_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DefaultDocAPIBonif_ID.
		@param DefaultDocAPIBonif_ID 
		Documento por defecto para Factura de Proveedores que vienen solo con Bonificaciones en unidades (sin precio)
	  */
	public void setDefaultDocAPIBonif_ID (int DefaultDocAPIBonif_ID)
	{
		if (DefaultDocAPIBonif_ID < 1) 
			set_Value (COLUMNNAME_DefaultDocAPIBonif_ID, null);
		else 
			set_Value (COLUMNNAME_DefaultDocAPIBonif_ID, Integer.valueOf(DefaultDocAPIBonif_ID));
	}

	/** Get DefaultDocAPIBonif_ID.
		@return Documento por defecto para Factura de Proveedores que vienen solo con Bonificaciones en unidades (sin precio)
	  */
	public int getDefaultDocAPIBonif_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DefaultDocAPIBonif_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DefaultDocAPI_ID.
		@param DefaultDocAPI_ID 
		Documento por defecto para Factura de Proveedores
	  */
	public void setDefaultDocAPI_ID (int DefaultDocAPI_ID)
	{
		if (DefaultDocAPI_ID < 1) 
			set_Value (COLUMNNAME_DefaultDocAPI_ID, null);
		else 
			set_Value (COLUMNNAME_DefaultDocAPI_ID, Integer.valueOf(DefaultDocAPI_ID));
	}

	/** Get DefaultDocAPI_ID.
		@return Documento por defecto para Factura de Proveedores
	  */
	public int getDefaultDocAPI_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DefaultDocAPI_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DefaultDocMMR_ID.
		@param DefaultDocMMR_ID 
		Documento por defecto para Recepción de Productos de Proveedores
	  */
	public void setDefaultDocMMR_ID (int DefaultDocMMR_ID)
	{
		if (DefaultDocMMR_ID < 1) 
			set_Value (COLUMNNAME_DefaultDocMMR_ID, null);
		else 
			set_Value (COLUMNNAME_DefaultDocMMR_ID, Integer.valueOf(DefaultDocMMR_ID));
	}

	/** Get DefaultDocMMR_ID.
		@return Documento por defecto para Recepción de Productos de Proveedores
	  */
	public int getDefaultDocMMR_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DefaultDocMMR_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Tax getLiteralE_Tax() throws RuntimeException
    {
		return (I_C_Tax)MTable.get(getCtx(), I_C_Tax.Table_Name)
			.getPO(getLiteralE_Tax_ID(), get_TrxName());	}

	/** Set LiteralE_Tax_ID.
		@param LiteralE_Tax_ID 
		ID de la tasa de Impuesto para gestionar compras y ventas a socios Literal E
	  */
	public void setLiteralE_Tax_ID (int LiteralE_Tax_ID)
	{
		if (LiteralE_Tax_ID < 1) 
			set_Value (COLUMNNAME_LiteralE_Tax_ID, null);
		else 
			set_Value (COLUMNNAME_LiteralE_Tax_ID, Integer.valueOf(LiteralE_Tax_ID));
	}

	/** Get LiteralE_Tax_ID.
		@return ID de la tasa de Impuesto para gestionar compras y ventas a socios Literal E
	  */
	public int getLiteralE_Tax_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LiteralE_Tax_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set MarginTolerance.
		@param MarginTolerance 
		Porcentaje de tolerancia para márgenes
	  */
	public void setMarginTolerance (BigDecimal MarginTolerance)
	{
		set_Value (COLUMNNAME_MarginTolerance, MarginTolerance);
	}

	/** Get MarginTolerance.
		@return Porcentaje de tolerancia para márgenes
	  */
	public BigDecimal getMarginTolerance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MarginTolerance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}

	/** Set Z_ComercialConfig ID.
		@param Z_ComercialConfig_ID Z_ComercialConfig ID	  */
	public void setZ_ComercialConfig_ID (int Z_ComercialConfig_ID)
	{
		if (Z_ComercialConfig_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_ComercialConfig_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_ComercialConfig_ID, Integer.valueOf(Z_ComercialConfig_ID));
	}

	/** Get Z_ComercialConfig ID.
		@return Z_ComercialConfig ID	  */
	public int getZ_ComercialConfig_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ComercialConfig_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}