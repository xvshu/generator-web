package cn.org.rapid_framework.generator.web;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.web.socket.WebsocketHandler;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * GeneratorController
 * Created by qinxf on 2017/11/13.
 */
@Controller
@RequestMapping("/generator")
public class GeneratorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorController.class);

    /**
     * 创建web项目
     */
    @RequestMapping(value="/web")
    public String web(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam String basePackage, @RequestParam String projectName,
                            @RequestParam String tableRemovePrefixes, @RequestParam String jdbcIp,
                            @RequestParam String jdbcPort, @RequestParam String jdbcDatabase,
                            @RequestParam String jdbcUserName, @RequestParam String jdbcPassWord,
                            @RequestParam String tableCreateJsp, @RequestParam String systemCode) throws Exception {

        WebsocketHandler.sendMessageToUser(projectName, new TextMessage(new GsonBuilder().disableHtmlEscaping().create().
                toJson("=GeneratorController.web=>basePackage["+basePackage+"],projectName["+projectName+"],tableRemovePrefixes["+tableRemovePrefixes
                        +"],jdbcIp["+jdbcIp+"],jdbcPort["+jdbcPort+"],jdbcDatabase["+jdbcDatabase+"],jdbcUserName["+jdbcUserName+"],jdbcPassWord["+jdbcPassWord+"],tableCreateJsp["+tableCreateJsp+"] generator start!")));

        LOGGER.info("=GeneratorController.web=>basePackage[{}],projectName[{}],tableRemovePrefixes[{}],jdbcIp[{}],jdbcPort[{}],jdbcDatabase[{}],jdbcUserName[{}],jdbcPassWord[{}],tableCreateJsp[{}] generator start!",
                basePackage,projectName,tableRemovePrefixes,jdbcIp,jdbcPort,jdbcDatabase,jdbcUserName,jdbcPassWord,tableCreateJsp);

        String realPath = request.getSession().getServletContext().getRealPath("/");
        LOGGER.info("---------------------------------realPath-------"+realPath);

        //生成项目
        GeneratorFacade g = new GeneratorFacade(basePackage,projectName,tableRemovePrefixes,jdbcIp,jdbcPort,jdbcDatabase,jdbcUserName,jdbcPassWord,realPath,tableCreateJsp,systemCode);
        g.deleteOutRootDir();							//删除生成器的输出目录

        if (!StringUtils.isEmpty(tableCreateJsp)){
            String [] tables  = tableCreateJsp.split(",");
            for (String table:tables ) {
                g.generateByTable(table ,realPath + "WEB-INF/classes/temp-web");
            }
        }
        g.generateByAllTable(realPath + "WEB-INF/classes/template");	//自动搜索数据库中的所有表并生成文件,template为模板的根目录
        TableFactory.getInstance().getConnection().close();

        //压缩项目Zip
        String path = GeneratorProperties.getRequiredProperty("outRoot") + projectName;
        PathToZip zc = new PathToZip(path + ".zip",projectName);
        zc.compress(path);

        //下载文件
        File file = new File(GeneratorProperties.getRequiredProperty("outRoot") + projectName + ".zip");
        // 以流的形式下载文件。
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"));
        toClient.write(buffer);
        toClient.flush();
        toClient.close();

        //清理目录
        file.delete();        //是否将生成的服务器端文件删除
        g.deleteOutRootDir();							//删除生成器的输出目录


        LOGGER.info("=GeneratorController.web=>basePackage[{}],projectName[{}],tableRemovePrefixes[{}],jdbcIp[{}],jdbcPort[{}],jdbcDatabase[{}],jdbcUserName[{}],jdbcPassWord[{}],tableCreateJsp[{}] generator end!",
                basePackage,projectName,tableRemovePrefixes,jdbcIp,jdbcPort,jdbcDatabase,jdbcUserName,jdbcPassWord,tableCreateJsp);

        WebsocketHandler.sendMessageToUser(projectName, new TextMessage(new GsonBuilder().disableHtmlEscaping().create().
                toJson("=GeneratorController.web=>basePackage["+basePackage+"],projectName["+projectName+"],tableRemovePrefixes["+tableRemovePrefixes
                        +"],jdbcIp["+jdbcIp+"],jdbcPort["+jdbcPort+"],jdbcDatabase["+jdbcDatabase+"],jdbcUserName["+jdbcUserName+"],jdbcPassWord["+jdbcPassWord+"],tableCreateJsp["+tableCreateJsp+"] generator end!")));

        WebsocketHandler.sendMessageToUser(projectName, new TextMessage(new GsonBuilder().disableHtmlEscaping().create().toJson("finish!")));

        return "/generator";
    }

    /**
     * 创建微服务
     */
    @RequestMapping(value="/micro")
    public String micro(HttpServletRequest request, HttpServletResponse response,
                                            @RequestParam String basePackage, @RequestParam String projectName,
                                            @RequestParam String tableRemovePrefixes, @RequestParam String jdbcIp,
                                            @RequestParam String jdbcPort, @RequestParam String jdbcDatabase,
                                            @RequestParam String jdbcUserName, @RequestParam String jdbcPassWord, @RequestParam String systemCode) throws Exception {

        WebsocketHandler.sendMessageToUser(projectName, new TextMessage(new GsonBuilder().disableHtmlEscaping().create().
                toJson("=GeneratorController.micro=>basePackage["+basePackage+"],projectName["+projectName+"],tableRemovePrefixes["+tableRemovePrefixes
                        +"],jdbcIp["+jdbcIp+"],jdbcPort["+jdbcPort+"],jdbcDatabase["+jdbcDatabase+"],jdbcUserName["+jdbcUserName+"],jdbcPassWord["+jdbcPassWord+"] generator start!")));

        LOGGER.info("=GeneratorController.micro=>basePackage[{}],projectName[{}],tableRemovePrefixes[{}],jdbcIp[{}],jdbcPort[{}],jdbcDatabase[{}],jdbcUserName[{}],jdbcPassWord[{}] generator start!",
                basePackage,projectName,tableRemovePrefixes,jdbcIp,jdbcPort,jdbcDatabase,jdbcUserName,jdbcPassWord);

        String realPath = request.getSession().getServletContext().getRealPath("/");
        LOGGER.info("---------------------------------realPath-------"+realPath);

        //生成项目
        GeneratorFacade g = new GeneratorFacade(basePackage,projectName,tableRemovePrefixes,jdbcIp,jdbcPort,jdbcDatabase,jdbcUserName,jdbcPassWord,realPath,"",systemCode);
        g.deleteOutRootDir();							//删除生成器的输出目录
        g.generateByAllTable(realPath + "WEB-INF/classes/template");	//自动搜索数据库中的所有表并生成文件,template为模板的根目录
        TableFactory.getInstance().getConnection().close();

        //压缩项目Zip
        String path = GeneratorProperties.getRequiredProperty("outRoot") + projectName;
        PathToZip zc = new PathToZip(path + ".zip",projectName);
        zc.compress(path);

        //下载文件
        File file = new File(GeneratorProperties.getRequiredProperty("outRoot") + projectName + ".zip");
        // 以流的形式下载文件。
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"));
        toClient.write(buffer);
        toClient.flush();
        toClient.close();

        //清理目录
        file.delete();        //是否将生成的服务器端文件删除
        g.deleteOutRootDir();							//删除生成器的输出目录


        LOGGER.info("=GeneratorController.micro=>basePackage[{}],projectName[{}],tableRemovePrefixes[{}],jdbcIp[{}],jdbcPort[{}],jdbcDatabase[{}],jdbcUserName[{}],jdbcPassWord[{}] generator end!",
                basePackage,projectName,tableRemovePrefixes,jdbcIp,jdbcPort,jdbcDatabase,jdbcUserName,jdbcPassWord);

        WebsocketHandler.sendMessageToUser(projectName, new TextMessage(new GsonBuilder().disableHtmlEscaping().create().
                toJson("=GeneratorController.micro=>basePackage["+basePackage+"],projectName["+projectName+"],tableRemovePrefixes["+tableRemovePrefixes
                        +"],jdbcIp["+jdbcIp+"],jdbcPort["+jdbcPort+"],jdbcDatabase["+jdbcDatabase+"],jdbcUserName["+jdbcUserName+"],jdbcPassWord["+jdbcPassWord+"] generator end!")));

        WebsocketHandler.sendMessageToUser(projectName, new TextMessage(new GsonBuilder().disableHtmlEscaping().create().toJson("finish!")));

        return "/generator";
    }

    /**
     * go to generator.html
     * @param model
     * @return
     */
    @RequestMapping(value="/go")
    public String generator(ModelMap model) {
        LOGGER.info("=GeneratorController.go=> to generator.html!");

        return "/generator";
    }


}
