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

package org.xpande.comercial.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.util.DB;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** Generated Process for (Z_SeleccionCreateFromInvoice)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.0
 */
public class SeleccionCreateFromInvoice extends SeleccionCreateFromInvoiceAbstract
{
	private MInvoice invoice = null;

	@Override
	protected void prepare()
	{
		this.invoice = new MInvoice(getCtx(), this.getRecord_ID(), get_TrxName());
		super.prepare();
	}

	@Override
	protected String doIt() throws Exception
	{
		try {

			AtomicInteger referenceId = new AtomicInteger(0);

			List<Integer> recordIds = getSelectionKeys();

			String createFromType = recordIds.size() > 0 ?
					getSelectionAsString(recordIds.get(0), "PP_CreateFromType") : null;

			//	Recorro filas de selección
			recordIds.stream().forEach(key -> {

				int productId = getSelectionAsInt(key, "PP_M_Product_ID");
				int chargeId = getSelectionAsInt(key, "PP_C_Charge_ID");
				int uomId = getSelectionAsInt(key, "PP_C_UOM_ID");
				BigDecimal qtyEntered = getSelectionAsBigDecimal(key, "PP_QtyEntered"); // Qty

				MInvoiceLine invoiceLine = new MInvoiceLine(invoice);
				BigDecimal qtyInvoiced = null;

				//	Precision of Qty UOM
				int precision = 2;
				if (productId > 0) {
					MProduct product = MProduct.get(Env.getCtx(), productId);
					if (product != null) {
						invoiceLine.setM_Product_ID(product.getM_Product_ID(), uomId);
						precision = product.getUOMPrecision();
						if (product.getC_UOM_ID() != uomId) {
							qtyEntered = qtyEntered.setScale(precision, BigDecimal.ROUND_HALF_UP);
							qtyInvoiced = MUOMConversion.convertProductFrom(Env.getCtx(), productId, uomId, qtyEntered);
						}
					}
				} else if(chargeId != 0) {
					invoiceLine.setC_Charge_ID(chargeId);
				}

				qtyEntered = qtyEntered.setScale(precision, BigDecimal.ROUND_HALF_UP);
				if (qtyInvoiced == null)
					qtyInvoiced = qtyEntered;

				invoiceLine.setQty(qtyEntered);
				invoiceLine.setQtyInvoiced(qtyInvoiced);

				// Desde Orden
				if(createFromType.equalsIgnoreCase("O")) {

					MOrderLine orderLine = new MOrderLine (getCtx(), key, get_TrxName());
					referenceId.set(orderLine.getC_Order_ID());

					//	Set InOut
					String whereClause = "EXISTS (SELECT 1 "
							+ "FROM M_InOut io "
							+ "WHERE io.M_InOut_ID = M_InOutLine.M_InOut_ID "
							+ "AND io.DocStatus IN ('CO','CL'))";
					MInOutLine[] inOutLines = MInOutLine.getOfOrderLine(Env.getCtx(), key, whereClause, get_TrxName());
					log.fine ("Receipt Lines with OrderLine = #" + inOutLines.length);
					final BigDecimal qty = qtyEntered;
					MInOutLine inOutLine = Arrays.stream(inOutLines)
							.filter(ioLine -> ioLine != null && ioLine.getQtyEntered().compareTo(qty) == 0)
							.findFirst().orElse(inOutLines.length > 0 ? inOutLines[0] : null);
					//	Set From
					if(inOutLine != null){
						invoiceLine.setShipLine(inOutLine);
					}
					else{
						invoiceLine.setOrderLine(orderLine);
					}


				}
				// Desde Entrega
				else if(createFromType.equalsIgnoreCase("R")) {
					MInOutLine inOutLine = new MInOutLine(getCtx(), key, get_TrxName());
					referenceId.set(inOutLine.getM_InOut_ID());
					invoiceLine.setShipLine(inOutLine);
				}

				// Para comprobantes de compra, considero posibles tasa de impuesto según las siguientes condiciones:
				// 1. Si el socio de negocio es Literal E el impuesto debe ser oara dicha situacion.
				// 2. Si este linea tiene un producto asociado y dicho producto tiene un impuesto especial de compra.
				// Seteos de tasa de impuesto segun condiciones.
				// Si el socio de negocio es literal E, entonces todos sus productos deben ir con la tasa de impuesto para Literal E
				if (!invoice.isSOTrx()){
					boolean esLiteralE = false;
					MBPartner partner = (MBPartner) invoice.getC_BPartner();
					if (partner.get_ValueAsBoolean("LiteralE")){

						esLiteralE = true;

						// Obtengo ID de tasa de impuesto para Literal E desde coniguración comercial
						String sql = " select LiteralE_Tax_ID From Z_ComercialConfig where lower(value) ='general' ";
						int literalE_Tax_ID = DB.getSQLValueEx(null, sql);
						if (literalE_Tax_ID > 0){
							invoiceLine.setC_Tax_ID(literalE_Tax_ID);
							invoiceLine.setLineNetAmt();
						}
					}
					// Si no es Literal E, puede suceder que el producto tenga un impuesto especial de compra/venta.
					if (!esLiteralE){

						if (invoiceLine.getM_Product_ID() > 0){
							MProduct prod = (MProduct) invoiceLine.getM_Product();
							if (prod.get_ValueAsInt("C_TaxCategory_ID_2") > 0){
								MTaxCategory taxCat = new MTaxCategory(getCtx(), prod.get_ValueAsInt("C_TaxCategory_ID_2"), null);
								MTax tax = taxCat.getDefaultTax();
								if (tax != null){
									if (tax.get_ID() > 0){
										invoiceLine.setC_Tax_ID(tax.get_ID());
										invoiceLine.setLineNetAmt();
									}
								}
							}
						}
					}
				}

				//	Save
				invoiceLine.saveEx();

			});

			// Seteo datos del cabezal
			// Desde Orden
			if(createFromType.equalsIgnoreCase("O")) {
				MOrder order = new MOrder(getCtx(), referenceId.get(), get_TrxName());
				invoice.setOrder(order);
			}
			else if(createFromType.equalsIgnoreCase("R")) {
				MInOut inOut = new MInOut(getCtx(), referenceId.get(), get_TrxName());
				invoice.setShipment(inOut);
			}
			invoice.saveEx();
		}
		catch (Exception e){
			throw new AdempiereException(e);
		}

		return "OK";
	}

}