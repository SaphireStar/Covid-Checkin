<%@ include file="/WEB-INF/views/include.jsp"%>
<html>
<head>
<title>Hello, Covid Check-in made easy!</title>
<style type="text/css">

@import url("<c:url value='/resources/css/notification.css'/>");
</style>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript">

	// Handle onclick event on Business box
	function SupendButtonClick(obj) {
	    $('button[id^="SupendButton"]').css("background-color", "#91bdff");
	    $(obj).css("background-color", "#007bff");
	    RefreshBussiness(obj);
	}
	
	// Send Ajax request to fetch the details of notifications.
	function RefreshBussiness(obj){
		var bussinessId = $(obj).val();
	    var userId = $("#userId").val();
	    $.post(
	            "http://localhost:8080/elec5619/notification/detail",
	            {"bussinessId": bussinessId, "userId": userId},
	            function (data) {
	            	$('div[id^="message"]').remove();
	            	$('p[id^="blank"]').remove();
	                data = $.parseJSON(data);
	                var arr=data.list;
	                var i = 0;
	                var bName;
	                if(arr.length==0){
	                	$( "#detail").append('<p id="blank" class="textweight">There is no Messages from this business!</p>');
	                }else{
		                $.each(arr, function(index,map){
		                	$('a[id^='+bussinessId+']').remove();
		                	var createTime = map.createTime;
		                	var content = map.content;
		                	var nId = map.nId;
		                	bName = map.bName;
		                	bAddress = map.bAddress;
		                	var deleteUrl ="delete"+ "&gt;"+ bussinessId +"&gt;" + nId;
		                	var messageName = "message" + i;
		                	i++;
		                	var objName = 'div[id^="'+ messageName +'"]';
		                	$( "#detail").append('<div class="message" style="margin-bottom:20px;" id="'+ messageName+ '"></div>');
		                	$( objName).append('<a class="time">'+createTime+'</a>');
		                	$( objName).append('<a class="time" style="color:green;float: right;margin-right:14px;" href = "javascript:void(0);" onclick ="Delete(this);" id="'+deleteUrl+'">DELETE</a>');
		                	$( objName).append('<dd style="margin-left:0px"><a style="font-size:16px;">'+content+'</a></dd>');
	                   });
		                $( "#right").prepend('<p id="blank" class="textweight"><a style="color:black;display:block;text-align:center;margin-right:8px;">'+ bAddress+'</a></p>');
		                $( "#right").prepend('<p id="blank" class="textweight"><a style="color:#007bff!important;display:block;text-align:center;font-size: 34px;">'+ bName +'</a></p>');
	                }
	               
	   });
	}
	
	// Send Ajax request to fetch the details of notifications from System.
	function SystemButtonClick() {
		$('button[id^="SupendButton"]').css("background-color", "#91bdff");
	    var userId = $("#userId").val();
	    $.post(
	            "http://localhost:8080/elec5619/notification/system",
	            {"userId": userId},
	            function (data) {
	            	$('div[id^="message"]').remove();
	            	$('p[id^="blank"]').remove();
	                data = $.parseJSON(data);
	                var arr=data.list;
	                if(arr.length==0){
	                	$( "#detail").append('<p id="blank" class="textweight">There is no System Messages!</p>');
	                }else{
	                	var i = 0;
	                	$.each(arr, function(index,map){
		                	$('a[id^="unread"]').remove();
		                	var createTime = map.createTime;
		                	var content = map.content;
		                	var nId = map.nId;
		                	var deleteUrl ="delete"+ "&gt;"+ 0 +"&gt;" + nId;
		                	var messageName = "message" + i;
		                	i++;
		                	var objName = 'div[id^="'+ messageName +'"]';
		                	$( "#detail").append('<div class="message" style="margin-bottom: 20px;" id="'+ messageName+ '"></div>');
		                	$( objName).append('<a class="time">'+createTime+'</a>');
		                	$( objName).append('<a class="time" style="color:green;float: right;margin-right:10px;" href = "javascript:void(0);" onclick ="Delete(this);" id="'+deleteUrl+'">DELETE</a>');
		                	$( objName).append('<dd style="margin-left:0px"><a style="font-size:14px; ">'+content+'</a></dd>');
                   			});
	                	};
	                $( "#right").prepend('<p id="blank" style="color:red;" class="textweight">System Messages:</p>'); 
	   });
	}
	
	// Send Ajax request to Delete notifications.
	function Delete(obj) {
		var url = $(obj).attr("id");	
		var strArray = url.split(">");
		var id = strArray[1];
		url =  "http://localhost:8080/elec5619/notification/delete/" + strArray[2];
		$.get(url,
			function (data) {
				data = $.parseJSON(data);
				var code = data.code;
				if(code == 0){
					var obj = $('<button value="'+id+'"></button>');
					if(id == 0){
						SystemButtonClick();
					}else{
						RefreshBussiness(obj);
					}
				}
		});
	}
	
	// search message that user interest based on business name
	function Search() {
		var str = $("#search").val();
		if(str == ""){
		
		}
		var userId = $("#userId").val();
		url =  "http://localhost:8080/elec5619/notification/search/";
		$.post(url,
			{"str": str, "userId": userId},
			function (data) {
				data = $.parseJSON(data);
				var code = data.code;
				var arr = data.list;
				if(code == 0){
					$('button[id^="SupendButton"]').remove();
					$.each(arr, function(index,map){
						var unread = '';
						if(map.unread!=0){
							unread = '<a id=${noti.bId} class="unread">'+ map.unread + '</a>';
						}
	                	
	                	var businessName = map.businessName;
	                	var createTime = map.createTime;
	                	var bId = map.bId;
	             
	                	$( "#notiList").append('<button id="SupendButton" class="backcb busbox busName"' + 
	                			'type="submit" onclick=SupendButtonClick(this); value=' + bId + '>'+
								'<p style="font-size:16px;">' + businessName + unread +
							'</p><p style="font-size:12px;">'+ createTime + '</p></button>');
                   });
				}
		});
	}
