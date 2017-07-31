package com.matrix.pojo.view;

import com.matrix.pojo.entity.McArticleType;

public class McArticleTypeView extends McArticleType{
	private Integer articleSum;
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getArticleSum() {
		return articleSum;
	}
	public void setArticleSum(Integer articleSum) {
		this.articleSum = articleSum;
	}
}
