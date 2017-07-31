package com.matrix.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.matrix.base.interfaces.IBaseService;
import com.matrix.pojo.entity.McArticleInfo;
import com.matrix.pojo.entity.McArticleType;

public interface IMcArticleTypeService extends IBaseService<McArticleType, Integer> {

	public JSONObject ajaxArticleAssortList(McArticleType e , HttpServletRequest request, HttpSession session);

	public JSONObject ajaxAddAssort(McArticleType e, HttpServletRequest request, HttpSession session);

	public JSONObject ajaxEditAssort(McArticleType e, HttpServletRequest request, HttpSession session);

	public JSONObject ajaxDeleteAssort(McArticleType e, HttpServletRequest request, HttpSession session);

	public JSONObject ajaxReleasedArticleInfo();

}
