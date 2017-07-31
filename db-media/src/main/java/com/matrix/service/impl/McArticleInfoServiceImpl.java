package com.matrix.service.impl;

import java.text.SimpleDateFormat;
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
import com.matrix.dao.IMcArticleInfoDao;
import com.matrix.dao.IMcArticleReleaseDao;
import com.matrix.dao.IMcArticleTypeDao;
import com.matrix.dict.LoadCacheReleasedArticleInfo;
import com.matrix.pojo.entity.McArticleInfo;
import com.matrix.pojo.entity.McArticleRelease;
import com.matrix.pojo.entity.McArticleType;
import com.matrix.pojo.view.McUserInfoView;
import com.matrix.service.IMcArticleInfoService;
import com.matrix.support.FileUploadSupport;
import com.matrix.support.ReleaseToCdnSupport;

@Service("mcArticleInfoService")
public class McArticleInfoServiceImpl extends BaseServiceImpl<McArticleInfo, Integer> implements IMcArticleInfoService {

	@Resource
	private IMcArticleInfoDao mcArticleInfoDao;
	
	@Resource
	private IMcArticleTypeDao mcArticleTypeDao;
	
	@Resource
	private IMcArticleReleaseDao mcArticleReleaseDao;
	
	
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
	public JSONObject ajaxArticleList(McArticleInfo e, HttpServletRequest request, HttpSession session) {
		JSONObject r = super.ajaxPageData(e, request);
		if(r.getString("status").equals("success")){
			JSONObject data = r.getJSONObject("data");
			JSONArray list = new JSONArray();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i = 0 ; i < data.getJSONArray("list").size() ; i ++){
				Date rtime = data.getJSONArray("list").getJSONObject(i).getDate("releaseTime");
				String rdate = ""; 
				if(rtime != null){
					rdate = sdf.format(rtime); 
				}
				Date ctime = data.getJSONArray("list").getJSONObject(i).getDate("createTime");
				String cdate = sdf.format(ctime); 
				Date utime = data.getJSONArray("list").getJSONObject(i).getDate("updateTime");
				String udate = sdf.format(utime); 
				JSONObject o = data.getJSONArray("list").getJSONObject(i);
				o.put("releaseTime", rdate); 
				o.put("createTime", cdate);
				o.put("updateTime", udate);
				list.add(o);
			}  
			data.put("list", list); 
			r.put("data", data); 
		}
		
