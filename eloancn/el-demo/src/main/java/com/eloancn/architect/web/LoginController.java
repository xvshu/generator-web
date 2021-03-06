package com.eloancn.architect.web;

import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.UserService;
import com.eloancn.organ.dto.UserDto;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 登录页面
 *
 * @author xvshu
 */
@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userOrganService;

    public final  static String SessionLoginUserKey = "SESSIONLOGINUSERKEY";

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    /**
     * 登录系统
     *
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/login/check")
    public String logon(@RequestParam("username") String userName, @RequestParam("password") String password, HttpSession session) {
        logger.debug("logon request: {username={}, password={}}", userName, password);
        boolean checkPassword = false;

        UserDto userDto=new UserDto();
        userDto.setUsername(userName);
        userDto.setLoginPasswd(password);
        ResultDTO<UserDto> resultDTO = userOrganService.login(userDto);

        if(resultDTO!=null && resultDTO.getData()!=null){
            checkPassword=true;
            setUserID(session,resultDTO.getData());
        }

        if (checkPassword) {
            return "redirect:/main/index";
        } else {
            return "redirect:/login?error=true";
        }
    }

    @RequestMapping(value = "/logout")
    public String logout() {

        //todo:远程调用，用户登出
        return "login";
    }

    public static  String getUserID(HttpSession session){
        String userID =null;
        UserDto userDto  = (UserDto)session.getAttribute(SessionLoginUserKey);
        if(userDto!=null && userDto.getId()!=null){
            userID=String.valueOf(userDto.getId());
        }
        return userID;
    }

    public static  void setUserID(HttpSession session,UserDto userDto){
        session.setAttribute(SessionLoginUserKey,userDto);
    }



}
