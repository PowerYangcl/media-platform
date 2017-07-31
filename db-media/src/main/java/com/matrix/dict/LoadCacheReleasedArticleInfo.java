package com.matrix.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.matrix.annotation.Inject;
import com.matrix.base.BaseClass;
import com.matrix.base.interfaces.IBaseCache;
import com.matrix.cache.CacheLaunch;
import com.matrix.cache.enums.SCacheEnum;
import com.matrix.cache.inf.IBaseLaunch;
import com.matrix.cache.inf.ICacheFactory;
import com.matrix.dao.IMcArticleInfoDao;
import com.matrix.dao.IMcArticleReleaseDao;
import com.matrix.pojo.cache.CatchReleasedArticleInfo;
import com.matrix.pojo.entity.McArticleInfo;
import com.matrix.pojo.entity.McArticleRelease;

/**
 * 将已发布的文章列表数据加入缓存
 * @author fq
 *
 */
public class LoadCacheReleasedArticleInfo extends BaseClass implements IBaseCache{
	
	public static String RELEASEDKEY = "released";

	private IBaseLaunch<ICacheFactory> launch = CacheLaunch.getInstance().Launch();
	
	public static LoadCacheReleasedArticleInfo instance = new LoadCacheReleasedArticleInfo();
	
	@Inject
	private IMcArticleInfoDao articelInfoDao; 
	
	@Inject
	private IMcArticleReleaseDao articleReleaseDao;
	
	/**
	 * 调用此方法强行刷新缓存数据
	 */
	@Override
	public void refresh() {
		
		/**
		 * 将已经发布的文章数据放缓存中，  只记录文章标题、作者、阅读数量、点赞数、标题图片、详情链接
		 */
		McArticleInfo queryParamInfo = new McArticleInfo();
		queryParamInfo.setReleaseType("02");
		List<McArticleInfo> queryPage = articelInfoDao.queryPage(queryParamInfo);
		List<CatchReleasedArticleInfo> saveList = new ArrayList<CatchReleasedArticleInfo>();
		
		/*
		 * 查询所有已发布详情链接
		 */
		Map<Integer, String> detailLink = new HashMap<Integer, String>();
		McArticleRelease releaseModel = new McArticleRelease();
		releaseModel.setFlag(1);
		List<McArticleRelease> findByArticleRelease = articleReleaseDao.findByArticleRelease(releaseModel);
		for (McArticleRelease mcArticleRelease : findByArticleRelease) {
			detailLink.put(mcArticleRelease.getArticleInfoId(), mcArticleRelease.getUrl());
		}
		
		
		for (McArticleInfo info : queryPage) {
			CatchReleasedArticleInfo releasedInfo = new CatchReleasedArticleInfo();
			releasedInfo.setArticleId(info.getId());
			releasedInfo.setTitle(info.getTitle());
			releasedInfo.setSource(info.getSource()); 
			releasedInfo.setAuthor(info.getAuthor());
			releasedInfo.setReaderCount(info.getReaderCount());
			releasedInfo.setThumbsUpCount(info.getThumbsUpCount());
			if(StringUtils.isNotBlank(info.getTitlePic())) {
				releasedInfo.setTitlePic(info.getTitlePic());
			} else {//没有图片，则取默认图片
				releasedInfo.setTitlePic(getConfig("center-media.articeTitleImageDefaultUrl"));
			}
			if(detailLink.containsKey(info.getId())) {
				releasedInfo.setDetailLik(detailLink.get(info.getId()));
			} else {
				releasedInfo.setDetailLik("");
			}
			saveList.add(releasedInfo);
		}
		launch.loadServiceCache(SCacheEnum.ArticleListPage).set(RELEASEDKEY, JSONObject.toJSONString(saveList));
		
	}

	/**
	 * 删除缓存数据
	 */
	@Override
	public void removeAll() {
		launch.loadServiceCache(SCacheEnum.ArticleListPage).del(RELEASEDKEY);
	}
	
	
	
	
}
