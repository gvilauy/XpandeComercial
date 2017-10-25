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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for Z_GeneraNCPagoLin
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_GeneraNCPagoLin extends PO implements I_Z_GeneraNCPagoLin, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171025L;

    /** Standard Constructor */
    public X_Z_GeneraNCPagoLin (Properties ctx, int Z_GeneraNCPagoLin_ID, String trxName)
    {
      super (ctx, Z_GeneraNCPagoLin_ID, trxName);
      /** if (Z_GeneraNCPagoLin_ID == 0)
        {
			setAmtDtoNC (Env.ZERO);
			setIsSelected (false);
// N
			setZ_GeneraNCPago_ID (0);
			setZ_GeneraNCPagoLin_ID (0);
			setZ_GeneraNCPagoSocio_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_GeneraNCPagoLin (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_GeneraNCPagoLin[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AmtDocument.
		@param AmtDocument 
		Monto documento
	  */
	public void setAmtDocument (BigDecimal AmtDocument)
	{
		set_Value (COLUMNNAME_AmtDocument, AmtDocument);
	}

	/** Get AmtDocument.
		@return Monto documento
	  */
	public BigDecimal getAmtDocument () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmtDocument);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set AmtDtoNC.
		@param AmtDtoNC 
		Monto de linea de comprobante considerando precio con descuentos de Notas de Credito al Pago
	  */
	public void setAmtDtoNC (BigDecimal AmtDtoNC)
	{
		set_Value (COLUMNNAME_AmtDtoNC, AmtDtoNC);
	}

	/** Get AmtDtoNC.
		@return Monto de linea de comprobante considerando precio con descuentos de Notas de Credito al Pago
	  */
	public BigDecimal getAmtDtoNC () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmtDtoNC);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	public I_C_DocType getC_DocType() throws RuntimeException
    {
		return (I_C_DocType)MTable.get(getCtx(), I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (I_C_Invoice)MTable.get(getCtx(), I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	/** Set DocumentNoRef.
		@param DocumentNoRef 
		Numero de documento referenciado
	  */
	public void setDocumentNoRef (String DocumentNoRef)
	{
		set_Value (COLUMNNAME_DocumentNoRef, DocumentNoRef);
	}

	/** Get DocumentNoRef.
		@return Numero de documento referenciado
	  */
	public String getDocumentNoRef () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNoRef);
	}

	/** Set DueDateDoc.
		@param DueDateDoc 
		Vencimiento del documento
	  */
	public void setDueDateDoc (Timestamp DueDateDoc)
	{
		set_Value (COLUMNNAME_DueDateDoc, DueDateDoc);
	}

	/** Get DueDateDoc.
		@return Vencimiento del documento
	  */
	public Timestamp getDueDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DueDateDoc);
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

	/** Set Z_GeneraNCPagoLin ID.
		@param Z_GeneraNCPagoLin_ID Z_GeneraNCPagoLin ID	  */
	public void setZ_GeneraNCPagoLin_ID (int Z_GeneraNCPagoLin_ID)
	{
		if (Z_GeneraNCPagoLin_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_GeneraNCPagoLin_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_GeneraNCPagoLin_ID, Integer.valueOf(Z_GeneraNCPagoLin_ID));
	}

	/** Get Z_GeneraNCPagoLin ID.
		@return Z_GeneraNCPagoLin ID	  */
	public int getZ_GeneraNCPagoLin_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_GeneraNCPagoLin_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.xpande.comercial.model.I_Z_GeneraNCPagoSocio getZ_GeneraNCPagoSocio() throws RuntimeException
    {
		return (org.xpande.comercial.model.I_Z_GeneraNCPagoSocio)MTable.get(getCtx(), org.xpande.comercial.model.I_Z_GeneraNCPagoSocio.Table_Name)
			.getPO(getZ_GeneraNCPagoSocio_ID(), get_TrxName());	}

	/** Set Z_GeneraNCPagoSocio ID.
		@param Z_GeneraNCPagoSocio_ID Z_GeneraNCPagoSocio ID	  */
	public void setZ_GeneraNCPagoSocio_ID (int Z_GeneraNCPagoSocio_ID)
	{
		if (Z_GeneraNCPagoSocio_ID < 1) 
			set_Value (COLUMNNAME_Z_GeneraNCPagoSocio_ID, null);
		else 
			set_Value (COLUMNNAME_Z_GeneraNCPagoSocio_ID, Integer.valueOf(Z_GeneraNCPagoSocio_ID));
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