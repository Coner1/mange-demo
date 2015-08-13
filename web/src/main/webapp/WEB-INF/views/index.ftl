<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
		<meta charset="utf-8" />
		<title>主页</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		<meta content="" name="description" />
		<meta content="" name="author" />
		<link href="${ctx}/static/lib/bootstrap/css/bootstrap.css" rel="stylesheet">
		<link href="${ctx}/static/lib/uui/css/u.css" rel="stylesheet">
		<link href="${ctx}/static/css/index.css" rel="stylesheet">
		<script src="${ctx}/static/lib/jQueryAlert/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/static/lib/jQueryAlert/jquery.ui.draggable.js" type="text/javascript"></script>
		<script src="${ctx}/static/lib/jQueryAlert/jquery.alerts.js" type="text/javascript"></script>
	    <script src="${ctx}/static/lib/pako/pako_deflate.js" type="text/javascript"></script>
		<link href="${ctx}/static/lib/jQueryAlert/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
		<script>
			window.$ctx = '${ctx}';
		</script>
	</head>

	<body>
		<!-- top -->
		<div class="idx-top navbar navbar-default" role="navigation" style="background-color:#464646;">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#example-navbar-collapse">
					<span class="sr-only">切换导航</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">后台管理</a>
				<div type="button" id="show_side" class="navbar-brand glyphicon glyphicon-th-large"></div>
			</div>
			<div class="collapse navbar-collapse" id="example-navbar-collapse">
				<ul class="right-nav nav navbar-nav navbar-right">
					<li>
						<a href="#" class="glyphicon glyphicon-envelope"></a>
						<span class="badge badge-important" id="msgs-badge"></span>
					</li>
					<li class="dropdown" style="width:160px;">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">
							<span class="glyphicon glyphicon-user"></span>
							<span class="">${cusername}</span>
							<span class="glyphicon glyphicon-chevron-down"></span>
						</a>
						<ul class="dropdown-menu">
							<li><a href="#">系统配置</a></li>
							<li><a href="#">个性化</a></li>
							<li><a href="${ctx}/logout">注销</a></li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
		<!-- left -->
		<div class="leftpanel">
			<ul class="left-menu">
				<li class="">
					<a href="javascript:;"> <i class="fa fa-file-text"></i> <span class="title">系统管理</span> <span class="arrow glyphicon glyphicon-chevron-left"></span> </a>
					<ul class="sub-menu">
						<li> <a href="#/mgr/account/accountlist" >用户管理</a> </li>
						<li> <a href="#/mgr/role/rolelist" >角色管理</a> </li>
						<li> <a href="#/mgr/function/functionlist" >功能管理</a> </li>
					</ul>
				</li>
				<li class="">
					<a href="javascript:;"> <i class="fa fa-file-text"></i> <span class="title">示例</span> <span class="arrow glyphicon glyphicon-chevron-left"></span> </a>
					<ul class="sub-menu">
						<li> <a href="#/example/demo/demolist">Demo</a> </li>
						<li> <a href="#/example/datatabledemo/dataTable">Datatable Demo</a> </li>
						<li> <a href="#/example/datatabledemo/form">formSubmit</a> </li>
					</ul>
				</li>
				<li class="">
					<a href="javascript:;"> <i class="fa fa-file-text"></i> <span class="title">盈众会员管理</span> <span class="arrow glyphicon glyphicon-chevron-left"></span> </a>
					<ul class="sub-menu">
						<li> <a href="#/yzqc/mbe_grpmg/mbe_grpmg_list">集团</a> </li>					
						<li> <a href="#/yzqc/mbe_areamg/mbe_areamg_list">区域</a> </li>
						<li> <a href="#/yzqc/mbe_divisionmg/mbe_divisionmg_list">事业部</a> </li>
						<li> <a href="#/yzqc/bd_demo/griddemo">档案示例</a> </li>
					</ul>
				</li>
			</ul>
		</div>
		<!-- content -->
		<div class="content">内容区</div>
		<script src="${ctx}/static/lib/requirejs/require.js"></script>
		<!--
		<script src="${ctx}/static/js/config.js"></script>
		-->
		<script src="${ctx}/static/js/require.config.js"></script>
		<script src="${ctx}/static/js/index.js"></script>
	</body>

</html>