package com.matrix.pojo.view;

import com.matrix.pojo.entity.McArticleInfo;

public class McArticleInfoView extends McArticleInfo{
    // 非实体类字段
    private String assort; // 文章分类名称  

	public String getAssort() {
		return assort;
	}
	public void setAssort(String assort) {
		this.assort = assort;
	}
	
}
