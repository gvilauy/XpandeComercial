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
import java.util.List;

/** Generated Process for (Z_SeleccionOrdenVentaEntrega)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.0
 */
public class SeleccionOrdenVentaEntrega extends SeleccionOrdenVentaEntregaAbstract
{
	private MInOut inOut = null;

	@Override
	protected void prepare()
	{
		this.inOut = new MInOut(getCtx(), this.getRecord_ID(), get_TrxName());
		super.prepare();
	}

	@Override
	protected String doIt() throws Exception
	{
		try {

			MWarehouse warehouse = (MWarehouse) this.inOut.getM_Warehouse();
			MLocator locator = MLocator.getDefault(warehouse);

			List<Integer> recordIds = getSelectionKeys();

			//	Recorro filas de selección de ordenes de venta
			recordIds.stream().forEach(key -> {

				MOrderLine orderLine = new MOrderLine(getCtx(), key.intValue(), get_TrxName());

				MProduct product = (MProduct) orderLine.getM_Product();

				MInOutLine inOutLine = new MInOutLine(this.inOut);
				inOutLine.setC_OrderLine_ID(orderLine.get_ID());
				inOutLine.setM_Warehouse_ID(warehouse.get_ID());
				inOutLine.setM_Locator_ID(locator.get_ID());
				inOutLine.setM_Product_ID(product.get_ID());
				inOutLine.setC_UOM_ID(product.getC_UOM_ID());

				inOutLine.setMovementQty(orderLine.getQtyOrdered());
				inOutLine.setQtyEntered(orderLine.getQtyEntered());

				inOutLine.saveEx();
			});

		}
		catch (Exception e){
			throw new AdempiereException(e);
		}

		return "OK";

	}
}