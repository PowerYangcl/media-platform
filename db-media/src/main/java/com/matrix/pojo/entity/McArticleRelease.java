package com.matrix.pojo.entity;

import java.util.Date;

public class McArticleRelease {
    private Integer id;
    private Integer articleInfoId;
    private Integer flag;
    private String url;
    private Integer releaserId;
    private String releaser;
    private Date releaseTime;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getArticleInfoId() {
		return articleInfoId;
	}
	public void setArticleInfoId(Integer articleInfoId) {
		this.articleInfoId = articleInfoId;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getReleaserId() {
		return releaserId;
	}
	public void setReleaserId(Integer releaserId) {
		this.releaserId = releaserId;
	}
	public String getReleaser() {
		return releaser;
	}
	public void setReleaser(String releaser) {
		this.releaser = releaser;
	}
	public Date getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}
}