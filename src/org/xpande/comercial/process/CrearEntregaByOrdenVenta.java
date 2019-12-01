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

/** Generated Process for (Z_CrearEntregaDesdeOrdenVenta)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.0
 */
public class CrearEntregaByOrdenVenta extends CrearEntregaByOrdenVentaAbstract
{

	@Override
	protected void prepare()
	{
		super.prepare();
	}

	@Override
	protected String doIt() throws Exception
	{
		try {

			List<Integer> recordIds = getSelectionKeys();

			//	Recorro filas de selección de lineas de ordenes a considerar
			recordIds.stream().forEach(key -> {

				MOrderLine orderLine = new MOrderLine(getCtx(), key.intValue(), get_TrxName());




			});

		}
		catch (Exception e){
			throw new AdempiereException(e);
		}

		return "OK";

	}
}