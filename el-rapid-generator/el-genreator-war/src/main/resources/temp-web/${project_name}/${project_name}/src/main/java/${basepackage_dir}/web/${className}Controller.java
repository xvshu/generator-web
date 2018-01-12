<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.web;

import com.alibaba.fastjson.JSON;
import ${basepackage}.model.${className};
import ${basepackage}.service.${className}ServiceBean;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;
import com.eloancn.framework.orm.mybatis.paginator.domain.Paginator;
import com.eloancn.framework.sevice.api.PageResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
<#include "/java_imports.include">

/**
 * 首页控制器
 *
 * @author xvshu
 */
@Controller
@RequestMapping("/${classNameLower}")
public class ${className}Controller {
    private static final Logger logger = LoggerFactory.getLogger(${className}Controller.class);

    @Autowired
    private ${className}ServiceBean ${classNameLower}ServiceBean;

    @RequestMapping(value = "/index")
    public String index() {
        return "/${classNameLower}/${classNameLower}list";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    public PageResultDTO<${className}> page(Integer page,Integer rows,HttpServletRequest request){
        logger.info("=${className}Controller.page=> page:{} rows:{}",String.valueOf(page),String .valueOf(rows));
        if(page==null){
            page=1;
        }
        if(rows==null){
            rows=10;
        }
        PageResultDTO<${className}> pageResultDTO = new PageResultDTO();

        PageBounds pageBounds = new PageBounds();
        pageBounds.setPage(page);
        pageBounds.setLimit(rows);
        PageList<${className}> pageResult = ${classNameLower}ServiceBean.search(null,pageBounds);
        int total = ${classNameLower}ServiceBean.searchCount(null);

        pageResultDTO.setItems(pageResult);
        pageResultDTO.setTotalCount(total);

        //todo：page无法set total
        logger.info("=${className}Controller.page=> pageResult:{}", JSON.toJSONString(pageResultDTO));
        return pageResultDTO;
    }


    @RequestMapping(value = "/insert")
    @ResponseBody
    public String insert(${className} ${classNameLower},HttpServletRequest request){
        logger.info("=${className}Controller.insert=> ${className}:{}",JSON.toJSONString(${classNameLower}));
        ${classNameLower}ServiceBean.insert(${classNameLower});
        return "插入成功";
    }

    @RequestMapping(value = "/delete/{id}",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String delete(@PathVariable("id")${table.idColumn.simpleJavaType} id, HttpServletRequest request){
        logger.info("=${className}Controller.delete=> id:{}",String.valueOf(id));
        ${classNameLower}ServiceBean.delete(id);
        return "删除成功";
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public String update(${className} ${classNameLower},HttpServletRequest request){
        logger.info("=${className}Controller.update=> name:{}",JSON.toJSONString(${classNameLower}));
        ${classNameLower}ServiceBean.update(${classNameLower});
        return "更新成功";
    }

}
