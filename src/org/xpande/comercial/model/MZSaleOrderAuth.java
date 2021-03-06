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
import org.compiere.model.*;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import sun.misc.MessageUtils;

/** Generated Model for Z_SaleOrderAuth
 *  @author Adempiere (generated) 
 *  @version Release 3.9.1 - $Id$ */
public class MZSaleOrderAuth extends X_Z_SaleOrderAuth implements DocAction, DocOptions {

	/**
	 *
	 */
	private static final long serialVersionUID = 20210602L;

    /** Standard Constructor */
    public MZSaleOrderAuth (Properties ctx, int Z_SaleOrderAuth_ID, String trxName)
    {
      super (ctx, Z_SaleOrderAuth_ID, trxName);
    }

    /** Load Constructor */
    public MZSaleOrderAuth (Properties ctx, ResultSet rs, String trxName)
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

		// Obtengo y recorro lineas con las ordenes de venta seleccionadas para aprobación
		List<MZSaleOrderAuthLin> authLinList = this.getSelectedLines();
		for (MZSaleOrderAuthLin authLin: authLinList){
			// Seteo orden como aprobada y la completo
			MOrder order = (MOrder) authLin.getC_Order();
			order.setIsCreditApproved(true);
			order.set_ValueOfColumn("CreditMessage", "Crédito Aprobado Manualmente. Número de Aprobación : " + this.getDocumentNo());
			order.set_ValueOfColumn("IsManualApproved", true);
			order.setDocStatus(X_C_Order.DOCSTATUS_Drafted);
			if (!order.processIt(DOCACTION_Complete)){
				if (order.getProcessMsg() != null){
					m_processMsg = "No se pudo completar la Aprobación. Falla al completar Orden de Venta: " + order.getDocumentNo() + "\n" + order.getProcessMsg();
				}
				else{
					m_processMsg = "No se pudo completar la Aprobación por errores al completar la Orden de Venta: " + order.getDocumentNo();
				}
				return DocAction.STATUS_Invalid;
			}
			order.saveEx();
		}

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
	 * Obtiene  y retorna lineas con ordenes de venta seleccionadas para aprobación.
	 * Xpande. Created by Gabriel Vila on 6/9/21.
	 * @return
	 */
	private List<MZSaleOrderAuthLin> getSelectedLines() {

		String whereClause = X_Z_SaleOrderAuthLin.COLUMNNAME_Z_SaleOrderAuth_ID + " =" + this.get_ID() +
				" AND " + X_Z_SaleOrderAuthLin.COLUMNNAME_IsSelected + " ='Y'";

		List<MZSaleOrderAuthLin> lines = new Query(getCtx(), I_Z_SaleOrderAuthLin.Table_Name, whereClause, get_TrxName()).list();

		return lines;
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
		return 0;
	}	//	getC_Currency_ID

    @Override
    public String toString()
    {
      StringBuffer sb = new StringBuffer ("MZSaleOrderAuth[")
        .append(getSummary()).append("]");
      return sb.toString();
    }

