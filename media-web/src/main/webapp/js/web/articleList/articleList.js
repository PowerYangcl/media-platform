var articleList = {
	/*
	 * 加载页面数据
	 */
	loadData : function( ) {
		$.ajax({ 
			type: "POST",
			url: "../../api/api_article_list_data_page.do",
			dataType: "json",
			success: function(data) {
				if(data.status == "success") {
					var d = JSON.parse(data.data);
					var contentHTML = articleList.parseData(d) ;
					$("#articleListContentShow").html(contentHTML);
				}
				
			}
		});
	} ,
	/*
	 * 解析列表数据
	 */
	parseData : function (data) {
		var parsedataHtml = '';
		for(var i=0;i<data.length ;i++) {
			var info = data[i];
			if(undefined != info.detailLik && null != info.detailLik && info.detailLik.length > 0) {//判断是否有跳转链接
				parsedataHtml += '<li onclick = "articleList.forward(\''+info.detailLik+'\',this)">';
			} else {
				parsedataHtml += '<li >';
			}
				parsedataHtml += '<div class="hui_img"><img src="'+info.titlePic+'"></div>';
				parsedataHtml += '<span class="hui_txt">'+info.title+'</span>';
				parsedataHtml += '<div class="hui_passBy">';
					parsedataHtml += '<span>'+info.source+'</span>';
					// parsedataHtml += '<span class="looked">'+info.readerCount+'</span>';            // 阅读数
					// parsedataHtml += '<span class="like">'+info.thumbsUpCount+'</span>';          // 点赞数
				parsedataHtml += '</div>';
			parsedataHtml += '</li>';
		}
		
		return parsedataHtml ;
	},
	
	/*
	 * 文章详情页跳转方式
	 */
	forward : function( url,obj) {
		var shareImg = $(obj).find(".hui_img img").attr("src");
		//将分享图片传到头条详情页
		window.location.href = url + encodeURI("?share_img="+shareImg);
	}
	
}



