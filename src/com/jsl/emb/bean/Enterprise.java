package com.jsl.emb.bean;

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
	
	
	

}
