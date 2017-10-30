package com.matrix.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matrix.base.BaseServiceImpl;
import com.matrix.cache.CacheLaunch;
import com.matrix.cache.enums.SCacheEnum;
import com.matrix.cache.inf.IBaseLaunch;
import com.matrix.cache.inf.ICacheFactory;
import com.matrix.dao.IMcArticleInfoDao;
import com.matrix.dao.IMcArticleTypeDao;
import com.matrix.dict.LoadCacheReleasedArticleInfo;
import com.matrix.pojo.entity.McArticleInfo;
import com.matrix.pojo.entity.McArticleType;
import com.matrix.pojo.view.McUserInfoView;
import com.matrix.service.IMcArticleTypeService;

@Service("mcArticleTypeService")
public class McArticleTypeServiceImpl extends BaseServiceImpl<McArticleType, Integer> implements IMcArticleTypeService {
	
	private IBaseLaunch<ICacheFactory> launch = CacheLaunch.getInstance().Launch();
	
	@Resource
	private IMcArticleInfoDao mcArticleInfoDao;
	
	@Resource
	private IMcArticleTypeDao mcArticleTypeDao;

	/**
	 * @description: 返回页面格式数据，同时 格式化时间 
	 * 
	 * @param e
	 * @param request
	 * @param session
	 * @author Yangcl 
	 * @date 2017年6月16日 下午3:15:12 
	 * @version 1.0.0.1
	 */
	@Override
	public JSONObject ajaxArticleAssortList(McArticleType e , HttpServletRequest request, HttpSession session) {
		return super.ajaxPageData(e, request);
	}

	
	/**
	 * @description: 添加一条记录 
	 * 
	 * @param e
	 * @param request
	 * @param session
	 * @author Yangcl 
	 * @date 2017年6月21日 下午3:17:53 
	 * @version 1.0.0.1
	 */
	public JSONObject ajaxAddAssort(McArticleType e, HttpServletRequest request, HttpSession session) {
		JSONObject result = new JSONObject();
		if(StringUtils.isBlank(e.getName())){
			result.put("status", "error");
			result.put("msg", this.getInfo(700010002));  // 分类名称不得为空! 
			return result;
		}
		
		McUserInfoView userInfo = (McUserInfoView) session.getAttribute("userInfo");
		e.setCreateTime(new Date());
		e.setUpdateTime(new Date());
		e.setCreateUserId(userInfo.getId());
		e.setUpdateUserId(userInfo.getId()); 
		int flag = mcArticleTypeDao.insertSelective(e);
		if(flag == 1){
			result.put("status", "success");
			result.put("msg", this.getInfo(700010003));  // 数据添加成功!
		}else{
			result.put("status", "error");
			result.put("msg", this.getInfo(700010004));  // 添加数据失败!
		}
		return result;
	}


	/**
	 * @description: 更新一条记录
	 * 
	 * @param e
	 * @param request
	 * @param session
	 * @author Yangcl 
	 * @date 2017年6月21日 下午5:54:53 
	 * @version 1.0.0.1
	 */
	public JSONObject ajaxEditAssort(McArticleType e, HttpServletRequest request, HttpSession session) {
		JSONObject result = new JSONObject();
		if(StringUtils.isBlank(e.getName())){
			result.put("status", "error");
			result.put("msg", this.getInfo(700010002));  // 分类名称不得为空! 
			return result;
		}
		
		McUserInfoView userInfo = (McUserInfoView) session.getAttribute("userInfo");
		e.setUpdateTime(new Date());
		e.setUpdateUserId(userInfo.getId()); 
		int flag = mcArticleTypeDao.updateSelective(e);
		if(flag == 1){
			result.put("status", "success");
			result.put("msg", this.getInfo(700010005));  // 数据更新成功!
		}else{
			result.put("status", "error");
			result.put("msg", this.getInfo(700010006));  //更新数据失败!
		}
		return result;
	}

	/**
	 * @description: 异步删除一条数据 
	 * 
	 * @param e
	 * @param request
	 * @param session
	 * @author Yangcl 
	 * @date 2017年6月21日 下午6:08:05 
	 * @version 1.0.0.1
	 */
	public JSONObject ajaxDeleteAssort(McArticleType e, HttpServletRequest request, HttpSession session) {
		JSONObject result = new JSONObject();
		int flag = mcArticleTypeDao.deleteById(e.getId());
		if(flag == 1){
			result.put("status", "success");
			result.put("msg", this.getInfo(700010007));  // 数据删除成功!
		}else{
			result.put("status", "error");
			result.put("msg", this.getInfo(700010008));  // 删除数据失败!  
		}
		return result;
	}

	/**
	 * 获取类型列表
	 * @author fq
	 */
	public List<McArticleType> findList(McArticleType mcArticleType) {
		return mcArticleTypeDao.findList(mcArticleType);
	}
	
	/**
	 * 获取已发布文章列表数据，从缓存中获取。
	 * （缓存中只存储：文章标题、标题图片、阅读数、点赞数、作者  ）
	 * @return
	 */
	public JSONObject ajaxReleasedArticleInfo() {
		
		JSONObject result = new JSONObject();
		if(!launch.loadServiceCache(SCacheEnum.ArticleListPage).exists(LoadCacheReleasedArticleInfo.RELEASEDKEY)) {
			new LoadCacheReleasedArticleInfo().refresh();
		}
		String jsonStr = launch.loadServiceCache(SCacheEnum.ArticleListPage).get(LoadCacheReleasedArticleInfo.RELEASEDKEY);
		result.put("status", "success");
		result.put("data", jsonStr);
		return result;
		
	}
	
	
}



















