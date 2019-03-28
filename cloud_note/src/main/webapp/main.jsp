<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>尚云主页</title>
	
	<%@include file="common.jsp" %>
	
	<style type="text/css">
	  body {
	       padding-top: 60px;
	       padding-bottom: 40px;
	       background: url(statics/images/bg.gif) repeat;
	     }
	</style>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" style="font-size:25px" href="http://localhost:8080/cloudnote/main">尚云笔记</a>
    </div>
    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li class="active"><a href="main"><i class="glyphicon glyphicon-cloud"></i>&nbsp;主&nbsp;&nbsp;页</a></li>
        <li><a href="note?action=view"><i class="glyphicon glyphicon-pencil"></i>&nbsp;发表云记</a></li>
        <li><a href="type?action=list"><i class="glyphicon glyphicon-list"></i>&nbsp;类别管理</a></li>
        <li><a href="user?action=userCenter"><i class="glyphicon glyphicon-user"></i>&nbsp;个人中心</a>
       
      </li></ul>
      <form class="navbar-form navbar-right" role="search" action="main">
        <div class="form-group">
          <input type="hidden" name="action" value="searchKey">
          <input type="text" name="title" class="form-control" placeholder="搜索云记" value="${title }">
        </div>
        <button type="submit" class="btn btn-default">查询</button>
      </form>      
    </div>
  </div>
</nav>
<div class="container">
	<div class="row-fluid">
		<div class="col-md-3">
			<div class="data_list">
				<div class="data_list_title"><span class="glyphicon glyphicon-user"></span>&nbsp;个人中心&nbsp;&nbsp;&nbsp;&nbsp;<a href="user?action=logout">退出</a></div>
				<div class="userimg">
					<img src="user?action=userHead&fn=${user.head }">
				</div>
				<div class="nick">${user.nick}</div>
				<div class="mood">(${user.mood})</div>
			</div>	
			<div class="data_list">
				<div class="data_list_title">
					<span class="glyphicon glyphicon-calendar">
					</span>&nbsp;云记日期 
				</div>
				
				<div>
					<ul class="nav nav-pills nav-stacked">
					 	<c:if test="${!empty dateInfo && dateInfo.size() >0 }">
					 		<c:forEach items="${dateInfo }" var="item">
								<li><a href="main?action=searchDate&dateStr=${item.name }">${item.name } <span class="badge">${item.noteCount }</span></a></li>
					 		</c:forEach>
					 	</c:if>
					 	<c:if test="${empty dateInfo || dateInfo.size() <1 }">
					 		<h4>暂未查询到数据！</h4>		
					 	</c:if>
					</ul>						
				</div>
				
			</div>		
			<div class="data_list">
				<div class="data_list_title">
					<span class="glyphicon glyphicon-list-alt">
					</span>&nbsp;云记类别 
				</div>
				
				<div>
					<ul class="nav nav-pills nav-stacked" id="myUl">
						<c:if test="${!empty typeInfo && typeInfo.size() >0 }">
					 		<c:forEach items="${typeInfo }" var="item">
					 			<li id="li_${item.typeId }"><a href="main?action=searchType&typeStr=${item.typeId }"><span id="sp_${item.typeId }">${item.name }</span><span class="badge">${item.noteCount }</span></a></li>
					 		</c:forEach>
					 	</c:if>
					 	<c:if test="${empty typeInfo || typeInfo.size() <1 }">
					 		<h4>暂未查询到数据！</h4>		
					 	</c:if>					 
					</ul>						
				</div>
				
			</div>			
		</div>
	</div>
	
	<!-- 动态改变的页面 -->
	<div class="col-md-9">
		<%-- <jsp:include page="user/info.jsp"></jsp:include> --%>
		<%-- <jsp:include page="noteType/list.jsp"></jsp:include> --%>
		<jsp:include page="${changePage }"></jsp:include>
	</div>		
	
	
</div>
