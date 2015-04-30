<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/bootstrap/js/modal.js"></script>
<link rel="stylesheet" href="${ctx}/bootstrap/css/bootstrap.min.css">
<script type="text/javascript">

	function deleteUser(id) {
		var del = confirm('你确定要删除该用户？');
		if (del) {
			$.ajax({
	            type:"delete",// 指定方法的类型
	            dataType:"text",
	            url: '${ctx}/rest/user/delete/1',
	            success: function(msg){
	            	//$('#myModal').modal('show');
	            	window.location.reload();
	            },
	            error: function (msg) {
	                alert(msg.responseText);
	            }
	        });
		}
	}
	
	function updateUser() {
		var del = confirm('你确定要更新该用户？');
		if (del) {
			$.ajax({
	            type:"put",// 指定方法的类型
	            dataType:"text",
	            contentType: 'application/json',
	            data : '{"id":"22","name":"yinleiaaa"}',
	            url: '${ctx}/rest/user/update',
	            success: function(msg){
	            	//$('#myModal').modal('show');
	            	window.location.reload();
	            },
	            error: function (msg) {
	                alert(msg.responseText);
	            }
	        });
		}
	}

</script>
</head>
<body>

&nbsp;&nbsp;&nbsp;&nbsp;<button name="deletes" onclick="deleteUser(1)">删除</button>
&nbsp;&nbsp;&nbsp;&nbsp;<button name="updates" onclick="updateUser()">更新</button>
</body>
</html>