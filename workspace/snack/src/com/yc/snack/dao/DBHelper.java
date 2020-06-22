package com.yc.snack.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper {
	private static final String DRIVERCLASSNAME = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/snack?useOldAliasMetadataBehavior=true&useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8";
	private static final String USER = "root";
	private static final String PASSEORD = "123456";
	
	//静态快，用来加载驱动
	static{
		try {
			Class.forName(DRIVERCLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
			
		}
	}

	/**
	 * 获取连接
	 */
	private  Connection getConnection(){
		Connection con = null;
		try {
			con=DriverManager.getConnection(URL, USER, PASSEORD);
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}
		return con;
	}

	/**
	 * 数据库连接池的方式获取连接
	 */
	//	public Connection getConnection(){
	//		//javax.naming.Context提供了查找JNDI 的接口
	//		try {
	//			Context ctx=new InitialContext();
	//			DataSource ds=(DataSource) ctx.lookup("java:comp/env/jdbc/news");
	//			con=ds.getConnection(); //与JNDI获取到的数据源建立连接
	//		} catch (NamingException e) {
	//			e.printStackTrace();
	//			
	//		} catch (SQLException e) {
	//			
	//			throw new RuntimeException(e);
	//		}
	//		return con;
	//	}

	/**
	 * 关闭的方法
	 */
	private void close(Connection con, PreparedStatement pstmt, ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				
				throw new RuntimeException(e);
			}
		}

		if(pstmt != null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				
				throw new RuntimeException(e);
			}
		}

		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
				
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 设置PreparedStatement对象的sql语句中的参数？
	 */
	private void setParams(PreparedStatement pstmt, List<Object> params){
		if(pstmt == null || params == null || params.isEmpty()) {
			return;
		}

		for(int i = 0, len = params.size(); i < len; i++){
			try {
				pstmt.setObject(i+1, params.get(i));
			} catch (SQLException e) {
				
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 设置PreparedStatement对象的sql语句中的参数？
	 */
	private void setParams(PreparedStatement pstmt, Object ... params){
		if(pstmt == null || params == null || params.length <= 0) {
			return;
		}

		for(int i = 0, len = params.length; i < len; i++){
			try {
				pstmt.setObject(i+1, params[i]);
			} catch (SQLException e) {
				
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 增删改
	 * @param sql：sql语句集合，里面可以加？
	 * @param params：表示?对应的参数值的集合
	 * @return int:返回的值。成功>0，失败<=0
	 */
	public int update(List<String> sql, List<List<Object>> params){
		Connection con = null;
		PreparedStatement pstmt = null;
		int result=0;
		try {
			con = getConnection();	
			con.setAutoCommit(false);  //事务处理
			for(int i = 0, len = sql.size(); i < len; i++){
				pstmt = con.prepareStatement(sql.get(i));  //预编译对象
				this.setParams(pstmt, params.get(i));    //设置参数
				result = pstmt.executeUpdate();
			}
			con.commit(); //没有错处执行
		} catch (SQLException e) {
			
			try {
				con.rollback();  //出错回滚
			} catch (SQLException e1) {
				
				throw new RuntimeException(e);
			}
			throw new RuntimeException(e);
		}finally{
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				
				throw new RuntimeException(e);
			}
			this.close(con, pstmt, null);
		}
		return result;
	}

	/**
	 * 单表增删改
	 * @param sql：sql语句集合，里面可以加？
	 * @param params：表示?对应的参数值的集合
	 * @return int:返回的值。成功>0，失败<=0
	 */
	public int update(String sql, List<Object> params){
		Connection con = null;
		PreparedStatement pstmt = null;
		int result=0;
		try {
			con = this.getConnection();	
			pstmt = con.prepareStatement(sql);  //预编译对象
			this.setParams(pstmt, params);    //设置参数
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, null);
		}
		return result;
	}

	/**
	 * 单表增删改
	 * @param sql：sql语句集合，里面可以加？
	 * @param params：表示?对应的参数值的集合
	 * @return int:返回的值。成功>0，失败<=0
	 */
	public int update(String sql, Object ... params){
		Connection con = null;
		PreparedStatement pstmt = null;
		int result=0;
		try {
			con = this.getConnection();	
			pstmt = con.prepareStatement(sql);  //预编译对象
			this.setParams(pstmt, params);    //设置参数
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, null);
		}
		return result;
	}

	/**
	 * 获取结果集中所有列的类名
	 * @param rs 结果集对象
	 * @return 
	 * @throws SQLException 
	 */
	private String[] getColumnNames(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData(); // 获取结果集中的元数据
		int colCount = rsmd.getColumnCount(); // 获取结果集中列的数量
		String[] colNames = new String[colCount];
		for (int i = 1; i <= colCount; i ++) { // 循环获取结果集中列的名字
			colNames[i - 1] = rsmd.getColumnName(i).toLowerCase(); // 将列名改成小写后存到数组中
		}
		return colNames;
	}

	/**
	 * 查询
	 * @param sql 要执行的查询语句
	 * @param params 要执行的sql语句中对应占位符?的值，即按照?的顺序给定的值
	 * @return 满足条件的数据 每一条数据存到一个map中以列名为键，以对应列的值位置，然后将每一条数据即map对象存到list中
	 */
	public List<Map<String, String>> finds(String sql, Object ... params) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection(); // 获取连接
			pstmt = con.prepareStatement(sql); // 预编译语句
			this.setParams(pstmt, params); // 给预编译语句中的占位符赋值
			rs = pstmt.executeQuery(); // 执行查询
			Map<String, String> map = null;

			// 如果获取结果集中列的类名 -> 取到列名后我们存到一个数组中，便于后面的循环取值 -> 如何确定数组的大小?
			String[] colNames = this.getColumnNames(rs);
			while(rs.next()) { // 每次循环得到一行数据
				map = new HashMap<String, String>();

				// 循环获取每一列的值，循环所有的列名，根据列名获取当前这一行这一列的值
				for (String colName : colNames) {
					map.put(colName, rs.getString(colName));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, rs);
		}
		return list;
	}

	public List<Map<String, String>> finds(String sql, List<Object> params) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection(); // 获取连接
			pstmt = con.prepareStatement(sql); // 预编译语句
			this.setParams(pstmt, params); // 给预编译语句中的占位符赋值
			rs = pstmt.executeQuery(); // 执行查询
			Map<String, String> map = null;

			// 如果获取结果集中列的类名 -> 取到列名后我们存到一个数组中，便于后面的循环取值 -> 如何确定数组的大小?
			String[] colNames = this.getColumnNames(rs);
			while(rs.next()) { // 每次循环得到一行数据
				map = new HashMap<String, String>();

				// 循环获取每一列的值，循环所有的列名，根据列名获取当前这一行这一列的值
				for (String colName : colNames) {
					map.put(colName, rs.getString(colName));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, rs);
		}
		return list;
	}

	/**
	 * 查询单行
	 * @param sql 要执行的查询语句
	 * @param params 要执行的sql语句中对应占位符?的值，即按照?的顺序给定的值
	 * @return 满足条件的数据 每一条数据存到一个map中以列名为键，以对应列的值位置，然后将每一条数据即map对象存到list中
	 */
	public Map<String, String> find(String sql, Object ... params) {
		Map<String, String> map = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = this.getConnection(); // 获取连接
			pstmt = con.prepareStatement(sql); // 预编译语句
			this.setParams(pstmt, params); // 给预编译语句中的占位符赋值
			rs = pstmt.executeQuery(); // 执行查询

			// 如果获取结果集中列的类名 -> 取到列名后我们存到一个数组中，便于后面的循环取值 -> 如何确定数组的大小?
			String[] colNames = this.getColumnNames(rs);
			if(rs.next()) { // 每次循环得到一行数据
				map = new HashMap<String, String>();

				// 循环获取每一列的值，循环所有的列名，根据列名获取当前这一行这一列的值
				for (String colName : colNames) {
					map.put(colName, rs.getString(colName));
				}
			}
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, rs);
		}
		return map;
	}

	public Map<String, String> find(String sql, List<Object> params) {
		Map<String, String> map = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection(); // 获取连接
			pstmt = con.prepareStatement(sql); // 预编译语句
			this.setParams(pstmt, params); // 给预编译语句中的占位符赋值
			rs = pstmt.executeQuery(); // 执行查询

			// 如果获取结果集中列的类名 -> 取到列名后我们存到一个数组中，便于后面的循环取值 -> 如何确定数组的大小?
			String[] colNames = this.getColumnNames(rs);
			if(rs.next()) { // 每次循环得到一行数据
				map = new HashMap<String, String>();

				// 循环获取每一列的值，循环所有的列名，根据列名获取当前这一行这一列的值
				for (String colName : colNames) {
					map.put(colName, rs.getString(colName));
				}
			}
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, rs);
		}
		return map;
	}

	/**
	 * 获取总记录数的方法  一行一列
	 * @param sql 要执行的查询语句
	 * @param params 要执行的sql语句中对应占位符?的值，即按照?的顺序给定的值
	 * @return 总记录数
	 */
	public int total(String sql, Object ... params) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection(); // 获取连接
			pstmt = con.prepareStatement(sql); // 预编译语句
			this.setParams(pstmt, params); // 给预编译语句中的占位符赋值
			rs = pstmt.executeQuery(); // 执行查询
			if(rs.next()) { // 每次循环得到一行数据
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, rs);
		}
		return result;
	}

	public int total(String sql, List<Object> params) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection(); // 获取连接
			pstmt = con.prepareStatement(sql); // 预编译语句
			this.setParams(pstmt, params); // 给预编译语句中的占位符赋值
			rs = pstmt.executeQuery(); // 执行查询
			if(rs.next()) { // 每次循环得到一行数据
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, rs);
		}
		return result;
	}
}
