package com.tz.test.basic;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tz.basis.Application;
import com.tz.extend.test.TestSupport;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@ComponentScan(basePackages = { "com.tz" })
public class TzServiceTest extends TestSupport {

	private static Logger logger = LoggerFactory.getLogger(TzServiceTest.class);

//	@Test
	public void RunTzServiceTest() throws Exception {
		runnTzTest("org.springframework.stereotype.Service");
	}
}
