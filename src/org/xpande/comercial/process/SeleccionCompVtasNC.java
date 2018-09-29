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
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.util.DB;
import org.xpande.comercial.model.MZInvoiceRef;

import java.math.BigDecimal;
import java.util.List;

/** Generated Process for (Z_SeleccionCompVtasNC)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.0
 */
public class SeleccionCompVtasNC extends SeleccionCompVtasNCAbstract
{

	MInvoice invoice = null;

	@Override
	protected void prepare()
	{

		this.invoice = new MInvoice(getCtx(), this.getRecord_ID(), get_TrxName());
		super.prepare();

	}

	@Override
	protected String doIt() throws Exception
	{

		try{

			List<Integer> recordIds =  getSelectionKeys();

			recordIds.stream().forEach( key -> {

				MInvoice invoiceTo = new MInvoice(getCtx(), key.intValue(), get_TrxName());
				MDocType doc = (MDocType) invoice.getC_DocTypeTarget();

				// Nueva referencia a factura
				MZInvoiceRef invoiceRef = new MZInvoiceRef(getCtx(), 0, get_TrxName());
				invoiceRef.setC_Invoice_ID(this.invoice.get_ID());
				invoiceRef.setC_Invoice_To_ID(invoiceTo.get_ID());
				invoiceRef.setAmtAllocation(invoiceTo.getGrandTotal());
				invoiceRef.setAmtOpen(invoiceTo.getGrandTotal());
				invoiceRef.setTotalAmt(invoiceTo.getGrandTotal());
				invoiceRef.setC_Currency_ID(invoiceTo.getC_Currency_ID());
				invoiceRef.setC_DocType_ID(invoiceTo.getC_DocTypeTarget_ID());
				invoiceRef.setDateDoc(invoiceTo.getDateInvoiced());
				invoiceRef.setDueDate(invoiceTo.getDateInvoiced());
				invoiceRef.setDocumentNoRef(invoiceTo.getDocumentNo());
				invoiceRef.saveEx();

			});

		}
		catch (Exception e){
			throw new AdempiereException(e);
		}

		return "OK";

	}
}