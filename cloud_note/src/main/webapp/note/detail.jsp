<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>


<div class="data_list">
	<div class="data_list_title">
		<span class="glyphicon glyphicon-eye-open"></span>&nbsp;查看云记 
	</div>
	<div>
			
		<div class="note_title"><h2>${resultInfo.result.title }</h2></div>
		<div class="note_info">
			发布时间：『 ${resultInfo.result.pubtime }』&nbsp;&nbsp;云记类别：${resultInfo.result.typeName }
		</div>
		<div class="note_content">
			<p>${resultInfo.result.content }</p>
		</div>
		<div class="note_btn">
			<button class="btn btn-primary" type="button" onclick="update(${resultInfo.result.noteId })">修改</button>
			<button class="btn btn-danger" type="button" onclick="deleteNote(${resultInfo.result.noteId })">删除</button>
		</div>			
	</div>	
</div>

<script type="text/javascript">
// 跳转到修改页面
function update(noteId){
	window.location.href = "note?action=view&noteId="+noteId;
}

// 删除云记
function deleteNote(noteId){
	//加入模态框
	swal({
	  title: '确认删除',
	  text: '是否删除该云记？',
	  type: 'warning',
	  showCancelButton: true,
	  confirmButtonText: '狠心踹脚',
	  cancelButtonText: '考虑一下'
	}).then(function() {
		window.location.href = "note?action=delete&noteId=" + noteId;
	});
}
</script>

</body>
</html>