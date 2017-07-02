package com.tz.extend.test;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.WebApplicationContext;

/**
 * <pre>
 * </pre>
 */
@Component
public abstract class TestSupport implements Test {

	private static Logger logger = LoggerFactory.getLogger(TestSupport.class);

	@Autowired
	protected ApplicationContext appContext;

	@Autowired
	protected WebApplicationContext wac;

	@Autowired
	protected MockServletContext mockServletContext;

	public MockMvc mockMvc;

	public MockMvc getMockMvc() {
		return mockMvc;
	}

	public void setMockMvc(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(this.wac).build();
	}

	private static final String LOG_NAME = "test";

	private static boolean inited = false;

	private Logger LOG = LoggerFactory.getLogger(LOG_NAME);

	private enum Stage {
		SCENARIO, GIVEN, WHEN, THEN
	};

	Stage stage = null;

	public void init() {
		if (!inited) {
			printFixtureInformation();
			inited = true;
		}
	}

	/**
	 * <pre>
	 * </pre>
	 */
	public void scenario(String message, Object... args) {
		if (isCurrent(Stage.GIVEN))
			and(message, args);
		else
			println("Scenario: " + message, args);
		this.stage = Stage.SCENARIO;
	}

	/**
	 * <pre>
	 * </pre>
	 */
	public void given(String message, Object... args) {

		init();
		checkFirstCallAndUseTestCaseMethodNameAsScenarioMessage();

		if (isCurrent(Stage.GIVEN))
			and(message, args);
		else
			println("\tGiven: " + message, args);
		this.stage = Stage.GIVEN;
	}

	/**
	 * <pre>
	 * </pre>
	 */
	public void when(String message, Object... args) {

		init();
		checkFirstCallAndUseTestCaseMethodNameAsScenarioMessage();

		if (isCurrent(Stage.WHEN))
			and(message, args);
		else
			println("\tWhen: " + message, args);
		this.stage = Stage.WHEN;
	}

	/**
	 * <pre>
	 * </pre>
	 */
	public void then(String message, Object... args) {
		if (isCurrent(Stage.THEN))
			and(message, args);
		else {

			if (!isCurrent(Stage.WHEN))
				throw new IllegalStateException("You have to call when() first");

			println("\tThen: " + message, args);
		}
		this.stage = Stage.THEN;
	}

	/**
	 * <pre>
	 * </pre>
	 */
	public void and(String message, Object... args) {
		println("\t\tAnd: " + String.format(message, args));
	}

	private void printFixtureInformation() {
		Method testCaseMethod = getTestCaseMethod();
		if (testCaseMethod == null)
			return;
		Class<?> clazz = testCaseMethod.getDeclaringClass();
		println("[Start scenario set : %s]", clazz.getSimpleName());
		println("\ttime: %tc", System.currentTimeMillis());
	}

	private void checkFirstCallAndUseTestCaseMethodNameAsScenarioMessage() {
		if (isNotFirstCall())
			return;

		String testCaseMethodName = getTestCaseMethodName();
		if (testCaseMethodName != null)
			scenario(getTestCaseMethodName());
	}

	/**
	 * <pre>
	 * </pre>
	 */
	private boolean isCurrent(Stage expect) {
		return (this.stage != null && this.stage == expect);
	}

	/**
	 * <pre>
	 * </pre>
	 */
	private boolean isNotFirstCall() {
		return stage != null;
	}

	/**
	 * <pre>
	 * </pre>
	 */
	private String getTestCaseMethodName() {

		Method method = getTestCaseMethod();
		return method == null ? null : method.getName();
	}

	private Method getTestCaseMethod() {
		Method result = null;
		StackTraceElement ste[] = Thread.currentThread().getStackTrace();

		int max = ste.length;
		for (int index = 1; index < max; index++) {
			StackTraceElement e = ste[index];

			try {
				Method method = ReflectionUtils.findMethod(Class.forName(e.getClassName()), e.getMethodName());
				//                if (method != null && method.isAnnotationPresent(org.junit.Test.class)) {
				//                    result = method;
				//                    break;
				//                }
			} catch (ClassNotFoundException e1) {
				continue;
			}
		}
		return result;
	}

	private void println(String message, Object... args) {
		LOG.info(String.format(message, args));
	}

	public void runnTzTest(String testType) {
		try {
			ConfigurableListableBeanFactory factory = null;
			if (appContext.getAutowireCapableBeanFactory() instanceof ConfigurableListableBeanFactory) {
				factory = (ConfigurableListableBeanFactory) appContext.getAutowireCapableBeanFactory();
				for (String name : factory.getBeanDefinitionNames()) {
					if (appContext.getBean(name).getClass().getName().indexOf(".test.") > -1) {
						BeanDefinition bd = factory.getBeanDefinition(name);
						if (bd instanceof AnnotatedBeanDefinition) {
							AnnotatedBeanDefinition abd = (AnnotatedBeanDefinition) bd;
							AnnotationMetadata classMeta = abd.getMetadata();
							Set<String> classAnno = classMeta.getAnnotationTypes();
							if (classAnno.toString().contains(testType)) {
								for (Method method : appContext.getBean(name).getClass().getDeclaredMethods()) {
									List<Annotation> methodAnno = Arrays.asList(method.getAnnotations());
									if (methodAnno.toString().contains("org.junit.Test")) {
										if (methodAnno.toString().contains("org.junit.Ignore")) {
											break;
										}
										Class<? extends Object> classObj = appContext.getBean(name).getClass();
										classObj.getMethod("setMockMvc", MockMvc.class).invoke(appContext.getBean(name), mockMvc);
										logger
												.debug(classObj.getName() + " / " + classMeta.getAnnotationTypes() + " / " + method.getName());
										Method defaultNull = classObj.getMethod(method.getName(), new Class<?>[0]);
										try {
											Object ob = defaultNull.invoke(appContext.getBean(name), new Object[0]);
											//assertTrue(classObj.getName() + " will succeed.", (Boolean) ob == true);
										} catch (Exception e) {
											logger.error(classObj.getName() + " / " + method.getName() + " will fail!");
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}