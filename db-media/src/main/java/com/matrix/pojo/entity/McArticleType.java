package com.matrix.pojo.entity;

import java.util.Date;

public class McArticleType {
    private Integer id;
    private String name;
    private Date createTime;
    private Integer createUserId;
    private Date updateTime;
    private Integer updateUserId;
    private String exta;
    private String extb;
    private String extc;
    private String extd;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getExta() {
		return exta;
	}
	public void setExta(String exta) {
		this.exta = exta;
	}
	public String getExtb() {
		return extb;
	}
	public void setExtb(String extb) {
		this.extb = extb;
	}
	public String getExtc() {
		return extc;
	}
	public void setExtc(String extc) {
		this.extc = extc;
	}
	public String getExtd() {
		return extd;
	}
	public void setExtd(String extd) {
		this.extd = extd;
	}
}