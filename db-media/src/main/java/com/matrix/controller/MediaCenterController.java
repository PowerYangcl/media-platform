package com.matrix.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.matrix.base.BaseController;
import com.matrix.map.MDataMap;
import com.matrix.pojo.entity.McArticleInfo;
import com.matrix.pojo.entity.McArticleRelease;
import com.matrix.pojo.entity.McArticleType;
import com.matrix.service.IMcArticleInfoService;
import com.matrix.service.IMcArticleTypeService;
import com.matrix.service.IPowerCacheService;
import com.matrix.support.ApiSupport;
import com.matrix.support.FileUploadSupport;
import com.matrix.support.ReleaseToCdnSupport;

/**
 * @description: 媒体库相关服务
 * 
 * @author Yangcl
 * @home https://github.com/PowerYangcl
 * @date 2017年6月15日 下午5:12:38
 * @version 1.0.0
 */

@Controller
@RequestMapping("media")
public class MediaCenterController extends BaseController {
	private static Logger logger = Logger.getLogger(MediaCenterController.class);

	@Autowired
	private IMcArticleTypeService mcArticleTypeService;
	
	@Autowired
	private IMcArticleInfoService mcArticleInfoService;
	
	@Autowired
	private IPowerCacheService powerApiService;

	/**
	 * @description: 已发布文章列表|已发布文章，所有人都可以见
	 * 
	 * @param session
	 * @author Yangcl
	 * @date 2017年6月16日 上午10:14:52
	 * @version 1.0.0.1
	 */
	@RequestMapping("page_media_released_list")
	public String mediaReleasedListPage(HttpSession session) {
		super.userBehavior(session, logger, "page_media_released_list", "已发布文章列表 mediaReleasedList.jsp");
		return "jsp/db-media/article/mediaReleasedList";
	}

	/**
	 * @description: 未发布文章列表| 未发布文章列表，保存的是编辑从草稿箱中提交的文章。只有主管可见，内容发布行为则由主管负责
	 * 
	 * @param session
	 * @author Yangcl
	 * @date 2017年6月16日 上午10:18:28
	 * @version 1.0.0.1
	 */
	@RequestMapping("page_media_unreleased_list")
	public String mediaUnreleasedListPage(HttpSession session) {
		super.userBehavior(session, logger, "page_media_unreleased_list", "未发布文章列表 mediaUnreleasedList.jsp");
		return "jsp/db-media/article/mediaUnreleasedList";
	}

	/**
	 * @description: 草稿箱列表| 保存了编辑尚未提交到未发布状态的文章，只有编辑人员可见，编辑人员可以看到自己的草稿，不能看到别人的草稿
	 * 
	 * @param session
	 * @author Yangcl
	 * @date 2017年6月16日 上午10:21:11
	 * @version 1.0.0.1
	 */
	@RequestMapping("page_media_drafts_list")
	public String mediaDraftsListPage(HttpSession session) {
		super.userBehavior(session, logger, "page_media_drafts_list", "草稿箱列表 mediaDraftsList.jsp");
		return "jsp/db-media/article/mediaDraftsList";
	}

	/**
	 * @description: 回收站列表| 回收站里是编辑人员从草稿箱中删除的文章，如果在回收站中删除了一片文章，则再也无法找回。
	 * 
	 * @param session
	 * @author Yangcl
	 * @date 2017年6月16日 上午10:25:01
	 * @version 1.0.0.1
	 */
	@RequestMapping("page_media_recycle_bin_list")
	public String mediaRecycleBinListPage(HttpSession session) {
		super.userBehavior(session, logger, "page_media_recycle_bin_list", "回收站列表 mediaRecycleBinList.jsp");
		return "jsp/db-media/article/mediaRecycleBinList";
	}
	
	
	/**
	 * @description: 文章分类管理|文章分类管理：海外购、生活电器、厨房用品等等。
	 * 
	 * @param session
	 * @author Yangcl
	 * @date 2017年6月16日 上午10:29:44
	 * @version 1.0.0.1
	 */
	@RequestMapping("page_media_article_assort_manage")
	public String mediaArticleAssortList(HttpSession session) {
		super.userBehavior(session, logger, "page_media_article_assort_manage", "已发布文章列表mediaArticleAssortList.jsp");
		return "jsp/db-media/assort/mediaArticleAssortList";
	}
	
