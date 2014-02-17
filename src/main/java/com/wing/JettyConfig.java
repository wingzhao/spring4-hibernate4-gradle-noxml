package com.wing;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.ClassInheritanceHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.springframework.web.WebApplicationInitializer;

import com.wing.config.WebAppInitializer;

/**
 * jetty config.相当于jetty的配置文件
 * <p/>
 * Created by wingzhao on 14-2-14.
 */
public interface JettyConfig {

	/**
	 * 获取线程池
	 * 
	 * @return
	 */
	public ThreadPool getThreadPool();

	/**
	 * 链接设置
	 * 
	 * @param server
	 *            jetty server
	 * @return
	 */
	public Connector[] getConnectors(Server server);

	/**
	 * 处理设置
	 * 
	 * @return
	 */
	public HandlerCollection getHandlers();

	public String getServerName();

	public int getPort();

	public String getHost();

	public String getContextPath();

	public int getMinThreads();

	public int getMaxThreads();

	public String getAccessLogDir();

	public class Factory {

		public static JettyConfig newDevelopConfig(String serverName, int port,
				String host) {
			return new DevelopConfig(serverName, port, host);
		}

		public static JettyConfig newTestConfig(String serverName, int port,
				String host, int minThreads, int maxThreads) {
			return new TestConfig(serverName, port, host, minThreads,
					maxThreads);
		}

		public static JettyConfig newOnlineConfig(String serverName, int port,
				String host, int minThreads, int maxThreads) {
			return new OnlineConfig(serverName, port, host, minThreads,
					maxThreads);
		}

