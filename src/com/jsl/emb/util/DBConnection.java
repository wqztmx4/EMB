package com.jsl.emb.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.jsl.emb.servlet.EnterpriseServlet;

public class DBConnection {
	
	static Logger logger= Logger.getLogger(DBConnection.class);

	private static DataSource dataSource;
	
	
	static{
		System.out.println("into static area.....");
		Properties DBConfig = new Properties();
		try {
			DBConfig.load(DBConnection.class.getClassLoader().getResourceAsStream("/DBConfig.properties"));
			dataSource = DruidDataSourceFactory.createDataSource(DBConfig);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static DataSource getDataSource(){
		return dataSource;
	}
	
	
	public static Connection getConnection(){
			try {
				return dataSource.getConnection();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				System.out.println("datasource对象未被创建");
				return null;
			}
	}
}
