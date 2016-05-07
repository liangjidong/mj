package org.chudian.mj.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public abstract class BaseDaoService {

	//private Logger log = Logger.getLogger(BaseDaoService.class);
	private static final String DB_URL = "jdbc:mysql://localhost:3306/newmj";
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
	private static final String USERNAME = "root";
	private static final String PWD = "wangyu1991$$";
	@SuppressWarnings("finally")
	public Connection getConnection(){
		//log.info("begin getConnection");
		Connection conn = null;
		try {
			Class.forName(DRIVER_NAME);//指定连接类型
			conn = DriverManager.getConnection(DB_URL, USERNAME, PWD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		//	log.error(e.getStackTrace());
			e.getStackTrace();
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		//	log.error(e.getStackTrace());
			e.getStackTrace();
		}finally{
			//log.info("end getConnection");
			return conn;
		}
	}
	public void closeConnection(Connection conn){
		//log.info("begin closeConnection");
          if(conn != null){
        	  try {
				conn.close();
				conn = null;
			//	log.info("end closeConnection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			//	log.error(e.getStackTrace());
				e.printStackTrace();
			}
          }
	}
	public void closePreparedStatement(PreparedStatement stat){
		//log.info("begin closePreparedStatement");
		if(stat != null){
			try {
				stat.close();
				stat = null;
			//	log.info("end closePreparedStatement");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//log.error(e.getStackTrace());
				e.printStackTrace();
			}
		}
	}
	public void closeResultSet(ResultSet set){
		//log.info("begin closeResultSet");
		if(set != null){
			try {
				set.close();
				set = null;
				//log.info("end closeResultSet");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//log.error(e.getStackTrace());
				e.printStackTrace();
			}
		}
		
	}
	@SuppressWarnings("finally")
	public ResultSet executeQueryWithPreparedStatement(Connection conn,String sql,List<DBParameter> paras){
	//	log.info("begin executeQueryWithPreparedStatement");
		PreparedStatement stat = null;
		ResultSet results = null;
		try {
			stat = conn.prepareStatement(sql);
			for(DBParameter para : paras)	{
				setPreparedStatementParameter(stat,para);
			}
			results = stat.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			 e.getStackTrace();
		//	log.error(e.getStackTrace());
		}finally{
		//	log.info("end executeQueryWithPreparedStatement");
			return results;
		}
		
	}
	public boolean executeUpdateWithPreparedStatement(Connection conn,String sql,List<DBParameter> paras){
	//	log.info("begin executeUpdateWithPreparedStatement");
		PreparedStatement stat;
		try {
			stat = conn.prepareStatement(sql);
			for(DBParameter para : paras)	{
				setPreparedStatementParameter(stat,para);
			}
			int isSuccess = stat.executeUpdate();
			//如果返回行数大于0,修改成功，返回true
			return (isSuccess > 0? true : false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//log.error(e.getStackTrace());
			 e.getStackTrace();
		}
		//log.info("end executeUpdateWithPreparedStatement");
		return false;
	}
	/**
	 * 替换SQL中的参数
	 * @param stat
	 * @param para
	 */
	private void setPreparedStatementParameter(PreparedStatement stat,DBParameter para){
		 int position = para.getPosition();
		 Object value = para.getValue();
		 try{
		 switch(para.getDbType()){
		     case String : 
		    	 stat.setString(position, (String) value);
		    	 break;
		     case Int : 
		    	 stat.setInt(position, (int) value);
		    	 break;
		     case Long:
		    	 stat.setLong(position, (long) value);
		    	 break;
		     case Double:
		    	 stat.setDouble(position, (double) value);
		         break;
		     case Timestamp:
		    	 stat.setTimestamp(position, (Timestamp) value);
		    	 break;
		     default:
		    	 throw new UnsupportedOperationException("cannot get DbType");
		 }
		 } catch(SQLException e){
			// log.error(e.getStackTrace());
			 e.getStackTrace();
		 }
	}
}
