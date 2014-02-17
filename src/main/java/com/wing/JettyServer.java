package com.wing;

import org.eclipse.jetty.server.Server;

/**
 * jetty Server
 * <p/>
 * Created by wingzhao on 14-2-14.
 */
public class JettyServer {

    private Server server;

    private JettyConfig jettyConfig;

    /**
     * 构造函数
     *
     * @param config
     */
    public JettyServer(JettyConfig config) {
        this.jettyConfig = config;
    }

    /**
     * start server
     *
     * @throws Exception
     */
    public void start() throws Exception {
        server = new Server(jettyConfig.getThreadPool());
        server.setConnectors(jettyConfig.getConnectors(server));
        server.setHandler(jettyConfig.getHandlers());
        server.setDumpAfterStart(true);
        server.setStopAtShutdown(true);
        server.start();
    }

    /**
     * join server
     *
     * @throws InterruptedException
     */
    public void join() throws InterruptedException {
        if (!server.isStarted())
            server.join();
    }

    /**
     * @throws Exception
     */
    public void stop() throws Exception {
        server.stop();
    }
}
