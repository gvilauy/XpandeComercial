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
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.xpande.core.model.MZProductoUPC;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Proceso asociado a SmartBrowser para selección de remitos de venta al momento de generar una factura de venta.
 * Xpande. Created by Gabriel Vila on 10/25/18
 * Generated Process for (Z_SeleccionRemitosVenta)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.0
 */
public class SeleccionRemitosVenta extends SeleccionRemitosVentaAbstract
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

			List<Integer> recordIds = getSelectionKeys();

			//	Recorro filas de selección de productos que fueron seleccionadas por el usuario
			recordIds.stream().forEach(key -> {

				MInOutLine inOutLine = new MInOutLine(getCtx(), key.intValue(), get_TrxName());

				String sql = " select qtyopen from zv_remitoventaopen where m_inoutline_id =" + inOutLine.get_ID();
				BigDecimal qtyOpen = DB.getSQLValueBDEx(null, sql);
				if (qtyOpen == null) qtyOpen = Env.ZERO;

				// Si tengo cantidad disponible en esta linea de remito de venta para facturar
				if (qtyOpen.compareTo(Env.ZERO) > 0){

					// Genera nueva linea de nota de credito asociada a esta linea del remito por diferencia
					MInvoiceLine invoiceLine = new MInvoiceLine(this.invoice);
					invoiceLine.setM_InOutLine_ID(inOutLine.get_ID());
					invoiceLine.setM_Product_ID(inOutLine.getM_Product_ID());
					invoiceLine.setQtyEntered(qtyOpen);
					invoiceLine.setQtyInvoiced(qtyOpen);
					invoiceLine.setC_UOM_ID(inOutLine.getC_UOM_ID());
					invoiceLine.set_ValueOfColumn("IsBySelection", true);
					MZProductoUPC productoUPC = MZProductoUPC.getByProduct(getCtx(), inOutLine.getM_Product_ID(), null);
					if ((productoUPC != null) && (productoUPC.get_ID() > 0)){
						invoiceLine.set_ValueOfColumn("UPC", productoUPC.getUPC());
					}
					invoiceLine.saveEx();
				}

			});

		}
		catch (Exception e){
			throw new AdempiereException(e);
		}

		return "OK";
	}
}