<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.api;

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
import ${basepackage}.dto.${className}Dto;
<#include "/java_imports.include">
@Path("/${classNameLower}Service")
public interface ${className}Service{
	/**  
     *   
     * <save one>  
     * @return ResultDTO<Integer>
     */ 
	@POST
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	ResultDTO<Long> insert${className}(${className}Dto ${classNameLower}Dto);
	 /**  
     *   
     * <update one>  
     * @return ResultDTO<Integer>
     */ 
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	ResultDTO<Integer> update${className}(${className}Dto ${classNameLower}Dto);
	 /**  
     *   
     * <find one by id>  
     * @return ResultDTO<${className}Dto>
     */ 
	@GET
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	ResultDTO<${className}Dto> get${className}ById(${table.idColumn.simpleJavaType} id);
	 /**  
     *   
     * <query all>  
     * @return ResultDTO<List<${className}Dto>>
     */ 
	@GET
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	ResultDTO<PageResultDTO<${className}Dto>> search${className}(PageParsDTO<Map> pageParsDTO);

}