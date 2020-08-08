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
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;

/** Generated Model for Z_ReservaVta
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class MZReservaVta extends X_Z_ReservaVta implements DocAction, DocOptions {

	/**
	 *
	 */
	private static final long serialVersionUID = 20200802L;

    /** Standard Constructor */
    public MZReservaVta (Properties ctx, int Z_ReservaVta_ID, String trxName)
    {
      super (ctx, Z_ReservaVta_ID, trxName);
    }

    /** Load Constructor */
    public MZReservaVta (Properties ctx, ResultSet rs, String trxName)
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

			//options[newIndex++] = DocumentEngine.ACTION_None;
			options[newIndex++] = DocumentEngine.ACTION_ReActivate;
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

		// Obtengo lineas de este documento
		List<MZReservaVtaLin> vtaLinList = this.getLines();

		// Si no tengo lineas a procesar, salgo y aviso.
		if (vtaLinList.size() <= 0){
			m_processMsg = "Este Documento no tiene lineas para procesar.";
			return DocAction.STATUS_Invalid;
		}

		String sql = "", action = "";
		MWarehouse warehouse = (MWarehouse) this.getM_Warehouse();

		// Recorro, valido y proceso lineas
		for (MZReservaVtaLin vtaLin: vtaLinList){

			MOrderLine orderLine = (MOrderLine) vtaLin.getC_OrderLine();
			MProduct product = (MProduct) vtaLin.getM_Product();
			MUOM uomProd = (MUOM) product.getC_UOM();

			// Obtengo disponible actual para este producto
			sql = " select z_stk_warproddisp(" + this.getAD_Client_ID() + ", " + this.getAD_Org_ID() + ", " +
					this.getM_Warehouse_ID()  + ", " + product.get_ID() + ")";
			BigDecimal qtyAvailableProd = DB.getSQLValueBDEx(null, sql);
			if (qtyAvailableProd == null) qtyAvailableProd = Env.ZERO;

			// Precision según unidad de medida del producto
			qtyAvailableProd = qtyAvailableProd.setScale(uomProd.getStdPrecision(), RoundingMode.HALF_UP);

			// Si la cantidad disponible actual es menor a la cantidad asignada, aviso y no hago nada
			if (qtyAvailableProd.compareTo(vtaLin.getQtyReserved()) < 0){

				MUOM umoVta = (MUOM) vtaLin.getC_UOM();

				BigDecimal qtyAvailableProdEnt = qtyAvailableProd.multiply(vtaLin.getUomMultiplyRate()).setScale(umoVta.getStdPrecision(), RoundingMode.HALF_UP);

				m_processMsg = "La cantidad disponible del producto " + product.getValue() + " - " + product.getName() +
						" ha variado y no alcanza para la cantidad a procesar.\n " +
						" Disponible actual = " + qtyAvailableProdEnt + ", cantidad a procesar = " + vtaLin.getQtyReservedEnt();
				return DocAction.STATUS_Invalid;
			}

			// Actualizo Storage para la reserva
			if (product.isStocked())
			{
				//	Mandatory Product Attribute Set Instance
				MAttributeSet.validateAttributeSetInstanceMandatory(product, I_C_OrderLine.Table_ID, true, orderLine.getM_AttributeSetInstance_ID());

				int mLocatorID = 0;

				//	Get Locator to reserve
				if (orderLine.getM_AttributeSetInstance_ID() != 0){
					//	Get existing Location
					mLocatorID = MStorage.getM_Locator_ID (orderLine.getM_Warehouse_ID(), orderLine.getM_Product_ID(), orderLine.getM_AttributeSetInstance_ID(), Env.ZERO, get_TrxName());
				}

				//	Get default Location
				if (mLocatorID == 0)
				{
					// try to take default locator for product first
					// if it is from the selected warehouse
					mLocatorID = product.getM_Locator_ID();
					if ( mLocatorID != 0) {
						MLocator locator = new MLocator(getCtx(), product.getM_Locator_ID(), get_TrxName());
						//product has default locator defined but is not from the order warehouse
						if(locator.getM_Warehouse_ID() != warehouse.get_ID()) {
							mLocatorID = warehouse.getDefaultLocator().getM_Locator_ID();
						}
					}
					else {
						mLocatorID = warehouse.getDefaultLocator().getM_Locator_ID();
					}
				}

				//	Update Storage
				if (!MStorage.add(getCtx(), warehouse.get_ID(), mLocatorID, orderLine.getM_Product_ID(),
						orderLine.getM_AttributeSetInstance_ID(), orderLine.getM_AttributeSetInstance_ID(),
						Env.ZERO, vtaLin.getQtyReserved(), Env.ZERO, get_TrxName())){

					m_processMsg = "No se pudo generar la reserva para el producto : " + product.getValue() + " - " + product.getName();
					return DocAction.STATUS_Invalid;
				}

				// Actualizo cantidad reservada de la linea de la orden de venta
				action = " update c_orderline set qtyreserved = qtyreserved + " + vtaLin.getQtyReserved() +
						" where c_orderline_id =" + vtaLin.getC_OrderLine_ID();
				DB.executeUpdateEx(action, get_TrxName());
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
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;

		// Valido que esta reserva no este asociada a un documento de Asignación de Transporte.
		if (this.getZ_AsignaTrLog_ID() > 0){
			m_processMsg = "No es posible Reactivar esta Reserva ya que asociada a una Asignación de Transporte.\n" +
					"Debe Eliminar dicha Asignación antes de Reactivar esta Reserva.";
			return false;
		}

		// Obtengo lineas de este documento
		List<MZReservaVtaLin> vtaLinList = this.getLines();

		String action = "";
		MWarehouse warehouse = (MWarehouse) this.getM_Warehouse();

		// Recorro lineas y deshago reserva en Storage y en linea de orden de venta.
		for (MZReservaVtaLin vtaLin: vtaLinList){

			MOrderLine orderLine = (MOrderLine) vtaLin.getC_OrderLine();
			MProduct product = (MProduct) vtaLin.getM_Product();

			// Actualizo Storage para la reserva
			if (product.isStocked())
			{
				//	Mandatory Product Attribute Set Instance
				MAttributeSet.validateAttributeSetInstanceMandatory(product, I_C_OrderLine.Table_ID, true, orderLine.getM_AttributeSetInstance_ID());

				int mLocatorID = 0;

				//	Get Locator to reserve
				if (orderLine.getM_AttributeSetInstance_ID() != 0){
					//	Get existing Location
					mLocatorID = MStorage.getM_Locator_ID (orderLine.getM_Warehouse_ID(), orderLine.getM_Product_ID(), orderLine.getM_AttributeSetInstance_ID(), Env.ZERO, get_TrxName());
				}

				//	Get default Location
				if (mLocatorID == 0)
				{
					// try to take default locator for product first
					// if it is from the selected warehouse
					mLocatorID = product.getM_Locator_ID();
					if ( mLocatorID != 0) {
						MLocator locator = new MLocator(getCtx(), product.getM_Locator_ID(), get_TrxName());
						//product has default locator defined but is not from the order warehouse
						if(locator.getM_Warehouse_ID() != warehouse.get_ID()) {
							mLocatorID = warehouse.getDefaultLocator().getM_Locator_ID();
						}
					}
					else {
						mLocatorID = warehouse.getDefaultLocator().getM_Locator_ID();
					}
				}

				//	Update Storage
				if (!MStorage.add(getCtx(), warehouse.get_ID(), mLocatorID, orderLine.getM_Product_ID(),
						orderLine.getM_AttributeSetInstance_ID(), orderLine.getM_AttributeSetInstance_ID(),
						Env.ZERO, vtaLin.getQtyReserved().negate(), Env.ZERO, get_TrxName())){

					m_processMsg = "No se pudo Reactivar la reserva para el producto : " + product.getValue() + " - " + product.getName();
					return false;
				}

				// Actualizo cantidad reservada de la linea de la orden de venta
				action = " update c_orderline set qtyreserved = qtyreserved - " + vtaLin.getQtyReserved() +
						" where c_orderline_id =" + vtaLin.getC_OrderLine_ID();
				DB.executeUpdateEx(action, get_TrxName());
			}
		}

		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;

		this.setProcessed(false);
		this.setDocStatus(DOCSTATUS_InProgress);
		this.setDocAction(DOCACTION_Complete);

		return true;
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
      StringBuffer sb = new StringBuffer ("MZReservaVta[")
        .append(getSummary()).append("]");
      return sb.toString();
    }


	/***
	 * Metodo que obtiene y retorna lineas de este documento.
	 * Xpande. Created by Gabriel Vila on 8/4/20.
	 * @return
	 */
	public List<MZReservaVtaLin> getLines(){

		String whereClause = X_Z_ReservaVtaLin.COLUMNNAME_Z_ReservaVta_ID + " =" + this.get_ID();

		List<MZReservaVtaLin> lines = new Query(getCtx(), I_Z_ReservaVtaLin.Table_Name, whereClause, get_TrxName()).list();

		return lines;
	}


}