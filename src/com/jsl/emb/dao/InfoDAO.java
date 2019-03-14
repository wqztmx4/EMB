package com.jsl.emb.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
		}else if(entry.getKey().equals("search_startDate")){
			pl_sql = " and "+entry.getKey()+" > "+entry.getValue();
		}else if(entry.getKey().equals("search_endDate")){
			pl_sql = " and "+entry.getKey()+" < "+entry.getValue();
		}else if(entry.getKey().equals("search_enterpriseName")){
			pl_sql = " and "+entry.getKey()+" like %"+entry.getValue()+"%";
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
	


}
