<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.web.pubserver;

import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.DepartmentService;
import com.eloancn.organ.dto.DepartmentDto;
import com.eloancn.organ.common.ResultOrganCode;
import com.eloancn.organ.dto.TreeNodeDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门功能
 * @author : CJT
 * @date : 2017/11/2
 */
@Controller("pubDepartmentController")
@RequestMapping("/pubserver/department")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
    
    @Autowired
    private DepartmentService departmentOrganService;

    /**
     * 加载所有部门信息
     * @return 返回结果
     */
    @RequestMapping(value = "/loadAll")
    @ResponseBody
    public ResultDTO<List<TreeNodeDto>> loadAll(){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("status",1);
        ResultDTO<List<TreeNodeDto>> resultDTO  = departmentOrganService.getAllDepartment(paramMap);
        logger.info("DepartmentController.loadALl Result:{}",resultDTO);
        return resultDTO;
    }

}
