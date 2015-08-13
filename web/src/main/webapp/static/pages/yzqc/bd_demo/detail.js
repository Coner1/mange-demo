define([ 'jquery', 'knockout', 'text!static/pages/yzqc/bd_demo/detail.html'],
	function($, ko, template) {
		var viewModel = {
			data : ko.observable({})
		}
		
		viewModel.submitForm = function(){
			var saveUrl = $ctx + '/member/add';
			if(viewModel.data().id != 0){
				saveUrl = $ctx + '/example/demo/update';
			} 
			//alert(JSON.stringify(viewModel.data()));
			$.ajax({
				type: 'POST',
				contentType: 'application/json', 
				url: saveUrl,
				data: JSON.stringify(viewModel.data()),
				dataType: 'json',
				success: function(data) {
					if(data!=null){  
						jAlert("保存成功!","提示");
					}
				},
				error: function(req, textStatus, errorThrown){
					if(req.responseJSON){
						var validateObj = req.responseJSON;
						if(validateObj.code){q
							jAlert(validateObj.code,"错误");
						}
					} 
					//jAlert("保存失败!","错误");
				}
			});
		}
	
		var loadData = function(id) {
			var infoUrl = $ctx + '/example/demo/create';
			if(id && id!=0){
				infoUrl = $ctx + '/example/demo/update/' + id;
			}
			$.ajax({
				type: 'GET',
				url: infoUrl,
				dataType: 'json',
				success: function(data) {
					viewModel.data(data);
				},
				error: function() {
					jAlert("获取详细信息失败!","错误");
				}
			});
		}
	
		var init = function(params) {
			loadData(params[0]);
		}
		
		return {
			'model' : viewModel,
			'template' : template,
			'init' : init
		};
	}
);