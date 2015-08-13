define([ 'jquery', 'knockout', 'text!static/pages/yzqc/mbe_areamg/mbe_areamg_detail.html'],
	function($, ko, template) {
		var viewModel = {
			data : ko.observable({})
		}
		viewModel.submitForm = function(){
			var saveUrl = $ctx + '/mbe_areamg/add.json';
			if(viewModel.data().pkAreamg != null){
				saveUrl = $ctx + '/mbe_areamg/update.json';
			}
			alert(saveUrl);
			$.ajax({
				type: 'POST',
				contentType: 'application/json', 
				url: saveUrl,
				data: JSON.stringify(viewModel.data()),
				dataType: 'json',
				success: function(data) {
					if(data!=null){  
						jAlert("保存成功!","提示");
						window.location.href="#/yzqc/mbe_areamg/mbe_areamg_list"; 
					}
				},
				error: function(req, textStatus, errorThrown){
					if(req.responseJSON){
						var validateObj = req.responseJSON;
						if(validateObj.code){
							jAlert(validateObj.code,"错误");
						}
					} 
					//jAlert("保存失败!","错误");
				}
			});
		}
	
		var loadData = function(id) {
			var infoUrl = $ctx + '/mbe_areamg/create.json';
			if(id && id!=0){
				infoUrl = $ctx + '/mbe_areamg/update/' + id+".json";
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