package com.jsl.emb.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jsl.emb.bean.Enterprise;
import com.jsl.emb.bean.Info;
import com.jsl.emb.util.BeanUtil;
import com.jsl.emb.util.DBConnection;

public class InfoDAO {

	public int add(Info info) throws SQLException {
		Connection connection = DBConnection.getConnection();
		String sql = "insert into info(publicDate,publicContent,enterpriseId) VALUES(?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setDate(1, info.getPublicDate());
		preparedStatement.setString(2, info.getPublicContent());
		preparedStatement.setInt(3, info.getEnterpriseId());
		int num = preparedStatement.executeUpdate();
		preparedStatement.close();
		connection.close();
		return num;
	}

	public List<Info> select(Map<String, String> map) throws SQLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		List<Info> infos = new ArrayList<Info>();
		Connection connection = DBConnection.getConnection();
		String sql = "select * from info a , enterprise b where 1=1 and a.enterpriseId = b.id ";
		Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
		String pl_sql="";
		Map.Entry<String, String> entry = iterator.next();
		if (entry.getKey().equals("id")) {
			pl_sql = " and a."+entry.getKey()+" = "+entry.getValue();
		}else if(entry.getKey().equals("startDate")){
			pl_sql = " and publicDate >= '"+entry.getValue()+"'";
		}else if(entry.getKey().equals("endDate")){
			pl_sql = " and publicDate <= '"+entry.getValue()+"'";
		}else if(entry.getKey().equals("enterpriseName")){
			pl_sql = " and "+entry.getKey()+" like '%"+entry.getValue()+"%'";
		}else{
			pl_sql = " and "+entry.getKey()+"="+entry.getValue();
		}
		sql+=pl_sql;
		}
		System.out.println(sql);
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Enterprise enterprise = (Enterprise)BeanUtil.autoBean(Enterprise.class, resultSet);
			Info info = new Info();
			info.setId(resultSet.getInt("id"));
			info.setPublicDate(resultSet.getDate("publicDate"));
			info.setPublicContent(resultSet.getString("publicContent"));
			info.setEnterpriseId(resultSet.getInt("enterpriseId"));
			info.setEnterprise(enterprise);
			//infos.add((Info)BeanUtil.autoBean(Info.class, resultSet));
			infos.add(info);
		}
		preparedStatement.close();
		connection.close();
		return infos;
	}

	public int edit(Info info) throws SQLException {
		Connection connection = DBConnection.getConnection();
		String sql = "update info set publicContent = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, info.getPublicContent());
		int num = preparedStatement.executeUpdate();
		preparedStatement.close();
		connection.close();
		return num;
	}
	

	/**
	 *查询当天未发布信息的单位 
	 * 	
	 * @return
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public List<Enterprise> selectUnpublicEnterprise() throws SQLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		List<Enterprise> enterprises = new ArrayList<Enterprise>();
		Connection connection = DBConnection.getConnection();
		String sql = "select a.id,enterpriseName,countries from enterprise a left join  info b on a.id = b.enterpriseId and b.publicDate=? where b.publicContent is  null and countries is not null";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Enterprise enterprise = (Enterprise)BeanUtil.autoBean(Enterprise.class, resultSet);
			enterprises.add(enterprise);
		}
		preparedStatement.close();
		connection.close();
		return enterprises;
	}

}
