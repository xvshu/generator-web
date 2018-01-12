<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.web.pubserver;

import com.alibaba.fastjson.JSONObject;
import com.eloancn.framework.sevice.api.PageParsDTO;
import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.UserService;
import com.eloancn.organ.dto.PageInfoDto;
import com.eloancn.organ.common.ResultOrganCode;
import com.eloancn.organ.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author : CJT
 * @date : 2017/11/2
 */
@Controller("pubUserController")
@RequestMapping("/pubserver/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userOrganService;

    /**
     * 加载所有用户（分页）
     * @param pageParsDTO 分页信息
     * @param rows 每页条数
     * @param paramMap 查询条件
     * @return 查询结果
     */
    @RequestMapping(value = "/loadAll")
    @ResponseBody
    public ResultDTO<PageInfoDto<UserDto>> loadAll(PageParsDTO pageParsDTO,Integer rows,String paramMap){
        logger.info("UserController.loadAll PageParsDTO:{},Rows:{},paramMap:{} ", pageParsDTO,rows,paramMap);
        if (null!=rows && 0<rows){
            pageParsDTO.setLimit(rows);
        }else{
            pageParsDTO.setPage(0);
            pageParsDTO.setLimit(0);
        }
        if (!StringUtils.isBlank(paramMap)){
            Map map = (Map) JSONObject.parse(paramMap);
            pageParsDTO.setParam(map);
        }
        ResultDTO<PageInfoDto<UserDto>> resultDTO = userOrganService.searchUser(pageParsDTO);
        logger.info("UserController.loadAll PageParsDTO:{},Rows:{},paramMap:{},Result:{} ", pageParsDTO,rows,paramMap,resultDTO);
        return resultDTO;
    }

}
