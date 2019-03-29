<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  
    
    <title>用户中心</title>
    
	

  </head>
  
  <body>
    <div class="data_list">
    <div class="data_list_title">
        <span class="glyphicon glyphicon-edit"></span>&nbsp;个人中心
    </div>
    <div class="container-fluid">
        <div class="row" style="padding-top: 20px;">
            <div class="col-md-8">
                <form class="form-horizontal" method="post" action="user?action=updateInfo"
                    enctype="multipart/form-data" >
                    <div class="form-group">
                        <input type="hidden" name="act" value="save"> <label
                            for="nickName" class="col-sm-2 control-label">昵称:</label>
                        <div class="col-sm-3">
                            <input class="form-control" name="nick" id="nickName"
                                placeholder="昵称" value="${user.nick}">
                        </div>
                        <label for="img" class="col-sm-2 control-label">头像:</label>
                        <div class="col-sm-5">
                            <input type="file" id="img" name="img">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="mood" class="col-sm-2 control-label">心情:</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" name="mood" id="mood" rows="3">${user.mood}
                            </textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="submit" id="btn" class="btn btn-success">修改</button>
                            &nbsp;&nbsp;<span style="color:red" id="msg"></span>
                        </div>
                    </div>
                </form>
            </div>
            <div class="col-md-4">
                <img  class="img-responsive center-block"
                    src="user?action=userHead&fn=${user.head}">
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function(){
    	// 文档加载完毕 绑定焦点事件
    	
    	
    	$("#nickName").focus(function(){
    		//  绑定获取焦点事件
    		$("#btn").attr("disabled",false);
    		$("#msg").html("");
    	})
    	
    	
    	
    	$("#nickName").blur(function(){
    		// 绑定失去焦点事件
    		var nickName='${user.nick}';//获取原始昵称
    		var nickName2=$("#nickName").val();// 修改后的昵称名称
    		
    		// 执行非空校验
    		if(""==$.trim(nickName2)){
    			// 添加提示信息
    			$("#msg").html("昵称内容不能为空!");
    			$("#btn").attr("disabled",true);
    			return ;
    		}
    		// nickName!=nickName2  执行ajax 发送
    		if(nickName!=nickName2){
    			// 发送ajax 执行校验
    			$.ajax({
    				type:"post",
    				url:"user",
    				data:"action=checkNick&nick="+nickName2,
    				dataType:"json",
    				success:function(data){
    					if(data.code==1){
    						$("#msg").html(data.msg);
    		    			$("#btn").attr("disabled",true);
    					}else{
    						$("#btn").attr("disabled",false);
    					}
    				}	
    			})
    		}
    	})
    })



</script>


    
  </body>
</html>
