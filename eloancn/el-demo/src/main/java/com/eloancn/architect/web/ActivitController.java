package com.eloancn.architect.web;

import com.alibaba.fastjson.JSON;
import com.eloancn.framework.activiti.TaskResult;
import com.eloancn.framework.activiti.model.ELProcessInstance;
import com.eloancn.framework.activiti.model.ELTask;
import com.eloancn.framework.activiti.model.Variable;
import com.eloancn.framework.activiti.service.ElActivitiService;
import com.eloancn.framework.activiti.util.Page;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * &#x8bf7;&#x5047;&#x63a7;&#x5236;&#x5668;&#xff0c;&#x5305;&#x542b;&#x4fdd;&#x5b58;&#x3001;&#x542f;&#x52a8;&#x6d41;&#x7a0b;
 *
 * @author xvshu
 */
@Controller
@RequestMapping(value = "/activiti")
public class ActivitController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static int PAGE_SIZE = 10;

    @Autowired
    private ElActivitiService elActiviti;




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
    @RequestMapping(value = "list/taskall/findall")
    @ResponseBody
    public Page<ActivitResult> taskAllPageFind(Integer page, Integer rows ,HttpSession session, HttpServletRequest request) {
        logger.info("=finished.finishedall=> page:{} rows:{}",page,rows);
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
        pageResult = elActiviti.findTodoTasks(userId,pageResult,BusCodeEnum.LI_CAI);
        List<TaskResult>  results = pageResult.getResult();
        List<ActivitResult> resultsfic = new ArrayList<>();

        for(TaskResult taskResult:results){
            ActivitResult activitResult = new ActivitResult();
            ELProcessInstance elProcessInstance = ELProcessInstance.covert(taskResult.getProcessInstance());
            activitResult.setElProcessInstance(elProcessInstance);
            activitResult.setTask(ELTask.convert(taskResult.getTask()));
            resultsfic.add(activitResult);
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

        public ELProcessInstance getElProcessInstance() {
            return elProcessInstance;
        }

        public void setElProcessInstance(ELProcessInstance elProcessInstance) {
            this.elProcessInstance = elProcessInstance;
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
    public String completeWF(String taskId, boolean isPass, Variable var, HttpSession session) {
        logger.info("=completeWF=> taskId:{} isPass:{}",taskId,isPass);
        try {
            Map<String, Object> variables = new HashedMap();

            String userId = LoginController.getUserID(session);
            if(userId==null){
                return "获取用户信息错误，请联系管理员";
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
