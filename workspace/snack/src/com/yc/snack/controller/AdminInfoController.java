package com.yc.snack.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mysql.cj.Session;
import com.yc.snack.dao.AdminInfoDao;

@WebServlet("/admin") // 说明通过 项目名/admin 来访问
public class AdminInfoController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	boolean debug = true;

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
		
		if ("login".equals(op)) { //说明用户是要进行后台
			login(request, response);
		} else if("checkLogin".equals(op)) {
			checkLogin(request, response);
		}
	}

	private void checkLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		HttpSession httpSession = request.getSession();
		
		Object obj = httpSession.getAttribute("currentLoginAdmin");
		PrintWriter out = response.getWriter();
		
		if(debug) {
			System.out.println("ok");
		}
		Gson gson = new GsonBuilder().serializeNulls().create();
		out.print(gson.toJson(obj));
		out.flush();	
	}

	/**
	 * 处理管理员登录方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String account = request.getParameter("account");
		String pwd = request.getParameter("pwd");
		
		// 调用DAO层校验用户输入的账号、密码是否正确
		AdminInfoDao adminInfoDao = new AdminInfoDao();
		Map<String, String> map = adminInfoDao.login(account, pwd);
		
		PrintWriter out = response.getWriter();
		if (map == null) {
			out.print("0");
		}else {
			// 说明
			request.getSession().setAttribute("currentLoginAdmin", map);
			out.print("1");
		}
	}

}
