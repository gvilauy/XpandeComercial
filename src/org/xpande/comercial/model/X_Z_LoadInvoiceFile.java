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

/** Generated Model for Z_LoadInvoiceFile
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_LoadInvoiceFile extends PO implements I_Z_LoadInvoiceFile, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190228L;

    /** Standard Constructor */
    public X_Z_LoadInvoiceFile (Properties ctx, int Z_LoadInvoiceFile_ID, String trxName)
    {
      super (ctx, Z_LoadInvoiceFile_ID, trxName);
      /** if (Z_LoadInvoiceFile_ID == 0)
        {
			setI_IsImported (false);
// N
			setIsConfirmed (false);
// N
			setIsOmitted (false);
// N
			setProcessed (false);
// N
			setZ_LoadInvoiceFile_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_LoadInvoiceFile (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_LoadInvoiceFile[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Trx Organization.
		@param AD_OrgTrx_ID 
		Performing or initiating organization
	  */
	public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
	{
		if (AD_OrgTrx_ID < 1) 
			set_Value (COLUMNNAME_AD_OrgTrx_ID, null);
		else 
			set_Value (COLUMNNAME_AD_OrgTrx_ID, Integer.valueOf(AD_OrgTrx_ID));
	}

	/** Get Trx Organization.
		@return Performing or initiating organization
	  */
	public int getAD_OrgTrx_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_OrgTrx_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Date Invoiced.
		@param DateInvoiced 
		Date printed on Invoice
	  */
	public void setDateInvoiced (Timestamp DateInvoiced)
	{
		set_Value (COLUMNNAME_DateInvoiced, DateInvoiced);
	}

	/** Get Date Invoiced.
		@return Date printed on Invoice
	  */
	public Timestamp getDateInvoiced () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateInvoiced);
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

	/** Set DocumentSerie.
		@param DocumentSerie 
		Serie de un Documento
	  */
	public void setDocumentSerie (String DocumentSerie)
	{
		set_Value (COLUMNNAME_DocumentSerie, DocumentSerie);
	}

	/** Get DocumentSerie.
		@return Serie de un Documento
	  */
	public String getDocumentSerie () 
	{
		return (String)get_Value(COLUMNNAME_DocumentSerie);
	}

	/** Set Due Date.
		@param DueDate 
		Date when the payment is due
	  */
	public void setDueDate (Timestamp DueDate)
	{
		set_Value (COLUMNNAME_DueDate, DueDate);
	}

	/** Get Due Date.
		@return Date when the payment is due
	  */
	public Timestamp getDueDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DueDate);
	}

	/** Set Error Msg.
		@param ErrorMsg Error Msg	  */
	public void setErrorMsg (String ErrorMsg)
	{
		set_Value (COLUMNNAME_ErrorMsg, ErrorMsg);
	}

	/** Get Error Msg.
		@return Error Msg	  */
	public String getErrorMsg () 
	{
		return (String)get_Value(COLUMNNAME_ErrorMsg);
	}

	/** Set FechaCadena.
		@param FechaCadena 
		Fecha en formato Cadena
	  */
	public void setFechaCadena (String FechaCadena)
	{
		set_Value (COLUMNNAME_FechaCadena, FechaCadena);
	}

	/** Get FechaCadena.
		@return Fecha en formato Cadena
	  */
	public String getFechaCadena () 
	{
		return (String)get_Value(COLUMNNAME_FechaCadena);
	}

	/** Set FileLineText.
		@param FileLineText FileLineText	  */
	public void setFileLineText (String FileLineText)
	{
		set_Value (COLUMNNAME_FileLineText, FileLineText);
	}

	/** Get FileLineText.
		@return FileLineText	  */
	public String getFileLineText () 
	{
		return (String)get_Value(COLUMNNAME_FileLineText);
	}

	/** Set Grand Total.
		@param GrandTotal 
		Total amount of document
	  */
	public void setGrandTotal (BigDecimal GrandTotal)
	{
		set_Value (COLUMNNAME_GrandTotal, GrandTotal);
	}

	/** Get Grand Total.
		@return Total amount of document
	  */
	public BigDecimal getGrandTotal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GrandTotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Imported.
		@param I_IsImported 
		Has this import been processed
	  */
	public void setI_IsImported (boolean I_IsImported)
	{
		set_Value (COLUMNNAME_I_IsImported, Boolean.valueOf(I_IsImported));
	}

	/** Get Imported.
		@return Has this import been processed
	  */
	public boolean isI_IsImported () 
	{
		Object oo = get_Value(COLUMNNAME_I_IsImported);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Confirmed.
		@param IsConfirmed 
		Assignment is confirmed
	  */
	public void setIsConfirmed (boolean IsConfirmed)
	{
		set_Value (COLUMNNAME_IsConfirmed, Boolean.valueOf(IsConfirmed));
	}

	/** Get Confirmed.
		@return Assignment is confirmed
	  */
	public boolean isConfirmed () 
	{
		Object oo = get_Value(COLUMNNAME_IsConfirmed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsOmitted.
		@param IsOmitted 
		Omitida si o no
	  */
	public void setIsOmitted (boolean IsOmitted)
	{
		set_Value (COLUMNNAME_IsOmitted, Boolean.valueOf(IsOmitted));
	}

	/** Get IsOmitted.
		@return Omitida si o no
	  */
	public boolean isOmitted () 
	{
		Object oo = get_Value(COLUMNNAME_IsOmitted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set LineNumber.
		@param LineNumber LineNumber	  */
	public void setLineNumber (int LineNumber)
	{
		set_Value (COLUMNNAME_LineNumber, Integer.valueOf(LineNumber));
	}

	/** Get LineNumber.
		@return LineNumber	  */
	public int getLineNumber () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LineNumber);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set VencCadena.
		@param VencCadena 
		Fecha de vencimiento en formato cadena de texto
	  */
	public void setVencCadena (String VencCadena)
	{
		set_Value (COLUMNNAME_VencCadena, VencCadena);
	}

	/** Get VencCadena.
		@return Fecha de vencimiento en formato cadena de texto
	  */
	public String getVencCadena () 
	{
		return (String)get_Value(COLUMNNAME_VencCadena);
	}

	/** Set Z_LoadInvoiceFile ID.
		@param Z_LoadInvoiceFile_ID Z_LoadInvoiceFile ID	  */
	public void setZ_LoadInvoiceFile_ID (int Z_LoadInvoiceFile_ID)
	{
		if (Z_LoadInvoiceFile_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_LoadInvoiceFile_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_LoadInvoiceFile_ID, Integer.valueOf(Z_LoadInvoiceFile_ID));
	}

	/** Get Z_LoadInvoiceFile ID.
		@return Z_LoadInvoiceFile ID	  */
	public int getZ_LoadInvoiceFile_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_LoadInvoiceFile_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_LoadInvoice getZ_LoadInvoice() throws RuntimeException
    {
		return (I_Z_LoadInvoice)MTable.get(getCtx(), I_Z_LoadInvoice.Table_Name)
			.getPO(getZ_LoadInvoice_ID(), get_TrxName());	}

	/** Set Z_LoadInvoice ID.
		@param Z_LoadInvoice_ID Z_LoadInvoice ID	  */
	public void setZ_LoadInvoice_ID (int Z_LoadInvoice_ID)
	{
		if (Z_LoadInvoice_ID < 1) 
			set_Value (COLUMNNAME_Z_LoadInvoice_ID, null);
		else 
			set_Value (COLUMNNAME_Z_LoadInvoice_ID, Integer.valueOf(Z_LoadInvoice_ID));
	}

	/** Get Z_LoadInvoice ID.
		@return Z_LoadInvoice ID	  */
	public int getZ_LoadInvoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_LoadInvoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}