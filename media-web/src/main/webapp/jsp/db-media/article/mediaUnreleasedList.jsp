<%@ include file="/inc/resource.inc" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/inc/iframe-head.jsp" %>
<script type="text/javascript">
	$(function() {
		var type_ = 'post';
		var url_ = '${basePath}media/ajax_article_list.do';
		var data_ = {
			releaseType : '01'
		};  
		var obj = JSON.parse(ajaxs.sendAjax(type_, url_, data_));
		aForm.launch(url_, 'table-form', obj).init().drawForm(loadTable);
		var arr = obj.atlist;
		if (arr.length != 0) {
			var html_ = '';
			for (var i = 0; i < arr.length; i++) {
				html_ += '<option value="' + arr[i].id + '">' + arr[i].name + '</option>';
			}
			$("#article-type-id").append(html_);
		}
	});

	// 回调函数
	function loadTable(url_) {
		if (url_ == undefined) { // 首次加载表单
			draw(aForm.jsonObj);
			return;
		}
		// 这种情况是响应上一页或下一页的触发事件
		var type_ = 'post';
		var data_ = {
			releaseType : '01',
			title : $("#title").val(),
			author : $("#author").val(),
			editor : $("#editor").val(),
			articleTypeId : $("#article-type-id").val()
		};
		var obj = JSON.parse(ajaxs.sendAjax(type_, url_, data_));
		aForm.launch(url_, 'table-form', obj).init();
		draw(obj);
	}

	// 画表格
	function draw(obj) {
		$('#ajax-tbody-1 tr').remove();
		var html_ = '';
		var list = obj.data.list;
		if (list.length > 0) {
			for (var i = 0; i < list.length; i++) {
				var toReleaseSource = ""; // 待发布文章的来源
				if(list[i].toReleaseSource == '02'){
					toReleaseSource = "已发布撤回"; 
				}if(list[i].toReleaseSource == '03'){
					toReleaseSource = "草稿箱提交"; 
				}else if(list[i].toReleaseSource == '04'){
					toReleaseSource = "回收站还原"; 
				}
				
				html_ += '<tr id="tr-' + list[i].id + '" class="gradeX">'
						+ '<td>' + list[i].title + '</td>'
						+ '<td width="80px"><img src="' + list[i].titlePic + '" height="70" width="70"/></td>'
						+ '<td >' + list[i].source + '</td>'
						+ '<td >' + list[i].author + '</td>'
						+ '<td width="100px">' + list[i].createTime + '</td>'
						// + '<td >' + list[i].editor + '</td>'
						// + '<td width="100px">' + list[i].releaseTime + '</td>'
						+ '<td >' + list[i].assort + '</td>'
						+ '<td>' + toReleaseSource + '</td>'						
						+ '<td width="100px">'
							+ '<div>阅读数：' + list[i].readerCount + '</div>'
							+ '<div>点赞数：' + list[i].thumbsUpCount + '</div>'
							+ '<div>分享数：' + list[i].shareCount + '</div>'
						+ '</td>'
						+ '<td width="150px" align="center">'
							+ '<a href="${basePath}media/media_article_edit_page_unrelease.do?id='+list[i].id+'" articleId="' + list[i].id + '"  title="点击编辑文章"  style="cursor: pointer;">编辑</a> | '
							+ '<a href="${basePath}media/media_article_preview_page.do?id='+list[i].id+'" target="_blank" articleId="' + list[i].id + '"  title="点击此处预览文章"  style="cursor: pointer;">预览</a> </br>'
							+ '<a onclick="articleStatusChange(this)" articleId="' + list[i].id + '"  releaseType="04" title="删除文章到回收站"  style="cursor: pointer;">删除</a> | '
							+ '<a onclick="articleStatusChange(this)" articleId="' + list[i].id + '"  releaseType="02" title="发布文章到已发布列表"  style="cursor: pointer;">发布</a> | '
							+ '<a onclick="articleReject(this)" articleId="' + list[i].id + '"  releaseType="03" title="驳回文章到草稿箱"  style="cursor: pointer;">驳回</a> '
						+ '</td></tr>'
			}
		} else {
			html_ = '<tr><td colspan="11" style="text-align: center;">' + obj.msg + '</td></tr>';
		}
		$('#ajax-tbody-1').append(html_);
	}

	function articleStatusChange(ele) {
		var title_ = $(ele).attr("title");
		jConfirm('您确定要' + title_ + '吗？', '系统提示', function(flag) {
			if (flag) {
				var type_ = 'post';
				var url_ = '${basePath}media/ajax_article_update.do';
				var data_ = {
					id : $(ele).attr("articleId"),
					releaseType : $(ele).attr("releaseType")
				}; 
				var obj = JSON.parse(ajaxs.sendAjax(type_, url_, data_));
				if (obj.status == 'success') {
					var currentPageNumber = $(".paginate_active").html(); // 定位到当前分页的页码，然后重新加载数据
					aForm.formPaging(currentPageNumber);
				}
				jAlert(obj.msg, '系统提示');
			}
		});
	}
	
	// 驳回 
	function articleReject(ele) {
		var title_ = $(ele).attr("title");
		jPrompt( '请输入驳回原因：' , '', '您确定要' + title_ + '吗？' , function(content) {
			if (content != null && content != '' ) { 
				var type_ = 'post';
				var url_ = '${basePath}media/ajax_article_update.do';
				var data_ = {
					id : $(ele).attr("articleId"),
					releaseType : $(ele).attr("releaseType"),
					recycleBinSource : '02' ,
					remark : content
				};
				
				var obj = JSON.parse(ajaxs.sendAjax(type_, url_, data_));
				if (obj.status == 'success') {
					var currentPageNumber = $(".paginate_active").html(); // 定位到当前分页的页码，然后重新加载数据
					aForm.formPaging(currentPageNumber);
				}
				jAlert(obj.msg, '系统提示');
			}else if (content == '') {
				jAlert('请输入驳回原因' , '系统提示');  
			}
		});
		
	}

	//搜索
	function searchUser() {
		aForm.formPaging(0);
	}

	// 重置查询条件
	function searchReset() {
		$(".form-search").val("");
		aForm.formPaging(0);
	}
