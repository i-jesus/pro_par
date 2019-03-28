<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh">
<div class="data_list">
	<div class="data_list_title">
		<span class="glyphicon glyphicon-list"></span>&nbsp;类型列表
		<span class="noteType_add">
			<button class="btn btn-sm btn-success" type="button" id="addBtn" data-toggle="modal" data-target="#myModal">添加类别</button>
		</span>
		
	 </div>
	 
	<div id="myDiv">
	
	<c:if test="${resultInfo.code == 0 }">
		<h2>${resultInfo.msg }</h2>
	</c:if>
	<c:if test="${resultInfo.code == 1 }">
	 	<table class="table table-hover table-striped" id="myTable">
		 		<tr>
		 			<th>编号</th>
		 			<th>类型</th>
		 			<th>操作</th>
		 		</tr>
		 		
		 		<c:forEach items="${resultInfo.result }" var="item">
		 			<tr id="tr_${item.typeId }">
			 			<td>${item.typeId }</td>
			 			<td>${item.typeName }</td>
			 			<td>
			 			<button class="btn btn-primary" type="button" onclick="updateType(${item.typeId },'${item.typeName }')" >修改</button>&nbsp;
			 			<button class="btn btn-danger del" type="button" onclick="deleteType(${item.typeId },'${item.typeName }')">删除</button>
			 			</td>
			 		</tr>
		 		</c:forEach>
		 		
		</table>
	</c:if>
	
	</div>	
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">×</span></button>
          <h4 class="modal-title" id="myModalLabel">新增</h4>
        </div>
        <div class="modal-body"> 
          <div class="form-group">
            <label for="typename">类型名称</label>
            <input type="hidden" name="typeId" id="typeId" value=""> 
            <input type="text" name="typename" class="form-control" id="typename" placeholder="类型名称" value="">         	
          </div>      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">
          <span class="glyphicon glyphicon-remove"></span>关闭</button>
          <button type="button" id="btn_submit" class="btn btn-primary">
          <span class="glyphicon glyphicon-floppy-disk"></span>保存</button>
        </div>
      </div>
    </div>
  </div>
    
  <script type="text/javascript" src="statics/js/notetype.js"></script>
  
</html>