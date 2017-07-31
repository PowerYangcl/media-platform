package com.matrix.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.matrix.base.interfaces.IBaseService;
import com.matrix.pojo.entity.McArticleInfo;
import com.matrix.pojo.entity.McArticleRelease;

public interface IMcArticleInfoService extends IBaseService<McArticleInfo, Integer> {

	public JSONObject ajaxArticleList(McArticleInfo e, HttpServletRequest request, HttpSession session);

	public JSONObject ajaxArticleUpdate(McArticleInfo e, HttpServletRequest request, HttpSession session);

	public JSONObject ajaxArticleDelete(Integer id, HttpServletRequest request, HttpSession session);
	
	public JSONObject ajaxArticleAdd(McArticleInfo e, HttpServletRequest request, HttpSession session);

	public JSONObject ajaxUploadImage(HttpServletRequest request);
	
	public JSONObject upReleaseInfo (McArticleRelease entity);

}