</script>

<div class="centercontent tables">
	<!--这个跳转页面的功能及跳转路径等等-->
	<div class="pageheader notab">
		<h1 class="pagetitle">待发布文章列表</h1>
		<span style="display: none">jsp/center-manager/assort/mediaReleasedList.jsp</span>
	</div>

	<div id="contentwrapper" class="contentwrapper">

		<%-- table-form 这个id分页使用 --%>
		<div id="table-form" class="dataTables_wrapper">
			<div class="contenttitle2">
				<p style="margin: 0px"> 
					<label>标题：</label> 
					<span class="field"> 
						<input id="title" type="text" name="title" class="form-search" />
					</span> 
					
					<label>作者：</label> 
					<span class="field"> 
						<input id="author" type="text" name="author" class="form-search" />
					</span> 
					
					<label>编辑人：</label> 
					<span class="field"> 
						<input id="editor" type="text" name="editor" class="form-search" />
					</span> 
					
					<label>分类：</label> 
					<span class="field"> 
						<select id="article-type-id" name="articleTypeId" class="form-search">
							<option value="">请选择---</option>
						</select>
					</span> 
					
					<a onclick="searchReset()" class="btn btn_orange btn_search radius50" style="float: right; cursor: pointer; margin-left: 10px"> 
						<span> 重 置 </span>
					</a> 
					<a onclick="searchUser()" class="btn btn_orange btn_search radius50" style="float: right; cursor: pointer; margin-left: 20px"> 
						<span> 查 询 </span>
					</a>
				</p>
			</div>

			<div id="dyntable2_length" class="dataTables_length dialog-show-count">
				<label> 当前显示 
					<select id="select-page-size" size="1" name="dyntable2_length" onchange="aForm.formPaging('1')">
						<option value="10">10</option>
						<option value="25">25</option>
						<option value="50">50</option>
						<option value="100">100</option>
					</select> 条记录
				</label>
			</div>

			<table id="dyntable2" cellpadding="0" cellspacing="0" border="0" class="stdtable">
				<thead>
					<tr>
						<th class="head0">标题</th> 
						<th class="head0"  width="80px">标题小图</th> 
						<th class="head0">文章来源</th> 
						<th class="head0">作者</th>
						<th class="head0" width="100px">创建时间</th>
						<!-- <th class="head0">编辑人</th>
						<th class="head0" width="100px">发布时间</th> -->
						<th class="head0">分类</th>
						<th class="head0">状态</th>
						<th class="head0" width="100px">访问量</th> 
						<th class="head0 " width="100px">操作</th>
					</tr>
				</thead>

				<tbody id="ajax-tbody-1" class="page-list">
					<!--  class="page-list" 标识是页面数据列表 行变色使用 -->
					<%-- 等待填充 --%>
				</tbody>
			</table>

		</div>
	</div>

</div>



























