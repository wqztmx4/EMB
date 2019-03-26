package com.jsl.emb.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.alibaba.fastjson.JSON;
import com.jsl.emb.dao.InfoDAO;

public class TimedTask implements ServletContextListener{
	

	InfoDAO dao = new InfoDAO();
	
	
	
	public boolean outputTypeJsonFile(){
		try {
		Map<String, String> map = new HashMap<String, String>();
		//查询危化品使用企业发的信息
		map.put("enterpriseType", "101");
		String  jsonString101 = "{\"total\":"+dao.select(map).size()+",\"rows\":"+JSON.toJSONString(dao.select(map))+"}";
		//查询危化品经营企业发的信息
		map.put("enterpriseType", "102");
		String jsonString102 = "{\"total\":"+dao.select(map).size()+",\"rows\":"+JSON.toJSONString(dao.select(map))+"}";
		//查询危化品经营生产发的信息
		map.put("enterpriseType", "103");
		String jsonString103 = "{\"total\":"+dao.select(map).size()+",\"rows\":"+JSON.toJSONString(dao.select(map))+"}";
		//输出查询结果
		OutFile.createJsonFile(jsonString101, "\\data", "101");
		OutFile.createJsonFile(jsonString102, "\\data", "102");
		OutFile.createJsonFile(jsonString103, "\\data", "103");
		return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	
	public boolean outputCountriesJsonFile(){
		try {
		//查询所有县市区有多少企业未发当天信息
		String jsonString_Countries = JSON.toJSONString(dao.selectUnpublicEnterprise());
		//输出查询结果
		return OutFile.createJsonFile(jsonString_Countries, "\\data", "a");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Tomcat服务终止");
	}




	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Tomcat服务启动");
		//隔一天执行一次的定时器timer
		Timer timer = new Timer();
		//在每天的10点之前的每分钟执行一次的timer2
		Timer timer2 = new Timer();
		
		 // 时间类
	    Calendar startDate = Calendar.getInstance();
	    //设置开始执行的时间为 某年-某月-某月 00:00:00
	    startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE), 10, 1, 0);
	    // 1小时的毫秒设定
	    long timeInterval = 24 * 60 * 60 * 1000;
		//查询各县市区未发信息的单位，该定时器方法在每天的10：01执行一次，间隔一天
	/*	timer.schedule(new TimerTask() {
		  @Override
		  public void run() {
		       outputCountriesJsonFile();
		  }
		}, startDate.getTime(), timeInterval);*/

	}
	
	

}
