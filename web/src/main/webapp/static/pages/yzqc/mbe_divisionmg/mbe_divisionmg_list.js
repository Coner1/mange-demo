define([ 'jquery', 'knockout', 'text!static/pages/yzqc/mbe_divisionmg/mbe_divisionmg_list.html' ], function($, ko, template) {

	//跳转信息
	var infoUrl = "/yzqc/mbe_divisionmg/mbe_divisionmg_detail/:pkDivisionmg";
	
	var addUrl = '/yzqc/mbe_divisionmg/mbe_divisionmg_detail/0';
	
	var deleteUrl = '/mbe_divisionmg/delete/';
	
	var pageUrl = '/mbe_divisionmg/page';

	// 添加详细信息页路由
	 addRouter(infoUrl);
     viewModel = {
         data: {
             content: ko.observableArray([]),

             totalPages: ko.observable(0),
             number: ko.observable(0)
         },
         searchText: ko.observable(""),
         setData: function (data) {
             this.data.content(data.content);
             this.data.totalPages(data.totalPages);
             this.data.number(data.number + 1);
         },
         infoUrl: infoUrl,
         addUrl: addUrl,
         deleteUrl: deleteUrl,
         pageUrl: pageUrl
     };

     viewModel.add = function () {
         window.router.setRoute(this.addUrl);
     };


     viewModel.del = function () {
         console.info($ctx + deleteUrl + this.pkDivisionmg + ".json");
         if (confirm("确认删除这条数据吗?")) {
             $.ajax({
                 type: 'DELETE',
                 async: false,
                 url: $ctx + deleteUrl + this.pkDivisionmg + ".json",
                 success: function (data) {
                     if (data) {
                         jAlert('删除成功!')
                         var pageNum = viewModel.data.number();
                         viewModel.data.content.remove(this);
                         viewModel.load(pageNum);
                     }
                 },
                 error: function (XMLHttpRequest, textStatus, errorThrown) {
                     jAlert("调用删除服务报错!!");
                 }
             });
         }
     }

     viewModel.load = function (pageIndex) {

         var queryData = {page: pageIndex};
         $(".form-search").find(".input-search").each(function () {
             queryData[this.name] = this.value;
         });
//      debugger;
         console.info($ctx + this.pageUrl + ".json?page=" + pageIndex);
         $.ajax({
             type: 'GET',
             url: $ctx + this.pageUrl + ".json",
             data: queryData,
             dataType: 'json',
             success: function (data) {
                 viewModel.setData(data);
                 $("#pagination").pagination({
                     totalPages: viewModel.data.totalPages(),
                     currentPage: viewModel.data.number(),
                     page: function (page) {
                         viewModel.load(page);
                     }
                 })
             }
         });

     }

     viewModel.searchPage = function () {
         this.load(1);
     }

     var init = function () {
         var pageNum = viewModel.data.number();
         if (pageNum > 0) {
             viewModel.load(pageNum);
         } else {
             viewModel.load(1);
         }
     }

     return {
         'model': viewModel,
         'template': template,
         'init': init
     };

 }
);