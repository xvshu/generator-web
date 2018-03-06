/*
 * Powered By [eloancn-generator]
 * Author:qinxf
 * Since 2017 - 2018
 */
 
package com.eloancn.architect.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.eloancn.framework.sevice.api.PageResultDTO;
import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.framework.sevice.api.PageParsDTO;
import com.eloancn.architect.dto.CompanyDto;
import java.util.List;
import java.util.Map;
import java.util.Date;
/**
 * <>
 * @author qinxf
 * @version 1.0
 * @Time 2018-01-18 15:49:19
 */

@Path("/companyService")
public interface CompanyService{
	/**  
     *   
     * <save one>  
     * @return ResultDTO<Integer>
     */ 
	@POST
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	ResultDTO<Integer> insertCompany(CompanyDto companyDto);
	 /**  
     *   
     * <update one>  
     * @return ResultDTO<Integer>
     */ 
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	ResultDTO<Integer> updateCompany(CompanyDto companyDto);
	 /**  
     *   
     * <find one by id>  
     * @return ResultDTO<CompanyDto>
     */ 
	@GET
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	ResultDTO<CompanyDto> getCompanyById(Long id);
	 /**  
     *   
     * <query all>  
     * @return ResultDTO<List<CompanyDto>>
     */ 
	@GET
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	ResultDTO<PageResultDTO<CompanyDto>> searchCompany(PageParsDTO<Map> pageParsDTO);

}