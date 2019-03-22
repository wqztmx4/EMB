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

import com.alibaba.fastjson.JSON;
import com.jsl.emb.dao.InfoDAO;
import com.jsl.emb.util.OutFile;

public class OutJsonFileServlet extends HttpServlet{
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		String jsonString101 = "{\"total\":"+dao.select(map).size()+",\"rows\":"+JSON.toJSONString(dao.select(map))+"}";
		//查询危化品经营企业发的信息
		map.put("enterpriseType", "102");
		String jsonString102 = "{\"total\":"+dao.select(map).size()+",\"rows\":"+JSON.toJSONString(dao.select(map))+"}";
		//查询危化品经营生产发的信息
		map.put("enterpriseType", "103");
		String jsonString103 = "{\"total\":"+dao.select(map).size()+",\"rows\":"+JSON.toJSONString(dao.select(map))+"}";
		//查询所有县市区有多少企业未发当天信息
		String jsonString_Countries = JSON.toJSONString(dao.selectUnpublicEnterprise());
		try {
			OutFile.createJsonFile(jsonString101, "\\data", "101");
			OutFile.createJsonFile(jsonString102, "\\data", "102");
			OutFile.createJsonFile(jsonString103, "\\data", "103");
			OutFile.createJsonFile(jsonString_Countries, "\\data", "a");
		} catch (IOException e) {
			e.printStackTrace();
		}
       
	}
	

}
