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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.DateUtil;
import org.compiere.acct.Doc;
import org.compiere.impexp.ImpFormat;
import org.compiere.model.*;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.xpande.cfe.model.MZCFEConfig;
import org.xpande.comercial.utils.ComercialUtils;
import org.xpande.core.model.MZSocioListaPrecio;
import org.xpande.core.utils.DateUtils;
import org.xpande.core.utils.PriceListUtils;
import org.xpande.core.utils.TaxUtils;

/** Generated Model for Z_LoadInvoice
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class MZLoadInvoice extends X_Z_LoadInvoice implements DocAction, DocOptions {

	/**
	 *
	 */
	private static final long serialVersionUID = 20190228L;

    /** Standard Constructor */
    public MZLoadInvoice (Properties ctx, int Z_LoadInvoice_ID, String trxName)
    {
      super (ctx, Z_LoadInvoice_ID, trxName);
    }

    /** Load Constructor */
    public MZLoadInvoice (Properties ctx, ResultSet rs, String trxName)
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

			options[newIndex++] = DocumentEngine.ACTION_ReActivate;
			//options[newIndex++] = DocumentEngine.ACTION_None;
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

		// Obtengo lineas procesadas desde el archivo
		List<MZLoadInvoiceFile> loadInvoiceFileList = this.getLinesConfirmed();
		// Obtengo lineas cargadas manualmente
		List<MZLoadInvoiceMan> invoiceManList = this.getLinesManual();

		// Si no tengo lineas aviso y salgo.
		if (loadInvoiceFileList.size() <= 0){
			if (invoiceManList.size() <= 0){
				m_processMsg = "El documento no tiene lineas para procesar";
				return DocAction.STATUS_Invalid;
			}
		}

		// Recorro y proceso lineas leídas desde archivo
		for (MZLoadInvoiceFile loadInvoiceFile: loadInvoiceFileList){
			m_processMsg = this.processLines(loadInvoiceFile, null);
			if (m_processMsg != null){
				return DocAction.STATUS_Invalid;
			}
		}

		// Recorro y proceso lineas cargadas manualmente.
		int contadorLinMan = 0;
		for (MZLoadInvoiceMan invoiceMan: invoiceManList){
			m_processMsg = this.processLines(null, invoiceMan);
			if (m_processMsg != null){
				return DocAction.STATUS_Invalid;
			}
			contadorLinMan++;
		}

		// Actualizo contadores generales con cantidad de lineas manuales procesadas.
		if (contadorLinMan > 0){
			this.setQty(this.getQty() + contadorLinMan);
			this.setQtyCount(this.getQtyCount() + contadorLinMan);
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
	 * Procesa linea leída desde archivo o linea manual.
	 * Genera invoice para dicha linea.
	 * Xpande. Created by Gabriel Vila on 2/16/21.
	 * @param loadInvoiceFile
	 * @param loadInvoiceMan
	 * @return
	 */
	private String processLines(MZLoadInvoiceFile loadInvoiceFile, MZLoadInvoiceMan loadInvoiceMan){

		String action;

		try{
			int cBPartnerID = 0, cCurrencyID = 0, adOrgID = -1, cDocTypeID = 0;
			String documentSerie, documentNoRef, description;
			Timestamp dateInvoiced, dueDate;
			BigDecimal grandTotal = Env.ZERO;

			if (loadInvoiceFile != null){
				cBPartnerID = loadInvoiceFile.getC_BPartner_ID();
				cCurrencyID = loadInvoiceFile.getC_Currency_ID();
				adOrgID = loadInvoiceFile.getAD_OrgTrx_ID();
				cDocTypeID = loadInvoiceFile.getC_DocType_ID();
				documentSerie = loadInvoiceFile.getDocumentSerie();
				documentNoRef = loadInvoiceFile.getDocumentNoRef();
				description = loadInvoiceFile.getDescription();
				dateInvoiced = loadInvoiceFile.getDateInvoiced();
				dueDate = loadInvoiceFile.getDueDate();
				grandTotal = loadInvoiceFile.getGrandTotal();
			}
			else{
				cBPartnerID = loadInvoiceMan.getC_BPartner_ID();
				cCurrencyID = loadInvoiceMan.getC_Currency_ID();
				adOrgID = loadInvoiceMan.getAD_OrgTrx_ID();
				cDocTypeID = loadInvoiceMan.getC_DocType_ID();
				documentSerie = loadInvoiceMan.getDocumentSerie();
				documentNoRef = loadInvoiceMan.getDocumentNoRef();
				description = loadInvoiceMan.getDescription();
				dateInvoiced = loadInvoiceMan.getDateInvoiced();
				dueDate = loadInvoiceMan.getDueDate();
				grandTotal = loadInvoiceMan.getGrandTotal();
			}

			MZComercialConfig comercialConfig = MZComercialConfig.getDefault(getCtx(), null);
			MBPartner partner = new MBPartner(getCtx(), cBPartnerID, null);
			MCurrency currency = new MCurrency(getCtx(), cCurrencyID, null);
			MDocType docType = new MDocType(getCtx(), cDocTypeID, null);
			MPriceList pl = null;

			MBPartnerLocation partnerLocation = partner.getPrimaryC_BPartner_Location();
			if ((partnerLocation == null) || (partnerLocation.get_ID() <= 0)){
				return "No se obtuvo Localización para Socio de Negocio: " + partner.getName();
			}

			MInvoice invoiceAux = ComercialUtils.getInvoiceByDocPartner(getCtx(), adOrgID,
					cDocTypeID, documentSerie, documentNoRef, cBPartnerID, get_TrxName());
			if ((invoiceAux != null) && (invoiceAux.get_ID() > 0)){

				BigDecimal amtSubTotal = (BigDecimal) invoiceAux.get_Value("AmtSubtotal");
				invoiceAux.set_ValueOfColumn("AmtSubtotal", amtSubTotal.add(grandTotal));
				invoiceAux.setTotalLines(invoiceAux.getTotalLines().add(grandTotal));
				invoiceAux.setGrandTotal(invoiceAux.getGrandTotal().add(grandTotal));
				invoiceAux.saveEx();

				// Si el venvimiento que tengo es distinto a la fecha de emisión, debo guardarlo
				if (dueDate != null){
					if (dueDate.compareTo(dateInvoiced) != 0){
						MInvoicePaySchedule invoicePaySchedule = new MInvoicePaySchedule(getCtx(), 0, get_TrxName());
						invoicePaySchedule.setAD_Org_ID(invoiceAux.getAD_Org_ID());
						invoicePaySchedule.setC_Invoice_ID(invoiceAux.get_ID());
						invoicePaySchedule.setDueDate(dueDate);
						invoicePaySchedule.setDueAmt(grandTotal);
						invoicePaySchedule.setIsValid(true);
						invoicePaySchedule.saveEx();

						// En el save del vencimiento, el flag de valido se setea en False, por eso fuerzo el flag en true
						action = " update c_invoicepayschedule set isvalid ='Y' where c_invoicepayschedule_id =" + invoicePaySchedule.get_ID();
						DB.executeUpdateEx(action, get_TrxName());
					}
				}
				return null;
			}

			// Lista de precios segun compra/venta
			if (!this.isSOTrx()){

				// Si el comprobante es de PROVEEDORES debo asegurarme que hay lista de precios de compra para este socio - moneda.
				// Si no hay debo crearla.
				if (this.getTipoCargaInvoicePO().equalsIgnoreCase(X_Z_LoadInvoice.TIPOCARGAINVOICEPO_COMPROBANTESDEPROVEEDORES)){
					MZSocioListaPrecio bpl = MZSocioListaPrecio.getByPartnerCurrency(getCtx(), cBPartnerID, cCurrencyID, get_TrxName());
					if ((bpl == null) || (bpl.get_ID() <= 0)){
						if (this.isCrearListaPrecioBP()){
							String nombreLista = "LISTA " + partner.getName().toUpperCase() + " " + currency.getISO_Code();

							// Me aseguro por las dudas que no existe una lista con este nombre
							String sql = " select m_pricelist_id from m_pricelist where upper(name) ='" + nombreLista.toUpperCase().trim() + "'";
							int pricelistIDAux = DB.getSQLValueEx(get_TrxName(), sql);
							if (pricelistIDAux <= 0){
								// Creo nueva lista de compra para socio de negocio - moneda.
								pl = new MPriceList(getCtx(), 0, get_TrxName());
								pl.setName(nombreLista);
								pl.setC_Currency_ID(cCurrencyID);
								pl.setIsSOPriceList(false);
								pl.setIsTaxIncluded(true);
								pl.setIsNetPrice(false);
								pl.setPricePrecision(currency.getStdPrecision());
								pl.setAD_Org_ID(0);
								pl.saveEx();

								MPriceListVersion plv = new MPriceListVersion(pl);
								plv.setName("VIGENTE " + partner.getName().toUpperCase() + " " + currency.getISO_Code());
								plv.setM_DiscountSchema_ID(1000000);
								plv.saveEx();

								bpl =  new MZSocioListaPrecio(getCtx(), 0, get_TrxName());
								bpl.setC_BPartner_ID(cBPartnerID);
								bpl.setC_Currency_ID(cCurrencyID);
								bpl.setM_PriceList_ID(pl.get_ID());
								bpl.saveEx();
							}
							else{
								pl = new MPriceList(getCtx(), pricelistIDAux, get_TrxName());
							}
						}
						else{
							pl = PriceListUtils.getPriceListByOrg(getCtx(), this.getAD_Client_ID(), adOrgID, cCurrencyID, false, false, get_TrxName());
							if ((pl == null) || (pl.get_ID() <= 0)){
								// Veo si hay lista de precios generica con organizacion cero
								pl = PriceListUtils.getPriceListByOrg(getCtx(), this.getAD_Client_ID(), 0, cCurrencyID, false, false, get_TrxName());
								if ((pl == null) || (pl.get_ID() <= 0)){
									return "No se pudo obtener Lista de Precios de Compra para Moneda (" + cCurrencyID + " y Organización (ID: " + adOrgID + ")";
								}
							}
						}
					}
					else{
						pl = (MPriceList) bpl.getM_PriceList();
					}
				}
			}
			else{
				pl = PriceListUtils.getPriceListByOrg(getCtx(), this.getAD_Client_ID(), adOrgID, cCurrencyID, true, null, get_TrxName());
				if ((pl == null) || (pl.get_ID() <= 0)){
					// Veo si hay lista de precios generica con organizacion cero
					pl = PriceListUtils.getPriceListByOrg(getCtx(), this.getAD_Client_ID(), 0, cCurrencyID, true, null, get_TrxName());
					if ((pl == null) || (pl.get_ID() <= 0)){
						return  "No se pudo obtener Lista de Precios de Venta para Moneda (" + cCurrencyID + " y Organización (ID: " + adOrgID + ")";
					}
				}
			}

			// Genero cabezal de invoice
			MInvoice invoice = new MInvoice(getCtx(), 0, get_TrxName());
			invoice.setAD_Org_ID(adOrgID);
			invoice.setC_DocTypeTarget_ID(docType.get_ID());
			invoice.setC_DocType_ID(docType.get_ID());
			invoice.set_ValueOfColumn("DocBaseType", docType.getDocBaseType());

			if (!this.isSOTrx()){
				if (this.getTipoCargaInvoicePO().equalsIgnoreCase(X_Z_LoadInvoice.TIPOCARGAINVOICEPO_COMPROBANTESDEPROVEEDORES)){
					invoice.set_ValueOfColumn("SubDocBaseType", "RET");
				}
				else{
					invoice.set_ValueOfColumn("SubDocBaseType", null);
				}
			}
			else{
				if (docType.getDocBaseType().equalsIgnoreCase(Doc.DOCTYPE_ARCredit)){
					// Si es documento de venta y tipo nota de crédito, tengo que pnerle una referencia
					invoice.set_ValueOfColumn("ReferenciaCFE", "NOTA CREDITO");
				}
			}
			invoice.set_ValueOfColumn("TaxID", partner.getTaxID());
			invoice.set_ValueOfColumn("DocumentSerie", documentSerie);
			invoice.setDocumentNo(documentNoRef);
			invoice.setC_BPartner_ID(cBPartnerID);
			invoice.setC_BPartner_Location_ID(partnerLocation.get_ID());
			invoice.setC_Currency_ID(cCurrencyID);
			invoice.setDateInvoiced(dateInvoiced);
			invoice.setDateAcct(dateInvoiced);
			invoice.setIsSOTrx(this.isSOTrx());
			invoice.setIsTaxIncluded(true);
			invoice.setDescription(description);
			invoice.set_ValueOfColumn("AmtSubtotal", grandTotal);
			invoice.setTotalLines(grandTotal);
			invoice.setGrandTotal(grandTotal);

			if ((pl != null) && (pl.get_ID() > 0)){
				if (pl.getC_Currency_ID() <= 0){
					pl.setC_Currency_ID(cCurrencyID);
					pl.saveEx();
				}
				invoice.setM_PriceList_ID(pl.get_ID());
			}

			// Si no tengo que contabilizar las invoices generadas, la dejo como completa y posteada.
			if (!this.isContabilizar()){
				invoice.setDocStatus(DocAction.STATUS_Completed);
				invoice.setDocAction(DocAction.ACTION_None);
				invoice.setProcessed(true);
				invoice.setPosted(true);
			}

			invoice.set_ValueOfColumn("Z_LoadInvoice_ID", this.get_ID());
			invoice.saveEx();

			// Actualizo termino de pago por fuera del save para que lo tome
			if (comercialConfig.getC_PaymentTerm_ID() > 0){
				action = " update c_invoice set c_paymentterm_id =" + comercialConfig.getC_PaymentTerm_ID() +
						" where c_invoice_id =" + invoice.get_ID();
				DB.executeUpdateEx(action, get_TrxName());
			}

			// Si el venvimiento que tengo es distinto a la fecha de emisión, debo guardarlo
			if (dueDate != null){
				if (dueDate.compareTo(dateInvoiced) != 0){
					MInvoicePaySchedule invoicePaySchedule = new MInvoicePaySchedule(getCtx(), 0, get_TrxName());
					invoicePaySchedule.setAD_Org_ID(invoice.getAD_Org_ID());
					invoicePaySchedule.setC_Invoice_ID(invoice.get_ID());
					invoicePaySchedule.setDueDate(dueDate);
					invoicePaySchedule.setDueAmt(grandTotal);
					invoicePaySchedule.setIsValid(true);
					invoicePaySchedule.saveEx();

					// En el save del vencimiento, el flag de valido se setea en False, por eso fuerzo el flag en true
					action = " update c_invoicepayschedule set isvalid ='Y' where c_invoicepayschedule_id =" + invoicePaySchedule.get_ID();
					DB.executeUpdateEx(action, get_TrxName());
				}
			}

			// Si tengo que contabilizar, agrego lineas para producto indicado, y mando a completar el comprobante para que contabilize.
			if (this.isContabilizar()){
				MProduct product = (MProduct) this.getM_Product();
				MInvoiceLine invoiceLine = new MInvoiceLine(invoice);
				invoiceLine.setAD_Org_ID(adOrgID);
				invoiceLine.setM_Product_ID(this.getM_Product_ID());

				invoiceLine.setQtyEntered(Env.ONE);
				invoiceLine.setQtyInvoiced(Env.ONE);
				invoiceLine.setC_UOM_ID(100);

				if (!this.isSOTrx()){
					invoiceLine.set_ValueOfColumn("PricePO", grandTotal);
					invoiceLine.set_ValueOfColumn("PricePONoDto", grandTotal);
				}
				invoiceLine.setPriceEntered(grandTotal);
				invoiceLine.setPriceActual(grandTotal);
				invoiceLine.setPriceLimit(grandTotal);
				invoiceLine.setPriceList(grandTotal);

				MTax tax = TaxUtils.getDefaultTaxByCategory(getCtx(), product.getC_TaxCategory_ID(), null);
				if ((tax != null) && (tax.get_ID() > 0)){
					invoiceLine.setC_Tax_ID(tax.get_ID());
				}
				else{
					invoiceLine.setTax();
				}

				invoiceLine.setTaxAmt();
				invoiceLine.set_ValueOfColumn("AmtSubtotal", grandTotal);
				invoiceLine.setLineNetAmt(grandTotal);
				invoiceLine.saveEx();

				if (!invoice.processIt(DocAction.ACTION_Complete)){
					m_processMsg = invoice.getProcessMsg();
					return DocAction.STATUS_Invalid;
				}
			}

			if (loadInvoiceFile != null){
				loadInvoiceFile.setC_Invoice_ID(invoice.get_ID());
				loadInvoiceFile.saveEx();
			}
			else{
				loadInvoiceMan.setC_Invoice_ID(invoice.get_ID());
				loadInvoiceMan.saveEx();
			}
		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}
		return null;
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

		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;

		// Reactivo invoices completadas y asociadas a esta carga
		List<MInvoice> invoiceCompletosList = this.getInvoicesCompleted();
		for (MInvoice invoice: invoiceCompletosList){
			if (!invoice.processIt(DocAction.ACTION_ReActivate)){
				m_processMsg = "No se pudo reactivar el comprobante numero : " + invoice.getDocumentNo();
				return false;
			}
		}

		// Elimino todos las invoices asociadas a esta carga
		String action = " delete from c_invoice where z_loadinvoice_id =" + this.get_ID();
		DB.executeUpdateEx(action, get_TrxName());

		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;

		this.setProcessed(false);
		this.setDocStatus(DOCSTATUS_InProgress);
		this.setDocAction(DOCACTION_Complete);

		return true;
	}	//	reActivateIt

	/***
	 * Obtiene y retorna invoices generadas en esta carga.
	 * Xpande. Created by Gabriel Vila on 2/16/21.
 	 * @return
	 */
	private List<MInvoice> getInvoicesCompleted() {

		String whereClause = "z_loadinvoice_id =" + this.get_ID() +
				" AND " + X_C_Invoice.COLUMNNAME_DocStatus + " ='CO'";

		List<MInvoice> lines = new Query(getCtx(), I_C_Invoice.Table_Name, whereClause, get_TrxName()).list();
		return lines;
	}

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
		return 0;
	}	//	getC_Currency_ID

    @Override
    public String toString()
    {
      StringBuffer sb = new StringBuffer ("MZLoadInvoice[")
        .append(getSummary()).append("]");
      return sb.toString();
    }


	/***
	 * Metodo que ejecuta el proceso de interface desde archivo para carga de comprobantes comerciales.
	 * Xpande. Created by Gabriel Vila on 2/28/19.
	 */
	public void executeInterface(){

		try{

			// Elimino información anterior.
			this.deleteFileData();

			// Lee lineas de archivo
			this.getDataFromFile();

			// Valida lineas de archivo y trae información asociada.
			this.setDataFromFile();

		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}
	}


	/***
	 * Obtiene y retorna lineas confirmadas de este documento.
	 * Xpande. Created by Gabriel Vila on 2/28/19.
	 * @return
	 */
	public List<MZLoadInvoiceFile> getLinesConfirmed(){

		String whereClause = X_Z_LoadInvoiceFile.COLUMNNAME_Z_LoadInvoice_ID + " =" + this.get_ID() +
				" AND " + X_Z_LoadInvoiceFile.COLUMNNAME_IsConfirmed + " ='Y'";

		List<MZLoadInvoiceFile> lines = new Query(getCtx(), I_Z_LoadInvoiceFile.Table_Name, whereClause, get_TrxName()).list();

		return lines;
	}

	/***
	 * Obtiene y retorna lineas de este documento.
	 * Xpande. Created by Gabriel Vila on 2/28/19.
	 * @return
	 */
	public List<MZLoadInvoiceFile> getLines(){

		String whereClause = X_Z_LoadInvoiceFile.COLUMNNAME_Z_LoadInvoice_ID + " =" + this.get_ID();

		List<MZLoadInvoiceFile> lines = new Query(getCtx(), I_Z_LoadInvoiceFile.Table_Name, whereClause, get_TrxName()).list();

		return lines;
	}


	/***
	 * Obtiene y retorna lista de comprobantes cargados manualmente.
	 * Xpande. Created by Gabriel Vila on 3/4/19.
	 * @return
	 */
	public List<MZLoadInvoiceMan> getLinesManual(){

		String whereClause = X_Z_LoadInvoiceMan.COLUMNNAME_Z_LoadInvoice_ID + " =" + this.get_ID();

		List<MZLoadInvoiceMan> lines = new Query(getCtx(), I_Z_LoadInvoiceMan.Table_Name, whereClause, get_TrxName()).list();

		return lines;
	}


	/***
	 * Valida lineas leídas desde archivo y carga información asociada.
	 * Xpande. Created by Gabriel Vila on 2/28/19.
	 */
	private void setDataFromFile() {

		try{

			MZCFEConfig cfeConfig = MZCFEConfig.getDefault(getCtx(), null);
			if (this.isSOTrx()){
				if ((cfeConfig == null) || (cfeConfig.get_ID() <= 0)){
					throw new AdempiereException("Falta parametrización en el sistema para conceptos de CFE");
				}
			}

			int contadorOK = 0;
			int contadorError = 0;

			List<MZLoadInvoiceFile> loadInvoiceFileList = this.getLines();
			for (MZLoadInvoiceFile loadInvoiceFile : loadInvoiceFileList){

				if (loadInvoiceFile.getErrorMsg() != null){
					contadorError++;
					continue;
				}

				loadInvoiceFile.setIsConfirmed(true);

				if (loadInvoiceFile.getAD_OrgTrx_ID() <= 0){
					loadInvoiceFile.setIsConfirmed(false);
					loadInvoiceFile.setErrorMsg("No existe Organización con ese Número o la misma debe ser distinta de CERO.");
				}
				else{
					MOrg orgTrx = new MOrg(getCtx(), loadInvoiceFile.getAD_OrgTrx_ID(), null);
					if ((orgTrx == null) || (orgTrx.get_ID() <= 0)){
						loadInvoiceFile.setIsConfirmed(false);
						loadInvoiceFile.setErrorMsg("No existe Organización con ese Número");
					}
				}

				if ((loadInvoiceFile.getTaxID() == null) || (loadInvoiceFile.getTaxID().trim().equalsIgnoreCase(""))){
					loadInvoiceFile.setIsConfirmed(false);
					loadInvoiceFile.setErrorMsg("Debe indicar Número de Identificación del Socio de Negocio, en la Linea del Archivo");
				}
				else{
					MBPartner partner = ComercialUtils.getPartnerByTaxID(getCtx(), loadInvoiceFile.getTaxID(), null);
					if ((partner == null) || (partner.get_ID() <= 0)){
						loadInvoiceFile.setIsConfirmed(false);
						loadInvoiceFile.setErrorMsg("No existe Socio de Negocio definido en el sistema con ese Número de Identificación : " + loadInvoiceFile.getTaxID());
					}
					else{
						loadInvoiceFile.setC_BPartner_ID(partner.get_ID());
					}
				}

				if (loadInvoiceFile.getC_DocType_ID() <= 0){
					loadInvoiceFile.setIsConfirmed(false);
					loadInvoiceFile.setErrorMsg("Debe indicar Tipo de Documento");
				}
				else{
					MDocType docType = new MDocType(getCtx(), loadInvoiceFile.getC_DocType_ID(), null);
					if ((docType == null) || (docType.get_ID() <= 0)){
						loadInvoiceFile.setIsConfirmed(false);
						loadInvoiceFile.setErrorMsg("No existe Tipo de Documento definido en el sistema con ese Número : " + loadInvoiceFile.getC_DocType_ID());
					}
					if ((this.isSOTrx() && !docType.isSOTrx()) || (!this.isSOTrx() && docType.isSOTrx())){
						loadInvoiceFile.setIsConfirmed(false);
						loadInvoiceFile.setErrorMsg("El Tipo de Documento ingresado no es igual a la opción Compra/Venta de este Documento: " + loadInvoiceFile.getC_DocType_ID());
					}
					// Si estoy procesano comprobantes de venta, me aseguro que el documento recibido no EMITA CFE !!.
					if (this.isSOTrx()){
						if (cfeConfig.isDocSendCFE(loadInvoiceFile.getAD_OrgTrx_ID(), docType.get_ID())){
							loadInvoiceFile.setIsConfirmed(false);
							loadInvoiceFile.setErrorMsg("El Tipo de Documento ingresado NO debe emitir Facturación Electrónica (CFE): " + loadInvoiceFile.getC_DocType_ID());
						}
					}
				}

				if ((loadInvoiceFile.getDocumentSerie() == null) || (loadInvoiceFile.getDocumentSerie().trim().equalsIgnoreCase(""))){
					if (!this.isSOTrx()){
						loadInvoiceFile.setIsConfirmed(false);
						loadInvoiceFile.setErrorMsg("Debe indicar Serie del Documento");
					}
				}

				if ((loadInvoiceFile.getDocumentNoRef() == null) || (loadInvoiceFile.getDocumentNoRef().trim().equalsIgnoreCase(""))){
					loadInvoiceFile.setIsConfirmed(false);
					loadInvoiceFile.setErrorMsg("Debe indicar Número del Documento");
				}

				// Valido que no existe un comprobante con ese socio de negocio, tipo de documento, serie y numero.
				MInvoice invoice = ComercialUtils.getInvoiceByDocPartner(getCtx(), loadInvoiceFile.getAD_OrgTrx_ID(), loadInvoiceFile.getC_DocType_ID(), loadInvoiceFile.getDocumentSerie(), loadInvoiceFile.getDocumentNoRef(), loadInvoiceFile.getC_BPartner_ID(), get_TrxName());
				if ((invoice != null) && (invoice.get_ID() > 0)){
					loadInvoiceFile.setIsConfirmed(false);
					loadInvoiceFile.setErrorMsg("Ya existe un comprobante en el sistema para ese Socio de Negocio - Documento");
				}

				if ((loadInvoiceFile.getFechaCadena() == null) || (loadInvoiceFile.getFechaCadena().trim().equalsIgnoreCase(""))){
					loadInvoiceFile.setIsConfirmed(false);
					loadInvoiceFile.setErrorMsg("Debe indicar Fecha del Comprobante");
				}
				else{
					Timestamp fecDoc = DateUtils.convertStringToTimestamp_ddMMyyyy(loadInvoiceFile.getFechaCadena());
					if (fecDoc == null){
						loadInvoiceFile.setIsConfirmed(false);
						loadInvoiceFile.setErrorMsg("Formato de Fecha de comprabante inválida : " + loadInvoiceFile.getFechaCadena());
					}
					loadInvoiceFile.setDateInvoiced(fecDoc);
				}

				if ((loadInvoiceFile.getVencCadena() == null) || (loadInvoiceFile.getVencCadena().trim().equalsIgnoreCase(""))){
					loadInvoiceFile.setIsConfirmed(false);
					loadInvoiceFile.setErrorMsg("Debe indicar Fecha de Vencimiento del Comprobante");
				}
				else{
					Timestamp fecVenc = DateUtils.convertStringToTimestamp_ddMMyyyy(loadInvoiceFile.getVencCadena());
					if (fecVenc == null){
						loadInvoiceFile.setIsConfirmed(false);
						loadInvoiceFile.setErrorMsg("Formato de Fecha de Vencimiento de comprabante inválida : " + loadInvoiceFile.getVencCadena());
					}
					loadInvoiceFile.setDueDate(fecVenc);
				}

				if ((loadInvoiceFile.getGrandTotal() == null) || (loadInvoiceFile.getGrandTotal().compareTo(Env.ZERO) == 0)){
					loadInvoiceFile.setIsConfirmed(false);
					loadInvoiceFile.setErrorMsg("Debe indicar Saldo Pendiente del Comprobante");
				}
				else {
					if (loadInvoiceFile.getGrandTotal().compareTo(Env.ZERO) < 0){
						loadInvoiceFile.setGrandTotal(loadInvoiceFile.getGrandTotal().negate());
					}
				}

				MCurrency currency = new MCurrency(getCtx(), loadInvoiceFile.getC_Currency_ID(), null);
				if ((currency == null) || (currency.get_ID() <= 0)){
					loadInvoiceFile.setIsConfirmed(false);
					loadInvoiceFile.setErrorMsg("No existe Moneda definida en el sistema con ese número : " + loadInvoiceFile.getC_Currency_ID());
				}

				if (loadInvoiceFile.isConfirmed()){
					contadorOK++;
				}
				else{
					contadorError++;
				}

				loadInvoiceFile.saveEx();
			}

			this.setQty(contadorOK);
			this.setQtyReject(contadorError);
			this.saveEx();

		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}
	}


	/***
	 * Elimina información leída desde archivo.
	 * Xpande. Created by Gabriel Vila on 2/28/19.
	 */
	private void deleteFileData() {

		String action = "";

		try{
			action = " delete from " + I_Z_LoadInvoiceFile.Table_Name +
					 " where " + X_Z_LoadInvoiceFile.COLUMNNAME_Z_LoadInvoice_ID + " =" + this.get_ID();
			DB.executeUpdateEx(action, get_TrxName());
		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}

	}


	/***
	 * Proceso que lee archivo de interface.
	 * Xpande. Created by Gabriel Vila on 2/28/19.
	 */
	public void getDataFromFile() {

		FileReader fReader = null;
		BufferedReader bReader = null;

		String lineaArchivo = null;
		String mensaje = "";
		String action = "";


		try{

			// Formato de importación de archivo de interface comprobantes comerciales
			ImpFormat formatoImpArchivo = ImpFormat.load("Comercial_CargaComprobantes");

			// Abro archivo
			File archivo = new File(this.getFileName());
			fReader = new FileReader(archivo);
			bReader = new BufferedReader(fReader);

			int contLineas = 0;
			int lineaID = 0;

			// Leo lineas del archivo
			lineaArchivo = bReader.readLine();

			while (lineaArchivo != null) {

				lineaArchivo = lineaArchivo.replace("'", "");
				//lineaArchivo = lineaArchivo.replace(",", "");
				contLineas++;

				lineaID = formatoImpArchivo.updateDB(lineaArchivo, getCtx(), get_TrxName());

				if (lineaID <= 0){
					MZLoadInvoiceFile loadInvoiceFile = new MZLoadInvoiceFile(getCtx(), 0, get_TrxName());
					loadInvoiceFile.setZ_LoadInvoice_ID(this.get_ID());
					loadInvoiceFile.setLineNumber(contLineas);
					loadInvoiceFile.setFileLineText(lineaArchivo);
					loadInvoiceFile.setIsConfirmed(false);
					loadInvoiceFile.setErrorMsg("Formato de Linea Incorrecto.");
					loadInvoiceFile.saveEx();
					/*
					mensaje = "Error al procesar linea " + contLineas + " : " + lineaArchivo;
					throw new AdempiereException(mensaje);
					*/
				}
				else{
					// Seteo atributos de linea procesada en tabla
					action = " update " + I_Z_LoadInvoiceFile.Table_Name +
							" set " + X_Z_LoadInvoiceFile.COLUMNNAME_Z_LoadInvoice_ID + " = " + this.get_ID() + ", " +
							" LineNumber =" + contLineas + ", " +
							" FileLineText ='" + lineaArchivo + "' " +
							" where " + X_Z_LoadInvoiceFile.COLUMNNAME_Z_LoadInvoiceFile_ID + " = " + lineaID;
					DB.executeUpdateEx(action, get_TrxName());
				}

				lineaArchivo = bReader.readLine();
			}

			this.setQtyCount(contLineas);
			this.saveEx();

		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}
		finally {
			if (bReader != null){
				try{
					bReader.close();
					if (fReader != null){
						fReader.close();
					}
				}
				catch (Exception e){
					log.log(Level.SEVERE, e.getMessage());
				}
			}
		}
	}

}