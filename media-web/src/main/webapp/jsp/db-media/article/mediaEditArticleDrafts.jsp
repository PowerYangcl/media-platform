<%@ include file="/inc/resource.inc" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 <%@ include file="/inc/iframe-head.jsp" %>
<%--  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> --%>
 
<script type="text/javascript" src="${js}/ueditor/ueditor.config.js"></script> 
<script type="text/javascript" src="${js}/ueditor/ueditor.all.js"></script> 
<script type="text/javascript" src="${js}/ueditor/lang/zh-cn/zh-cn.js"></script> 

 <script type="text/javascript">
	$(function() {
		var type_ = 'post';
		var url_ = '${basePath}media/ajax_article_type_list.do';
		var data_ = {};  
		var obj = JSON.parse(ajaxs.sendAjax(type_, url_, data_));
		var arr = obj.type_list;
		if (arr.length != 0) {
			var html_ = '';
			for (var i = 0; i < arr.length; i++) {
				var curtType = '${model.articleTypeId }';
				if(arr[i].id == curtType) {
					html_ += '<option value="' + arr[i].id + '" selected="selected" >' + arr[i].name + '</option>';
				} else {
					html_ += '<option value="' + arr[i].id + '" >' + arr[i].name + '</option>';
				}
			}
			$("#article-type-id").append(html_);
		}
		
		/**
		* 更改ueditor  start
		*/
		var ue = UE.getEditor('editor');
		var pd = $(window.parent.document);
		var w = pd[0].body.clientWidth - pd.find("#left-menu div:visible")[0].clientWidth - 140;
		var h = pd[0].body.clientHeight -350 ;
		$("#editor").width(w);
		$("#editor").height(h);
		//自定义ueditor的上传文件方法
		UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;  
	    UE.Editor.prototype.getActionUrl = function(action) {  
	        if (action == 'uploadimage' ) {  
	        	 return '${basePath}media/ajax_upload_ueditor_file_cfile.do?type=' + action;   // 上传使用自定义的方法
	        } else {  
	            return this._bkGetActionUrl.call(this, action);  
	        }  
	    }; 
	    
		/**
		* 更改ueditor  end
		*/
		
		//编辑页数据回显
		setTimeout(function(){
			UE.getEditor('editor').setContent('${model.htmlContent}');
		},1000);
	    
	    uploadedImage('${model.titlePic }');
		$("#htmlContent").val('${model.htmlContent}');
		
		if('${model.relProductInfoJson}' == ''){
			return;
		}
		var rel_product_json_info = JSON.parse('${model.relProductInfoJson}');
		if(undefined != rel_product_json_info && null != rel_product_json_info && rel_product_json_info.length > 0) {
			var tHTML = "";
			for (var i=0;i<rel_product_json_info.length;i++) {
				tHTML += '<tr>';
				tHTML += '<td>'+rel_product_json_info[i].productCode+'</td>';
				tHTML += '<td>'+rel_product_json_info[i].productName+'</td>';
				tHTML += '<td><img src="'+rel_product_json_info[i].productImg+'" style="width:70px;"/></td>';
				tHTML += '<td>'+rel_product_json_info[i].productPrice+'</td>';
				tHTML += '<td>' +
					'<input class="stdbtn btn_orange"  type="button" onclick="productMove(\'up\',this);" value="上移"/>' +
					'<input class="stdbtn btn_orange"  type="button" onclick="productMove(\'down\',this);" value="下移"/>' +
					'<input class="stdbtn btn_orange"  type="button" onclick="productDel(this);" value="删除"/>' +
				'</td>';
				tHTML += '</tr>';
			}
			$("#showAddProductInfo").html(tHTML);
		}
		
		
	});
	function addInfo(type) {
		//UE.getEditor("editor").window.innerWidth;
		$("#htmlContent").val(UE.getEditor('editor').getContent());
		var type_ = 'post';
		var url_ = '${basePath}media/ajax_article_update.do';
		var data_ = $("#form-example").serializeArray();; // 可以为null，后台会进行默认处理
		
		//组装推荐商品信息json
		var productInfoJson = new Array();
		$("#showAddProductInfo tr").each(function(index){//添加所有关联的商品
			var tdArr = $(this).find("td");
			var product = new Object();
			product.productCode  = $(tdArr[0]).html();;
			product.productName  = $(tdArr[1]).html();
			product.productImg   = $(tdArr[2]).find("img").attr("src");
			product.productPrice = $(tdArr[3]).html();
			productInfoJson[index] = product;
		});
		var pc = new Object();
		pc.name="relProductInfoJson";
		pc.value= JSON.stringify(productInfoJson);
		data_.push(pc);
		
		var typeObj = new Object();
		typeObj.name = 'releaseType';
		typeObj.value= type;
		data_.push(typeObj);
		
		if(trim($("#title").val()) == "" || trim($("#source").val()) == ""){
			jAlert("必填字段为空，请核实后重新提交!", '系统提示' );
		}else{
			var obj = JSON.parse(ajaxs.sendAjax(type_, url_, data_));
			if("success" == obj.status) {
				var msg = '';
				var herf_ = '';
				if(type == '03'){
					msg = '文章保存成功，单击确定回到草稿箱列表';
					herf_ = '${basePath}media/page_media_drafts_list.do';
				}else{
					$('.vernav2 > ul > li > ul > li' , window.parent.document).removeClass("current");
					msg = '文章已提交到待发布列表，单击确定回到待发布文章列表';
					herf_ = '${basePath}media/page_media_unreleased_list.do';
				}
				jAlert( msg , '系统提示' , function(){
					window.location.href = herf_;
				});
				
			} else {
				jAlert(obj.msg, '系统提示');
			}
		}
	}
	
	function trim(str) {
	    return str.replace(/(^\s+$)/g, "");
	}
	
	/* uploadedImage 此方法为上传图片回调方法 */
	function uploadedImage(imgs) {
		$("#titlePic").val(imgs);
		var tHtml = '';
		var imgArr ;
		if(imgs.length > 0) {
			imgArr = imgs.split(",");
			for(var i=0;i<imgArr.length ;i++) {
				tHtml += '<img src="'+imgArr[i]+'" style="width:100px;" />';
			}
			$("#show_upload_image_div").html(tHtml);
		}
		
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
		$("#showMessage").hide();
		/* 获取添加的商品编号 */
		var pc_code = $("#input_product_code").val();
		var reg = new RegExp("^[0-9]{6,}$");
		if(undefined == pc_code || pc_code.length <= 0) {
			$("#showMessage").html("商品编号填写错误！");
			$("#showMessage").show();
		} else if(!reg.test(pc_code)) {
			$("#showMessage").html("商品编号填写错误！");
			$("#showMessage").show();
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
		$("#input_product_code").val("");
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

<div class="centercontent">
	<div class="pageheader notab">
		<h1 class="pagetitle">编辑文章</h1>
	</div>
	<div id="validation" class="subcontent" style="display: block">
		<form id="form-example" class="stdform" method="post" action="" >
			<p>
				<label>标题</label> 
				<span class="field"> 
					<input type="text" name="title" id="title" class="ae-form-input" value="${model.title }" maxlength="30" placeholder="必填字段 5~30个字"/>
				</span>
			</p>
			<p>
				<label>标题小图</label>
				<iframe src="../jsp/sys_page/uploadImage.jsp" style="height:40px;"></iframe>
				<input type="hidden" name="id" id="id" value="${model.id }" />
				<input type="hidden" name="titlePic" id="titlePic" />
				<input type="hidden" name="htmlContent" id="htmlContent" />
				<div class="field" id="show_upload_image_div" >
					<%-- <c:if test="${not empty model.titlePic}">
						<img src="${model.titlePic }" style="width:100px;" />
						<c:set value="${ fn:split(model.titlePic, ',') }" var="imgArr" />
						<c:forEach items="${ imgArr }" var="s">
							<img src="${s }" style="width:100px;" />
						</c:forEach>
					</c:if> --%>
				</div>
				<span class="field" style="color:red">建议尺寸：240像素*180像素</span>
			</p>
			<p>
				<label>文章来源</label> 
				<span class="field"> 
					<input type="text" name="source" id="source" class="ae-form-input" value="${model.source }" maxlength="10" placeholder="必填字段 1~10个字"/> 
				</span>
			</p>

			<p>
				<label>分类</label>
				<span class="field"> <%-- class="radius3" style="margin-left:0px; width:370px" --%> 
					<select id="article-type-id" name="articleTypeId" class="ae-form-input" style="min-width: 31%;" >
					</select>
				</span>
			</p>
			
			<p>
				<label>原文链接</label> 
				<span class="field"> 
					<input type="text" name="outUrl" id="outUrl" class="ae-form-input" value="${model.outUrl }" maxlength="200"/>
				</span>
			</p>
		</form>
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
			</div>

		</div>
	</div>
	<br /> 
	<a style="margin-left: 220px;"> 
		<button style="margin-bottom:100px"  onclick="addInfo('03')" class="stdbtn btn_orange">保存到草稿箱 </button>
	</a>
	<a style="margin-left: 220px ;"> 
		<button style="margin-bottom:100px" onclick="addInfo('01')" class="stdbtn btn_orange">提交到待发布</button>
	</a>
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



