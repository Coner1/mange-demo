define([ 'jquery', 'knockout', 'text!static/pages/example/demo/testrestful.html' ], function($, ko, template) {

	var infoUrl = "/example/demo/demoinfo/:id";
	
	var addUrl = '/example/demo/demoinfo/0';
	
	var deleteUrl = '/example/demo/delete/';
	
	var pageUrl = '/example/demo/page?page=';

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
	
	viewModel.callRestfulServiceDirect = function(){
		var url = $ctx + "/restful/call";
		var signUrl = "http://localhost:8081/ecmgr/api/demo/restservice";
		$.ajax({
			type : 'POST',
			dataType : 'json',
			async : false,
			url : url,
			data : {'callUrl':signUrl},
			success : function(data) {
				if ("fail" == data.result){
					jAlert('调用服务失败!')
				} else {
					jAlert(data.result)
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				jAlert('调用服务出错!')
			}
		});
	};
	
	viewModel.testRestFul = function(){
		var url = $ctx + "/sign/signRequest"
		var signUrl = "http://localhost:8081/ecmgr/api/demo/testrestful";
		$.ajax({
			type : 'POST',
			dataType : 'json',
			async : false,
			url : url,
			data : {'signUrl':signUrl},
			/*
			不跨域的情况下，可以设置header，两种方式都生效。跨域不可以!
			beforeSend: function(request) {
                request.setRequestHeader("Test", "Chenxizhang");
            },
            headers: {
	            "Authorization" : "mysign"
	        },
	        */
			success : function(data) {
				if ("fail" == data.result){
					jAlert('调用签名服务失败!')
				} else {
					callRestFultService(data.result,signUrl);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				jAlert('调用签名服务失败!')
			}
		});
	};
	
	function callRestFultService(sign,callUrl){
		$.ajax({
			type : 'GET',
			dataType : 'jsonp',
			async : true,
			url : callUrl,
			data : {
				"sign" : sign,
				"clientType" : "UAP_MOBILE"
			},
			contentType : 'application/json',
	        jsonp : "jsonpcallback",
			success : function(data) {
				if (data){
					jAlert('调用成功!')
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				jAlert("调用服务报错!!");
			}
		});
		
	}
		
	viewModel.del = function() {
		$.ajax({
			type : 'DELETE',
			dataType : 'json',
			async : false,
			url : $ctx + deleteUrl + this.id,
			success : function(data) {
				if (data){
					jAlert('删除成功!')
					var pageNum = viewModel.data.number();
					viewModel.data.content.remove(this);
					viewModel.load(pageNum);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				jAlert("调用删除服务报错!!");
			}
		});
		
	}
		
	viewModel.load = function(pageIndex){
		
		 var queryData = {};
         $(".form-search").find(".input_search").each(function(){
             queryData[this.name] = this.value;
         });
       
		$.ajax({
			type : 'GET',
			url : $ctx + this.pageUrl + pageIndex,
			data : queryData,
			dataType : 'json',
			success : function(data) {
				viewModel.setData(data);
				$("#pagination").pagination({
					totalPages : viewModel.data.totalPages(),
					currentPage : viewModel.data.number(),
					page : function(page) {
						viewModel.load(page);
					}
				})							
			}
		});
		
	}
		
	viewModel.searchPage = function() {
		this.load(1);                                                                                          
	}
	
	var init = function() {
		var pageNum = viewModel.data.number();
		if(pageNum > 0){
			viewModel.load(pageNum);
		} else {
			viewModel.load(1);
		}
	}
	
	return {
		'model' : viewModel,
		'template' : template,
		'init' : init
	};

});