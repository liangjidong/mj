package org.mj.mapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zhangfei
 * @project jwc
 * @date Dec 9, 2012
 * @descrption  spring加载
 */
public abstract class AbstractTest {
	
	protected ApplicationContext context = null;
	protected Log logger = null;

	public AbstractTest() {
		context = new ClassPathXmlApplicationContext("classpath:spring-mybatis.xml");
		logger = LogFactory.getLog(this.getClass());
	}
	

}

