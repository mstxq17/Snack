package com.yc.snack.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yc.snack.dao.GoodsTypeDao;
import com.yc.snack.util.StringUtil;

@WebServlet("/type")
public class GoodsTypeController extends HttpServlet {
	
	boolean debug = true;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		super.service(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String op = request.getParameter("op");
		if ("add".equals(op)) { // 说明是添加类型
			add(request, response);
		} else if ("finds".equals(op)) { // 查询所有
			finds(request, response);
		} else if ("find".equals(op)) { // 查询可用的类型信息
			find(request, response);
		} else if ("update".equals(op)) { // 上架或下架类型
			update(request, response);
		}
	}

	private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String tid = request.getParameter("tid");
		String status = request.getParameter("status");
		if(debug) {
			System.out.println(tid);
			System.out.println(status);
		}
		int result = 0;
		if (StringUtil.checkNull(tid, status)) {
			result = -1;
		} else {
			GoodsTypeDao goodsTypeDao = new GoodsTypeDao();
			result = goodsTypeDao.update(tid, Integer.parseInt(status));
		}
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();

	}

	private void find(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		GoodsTypeDao goodsTypeDao = new GoodsTypeDao();
		List<Map<String, String>> list = goodsTypeDao.find();

		// 以json格式发送数据
		Gson gson = new GsonBuilder().serializeNulls().create();
		PrintWriter out = response.getWriter();
		out.print(gson.toJson(list));
		out.flush();

	}

	private void finds(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		GoodsTypeDao goodsTypeDao = new GoodsTypeDao();
		List<Map<String, String>> list = goodsTypeDao.finds();

		// 以json格式发送数据
		Gson gson = new GsonBuilder().serializeNulls().create();
		PrintWriter out = response.getWriter();
		out.print(gson.toJson(list));
		out.flush();

	}

	private void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		GoodsTypeDao goodsTypeDao = new GoodsTypeDao();
		String tname = request.getParameter("tname");
		
		int result = 0;
		if (StringUtil.checkNull(tname)) {
			result = -1;
		} else {
			result = goodsTypeDao.add(tname);
		}
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
	}

}