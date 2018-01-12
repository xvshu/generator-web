package com.eloancn.architect.demo.cas;

import com.eloancn.architect.base.SpringBaseTest;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;

/**
 * LoginControllerTest测试
 * Created by qinxf on 2017/11/6.
 */
public class NameLoginControllerTest extends SpringBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(NameLoginControllerTest.class);

    @Autowired
    private NameLoginController loginController;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @org.junit.Test
    public void testLoginController() throws Exception {
        LOGGER.info("=NameLoginControllerTest.testLoginController=>start at time[{}]", System.currentTimeMillis());
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/login/name").param("name", "zhangsan"));
        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView result = mvcResult.getModelAndView();
        Assert.isTrue(result.getViewName().equals("/login/name"));
        LOGGER.info("=NameLoginControllerTest.testLoginController=>end at time[{}]", System.currentTimeMillis());
    }
}
