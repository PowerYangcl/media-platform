package com.matrix.pojo.cache;

public class CatchReleasedArticleInfo {
	
	private Integer articleId ;

	private String title;//标题
	
	private String titlePic;//标题图
	
	private String source;  // 文章来源
	
	private String author; // 作者
	
	private Integer thumbsUpCount;//点赞数量
	
	private Integer readerCount;//阅读数量
	
	private String detailLik ;//详情链接

	public String getTitle() {
		return title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getThumbsUpCount() {
		return thumbsUpCount;
	}

	public void setThumbsUpCount(Integer thumbsUpCount) {
		this.thumbsUpCount = thumbsUpCount;
	}

	public Integer getReaderCount() {
		return readerCount;
	}

	public void setReaderCount(Integer readerCount) {
		this.readerCount = readerCount;
	}

	public String getDetailLik() {
		return detailLik;
	}

	public void setDetailLik(String detailLik) {
		this.detailLik = detailLik;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}
	
	
	
}