</script>

<script>
    // Send requests every 10 sec to find out if there is new message.
    window.setInterval(checkUpdate, 1000 * 10);
    function checkUpdate() {
        var userId = $("#userId").val();
        console.log("check update");
	    $.post(
	            "http://localhost:8080/elec5619/notification/check",
	            {"userId": userId},
	            function (data) {
	                data = $.parseJSON(data);
	                var code = data.code;
	                var name = data.name;
	                var content = data.content;
	                if(code == 0){
	                	
	                }else{
	                	notifyMe(name, content);
	                }
	   });
    }
    
 	// Pop out a window in browser if there is newest message
    function notifyMe(name, content){
        if (!("Notification" in window)) {
            alert("This browser does not support desktop notification");
        }else if (Notification.permission === "granted") {
            var notification = new Notification(name, {
                body: content,
                icon: 'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1493952301,1278532971&fm=26&gp=0.jpg',

            });
            notification.onclick = function() {
            	var userId = $("#userId").val();
            	var url = "http://localhost:8080/elec5619/notification/user/"+ userId;
            	window.location.href=url;
                window.focus();
                notification.close();
            };
            setTimeout(notification.close.bind(notification), 15000);
            }
        
        else if (Notification.permission !== 'denied') {
            Notification.requestPermission(function (permission) {
                
                if (permission === "granted") {
                    var notification = new Notification('Covid-19 Check-In', {
                        body: 'Successfully subscribed to Covid-19 Alert!'
                    });
                }
            });
        }
    }
</script>

</head>
<body>
	<div class="topbar">
		<div>
			<a> ${user.lastName} 's</a>
			<a> Messsages</a>
		</div>
		<div style="float:right; margin-right:50px;">
			<button><a  href="http://localhost:3000" > HOME </a></button>
		</div>
	</div>
	<input type="int" style="display:none" value=${user.id} id="userId">
	<button class="btn" type="submit" onclick=SystemButtonClick();>System Messages
			<a id="unread" class="unread"> ${unread} </a>
	</button>
	<div class="container">
			<div class="boxcard boxcard-container two-div ">
				<div class="search">
		            <input type="text" placeholder="Input business name..." id="search" />
		            <button onclick=Search();><i>Search</i></button>
		        </div>
				<div>
					<p class="textweight" style="color:black;"> Business List: </p>
				</div>
				<div id="notiList">
					<c:forEach items="${notiList}" var="noti">
							<button id="SupendButton" value=${noti.bId} class="backcb busbox busName" type="submit" onclick=SupendButtonClick(this);>
									<p style="font-size:16px;">${noti.businessName}	
		   								<c:if test="${noti.unread!=0}">
											<a id=${noti.bId} class="unread"> ${noti.unread} </a>
										</c:if>
									</p>
								<p style="font-size:12px;"> ${noti.createTime}</p>
								</button>
					</c:forEach>
				</div><!-- NotiList -->
			</div> <!-- box card-container -->
			
			<div id="right" class="card card-container two-div">

				<div id="detail"> </div>
			</div> <!-- card-container -->
			<!-- <button onclick=notifyMe();>Test</button> -->
	</div>
</body>
</html>
