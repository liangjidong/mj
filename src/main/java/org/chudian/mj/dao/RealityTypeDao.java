package org.chudian.mj.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealityTypeDao {
	private BaseDaoService mySqlService = null;
	private ResultSet results = null;
	private List<DBParameter> paras = null;
	private Connection conn = null;

	public RealityTypeDao() {
		this.mySqlService = new MySQLDaoService();
		paras = new ArrayList<DBParameter>();
	}

	public Map<String, String> getRealityType(String fileName) {
		conn = mySqlService.getConnection();
		// log.info("begin getVideoUrl -- fileName is " + fileName);
		String sql = "select * from picture where name=?";
		paras.add(new DBParameter(1, DBParaType.String, fileName));
		results = mySqlService.executeQueryWithPreparedStatement(conn, sql,
				paras);
paras.clear();
		
		String pictureId = "";
		String videoId = "";
		String videoUrl = "";
		String webUrl = "";
		String mjProductId = "";
		String realityType="1";
		Map<String,String> result = new HashMap<String,String>();
		try {
			while (results.next()) {
				 pictureId = results.getString("id");
				 videoId = results.getString("video_id");
				 webUrl = results.getString("keepword1");
				 realityType = results.getString("reality_type");
			}
			mySqlService.closeResultSet(results); 
			result.put("webUrl", webUrl);
			result.put("videoUrl", videoUrl);
			result.put("mjProductId", mjProductId);
			result.put("realitytype", realityType);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//log.error(e.getStackTrace());
			e.printStackTrace();
		}finally{
			mySqlService.closeConnection(conn);
		//	log.info("end getVideoUrl -- result is " + result);
			return result;
		}
	}
	
	public static void main(String[] args){
		RealityTypeDao r= new RealityTypeDao();
		Map<String,String> result=r.getRealityType("2.jpg");
		System.out.println(result.get("realitytype"));
	}
}
