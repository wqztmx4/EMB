package com.jsl.emb.bean;

import java.util.ArrayList;
import java.util.Objects;

public class Enterprise {
	
	//企业ID
	private int   id;
	//企业名称
	private String enterpriseName;
	//企业类型
	private String enterpriseType;
	//承诺人
	private String acceptor;
	//登录用户名
	private String userName;
	//密码
	private String password;
	//企业所属县区
	private int    countries;
	//角色，暂时默认企业角色为9
	private int    role = 9;
	//企业排序
	private int e_order;
	//企业状态    
	//正常：0，临时停产：1，长期停产，2；
	private int enterpriseStatus;
	
	
	
	
	
	
	
	/**
	 * getter and setter
	 * @return
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getEnterpriseType() {
		return enterpriseType;
	}
	public void setEnterpriseType(String enterpriseType) {
		this.enterpriseType = enterpriseType;
	}
	public String getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getCountries() {
		return countries;
	}
	public void setCountries(int countries) {
		this.countries = countries;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getE_order() {
		return e_order;
	}
	public void setE_order(int e_order) {
		this.e_order = e_order;
	}
	public int getEnterpriseStatus() {
		return enterpriseStatus;
	}
	public void setEnterpriseStatus(int enterpriseStatus) {
		this.enterpriseStatus = enterpriseStatus;
	}




	/**
	 * 与info表关联
	 */
	private ArrayList<Info> infos;
	
	public ArrayList<Info> getInfos() {
		return infos;
	}
	public void setInfos(ArrayList<Info> infos) {
		this.infos = infos;
	}
	
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enterprise enterprise = (Enterprise) o;
        return userName == enterprise.userName &&
                Objects.equals(enterpriseName, enterprise.enterpriseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, enterpriseName);
    }




}
