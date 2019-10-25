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

/** Generated Model for Z_PautaComVtaDtos
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_PautaComVtaDtos extends PO implements I_Z_PautaComVtaDtos, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20191024L;

    /** Standard Constructor */
    public X_Z_PautaComVtaDtos (Properties ctx, int Z_PautaComVtaDtos_ID, String trxName)
    {
      super (ctx, Z_PautaComVtaDtos_ID, trxName);
      /** if (Z_PautaComVtaDtos_ID == 0)
        {
			setDiscount (Env.ZERO);
			setDiscountType (null);
			setTipoBonificaQty (null);
// CRUZADA
			setZ_PautaComVtaDtos_ID (0);
			setZ_PautaComVtaSeg_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_PautaComVtaDtos (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_PautaComVtaDtos[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Break Value.
		@param BreakValue 
		Low Value of trade discount break level
	  */
	public void setBreakValue (BigDecimal BreakValue)
	{
		set_Value (COLUMNNAME_BreakValue, BreakValue);
	}

	/** Get Break Value.
		@return Low Value of trade discount break level
	  */
	public BigDecimal getBreakValue () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BreakValue);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Discount %.
		@param Discount 
		Discount in percent
	  */
	public void setDiscount (BigDecimal Discount)
	{
		set_Value (COLUMNNAME_Discount, Discount);
	}

	/** Get Discount %.
		@return Discount in percent
	  */
	public BigDecimal getDiscount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Discount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getDiscount()));
    }

	/** DiscountType AD_Reference_ID=1000004 */
	public static final int DISCOUNTTYPE_AD_Reference_ID=1000004;
	/** OPERATIVO EN FACTURA = OPERATIVO */
	public static final String DISCOUNTTYPE_OPERATIVOENFACTURA = "OPERATIVO";
	/** FINANCIERO EN FACTURA = FINANCIERO_FACTURA */
	public static final String DISCOUNTTYPE_FINANCIEROENFACTURA = "FINANCIERO_FACTURA";
	/** NOTA DE CREDITO AL PAGO = NC_PAGO */
	public static final String DISCOUNTTYPE_NOTADECREDITOALPAGO = "NC_PAGO";
	/** FINANCIERO AL PAGO = FINANCIERO_PAGO */
	public static final String DISCOUNTTYPE_FINANCIEROALPAGO = "FINANCIERO_PAGO";
	/** CANTIDADES BONIFICADAS = CANT_BONIFICADA */
	public static final String DISCOUNTTYPE_CANTIDADESBONIFICADAS = "CANT_BONIFICADA";
	/** RETORNO = RETORNO */
	public static final String DISCOUNTTYPE_RETORNO = "RETORNO";
	/** Set Discount Type.
		@param DiscountType 
		Type of trade discount calculation
	  */
	public void setDiscountType (String DiscountType)
	{

		set_Value (COLUMNNAME_DiscountType, DiscountType);
	}

	/** Get Discount Type.
		@return Type of trade discount calculation
	  */
	public String getDiscountType () 
	{
		return (String)get_Value(COLUMNNAME_DiscountType);
	}

	/** Set Reward Quantity.
		@param QtyReward Reward Quantity	  */
	public void setQtyReward (BigDecimal QtyReward)
	{
		set_Value (COLUMNNAME_QtyReward, QtyReward);
	}

	/** Get Reward Quantity.
		@return Reward Quantity	  */
	public BigDecimal getQtyReward () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyReward);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** TipoBonificaQty AD_Reference_ID=1000033 */
	public static final int TIPOBONIFICAQTY_AD_Reference_ID=1000033;
	/** MISMO PRODUCTO = SIMPLE */
	public static final String TIPOBONIFICAQTY_MISMOPRODUCTO = "SIMPLE";
	/** PRODUCTOS DEL SEGMENTO = SIMPLE_SET */
	public static final String TIPOBONIFICAQTY_PRODUCTOSDELSEGMENTO = "SIMPLE_SET";
	/** PRODUCTOS DEL SOCIO DE NEGOCIO = CRUZADA */
	public static final String TIPOBONIFICAQTY_PRODUCTOSDELSOCIODENEGOCIO = "CRUZADA";
	/** Set TipoBonificaQty.
		@param TipoBonificaQty 
		Lista de valores para Tipos de Bonificacion en Cantidadades
	  */
	public void setTipoBonificaQty (String TipoBonificaQty)
	{

		set_Value (COLUMNNAME_TipoBonificaQty, TipoBonificaQty);
	}

	/** Get TipoBonificaQty.
		@return Lista de valores para Tipos de Bonificacion en Cantidadades
	  */
	public String getTipoBonificaQty () 
	{
		return (String)get_Value(COLUMNNAME_TipoBonificaQty);
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

	/** Set Z_PautaComVtaDtos ID.
		@param Z_PautaComVtaDtos_ID Z_PautaComVtaDtos ID	  */
	public void setZ_PautaComVtaDtos_ID (int Z_PautaComVtaDtos_ID)
	{
		if (Z_PautaComVtaDtos_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_PautaComVtaDtos_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_PautaComVtaDtos_ID, Integer.valueOf(Z_PautaComVtaDtos_ID));
	}

	/** Get Z_PautaComVtaDtos ID.
		@return Z_PautaComVtaDtos ID	  */
	public int getZ_PautaComVtaDtos_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_PautaComVtaDtos_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_PautaComVtaSeg getZ_PautaComVtaSeg() throws RuntimeException
    {
		return (I_Z_PautaComVtaSeg)MTable.get(getCtx(), I_Z_PautaComVtaSeg.Table_Name)
			.getPO(getZ_PautaComVtaSeg_ID(), get_TrxName());	}

	/** Set Z_PautaComVtaSeg ID.
		@param Z_PautaComVtaSeg_ID Z_PautaComVtaSeg ID	  */
	public void setZ_PautaComVtaSeg_ID (int Z_PautaComVtaSeg_ID)
	{
		if (Z_PautaComVtaSeg_ID < 1) 
			set_Value (COLUMNNAME_Z_PautaComVtaSeg_ID, null);
		else 
			set_Value (COLUMNNAME_Z_PautaComVtaSeg_ID, Integer.valueOf(Z_PautaComVtaSeg_ID));
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
}