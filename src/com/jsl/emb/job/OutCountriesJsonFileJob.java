package com.jsl.emb.job;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.jsl.emb.dao.InfoDAO;
import com.jsl.emb.util.OutFile;

public class OutCountriesJsonFileJob implements Job {
	
	Logger logger= Logger.getLogger(OutCountriesJsonFileJob.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        System.out.println("method outCountriesJson:  "+sdf.format(new Date()));
		try {
			InfoDAO dao = new InfoDAO();
			//查询所有县市区有多少企业未发当天信息
			String jsonString_Countries = "{\"total\":"+dao.selectUnpublicEnterprise().size()+",\"rows\":"+JSON.toJSONString(dao.selectUnpublicEnterprise())+"}";
			//输出查询结果
			OutFile.createJsonFile(jsonString_Countries, "\\data", "a");
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			} catch (IllegalArgumentException e1) {
				logger.error(e1.getMessage(),e1);
			} catch (IllegalAccessException e1) {
				logger.error(e1.getMessage(),e1);
			} catch (InvocationTargetException e1) {
				logger.error(e1.getMessage(),e1);
			} catch (InstantiationException e1) {
				logger.error(e1.getMessage(),e1);
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
			}
	}
	
	

}
