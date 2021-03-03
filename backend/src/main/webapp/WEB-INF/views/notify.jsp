<%@ include file="/WEB-INF/views/include.jsp"%>
<html>
<head>
<title>Hello, Covid Check-in made easy!</title>
<style type="text/css">
@import url("<c:url value='/resources/css/notify.css'/>");
</style>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript">

	// Send Ajax request to delete send history
	function Delete(obj) {
		var nid = $(obj).attr("id");	
		url =  "http://localhost:8080/elec5619/notification/delete/" + nid;
		$.get(url,
			function (data) {
				data = $.parseJSON(data);
				var code = data.code;
				if(code == 0){
					reFresh();
				}
		});
	}
	
	// Send Ajax request to refresh send history
	function reFresh() {
		var bId = $("#businessId").val();
		url =  "http://localhost:8080/elec5619/notification/history";
		$.post(url,
			{"bussinessID": bId},
			function (data) {
				data = $.parseJSON(data);
				var code = data.code;
				var arr = data.list;
				if(code == 0){
					$('tr[id^="tableItem"]').remove();
					$.each(arr, function(index,map){
						var status = map.status == 1? "Success":"Sending";
						$("#table").append('<tr id="tableItem" style="font-size:12px; "><td>' + map.createTime + '</td><td>' + 
								map.content + '</td><td>' + status +
								'</td><td class="time" style="color:green;" href = "javascript:void(0);" onclick ="Delete(this);" id=' + 
								map.nId + '>DELETE</td></tr>');
					});
				}
		});
	}
	
	 $().ready(function(){
			reFresh();
		});
	
</script>
</head>
<body>
	<div class="topbar">
		<div>
			<a> Notify</a>
		</div>
		<div style="float:right; margin-right:50px;">
			<button><a  href="http://localhost:3000" > HOME </a></button>
			<button><a  href="http://localhost:3000/business/${business.id}"> BusinessManage</a></button>
		</div>
	</div>
	<div class="container">
		<div class="card card-container">
			<p class="textweight">Want to notify your customers?</p>
			<form action="/elec5619/notification/notify" method="post"
				enctype="multipart/form-data" onsubmit="return submitvalification()">
		        <a class="textblue">Provide a time range and leave a Message.</a>
		        <p>
			        From:
			        <input class="input-control" type="text" name="startTime" value="2020-10-01 9:00:00" id="startTime" placeholder="startTime: yyyy-MM-dd HH:mm:ss" required>
			        <a style="margin-left:40px;">
			        	To:
			        	<input class="input-control" type="text" name="endTime" value="2021-10-03 20:00:00" id="endTime" placeholder="endTime:yyyy-MM-dd HH:mm:ss" required>
			        </a>
			   	</p>
			   	<!-- Return message show if the notify operation success -->
			   	<c:if test="${error==2}">
					<p id="errormessage" style="color:green;"> Send Message Successfully!</p>
				</c:if>
				<c:if test="${error==1}">
					<p id="errormessage" style="color:red;"> Wrong Format!</p>
				</c:if>
				<c:if test="${error==0}">
					<p id="errormessage" style="color:red;"> No User Check-In During this Duration!</p>
				</c:if>
			   	<textarea name="content" style="font-size:16px" maxlength=250 placeholder="Max Length: 250 characters" rows=5 cols=36 required></textarea>
		        <input id="businessId" type="int" style="display:none" value=${business.id} name="businessId" placeholder="businessId" required autofocus>
		        <p>
		        <button class="btn btn-lg btn-primary btn-block btn-signin" type="submit">Notify</button>
		        </p>
		     </form>
		</div><!-- /card-container -->
		<div class="card card-container" style="margin-top:-10px; font-size:14px;">
			<p  style="">
				<a class="textweight">Send History</a>
				<button style="float:right;" type="submit" onclick=reFresh();><img style="24px;" src="https://img.icons8.com/offices/30/000000/refresh.png"/></button>
			</p>
					<table id="table" width="100%" border="1" >
							<tr id="tableHead">
							<th>createTime</th>
							<th>content</th>
							<th>Status</th>
							</tr>
								<c:forEach items="${notiList}" var="noti">
									<tr id="tableItem" style="font-size:14px;">
									<td>${noti.createTime}</td>
									<td>${noti.content}</td>
									<td>${noti.status==1?"Success":"Sending"}</td>
									<td class="time" style="color:green;" href = "javascript:void(0);" onclick ="Delete(this);" id=${noti.nId}>DELETE</td>
									</tr>						
								</c:forEach>
					</table>
			</div>
	</div><!-- /container -->
</body>
</html>
