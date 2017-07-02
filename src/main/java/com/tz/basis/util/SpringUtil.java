package com.tz.basis.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 */
@Component
public class SpringUtil {

	private static final Logger logger = LoggerFactory.getLogger(SpringUtil.class);

	@Autowired
	private ApplicationContext applicationContext;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * <pre>
	 * getBean(HttpServletRequest req, String beanId)
	 * </pre>
	 *
	 * @param req
	 * @param beanId
	 * @return
	 */
	public Object getBean(String beanId) {
		try {
			if (getApplicationContext().containsBean(beanId)) {
				return getApplicationContext().getBean(beanId);
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * <pre>
	 * getBean(HttpServletRequest req, String beanId)
	 * </pre>
	 *
	 * @param req
	 * @param beanId
	 * @return
	 */
	public Object getBean(HttpServletRequest req, String beanId) {
		try {
			if (getApplicationContext().containsBean(beanId)) {
				return getApplicationContext().getBean(beanId);
			}
		} catch (Exception e) {
		}

		Object beanObject = null;

		// DispatcherServlet으로 로딩된 context를 가져 온다.
		WebApplicationContext webApplicationContext = RequestContextUtils.getWebApplicationContext(req);
		if (webApplicationContext.containsBean(beanId)) {
			webApplicationContext.getBeanDefinitionNames();
			beanObject = webApplicationContext.getBean(beanId);
			return beanObject;
		}

		HttpSession hs = req.getSession();
		ServletContext sc = hs.getServletContext();
		// ContextLoaderListener으로 로딩된 context를 가져 온다.
		webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);

		if (webApplicationContext.containsBean(beanId)) {
			beanObject = webApplicationContext.getBean(beanId);
			return beanObject;
		}

		return beanObject;
	}

}
