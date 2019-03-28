<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>云记列表</title>
</head>
<body>


	<div class="data_list">
		<div class="data_list_title"><span class="glyphicon glyphicon glyphicon-th-list"></span>&nbsp;
		云记列表 </div>
			
			<c:if test="${resultInfo.code == 0 }">
				<h4>${resultInfo.msg }</h4>
			</c:if>
			<c:if test="${resultInfo.code == 1 }">
				
				<div class="note_datas">
					<ul>
						<c:forEach items="${resultInfo.result.datas }" var="item">
							<li>『 ${item.pubtime }』&nbsp;&nbsp;<a href="note?action=detail&noteId=${item.noteId }">${item.title }</a> </li>
						</c:forEach>
					</ul>
				</div>
				
				
				<nav style="text-align: center">
				  <ul class="pagination center">
		    			<c:if test="${resultInfo.result.pageNum != 1 }">
		                    <li>
		                    	<a href="main?pageNum=1&action=${action }&title=${title}&dateStr=${dateStr}&typeStr=${typeStr}" class="active">首页</a>
		                    </li>
	                    </c:if>
	                    <c:if test="${resultInfo.result.pageNum > 1 }">
		                    <li>
		                        <a href="main?pageNum=${resultInfo.result.pageNum -1 }&action=${action }&title=${title}&dateStr=${dateStr}&typeStr=${typeStr}">上一页</a>
		                    </li>
	                    </c:if>
	                    
	                    <c:forEach begin="${resultInfo.result.navStartPage }" end="${resultInfo.result.navEndPage }" var="p">
		                    <li <c:if test="${resultInfo.result.pageNum == p }">class="active"</c:if> >
		                    	<a href="main?pageNum=${p}&action=${action }&title=${title}&dateStr=${dateStr}&typeStr=${typeStr}">${p }</a>
		                    </li>
	                    </c:forEach>
	                    
	                    
	                    <c:if test="${resultInfo.result.pageNum < resultInfo.result.totalPages }">
		                    <li>
		                    	<a href="main?pageNum=${resultInfo.result.pageNum + 1 }&action=${action }&title=${title}&dateStr=${dateStr}&typeStr=${typeStr}">下一页</a>
		                    </li>
	                    </c:if>
	                    <c:if test="${resultInfo.result.pageNum != resultInfo.result.totalPages }">
		                    <li>
		                        <a href="main?pageNum=${resultInfo.result.totalPages }&action=${action }&title=${title}&dateStr=${dateStr}&typeStr=${typeStr}" class="active">末页</a>
		                    </li>
	                    </c:if>
				  </ul>
				</nav>
		</c:if>
	</div>


</body>
</html>