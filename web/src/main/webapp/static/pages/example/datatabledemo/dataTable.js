define([ 'jquery', 'knockout', 'text!static/pages/example/datatabledemo/dataTable.html', 'uui', 'biz'], function($,ko,template) {
	
	var urlDispatch = $ctx+'/evt/dispatch' ;
	
	var init = function(id, viewSpace) {
		//debugger
		var viewModel = {
			dataTable1 : new $.DataTable({
				params:{
					"cls" : "uap.web.example.entity.DemoB"
				},
				meta : {
					'theid' : {},
					'code' : {},
					'name' : {},
					'memo' : {},
					'isdefault' : {}
				},
				pageSize: 15,
				pageCache:true
			}),
			addRow: function() {
				//	if(viewModel.dataTable1.currSelectedIndex !== -1){
				//	viewModel.dataTable1.insertRow(viewModel.dataTable1.currSelectedIndex)
				//}else{
						viewModel.dataTable1.createEmptyRow()
				//	}
					
			},
			delRow: function(model, event) {
				if(viewModel.dataTable1.currSelectedIndex == -1 ){
					alert("请选择行") ;
					return ;
				}
				
				//viewModel.dataTable1.removeRow(viewModel.dataTable1.currSelectedIndex)
				if(confirm("确定要删除吗！")){
				app.serverEvent().addDataTable("dataTable1", "current").fire({
					url : '/web_example/evt/dispatch',
					ctrl : 'iweb.DemoController',
					method : 'del',
					success : function(data) {
					} ,
					error: function(req, textStatus, errorThrown){
						if(req.responseJSON){
							var validateObj = req.responseJSON;
							if(validateObj.code){q
								jAlert(validateObj.code,"错误");
							}
						} 
					}
					
				})
			  }
			
			},
			saveInfo: function() {
				if(viewModel.dataTable1.currSelectedIndex == -1 ){//目前datatable还待完善，先一行一行的操作
					alert("请选择行") ;
					return ;
				}
				app.serverEvent().addDataTable("dataTable1", "change").fire({
					url : urlDispatch,
					ctrl : 'iweb.DemoController',
					method : 'update',
					success : function(data) {
					} 
				})
			},
			pageChange: function (pageIndex,pageSize){
				
				var dt = app.getDataTable("dataTable1")
				if(dt.hasPage(pageIndex)){
					dt.setCurrentPage(pageIndex)
				} else {
					viewModel.dataTable1.pageSize(pageSize) ;
					app.serverEvent().addDataTable("dataTable1","all").addParameter('pageIndex',pageIndex).fire({
						url : urlDispatch,
						ctrl : 'iweb.DemoController',
						method : 'loadData',
						success: function(data){
						}
					})
				}
				
			},
			upClick:function(){
				var index = viewModel.dataTable1.currSelectedIndex;
				if(index == -1 || index == 0) return;
				var selectRow = this.dataTable1.getRow(index)
				this.dataTable1.removeRow(index)
				this.dataTable1.insertRow(index-1,selectRow)
				
			},
			downClick:function(){
				var index = viewModel.dataTable1.currSelectedIndex;
				var length = viewModel.dataTable1.getAllRows().length;
				if(index == -1 || index == length-1) return;
				var selectRow = this.dataTable1.getRow(index)
				this.dataTable1.removeRow(index)
				this.dataTable1.insertRow(index+1,selectRow)
				
			},
			searchPage:function(){
				 var queryData = {};
		         $(".form-search").find(".input_search").each(function(){
		             queryData[this.name] = this.value;
		         });
		         viewModel.dataTable1.addParams( queryData) ;
		         app.serverEvent().addDataTable("dataTable1", "all").fire({
		 			url : '/web_example/evt/dispatch',
		 			ctrl : 'iweb.DemoController',
		 			method : 'loadData',
		 			success : function(data) {
		 			},
		 			error: function(req, textStatus, errorThrown){
						if(req.responseJSON){
							var validateObj = req.responseJSON;
							if(validateObj.code){
								jAlert(validateObj.code,"错误");
							}
						} 
						alert("查询错误 ") ;
					}
		 		})
			},
			combodata: [{
				pk: 'Y',
				name: '是'
			}, {
				pk: 'N',
				name: '否'
			}],
			comboSelect:function(item){
				window.item = item
			}
		}

		var app = $.createApp()
		app.init(viewModel, viewSpace)
        
		 window.vieModel = viewModel
		 window.app = app
		
		app.serverEvent().addDataTable("dataTable1", "all").fire({
			url : '/web_example/evt/dispatch',
			ctrl : 'iweb.DemoController',
			method : 'loadData',			 
			success : function(data) {
			}
		})
	}

	return {
		'template' : template,
		'init' : init
	};

})
