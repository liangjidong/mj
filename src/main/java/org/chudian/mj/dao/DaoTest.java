package org.chudian.mj.dao;

import java.util.Map;

import org.apache.log4j.Logger;
import org.chudian.mj.utils.SocketDB;

public class DaoTest {
	
    public static void main(String []args){
    	Logger log = Logger.getLogger(DaoTest.class);
    	VideoDaoImpl videoImpl = new VideoDaoImpl();
    	for(int i = 0;i < 8;i++){
    		Map<String,String> result_db = videoImpl.getVideoUrl("118_1.jpg");
    		String videoUrl = (String) result_db.get("videoUrl");
    		String webUrl = (String) result_db.get("webUrl");
    		String mjProductId = (String) result_db.get("mjProductId");
    		System.out.println("videoUrl="+videoUrl + " webUrl="+ webUrl + " mjProductId="+ mjProductId);
    	}
    	ThreeDModelDaoImpl threeImpl = new ThreeDModelDaoImpl();
    	for(int i = 0;i < 8;i++){
    		Map<String,String> result_db = threeImpl.getThreeDUrl("118_1.jpg");
    		String webUrl = (String) result_db.get("webUrl");
    		String threeDUrl = (String) result_db.get("threeDUrl");
    		String mjProductId = (String) result_db.get("mjProductId");
    		System.out.println("threeDUrl="+threeDUrl + " webUrl="+ webUrl + " mjProductId="+ mjProductId);
    	}
//    	System.out.println(result_db);
//    	SocketDB socketDB = new SocketDB();
//    	Map<String,String> result_db1 = socketDB.getVideoUrl("118_1.jpg");
//        System.out.println(result_db1);
    }
}
