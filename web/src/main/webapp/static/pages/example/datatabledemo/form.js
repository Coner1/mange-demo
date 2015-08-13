define([ 'jquery','knockout','text!static/pages/example/datatabledemo/form.html', 'css!static/css/temp.css' ], function($,ko,template) {
	
	var init = function(id, viewSpace) {
		//debugger
		$("#btnW").on('click',function(){
			
			var demo = {
            		"name" : $('#name').val(),
            		"sex" :$('#sex').val(),
            		"age" :$('#age').val(),
            		"code" :$('#code').val(),
            		"company" :$('#company').val(),
            		"department":$('#department').val(),
            		"email":$('#email').val()
            }
			
			
			$.ajax({
			    url: $ctx+"/cxf/jaxrs/weixin/postWebservices",
                type: 'post',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(demo) , 
                success: function (json) {
                	alert(json.msg) ;
                }
		}) 
	})
		
		$('#btnS').on('click',function(){
			
			var demo = {
            		"name" : $('#name').val(),
            		"sex" :$('#sex').val(),
            		"age" :$('#age').val(),
            		"code" :$('#code').val(),
            		"company" :$('#company').val(),
            		"department":$('#department').val(),
            		"email":$('#email').val()
            }
			
			$.ajax({
			    url: $ctx+"/weixin/winxinservlet",
                type: 'post',
                data: $.param(demo),
                success: function (json) {
                	alert(json ) ;
                }
			
			
		})
			
	})
  }
		 			
					
					
				

	return {
		'template' : template,
		'init' : init
	};

})