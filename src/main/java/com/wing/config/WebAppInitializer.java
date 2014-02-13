package com.wing.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

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

    /**
     * 初始化方法
     *
     * @param sc
     * @throws ServletException
     */
    @Override
    public void onStartup(final ServletContext sc) throws ServletException {
        System.out.println("start init web application");
        AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
        root.setServletContext(sc);
        root.scan("com.wing.config");
        root.refresh();
        sc.addListener(new ContextLoaderListener(root));
        ServletRegistration.Dynamic appServlet =
                sc.addServlet("appServlet", new DispatcherServlet(new GenericWebApplicationContext()));

        appServlet.setLoadOnStartup(1);
        Set<String> mappingConflicts = appServlet.addMapping("/");
        if (!mappingConflicts.isEmpty()) {
            System.out.println("error");
        }

    }
}
