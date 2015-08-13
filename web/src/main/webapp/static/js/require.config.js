require.config({
	baseUrl: ".",
	paths: {
		text: "static/lib/requirejs/text",
		css: "static/lib/requirejs/css",
		jquery: "static/lib/jquery/jquery-1.11.2",
		bootstrap: 'static/lib/bootstrap/js/bootstrap',
		knockout: "static/lib/knockout/knockout-3.2.0.debug",
		uui: "static/lib/uui/js/u",
		wizard:"static/lib/jquery-bootstrap-wizard/jquery.bootstrap.wizard",
		director:"static/lib/director/director",
		'jquery.file.upload' : "static/libs/juqery-file-upload/9.9.2/js/jquery.fileupload",
		'jquery.ui.widget':"static/lib/jquery-ui/jquery.ui.widget",
		'jquery.iframe.transport':"static/lib/jquery-iframe-transport/jquery.iframe-transport",
		biz: "static/lib/uui/js/u.biz"
	},
	shim: {
		'uui':{
			deps: ["jquery","bootstrap"]
		},
		'bootstrap': {
			deps: ["jquery"]
		},
		'jquery.file.upload':{
			deps: ["jquery","jquery.ui.widget","jquery.iframe.transport","css!static/libs/juqery-file-upload/9.9.2/css/jquery.fileupload.css"]
		},
		biz:{
			deps: ["uui","knockout"]
		}
	}
});


//require.config({
//	baseUrl: "/web_example",
//	paths: {
//		text: "static/libs/requirejs/text",
//		css: "static/libs/requirejs/css",
//		jquery: "static/libs/jquery/jquery-1.11.2",
//		bootstrap: 'static/libs/bootstrap/js/bootstrap',
//		knockout: "static/libs/knockout/knockout-3.2.0.debug",
//		uui: "static/libs/uui/js/u",
//		 
//		director:"static/lib/director/director",
//		'jquery.file.upload' : "static/lib/juqery-file-upload/9.9.2/js/jquery.fileupload",
//		'jquery.ui.widget':"static/lib/jquery-ui/jquery.ui.widget",
//		'jquery.iframe.transport':"static/lib/jquery-iframe-transport/jquery.iframe-transport",
//		biz: "static/libs/uui/js/u.biz"
//	},
//	shim: {
//		'uui':{
//			deps: ["jquery","bootstrap"]
//		},
//		'bootstrap': {
//			deps: ["jquery"]
//		},
//		'jquery.file.upload':{
//			deps: ["jquery","jquery.ui.widget","jquery.iframe.transport","css!static/lib/juqery-file-upload/9.9.2/css/jquery.fileupload.css"]
//		},
//		biz:{
//			deps: ["uui","knockout"]
//		}
//	}
//});
