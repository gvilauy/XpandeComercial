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

import org.compiere.model.MBPartner;
import org.xpande.comercial.model.MZPautaComVta;
import org.xpande.comercial.model.MZPautaComVtaBP;

import java.util.List;

/** Generated Process for (Z_SeleccionSociosPautaComVta)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.0
 */
public class SeleccionPautaComVtaBP extends SeleccionPautaComVtaBPAbstract
{
	private MZPautaComVta pautaComVta = null;

	@Override
	protected void prepare()
	{
		this.pautaComVta = new MZPautaComVta(getCtx(), this.getRecord_ID(), get_TrxName());

		super.prepare();
	}

	@Override
	protected String doIt() throws Exception
	{
		List<Integer> recordIds =  getSelectionKeys();

		//	Recorro filas seleccionadas
		recordIds.stream().forEach( key -> {

			// Asocio socio de negocio
			MBPartner mbPartner = new MBPartner(getCtx(), key.intValue(), get_TrxName());
			MZPautaComVtaBP pautaComVtaBP = new MZPautaComVtaBP(getCtx(), 0, get_TrxName());
			pautaComVtaBP.setZ_PautaComVta_ID(this.pautaComVta.get_ID());
			pautaComVtaBP.setC_BPartner_ID(mbPartner.get_ID());

			if (mbPartner.getSalesRep_ID() > 0){
				pautaComVtaBP.setSalesRep_ID(mbPartner.getSalesRep_ID());
			}

			if (mbPartner.get_ValueAsInt("Z_CanalVenta_ID") > 0){
				pautaComVtaBP.setZ_CanalVenta_ID(mbPartner.get_ValueAsInt("Z_CanalVenta_ID"));
			}

			pautaComVtaBP.saveEx();

		});

		return "";
	}
}