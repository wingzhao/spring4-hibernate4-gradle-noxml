package com.wing.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Set;

/**
 * web工程初始化。相当于原来的web.xml
 * <p/>
 * Created by wingzhao on 14-2-13.
 */
public class WebAppInitializer implements WebApplicationInitializer {

    Logger logger = LoggerFactory.getLogger(WebAppInitializer.class);

    /**
     * 初始化方法
     *
     * @param context
     * @throws ServletException
     */
    @Override
    public void onStartup(final ServletContext context) throws ServletException {
        logger.info("start init web application");
        setupListener(context);
        setupDispatcher(context);
    }


    /**
     * 注册监听
     */
    private void setupListener(ServletContext context) {
        AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
        root.setServletContext(context);
        root.scan("com.wing.config");
        root.refresh();
        context.addListener(new ContextLoaderListener(root));
        Log4jConfigListener log4jConfigListener = new Log4jConfigListener();

    }

    /**
     * 注册调度
     */
    private void setupDispatcher(ServletContext context) {
        ServletRegistration.Dynamic appServlet =
                context.addServlet("appServlet", new DispatcherServlet(new GenericWebApplicationContext()));
        appServlet.setLoadOnStartup(1);
        Set<String> mappingConflicts = appServlet.addMapping("/");
        if (!mappingConflicts.isEmpty()) {
            System.out.println("error");
        }
    }


    /**
     * 注册过滤器
     */
    private void setupFilter(ServletContext context){

    }

    /**
     * 注册servlet
     */
    private void setupServlet(ServletContext context) {

    }
}