	/**
	 * Carga información de pedidos a considerar en este proceso de aprobacion.
	 * Xpande. Created by Gabriel Vila on 6/9/21.
	 * @return
	 */
	public String getOrders(){

		String sql, action;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			// Elimino datos existenes antes de cargar nuevamente pedidos
			action = " delete from " + X_Z_SaleOrderAuthLin.Table_Name + " where z_saleorderauth_id =" + this.get_ID();
			DB.executeUpdateEx(action, get_TrxName());
			action = " delete from " + X_Z_SaleOrderAuthBP.Table_Name + " where z_saleorderauth_id =" + this.get_ID();
			DB.executeUpdateEx(action, get_TrxName());

			String whereClause = this.getWhereDocuments();

		    sql = " select hdr.c_order_id, hdr.c_bpartner_id, " +
					" coalesce(bp.SO_CreditLimit,0) as SO_CreditLimit, coalesce(bp.SO_CreditUsed,0) as SO_CreditUsed " +
					" from c_order hdr " +
					" inner join c_bpartner bp on hdr.c_bpartner_id = bp.c_bpartner_id " +
					" where hdr.ad_client_id =" + this.getAD_Client_ID() +
					" and hdr.issotrx='Y' " +
					" and hdr.docstatus <>'CO' " +
					" and iscreditapproved ='N' " + whereClause +
					" order by hdr.c_bpartner_id, hdr.dateordered, hdr.c_order_id ";

			int cBpartnerIDAux = 0;
			BigDecimal amtApproval = Env.ZERO;
			MZSaleOrderAuthBP authBP = null;

			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();
			while(rs.next()){
				// Corte por socio de negocio
				if (rs.getInt("c_bpartner_id") != cBpartnerIDAux){
					if (authBP != null){
						authBP.setAmtApproval(amtApproval);
						authBP.saveEx();
					}
					amtApproval = Env.ZERO;
					authBP =  new MZSaleOrderAuthBP(getCtx(), 0, get_TrxName());
					authBP.setZ_SaleOrderAuth_ID(this.get_ID());
					authBP.setC_BPartner_ID(rs.getInt("c_bpartner_id"));
					authBP.setDueAmt(Env.ZERO);
					authBP.setSO_CreditLimit(rs.getBigDecimal("so_creditlimit"));
					authBP.setSO_CreditUsed(rs.getBigDecimal("so_creditused"));
					authBP.setStatusApprovalSO(X_Z_SaleOrderAuthBP.STATUSAPPROVALSO_SINAPROBACION);
					authBP.saveEx();

					cBpartnerIDAux = rs.getInt("c_bpartner_id");
				}

				// Guardo detalle del pedido
				MOrder order = new MOrder(getCtx(), rs.getInt("c_order_id"), get_TrxName());
				MZSaleOrderAuthLin authLin = new MZSaleOrderAuthLin(getCtx(), 0, get_TrxName());
				authLin.setZ_SaleOrderAuth_ID(this.get_ID());
				authLin.setZ_SaleOrderAuthBP_ID(authBP.get_ID());
				authLin.setC_BPartner_ID(order.getC_BPartner_ID());
				authLin.setC_Order_ID(order.get_ID());
				authLin.setDateOrdered(order.getDateOrdered());
				authLin.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());
				authLin.setDatePromised(order.getDatePromised());
				authLin.setIsSelected(false);
				authLin.setSalesRep_ID(order.getSalesRep_ID());
				authLin.setTotalAmt(order.getGrandTotal());
				authLin.saveEx();

				amtApproval = amtApproval.add(authLin.getTotalAmt());
			}
			if (authBP != null){
				authBP.setAmtApproval(amtApproval);
				authBP.saveEx();
			}

		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}
		finally {
		    DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return null;
	}

	/***
	 * Obtiene condiciones para obtener información de documentos a considerar en este proceso.
	 * Xpande. Created by Gabriel Vila on 8/1/20.
	 * @return
	 */
	private String getWhereDocuments(){

		String whereClause = "";
		try{
			// Filtros de fechas
			if (this.getDateOrderedFrom() != null){
				whereClause += " AND hdr.DateOrdered >='" + this.getDateOrderedFrom() + "' ";
			}
			if (this.getDateOrderedTo() != null){
				whereClause += " AND hdr.DateOrdered <='" + this.getDateOrderedTo() + "' ";
			}
			if (this.getDatePromisedFrom() != null){
				whereClause += " AND hdr.DatePromised >='" + this.getDatePromisedFrom() + "' ";
			}
			if (this.getDatePromisedTo() != null){
				whereClause += " AND hdr.DatePromised <='" + this.getDatePromisedTo() + "' ";
			}
			// Filtro de socios de negocio
			String filtroSocios = this.getFiltroSocios();
			if (filtroSocios != null){
				whereClause += " AND " + filtroSocios;
			}
			// Filtro de vendedor
			if (this.getSalesRep_ID() > 0){
				whereClause += " AND hdr.salesrep_id =" + this.getSalesRep_ID();
			}
			// Filtro de canal de venta
			if (this.getZ_CanalVenta_ID() > 0){
				whereClause += " AND bp.z_canalventa_id =" + this.getZ_CanalVenta_ID();
			}
		}
		catch (Exception e){
			throw new AdempiereException(e);
		}
		return whereClause;
	}

	/***
	 * Obtiene filtro de socios de negocio a aplicar para obtener documentos.
	 * Xpande. Created by Gabriel Vila on 12/5/19.
	 * @return
	 */
	private String getFiltroSocios() {

		String whereClause = null;
		try{
			// Verifico si tengo socios de negocio para filtrar
			String sql = " select count(*) from z_sorderauthfiltbp where z_saleorderauth_id =" + this.get_ID();
			int contador = DB.getSQLValue(get_TrxName(), sql);
			// Si no tengo, no hago nada
			if (contador <= 0){
				return null;
			}
			// Tengo socios de negocio para filtrar
			whereClause = " hdr.c_bpartner_id IN (select c_bpartner_id from z_sorderauthfiltbp where z_saleorderauth_id =" + this.get_ID() + ") ";
		}
		catch (Exception e){
			throw new AdempiereException(e);
		}
		return whereClause;
	}
}