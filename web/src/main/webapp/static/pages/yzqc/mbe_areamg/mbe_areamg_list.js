define([ 'jquery', 'knockout', 'text!static/pages/yzqc/mbe_areamg/mbe_areamg_list.html' ], function($, ko, template) {

	//跳转信息
	var infoUrl = "/yzqc/mbe_areamg/mbe_areamg_detail/:pkAreamg";
	
	var addUrl = '/yzqc/mbe_areamg/mbe_areamg_detail/0';
	
	var deleteUrl = '/mbe_areamg/del/';
	
	var pageUrl = '/mbe_areamg/page';
	
	var pageSize = 20;

	// 添加详细信息页路由
	addRouter(infoUrl);
	viewModel = {
		data : {
			content : ko.observableArray([]),
			
			totalPages : ko.observable(0),
			number : ko.observable(0)
		},
		searchText : ko.observable(""),
		setData : function(data) {
			this.data.content(data.content);
			this.data.totalPages(data.totalPages);
			this.data.number(data.number + 1);
		},
		infoUrl : infoUrl,
		addUrl : addUrl,
		deleteUrl : deleteUrl,
		pageUrl : pageUrl
	};

	viewModel.add = function(){
		window.router.setRoute(this.addUrl);	
	};
	
		
	viewModel.del = function() {
//		alert($ctx + deleteUrl + this.pkAreamg+".json");
		$.ajax({
			type : 'DELETE',
			async : false,
			url : $ctx + deleteUrl + this.pkAreamg+".json",
			success : function(data) {
				if (data){
					jAlert('删除成功!')
					var pageNum = viewModel.data.number();
					viewModel.data.content.remove(this);
					viewModel.load(pageNum,pageSize);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				jAlert("调用删除服务报错!!");
			}
		});
		
	}
		
	viewModel.load = function(pageIndex,pageSize){
		
		 var queryData = {};
         $(".form-search").find(".input_search").each(function(){
             queryData[this.name] = this.value;
         });
//         alert($ctx + this.pageUrl +"/"+ pageIndex+".json");
		$.ajax({
			type : 'GET',
			url : $ctx + this.pageUrl +".json?page="+ pageIndex+"&pageSize="+pageSize,
			data : queryData,
			dataType : 'json',
			success : function(data) {
				viewModel.setData(data);
				$("#pagination").pagination({
					totalPages : viewModel.data.totalPages(),
					currentPage : viewModel.data.number(),
					pageSize:pageSize,
					page : function(page) {
						pageSize = $(".page_z").val();
						viewModel.load(page,pageSize);
					}
				})							
			}
		});
		
	}
		
	viewModel.searchPage = function() {
//		alert(searchPage);
		pageSize = $(".page_z").val();
		this.load(1,pageSize);                                                                                          
	}
	
	var init = function() {
		var pageNum = viewModel.data.number();
		if(pageNum > 0){
			viewModel.load(pageNum,pageSize);
		} else {
			viewModel.load(1,pageSize);
		}
	}
	
	return {
		'model' : viewModel,
		'template' : template,
		'init' : init
	};

});