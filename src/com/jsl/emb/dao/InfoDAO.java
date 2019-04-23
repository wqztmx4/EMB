package com.jsl.emb.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sun.util.logging.resources.logging;

import com.jsl.emb.bean.Enterprise;
import com.jsl.emb.bean.Info;
import com.jsl.emb.util.BeanUtil;
import com.jsl.emb.util.CalendarUtil;
import com.jsl.emb.util.DBConnection;

public class InfoDAO {

	public int add(Info info) throws SQLException {
		Connection connection = DBConnection.getConnection();
		String sql_s = "select * from info where enterpriseId = ? and publicDate = ?";
		PreparedStatement statement = connection.prepareStatement(sql_s);
		statement.setInt(1, info.getEnterpriseId());
		statement.setDate(2, info.getPublicDate());
		ResultSet resultSet = statement.executeQuery();
		if(resultSet.next()) {
			resultSet.close();
			statement.close();
			connection.close();
			return 8001;
		}
		String sql_a = "insert into info(publicDate,publicContent,enterpriseId) VALUES(?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(sql_a);
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
		sql+=" order by publicDate desc";
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
		resultSet.close();
		preparedStatement.close();
		connection.close();
		return infos;
	}

	public int edit(Info info) throws SQLException {
		Connection connection = DBConnection.getConnection();
		String sql = "update info set publicContent = ? where id = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, info.getPublicContent());
		preparedStatement.setInt(2, info.getId());
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
		String sql = "select a.id, enterpriseName,countries ,enterpriseType from enterprise a where a.id  NOT IN(select enterpriseId from info where publicDate = ?)  and (enterpriseType=101 or enterpriseType=103)  and enterpriseStatus<>2";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		//preparedStatement.setString(1, "2019-4-21");
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Enterprise enterprise = (Enterprise)BeanUtil.autoBean(Enterprise.class, resultSet);
			enterprises.add(enterprise);
		}
		resultSet.close();
		preparedStatement.close();
		connection.close();
		return enterprises;
	}

	
	
	/**
	 * 根据查询的时间段，返回各县市区已发布和未发布的信息统计情况
	 * @param map
	 * @return
	 * @throws SQLException 
	 * @throws ParseException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
/*	public List<Info> statisticsByTime(Map<String, String> map) throws SQLException, ParseException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		List<Info> infos = new ArrayList<Info>();
		Connection connection = DBConnection.getConnection();
		String sql = "select a.enterpriseName ,a.countries,b.publicDate,b.publicContent from enterprise a LEFT JOIN info b on a.id = b.enterpriseId and publicDate= ?  WHERE  enterpriseStatus <>2  and (enterpriseType = 101 or enterpriseType = 103)  ";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = null;
		List<String> dates = CalendarUtil.findEveryDay(map.get("startDate"), map.get("endDate"));
		for (int i = 0; i < dates.size(); i++) {
			preparedStatement.setString(1, dates.get(i));
			//preparedStatement.setString(2, map.get("countries"));
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Enterprise enterprise = (Enterprise)BeanUtil.autoBean(Enterprise.class, resultSet);
				Info info = new Info();
				info.setId(resultSet.getInt("id"));
				info.setPublicDate((java.sql.Date) new SimpleDateFormat("yyyy-MM-dd").parse(dates.get(i)));
				info.setPublicContent(resultSet.getString("publicContent"));
				info.setEnterpriseId(resultSet.getInt("enterpriseId"));
				info.setEnterprise(enterprise);
				infos.add(info);
			}
		}
		resultSet.close();
		preparedStatement.close();
		connection.close();
		return infos;
	}*/
	
	
	
	/**
	 * 
	 * @param map 根据传入参数，查询某一天的未发布企业
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public List<Enterprise> queryStatisticsByDate(Map<String, String> map) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		List<Enterprise> enterprises = new ArrayList<Enterprise>();
		Connection connection = DBConnection.getConnection();
		String sql = "select a.id, enterpriseName,countries ,enterpriseType from enterprise a where a.id  NOT IN(select enterpriseId from info where publicDate = ?)  and (enterpriseType=101 or enterpriseType=103)  and enterpriseStatus<>2";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, map.get("startDate"));
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Enterprise enterprise = (Enterprise)BeanUtil.autoBean(Enterprise.class, resultSet);
			enterprises.add(enterprise);
		}
		resultSet.close();
		preparedStatement.close();
		connection.close();
		return enterprises;
	}
	
	
	
	
	
	/**
	 * 根据规则，查询连续两天未发或者一周三天没发信息的企业
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws ParseException
	 */
	public List<Enterprise> queryUnrecordByRole() throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, ParseException {
		List[] arrList = new List[7];
		List<Enterprise> enterprises = new ArrayList<Enterprise>();
		Connection connection = DBConnection.getConnection();
		String sql = "select a.id, enterpriseName,countries ,enterpriseType from enterprise a where a.id  NOT IN(select enterpriseId from info where publicDate = ?)  and (enterpriseType=101 or enterpriseType=103)  and enterpriseStatus<>2";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		List<String> dates = CalendarUtil.findEveryDay(new SimpleDateFormat("yyyy-MM-dd").format(CalendarUtil.getBeginDayOfLastWeek()), new SimpleDateFormat("yyyy-MM-dd").format(CalendarUtil.getEndDayOfLastWeek()));
		for (int i = 0; i < dates.size(); i++) {
			preparedStatement.setString(1, dates.get(i));
			ResultSet resultSet = preparedStatement.executeQuery();
			List<Enterprise> temp = new ArrayList<Enterprise>();
			while (resultSet.next()) {
				Enterprise enterprise = (Enterprise)BeanUtil.autoBean(Enterprise.class, resultSet);
				
				temp.add(enterprise);
				
				enterprises.add(enterprise);
			}
			arrList[i] = temp;
			resultSet.close();
		}
		
		
		List<Enterprise> repeatElements = new ArrayList<Enterprise>();
		
		//此处为查找连续两天未发的企业
		for (int i = 0; i < arrList.length-1; i++) {
			List<Enterprise> es = new ArrayList<Enterprise>();
			es.addAll(arrList[i]);
			es.addAll(arrList[i+1]);
			for (int j = 0; j < es.size()-1; j++) {
				for (int j2 = j+1; j2 < es.size(); j2++) {
					if(es.get(j).getEnterpriseName().equals(es.get(j2).getEnterpriseName())){
						if(!repeatElements.contains(es.get(j))){
							repeatElements.add(es.get(j));
						}
					}
				}
			}
		}
		
		//此处为查找一周内三天未发的企业。
		Map<Enterprise,Integer> map = new HashMap<Enterprise, Integer>();
		for (int i = 0; i < enterprises.size(); i++) {
			if(map.containsKey(enterprises.get(i))){
				map.put(enterprises.get(i),map.get(enterprises.get(i))+1);
			}else{
				map.put(enterprises.get(i),0);
			}
		}
		for (Map.Entry<Enterprise, Integer> entry : map.entrySet()) {
			if(entry.getValue()==3){
				if(!repeatElements.contains(entry.getKey())){
					repeatElements.add(entry.getKey());
				}
			}
		}
		
		
		preparedStatement.close();
		connection.close();
		return repeatElements;
	}

}
