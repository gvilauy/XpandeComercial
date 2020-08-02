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

/** Generated Model for Z_GeneraEntrega
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_GeneraEntrega extends PO implements I_Z_GeneraEntrega, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200801L;

    /** Standard Constructor */
    public X_Z_GeneraEntrega (Properties ctx, int Z_GeneraEntrega_ID, String trxName)
    {
      super (ctx, Z_GeneraEntrega_ID, trxName);
      /** if (Z_GeneraEntrega_ID == 0)
        {
			setC_DocType_ID (0);
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setIsApproved (false);
// N
			setIsStockDisponible (true);
// Y
			setProcessed (false);
// N
			setProcessing (false);
// N
			setShowFilters (true);
// Y
			setTipoGeneraEntrega (null);
// PRODUCT
			setZ_GeneraEntrega_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_GeneraEntrega (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_GeneraEntrega[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set DateOrderedFrom.
		@param DateOrderedFrom 
		Fecha orden desde
	  */
	public void setDateOrderedFrom (Timestamp DateOrderedFrom)
	{
		set_Value (COLUMNNAME_DateOrderedFrom, DateOrderedFrom);
	}

	/** Get DateOrderedFrom.
		@return Fecha orden desde
	  */
	public Timestamp getDateOrderedFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateOrderedFrom);
	}

	/** Set DateOrderedTo.
		@param DateOrderedTo 
		Fecha Orden Hasta
	  */
	public void setDateOrderedTo (Timestamp DateOrderedTo)
	{
		set_Value (COLUMNNAME_DateOrderedTo, DateOrderedTo);
	}

	/** Get DateOrderedTo.
		@return Fecha Orden Hasta
	  */
	public Timestamp getDateOrderedTo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateOrderedTo);
	}

	/** Set DatePromisedFrom.
		@param DatePromisedFrom 
		Fecha prometida desde
	  */
	public void setDatePromisedFrom (Timestamp DatePromisedFrom)
	{
		set_Value (COLUMNNAME_DatePromisedFrom, DatePromisedFrom);
	}

	/** Get DatePromisedFrom.
		@return Fecha prometida desde
	  */
	public Timestamp getDatePromisedFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DatePromisedFrom);
	}

	/** Set DatePromisedTo.
		@param DatePromisedTo 
		Fecha prometida hasta
	  */
	public void setDatePromisedTo (Timestamp DatePromisedTo)
	{
		set_Value (COLUMNNAME_DatePromisedTo, DatePromisedTo);
	}

	/** Get DatePromisedTo.
		@return Fecha prometida hasta
	  */
	public Timestamp getDatePromisedTo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DatePromisedTo);
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

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID=135;
	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";
	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";
	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";
	/** Post = PO */
	public static final String DOCACTION_Post = "PO";
	/** Void = VO */
	public static final String DOCACTION_Void = "VO";
	/** Close = CL */
	public static final String DOCACTION_Close = "CL";
	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";
	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";
	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";
	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";
	/** <None> = -- */
	public static final String DOCACTION_None = "--";
	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";
	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";
	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";
	/** Set Document Action.
		@param DocAction 
		The targeted status of the document
	  */
	public void setDocAction (String DocAction)
	{

		set_Value (COLUMNNAME_DocAction, DocAction);
	}

	/** Get Document Action.
		@return The targeted status of the document
	  */
	public String getDocAction () 
	{
		return (String)get_Value(COLUMNNAME_DocAction);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set Approved.
		@param IsApproved 
		Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved)
	{
		set_Value (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
	}

	/** Get Approved.
		@return Indicates if this document requires approval
	  */
	public boolean isApproved () 
	{
		Object oo = get_Value(COLUMNNAME_IsApproved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsStockDisponible.
		@param IsStockDisponible 
		Si tiene en cuenta o no el stock disponible de mercaderia
	  */
	public void setIsStockDisponible (boolean IsStockDisponible)
	{
		set_Value (COLUMNNAME_IsStockDisponible, Boolean.valueOf(IsStockDisponible));
	}

	/** Get IsStockDisponible.
		@return Si tiene en cuenta o no el stock disponible de mercaderia
	  */
	public boolean isStockDisponible () 
	{
		Object oo = get_Value(COLUMNNAME_IsStockDisponible);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ShowFilters.
		@param ShowFilters 
		Si se muestran o no filltros
	  */
	public void setShowFilters (boolean ShowFilters)
	{
		set_Value (COLUMNNAME_ShowFilters, Boolean.valueOf(ShowFilters));
	}

	/** Get ShowFilters.
		@return Si se muestran o no filltros
	  */
	public boolean isShowFilters () 
	{
		Object oo = get_Value(COLUMNNAME_ShowFilters);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set TextoFiltro.
		@param TextoFiltro 
		Texto genérico para filtro de valores
	  */
	public void setTextoFiltro (String TextoFiltro)
	{
		set_Value (COLUMNNAME_TextoFiltro, TextoFiltro);
	}

	/** Get TextoFiltro.
		@return Texto genérico para filtro de valores
	  */
	public String getTextoFiltro () 
	{
		return (String)get_Value(COLUMNNAME_TextoFiltro);
	}

	/** TipoGeneraEntrega AD_Reference_ID=1000047 */
	public static final int TIPOGENERAENTREGA_AD_Reference_ID=1000047;
	/** PRODUCTO = PRODUCT */
	public static final String TIPOGENERAENTREGA_PRODUCTO = "PRODUCT";
	/** SOCIO DE NEGOCIO = PARTNER */
	public static final String TIPOGENERAENTREGA_SOCIODENEGOCIO = "PARTNER";
	/** Set TipoGeneraEntrega.
		@param TipoGeneraEntrega 
		Lista de valores posibles para la forma de procesar la generación de entregas / reservas.
	  */
	public void setTipoGeneraEntrega (String TipoGeneraEntrega)
	{

		set_Value (COLUMNNAME_TipoGeneraEntrega, TipoGeneraEntrega);
	}

	/** Get TipoGeneraEntrega.
		@return Lista de valores posibles para la forma de procesar la generación de entregas / reservas.
	  */
	public String getTipoGeneraEntrega () 
	{
		return (String)get_Value(COLUMNNAME_TipoGeneraEntrega);
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

	public I_Z_CanalVenta getZ_CanalVenta() throws RuntimeException
    {
		return (I_Z_CanalVenta)MTable.get(getCtx(), I_Z_CanalVenta.Table_Name)
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

	/** Set Z_GeneraEntrega ID.
		@param Z_GeneraEntrega_ID Z_GeneraEntrega ID	  */
	public void setZ_GeneraEntrega_ID (int Z_GeneraEntrega_ID)
	{
		if (Z_GeneraEntrega_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_GeneraEntrega_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_GeneraEntrega_ID, Integer.valueOf(Z_GeneraEntrega_ID));
	}

	/** Get Z_GeneraEntrega ID.
		@return Z_GeneraEntrega ID	  */
	public int getZ_GeneraEntrega_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_GeneraEntrega_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}