//cookie 记住userid, 但不保存密码
$(document).ready(function(){
	$("#twibo").maxlength();
	$("#sendTwibo").click(sendTwibo);
	getTwibo();	
});	

function showDialog(_id){	
	showComment(_id);
	bootbox.prompt($("p[name='content"+_id+"']").text(), function(result) {                
	  if (result) {                                             
		//发送评论
		sendComment(_id,result);                                
	  }
	});
}

function showComment(_id){
	$.post(ctx+"/twibo/comments",{tid:_id,offset:0,length:20}, function(data){
		if(data && data.status==10000){
			var list=data.result;
			for(var i=0,len=list.length;i<len;i++){
				var d = new Date(list[i].timestamp);
				var timevar="<div class='text-left twibo-time'>"+d.toLocaleString()+"</div>";
				$("div .modal-body").append("<div class='comment-bk'><div class='comment-info'>"+list[i].user.nickName+"</div><div class='comment-content'>"+list[i].content+"</div><p class='twibo-line'>"+timevar+"</p></div>");
			}
			
		}
	});
}

function sendComment(_id,_content){
	$.post(ctx+"/twibo/post/comment",{tid:_id,content:_content}, function(data){
		if(data && data.status==10000){
			alert("评论成功");
		}
	});
}


function sendTwibo(){
	$.post(ctx+"/twibo/post/twibo",{content:$("#twibo").val()},function(data){
		//发送成功
		if(data && data.status==10000){
			$("#twibo").val("");
			var twiSize=parseInt($("#twiSize").text());
			$("#twiSize").text(++twiSize);
		}
	});
}

function getTwibo(){
	//默认显示20条
	$.post(ctx+"/twibo/list",{offset:0,length:20},function(data){
		if(data && data.status==10000){
			var list=data.result;
			for(var i=0,len=list.length;i<len;i++){
				var d = new Date(list[i].timestamp);
				if(NICKNAME==list[i].user.nickName){
					headvar="<div class='twibo-info' style='color:black;'>"+list[i].user.nickName+"</div>";
				}else{
					headvar="<div class='twibo-info'>"+list[i].user.nickName+"</div>";
				}
				var timevar="<div class='text-left twibo-time'>"+d.toLocaleString()+"</div>";
				
				var link="<ul class='list-inline pull-right'><li>转发</li><li><a href='javascript:void(0)' onclick=\"showDialog('"+list[i]._id+"')\">评论("+list[i].commentSize+")</a></li></ul>";
				$("#twibo-pool").append("<div class='twibo-bk'><div class='twibo-detail'>"+headvar+"<p name='content"+list[i]._id+"' class='twibo-content'>"+list[i].content+" </p><p class='twibo-line'>"+link+timevar+"</p></div></div>");
			}
		}
	});
}