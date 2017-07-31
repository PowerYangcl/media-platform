<%@ include file="/inc/resource.inc"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/inc/iframe-head.jsp"%>

<style type="text/css">

</style>


<div class="centercontent tables">
	<!--这个跳转页面的功能及跳转路径等等-->
	<div class="pageheader notab">
		<h1 class="pagetitle">ueditor 示例</h1>
		<!-- <span class="pagedesc">
                        这个页面描述了系统中最常用的功能：自定义提示框。
                    </span> -->
		<span style="display: none">jsp/example/ueditorExample.jsp</span>
	</div>

	<div id="contentwrapper" class="contentwrapper">
		<div id="dyntable2_wrapper" class="dataTables_wrapper">

			<div class="subcontent" style="display: block; margin-top: 10px; margin-left: 20px">
				<script id="editor" type="text/plain" style="width:1024px;height:500px;"></script> 
				<!-- 推荐商品列表操作 -->
				<div  class="dataTables_wrapper" style="margin-top:10px;">
					<!-- <div class="title"><h3>推荐商品列表</h3></div> -->
					<div class="widgetbox" style="width: 800px">
                        <div class="title">
                            <h3>推荐商品维护</h3>
                        </div>
                        <button onclick="openAddDialog()" class="stdbtn btn_orange">添加推荐商品</button>
                    </div>
					<table class="stdtable" style="width:700px;">
						<thead>
							<tr>
								<th>商品编号</th>
								<th>商品名称</th>
								<th>商品图片</th>
								<th>商品价格</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="showAddProductInfo"></tbody>
					</table>
				</div>
				<a href="javascript:void(0)" class="anchorbutton" onclick="saveArticle()">保存</a>  
			</div>

		</div>
	</div>
</div>

<!-- 自定义Dialog  div -->
<div id="add-dialog-div" class="dialog-page-div" style="display: none;width: 400px;height: 300px">
    <p class="dialog-title">	<!-- dialog-title是必填的一个类，修饰弹窗的头部 -->
        <a href="javascript:void(0);" onclick="closeDialog()" class="dialog-close"></a>
        	添加推荐商品
    </p>

    <div id="dialog-content-wrapper" class="contentwrapper">
        <div id="dialog-table-form" class="dataTables_wrapper" >
            <form id="dialog-table" onsubmit="return false;">
	            <table class="dialog-table"  style="width:100%;">
	                <tbody>
	                	<tr >
	                		<td style="text-align: right">
	                			商品编号：
	                		</td>
	                		<td style="text-align: left">
	                			<input type="text" name="product_code" class="dialog-form-input" id="input_product_code"/><!-- 输入的商品编号 -->
	                		</td>
	                	</tr>
	                	<div id="showMessage" style="color:red;"></div><!-- 错误信息展示 -->  
	                </tbody>
	             	
	            </table>
	           <button class="stdbtn btn_orange" style="opacity:1" onclick="saveProductInfo(this);">提 &nbsp&nbsp&nbsp&nbsp&nbsp 交</button>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript" charset="utf-8" src="${js}/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${js}/ueditor/ueditor.all.js"> </script>
