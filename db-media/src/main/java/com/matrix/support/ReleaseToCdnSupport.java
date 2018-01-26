package com.matrix.support;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matrix.base.BaseClass;
//import com.matrix.imageComPress.MFileItem;
import com.matrix.pojo.entity.McArticleInfo;
import com.matrix.pojo.view.ProductInfo;
import com.matrix.util.IoUtil;

/**
 * @description: 将文章生成静态文件，并发布到cdn上 
 * 
 * @author Yangcl
 * @date 2017年7月12日 下午5:33:46 
 * @version 1.0.0
 */
public class ReleaseToCdnSupport extends BaseClass{

	private ReleaseToCdnSupport(){} 
	
	private static class LazyHolder {
		private static final ReleaseToCdnSupport INSTANCE = new ReleaseToCdnSupport();
	}
	public static final ReleaseToCdnSupport getInstance() {
		return LazyHolder.INSTANCE; 
	}
	
	private static final String path = System.getProperty("user.home") + File.separator + "matrixzoos" + File.separator + "dir" + File.separator + "temp" + File.separator;
	
	
	/**
	 * @description: 上传到cfile服务器上
	 * 
	 * @author Yangcl 
	 * @date 2017年7月12日 下午5:36:53 
	 * @version 1.0.0.1
	 */
	public JSONObject releaseArticleToCdn(McArticleInfo e){
		String path_ = path + "media" + File.separator;
		IoUtil.getInstance().createDir(path_);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String dateStr = sdf.format(new Date());
		path_ = path_  + dateStr + ".html";
		File target = new File(path_);
		String parseProductInfo = this.parseAllProductInfo(e.getRelProductInfoJson());
		String ueditContent = e.getHtmlContent();
		
		/**
		 * 压缩图片
		 */
//		Map<String, String> compressArticleImage = this.compressArticleImage(e);//进行压缩
//		Set<String> newImgKeySet = compressArticleImage.keySet();
//		//压缩后的图片替换
//		for (String imgkey : newImgKeySet) {
//			if(StringUtils.isNotBlank(ueditContent)) {
//				ueditContent = ueditContent.replaceAll(imgkey, compressArticleImage.get(imgkey));
//			}
//			if(StringUtils.isNotBlank(parseProductInfo)) {
//				parseProductInfo = parseProductInfo.replaceAll(imgkey, compressArticleImage.get(imgkey));
//			}
//		}
		
		JSONObject result = this.htmlDocInit(ueditContent , parseProductInfo, target , e);
		if(result.getString("status").equals("success")){
			// 上传本地文件到cfile服务器 
			JSONObject ulf = FileUploadSupport.getInstance().uploadLocalFile(target);
			if(ulf.getString("status").equals("success")){
				result.put("fileUrl" , ulf.getString("url"));  
			}else{
				result.put("status", "error");
				result.put("msg", this.getInfo(700010013 , "ReleaseToCdnSupport.releaseArticleToCdn()"));  //html文件上传到cfile服务器失败!{0} 请联系后台开发人员
				return result;
			}
		}
		return result; 
	}
	
