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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;

/** Generated Model for Z_PautaComVta
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class MZPautaComVta extends X_Z_PautaComVta implements DocAction, DocOptions {

	/**
	 *
	 */
	private static final long serialVersionUID = 20191024L;

    /** Standard Constructor */
    public MZPautaComVta (Properties ctx, int Z_PautaComVta_ID, String trxName)
    {
      super (ctx, Z_PautaComVta_ID, trxName);
    }

    /** Load Constructor */
    public MZPautaComVta (Properties ctx, ResultSet rs, String trxName)
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

		log.info("reActivateIt - " + toString());

		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;

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
		return 0;
	}	//	getC_Currency_ID

    @Override
    public String toString()
    {
      StringBuffer sb = new StringBuffer ("MZPautaComVta[")
        .append(getSummary()).append("]");
      return sb.toString();
    }


	/***
	 * Obtiene y retorna lista de descuentos en factura para un determinada fecha - socio de negocio - producto.
	 * Xpande. Created by Gabriel Vila on 1/2/20.
	 * @param ctx
	 * @param fechaDoc
	 * @param adClientID
	 * @param adOrgID
	 * @param cBpartnerID
	 * @param mProductID
	 * @param trxName
	 * @return
	 */
	public static List<MZPautaComVtaDtos> getInvoiceDiscounts(Properties ctx, Timestamp fechaDoc, int adClientID, int adOrgID, int cBpartnerID, int mProductID, String trxName) {

		List<MZPautaComVtaDtos> dtosList = new ArrayList<MZPautaComVtaDtos>();

		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{

			// Modelo del producto
			MProduct product = new MProduct(ctx, mProductID, null);
			int zProductoSeccionID = product.get_ValueAsInt("Z_ProductoSeccion_ID");
			int zProductoRubroID = product.get_ValueAsInt("Z_ProductoRubro_ID");


			// Descuentos que aplican para la jerarquia de este producto
		    sql = " select pdto.z_pautacomvtadtos_id " +
					" from z_pautacomvtadtos pdto " +
					" inner join z_pautacomvtaseg pseg on pdto.z_pautacomvtaseg_id = pseg.z_pautacomvtaseg_id " +
					" inner join z_pautacomvta pvta on pseg.z_pautacomvta_id = pvta.z_pautacomvta_id " +
					" where pvta.ad_client_id =" + adClientID +
					" and pvta.ad_org_id =" + adOrgID +
					" and pvta.docstatus='CO' " +
					" and coalesce(pseg.startdate, '" + fechaDoc + "') <= '" + fechaDoc + "'" +
					" and coalesce(pseg.enddate, '" + fechaDoc + "') >= '" + fechaDoc + "'" +
					" and pseg.isgeneral='Y' " +
					" and case when pseg.z_productoseccion_id > 0 then " +
					" coalesce(pseg.z_productoseccion_id, " + zProductoSeccionID + ") =" + zProductoSeccionID + " else 1=1 end " +
					" and case when pseg.z_productorubro_id > 0 then " +
					" coalesce(pseg.z_productorubro_id, " + zProductoRubroID + ") =" + zProductoRubroID + " else 1=1 end " +
					" and pdto.discounttype in ('" + X_Z_PautaComVtaDtos.DISCOUNTTYPE_OPERATIVOENFACTURA + "','" +
					X_Z_PautaComVtaDtos.DISCOUNTTYPE_FINANCIEROENFACTURA + "')";

			pstmt = DB.prepareStatement(sql, trxName);
			rs = pstmt.executeQuery();

			while(rs.next()){
				MZPautaComVtaDtos pautaComVtaDtos = new MZPautaComVtaDtos(ctx, rs.getInt("z_pautacomvtadtos_id"), trxName);
				dtosList.add(pautaComVtaDtos);
			}

			DB.close(rs, pstmt);
			rs = null; pstmt = null;

			// Descuentos que aplican directo para este producto
			sql = " select pdto.z_pautacomvtadtos_id " +
					" from z_pautacomvtadtos pdto " +
					" inner join z_pautacomvtaseg pseg on pdto.z_pautacomvtaseg_id = pseg.z_pautacomvtaseg_id " +
					" inner join z_pautacomvtaprod pprod on pprod.z_pautacomvtaseg_id = pseg.z_pautacomvtaseg_id " +
					 "inner join z_pautacomvta pvta on pseg.z_pautacomvta_id = pvta.z_pautacomvta_id " +
					" where pvta.ad_client_id =" + adClientID +
					" and pvta.ad_org_id =" + adOrgID +
					" and pvta.docstatus='CO' " +
					" and coalesce(pseg.startdate, '" + fechaDoc + "') <= '" + fechaDoc + "'" +
					" and coalesce(pseg.enddate, '" + fechaDoc + "') >= '" + fechaDoc + "'" +
					" and pseg.isgeneral='N' " +
					" and pprod.m_product_id =" + mProductID +
					" and pdto.discounttype in ('" + X_Z_PautaComVtaDtos.DISCOUNTTYPE_OPERATIVOENFACTURA + "','" +
					X_Z_PautaComVtaDtos.DISCOUNTTYPE_FINANCIEROENFACTURA + "')";

			pstmt = DB.prepareStatement(sql, trxName);
			rs = pstmt.executeQuery();

			while(rs.next()){
				MZPautaComVtaDtos pautaComVtaDtos = new MZPautaComVtaDtos(ctx, rs.getInt("z_pautacomvtadtos_id"), trxName);
				dtosList.add(pautaComVtaDtos);
			}

		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}
		finally {
		    DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return dtosList;
	}

}