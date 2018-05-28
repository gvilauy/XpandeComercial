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

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.acct.Doc;
import org.compiere.model.*;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.xpande.core.model.MZDocReference;

/** Generated Model for Z_GeneraNCPago
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class MZGeneraNCPago extends X_Z_GeneraNCPago implements DocAction, DocOptions {

	/**
	 *
	 */
	private static final long serialVersionUID = 20171025L;

    /** Standard Constructor */
    public MZGeneraNCPago (Properties ctx, int Z_GeneraNCPago_ID, String trxName)
    {
      super (ctx, Z_GeneraNCPago_ID, trxName);
    }

    /** Load Constructor */
    public MZGeneraNCPago (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

	@Override
	public int customizeValidActions(String docStatus, Object processing, String orderType, String isSOTrx, int AD_Table_ID, String[] docAction, String[] options, int index) {

		int newIndex = 0;

		if ((docStatus.equalsIgnoreCase(STATUS_Drafted))
				|| (docStatus.equalsIgnoreCase(STATUS_Invalid))
				|| (docStatus.equalsIgnoreCase(STATUS_InProgress))){

			options[newIndex++] = DocumentEngine.ACTION_Complete;

		}
		else if (docStatus.equalsIgnoreCase(STATUS_Completed)){

			options[newIndex++] = DocumentEngine.ACTION_None;
			//options[newIndex++] = DocumentEngine.ACTION_ReActivate;
			//options[newIndex++] = DocumentEngine.ACTION_Void;
		}

		return newIndex;
	}


	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName() + get_ID() +"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF

	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	processIt
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt()
	{
		log.info("unlockIt - " + toString());
	//	setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt
	
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt()
	{
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());

		/*
		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateDoc(), dt.getDocBaseType(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Invalid;
		}
		*/

		//	Add up Amounts
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt()
	{
		log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		log.info("rejectIt - " + toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		//	Implicit Approval
		if (!isApproved())
			approveIt();
		log.info(toString());
		//

		// Validaciones del documento
		m_processMsg = this.validateDocument();
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		// Obtengo socios de negocio a procesar
		List<MZGeneraNCPagoSocio> ncPagoSocios = this.getNCPagoSocios();
		for (MZGeneraNCPagoSocio ncPagoSocio: ncPagoSocios){

			// Obtengo documentos, de este socio de negocio, seleccionados para procesar
			List<MZGeneraNCPagoLin> ncPagoLinList = ncPagoSocio.getSelectedDocuments();

			// Si tengo documentos seleccionados para este socio de negocio
			if (ncPagoLinList.size() > 0){

				MDocType docType = (MDocType) this.getC_DocTypeTarget();

				// Genero cabezal de nota de crédito para este socio de negocio
				MInvoice invoiceNC = new MInvoice(getCtx(), 0, get_TrxName());
				invoiceNC.setC_DocTypeTarget_ID(this.getC_DocTypeTarget_ID());
				invoiceNC.setC_DocType_ID(this.getC_DocTypeTarget_ID());
				invoiceNC.set_ValueOfColumn("DocBaseType", docType.getDocBaseType());
				invoiceNC.set_ValueOfColumn("SubDocBaseType", "RET");
				invoiceNC.set_ValueOfColumn("DocumentSerie", "A");
				invoiceNC.setDocumentNo("NOREC-" + this.getDocumentNo());
				invoiceNC.setC_BPartner_ID(ncPagoSocio.getC_BPartner_ID());
				invoiceNC.setC_Currency_ID(this.getC_Currency_ID());
				invoiceNC.setDateInvoiced(this.getDateDoc());
				invoiceNC.setDateAcct(this.getDateDoc());
				invoiceNC.setIsSOTrx(false);
				invoiceNC.setIsTaxIncluded(true);
				invoiceNC.saveEx();

				// Recorro documentos (facturas)
				for (MZGeneraNCPagoLin ncPagoLin: ncPagoLinList){

					// Nueva referencia a factura
					MZInvoiceRef invoiceRef = new MZInvoiceRef(getCtx(), 0, get_TrxName());
					invoiceRef.setC_Invoice_ID(invoiceNC.get_ID());
					invoiceRef.setC_Invoice_To_ID(ncPagoLin.getC_Invoice_ID());
					invoiceRef.setAmtAllocation(ncPagoLin.getAmtDtoNC());
					invoiceRef.setAmtOpen(ncPagoLin.getAmtDocument());
					invoiceRef.setTotalAmt(ncPagoLin.getAmtDocument());
					invoiceRef.setC_Currency_ID(ncPagoLin.getC_Currency_ID());
					invoiceRef.setC_DocType_ID(ncPagoLin.getC_DocType_ID());
					invoiceRef.setDateDoc(ncPagoLin.getDateDoc());
					invoiceRef.setDueDate(ncPagoLin.getDueDateDoc());
					invoiceRef.setDocumentNoRef(ncPagoLin.getDocumentNoRef());
					invoiceRef.saveEx();

					// Obtengo y recorro productos de esta factura a considerar en la nota de crédito
					List<MZGeneraNCPagoProd> ncPagoProdList = ncPagoLin.getProductos();
					for (MZGeneraNCPagoProd ncPagoProd: ncPagoProdList){

						MProduct product = (MProduct) ncPagoProd.getM_Product();

						// Obtengo, si existe, linea de nota de crédito asociada a este producto.
						// Si no existe, creo nueva linea en la nota de cŕedito para este producto.
						MInvoiceLine invoiceLine = null;
						String whereClause = X_C_InvoiceLine.COLUMNNAME_C_Invoice_ID + " =" + invoiceNC.get_ID() +
								" AND " + X_C_InvoiceLine.COLUMNNAME_M_Product_ID + " =" + product.get_ID();
						int[] invoiceLineIDs = MInvoiceLine.getAllIDs(I_C_InvoiceLine.Table_Name, whereClause, get_TrxName());
						// No existe, creo linea para este producto
						if (invoiceLineIDs.length <= 0){
							invoiceLine = new MInvoiceLine(invoiceNC);
							invoiceLine.setM_Product_ID(product.get_ID());
							invoiceLine.setQtyEntered(Env.ONE);
							invoiceLine.setQtyInvoiced(Env.ONE);
							invoiceLine.setC_UOM_ID(product.getC_UOM_ID());
							invoiceLine.setPriceEntered(ncPagoProd.getAmtDtoNC());
							invoiceLine.setPriceActual(ncPagoProd.getAmtDtoNC());
						}
						else{
							invoiceLine = new MInvoiceLine(getCtx(), invoiceLineIDs[0], get_TrxName());
							invoiceLine.setPriceEntered(invoiceLine.getPriceEntered().add(ncPagoProd.getAmtDtoNC()));
						}
						invoiceLine.saveEx();
					}
				}

				// Completo documento de nota de crédito al pago
				if (!invoiceNC.processIt(DocAction.ACTION_Complete)){
					m_processMsg = invoiceNC.getProcessMsg();
					return DocAction.STATUS_Invalid;
				}
				invoiceNC.saveEx();

				// Genero relación entre este documento y la nota de credito
				MZDocReference docReference = new MZDocReference(getCtx(), 0, get_TrxName());
				docReference.setAD_Table_ID(I_Z_GeneraNCPago.Table_ID);
				docReference.setRecord_ID(this.get_ID());
				docReference.setC_Invoice_ID(invoiceNC.get_ID());
				docReference.setZ_GeneraNCPago_ID(this.get_ID());
				docReference.setC_BPartner_ID(invoiceNC.getC_BPartner_ID());
				docReference.setDocumentNoRef(invoiceNC.getDocumentNo());
				docReference.saveEx();
			}
		}


		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		//	Set Definitive Document No
		setDefiniteDocumentNo();

		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt


	/***
	 * Obtiene y retorna lista de socios de negocio a considerar en este proceso.
	 * Xpande. Created by Gabriel Vila on 10/25/17.
	 * @return
	 */
	private List<MZGeneraNCPagoSocio> getNCPagoSocios() {

		String whereClause = X_Z_GeneraNCPagoSocio.COLUMNNAME_Z_GeneraNCPago_ID + " =" + this.get_ID();

		List<MZGeneraNCPagoSocio> lines = new Query(getCtx(), I_Z_GeneraNCPagoSocio.Table_Name, whereClause, get_TrxName()).list();

		return lines;
	}


	/***
	 * Validaciones previas a completar este documento.
	 * Xpande. Created by Gabriel Vila on 10/25/17.
	 * @return
	 */
	private String validateDocument() {

		String message = null;
		String sql = "";

		try{
			// Verifico que tengo al menos una factura seleccionada para procesar
			sql = " select count(*) from z_generancpagolin where z_generancpago_id =" + this.get_ID() +
					" and isselected ='Y' ";
			int contador = DB.getSQLValueEx(get_TrxName(), sql);
			if (contador <= 0){
				return "No hay Facturas seleccionadas para procesar.";
			}

		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}

		return message;
	}

	/**
	 * 	Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setDateDoc(new Timestamp(System.currentTimeMillis()));
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = null;
			int index = p_info.getColumnIndex("C_DocType_ID");
			if (index == -1)
				index = p_info.getColumnIndex("C_DocTypeTarget_ID");
			if (index != -1)		//	get based on Doc Type (might return null)
				value = DB.getDocumentNo(get_ValueAsInt(index), get_TrxName(), true);
			if (value != null) {
				setDocumentNo(value);
			}
		}
	}

	/**
	 * 	Void Document.
	 * 	Same as Close.
	 * 	@return true if success 
	 */
	public boolean voidIt()
	{
		log.info("voidIt - " + toString());
		return closeIt();
	}	//	voidIt
	
	/**
	 * 	Close Document.
	 * 	Cancel not delivered Qunatities
	 * 	@return true if success 
	 */
	public boolean closeIt()
	{
		log.info("closeIt - " + toString());

		//	Close Not delivered Qty
		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt
	
	/**
	 * 	Reverse Correction
	 * 	@return true if success 
	 */
	public boolean reverseCorrectIt()
	{
		log.info("reverseCorrectIt - " + toString());
		return false;
	}	//	reverseCorrectionIt
	
	/**
	 * 	Reverse Accrual - none
	 * 	@return true if success 
	 */
	public boolean reverseAccrualIt()
	{
		log.info("reverseAccrualIt - " + toString());
		return false;
	}	//	reverseAccrualIt
	
	/** 
	 * 	Re-activate
	 * 	@return true if success 
	 */
	public boolean reActivateIt()
	{
		log.info("reActivateIt - " + toString());
		setProcessed(false);
		if (reverseCorrectIt())
			return true;
		return false;
	}	//	reActivateIt
	
	
	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
	//	sb.append(": ")
	//		.append(Msg.translate(getCtx(),"TotalLines")).append("=").append(getTotalLines())
	//		.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
	//	return getSalesRep_ID();
		return 0;
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return null;	//getTotalLines();
	}	//	getApprovalAmt
	
	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID()
	{
	//	MPriceList pl = MPriceList.get(getCtx(), getM_PriceList_ID());
	//	return pl.getC_Currency_ID();
		return super.getC_Currency_ID();
	}	//	getC_Currency_ID

    @Override
    public String toString()
    {
      StringBuffer sb = new StringBuffer ("MZGeneraNCPago[")
        .append(getSummary()).append("]");
      return sb.toString();
    }

	/***
	 * Obtiene y carga información de documentos a considerarse para la generación de notas de créito, agrupados por socio de negocio.
	 * Xpande. Created by Gabriel Vila on 10/25/17.
	 * @param tipoAccion
	 * @return
	 */
	public String getDocumentos(String tipoAccion) {

		String message = null;

		try{

			boolean getDocumentosNuevos = true;

			if (!tipoAccion.equalsIgnoreCase("NUEVOS")){
				getDocumentosNuevos = false;
			}

			// Elimino generacion anterior en caso de que el usuario asi lo indique
			if (!getDocumentosNuevos){
				this.deleteDocuments();
			}

			// Obtengo invoices a considerar y genero lineas
			message = this.getInvoices();
			if (message != null){
				return message;
			}

		}
		catch (Exception e){
			throw new AdempiereException(e);
		}

		return message;
	}


	/***
	 * Elimina documentos anteriores.
	 * Xpande. Created by Gabriel Vila on 10/25/17.
	 */
	private void deleteDocuments() {

		String action = "";
		try{

			action = " delete from z_generancpagosocio cascade where z_generancpago_id =" + this.get_ID();
			DB.executeUpdateEx(action, get_TrxName());

		}
		catch (Exception e){
			throw new AdempiereException(e);
		}

	}


	/***
	 * Obtiene invoices a considerar y carga información en tablas correspondientes.
	 * Xpande. Created by Gabriel Vila on 10/25/17.
	 * @return
	 */
	private String getInvoices() {

		String message = null;
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{

			String whereClause = "";

			// Filtros de fechas
			if (this.getDateEmittedFrom() != null){
				whereClause = " AND hdr.DateInvoiced >='" + this.getDateEmittedFrom() + "' ";
			}
			if (this.getDateEmittedTo() != null){
				whereClause = " AND hdr.DateInvoiced <='" + this.getDateEmittedTo() + "' ";
			}
			if (this.getDueDateFrom() != null){
				whereClause = " AND coalesce(coalesce(ips.duedate, paymentTermDueDate(hdr.C_PaymentTerm_ID, hdr.DateInvoiced)), hdr.dateinvoiced) >='" + this.getDueDateFrom() + "' ";
			}
			if (this.getDueDateTo() != null){
				whereClause = " AND coalesce(coalesce(ips.duedate, paymentTermDueDate(hdr.C_PaymentTerm_ID, hdr.DateInvoiced)), hdr.dateinvoiced) <='" + this.getDueDateTo() + "' ";
			}

			// Filtros de monedas
			String filtroMonedas = " AND hdr.c_currency_id =" + this.getC_Currency_ID();

			/*
			if (this.getC_Currency_2_ID() > 0){
				filtroMonedas = " AND hdr.c_currency_id IN (" + this.getC_Currency_ID() + ", " + this.getC_Currency_2_ID() + ") ";
			}
			*/

			whereClause += filtroMonedas;

			// Filtro de socios de negocio
			String filtroSocios = this.getFiltroSocios();
			if (filtroSocios != null){
				whereClause += " AND " + filtroSocios;
			}

			// Query
			sql = "  select hdr.c_bpartner_id, hdr.c_invoice_id, hdr.c_doctypetarget_id, (hdr.documentserie || hdr.documentno) as documentno,  hdr.dateinvoiced, hdr.c_currency_id, " +
					" coalesce(ips.dueamt,hdr.grandtotal) as grandtotal, " +
					" coalesce(coalesce(ips.duedate, paymentTermDueDate(hdr.C_PaymentTerm_ID, hdr.DateInvoiced)), hdr.dateinvoiced)::timestamp without time zone  as duedate " +
					" from c_invoice hdr " +
					" inner join c_bpartner bp on hdr.c_bpartner_id = bp.c_bpartner_id " +
					" inner join c_doctype doc on hdr.c_doctypetarget_id = doc.c_doctype_id  " +
					" left outer join c_invoicepayschedule ips on hdr.c_invoice_id = ips.c_invoice_id " +
					" where hdr.ad_client_id =" + this.getAD_Client_ID() +
					" and hdr.issotrx='N' " +
					" and hdr.docstatus='CO' " +
					" and hdr.tienedtosnc ='Y' " +
					" and hdr.c_invoice_id not in (select c_invoice_id from z_generancpagolin  where c_invoice_id is not null  and z_generancpago_id =" + this.get_ID() + ") " +
					" and hdr.c_invoice_id not in (select c_invoice_to_id from z_invoiceref a " +
					" inner join c_invoice b on a.c_invoice_id = b.c_invoice_id " +
					" where c_invoice_to_id is not null and b.docstatus='CO') " +
					whereClause +
					" order by hdr.c_bpartner_id ";

			int cBPartnerIDAux = 0;
			MZGeneraNCPagoSocio ncPagoSocio = null;

			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();

			while(rs.next()){

				// Corte por socio de negocio
				if (rs.getInt("c_bpartner_id") != cBPartnerIDAux){

					cBPartnerIDAux = rs.getInt("c_bpartner_id");

					// Obtengo modelo de socio a considerar en esta generación, si ya existe
					// Si no existe lo creo ahora
					ncPagoSocio = this.getNCPagoSocio(cBPartnerIDAux);
					if ((ncPagoSocio == null) || (ncPagoSocio.get_ID() <= 0)){
						MBPartner partner = new MBPartner(getCtx(), cBPartnerIDAux, null);
						ncPagoSocio = new MZGeneraNCPagoSocio(getCtx(), 0, get_TrxName());
						ncPagoSocio.setZ_GeneraNCPago_ID(this.get_ID());
						ncPagoSocio.setC_BPartner_ID(cBPartnerIDAux);
						ncPagoSocio.setTaxID(partner.getTaxID());
						ncPagoSocio.setC_Currency_ID(this.getC_Currency_ID());
						ncPagoSocio.setTotalAmt(Env.ZERO);
						ncPagoSocio.saveEx();
					}
				}

				BigDecimal amtDocument = rs.getBigDecimal("grandtotal");

				MZGeneraNCPagoLin ncPagoLin = new MZGeneraNCPagoLin(getCtx(), 0, get_TrxName());
				ncPagoLin.setZ_GeneraNCPago_ID(this.get_ID());
				ncPagoLin.setZ_GeneraNCPagoSocio_ID(ncPagoSocio.get_ID());
				ncPagoLin.setAmtDocument(amtDocument);
				ncPagoLin.setAmtDtoNC(amtDocument);
				ncPagoLin.setC_Currency_ID(rs.getInt("c_currency_id"));
				ncPagoLin.setC_DocType_ID(rs.getInt("c_doctypetarget_id"));
				ncPagoLin.setDateDoc(rs.getTimestamp("dateinvoiced"));
				ncPagoLin.setDueDateDoc(rs.getTimestamp("duedate"));
				ncPagoLin.setDocumentNoRef(rs.getString("documentno"));
				ncPagoLin.setC_Invoice_ID(rs.getInt("c_invoice_id"));

				ncPagoLin.saveEx();

				// Cargo para este factura, el detalle de productos a los cuales se les aplica el descuento con NC al pago
				BigDecimal totalDtos = Env.ZERO;
				List<MInvoiceLine> invoiceLines = this.getInvoiceLinesDto(ncPagoLin.getC_Invoice_ID());
				for (MInvoiceLine invoiceLine: invoiceLines){

					BigDecimal amtDtoNC = (BigDecimal) invoiceLine.get_Value("AmtDtoNC");
					if ((amtDtoNC != null) && (amtDtoNC.compareTo(Env.ZERO) > 0)){
						MZGeneraNCPagoProd ncPagoProd = new MZGeneraNCPagoProd(getCtx(), 0, get_TrxName());
						ncPagoProd.setZ_GeneraNCPagoLin_ID(ncPagoLin.get_ID());
						ncPagoProd.setAmtDtoNC(invoiceLine.getLineNetAmt().subtract(amtDtoNC));
						ncPagoProd.setC_InvoiceLine_ID(invoiceLine.get_ID());
						ncPagoProd.setLineNetAmt(invoiceLine.getLineNetAmt());
						ncPagoProd.setM_Product_ID(invoiceLine.getM_Product_ID());
						ncPagoProd.setQtyEntered(invoiceLine.getQtyEntered());
						ncPagoProd.setPriceEntered(invoiceLine.getPriceEntered());
						ncPagoProd.setPriceDtoNC((BigDecimal) invoiceLine.get_Value("PriceDtoNC"));
						ncPagoProd.setPorcDtoNC((BigDecimal) invoiceLine.get_Value("PorcDtoNC"));
						ncPagoProd.saveEx();

						totalDtos = totalDtos.add(ncPagoProd.getAmtDtoNC());
					}
				}
				if (totalDtos.compareTo(Env.ZERO) > 0){
					ncPagoLin.setAmtDtoNC(totalDtos);
					ncPagoLin.saveEx();
				}
				else{
					ncPagoLin.deleteEx(true);
				}

			}

		}
		catch (Exception e){
			throw new AdempiereException(e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return message;
	}

	/***
	 * Obtiene filtro de socios de negocio a aplicar para obtener documentos.
	 * Xpande. Created by Gabriel Vila on 10/25/17.
	 * @return
	 */
	private String getFiltroSocios() {

		String whereClause = null;

		try{

			String incluirSocios = " IN ";

			if (this.getTipoFiltroSocioPago().equalsIgnoreCase(X_Z_GeneraNCPago.TIPOFILTROSOCIOPAGO_EXCLUIRSOCIOSDENEGOCIOFILTRO)){
				incluirSocios = " NOT IN ";
			}

			// Verifico si tengo socios de negocio para filtrar
			String sql = " select count(*) from z_genncpagofiltrosocio where z_generancpago_id =" + this.get_ID();
			int contador = DB.getSQLValue(get_TrxName(), sql);

			// Si no tengo, no hago nada
			if (contador <= 0){
				return whereClause;
			}

			// Tengo socios de negocio para filtrar
			whereClause = " hdr.c_bpartner_id " + incluirSocios +
					" (select c_bpartner_id from z_genncpagofiltrosocio where z_generancpago_id =" + this.get_ID() + ") ";
		}
		catch (Exception e){
			throw new AdempiereException(e);
		}

		return whereClause;
	}

	/***
	 * Obtiene y retorna modelo de socio a considerar en este proceso según id de socio recibido.
	 * Xpande. Created by Gabriel Vila on 10/25/17.
	 * @param cBPartnerID
	 * @return
	 */
	private MZGeneraNCPagoSocio getNCPagoSocio(int cBPartnerID) {

		String whereClause = X_Z_GeneraNCPagoSocio.COLUMNNAME_Z_GeneraNCPago_ID + " =" + this.get_ID() +
				" AND " + X_Z_GeneraNCPagoSocio.COLUMNNAME_C_BPartner_ID + " =" + cBPartnerID;

		MZGeneraNCPagoSocio model = new Query(getCtx(), I_Z_GeneraNCPagoSocio.Table_Name, whereClause, get_TrxName()).first();

		return model;
	}


	/***
	 * Obtiene y retorna lineas de una determinada factura, que tienen descuentos por notas de crédito al pago.
	 * Xpande. Created by Gabriel Vila on 10/25/17.
	 * @param cInvoiceID
	 * @return
	 */
	private List<MInvoiceLine> getInvoiceLinesDto(int cInvoiceID) {

		String whereClause = X_C_InvoiceLine.COLUMNNAME_C_Invoice_ID + " =" + cInvoiceID + " AND TieneDtosNC ='Y' ";

		List<MInvoiceLine> lines = new Query(getCtx(), I_C_InvoiceLine.Table_Name, whereClause, get_TrxName()).list();

		return lines;
	}

}