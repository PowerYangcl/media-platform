<%@ include file="/inc/resource.inc" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/head.jsp" %>
		<script type="text/javascript">
			function iframeChange(){
				var lw = $("#left-menu div:visible")[0]; // 取左侧菜单的宽度
				var toph = $(".header")[0].offsetHeight + $(".topheader")[0].offsetHeight;  // 取顶部高 
				var ifm= document.getElementById("sub-page"); 
			    ifm.height = document.documentElement.clientHeight - toph;
			    ifm.width = document.documentElement.clientWidth - lw.clientWidth;
			}
			window.onresize=function(){  
				iframeChange();  
			} 
		</script>
	</head>

	<body class="withvernav" style="overflow-x:hidden">
	
	    <div class="bodywrapper">
			<%@ include file="/inc/top.jsp" %>
			<%@ include file="/inc/left.jsp" %>
			<iframe style="margin-left:231px;overflow-x:hidden" id="sub-page"  frameborder="0" onload="iframeChange()" src="${basePath}manager/page_manager_index.do" ></iframe>
	    </div>
	
	</body>
</html>










