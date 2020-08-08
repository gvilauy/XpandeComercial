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

/** Generated Model for Z_GeneraEntrega
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class MZGeneraEntrega extends X_Z_GeneraEntrega implements DocAction, DocOptions {

	/**
	 *
	 */
	private static final long serialVersionUID = 20191204L;

    /** Standard Constructor */
    public MZGeneraEntrega (Properties ctx, int Z_GeneraEntrega_ID, String trxName)
    {
      super (ctx, Z_GeneraEntrega_ID, trxName);
    }

    /** Load Constructor */
    public MZGeneraEntrega (Properties ctx, ResultSet rs, String trxName)
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

		// Obtiene lineas y productos a considerar
		List<MZGeneraEntregaLin> entregaLinList = this.getLinesByOrder();
		List<MZGeneraEntProd> entProdList = this.getLinesEntProd();

		// Valido condiciones para completar este documento
		m_processMsg = this.validateDocument(entProdList, entregaLinList);
		if (m_processMsg != null){
			return DocAction.STATUS_Invalid;
		}

		// Genero reservas
		m_processMsg = this.generateReservas(entregaLinList);
		if (m_processMsg != null){
			return DocAction.STATUS_Invalid;
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

		// Obtengo lista de documentos de reserva generados en este proceso
		List<MZReservaVta> reservaVtaList = this.getReservas();

		// Recorro, reactivo y elimino reservas
		for (MZReservaVta reservaVta: reservaVtaList){

			// Si le reserva este completa, la reactivo antes de eliminarla.
			if (reservaVta.getDocStatus().equalsIgnoreCase(DOCSTATUS_Completed)){

				// Si no puedo reactivar, aviso y salgo
				if (!reservaVta.reActivateIt()){
					m_processMsg = "No se pudo Reactivar la Reserva : " + reservaVta.getDocumentNo() + ". ";
					if (reservaVta.getProcessMsg() != null){
						m_processMsg += reservaVta.getProcessMsg();
						return false;
					}
				}
				reservaVta.saveEx();
			}

			// Delete de Reserva
			reservaVta.deleteEx(true);
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
      StringBuffer sb = new StringBuffer ("MZGeneraEntrega[")
        .append(getSummary()).append("]");
      return sb.toString();
    }

	/***
	 * Obtiene lineas de ordenes de venta para procesar según filtros.
	 * Xpande. Created by Gabriel Vila on 12/5/19.
	 * @param tipoAccion
	 * @return
	 */
	public void getData(String tipoAccion) {

		try{

			boolean getDocumentosNuevos = true;

			if (!tipoAccion.equalsIgnoreCase("NUEVOS")){
				getDocumentosNuevos = false;
			}

			// Elimino generacion anterior en caso de que el usuario asi lo indique
			if (!getDocumentosNuevos){
				this.deleteDocuments();
			}

			// Obtiene ordenes de venta segun criterios de búsqueda y genera lineas.
			this.getOrders();

			// Si esta marcada la opcion para asignar stock disponible de manera automatica, lo hago.
			if (this.isConfirmed()){
				this.asignarStockDisponible(-1);
			}

		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}
	}

	/***
	 * Elimina documentos existentes.
	 * Xpande. Created by Gabriel Vila on 12/5/19.
	 */
	private void deleteDocuments() {

		String action = "";

		try{

			action = " delete from z_generaentregabp cascade where z_generaentrega_id =" + this.get_ID();
			DB.executeUpdateEx(action, get_TrxName());

			action = " delete from z_generaentprod cascade where z_generaentrega_id =" + this.get_ID();
			DB.executeUpdateEx(action, get_TrxName());

		}
		catch (Exception e){
			throw new AdempiereException(e);
		}
	}

	/***
	 * Obtiene condiciones para obtener información de documentos a considerar en este proceso.
	 * Xpande. Created by Gabriel Vila on 8/1/20.
	 * @return
	 */
	private String getWhereDocuments(){

		String whereClause = "";

		try{

			// Filtro almacen
			whereClause = "  and hdr.m_warehouse_id =" + this.getM_Warehouse_ID();

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

			// Filtro de monedas
			if (this.getC_Currency_ID() > 0){
				whereClause += " AND hdr.c_currency_id =" + this.getC_Currency_ID();
			}

			// Filtro de socios de negocio
			String filtroSocios = this.getFiltroSocios();
			if (filtroSocios != null){
				whereClause += " AND " + filtroSocios;
			}

			// Filtro de Regiones de Venta
			String filtroRegiones = this.getFiltroRegiones();
			if (filtroSocios != null){
				whereClause += " AND " + filtroRegiones;
			}

			// Filtro de productos
			String filtroProductos = this.getFiltroProductos();
			if (filtroSocios != null){
				whereClause += " AND " + filtroProductos;
			}
		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}

		return whereClause;
	}

	/***
	 * Obtiene lineas de ordenes de venta a considerar ordenadas por producto y genera
	 * lineas por cada una de ellas.
	 * Xpande. Created by Gabriel Vila on 8/1/20.
	 * @return
	 */
	private void getOrders(){

		String sql = "", action = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			String whereClause = this.getWhereDocuments();

			// Query
			sql = " select hdr.* " +
					" from zv_comercial_openso hdr " +
					" where hdr.ad_client_id =" + this.getAD_Client_ID() +
					" and hdr.ad_org_id =" + this.getAD_Org_ID() +
					" and hdr.c_orderline_id not in (select c_orderline_id from z_generaentregalin " +
					" where z_generaentrega_id =" + this.get_ID() + ") " +
					whereClause +
					" order by hdr.m_product_id, hdr.dateordered, hdr.c_order_id ";

			int mProductIDAux = 0;
			MZGeneraEntProd entregaProd = null;
			MZGeneraEntregaBP entregaBP = null;

			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();

			while(rs.next()){

				// Corte por producto
				if (rs.getInt("m_product_id") != mProductIDAux){

					mProductIDAux = rs.getInt("m_product_id");

					// Obtengo modelo de producto a considerar en esta generación, si ya existe.
					// Si no existe lo creo ahora
					entregaProd = this.getEntregaProd(mProductIDAux);
					if ((entregaProd == null) || (entregaProd.get_ID() <= 0)){
						entregaProd = new MZGeneraEntProd(getCtx(), 0, get_TrxName());
						entregaProd.setZ_GeneraEntrega_ID(this.get_ID());
						entregaProd.setM_Product_ID(mProductIDAux);
						entregaProd.setC_UOM_ID(rs.getInt("c_uom_to_id"));
						entregaProd.setQtyOrdered(Env.ZERO);
						entregaProd.setQtyReserved(Env.ZERO);
						entregaProd.setQtyDelivered(Env.ZERO);
						entregaProd.setQtyOpen(Env.ZERO);
						entregaProd.setQtyToDeliver(Env.ZERO);
						entregaProd.setQtyOnHand(Env.ZERO);
					}
					entregaProd.setQtyAvailable(rs.getBigDecimal("qtyavailable"));
					entregaProd.saveEx();
				}

				// Si se indica tipo de proceso por Socio de negocio
				if (this.getTipoGeneraEntrega().equalsIgnoreCase(X_Z_GeneraEntrega.TIPOGENERAENTREGA_SOCIODENEGOCIO)){
					// Obtengo modelo de socio a considerar en esta generación, si ya existe
					// Si no existe lo creo ahora
					entregaBP = this.getEntregaBP(rs.getInt("c_bpartner_id"));
					if ((entregaBP == null) || (entregaBP.get_ID() <= 0)){
						MBPartner partner = new MBPartner(getCtx(), rs.getInt("c_bpartner_id"), null);
						entregaBP = new MZGeneraEntregaBP(getCtx(), 0, get_TrxName());
						entregaBP.setZ_GeneraEntrega_ID(this.get_ID());
						entregaBP.setC_BPartner_ID(rs.getInt("c_bpartner_id"));
						entregaBP.setTaxID(partner.getTaxID());

						if (partner.get_ValueAsInt("Z_CanalVenta_ID") > 0){
							entregaBP.setZ_CanalVenta_ID(partner.get_ValueAsInt("Z_CanalVenta_ID"));
						}
						entregaBP.saveEx();
					}
				}

				MZGeneraEntregaLin entregaLin = new MZGeneraEntregaLin(getCtx(), 0, get_TrxName());
				entregaLin.setZ_GeneraEntrega_ID(this.get_ID());
				entregaLin.setZ_GeneraEntProd_ID(entregaProd.get_ID());

				if (entregaBP != null){
					entregaLin.setZ_GeneraEntregaBP_ID(entregaBP.get_ID());
				}

				entregaLin.setAD_Org_ID(this.getAD_Org_ID());
				entregaLin.setC_Order_ID(rs.getInt("c_order_id"));
				entregaLin.setC_OrderLine_ID(rs.getInt("c_orderline_id"));
				entregaLin.setC_BPartner_ID(rs.getInt("c_bpartner_id"));
				entregaLin.setC_BPartner_Location_ID(rs.getInt("c_bpartner_location_id"));
				entregaLin.setSalesRep_ID(rs.getInt("salesrep_id"));
				entregaLin.setC_Currency_ID(rs.getInt("c_currency_id"));
				entregaLin.setC_DocType_ID(rs.getInt("c_doctype_id"));
				entregaLin.setC_SalesRegion_ID(rs.getInt("c_salesregion_id"));
				entregaLin.setDateOrdered(rs.getTimestamp("dateordered"));
				entregaLin.setDatePromised(rs.getTimestamp("datepromised"));
				entregaLin.setPOReference(rs.getString("poreference"));
				entregaLin.setM_Warehouse_ID(rs.getInt("m_warehouse_id"));
				entregaLin.setM_Product_ID(rs.getInt("m_product_id"));
				entregaLin.setC_UOM_ID(rs.getInt("c_uom_id"));
				entregaLin.setC_UOM_To_ID(rs.getInt("c_uom_to_id"));
				entregaLin.setUomMultiplyRate(rs.getBigDecimal("UomMultiplyRate"));
				entregaLin.setQtyEntered(rs.getBigDecimal("qtyentered"));
				entregaLin.setQtyOrdered(rs.getBigDecimal("qtyordered"));
				entregaLin.setQtyReserved(rs.getBigDecimal("qtyreserved"));
				entregaLin.setQtyDelivered(rs.getBigDecimal("qtydelivered"));
				entregaLin.setQtyOpenUomProd(rs.getBigDecimal("qtyopen"));
				entregaLin.setQtyOpen(rs.getBigDecimal("qtyopen"));
				entregaLin.setQtyToDeliver(Env.ZERO);

				entregaProd.setQtyOrdered(entregaProd.getQtyOrdered().add(entregaLin.getQtyOrdered()));
				entregaProd.setQtyReserved(entregaProd.getQtyReserved().add(entregaLin.getQtyReserved()));
				entregaProd.setQtyDelivered(entregaProd.getQtyDelivered().add(entregaLin.getQtyDelivered()));
				entregaProd.setQtyOpen(entregaProd.getQtyOpen().add(entregaLin.getQtyOpen()));
				entregaProd.saveEx();

				// Convierto cantidades según factor a unidad de esta linea
				if (rs.getInt("c_uom_id") != rs.getInt("c_uom_to_id")){
					MUOM uomTo = (MUOM) entregaLin.getC_UOM();
					entregaLin.setQtyReserved(entregaLin.getQtyReserved().multiply(entregaLin.getUomMultiplyRate()).setScale(uomTo.getStdPrecision(), RoundingMode.HALF_UP));
					entregaLin.setQtyDelivered(entregaLin.getQtyDelivered().multiply(entregaLin.getUomMultiplyRate()).setScale(uomTo.getStdPrecision(), RoundingMode.HALF_UP));
					entregaLin.setQtyOpen(entregaLin.getQtyOpen().multiply(entregaLin.getUomMultiplyRate()).setScale(uomTo.getStdPrecision(), RoundingMode.HALF_UP));
				}

				entregaLin.setLineNetAmt(rs.getBigDecimal("linenetamt"));
				entregaLin.setAmtOpen(rs.getBigDecimal("amtopen"));
				entregaLin.saveEx();
			}


		}
		catch (Exception e){
			throw new AdempiereException(e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
	}

	/***
	 * Asigna stock disponible a las lineas de ordenes de venta.
	 * Xpande. Created by Gabriel Vila on 8/7/20.
	 * @param mProductID
	 */
	private void asignarStockDisponible(int mProductID){

		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{

			String whereClause = "";
			if (mProductID > 0){
				whereClause = " and m_product_id =" + mProductID;
			}

		    sql = " select z_generaentregalin_id from z_generaentregalin " +
				  " where z_generaentrega_id =" + this.get_ID() + whereClause +
				  " order by m_product_id, dateordered, c_order_id ";

			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();

			MZGeneraEntProd entregaProd = null;
			int mProductIDAux = 0;
			BigDecimal qtyAvailable = Env.ZERO;

			while(rs.next()){

				MZGeneraEntregaLin entregaLin = new MZGeneraEntregaLin(getCtx(), rs.getInt("z_generaentregalin_id"), get_TrxName());

				// Corte por producto
				if (entregaLin.getM_Product_ID() != mProductIDAux){

					mProductIDAux = entregaLin.getM_Product_ID();

					// Obtengo modelo de producto a considerar, si no hay no hago nada con este producto.
					entregaProd = this.getEntregaProd(mProductIDAux);
					if ((entregaProd == null) || (entregaProd.get_ID() <= 0)){
						mProductIDAux = -1;
						qtyAvailable = Env.ZERO;
						continue;
					}

					MUOM uomProd = (MUOM) entregaProd.getC_UOM();

					// Inicializo cantidades para asiganción
					// Obtengo stock disponible actual para este producto
					sql = " select z_stk_warproddisp(" + this.getAD_Client_ID() + ", " + this.getAD_Org_ID() + ", " +
							this.getM_Warehouse_ID()  + ", " + mProductIDAux + ")";
					BigDecimal qtyAvailableProd = DB.getSQLValueBDEx(null, sql);
					if (qtyAvailableProd == null) qtyAvailableProd = Env.ZERO;

					// Precision según unidad de medida del producto
					qtyAvailableProd = qtyAvailableProd.setScale(uomProd.getStdPrecision(), RoundingMode.HALF_UP);

					// Si no tengo disponible no hago nada
					if (qtyAvailableProd.compareTo(Env.ZERO) <= 0){
						mProductIDAux = -1;
						qtyAvailable = Env.ZERO;
						continue;
					}

					entregaProd.setQtyAvailable(qtyAvailableProd);
					entregaProd.setQtyOnHand(entregaProd.getQtyAvailable());
					entregaProd.setQtyToDeliver(Env.ZERO);
					entregaProd.saveEx();

					qtyAvailable = entregaProd.getQtyAvailable();
				}

				// Si tengo disponible, asigno
				if (qtyAvailable.compareTo(entregaLin.getQtyOpenUomProd()) >= 0){

					entregaLin.setQtyToDeliver(entregaLin.getQtyOpen());
					entregaLin.saveEx();

					entregaProd.setQtyToDeliver(entregaProd.getQtyToDeliver().add(entregaLin.getQtyOpenUomProd()));
					entregaProd.saveEx();

					// Bajo el disponible para este producto
					qtyAvailable = qtyAvailable.subtract(entregaLin.getQtyOpenUomProd());
				}

			}

			// Actualizo disponible final de productos
			String action = " update z_generaentprod set qtyonhand = (qtyavailable - qtytodeliver) " +
					        " where z_generaentrega_id =" + this.get_ID() + whereClause;
			DB.executeUpdateEx(action, get_TrxName());
		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}
		finally {
		    DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
	}


	/***
	 * Obtiene y retorna modelo de entrega de socio de negocio según id de socio de negocio recibido.
	 * Xpande. Created by Gabriel Vila on 12/5/19.
	 * @param cBPartnerID
	 * @return
	 */
	private MZGeneraEntregaBP getEntregaBP(int cBPartnerID) {

		String whereClause = X_Z_GeneraEntregaBP.COLUMNNAME_Z_GeneraEntrega_ID + " =" + this.get_ID() +
				" AND " + X_Z_GeneraEntregaBP.COLUMNNAME_C_BPartner_ID + " =" + cBPartnerID;

		MZGeneraEntregaBP model = new Query(getCtx(), I_Z_GeneraEntregaBP.Table_Name, whereClause, get_TrxName()).first();

		return model;
	}

	/***
	 * Obtiene y retorna modelo de entrega de producto según id de producto recibido.
	 * Xpande. Created by Gabriel Vila on 8/1/20.
	 * @param mProductID
	 * @return
	 */
	private MZGeneraEntProd getEntregaProd(int mProductID) {

		String whereClause = X_Z_GeneraEntProd.COLUMNNAME_Z_GeneraEntrega_ID + " =" + this.get_ID() +
				" AND " + X_Z_GeneraEntProd.COLUMNNAME_M_Product_ID + " =" + mProductID;

		MZGeneraEntProd model = new Query(getCtx(), I_Z_GeneraEntProd.Table_Name, whereClause, get_TrxName()).first();

		return model;
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
			String sql = " select count(*) from z_generaentfiltbp where z_generaentrega_id =" + this.get_ID();
			int contador = DB.getSQLValue(get_TrxName(), sql);

			// Si no tengo, no hago nada
			if (contador <= 0){
				return whereClause;
			}

			// Tengo socios de negocio para filtrar
			whereClause = " hdr.c_bpartner_id IN (select c_bpartner_id from z_generaentfiltbp where z_generaentrega_id =" + this.get_ID() + ") ";
		}
		catch (Exception e){
			throw new AdempiereException(e);
		}

		return whereClause;
	}

	/***
	 * Obtiene filtro de regiones de venta a aplicar para obtener documentos.
	 * Xpande. Created by Gabriel Vila on 12/5/19.
	 * @return
	 */
	private String getFiltroRegiones() {

		String whereClause = null;

		try{

			// Verifico si tengo socios de negocio para filtrar
			String sql = " select count(*) from z_generaentfiltregion where z_generaentrega_id =" + this.get_ID();
			int contador = DB.getSQLValue(get_TrxName(), sql);

			// Si no tengo, no hago nada
			if (contador <= 0){
				return whereClause;
			}

			// Tengo socios de negocio para filtrar
			whereClause = " hdr.c_salesregion_id IN (select c_salesregion_id from z_generaentfiltregion where z_generaentrega_id =" + this.get_ID() + ") ";
		}
		catch (Exception e){
			throw new AdempiereException(e);
		}

		return whereClause;
	}

	/***
	 * Obtiene filtro de productos a aplicar para obtener documentos.
	 * Xpande. Created by Gabriel Vila on 12/5/19.
	 * @return
	 */
	private String getFiltroProductos() {

		String whereClause = null;

		try{

			// Verifico si tengo socios de negocio para filtrar
			String sql = " select count(*) from z_generaentfiltprod where z_generaentrega_id =" + this.get_ID();
			int contador = DB.getSQLValue(get_TrxName(), sql);

			// Si no tengo, no hago nada
			if (contador <= 0){
				return whereClause;
			}

			// Tengo socios de negocio para filtrar
			whereClause = " hdr.m_product_id IN (select m_product_id from z_generaentfiltprod where z_generaentrega_id =" + this.get_ID() + ") ";
		}
		catch (Exception e){
			throw new AdempiereException(e);
		}

		return whereClause;
	}

	/***
	 * Genera reservas para cada orden de venta considerada en este proceso.
	 * Xpande. Created by Gabriel Vila on 8/2/20.
	 * @param entregaLinList
	 * @return
	 */
	private String generateReservas(List<MZGeneraEntregaLin> entregaLinList){

		String message = null;

		try{
			// Documento para reservas de venta
			MDocType[] docTypes = MDocType.getOfDocBaseType(getCtx(), "RVT");
			if (docTypes.length <= 0){
				return "Falta configurar Tipo de Documento para Reservas de Venta (RVT)";
			}
			MDocType docType = docTypes[0];

			MZReservaVta reservaVta = null;
			BigDecimal totalAmt = Env.ZERO;
			int cOrderIDAux = -1;

			// Recorro lineas del documento
			for (MZGeneraEntregaLin entregaLin: entregaLinList){

				// Corte por orden de venta
				if (entregaLin.getC_Order_ID() != cOrderIDAux){

					// Si ya tengo un cabezal de reserva en proceso
					if ((reservaVta != null) && (reservaVta.get_ID() > 0)){

						reservaVta.setTotalAmt(totalAmt);

						// Lo completo
						if (!reservaVta.processIt(DOCACTION_Complete)){
							message = "No se pudo completar documento de Reserva de Venta. ";
							if (reservaVta.getProcessMsg() != null){
								message += reservaVta.getProcessMsg();
							}
							return message;
						}
						reservaVta.saveEx();
					}

					// Nuevo cabezal de reserva
					reservaVta = new MZReservaVta(getCtx(), 0, get_TrxName());
					reservaVta.setAD_Org_ID(this.getAD_Org_ID());
					reservaVta.setZ_GeneraEntrega_ID(this.get_ID());
					reservaVta.setC_BPartner_ID(entregaLin.getC_BPartner_ID());
					reservaVta.setC_BPartner_Location_ID(entregaLin.getC_BPartner_Location_ID());
					reservaVta.setC_Order_ID(entregaLin.getC_Order_ID());
					reservaVta.setC_Currency_ID(entregaLin.getC_Currency_ID());
					reservaVta.setC_DocType_ID(docType.get_ID());
					reservaVta.setDateDoc(this.getDateDoc());
					reservaVta.setDateOrdered(entregaLin.getDateOrdered());

					if (entregaLin.getC_SalesRegion_ID() > 0){
						reservaVta.setC_SalesRegion_ID(entregaLin.getC_SalesRegion_ID());
					}

					if (entregaLin.getSalesRep_ID() > 0){
						reservaVta.setSalesRep_ID(entregaLin.getSalesRep_ID());
					}

					reservaVta.setDescription("Generada automáticamente desde Generador de Reservas número : " + this.getDocumentNo());
					reservaVta.setM_Warehouse_ID(this.getM_Warehouse_ID());
					reservaVta.setTotalAmt(Env.ZERO);
					reservaVta.saveEx();

					cOrderIDAux = entregaLin.getC_Order_ID();
					totalAmt = Env.ZERO;
				}

				MOrderLine orderLine = (MOrderLine) entregaLin.getC_OrderLine();

				// Nueva linea de reserva
				MZReservaVtaLin reservaVtaLin = new MZReservaVtaLin(getCtx(), 0, get_TrxName());
				reservaVtaLin.setAD_Org_ID(reservaVta.getAD_Org_ID());
				reservaVtaLin.setZ_ReservaVta_ID(reservaVta.get_ID());
				reservaVtaLin.setC_OrderLine_ID(entregaLin.getC_OrderLine_ID());
				reservaVtaLin.setC_UOM_ID(entregaLin.getC_UOM_ID());
				reservaVtaLin.setUomMultiplyRate(entregaLin.getUomMultiplyRate());

				BigDecimal montoLinRes = entregaLin.getQtyToDeliver().multiply(orderLine.getPriceEntered().setScale(2, RoundingMode.HALF_UP));
				reservaVtaLin.setLineNetAmt(montoLinRes);
				reservaVtaLin.setM_Product_ID(entregaLin.getM_Product_ID());

				MZGeneraEntProd entProd = (MZGeneraEntProd) entregaLin.getZ_GeneraEntProd();

				BigDecimal qtyAvailableEnt = entProd.getQtyAvailable().multiply(entregaLin.getUomMultiplyRate());
				MUOM uomProd = (MUOM) entProd.getC_UOM();
				qtyAvailableEnt = qtyAvailableEnt.setScale(uomProd.getStdPrecision(), RoundingMode.HALF_UP);

				reservaVtaLin.setQtyAvailable(entProd.getQtyAvailable());
				reservaVtaLin.setQtyAvailableEnt(qtyAvailableEnt);
				reservaVtaLin.setQtyEntered(entregaLin.getQtyEntered());
				reservaVtaLin.setQtyOrdered(entregaLin.getQtyOrdered());


				BigDecimal qtyReserved = entregaLin.getQtyToDeliver().divide(entregaLin.getUomMultiplyRate(), uomProd.getStdPrecision(), RoundingMode.HALF_UP);
				reservaVtaLin.setQtyReservedEnt(entregaLin.getQtyToDeliver());
				reservaVtaLin.setQtyReserved(qtyReserved);
				reservaVtaLin.saveEx();
			}

			// Si ya tengo un cabezal de reserva en proceso
			if ((reservaVta != null) && (reservaVta.get_ID() > 0)){

				reservaVta.setTotalAmt(totalAmt);

				// Lo completo
				if (!reservaVta.processIt(DOCACTION_Complete)){
					message = "No se pudo completar documento de Reserva de Venta. ";
					if (reservaVta.getProcessMsg() != null){
						message += reservaVta.getProcessMsg();
					}
					return message;
				}
				reservaVta.saveEx();
			}
		}
		catch (Exception e){
			throw new AdempiereException(e);
		}

		return null;
	}


	/***
	 * Obtiene y retorna lineas ordenadas por orden de venta y linea de venta.
	 * Xpande. Created by Gabriel Vila on 8/2/20.
	 * @return
	 */
	public List<MZGeneraEntregaLin> getLinesByOrder() {

		String whereClause = X_Z_GeneraEntregaLin.COLUMNNAME_Z_GeneraEntrega_ID + " =" + this.get_ID();

		List<MZGeneraEntregaLin> lines = new Query(getCtx(), I_Z_GeneraEntregaLin.Table_Name, whereClause, get_TrxName())
				.setOrderBy(" c_order_id, c_orderline_id ").list();

		return lines;
	}

	/***
	 * Obtiene y retorna lineas de entrega de productos.
	 * Xpande. Created by Gabriel Vila on 8/2/20.
	 * @return
	 */
	public List<MZGeneraEntProd> getLinesEntProd(){

		String whereClause = X_Z_GeneraEntProd.COLUMNNAME_Z_GeneraEntrega_ID + " =" + this.get_ID();

		List<MZGeneraEntProd> lines = new Query(getCtx(), I_Z_GeneraEntProd.Table_Name, whereClause, get_TrxName())
				.setOrderBy(" m_product_id ").list();

		return lines;
	}


	/***
	 * Validaciones previas a completar este documento.
	 * Xpande. Created by Gabriel Vila on 8/2/20.
	 * @param entProdList
	 * @param entregaLinList
	 * @return
	 */
	private String validateDocument(List<MZGeneraEntProd> entProdList, List<MZGeneraEntregaLin> entregaLinList){

		String sql = "";

		try{

			// Si no tengo lineas aviso.
			if (entProdList.size() <= 0){
				return "No hay lineas para procesar.";
			}
			if (entregaLinList.size() <= 0){
				return "No hay lineas para procesar.";
			}

			// Valido que el disponible actual de cada producto alcance para la asignacion efectuada.
			for (MZGeneraEntProd entProd: entProdList){

				MUOM uomProd = (MUOM) entProd.getC_UOM();

				// Obtengo disponible actual para este producto
				sql = " select z_stk_warproddisp(" + this.getAD_Client_ID() + ", " + this.getAD_Org_ID() + ", " +
						this.getM_Warehouse_ID()  + ", " + entProd.getM_Product_ID() + ")";
				BigDecimal qtyAvailableProd = DB.getSQLValueBDEx(null, sql);
				if (qtyAvailableProd == null) qtyAvailableProd = Env.ZERO;

				// Precision según unidad de medida del producto
				qtyAvailableProd = qtyAvailableProd.setScale(uomProd.getStdPrecision(), RoundingMode.HALF_UP);

				// Actualizo totales de este modelo de generacion de producto
				entProd.setQtyAvailable(qtyAvailableProd);
				entProd.setQtyOnHand(entProd.getQtyAvailable().subtract(entProd.getQtyToDeliver()));
				entProd.saveEx();

				// Si la cantidad disponible actual es menor a la cantidad asignada, aviso y no hago nada
				if (qtyAvailableProd.compareTo(entProd.getQtyToDeliver()) < 0){

					MProduct product = (MProduct) entProd.getM_Product();
					return "La cantidad disponible del producto " + product.getValue() + " - " + product.getName() +
							" ha variado y no alcanza para la cantidad a procesar.\n " +
							" Disponible actual = " + qtyAvailableProd + ", cantidad a procesar = " + entProd.getQtyToDeliver();
				}
			}
		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}

		return null;
	}

	/***
	 * Obtiene y retorna lista de documentos de reserva generados en este proceso.
	 * Xpande. Created by Gabriel Vila on 8/7/20.
	 * @return
	 */
	public List<MZReservaVta> getReservas(){

		String whereClause = X_Z_ReservaVta.COLUMNNAME_Z_GeneraEntrega_ID + " =" + this.get_ID();

		List<MZReservaVta> lines = new Query(getCtx(), I_Z_ReservaVta.Table_Name, whereClause, get_TrxName()).list();

		return lines;
	}
}