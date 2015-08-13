require(['jquery', 'knockout', 'bootstrap', 'uui', 'director'], function($, ko) {

	
	
	window.addRouter = function(path, func) {
		var pos = path.indexOf('/:');
		var truePath = path;
		if (pos != -1)
			truePath = path.substring(0,pos);
		func = func || function() {
			var params = arguments;
			initPage('static/pages' + truePath,params);
		}
		var tmparray = truePath.split("/");
		if(tmparray[1] in router.routes && tmparray[2] in router.routes[tmparray[1]] && tmparray[3] in router.routes[tmparray[1]][tmparray[2]]){
			return;
		}else{
			router.on(path, func);
		}
	}

	window.router = Router();
	
	initMenuTree = function(){
		$('#show_side').click(function() {
			var $leftpanel = $('.leftpanel');
			//展开
			if ($leftpanel.hasClass('leftpanel-collapse')) {
				$leftpanel.removeClass('leftpanel-collapse')
				$('.content').removeClass('content-collapse')
				$('.left-menu').children('li').children('a').children('.title').show();
				$('.left-menu').children('li').children('a').children('.arrow').show();
			} else {
				//合闭
				$leftpanel.addClass('leftpanel-collapse')
				$('.content').addClass('content-collapse')
				$('.left-menu').children('li').children('a').children('.title').hide();
				$('.left-menu').children('li').children('a').children('.arrow').hide();
				$('.left-menu').children('li.open').children('a').children('.arrow').removeClass('open').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-left');
				$('.left-menu').children('li.open').children('a').children('.arrow').removeClass('active');
				$('.left-menu').children('li.open').children('.sub-menu').slideUp(200);
			}
		});

		$('.left-menu li > a').on('click', function(e) {
			if ($(this).children('.title').length > 0 && !$(this).children('.title').is(':visible')) {
				$('#show_side').click();
			}
			if ($(this).next().hasClass('sub-menu') === false) {
				return;
			}
			var parent = $(this).parent().parent();
			parent.children('li.open').children('a').children('.arrow').removeClass('open').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-left');
			parent.children('li.open').children('a').children('.arrow').removeClass('active');
			parent.children('li.open').children('.sub-menu').slideUp(200);
			parent.children('li').removeClass('open');
			//  parent.children('li').removeClass('active');

			var sub = $(this).next();
			if (sub.is(":visible")) {
				$('.arrow', $(this)).removeClass("open").removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-left');
				$(this).parent().removeClass("active");
				sub.slideUp(200);
			} else {
				$('.arrow', $(this)).addClass("open").removeClass('glyphicon-chevron-left').addClass('glyphicon-chevron-down');
				$(this).parent().addClass("open");
				sub.slideDown(200);
			}

			e.preventDefault();
		})		
	}
	
	$(function(){
		/*
		$.ajax({
			type : 'GET',
			url : $ctx + "/mgr/function/rootmenu",
			data : '',
			dataType : 'json',
			success : function(data) {
				initFuncTree(data);
			}
		});
		*/
		initMenuTree();
		$('.left-menu').find("a[href*='#']").each(function() {
			var path = this.hash.replace('#', '');
			addRouter(path);
		});
		
		window.router.init();
		
		setInterval(function(){
			
			 $.ajax({
			  	   type:'GET',
	               url: '/web_example/weixin/msgnum',
	               async:true,
	               success:function(data){
	            	   
	            	   var num = eval(data)
	            	   $('#msgs-badge').html(num)
	               
	               }
	          })			
		},10000)
	})
	
	initFuncTree = function(menuData){
		if(menuData.id == 0){
			var rootMenuArray = menuData.children;
			for (var i = 0; i < rootMenuArray.length; i++) {
				var menu = rootMenuArray[i];
				var liObj = $("<li class=\"\">");
				var aObj = $("<a href=\"javascript:;\"> <i class=\"fa fa-file-text\"></i> <span class=\"title\">"+menu.funcName+"</span> <span class=\"arrow glyphicon glyphicon-chevron-left\"></span> </a>");
				var ulObj = $("<ul class=\"sub-menu\">");
				if(menu.children.length > 0){
					for (var j = 0; j < menu.children.length; j++) {
						var subMenuObj = menu.children[j];
						var subLiObj = $("<li> <a href=\"#"+ subMenuObj.funcUrl +"\" >"+ subMenuObj.funcName +"</a> </li>");
						$(ulObj).append(subLiObj);
					}
				}
				$(liObj).append(aObj).append(ulObj);
				$(".left-menu").append(liObj);
			}
		}
		initMenuTree();
		
	/*	$('.left-menu').find("a[href*='#']").each(function() {
			var path = this.hash.replace('#', '');
			addRouter(path);
		});  */
	}

	function initPage(p, id) {
		var module = p;
		requirejs.undef(module);
		require([module], function(module) {
			ko.cleanNode($('.content')[0]);
			$('.content').html('');
			$('.content').html(module.template);
			if(module.model){
				module.model.data.content = ko.observableArray([]);
				ko.applyBindings(module.model, $('.content')[0]);
				module.init(id);
			}else{
				module.init(id, $('.content')[0]);
			}
		})
	}
});