		/**
         *
         */
		private static abstract class AbstractJettyConfig implements
				JettyConfig {

			private AbstractJettyConfig(String serverName, int port,
					String host, int minThreads, int maxThreads) {
				this.serverName = serverName;
				this.port = port;
				this.host = host;
				this.minThreads = minThreads;
				this.maxThreads = maxThreads;
			}

			/**
			 * server name
			 */
			private String serverName;
			/**
			 * 端口
			 */
			private int port;

			/**
			 * context path
			 */
			private String contextPath;

			/**
			 * host
			 */
			private String host;
			/**
			 * 初始线程数
			 */
			private int minThreads;
			/**
			 * 最大线程数
			 */
			private int maxThreads;

			@Override
			public ThreadPool getThreadPool() {
				QueuedThreadPool threadPool = new QueuedThreadPool();
				threadPool.setName(serverName);
				threadPool.setMinThreads(minThreads);
				threadPool.setMaxThreads(maxThreads);
				return threadPool;
			}

			@Override
			public Connector[] getConnectors(Server server) {
				Set<Connector> connectors = new HashSet<>();

				// HTTP Configuration
				HttpConfiguration http_config = new HttpConfiguration();
				http_config.setSecureScheme("https");
				http_config.setSecurePort(8443);

				// HTTP connector
				ServerConnector httpConnector = new ServerConnector(server,
						new HttpConnectionFactory(http_config));
				httpConnector.setPort(port);
				httpConnector.setHost(host);

				connectors.add(httpConnector);

				// String jettyHome = ".";
				/*
				 * // SSL Context Factory for HTTPS and SPDY SslContextFactory
				 * sslContextFactory = new SslContextFactory();
				 * sslContextFactory.setKeyStorePath(jettyHome +
				 * "/etc/keystore"); sslContextFactory.setKeyStorePassword(
				 * "OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
				 * sslContextFactory.setKeyManagerPassword
				 * ("OBF:1u2u1wml1z7s1z7a1wnl1u2g");
				 * 
				 * // HTTPS Configuration HttpConfiguration https_config = new
				 * HttpConfiguration(http_config);
				 * https_config.addCustomizer(new SecureRequestCustomizer());
				 * 
				 * // SPDY versions HTTPSPDYServerConnectionFactory spdy2 = new
				 * HTTPSPDYServerConnectionFactory(2, https_config);
				 * 
				 * HTTPSPDYServerConnectionFactory spdy3 = new
				 * HTTPSPDYServerConnectionFactory(3, https_config, new
				 * ReferrerPushStrategy());
				 * 
				 * // NPN Factory
				 * SPDYServerConnectionFactory.checkNPNAvailable();
				 * NPNServerConnectionFactory npn = new
				 * NPNServerConnectionFactory(spdy3.getProtocol(),
				 * spdy2.getProtocol(), http.getDefaultProtocol());
				 * npn.setDefaultProtocol(http.getDefaultProtocol());
				 * 
				 * // SSL Factory SslConnectionFactory ssl = new
				 * SslConnectionFactory(sslContextFactory, npn.getProtocol());
				 * 
				 * // SPDY Connector ServerConnector spdyConnector = new
				 * ServerConnector(server, ssl, npn, spdy3, spdy2, new
				 * HttpConnectionFactory(https_config));
				 * spdyConnector.setPort(8443);
				 * server.addConnector(spdyConnector);
				 */
				return connectors.toArray(new Connector[connectors.size()]);
			}

			@Override
			public HandlerCollection getHandlers() {
				WebAppContext webAppContext = new WebAppContext();
				webAppContext.setBaseResource(Resource
						.newClassPathResource("webapp"));
				webAppContext.setContextPath("/");
				webAppContext.setConfigurations(new Configuration[] {
						new WebXmlConfiguration(),
						new AnnotationConfiguration() {
							@Override
							public void preConfigure(WebAppContext context)
									throws Exception {
								ConcurrentHashMap<String, ConcurrentHashSet<String>> map = new ConcurrentHashMap<>();
								ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
								set.add(WebAppInitializer.class.getName());
								map.put(WebApplicationInitializer.class
										.getName(), set);
								context.setAttribute(CLASS_INHERITANCE_MAP, map);
								_classInheritanceHandler = new ClassInheritanceHandler(
										map);
							}
						} });
				webAppContext.setParentLoaderPriority(true);
				RequestLogHandler logHandler = new RequestLogHandler();
				logHandler.setRequestLog(createRequestLog());
				HandlerCollection handlers = new HandlerCollection();
				handlers.setHandlers(new Handler[] { webAppContext, logHandler });
				return handlers;
			}

			/**
			 * create access log config
			 * 
			 * @return
			 */
			private RequestLog createRequestLog() {
				NCSARequestLog log = new NCSARequestLog();
				File logPath = new File(getAccessLogDir()
						+ "yyyy_mm_dd.request.log");
				logPath.getParentFile().mkdirs();
				log.setFilename(logPath.getPath());
				log.setRetainDays(30);
				log.setExtended(false);
				log.setAppend(true);
				log.setLogTimeZone("UTC");
				log.setLogLatency(true);
				return log;
			}

			@Override
			public String getServerName() {
				return serverName;
			}

			@Override
			public int getPort() {
				return port;
			}

			@Override
			public String getHost() {
				return host;
			}

			@Override
			public String getContextPath() {
				return contextPath;
			}

			@Override
			public int getMinThreads() {
				return minThreads;
			}

			@Override
			public int getMaxThreads() {
				return maxThreads;
			}

			@Override
			public String getAccessLogDir() {
				return String.format("./logs/%s/", serverName);
			}
		}

		/**
		 * 开发时配置
		 */
		private static final class DevelopConfig extends AbstractJettyConfig {
			private DevelopConfig(String serverName, int port, String host) {
				super(serverName, port, host, 2, 10);
			}
		}

		/**
		 * 测试时配置
		 */
		private static final class TestConfig extends AbstractJettyConfig {
			private TestConfig(String serverName, int port, String host,
					int minThreads, int maxThreads) {
				super(serverName, port, host, minThreads, maxThreads);
			}
		}

		/**
		 * 线上配置
		 */
		private static final class OnlineConfig extends AbstractJettyConfig {
			private OnlineConfig(String serverName, int port, String host,
					int minThreads, int maxThreads) {
				super(serverName, port, host, minThreads, maxThreads);
			}
		}

	}

}
