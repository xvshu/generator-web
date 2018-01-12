package com.eloancn.architect.web;

import com.alibaba.fastjson.JSON;
import com.eloancn.architect.model.Name;
import com.eloancn.architect.service.NameServiceBean;
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

/**
 * 首页控制器
 *
 * @author xvshu
 */
@Controller
@RequestMapping("/name")
public class NameController {
    private static final Logger logger = LoggerFactory.getLogger(NameController.class);

    @Autowired
    private NameServiceBean nameServiceBean;

    @RequestMapping(value = "/index")
    public String index() {
        return "/name/namelist";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    public PageResultDTO<Name> page(Integer page,Integer rows,HttpServletRequest request){
        logger.info("=NameController.page=> page:{} rows:{}",String.valueOf(page),String .valueOf(rows));
        if(page==null){
            page=1;
        }
        if(rows==null){
            rows=10;
        }
        PageResultDTO<Name> pageResultDTO = new PageResultDTO();

        PageBounds pageBounds = new PageBounds();
        pageBounds.setPage(page);
        pageBounds.setLimit(rows);
        PageList<Name> pageResult = nameServiceBean.search(null,pageBounds);
        int total = nameServiceBean.searchCount(null);

        pageResultDTO.setItems(pageResult);
        pageResultDTO.setTotalCount(total);

        //todo：page无法set total
        logger.info("=NameController.page=> pageResult:{}", JSON.toJSONString(pageResultDTO));
        return pageResultDTO;
    }


    @RequestMapping(value = "/insert")
    @ResponseBody
    public String insert(Name name,HttpServletRequest request){
        logger.info("=NameController.insert=> name:{}",JSON.toJSONString(name));
        nameServiceBean.insert(name);
        return "success";
    }

    @RequestMapping(value = "/delete/{id}",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String delete(@PathVariable("id")int id, HttpServletRequest request){
        logger.info("=NameController.delete=> id:{}",String.valueOf(id));
        nameServiceBean.delete(id);
        return "success";
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public String update(Name name,HttpServletRequest request){
        logger.info("=NameController.update=> name:{}",JSON.toJSONString(name));
        nameServiceBean.update(name);
        return "success";
    }

}
