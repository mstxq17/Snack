package com.yc.snack.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import com.google.gson.Gson;
import com.yc.snack.dao.GoodsInfoDao;
import com.yc.snack.util.FileUploadUtil;
import com.yc.snack.util.StringUtil;

@WebServlet("/goods") // 说明通过 项目名/admin 来访问
public class GoodsInfoController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户每次请求Servlet时,
	 */
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String op = request.getParameter("op"); 
		
		if ("upload".equals(op)) { //说明用户是要进行后台
			upload(request, response);
		}else if("addGood".equals(op)) {
            addGood(request, response);
        }else if("finds".equals(op)){
        	finds(request, response);
        }else if("findByPage".equals(op)) {
        	findByPage(request, response);
        }else if("findByGid".equals(op)) {
        	findByGid(request, response);
        }
	}
	
    /**
     * @param request
     * @param response
     * @throws IOException
     */
    private void findByPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String tid = request.getParameter("tid");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		
		if (StringUtil.checkNull(page)) {
			page = "1";
		}
		if (StringUtil.checkNull(rows)) {
			rows = "20";
		}
		
		GoodsInfoDao goodsInfoDao = new GoodsInfoDao();
		List<Map<String, String>> list = goodsInfoDao.findByPage(tid, Integer.parseInt(page), Integer.parseInt(rows));
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.print(gson.toJson(list));
		out.flush();
		
		
	}

	private void finds(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String tid = request.getParameter("tid");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		
		if (StringUtil.checkNull(page)) {
			page = "1";
		}
		if (StringUtil.checkNull(rows)) {
			rows = "20";
		}
		
		GoodsInfoDao goodsInfoDao = new GoodsInfoDao();
		int total = goodsInfoDao.total(tid);
		List<Map<String, String>> list = goodsInfoDao.findByPage(tid, Integer.parseInt(page), Integer.parseInt(rows));
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", total);
		result.put("rows", list);
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.print(gson.toJson(result));
		out.flush();	
	}

	/** 根据商品编号，查详细
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void findByGid(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String gid = request.getParameter("gid");
		Map<String, String> map = null;
		if (StringUtil.checkNull(gid)) {
			map = Collections.emptyMap();
		}else {
			GoodsInfoDao  goodsInfoDao = new GoodsInfoDao();
			map = goodsInfoDao.findByGid(gid);
		}
		
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.print(gson.toJson(map));
		out.flush();
	}
	
	

	private void addGood(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO Auto-generated method stub
        FileUploadUtil fuu = new FileUploadUtil();
        PageContext pageContext = JspFactory.getDefaultFactory().getPageContext(this, request, response,  null, true, 2048, true);
        int result = 0;
        Map<String, String> map;
        try {
            map = fuu.uploads(pageContext);
            GoodsInfoDao goodsInfoDao = new GoodsInfoDao();
            result = goodsInfoDao.add(map);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        
    }

	/**
	 * 处理上传
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void upload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		FileUploadUtil fuu = new FileUploadUtil();
		Map<String, Object> result = new HashMap<String, Object>();;
		try {
			PageContext pageContext = JspFactory.getDefaultFactory().getPageContext(this, request, response,  null, true, 2048, true);
			Map<String, String> map = fuu.upload(pageContext);
			result.put("fileName", map.get("fileName"));
			result.put("uploaded", 1);
			result.put("url",  "../../" + map.getOrDefault("upload", ""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.print(gson.toJson(result));
		out.flush();
		
	}

}
