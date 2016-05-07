package org.chudian.mj.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreeDModelDaoImpl{
	private BaseDaoService mySqlService = null;
	private ResultSet results = null;
	private List<DBParameter> paras = null;
	private Connection conn = null;
	public ThreeDModelDaoImpl(){
		this.mySqlService = new MySQLDaoService();
		paras = new ArrayList<DBParameter>();
	}
	public Map<String,String> getThreeDUrl(String fileName){
		conn = mySqlService.getConnection();
		Map<String,String> urlMaps = new HashMap<String,String>();
		String sql = "select * from picture where name=?";
		paras.add(new DBParameter(1,DBParaType.String,fileName));
		results = mySqlService.executeQueryWithPreparedStatement(conn, sql, paras);
		//执行完查询，清空参数集，为下一次使用准备
		paras.clear();
		int threeDId = 0;
		String pictureId = "";
		String threeDUrl = ""; 	
		String mjProductId = "";
		String webUrl = "";
		try {
			while(results != null && results.next()){
				threeDId = results.getInt("threed_id"); 
				webUrl = results.getString("keepword1");
				pictureId = results.getString("id");
			}
			mySqlService.closeResultSet(results);
			if(threeDId != 0){
				threeDUrl = getThreeDModelUrlResult(threeDId);
			}
			if(!pictureId.equals("")){
				mjProductId = getProductId(pictureId);
			}
			urlMaps.put("webUrl", webUrl);
			urlMaps.put("threeDUrl", threeDUrl); 
			urlMaps.put("mjProductId", mjProductId);
			System.out.println(threeDUrl);
			System.out.println("haha");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			paras.clear();
			mySqlService.closeConnection(conn);
		}
		return urlMaps;
	}
	private String getThreeDModelUrlResult(int threeDId) throws SQLException{
		String url = "";
		String threeDsql = "select url from three_d where id=?";
		paras.add(new DBParameter(1,DBParaType.Int,threeDId));
		ResultSet urlResults = mySqlService.executeQueryWithPreparedStatement(conn, threeDsql, paras);
		paras.clear();
		while(urlResults != null && urlResults.next()){
			url = urlResults.getString("url");
		}
		mySqlService.closeResultSet(urlResults);
		return url;
	}
	
	private String getProductId(String pictId) throws SQLException{
		String productId = "";
		String productSql = "select * from mjproduct where picture_id=?";
		paras.add(new DBParameter(1,DBParaType.Int,Integer.valueOf(pictId)));
		ResultSet resultProductId = mySqlService.executeQueryWithPreparedStatement(conn, productSql, paras);
		paras.clear();
		while (resultProductId!= null && resultProductId.next()) {
			productId = resultProductId.getString(1);
		}
		mySqlService.closeResultSet(resultProductId);
		return productId;
	}
	public static void main(String[] args) throws SQLException{
		ThreeDModelDaoImpl test=new ThreeDModelDaoImpl();
		Map<String,String> t=test.getThreeDUrl("2.jpg");
		System.out.println("a");
		System.out.println(t.get("threeDUrl"));
		System.out.println(t.get("mjProductId"));
	}
}
