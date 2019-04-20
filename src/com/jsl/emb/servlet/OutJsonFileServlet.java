package com.jsl.emb.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jsl.emb.dao.InfoDAO;
import com.jsl.emb.util.OutFile;

public class OutJsonFileServlet extends HttpServlet{
	
	Logger logger= Logger.getLogger(OutJsonFileServlet.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	InfoDAO dao = new InfoDAO();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			outputJsonFile();
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		}catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	doGet(req, resp);
	}
	
	public void outputJsonFile() throws IllegalArgumentException, SQLException, IllegalAccessException, InvocationTargetException, InstantiationException{
		Map<String, String> map = new HashMap<String, String>();
		//查询危化品使用企业发的信息
		map.put("enterpriseType", "101");
		map.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		String jsonString101 = "{\"total\":"+dao.select(map).size()+",\"rows\":"+JSON.toJSONString(dao.select(map))+"}";
		//查询危化品经营企业发的信息
		map.put("enterpriseType", "102");
		map.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		String jsonString102 = "{\"total\":"+dao.select(map).size()+",\"rows\":"+JSON.toJSONString(dao.select(map))+"}";
		//查询危化品经营生产发的信息
		map.put("enterpriseType", "103");
		map.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		String jsonString103 = "{\"total\":"+dao.select(map).size()+",\"rows\":"+JSON.toJSONString(dao.select(map))+"}";
		//查询所有县市区有多少企业未发当天信息
		String jsonString_Countries = JSON.toJSONString(dao.selectUnpublicEnterprise());
		try {
			OutFile.createJsonFile(jsonString101, "\\data", "101");
			OutFile.createJsonFile(jsonString102, "\\data", "102");
			OutFile.createJsonFile(jsonString103, "\\data", "103");
			OutFile.createJsonFile(jsonString_Countries, "\\data", "a");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
       
	}
	

}
