package com.yc.snack.dao;

import java.util.List;
import java.util.Map;

/**
 * 商品类型
 * 
 * @author xq17
 *
 */
public class GoodsTypeDao {

	/**
	 * 添加商品类型信息
	 * 
	 * @param tname
	 * @return
	 */
	boolean debug = true;
	
	public int add(String tname) {
		DBHelper db = new DBHelper();
		String sql = "insert into goodstype values(0, ?, 1)";
		if (debug) {
			System.out.println(sql);
		}
		return db.update(sql, tname);
	}

	/**
	 * 查询所有类型信息
	 * 
	 * @return
	 */
	public List<Map<String, String>> finds() {
		DBHelper db = new DBHelper();
		String sql = "select tid, tname, status from goodstype";
		if (debug) {
			System.out.println(sql);
		}
		return db.finds(sql);
	}

	/**
	 * 查询所有可用类型信息
	 * @return
	 */
	public List<Map<String, String >> find(){
		DBHelper db = new DBHelper();
		String sql = "select tid, tname from goodstype where status != 0";
		if (debug) {
			System.out.println(sql);
		}
		return db.finds(sql);
	}
	
	/**
	 *修改指定类型的状态
	 * @param tid 修改的类型编号
	 * @param status 要修改的状态
	 * @return 
	 */
	public int update(String tid, int status) {
		DBHelper db = new DBHelper();
		String sql = "update goodstype set status = ? where tid = ?";
		if (debug) {
			System.out.println(sql);
		}
		return db.update(sql, status, tid);
	}
}