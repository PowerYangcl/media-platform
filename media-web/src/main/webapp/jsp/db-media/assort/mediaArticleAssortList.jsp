<%@ include file="/inc/resource.inc" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 <%@ include file="/inc/iframe-head.jsp" %>

<div class="centercontent tables">
	<!--这个跳转页面的功能及跳转路径等等-->
	<div class="pageheader notab">
		<h1 class="pagetitle">文章分类管理</h1>
		<span style="display: none">jsp/center-manager/assort/mediaArticleAssortList.jsp</span>
	</div>

	<div id="contentwrapper" class="contentwrapper">

		<%-- table-form 这个id分页使用 --%>
		<div id="table-form" class="dataTables_wrapper">
			<div class="contenttitle2">
				<p style="margin: 0px">
					<label>分类名称：</label> 
					<span class="field"> 
						<input id="name" type="text" name="name" class="form-search" />
					</span> 
					
					<a onclick="showAddDialog()" class="btn btn_orange btn_link radius50" style="float: right; cursor: pointer; margin-left: 10px"> 
						<span> 添 加 </span>
					</a>
					<a onclick="searchReset()" class="btn btn_orange btn_search radius50" style="float: right; cursor: pointer; margin-left: 10px"> 
						<span> 重 置 </span>
					</a> 
					<a onclick="searchAssort()" class="btn btn_orange btn_search radius50" style="float: right; cursor: pointer; margin-left: 20px"> 
						<span> 查 询 </span>
					</a>
				</p>
			</div>

			<div id="dyntable2_length" class="dataTables_length dialog-show-count">
				<label> 当前显示 <%-- TODO 注意：select-page-size 这个ID是写定的，如果没有这个显示条数，则默认显示10条 - Yangcl --%> 
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
						<th class="head0">分类</th>  
						<th class="head0">最后修改人</th> 
						<th class="head0">最后修改时间</th>
						<th class="head0">文章数量</th>
						<th class="head0 " width="150px">操作</th>
					</tr>
				</thead>
				<tbody id="ajax-tbody-1" class="page-list"><!--  class="page-list" 标识是页面数据列表 行变色使用 -->
					<%-- 等待填充 --%>
				</tbody>
			</table>

		</div>
	</div>

</div>

