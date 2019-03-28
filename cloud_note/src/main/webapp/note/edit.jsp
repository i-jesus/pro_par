<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发布云记</title>
</head>
<body>

<div class="data_list">
	<div class="data_list_title">
		<span class="glyphicon glyphicon-cloud-upload"></span>&nbsp;修改云记
	</div>
	
	<c:if test="${resultInfo.code == 0 }">
		<h2>确保云记类别存在，才能发布云记!</h2>
		<h4><a href="type?action=list">添加云记类别</a></h4>
	</c:if>
	
	<c:if test="${resultInfo.code == 1 }">
		<div class="container-fluid">
			<div class="container-fluid">
			  <div class="row" style="padding-top: 20px;">
			  	<div class="col-md-12">
			  		<form class="form-horizontal" method="post" action="note">
			  		   <div class="form-group">
					    <label for="typeId" class="col-sm-2 control-label">类别:</label>
					    <div class="col-sm-6">
					    	<select id="typeId" class="form-control" name="typeId" >
								<option value="">请选择云记类别...</option>
								<c:forEach items="${resultInfo.result }" var="item">
									<%-- <option value="${item.typeId }" <c:if test="${item.typeId == noteInfo.typeId }">selected</c:if> >${item.typeName }</option> --%>
									<c:choose>
										<c:when test="${item.typeId == noteInfo.typeId }">
											<option value="${item.typeId }" selected>${item.typeName }</option>
										</c:when>
										<c:otherwise>
											<option value="${item.typeId }" >${item.typeName }</option>
										</c:otherwise>
									</c:choose>
									
								</c:forEach>
							</select>
					    </div>
					  </div>
					  <div class="form-group">
					  	<input type="hidden" name="noteId" value="${noteInfo.noteId }">
					  	<input type="hidden" name="action" value="edit">
					    <label for="title" class="col-sm-2 control-label">标题:</label>
					    <div class="col-sm-10">
					      <input class="form-control" name="title" id="title" placeholder="云记标题" value="${noteInfo.title }">
					    </div>
					   </div>
					  
					  <div class="form-group">
					    <div class="col-sm-12">
					    	<!-- 加载富文本框 -->
					    	<textarea id="noteEditor" name="content">${noteInfo.content }</textarea>
					    </div>
					  </div>			 
					  <div class="form-group">
					    <div class="col-sm-offset-6 col-sm-4">
					      <input type="submit" class="btn btn-primary" onclick="return saveNote();" value="保存">
							<font id="error" color="red"></font>  
					    </div>
					  </div>
					</form>
			  	</div>
			  </div>
			</div>	
		</div>	
		<script type="text/javascript">
		var ue;
		$(function(){
			/* ue = UE.getEditor('noteEditor'); */
			ue = new UE.ui.Editor({initialFrameHeight:'300',initialFrameWidth:'100%'});  
		    ue.render("noteEditor"); 
		    
			//对编辑器的操作最好在编辑器ready之后再做
			ue.ready(function() {
			    //设置编辑器的内容
			    //ue.setContent('hello');
			    //获取html内容，返回: <p>hello</p>
			    var html = ue.getContent();
			    console.log(html);
			    //获取纯文本内容，返回: hello
			    var txt = ue.getContentTxt();
			    console.log(txt);
			});
		    
		 });
		 
		//验证
		function saveNote(){
			//验证非空
			// 得到下拉框的值
			var typeId = $("#typeId").val();
			if (typeId == null || typeId.trim() == "") {
				$("#error").html("请选择云记类型！");
				return false;
			}
			// 得到标题
			var title = $("#title").val();
			if (title == null || title.trim() == "") {
				$("#error").html("云记标题不能为空！");
				return false;
			}
			// 得到富文本框的值（html内容）
			var content = ue.getContent();
			console.log(content);
			if (content == null || content.trim() == "") {
				$("#error").html("云记内容不能为空！");
				return false;
			}
			return true;
		}
	</script>
	</c:if>	
</div>

</body>
</html>