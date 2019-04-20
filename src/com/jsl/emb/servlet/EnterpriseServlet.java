package com.jsl.emb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jsl.emb.bean.Enterprise;
import com.jsl.emb.dao.EnterpriseDAO;
import com.jsl.emb.listener.MyServletContextListener;

public class EnterpriseServlet extends HttpServlet {
	
	Logger logger= Logger.getLogger(EnterpriseServlet.class);
	
	EnterpriseDAO dao = new EnterpriseDAO();

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
			} else if (method.equals("changePassword")) {
				out.println("{\"changedNum\":"+changePassword(req, resp)+"}");
			} else {

			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}

	
	
	private int changePassword(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		Enterprise enterprise = new Enterprise();
		enterprise.setId(Integer.parseInt(req.getParameter("enterpriseId")));
		enterprise.setPassword(req.getParameter("oldPassword"));
		String newPassword = req.getParameter("newPassword");
		return dao.changePassword(enterprise,newPassword);
	}

	private List<Enterprise> select(HttpServletRequest req, HttpServletResponse resp) {
		
		Map<String, String> map = new HashMap<String, String>();
		if(req.getParameter("id")!=null){
			map.put("id", req.getParameter("id"));
		}
		if(req.getParameter("search_enterpriseType")!=null&&!req.getParameter("search_enterpriseType").equals("")){
			map.put("enterpriseType", req.getParameter("search_enterpriseType"));
		}
		if(req.getParameter("search_countries")!=null&&!req.getParameter("search_countries").equals("")){
			map.put("countries", req.getParameter("search_countries"));
		}
		if(req.getParameter("search_enterpriseName")!=null&&!req.getParameter("search_enterpriseName").equals("")){
			map.put("enterpriseName", req.getParameter("search_enterpriseName"));
		}
		if(req.getParameter("search_enterpriseStatus")!=null&&!req.getParameter("search_enterpriseStatus").equals("")){
			map.put("enterpriseStatus", req.getParameter("search_enterpriseStatus"));
		}
		try {
			return dao.select(map);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		};
		return null;		
	}

	private int remove(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		String ids = req.getParameter("remove_ids");
		return dao.remove(ids);

	}

	
	
	
	private int edit(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		Enterprise enterprise = new Enterprise();
		enterprise.setId(Integer.parseInt(req.getParameter("id")));
		enterprise.setEnterpriseName(req.getParameter("enterpriseName"));
		enterprise.setEnterpriseType(req.getParameter("enterpriseType"));
		enterprise.setAcceptor(req.getParameter("acceptor"));
		enterprise.setUserName(req.getParameter("userName"));
		enterprise.setPassword(req.getParameter("password"));
		enterprise.setCountries(Integer.parseInt(req.getParameter("countries")));
		enterprise.setEnterpriseStatus(Integer.parseInt(req.getParameter("enterpriseStatus")));
		return dao.edit(enterprise);
	}

	/**
	 * 添加企业信息
	 * 
	 * @param req
	 * @param resp
	 * @throws SQLException
	 */
	private int add(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException {
		Enterprise enterprise = new Enterprise();
		enterprise.setEnterpriseName(req.getParameter("enterpriseName"));
		enterprise.setEnterpriseType(req.getParameter("enterpriseType"));
		enterprise.setAcceptor(req.getParameter("acceptor"));
		enterprise.setUserName(req.getParameter("userName"));
		enterprise.setPassword(req.getParameter("password"));
		enterprise.setCountries(Integer.parseInt(req.getParameter("countries")));
		enterprise.setEnterpriseStatus(Integer.parseInt(req.getParameter("enterpriseStatus")));
		return dao.add(enterprise);
	}

}
