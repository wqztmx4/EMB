package com.jsl.emb.job;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.jsl.emb.dao.InfoDAO;
import com.jsl.emb.util.OutFile;

public class OutTypeJsonFileJob implements Job {
	


	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        System.out.println("method outTypeJsonFlie:  "+sdf.format(new Date()));
		try {
			InfoDAO dao = new InfoDAO();
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
	}

}
