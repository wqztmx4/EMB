package com.jsl.emb.bean;

import java.sql.Date;

public class Info {

	//信息ID
	private int id;
	
	//发布时间
	private Date publicDate;
	
	//发布内容
	private String publicContent;
	
	//关联的企业ID
	private int enterpriseId;
	
	
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getPublicDate() {
		return publicDate;
	}

	public void setPublicDate(Date publicDate) {
		this.publicDate = publicDate;
	}

	public String getPublicContent() {
		return publicContent;
	}

	public void setPublicContent(String publicContent) {
		this.publicContent = publicContent;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
	

}
