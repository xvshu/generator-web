package com.eloancn.framework.cas.listener;

import com.eloancn.framework.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * web项目文件动静分离
 * Created by qinxf on 2017/11/28.
 */
public class StaticPathListener implements ServletContextListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(StaticPathListener.class);

    private final static String STATIC_IMAGE_PATH = "static.image.path";
    private final static String STATIC_IMAGE_PATH_VERSION = "static.image.path.version";
    private final static String STATIC_FILE_PATH = "static.file.path";
    private final static String STATIC_FILE_PATH_VERSION = "static.file.path.version";


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            sce.getServletContext().setAttribute(STATIC_IMAGE_PATH , PropertiesUtil.getProperties(STATIC_IMAGE_PATH));
            sce.getServletContext().setAttribute(STATIC_IMAGE_PATH_VERSION , PropertiesUtil.getProperties(STATIC_IMAGE_PATH_VERSION));
            sce.getServletContext().setAttribute(STATIC_FILE_PATH , PropertiesUtil.getProperties(STATIC_FILE_PATH));
            sce.getServletContext().setAttribute(STATIC_FILE_PATH_VERSION , PropertiesUtil.getProperties(STATIC_FILE_PATH_VERSION));

        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute(STATIC_IMAGE_PATH);
        sce.getServletContext().removeAttribute(STATIC_IMAGE_PATH_VERSION);
        sce.getServletContext().removeAttribute(STATIC_FILE_PATH);
        sce.getServletContext().removeAttribute(STATIC_FILE_PATH_VERSION);
    }
}
