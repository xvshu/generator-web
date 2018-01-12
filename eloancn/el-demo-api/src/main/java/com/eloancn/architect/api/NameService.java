/*
 * Powered By [crazy-framework]
 * Web Site: http://www.eloan.com
 * Since 2015 - 2017
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
import com.eloancn.architect.dto.NameDto;
import java.util.List;
import java.util.Map;
import java.util.Date;
/**
 * <>
 * @author crazy
 * @version 1.0
 * @Time 2017-11-03 17:48:52
 */

@Path("/nameService")
public interface NameService{
	/**  
     *   
     * <save one>  
     * @param name  
     * @return ResultDTO<Integer>
     */ 
	@POST
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	ResultDTO<Integer> insertName(NameDto nameDto);
	 /**  
     *   
     * <update one>  
     * @param name  
     * @return ResultDTO<Integer>  
     */ 
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	ResultDTO<Integer> updateName(NameDto nameDto);
	 /**  
     *   
     * <find one by id>  
     * @param id  
     * @return ResultDTO<NameDto>
     */ 
	@GET
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	ResultDTO<NameDto> getNameById(Integer id);
	 /**  
     *   
     * <query all>  
     * @param page  
     * @param limit  
     * @param sort  
     * @param dir  
     * @return ResultDTO<List<NameDto>>
     */ 
	@GET
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	ResultDTO<PageResultDTO<NameDto>> searchName(PageParsDTO<Map> pageParsDTO);

}