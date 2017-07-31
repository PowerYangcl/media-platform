package com.matrix.support;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matrix.base.BaseClass;
import com.matrix.map.MDataMap;

/**
 * 
 * 调用第三方接口
 * @author fq
 *
 */
public class ApiSupport extends BaseClass{
	
	public static ApiSupport create() {
		return new ApiSupport();
	}

	/**
	 * 调用惠家有接口
	 * @param url
	 * @param param
	 * @return
	 */
	public String hjySupport(String url,MDataMap param) {
		String result = null;
		param.put("api_key", getConfig("center-media.cfamilyApiKey"));
		try {
			result = WebClientSupport.create().upPost( url,  param);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据商品编号获取商品价格、名称、主图
	 * 没有商品数据返回null
	 * @param produtCode
	 * @return  MDataMap  keys:(product_code,product_name,product_main_url,product_price)
	 */
	public MDataMap getCfamilyProductInfo(String produtCode) {
		MDataMap result = null; 
		
		MDataMap param = new MDataMap();
		param.put("productCode", produtCode);//商品编号
		String hjySupport = ApiSupport.create().hjySupport(getConfig("center-media.cfamily_product_info_api_" + this.getConfig("matrix-core.model")) , param);
		if(null != hjySupport) {
			
			JSONObject obj = JSONObject.parseObject(hjySupport);
			if(1 == obj.getInteger("resultCode")) {//返回成功状态
				
				//循环所有sku，取最低价格
				BigDecimal minSkuPrice = BigDecimal.ZERO;
				JSONArray skuList = obj.getJSONArray("skuList");
				if(skuList.size() > 0) {//查看该商品下是否存在sku，如果没有sku，则商品不存在
					
					for (int i = 0; i < skuList.size(); i++) {
						
						BigDecimal curntSkuSellPrice = skuList.getJSONObject(i).getBigDecimal("sellPrice");
						if(i == 0) {
							minSkuPrice = curntSkuSellPrice;
						} else if(minSkuPrice.compareTo(curntSkuSellPrice) > 0) {
							minSkuPrice = curntSkuSellPrice;
						}
						
					}
					
					result = new MDataMap();
					
					result.put("product_code", produtCode);
					result.put("product_name", obj.getString("productName"));
					result.put("product_main_url", JSONObject.parseObject(obj.getString("mainpicUrl")).getString("picNewUrl"));
					result.put("product_price", String.valueOf(minSkuPrice.setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
				
			}
			
		}
		
		return result;
	}
	
	
}
