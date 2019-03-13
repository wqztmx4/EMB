package com.jsl.emb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.jsl.emb.bean.Enterprise;
import com.jsl.emb.bean.Info;
import com.jsl.emb.dao.InfoDAO;

public class InfoPublicServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	InfoDAO infoDAO = new InfoDAO();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			req.setCharacterEncoding("UTF-8");     //post提交，中文乱码转换
			//这里需要转换编码格式，否则会出现json中文乱码
			resp.setContentType("application/json;charset=utf-8");
			resp.setCharacterEncoding("utf-8");
			
			
			/*
			 * 构造返回的PrintWriter
			 * */			
			PrintWriter out = resp.getWriter();
			out = resp.getWriter();
			
			String method = req.getParameter("method");
			if(method.equals("select")){
				//根据返回的对象构造前台的分页json数据结构
				out.println("{\"total\":"+select(req,resp).size()+",\"rows\":"+JSON.toJSONString(select(req,resp))+"}");
			}else if (method.equals("add")) {
				out.println("{\"changedNum\":"+add(req, resp)+"}");
			} else if (method.equals("edit")) {
				out.println("{\"changedNum\":"+edit(req, resp)+"}");
			} else if (method.equals("remove")) {
				out.println("{\"changedNum\":"+remove(req, resp)+"}");
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int remove(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		return 1;
	}

	private int edit(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		Info info = new Info();
		info.setId(Integer.parseInt(req.getParameter("id")));
		info.setPublicContent(req.getParameter("publicContent"));
		return infoDAO.edit(info);
	}

	private int add(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		Info info = new Info();
		info.setEnterpriseId(Integer.parseInt(req.getParameter("enterpriseId")));
		info.setPublicContent(req.getParameter("publicContent"));
		info.setPublicDate(new java.sql.Date(new java.util.Date().getTime()));
		return infoDAO.add(info);
	}

	private List<Info> select(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Map<String, String> map = new HashMap<String, String>();
		if(req.getParameter("id")!=null){
			map.put("id", req.getParameter("id"));
		}
		if(req.getParameter("enterpriseId")!=null){
			map.put("enterpriseId", req.getParameter("enterpriseId"));
		}
		if(req.getParameter("search_startDate")!=null){
			map.put("search_startDate", req.getParameter("search_startDate"));
		}
		if(req.getParameter("search_endDate")!=null){
			map.put("search_endDate", req.getParameter("search_endDate"));
		}
		try {
			return infoDAO.select(map);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		};
		return null;		
	}

}