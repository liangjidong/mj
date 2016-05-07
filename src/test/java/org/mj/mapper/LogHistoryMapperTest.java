package org.mj.mapper;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.chudian.mj.bean.LogHistory;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.mapper.LogHistoryMapper;
import org.chudian.mj.service.LogHistoryService;
import org.chudian.mj.service.impl.LogHistoryServiceImpl;
import org.junit.Test;
public class LogHistoryMapperTest  extends AbstractTest{

	LogHistoryMapper logHistoryMapper=null;
	LogHistoryService logHistoryService=null;
	public LogHistoryMapperTest(){
		super();
		logHistoryMapper=(LogHistoryMapper) context.getBean("logHistoryMapper");
		logHistoryService=new LogHistoryServiceImpl(); 
	}

	@Test
	public  void testUpdateStatus() {
		QueryBase queryBase = new QueryBase();
		queryBase.addParameter("userId", 2); 
		//System.out.println(logHistoryMapper.queryByUserId(queryBase));
		queryBase.setFirstRow(0L);
		queryBase.setMaxRow(10L);
		logHistoryService.query(queryBase);
		System.out.println(queryBase);
//		LogHistory record=new LogHistory();
//		record.setUserId(2);
//		record.setOperationContent("haha");
//		record.setOperationType("aa");
//		record.setOperatonTime(new Date());
//		logHistoryMapper.insert(record);
		
	}

}
