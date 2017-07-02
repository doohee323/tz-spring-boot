package com.tz.basis.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Properties;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer {

	private static final Logger logger = LoggerFactory.getLogger(WebAppInitializer.class);

	@Override
	public void onStartup(ServletContext container) throws ServletException {
		useRootContext(container);
		useDispatcherContext(container);
		addFilter(container);
	}

	private void useRootContext(ServletContext container) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			String filename = "config/application.properties";
			input = WebAppInitializer.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				logger.error("Sorry, unable to find " + filename);
				return;
			}
			prop.load(input);
			String server_type = prop.getProperty("server_type");
			logger.error("======================== server_type: " + server_type);
			if (server_type != null && server_type.equalsIgnoreCase("jetty")) {
				container.setInitParameter("log4jConfigLocation", "classpath:/config/log4j.xml");
				AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
				rootContext.register(DatabaseConfig.class, AppConfig.class, SwaggerConfig.class, SecurityConfig.class);
				container.addListener(new ContextLoaderListener(rootContext));
			}
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	private void useDispatcherContext(ServletContext container) {
		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
		dispatcherContext.register(DispatcherConfig.class);
		DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

		ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", dispatcherServlet);
		if(dispatcher != null) {
			dispatcher.addMapping("/");
			dispatcher.setLoadOnStartup(1);
		}
	}

	private void addFilter(ServletContext servletContext) {
		FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("CharacterEncodingFilter",
				new CharacterEncodingFilter());
		
		if(characterEncodingFilter != null) {
			characterEncodingFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
			characterEncodingFilter.setInitParameter("encoding", "UTF-8");
			characterEncodingFilter.setInitParameter("forceEncoding", "true");

			servletContext.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain"))
					.addMappingForUrlPatterns(null, false, "/*");

			servletContext.addFilter("HttpMethodFilter", org.springframework.web.filter.HiddenHttpMethodFilter.class)
					.addMappingForUrlPatterns(null, false, "/*");
		}
	}

}