/**
 * 点击添加按钮
 */
$("#addBtn").click(function(){
	// 设置模态框的标题
	$("#myModalLabel").html("新增类型");
	// 设置按钮的文本
	$("#btn_submit").html("<span class='glyphicon glyphicon-floppy-disk'></span>添加");
	// 清空typeId和typeName
	$("#typeId").val("");
	$("#typename").val("");
});

/**
 * 点击修改按钮
 * @param typeId
 * @param typeName
 */
function updateType(typeId, typeName) {
	// 设置模态框的标题
	$("#myModalLabel").html("修改类型【"+typeName+"】");
	// 设置按钮的文本
	$("#btn_submit").html("<span class='glyphicon glyphicon-floppy-disk'></span>修改");
	// 设置typeId和typeName
	$("#typeId").val(typeId);
	$("#typename").val(typeName);
	
	// 打开模态框
	$("#myModal").modal("show");
}


/**
 * 保存按钮
 */
$("#btn_submit").click(function(){
	// 类型名称
	var typename = $("#typename").val();
	if (typename == null || typename.length < 1) {
		swal("","类型名称不能为空！","warning");
		return;
	}
	// 修改操作时，可以得到typeId
	var typeId = $("#typeId").val();
	
	// 验证类型名称是否可用
	var data = {"action":"checkTypeName",typeId:typeId,typeName:typename};
	$.getJSON("type",data,function(data){
		if (data.code == 1) { // 可用
			// 添加或者修改操作
			addOrUpdate(typeId,typename);
			
		} else { // 不可用
			swal("",data.msg,"warning");
			return;
		}
	});
	
});

/**
 * 添加或者修改
 * @param data
 */
function addOrUpdate(typeId,typeName) {
	// 发送ajax请求，做添加或修改操作
	var data = {"action":"addOrUpdate",typeId:typeId,typeName:typeName};
	$.getJSON("type",data, function(result){ // resultInfo
		if (result.code == 1) { // 成功
			// 关闭模态框
			$("#myModal").modal("hide");
			swal("",result.msg,"success");
			console.info(result);
			// 判断是添加操作还是修改，如果是添加操作，就在table加一行
			if (typeId == null || typeId == "") {
				// 添加成功后，动态给表格添加一行记录
				addTr(result.result.typeId,typeName);
				// 添加成功之后，动态给左侧的云记类别分组栏加一条记录
				addli(result.result.typeId,typeName);
			} else { // 如果是修改操作，修改tr中的typeName值
				// 定位到修改的tr对象
				var tr = $("#tr_"+typeId);
				console.log(tr);
				// tr的子节点是td，eq是去匹配具体的单元格，下标从0开始，1代表是typeName的那个单元格，text是赋值
				tr.children().eq(1).text(typeName);
				
				// 动态修改左侧的云记类别分组栏
				$("#sp_"+typeId).html(typeName);
			}
			
			return;
		} else { // 失败
			swal("",result.msg,"error");
			return;
		}
	});
}

/*
 * DOM操作
 * 添加/修改成功后，动态给表格添加一行记录
 */
function addTr(typeId,typeName){
	// 得到操作的tr对象
	var tr = "<tr id='tr_"+typeId+"'><td>"+typeId+"</td>" +
			"<td>"+typeName+"</td> <td> " +
			"<button class='btn btn-primary' type=\"button\"  onclick=\"updateType("+typeId+",'"+typeName+"')\">修改</button>&nbsp;" +
			"<button class='btn btn-danger del' type=\"button\" onclick=\"deleteType("+typeId+",'"+typeName+"')\">删除</button> </td></tr>";
	console.log(tr);
	// 得到table对象
	var table = $("#myTable");
	// 判断table是否存在
	if (table.length > 0) { // 存在
		// 将tr对象添加到table的最后面
		table.append(tr);
	} else { // 如果table不存在，需要新建table，并将<h2>标签移除，再将table放到指定位置
		// 创建table对象
		table = "<table class='table table-hover table-striped' id='myTable'>" +
				"<tr><th>编号</th><th>类型</th><th>操作</th></tr>" + tr +
				"</table>";
		// 得到h2标签
		var h2 = $("h2");
		// 将h2标签从div中移除
		h2.remove();
		// 得到table存放的位置
		var div = $("#myDiv");
		// 将table加到div中
		div.append(table);
	}
	
}


/*
 * 添加/修改成功之后，动态给左侧的云记类别分组栏加一条记录
 */
function addli(typeId,typeName){
	// 得到li对象
	var li = "<li id='li_"+typeId+"'><a href=''><span id='sp_"+typeId+"'>" + typeName + "</span><span class='badge'>0</span></a></li>";
	// 得到li放置的ul对象
	var ul = $("#myUl");
	// 如果没有li对象时，需要先移除提示信息h4标签
	var h4 = $("h4");
	// 如果h4标签存在就移除
	if (h4.length > 0) {
		h4.remove();
	}
	// 将li添加到ul最后面
	ul.append(li);
}


/**
 * 删除类型
 * @param typeId
 * @param typeName
 */
function deleteType(typeId,typeName){
	//加入模态框
	swal({
	  title: '确认删除',
	  text: '是否删除【'+typeName+"】?",
	  type: 'warning',
	  showCancelButton: true,
	  confirmButtonText: '狠心踹脚',
	  cancelButtonText: '考虑一下'
	}).then(function() {
		// 存在子记录，不能删除
		
		// ajax请求删除数据
		$.getJSON("type",{action:"deleteType",typeId:typeId},function(data){
			if (data.code == 1) { // 删除成功
				swal("",data.msg,"success");
				// 左侧菜单栏对应的类型删除
				$("#li_"+typeId).remove();
				
				// 删除记录以后，将tr从表格中移除（如果只有一条数据时，需要删除表格并添加h2标签）
				var table = $("#myTable");
				console.log(table);
				// 判断表格中tr的数量是否少于等于2，如果是，说明只有一条数据
				var len = table.children().children().length;
				if (len <= 2) {
					$("#myDiv").html("<h2>暂未查询到类型数据！</h2>");
				} else {
					$("#tr_"+typeId).remove(); // 移除tr
				}
				
			} else { // 删除失败或者存在子记录，无法删除
				swal("",data.msg,"error");
				return;
			}
		});
	});
}





