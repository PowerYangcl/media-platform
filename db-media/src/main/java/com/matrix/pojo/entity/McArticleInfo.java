package com.matrix.pojo.entity;

import java.util.Date;

public class McArticleInfo {
    private Integer id;
    private String title;
    private String titlePic;
    private String source;
    private String author;
    private Integer authorId;
    private String editor;
    private Integer editorId;
    private String releaseType;
    private String releaser; // 发布人
    private Date releaseTime;
    private Integer articleTypeId;
    private Integer readerCount;
    private Integer thumbsUpCount;
    private Integer shareCount;
    private String topType;
    private String sourceLink;
    private Date createTime;
    private Date updateTime;
    private String htmlContent;
    private String toReleaseSource;  // 待发布文章的来源，依从于releaseType，标识的是该条记录上一个状态是什么：02已发布撤回的 03草稿箱提交的 04回收站还原的
    private String recycleBinSource;  	// recycle_bin_source  回收站文章来源 01新建的文章 02 被驳回的文章	
    private String remark;
    private String relProductInfoJson;//关联推荐商品json数据
    private String outUrl; // 原文链接 out_url
    
	public String getOutUrl() {
		return outUrl;
	}
	public void setOutUrl(String outUrl) {
		this.outUrl = outUrl;
	}
	public String getReleaser() {
		return releaser;
	}
	public void setReleaser(String releaser) {
		this.releaser = releaser;
	}
	public String getRecycleBinSource() {
		return recycleBinSource;
	}
	public void setRecycleBinSource(String recycleBinSource) {
		this.recycleBinSource = recycleBinSource;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitlePic() {
		return titlePic;
	}
	public void setTitlePic(String titlePic) {
		this.titlePic = titlePic;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Integer getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public Integer getEditorId() {
		return editorId;
	}
	public void setEditorId(Integer editorId) {
		this.editorId = editorId;
	}
	public String getReleaseType() {
		return releaseType;
	}
	public void setReleaseType(String releaseType) {
		this.releaseType = releaseType;
	}
	public Date getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}
	public Integer getArticleTypeId() {
		return articleTypeId;
	}
	public void setArticleTypeId(Integer articleTypeId) {
		this.articleTypeId = articleTypeId;
	}
	public Integer getReaderCount() {
		return readerCount;
	}
	public void setReaderCount(Integer readerCount) {
		this.readerCount = readerCount;
	}
	public Integer getThumbsUpCount() {
		return thumbsUpCount;
	}
	public void setThumbsUpCount(Integer thumbsUpCount) {
		this.thumbsUpCount = thumbsUpCount;
	}
	public Integer getShareCount() {
		return shareCount;
	}
	public void setShareCount(Integer shareCount) {
		this.shareCount = shareCount;
	}
	public String getTopType() {
		return topType;
	}
	public void setTopType(String topType) {
		this.topType = topType;
	}
	public String getSourceLink() {
		return sourceLink;
	}
	public void setSourceLink(String sourceLink) {
		this.sourceLink = sourceLink;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getHtmlContent() {
		return htmlContent;
	}
	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
	public String getToReleaseSource() {
		return toReleaseSource;
	}
	public void setToReleaseSource(String toReleaseSource) {
		this.toReleaseSource = toReleaseSource;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRelProductInfoJson() {
		return relProductInfoJson;
	}
	public void setRelProductInfoJson(String relProductInfoJson) {
		this.relProductInfoJson = relProductInfoJson;
	}
	
}