<script type="text/javascript">

      /**
       * Ajax 页面分页示例
       * $ { basePath } 这个是必填的，单纯使用“media/ajax_page_data.do” 会404 
       *
       * var data_ = null; 这里暂设置为null，这两处为空的地方可以根据实际情况处理。注意loadTable()也有。
       */
       $(function(){
      	 // aForm.formPaging('$ { num }'); 如果从修改页面返回到列表页面，则可以进入这段代码 直接定位。
           var type_ = 'post';
           var url_ = '${basePath}media/ajax_article_assort_list.do';
           var data_ = null;  // 可以为null，后台会进行默认处理
           var obj = JSON.parse(ajaxs.sendAjax(type_ , url_ , data_));
           aForm.launch(url_ , 'table-form' , obj).init().drawForm(loadTable);
       });

       // 回调函数
       function loadTable(url_){
           if(url_ == undefined){ // 首次加载表单
               draw(aForm.jsonObj);
               return;
           }
           // 这种情况是响应上一页或下一页的触发事件
           var type_ = 'post';
           var data_ = {
               name: $("#name").val()
           };
           var obj = JSON.parse(ajaxs.sendAjax(type_ , url_ , data_));
           aForm.launch(url_ , 'table-form' , obj).init();
           draw(obj);
       }

       // 画表格
       function draw(obj){
           $('#ajax-tbody-1 tr').remove();
           var html_ = '';
           var list = obj.data.list;
           if(list.length>0){
            for(var i = 0 ; i < list.length ; i ++){
                html_ += '<tr id="tr-' + list[i].id + '" class="gradeX">'
                +'<td width="150px"  class="assort-name">' + list[i].name + '</td>'
                +'<td>' + list[i].userName + '</td>'
                +'<td>' + list[i].updateTime + '</td>'
                +'<td class="center article-sum">' + list[i].articleSum + '</td>'
                +'<td width="150px" align="center">'
                +'<a href="javascript:void(0)" onclick="showEditDialog(this)" assortId="' + list[i].id + '" title="修改"  style="cursor: pointer;">修改</a>  | '  
                +'<a onclick="deleteAssort(this)" assortId="' + list[i].id + '" title="删除"  style="cursor: pointer;">删除</a> '
                +'</td></tr>'
            }
           }else{
           	html_='<tr><td colspan="11" style="text-align: center;">'+obj.msg+'</td></tr>';
           }
           
           $('#ajax-tbody-1').append(html_);
       }
      

      //搜索
      function searchAssort(){
          aForm.formPaging(0);
      }

      // 重置查询条件
      function searchReset(){
          $("#name").val(""); 
          aForm.formPaging(0);
      }
      
      // 显示添加弹窗
  	  function showAddDialog(){
    	  $("#dialog-name").val("");
    	  $("#title-content").text("新建分类"); 
    	  $("#dialog-submit").bind('click', addAssort);
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
  	  
      // 提交要添加的内容  
  	  function addAssort(){
  		  var type_ = 'post';
          var url_ = '${basePath}media/ajax_add_assort.do';
          var data_ = $("#dialog-table").serializeArray();
          var obj = JSON.parse(ajaxs.sendAjax(type_ , url_ , data_));
          if(obj.status == 'success'){
              var currentPageNumber = $(".paginate_active").html();   // 定位到当前分页的页码，然后重新加载数据
              aForm.formPaging(currentPageNumber);
          }
  	  }
      
      
  	  // 显示修改弹窗
  	  function showEditDialog(obj){
  		  var name = $(obj).parent().parent().children(".assort-name")[0].innerText;
    	  $("#dialog-name").val(name);
    	  $("#title-content").text("编辑分类");
    	  $("#dialog-submit").bind('click', {id_:$(obj).attr("assortId") , name_:name}, editAssort);  
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
  	  
  	  // 提交要修改的内容  
  	  function editAssort(event){
		  var type_ = 'post';
		  var url_ = '${basePath}media/ajax_edit_assort.do';
		  var data_ = $("#dialog-table").serializeArray();
		  var o = new Object();
		  o.name = "id";
		  o.value = event.data.id_;
		  data_.push(o);
		  var obj = JSON.parse(ajaxs.sendAjax(type_ , url_ , data_));
          if(obj.status == 'success'){
              var currentPageNumber = $(".paginate_active").html();   // 定位到当前分页的页码，然后重新加载数据
              aForm.formPaging(currentPageNumber);
          }
  	  }

	  function closeDialog(){
          $.unblockUI();
      }
	  
	  
	function deleteAssort(ele) {
		var count = $(ele).parent().parent().children(".article-sum")[0].textContent;
		if(count != "0"){
			var name = $(ele).parent().parent().children(".assort-name")[0].textContent;
			jAlert("【" + name + "】分类下已经有文章，不可以删除" , '系统提示');
		}else{
			jConfirm('您确定要删除这条记录吗？', '系统提示', function(flag) {
				if (flag) {
					var type_ = 'post';
					var url_ = '${basePath}media/ajax_delete_assort.do'; 
					var data_ = new Array();
					var o = new Object();
					o.name = "id";
					o.value = $(ele).attr("assortId");
					data_.push(o);
					var obj = JSON.parse(ajaxs.sendAjax(type_, url_, data_));
					if (obj.status == 'success') {
						var currentPageNumber = $(".paginate_active").html(); // 定位到当前分页的页码，然后重新加载数据
						aForm.formPaging(currentPageNumber);
					} else {
					}
					jAlert(obj.msg, '系统提示');
				}
			});
		}
	}
</script>
<div id="add-dialog-div" class="dialog-page-div" style="display: none;width: 400px;height: 150px">
    <p class="dialog-title">	
        <a href="#" onclick="closeDialog()" class="dialog-close"></a>
        <span id="title-content"></span>
    </p>
    <div id="dialog-content-wrapper" class="contentwrapper">
        <div id="dialog-table-form" class="dataTables_wrapper" >
            <form id="dialog-table" >
	            <table class="dialog-table">
	                <tbody>
	                	<tr >
	                		<td style="text-align: right">
	                			分类名称：
	                		</td>
	                		<td style="text-align: left">
	                			<input id="dialog-name" type="text" name="name" class="dialog-form-input" style="width:200px;"/>
	                		</td>
	                	</tr>
	                </tbody>
	                <tfoot>
		                <tr>
					      <td colspan="2" style="text-align: right">
					      	    <button id="dialog-submit" class="stdbtn btn_orange" onclick="" style="opacity:1">提 &nbsp&nbsp&nbsp&nbsp&nbsp 交</button>
					      </td> 
					    </tr>
	                </tfoot>
	            </table>
            </form>
        </div>
    </div>
</div>
























