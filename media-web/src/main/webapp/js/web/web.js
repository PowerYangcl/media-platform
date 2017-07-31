var web = {
	share_img_url : "",
	init : function() {
		 web.share_img_url = web.up_urlparam("share_img");
		 web.articleDetailShare();//媒体站详情页分享
	},
	versions: function() { 
		var u = navigator.userAgent, app = navigator.appVersion; 
		return {//移动终端浏览器版本信息  
			trident: u.indexOf('Trident') > -1, //IE内核 
			presto: u.indexOf('Presto') > -1, //opera内核 
			webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核 
			gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核 
			mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端 
			ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端 
			android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器 
			iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器 
			iPad: u.indexOf('iPad') > -1, //是否iPad 
			webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部 
		}; 
	}(), 
	language: (navigator.browserLanguage || navigator.language).toLowerCase(),
	/*
	 * 分享
	 */
	share : function (share_title,share_img,share_content,share_link) {
		/*
		 * 判断设备类型
		 */
		try {
			if(web.versions.android) {
				//callbackFunc("android",share_title,ad_name,share_content,share_pic,"true");
				window.share.shareOnAndroid(share_title,share_img,share_content,share_link,"true");
			} else if(web.versions.ios) {
				//callbackFunc("ios",share_title,ad_name,share_content,share_pic,"true");
				OCModel.getjsshare("ios",share_title,share_img,share_content,share_link,"true");
			}
		}catch (e) {}
		
	},
	/**
	 * get 参数获取
	 */
	up_urlparam :function (sKey, sUrl) {
		var sReturn = "";
		
		if (!sUrl) {
			sUrl = window.location.href;
			if (sUrl.indexOf('?') < 1) {
				sUrl = sUrl + "?";
			}
		}
	
		var sParams = sUrl.split('?')[1].split('&');
	
		for (var i = 0, j = sParams.length; i < j; i++) {
	
			var sKv = sParams[i].split("=");
			if (sKv[0] == sKey) {
				sReturn = sKv[1];
				break;
			}
		}
	
		return sReturn;
	
	},
	/*
	 * 文章详情分享
	 */
	articleDetailShare : function() {
		var share_title = $("title").html();
		var share_img = web.share_img_url;
		var share_content = $("p:eq(0)").html();//将第一个p标签的内容作为分享内容
		var share_link = window.location.href;
		web.share(share_title,share_img,share_content,share_link);
	}
};
