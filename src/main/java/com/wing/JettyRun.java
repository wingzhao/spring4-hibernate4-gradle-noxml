package com.wing;

import com.wing.config.WebAppInitializer;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jetty web server start
 * <p/>
 * <p/>
 * Created by wingzhao on 14-2-13.
 */
public class JettyRun {

    private static final Logger logger = LoggerFactory.getLogger(JettyRun.class);


    public static void main(String[] args) throws Exception {
        logger.info("go jetty");
        int port = 0;
        String resourceBase = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (i == 0) {
                port = Integer.parseInt(arg);
            } else if (i == 1) {
                resourceBase = arg;
            }
        }
        if (port == 0) {
            port = 8080;
        }
        if (resourceBase == null) {
            resourceBase = "./src/main/webapp/";
        }
        Server server = new Server(port);
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setResourceBase(resourceBase);
        webAppContext.setContextPath("/");
        webAppContext.setConfigurations(new Configuration[]{new AnnotationConfiguration()});
        webAppContext.setParentLoaderPriority(true);
        WebAppInitializer webAppInitializer = new WebAppInitializer();
        server.setHandler(webAppContext);
        server.addBean(webAppInitializer);
        server.start();
        server.join();
    }
}