<script type="text/javascript" charset="utf-8" src="${js}/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript">
	var ue = UE.getEditor('editor');
	
	$(function(){
		var pd = $(window.parent.document);
		var w = pd[0].body.clientWidth - pd.find("#left-menu div:visible")[0].clientWidth - 140;
		var h = pd[0].body.clientHeight -350 ;
		$("#editor").width(w);
		$("#editor").height(h);
		
		// 开始自定义
		
		//自定义ueditor的上传文件方法
		UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;  
	    UE.Editor.prototype.getActionUrl = function(action) {  
	        if (action == 'uploadimage' ) {  
	        	 return '${basePath}example/ajax_upload_file_cfile.do?type=' + action;   // 上传使用自定义的方法
	        	//return 'http://fq.test.com:8081/cfamily/upload1/upload';        
	        } else if(action == 'uploadfile'){
	        	return 'http://172.21.1.159:8081/cfamily/upload/upload';        
	        }else {  
	            return this._bkGetActionUrl.call(this, action);  
	        }  
	    }; 
	    /* //自定义按钮  跳转商品详情的按钮
	     UE.commands['hjy_add_product'] = {
	    		execCommand : function(){
    	    	console.log(0);
    	    	
    	    	var range = this.selection.getRange(),
    	        node = range.getClosedNode();
    	    	node.onclick=function(){window.open('http://test-wx.huijiayou.cn/Product_Detail.html?pid=8016410843&fromshare=1&wxPhone=&goods_num:8016410843')}
    	    	 
    	    } ,
    	    queryCommandState:function(){
    	    	var range = this.selection.getRange(),
    	        node = range.getClosedNode();
    	    	if(undefined != node) {
    	    		return 1;
    	    	} else {
	    	        return -1;
    	    	}
    	    }
    	}; */
	    
	});  
	
	/**
	 * 保存文章 
	 */
	function saveArticle(){
		var meta_ = '<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">'
	    +'<meta content="yes" name="apple-mobile-web-app-capable" />'
	        +'<meta content="black" name="apple-mobile-web-app-status-bar-style" />'
	        +'<meta content="telephone=no" name="format-detection" />'
	        +'<meta content="email=no" name="format-detection" />'
	    	+'<meta charset="UTF-8">'
		var style_ = '<style>img{max-width: 100%;}</style>';
		
		var str = '<html>' + meta_ + '<head>' + style_ + '</head><body>' + ue.getContent()  + '</body></html>';
		str = str.replace(/&#39;/g,"'").toString();
        console.log(str);
	}
	
	
	function openAddDialog(){
		var dialogId = 'add-dialog-div';   // 弹窗ID
		$.blockUI({
            showOverlay:true ,
            css:  {
                cursor:'auto',
                left:($(window).width() - $("#" + dialogId).width())/2 + 'px',
                width:$("#" + dialogId).width()+'px',
                top:($(window).height()-$("#" + dialogId).height())/2 + 'px',
                position:'fixed', //居中
                border: '3px solid #FB9337'  // 边界
            },
            message: $('#' + dialogId),
            fadeIn: 500,//淡入时间
            fadeOut: 1000  //淡出时间
        });
	}

	function closeDialog(){
        $.unblockUI();
    }
	
	function saveProductInfo(obj) {
		/* 获取添加的商品编号 */
		var pc_code = $("#input_product_code").val();
		var reg = new RegExp("^[0-9]{6,}$");
		if(undefined == pc_code || pc_code.length <= 0) {
			$("#showMessage").html("请填写商品编号");
			$("#showMessage").show();
		} else if(!reg.test(pc_code)) {
			$("#showMessage").show("请填写正确的商品编号");
		} else {
			var type_ = 'post';
			var url_ = '${basePath}media/ajax_cfamily_product_info.do';
			var data_ = {product_code:pc_code}; 
			var result = ajaxs.sendAjax(type_, url_, data_);
			if(result.length > 0) {
				var obj = JSON.parse(result);
				$("#showAddProductInfo").append(
						'<tr>' +
							'<td>'+pc_code+'</td>' +
							'<td>'+obj.product_name+'</td>' +
							'<td><img src="'+obj.product_main_url+'" style="width:70px;"/></td>' +
							'<td>'+obj.product_price+'</td>' +
							'<td>' +
								'<input class="stdbtn btn_orange"  type="button" onclick="productMove(\'up\',this);" value="上移"/>' +
								'<input class="stdbtn btn_orange"  type="button" onclick="productMove(\'down\',this);" value="下移"/>' +
								'<input class="stdbtn btn_orange"  type="button" onclick="productDel(this);" value="删除"/>' +
							'</td>' +
						'</tr>'
						);
				//添加完成清空数据
				$("#input_product_code").val();
				$.unblockUI();
			} else {
				//商品不存在
				$("#showMessage").show("商品不存在");
			}
		}
		
	}
 	function productMove(flg,obj) {
 		console.log($(flg).index());
 		if(flg == "up") {
 			console.log("up");
 			var objParentTR = $(obj).parent().parent(); 
 			var prevTR = objParentTR.prev(); 
 			if (prevTR.length > 0) { 
 				prevTR.insertAfter(objParentTR); 
 			} 
 		} else if(flg == "down") {
 			console.log("down");
 			var objParentTR = $(obj).parent().parent(); 
 			var nextTR = objParentTR.next(); 
 			if (nextTR.length > 0) { 
 				nextTR.insertBefore(objParentTR); 
 			} 
 		}
 	}
 	function productDel(obj) {
 		$(obj).parent().parent().remove();
 	}
</script>






























