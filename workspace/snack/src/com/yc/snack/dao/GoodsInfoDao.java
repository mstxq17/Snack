package com.yc.snack.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yc.snack.util.StringUtil;

/**
 * 商品类型
 * 
 * @author xq17
 *
 */
public class GoodsInfoDao {

	boolean debug = true;
	
	public int add(Map<String, String> map) {
		DBHelper db  = new DBHelper();
		String sql = "insert into goodsinfo values(0,?,?,?,?,?,?,?,?,?,?,1)";
		if(debug) {
			System.out.println(sql);
			System.out.println(map);
		}
		return db.update(sql, map.get("gname"), map.get("tid"), map.get("price"), map.get("intro")
				,map.get("balance"), map.get("pic"), map.get("unit"), map.get("qperied"), 
				map.get("weight"), map.get("descr"));
	}
	
	
	/**
	 * 分页查询
	 * @param tid
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, String>>  findByPage(String tid, int page, int rows){
		DBHelper db  = new DBHelper();
		String sql = "select gid, gname, price, weight, pics from goodsinfo where status !=0";
		if(debug) {
			System.out.println(sql);
		}
		List<Object> params = new ArrayList<Object>();
		
		if(!StringUtil.checkNull(tid)) {
			sql += " and tid = ?";
			params.add(tid);
		}
		sql += " order by gid desc limit ?,?";
		params.add((page-1)*rows);
		params.add(rows);
		return db.finds(sql, params);
	}
	
	/**
	 * 根据商品编号，查询商品详细
	 * @param gid
	 * @return
	 */
	public Map<String, String> findByGid(String gid){
		DBHelper db  = new DBHelper();
		String sql = "select gid, g.tid, tname, gname, price, weight, intro, pics, balance, unit, qperied,"
				+ "descr from goodsinfo g, goodstype t where g.tid = t.tid and gid = ?";
		if(debug) {
			System.out.println(sql);
		}
		return db.find(sql, gid);
	}
	
	/**
	 * 满足条件的总记录数
	 * @param tid
	 * @return
	 */
	public int total(String tid) {
		DBHelper db  = new DBHelper();
		String sql = "select count(gid) from goodsinfo where status != 0 ";
		if(debug) {
			System.out.println(sql);
		}
		List<Object> params = new ArrayList<Object>();
		if(!StringUtil.checkNull(tid)) {
			sql +=  " and tid = ?";
			params.add(tid);
		}
		return  db.total(sql, params);
	}
	
	
}