	/**
	 * @description: 获取文章分类管理列表数据
	 * 
	 * @param session
	 * @author Yangcl 
	 * @date 2017年6月16日 下午3:01:44 
	 * @version 1.0.0.1
	 */
	@RequestMapping(value = "ajax_article_assort_list", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajaxArticleAssortList(McArticleType e , HttpServletRequest request , HttpSession session) {
		super.userBehavior(session , logger , "ajax_article_assort_list" , "获取文章分类管理列表数据");   
		return mcArticleTypeService.ajaxArticleAssortList(e , request , session); 
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
	@RequestMapping(value = "ajax_add_assort", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajaxAddAssort(McArticleType e , HttpServletRequest request , HttpSession session) {
		super.userBehavior(session , logger , "ajax_add_assort" , "添加分类记录");   
		return mcArticleTypeService.ajaxAddAssort(e , request , session); 
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
	@RequestMapping(value = "ajax_edit_assort", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajaxEditAssort(McArticleType e , HttpServletRequest request , HttpSession session) {
		super.userBehavior(session , logger , "ajax_edit_assort" , "更新分类记录");   
		return mcArticleTypeService.ajaxEditAssort(e , request , session);  
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
	@RequestMapping(value = "ajax_delete_assort", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajaxDeleteAssort(McArticleType e , HttpServletRequest request , HttpSession session) {
		super.userBehavior(session , logger , "ajax_delete_assort" , "删除分类记录");   
		return mcArticleTypeService.ajaxDeleteAssort(e , request , session);  
	}
	
	
	/**
	 * @description: 根据release_type，获取对应状态的分类列表
	 * 	release_type：发布状态， 01:未发布|02:已发布|03:草稿箱|04:回收站
	 * 
	 * @param e
	 * @param request
	 * @param session
	 * @author Yangcl 
	 * @date 2017年6月28日 下午3:08:44 
	 * @version 1.0.0.1
	 */
	@RequestMapping(value = "ajax_article_list", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajaxArticleList(McArticleInfo e , HttpServletRequest request , HttpSession session) {
		super.userBehavior(session , logger , "ajax_article_list" , "根据release_type获取对应状态的分类列表");   
		return mcArticleInfoService.ajaxArticleList(e , request , session); 
	}
	
	/**
	 * @description: 更新或编辑一篇文章
	 * 
	 * @param e
	 * @param request
	 * @param session
	 * @return
	 * @author Yangcl 
	 * @date 2017年6月29日 下午3:05:07 
	 * @version 1.0.0.1
	 */
	@RequestMapping(value = "ajax_article_update", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajaxArticleUpdate(McArticleInfo e , HttpServletRequest request , HttpSession session) {
		super.userBehavior(session , logger , "ajax_article_update" , "更新一篇文章");   
		return mcArticleInfoService.ajaxArticleUpdate(e , request , session);  
	}
	
	/**
	 * 根据商品编号获取商品信息
	 * 商品信息包括：商品编号，名称、价格、主图
	 * @param request
	 * @return
	 * @author fq
	 */
	@RequestMapping(value = "ajax_cfamily_product_info", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public MDataMap getHJYProductInfo ( HttpServletRequest request ,HttpSession session) {
		MDataMap result = null ;
		String product_code = request.getParameter("product_code");
		super.userBehavior(session, logger, "ajax_cfamily_product_info", "获取商品数据[productcode:"+product_code+"]");
		if(StringUtils.isNotBlank(product_code) ) {
			MDataMap cfamilyProductInfo = ApiSupport.create().getCfamilyProductInfo(product_code);
			if(null != cfamilyProductInfo) {
				result =  cfamilyProductInfo;
			}
			
		}
		return result;
	}
	
	
	/**
	 * @description: 彻底删除一篇文章
	 * 
	 * @param id
	 * @param request
	 * @param session
	 * @return
	 * @author Lizf 
	 * @date 2017年7月4日17:03:37
	 * @version 1.0.0.1
	 */
	@RequestMapping(value = "ajax_article_delete", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajaxArticleDelete(Integer id , HttpServletRequest request , HttpSession session) {
		super.userBehavior(session , logger , "ajax_article_delete" , "彻底删除一篇文章");   
		return mcArticleInfoService.ajaxArticleDelete(id , request , session);  
	}
	
	
	/**
	 * 跳转添加文章页面
	 * @param e
	 * @param request
	 * @return
	 * @author fq
	 */
	@RequestMapping("media_article_add_page")
	public String toArticleAddPage(McArticleInfo e , HttpServletRequest request , HttpSession session) {
		super.userBehavior(session , logger , "media_article_add_page" , "跳转添加文章页面");   
		return "jsp/db-media/article/mediaAddArticle";
	}
	
	/**
	 * 跳转编辑文章页面|来自草稿箱
	 * @param e
	 * @param request
	 * @return
	 * @author fq
	 */
	@RequestMapping("media_article_edit_page_drafts")
	public String toArticleEditDraftsPage(McArticleInfo e , HttpServletRequest request , HttpSession session) {
		super.userBehavior(session , logger , "media_article_edit_page" , "跳转编辑文章页面[id:"+e.getId()+"]");
		e = mcArticleInfoService.find(e.getId());
		request.setAttribute("model", e);
		return "jsp/db-media/article/mediaEditArticleDrafts";
	}
	
	/**
	 * 跳转编辑文章页面|来自待发布
	 * @param e
	 * @param request
	 * @return
	 * @author fq
	 */
	@RequestMapping("media_article_edit_page_unrelease")
	public String toArticleEditUnreleasePage(McArticleInfo e , HttpServletRequest request , HttpSession session) {
		super.userBehavior(session , logger , "media_article_edit_page" , "跳转编辑文章页面[id:"+e.getId()+"]");
		e = mcArticleInfoService.find(e.getId());
		request.setAttribute("model", e);
		return "jsp/db-media/article/mediaEditArticleUnrelease";
	}
	
	/**
	 * 获取文章分类数据
	 * @return
	 * @author fq
	 */
	@RequestMapping(value = "ajax_article_type_list", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajax_article_type_list(HttpServletRequest request,HttpSession session) {
		super.userBehavior(session , logger , "ajax_article_type_list" , "获取文章分类数据");   
		JSONObject result  = new JSONObject();
		result.put("type_list", mcArticleTypeService.findList(new McArticleType()));
		return result;
	}
	
	/**
	 * 添加一条文章数据
	 * @param e
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "ajax_article_add", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajaxArticleAdd(McArticleInfo e , HttpServletRequest request , HttpSession session) {
		super.userBehavior(session , logger , "ajax_article_add" , "添加一篇文章");   
		return mcArticleInfoService.ajaxArticleAdd(e , request , session);  
	}
	
	/**
	 * 上传图片接口
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "ajax_upload_file_cfile", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajaxUploadImage(HttpServletRequest request, HttpSession session) {
		super.userBehavior(session , logger , "ajax_upload_file_cfile" , "上传一张图片");   
		return mcArticleInfoService.ajaxUploadImage(request);
	}
	
	/**
	 * 专为ueditor 上传图片使用   ！！！！！
	 * @param type
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "ajax_upload_ueditor_file_cfile", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajaxUploadImageUeditor(String type ,HttpServletRequest request, HttpSession session) {
		super.userBehavior(session , logger , "ajax_upload_ueditor_file_cfile" , "编辑器上传一个文件");   
		if(StringUtils.isNotBlank(type) && "uploadimage".equals(type) ) {
			return FileUploadSupport.getInstance().uploadOnePicture(request);
		} else {
			return new JSONObject();
		}
		
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "media_article_preview_page", produces = { "application/json;charset=utf-8" })
	public void previewArticle (McArticleInfo info , HttpServletRequest request ,HttpServletResponse response, HttpSession session){
		if(null != info.getId()) {
			
			info = mcArticleInfoService.find(info.getId());
			if(null != info) {
				
				//判断文章发布状态
				if("02".equals(info.getReleaseType())) {//已发布
					McArticleRelease entity = new McArticleRelease();
					entity.setArticleInfoId(info.getId());
					entity.setFlag(1);
					JSONObject upReleaseInfo1 = mcArticleInfoService.upReleaseInfo(entity);
					if("success".equals(upReleaseInfo1.getString("status"))) {
						List<McArticleRelease> list = (List<McArticleRelease>) upReleaseInfo1.get("list");
						if(list.size() > 0) {
							McArticleRelease mcArticleRelease = list.get(0);
							String url = mcArticleRelease.getUrl();//发布后的文章所生成静态页（html）的路径
							try {
								response.sendRedirect(url);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					
				} else {//未发布
					String parseAllProductInfo = ReleaseToCdnSupport.getInstance().parseAllProductInfo(info.getRelProductInfoJson());
					String html = ReleaseToCdnSupport.getInstance().htmlInit(info.getHtmlContent() ,  parseAllProductInfo , info);
					PrintWriter writer = null;
					try {
						response.setCharacterEncoding("utf-8");
						writer = response.getWriter();
						writer.write(html);
						writer.flush();
						writer.close();
					} catch (IOException e) {
						if(null != writer) {
							writer.close();
						}
						e.printStackTrace();
					}
				}
				
			}
			
		}
	}
	
	/**
	 * 文章列表页数据接口  
	 * @param e
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "ajax_article_list_data_page", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject ajax_article_list_data_page(McArticleType e , HttpServletRequest request , HttpSession session) {
		super.userBehavior(session , logger , "ajax_edit_assort" , "更新分类记录");   
		return mcArticleTypeService.ajaxReleasedArticleInfo();  
	} 
	
}

