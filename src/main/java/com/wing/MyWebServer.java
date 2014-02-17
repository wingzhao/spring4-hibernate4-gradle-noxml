package com.wing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jetty web server start
 * <p/>
 * <p/>
 * Created by wingzhao on 14-2-13.
 */
public class MyWebServer {

    private static final Logger logger = LoggerFactory.getLogger(MyWebServer.class);

    private JettyServer jettyServer;

    public MyWebServer() {
        jettyServer = new JettyServer(JettyConfig.Factory.newDevelopConfig("7kmDev", 8080, "localhost"));
    }

    /**
     * 
     */
    public void start() throws Exception {
        jettyServer.start();
        jettyServer.join();
    }

    public static void main(String[] args) throws Exception {
        logger.info("go jetty");
        MyWebServer jettyRun = new MyWebServer();
        jettyRun.start();
    }
}
