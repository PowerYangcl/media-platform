package com.matrix.dao;

import java.util.List;

import com.matrix.base.interfaces.IBaseDao;
import com.matrix.pojo.entity.McArticleRelease;

public interface IMcArticleReleaseDao extends IBaseDao<McArticleRelease, Integer>{
	
	public Integer updateByArticleInfoId(McArticleRelease e);
	
	public List<McArticleRelease> findByArticleRelease(McArticleRelease e);
}
