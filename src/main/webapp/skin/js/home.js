//cookie 记住userid, 但不保存密码
$(document).ready(function(){
	$("#twibo").maxlength();
	$("#sendTwibo").click(sendTwibo);
	getTwibo();	
});	

function showDialog(_id){	
	showComment(_id);
	bootbox.prompt($("div[name='"+_id+"'] .twibo-content").text(), function(result) {                
	  if (result) {                                             
		//发送评论
		  sendComment(_id,result);                                
	  }
	});
}

function showComment(_id){
	$.post(ctx+"/twibo/comments",{tid:_id}, function(data){
		if(data && data.status==10000){
			var list=data.result;
			for(var i=0,len=list.length;i<len;i++){
				$("div .modal-body").append(list[i].user.nickName+" 说："+list[i].content+" 时间："+list[i].timestamp);
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
				var headvar="<div ToolTip='"+list[i].user.nickName+"'>孔子曰</div>";
				var timevar="<div class='text-left twibo-time'>"+d.toLocaleString()+"</div>";
				var link="<ul class='list-inline pull-right'><li>转发</li><li><a href='javascript:void(0)' onclick=\"showDialog('"+list[i]._id+"')\">评论</a></li></ul>";
				$("#twibo-pool").append("<div name='"+list[i]._id+"' class='twibo-bk'>"+headvar+"<p class='twibo-content'>"+list[i].content+" </p><p class='twibo-line'>"+link+timevar+"</p></div>");
			}
		}
	});
}