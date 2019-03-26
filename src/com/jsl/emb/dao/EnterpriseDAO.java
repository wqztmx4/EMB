package com.jsl.emb.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jsl.emb.bean.Enterprise;
import com.jsl.emb.util.BeanUtil;
import com.jsl.emb.util.DBConnection;

public class EnterpriseDAO {
	
	
	/**
	 * 将企业信息插入数据库
	 * @param enterprise
	 * @return
	 * @throws SQLException
	 */
	public int add(Enterprise enterprise) throws SQLException{
		Connection connection = DBConnection.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("insert into enterprise(enterpriseName,enterpriseType,acceptor,userName,password,countries,role) VALUES(?,?,?,?,?,?,?)");
		preparedStatement.setString(1, enterprise.getEnterpriseName());
		preparedStatement.setString(2, enterprise.getEnterpriseType());
		preparedStatement.setString(3, enterprise.getAcceptor());
		preparedStatement.setString(4, enterprise.getUserName());
		preparedStatement.setString(5, enterprise.getPassword());
		preparedStatement.setInt(6, enterprise.getCountries());
		preparedStatement.setInt(7, enterprise.getRole());
		int insertNum = preparedStatement.executeUpdate();
		System.out.println(preparedStatement.toString());
		preparedStatement.close();
		connection.close();
		return insertNum;
	}
	
	
	
	/**
	 * 修改企业信息并插入数据库
	 * @param enterprise
	 * @return
	 * @throws SQLException
	 */
	public int edit(Enterprise enterprise) throws SQLException{
		Connection connection = DBConnection.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("update enterprise set enterpriseName = ?,enterpriseType = ?,acceptor = ?,userName = ?,password = ?,countries = ?,role = ? where id = ?");
		preparedStatement.setString(1, enterprise.getEnterpriseName());
		preparedStatement.setString(2, enterprise.getEnterpriseType());
		preparedStatement.setString(3, enterprise.getAcceptor());
		preparedStatement.setString(4, enterprise.getUserName());
		preparedStatement.setString(5, enterprise.getPassword());
		preparedStatement.setInt(6, enterprise.getCountries());
		preparedStatement.setInt(7, enterprise.getRole());
		preparedStatement.setInt(8, enterprise.getId());
		int insertNum = preparedStatement.executeUpdate();
		System.out.println(preparedStatement.toString());
		preparedStatement.close();
		connection.close();
		return insertNum;
	}
	
	
	
	/**
	 * 删除企业信息
	 * @param ids 需要删除的企业id
	 * @return
	 * @throws SQLException
	 */
	public int remove(String ids) throws SQLException{
		Connection connection = DBConnection.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("delete from enterprise where id in (?)");
		preparedStatement.setString(1, ids);
		int insertNum = preparedStatement.executeUpdate();
		preparedStatement.close();
		connection.close();
		return insertNum;
	}
	
	
	
	/**
	 * 根据map中的参数条件，查询enterprise表
	 * @param map
	 * @return
	 * @throws SQLException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public List<Enterprise> select(Map map) throws SQLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException{
		List<Enterprise> enterprises = new ArrayList<Enterprise>();
		String sql = "select * from enterprise where 1=1 and role=9 ";
		Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
		String pl_sql="";
		Map.Entry<String, String> entry = iterator.next();
		if(entry.getKey().equals("enterpriseName")){
			pl_sql = " and "+entry.getKey()+" like '%"+entry.getValue()+"%'";
		}else{
			pl_sql = " and "+entry.getKey()+"="+entry.getValue();
		}
		sql+=pl_sql;
		}
		System.out.println(sql);
		Connection connection = DBConnection.getConnection();
		PreparedStatement preparedStatement= connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			enterprises.add((Enterprise)BeanUtil.autoBean(Enterprise.class, resultSet));
		}
		preparedStatement.close();
		connection.close();
		return enterprises;
	}


	/**
	 * 根据对象中的信息判断，输入原密码是否正确，若正确，则根据新参数修改密码
	 * @param enterprise
	 * @param newPassword
	 * @return
	 * @throws SQLException 
	 */
	public int changePassword(Enterprise enterprise, String newPassword) throws SQLException{
		Connection connection = DBConnection.getConnection();
		String sql_select = "select password from enterprise where id = ?";
		PreparedStatement preparedStatement;
		preparedStatement = connection.prepareStatement(sql_select);
		preparedStatement.setInt(1, enterprise.getId());
		ResultSet resultSet  =  preparedStatement.executeQuery();
		while (resultSet.next()) {
			if(resultSet.getString("password").equals(enterprise.getPassword())){
				resultSet.close();
				preparedStatement.close();
				String sql_update = "update enterprise set password = ? where id = ?";
				PreparedStatement preparedStatement2 = connection.prepareStatement(sql_update);
				preparedStatement2.setString(1, newPassword);
				preparedStatement2.setInt(2, enterprise.getId());
				int num = preparedStatement2.executeUpdate();
				preparedStatement2.close();
				connection.close();
				return num;
			}
		}
		resultSet.close();
		preparedStatement.close();
		connection.close();
		return 0;
	}
	


}