	/**
	 * @description: 将目标路径下的文件夹和文件持久化到一个html文件中。
	 * 
	 * @param html
	 * @param target
	 * @author Yangcl 
	 * @date 2017年7月12日 下午6:16:28 
	 * @version 1.0.0.1
	 */
	 private JSONObject htmlDocInit(String uehtml , String phtml, File target , McArticleInfo e) {
		JSONObject result = new JSONObject();
		result.put("status", "success");
		if (target.exists()) {
			target.delete();
		}
		try {
			target.createNewFile();
		} catch (IOException ex) {
			String name = target.getName();
			result.put("status", "error");
			result.put("msg", this.getInfo(700010010, name));  // 目标文件【{0}】创建失败
			return result;
		}
		
		String html = this.htmlInit(uehtml , phtml , e);
		
		List<String> list = new ArrayList<String>();
		list.add(html);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "utf-8"));
			for (String str : list) {
				bw.write(str);
				bw.newLine();
			}
			bw.flush();
		} catch (IOException ex) {
			target.deleteOnExit();
			result.put("status", "error");
			result.put("msg", this.getInfo(700010011 , "ReleaseToCdnSupport.htmlDocInit()"));  // 程序读写失败! {0}创建html文件失败! 请联系后台开发人员
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ex) {
					target.deleteOnExit();
					result.put("status", "error");
					result.put("msg", this.getInfo(700010012 , "ReleaseToCdnSupport.htmlDocInit()"));  // 服务器IO流关闭异常!{0} 请联系后台开发人员
				}
			}
		}
		
		return result;
	}

	public String parseAllProductInfo(String jsonString) {
		String rtnStr = "";
		if(StringUtils.isNotBlank(jsonString)) {
			try {
				List<ProductInfo> productInfo = JSONArray.parseArray(jsonString, ProductInfo.class);
				if(null != productInfo && productInfo.size() > 0) {
					rtnStr += "<div class='mobile_2' >";
					rtnStr += "<section class='rebate'>";
					rtnStr += "<ul>";
					for (int i = 0; i < productInfo.size(); i++) {
						if(i > 0 && i%2 == 0) {
							rtnStr += "<div style='clear:both;'></div>";
						}
						String parseProduct = this.parseProduct(productInfo.get(i));
						rtnStr += parseProduct;
					}
					
					rtnStr += "</ul>";
					rtnStr += "</section>";
					rtnStr += "</div>";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return rtnStr;
		
	}
	
	public String parseProduct(ProductInfo info) {
		String rtnStr = "";
		String fowardUrl = getConfig("center-media.cfamily_product_" + this.getConfig("matrix-core.model")) + info.getProductCode()+"&goods_num:"+info.getProductCode();
		rtnStr += "<li module='202105'>";
			rtnStr += "<a href='"+fowardUrl+"' target='_blank' >";
				if(StringUtils.isNotBlank(info.getProductImg())) {
					rtnStr += "<img src='"+info.getProductImg()+"' class='p-img' >";
				} 
				
				rtnStr += "<p >";
					rtnStr += "<b>"+info.getProductName()+"</b>";
					rtnStr += "<font>¥"+info.getProductPrice()+"</font>";
					rtnStr += "<span><img src='"+getConfig("center-media.productLJGMImage")+"' alt='立即购买'></span>";
				rtnStr += "</p>";
			rtnStr += "</a>";
		rtnStr += "</li>";
		return rtnStr;
	}
	
	/**
	 * 媒体站静态页样式
	 * @return
	 */
	public String upHtmlStyle() {
		String rtnStyle = "<style>";
			rtnStyle += "*{margin:0;padding:0;}";
			rtnStyle += "html { font-size: 100px;    max-width: 640px;   margin: 0 auto;}";
			rtnStyle += "body{overflow-x:hidden;}";
			rtnStyle += "body, input, textarea, select{ font-family: 'PingFangSC-Light', Verdana, Arial, Helvetica, sans-serif;}";
			rtnStyle += "body, div, dl, dt, dd, ul, ol, li, h1, h2, h3, pre, form, fieldset, input, textarea, blockquote, p, header, footer, nav, section, article, aside, video { padding: 0px; margin: 0px; tap-highlight-color: rgba(0, 0, 0, 0); -webkit-tap-highlight-color: rgba(0, 0, 0, 0); -ms-tap-highlight-color: rgba(0, 0, 0, 0); }";
			rtnStyle += "table, td, tr, th { font-size: 12px; }";
			rtnStyle += "table { margin: 0px auto; }";
			rtnStyle += "img { vertical-align: top; border: 0px; }";
			rtnStyle += "ol, ul { list-style: none; }";
			rtnStyle += "li { list-style-type: none; vertical-align: top;}";
			rtnStyle += "h1, h2, h3, h4, h5, h6 { font-weight: normal;}";
			rtnStyle += "address, caption, cite, code, dfn, em, i, b { font-weight: normal; font-style: normal; }";
			rtnStyle += "a { -webkit-appearance: none; outline: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); -webkit-touch-callout: none; text-decoration: none;}";
			rtnStyle += "a:hover { text-decoration: none; }";
			rtnStyle += "img { width: 100%; height: auto; vertical-align: top; }";
			rtnStyle += "input, textarea, select { outline: none; resize: none; border: 0;}";
			rtnStyle += ".clearfix:after { content: ''; clear: both; display: block; line-height: 0px; font-size: 0px; height: 0px; visibility: hidden; }";
			rtnStyle += ".clear { clear: both; display: block; line-height: 0px; font-size: 0px; height: 0px; visibility: hidden; }";
			
			rtnStyle += "img{max-width: 100%;}";
			rtnStyle += "body > p{font-size: 18px; line-height:27px; margin-left:10px;margin-right:10px; letter-spacing: 0.5px}";
			rtnStyle += ".mobile_2{    padding-top: .25rem;}";
			rtnStyle += ".rebate ul li{overflow:hidden;width:50%;box-sizing:border-box;float:left;padding:0 .02rem .04rem 0;}";
			rtnStyle += ".rebate ul li a{display:block;background:#fff;position:relative;}";
			rtnStyle += ".rebate ul li em{display:block;width:.62rem;height:.62rem;background:url(../../images/template/ico-qgl.png) no-repeat;background-size:100%;position:absolute;left:50%;margin-left:-.31rem;top:50%;margin-top:-.62rem;}";
			rtnStyle += ".rebate ul li em.e1{background:url(../../images/template/ico-xj.png) no-repeat;background-size:100%;}";
			rtnStyle += ".rebate ul li p b{display:block;line-height:.15rem;color:#222;font-size:.12rem;height:.3rem;overflow:hidden;}";
			rtnStyle += ".rebate ul li p font{display:block;font-size:.16rem;color:#dc0f50;}";
			rtnStyle += ".rebate ul li p font i{color:#999;font-size:.12rem;padding-left:.18rem;text-decoration:line-through;}";
			
			rtnStyle += ".rebate ul{overflow:hidden;padding:0 .02rem 0 .07rem;/* margin:0 0 .05rem 0; */}";
			rtnStyle += ".rebate ul li{padding:0 .04rem 0 0;margin-bottom:.05rem;}";
			rtnStyle += ".rebate ul li p{height:1rem;padding:.05rem .1rem;box-sizing:border-box;}";
			rtnStyle += ".rebate ul li em{margin-top:-.8rem;}";
			rtnStyle += ".rebate ul li span{display:block;width:.35rem;height:.14rem;background:#dc6c8f;text-align:center;line-height:.14rem;color:#fff;font-size:.12rem;float:right;margin-top:-.14rem;position:relative;z-index:10;}";
			rtnStyle += ".rebate ul li p span{display:block;width:1.01rem;height:.27rem;margin:.07rem auto;float:none;background:none;}";
			
			rtnStyle += "h1, h2, h3, h4, h5, h6 {font-size: 20px;margin-top:26px;margin-bottom:26px;margin-left:10px;font-weight:bold;letter-spacing: 0.5px}";
			rtnStyle += "p > img{padding-top:16px; padding-bottom:16px}";
			rtnStyle += "";
		rtnStyle += "</style>";
		return rtnStyle;
	}
	
	/**
	 * @description: 
	 * 
	 * @param uehtml 来自ue编辑器的内容
	 * @param phtml 来自商品的html内容  
	 * @return
	 * @author fq 
	 * @date 2017年7月18日 下午3:30:02 
	 * @version 1.0.0.1
	 */
	public String htmlInit (String uehtml , String phtml , McArticleInfo e) {
		String ueHtml = "您尚未编辑文章内容! ";
		if(StringUtils.isNotBlank(uehtml)){ 
			ueHtml = uehtml;
		}
		
		String title = "<p style='font-weight:bold;font-size:26px; line-height:30px;margin-top:40px'>" + e.getTitle() + "</p>";
		String source = "<p style='color:#666666;font-size:14px; line-height:24px;margin-top:16px; margin-bottom:28px'>" + e.getSource() + "</p>"; 
		
		String outUrl = "";
		if(StringUtils.isNotBlank(e.getOutUrl())){
			String s = e.getOutUrl();
			if(!StringUtils.contains(s, "?")){
				s = s + "?opentype=1&frd=yes";
			}else{
				s = s + "&opentype=1&frd=yes";
			}
			
			outUrl = "<div style='margin-top:40px;margin-bottom:20px;'><a href = '" + s + "' style='color:#28A7F3;margin-left:10px;font-size:18px;' > 阅读原文</a><div>";
		}
		
		String body = title + source + ueHtml + phtml + outUrl;
		String html = "";
		String meta_ = "<meta name='viewport' content='width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no'>"
			    +"<meta content='yes' name='apple-mobile-web-app-capable' />"
		        +"<meta content='black' name='apple-mobile-web-app-status-bar-style' />"
		        +"<meta content='telephone=no' name='format-detection' />"
		        +"<meta content='email=no' name='format-detection' />"
		    	+"<meta charset='UTF-8'><title>惠家有</title>";
		String style_ = this.upHtmlStyle();
		
		String jscript = "";
		String jsUrls = getConfig("center-media.articel_detail_js_url_" + this.getConfig("matrix-core.model")); 
		if(StringUtils.isNotBlank(jsUrls)) {
			String[] jsArr = jsUrls.split(",");
			for (String url : jsArr) {
				jscript += "<script type='text/javascript' src='"+url+"'></script>";
			}
		}
		jscript += "<script type='text/javascript'>$(window).load(function() {var a = ($('.rebate ul').width()-8) / 2;$('.p-img').css('width' , a); $('.p-img').css('height' , a); web.init();  });</script>";
		
		html = "<html>" + meta_ + "<head>" + style_ + jscript + "</head><body>" + body  + "</body></html>";
		return html;
	}
	
//	public Map<String, String> compressArticleImage (McArticleInfo e) {
//		Map<String, String> map = new HashMap<String, String>();//存放压缩前后的图片，key:oldImage  ; value:newImage;  用map也为了去重
//		
//		//获取编辑器内容图片
//		String htmlContent = e.getHtmlContent();
//		if(StringUtils.isNotBlank(htmlContent)) {
//			String[] arr = htmlContent.split("<img");
//			for (int i = 1; i < arr.length; i++) {//从第二个元素开始取图片链接
//				
//				int beginIndex = arr[i].indexOf("http");
//				int endIndex = arr[i].indexOf("\"", beginIndex);
//				if(beginIndex < endIndex) {
//					String src = arr[i].substring(beginIndex, endIndex);
//					map.put(src, "");
//				}
//				
//			}
//		}
//		
//		//获取商品图片
//		String relProductInfoJson = e.getRelProductInfoJson();
//		if(StringUtils.isNotBlank(relProductInfoJson)) {
//			JSONArray productList = JsonHelper.fromJson(relProductInfoJson, new JSONArray());
//			for (Object object : productList) {
//				JSONObject productInfo = (JSONObject) object;
//				String img = productInfo.getString("productImg");
//				if(StringUtils.isNotBlank(img)) {
//					map.put(img, "");
//				}
//			}
//		}
//		Set<String> keySet = map.keySet();
//		List<String> compressImgList = new ArrayList<String>();
////		System.out.println("======================需要压缩的图片=========================");
//		for (String key : keySet) {
////			System.out.println(key);
//			compressImgList.add(key);
//		}
////		System.out.println("=============================================================");
//		//进行图片压缩
//		if(compressImgList.size() > 0) {
//			Map<String, MFileItem> uploadImage = FileUploadSupport.getInstance().uploadImage(compressImgList);
//			for (String key : keySet) {
//				if(uploadImage.containsKey(key)) {
//					map.put(key, uploadImage.get(key).getFileUrl());
//				}
//			}
//			
//		}
//		
//		return map;
//	}
}


























