package org.mj.mapper;

import static org.junit.Assert.*;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.chudian.mj.bean.Mjproduct;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.mapper.MjproductMapper;
import org.junit.Test;
import org.mj.mapper.AbstractTest;;

public class MjproductMapperTest extends AbstractTest{
	MjproductMapper mjproductMapper=null;
	public MjproductMapperTest(){
		super();
		mjproductMapper=(MjproductMapper) context.getBean("mjproductMapper");
	}

	@Test
	public  void testUpdateStatus() {
		QueryBase query=new QueryBase();
//		query.setFirstRow(0L);
//		query.setMaxRow(10L);
		query.addParameter("userId", 17);
		List<Mjproduct> productlist=mjproductMapper.queryProducts(query);
		System.out.println(productlist.size());
	}

}
