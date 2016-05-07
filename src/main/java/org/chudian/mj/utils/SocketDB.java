package org.chudian.mj.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketDB {

	static String sql = null ,sql2 = null,sql3 = null;
	static DBHelper db1 = null ,db2 = null, db3 = null;
	static ResultSet ret = null, ret2 = null, ret3 = null;

	public Map  getVideoUrl(String fileName){
		sql = "select * from picture where name=" +"'" + fileName + "'";
		System.out.println(sql);
		db1 = new DBHelper(sql);
		String pictureId = "";
		String videoId = "";
		String videoUrl = "";
		String webUrl = "";
		String mjProductId = "";
		Map result = new HashMap();
		try {
			ret = db1.pst.executeQuery();//执行语句，得到结果集
			while (ret.next()) {
				 pictureId = ret.getString(1);
				 videoId = ret.getString(3);
				 webUrl = ret.getString(13);

			}
			if(!videoId.equals("")){
				sql2 = "select * from video where id=" + videoId; 
				ret2 = db1.pst.executeQuery();//执行语句，得到结果集
				while (ret2.next()) {
					videoUrl = ret2.getString(6);
				}
			}
			if(!pictureId.equals("")){
				sql3 = "select * from mjproduct where picture_id=" + pictureId;
				System.out.println(sql3);
				db3 = new DBHelper(sql3);
				ret3 = db3.pst.executeQuery();
				while (ret3.next()){
					mjProductId = ret3.getString(1);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("videoId == " + videoId);
		System.out.println("webUrl == " + webUrl);
		System.out.println("mjProductId == " + mjProductId);
		result.put("webUrl", webUrl);
		result.put("videoUrl", videoUrl);
		result.put("mjProductId", mjProductId);
		db1.close();
		return result;
	}

	public static void main(String[] args) {
		SocketDB demo = new SocketDB();
	}

}

