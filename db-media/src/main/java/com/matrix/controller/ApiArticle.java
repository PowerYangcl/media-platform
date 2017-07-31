package com.matrix.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.matrix.pojo.entity.McArticleType;
import com.matrix.service.IMcArticleTypeService;

/**
 * 已发布文章列表页接口数据
 * 接口都以api 开头  eg:api_article_list_data_page
 * @author fq
 *
 */
@Controller
@RequestMapping("api")
public class ApiArticle {          //  extends BaseApi
	private static Logger logger = Logger.getLogger(MediaCenterController.class);

	@Autowired
	private IMcArticleTypeService mcArticleTypeService;
	
	
	/**
	 * 文章列表页数据接口  
	 * @param e
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "api_article_list_data_page", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajax_article_list_data_page(McArticleType e , HttpServletRequest request , HttpSession session) {
//		super.userBehavior(session , logger , "ajax_edit_assort" , "更新分类记录");   
		return mcArticleTypeService.ajaxReleasedArticleInfo();  
	} 
	
}
