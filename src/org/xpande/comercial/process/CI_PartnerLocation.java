package org.xpande.comercial.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MLocation;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Carga Inicial de Partner Locations a partir de datos precargados en tabla: ci_partnerlocation.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 6/29/18.
 */
public class CI_PartnerLocation extends SvrProcess {

    @Override
    protected void prepare() {

    }

    @Override
    protected String doIt() throws Exception {

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            sql = " select * from ci_partnerlocation ";

        	pstmt = DB.prepareStatement(sql, get_TrxName());
        	rs = pstmt.executeQuery();

        	while(rs.next()){

        	    MLocation location = new MLocation(getCtx(), rs.getInt("c_country_id"), rs.getInt("c_region_id"),
                        rs.getString("city"), get_TrxName());

        	    if (rs.getInt("c_city_id") > 0){
                    location.setC_City_ID(rs.getInt("c_city_id"));
                }

        	    location.setAddress1(rs.getString("address1"));
        	    location.setRegionName(rs.getString("regionname"));
        	    location.saveEx();

                MBPartnerLocation partnerLocation = new MBPartnerLocation(getCtx(), 0, get_TrxName());
                partnerLocation.setC_BPartner_ID(rs.getInt("c_bpartner_id"));
                partnerLocation.setC_Location_ID(location.get_ID());
                partnerLocation.setName(location.getRegionName());
                partnerLocation.saveEx();
        	}

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
        	rs = null; pstmt = null;
        }

        return "OK";
    }
}
