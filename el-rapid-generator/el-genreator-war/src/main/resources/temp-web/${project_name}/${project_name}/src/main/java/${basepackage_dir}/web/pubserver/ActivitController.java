
<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.web.pubserver;

import com.alibaba.fastjson.JSON;
import com.el.config.api.SystemActivitiService;
import com.el.config.dto.SystemActivitiDto;
import com.eloancn.framework.activiti.TaskResult;
import com.eloancn.framework.activiti.model.ELProcessInstance;
import com.eloancn.framework.activiti.model.ELTask;
import com.eloancn.framework.activiti.model.Variable;
import com.eloancn.framework.activiti.service.ElActivitiService;
import com.eloancn.framework.activiti.util.Page;
import com.eloancn.framework.activiti.util.TaskType;
import com.eloancn.organ.common.BusCodeEnum;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.eloancn.framework.sevice.api.ResultDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Date;
<#include "/java_imports.include">

/**
 * &#x8bf7;&#x5047;&#x63a7;&#x5236;&#x5668;&#xff0c;&#x5305;&#x542b;&#x4fdd;&#x5b58;&#x3001;&#x542f;&#x52a8;&#x6d41;&#x7a0b;
 *
 * @author xvshu
 */
@Controller("pubActivitController")
@RequestMapping(value = "/pubserver/activiti")
public class ActivitController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static int PAGE_SIZE = 10;

    @Autowired
    private ElActivitiService elActiviti;

    @Autowired
    private SystemActivitiService systemActivitiService;




    /**
     * 任务列表
     *
     */
    @RequestMapping(value = "list/taskall")
    public ModelAndView taskListAll(HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/activiti/all-task");
        return mav;
    }

    /**
     * 任务列表
     *
     */
    @RequestMapping(value = "/taskdo")
    public ModelAndView taskDo(String taskId,String taskType, HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/activiti/task-do");
        mav.addObject("taskId",taskId);
        mav.addObject("taskType",taskType);
        return mav;
    }


    /**
     * 任务列表
     *
     */
    @RequestMapping(value = "list/taskall/findall")
    @ResponseBody
    public Page<ActivitResult> taskAllPageFind(Integer page, Integer rows , HttpSession session, HttpServletRequest request) {
        logger.info("=ActivitController.findall=> page:{} rows:{}",page,rows);
        String userId = LoginController.getUserID(session);
        if(userId==null){
            return null;
        }

        if(page==null){
            page=1;
        }
        if(rows==null){
            rows=10;
        }

        Page<TaskResult> pageResult = new Page<TaskResult>(PAGE_SIZE);
        pageResult.setPageNo(page);
        pageResult.setPageSize(rows);

        //todo:BusCodeEnum.LI_CAI 修改为自己系统标识
        List<String> processDefinitionKeys = new ArrayList<String>();
        //本系统涉及的流程key集合
        try{
            SystemActivitiDto searchDto =new SystemActivitiDto();
            searchDto.setSystemCode("${systemCode}");
            ResultDTO<List<SystemActivitiDto>> listResultDTO = systemActivitiService.searchAndEques(searchDto);
            for(SystemActivitiDto oneDto : listResultDTO.getData()){
                processDefinitionKeys.add(oneDto.getActivitiKey());
            }
        }catch (Exception ex){
            logger.error("taskAllPageFind =>",ex);
        }

        if(processDefinitionKeys==null || processDefinitionKeys.size()<=0){
            return new Page<ActivitResult>() ;
        }

        pageResult = elActiviti.findTodoTasks(processDefinitionKeys,userId,pageResult,BusCodeEnum.LI_CAI);

        List<TaskResult> results = pageResult.getResult();
        List<ActivitResult> resultsfic = new ArrayList<>();

        if(results!=null){
            for(TaskResult taskResult:results){
                ActivitResult activitResult = new ActivitResult();
                ELProcessInstance elProcessInstance = ELProcessInstance.covert(taskResult.getProcessInstance());
                if(taskResult.getTaskType().equals(TaskType.ActivitiTask) && elProcessInstance==null){
                    elProcessInstance=new ELProcessInstance();
                    elProcessInstance.setProcessDefinitionName(taskResult.getTask().getName());
                }
                if(taskResult.getTaskType().equals(TaskType.ActivitiTask)){
                    elProcessInstance.setVariables(elActiviti.getVariables(taskResult.getProcessInstance().getProcessInstanceId()));
                }
                activitResult.setElProcessInstance(elProcessInstance);
                activitResult.setTask(ELTask.convert(taskResult.getTask()));
                activitResult.setTaskType(taskResult.getTaskType());
                resultsfic.add(activitResult);
            }
        }


        Page<ActivitResult> pageResultFix = new Page<ActivitResult>(PAGE_SIZE);
        pageResultFix.setPageNo(page);
        pageResultFix.setPageSize(rows);
        pageResultFix.setResult(resultsfic);
        pageResultFix.setTotalCount(pageResult.getTotalCount());
        logger.info("=findTodoTasks=> result:{} ", JSON.toJSONString(pageResultFix));
        return pageResultFix;
    }

    public class ActivitResult{
        private ELProcessInstance elProcessInstance;
        private ELTask task;
        private TaskType taskType;

        public ELProcessInstance getElProcessInstance() {
            return elProcessInstance;
        }

        public void setElProcessInstance(ELProcessInstance elProcessInstance) {
            this.elProcessInstance = elProcessInstance;
        }
        public TaskType getTaskType() {
            return taskType;
        }

        public void setTaskType(TaskType taskType) {
            this.taskType = taskType;
        }

        public ELTask getTask() {
            return task;
        }

        public void setTask(ELTask task) {
            this.task = task;
        }
    }






    /**
     * 签收任务
     */
    @RequestMapping(value = "task/claimWF/{id}")
    @ResponseBody
    public String claimWF(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
        //todo:获取真实用户id
        String userId = LoginController.getUserID(session);
        if(userId==null){
            return "获取用户信息错误，请联系管理员";
        }

        //todo:BusCodeEnum.LI_CAI 修改为自己系统标识
        elActiviti.claim(taskId, userId,BusCodeEnum.LI_CAI);
        return "任务已签收";
    }





    /**
     * 完成任务
     *
     * @return
     */
    @RequestMapping(value = "completeWf", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String completeWF(String taskId, boolean isPass,String taskType, Variable var, HttpSession session) {
        logger.info("=completeWF=> taskId:{} isPass:{}",taskId,isPass);
        try {
            Map<String, Object> variables = new HashedMap();

            String userId = LoginController.getUserID(session);
            if(userId==null){
                return "获取用户信息错误，请联系管理员";
            }
            if(taskType!=null && taskType.equals(TaskType.AlermTask.toString())){
                elActiviti.msgComplete(userId,taskId,BusCodeEnum.LI_CAI);
                return "办理成功！";
            }

            String nextUserId = (String)variables.get("nextUserId");

            String[] nUsers =null;
            if(nextUserId!=null &&nextUserId.length()>0){
                nUsers = nextUserId.split(",");
            }
            List<String> nextUserIds = new ArrayList<>();
            if(nUsers!=null && nUsers.length>0){
                nextUserIds.addAll(Arrays.asList(nUsers));
            }

            variables.put("pass",isPass);

            logger.info("=ActivitDemoController.complete=>taskId:{} nextUserId:{} variables:{}",taskId, JSON.toJSONString(nextUserId), JSON.toJSONString(variables));

            //todo:BusCodeEnum.LI_CAI 修改为自己系统标识
            elActiviti.nodeComplete(userId,taskId, variables,nextUserIds,BusCodeEnum.LI_CAI);
            return "办理成功！";
        } catch (Exception e) {
            logger.error("=ActivitDemoController.complete=>error on complete task {}, variables={}", new Object[]{taskId, var.getVariableMap(), e});
            return "出现错误，请联系管理员";
        }
    }



}
