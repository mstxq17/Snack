package com.yc.snack.dao;


import java.util.Map;

import org.junit.Test;


public class AdminInfoDao {
	public Map<String, String> login(String account, String pwd){
		DBHelper db = new DBHelper();
		String sql = "select aid, aname, tel from admininfo where (aname = ? or tel = ?) and pwd = md5(?)";
		return db.find(sql, account , account, pwd);
	}
	
	@Test
	public void test() {
		AdminInfoDao adminInfo = new AdminInfoDao();
		System.out.println( adminInfo.login("navy",  "123321"));
		
	}
}