		r.put("atlist", mcArticleTypeDao.findList(new McArticleType())); 
		return r;
	}


	/**
	 * @description: 更新或编辑一篇文章|如果releaseType = 02，则生成html文件发布到cdn
	 * 
	 * @param e
	 * @param request
	 * @param session
	 * @return
	 * @author Yangcl 
	 * @date 2017年6月29日 下午3:05:07 
	 * @version 1.0.0.1
	 */
	public JSONObject ajaxArticleUpdate(McArticleInfo e, HttpServletRequest request, HttpSession session) {
		JSONObject result = new JSONObject();
		e.setUpdateTime(new Date()); 
		String fileUrl = "";
		McArticleInfo e_ = null;
		McUserInfoView userInfo = (McUserInfoView) session.getAttribute("userInfo");
		if(e.getReleaseType().equals("02")){  // 如果releaseType = 02，则生成html文件发布到cdn
			e_ = mcArticleInfoDao.find(e.getId());
			if(e_ != null && StringUtils.isNotBlank(e_.getHtmlContent())){
				JSONObject r = ReleaseToCdnSupport.getInstance().releaseArticleToCdn(e_);
				if(r.getString("status").equals("error")){
					return r; 
				}
				fileUrl = r.getString("fileUrl");
			}else{
				result.put("status", "error");
				result.put("msg", this.getInfo(700010009));  // 文章发布失败! 文章内容为空，请检查!
				return result;
			}
			e.setReleaser(userInfo.getUserName()); 
			e.setReleaseTime(new Date()); 
		}
 
		
		int flag = mcArticleInfoDao.updateSelective(e);
		if(flag == 1){
			result.put("status", "success");
			result.put("msg", this.getInfo(700010005));  // 数据更新成功!
			// 如果releaseType = 02，则fileUrl 添加数据 
			if(e_ != null && e.getReleaseType().equals("02")){
				McArticleRelease ar = new McArticleRelease();
				ar.setArticleInfoId(e_.getId());
				ar.setFlag(0); // 将其他记录设置为失效状态
				mcArticleReleaseDao.updateByArticleInfoId(ar);
				ar.setFlag(1);
				ar.setUrl(fileUrl);
				ar.setReleaser(e.getReleaser());
				ar.setReleaserId(userInfo.getId());
				ar.setReleaseTime(new Date());
				mcArticleReleaseDao.insertSelective(ar);
			}
			
			//如果是发布或者取消发布  ，将刷新已发布的文章列表缓存
			if( "02".equals(e.getReleaseType())  || "01".equals(e.getReleaseType()) ){
				new LoadCacheReleasedArticleInfo().refresh();
			}
			
			
		}else{
			result.put("status", "error");
			result.put("msg", this.getInfo(700010006));  //更新数据失败!
		}
		
		return result;
	}
	


	@Override
	public JSONObject ajaxArticleDelete(Integer id, HttpServletRequest request, HttpSession session) {
		JSONObject result = new JSONObject();
		if (id == null) {
			result.put("status", "error");
			result.put("msg", this.getInfo(700010008)); //数据删除失败！
			return result;
		}
		
		int flag = mcArticleInfoDao.deleteById(id);
		if(flag == 1){
			result.put("status", "success");
			result.put("msg", this.getInfo(700010007));  // 数据删除成功!
		}else{
			result.put("status", "error");
			result.put("msg", this.getInfo(700010008));  //更新删除失败!
		}
		
		return result;
	}  
	
	public JSONObject ajaxArticleAdd(McArticleInfo e, HttpServletRequest request, HttpSession session) {
		JSONObject result = new JSONObject();
		if(StringUtils.isAnyBlank(e.getTitle() , e.getSource())){
			result.put("status", "error");
			result.put("msg", this.getInfo(700010015));  // 必填字段为空，请核实后重新提交!
			return result;
		}
		
		/**
		 * 添加初始值
		 */
		e.setReleaseTime(new Date());
		e.setCreateTime(new Date());
		McUserInfoView userInfo = (McUserInfoView) session.getAttribute("userInfo");
		e.setAuthor(userInfo.getUserName());
		e.setAuthorId(userInfo.getId());
		e.setUpdateTime(new Date());
		e.setEditor(userInfo.getUserName());
		e.setEditorId(userInfo.getId());
		
		Integer insertSelective = mcArticleInfoDao.insertSelective(e);
		if(insertSelective == 1) {
			result.put("status", "success");
			result.put("msg", this.getInfo(700010003));// 添加成功
		} else {
			result.put("status", "error");
			result.put("msg", this.getInfo(700010004));  //添加失败
		}
		return result;
		
	}
	
	/**
	 * 图片上传
	 * @return
	 */
	public JSONObject ajaxUploadImage(HttpServletRequest request) {
		List<String> uploadPic = FileUploadSupport.getInstance().uploadPic(request);
		JSONObject result = new JSONObject();
		result.put("imgs", StringUtils.join(uploadPic,","));
		return result;
	}

	
	/**
	 * 查询发布信息
	 * @param entity
	 * @return
	 */
	public JSONObject upReleaseInfo (McArticleRelease entity) {
		JSONObject result = new JSONObject();
		result.put("status", "success");
		try {
			List<McArticleRelease> findByArticleRelease = mcArticleReleaseDao.findByArticleRelease(entity);
			result.put("list", findByArticleRelease);
		} catch (Exception e) {
			result.put("status", "error");
			result.put("msg", getInfo(700010014));
			e.printStackTrace();
		}
		return result;
	}

}





































