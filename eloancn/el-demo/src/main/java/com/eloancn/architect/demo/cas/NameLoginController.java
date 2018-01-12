package com.eloancn.architect.demo.cas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * NameLoginController
 * Created by qinxf on 2017/11/6.
 */
@Controller
@RequestMapping("/login")
public class NameLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NameLoginController.class);

    /**
     * 使用name登陆
     * @param model
     * @param name
     * @return
     */
    @RequestMapping(value="/name")
    public String login(ModelMap model, @RequestParam String name) {
        LOGGER.info("=LoginController.login=>user name [{}] is login!",name);
        return "/login/name";
    }
}
