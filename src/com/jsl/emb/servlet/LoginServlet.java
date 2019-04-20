package com.jsl.emb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jsl.emb.bean.Enterprise;
import com.jsl.emb.util.DBConnection;

public class LoginServlet extends HttpServlet{
	
	Logger logger= Logger.getLogger(LoginServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//这里需要转换编码格式，否则会出现json中文乱码
		resp.setContentType("application/json;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		
		//接受参数
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		Connection conn =  DBConnection.getConnection();
		PreparedStatement preparedStatement;
		try {
			preparedStatement = conn.prepareStatement("select *  from Enterprise where username = ? and password = ?");
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Enterprise enterprise = new Enterprise();
				enterprise.setId(resultSet.getInt("id"));
				enterprise.setEnterpriseName(resultSet.getString("enterpriseName"));
				enterprise.setEnterpriseType(resultSet.getString("enterpriseType"));
				//enterprise.setUserName(resultSet.getString("userName"));
				enterprise.setAcceptor(resultSet.getString("acceptor"));
				enterprise.setCountries(resultSet.getInt("countries"));
				enterprise.setRole(resultSet.getInt("role"));
				PrintWriter out = resp.getWriter();
				out = resp.getWriter();
				out.println(JSON.toJSONString(enterprise));
			}
			resultSet.close();
			preparedStatement.close();
			conn.close();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}finally{
			
		}
		
		
	}
	
	

}
