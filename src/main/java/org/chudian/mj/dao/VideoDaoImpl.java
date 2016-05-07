package org.chudian.mj.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.chudian.mj.utils.DBHelper;

public class VideoDaoImpl {
	//private Logger log = Logger.getLogger(VideoDaoImpl.class);
	private BaseDaoService mySqlService = null;
	private ResultSet results = null;
	private List<DBParameter> paras = null;
	private Connection conn = null;
	public VideoDaoImpl(){
		this.mySqlService = new MySQLDaoService();
		paras = new ArrayList<DBParameter>();
	}
	public Map  getVideoUrl(String fileName){
		conn = mySqlService.getConnection();
		//log.info("begin getVideoUrl -- fileName is " + fileName);
		String sql = "select * from picture where name=?";
		paras.add(new DBParameter(1,DBParaType.String,fileName));
		results = mySqlService.executeQueryWithPreparedStatement(conn, sql, paras);
		//执行完查询，清空参数集，为下一次使用准备
		paras.clear();
		
		String pictureId = "";
		String videoId = "";
		String videoUrl = "";
		String webUrl = "";
		String mjProductId = "";
		Map<String,String> result = new HashMap<String,String>();
		try {
			while (results.next()) {
				 pictureId = results.getString("id");
				 videoId = results.getString("video_id");
				 webUrl = results.getString("keepword1");
			}
			mySqlService.closeResultSet(results);
			if(!videoId.equals("")){
				videoUrl = this.getVideoUrlResult(videoId);
			}
			if(!pictureId.equals("")){
				mjProductId = getProductId(pictureId);
			}
			result.put("webUrl", webUrl);
			result.put("videoUrl", videoUrl);
			result.put("mjProductId", mjProductId);
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
	private String getVideoUrlResult(String videoId) throws SQLException{
		String videoUrl = "";
		String videoSql = "select final_url from video where id=?";
		paras.add(new DBParameter(1,DBParaType.Int,Integer.valueOf(videoId)));
		ResultSet resultVideo = mySqlService.executeQueryWithPreparedStatement(conn, videoSql, paras);
		paras.clear();
		while (resultVideo.next()) {
			videoUrl = resultVideo.getString("final_url");
		}
		mySqlService.closeResultSet(resultVideo);
		return videoUrl;
	}
	
	private String getProductId(String pictId) throws SQLException{
		String productId = "";
		String productSql = "select * from mjproduct where picture_id=?";
		paras.add(new DBParameter(1,DBParaType.Int,Integer.valueOf(pictId)));
		ResultSet resultProductId = mySqlService.executeQueryWithPreparedStatement(conn, productSql, paras);
		paras.clear();
		while (resultProductId.next()) {
			productId = resultProductId.getString(6);
		}
		mySqlService.closeResultSet(resultProductId);
		return productId;
	}
}
