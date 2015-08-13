( function( factory ) {
	if ( typeof define === "function" && define.amd ) {
		// AMD. Register as an anonymous module.
		define(["jquery", "knockout"], factory );
	} else {
		// Browser globals
		factory($, ko);
	}
}( function($, ko) {
;
	//+ function($, exports) {
	'use strict';
	var IWEB_VERSION = "1.0.0"
	var IWEB_THEME = "i_theme"
	var IWEB_LOCALE = "i_locale"
	var IWEB_USERCODE = "usercode"
	var LOG_Level = "ill"
	var systemTimeZoneOffset = -480; //TODO 目前默认即取东八区 -60*8 = -480
	var IWEB_CONTEXT_PATH = "contextpath"
	var iweb = {
		version: IWEB_VERSION
	};

	if (!window.getCookie) {
		window.getCookie = function(sName) {
			var sRE = "(?:; )?" + sName + "=([^;]*);?";
			var oRE = new RegExp(sRE);

			if (oRE.test(document.cookie)) {
				return decodeURIComponent(RegExp["$1"]);
			} else
				return null;
		};
	}

	/**
	 * 创建一个带壳的对象,防止外部修改
	 * @param {Object} proto
	 */
	window.createShellObject = function(proto) {
		var exf = function() {}
		exf.prototype = proto;
		return new exf();
	};


	// 导出到window对象中

	//core context
	(function() {
		// 从Cookie中获取初始化信息
		var environment = {}

		/**
		 * client attributes
		 */
		var clientAttributes = {};

		var sessionAttributes = {};

		var maskerMeta = {
			'float': {
				precision: 2
			},
			'datetime': {
				format: 'yyyy-MM-dd hh:mm:ss',
				metaType: 'DateTimeFormatMeta',
				speratorSymbol: '-'
			},
			'time':{
				format:'hh:mm:ss'
			},
			'date':{
				format:'yyyy-MM-dd'
			},
			'currency':{
				precision: 2,
				curSymbol: '￥'
			}
		}

		var fn = {}

		/**
		 * 获取环境信息
		 * @return {environment}
		 */
		fn.getEnvironment = function() {
			return createShellObject(environment);
		}

		/**
		 * 获取客户端参数对象
		 * @return {clientAttributes}
		 */
		fn.getClientAttributes = function() {
			var exf = function() {}
			return createShellObject(clientAttributes);
		}


		fn.setContextPath = function(contextPath) {
			return environment[IWEB_CONTEXT_PATH] = contextPath
		}
		fn.getContextPath = function(contextPath) {
				return environment[IWEB_CONTEXT_PATH]
			}
			/**
			 * 设置客户端参数对象
			 * @param {Object} k 对象名称
			 * @param {Object} v 对象值(建议使用简单类型)
			 */
		fn.setClientAttribute = function(k, v) {
				clientAttributes[k] = v;
			}
			/**
			 * 获取会话级参数对象
			 * @return {clientAttributes}
			 */
		fn.getSessionAttributes = function() {
			var exf = function() {}
			return createShellObject(sessionAttributes);
		}

		/**
		 * 设置会话级参数对象
		 * @param {Object} k 对象名称
		 * @param {Object} v 对象值(建议使用简单类型)
		 */
		fn.setSessionAttribute = function(k, v) {
			sessionAttributes[k] = v;
			setCookie("ISES_" + k, v);
		}

		/**
		 * 移除客户端参数
		 * @param {Object} k 对象名称
		 */
		fn.removeClientAttribute = function(k) {
			clientAttributes[k] = null;
			execIgnoreError(function() {
				delete clientAttributes[k];
			})
		}

		/**
		 * 获取根组件
		 */
		fn.getRootComponent = function() {
			return this.rootComponet;
		}

		/**
		 * 设置根组件
		 * @param {Object} component
		 */
		fn.setRootComponent = function(component) {
			this.rootComponet = component
		}

		/**
		 * 获取主题名称
		 */
		fn.getTheme = function() {
			return this.getEnvironment().theme
		}

		/**
		 * 获取地区信息编码
		 */
		fn.getLocale = function() {
			return this.getEnvironment().locale
		}

		/**
		 * 收集环境信息(包括客户端参数)
		 * @return {Object}
		 */
		fn.collectEnvironment = function() {
			var _env = this.getEnvironment();
			var _ses = this.getSessionAttributes();

			for (var i in clientAttributes) {
				_ses[i] = clientAttributes[i];
			}
			_env.clientAttributes = _ses;
			return _env
		}

		fn.changeTheme = function(theme) {
			environment.theme = theme;
			setCookie(IWEB_THEME, theme)
			$(document).trigger("themeChange");
		}
		fn.changeLocale = function(locale) {
				econsoleconsolenvironment.locale = locale;
				setCookie(IWEB_LOCALE, locale)
				$(document).trigger("localeChange");
			}
			/**
			 * 设置数据格式信息
			 * @param {String} type
			 * @param {Object} meta
			 */
		fn.setMaskerMeta = function(type, meta) {
			if (!maskerMeta[type])
				maskerMeta[type] = meta
			else{
				if (typeof meta != 'object')
					maskerMeta[type] = meta
				else			
					for (var key in meta){
						maskerMeta[type][key] = meta[key]
					}
			}
		}
		fn.getMaskerMeta = function(type) {
			return maskerMeta[type]
		}

		/**
		 * 注册系统时间偏移量
		 * @param {Object} offset
		 */
		fn.registerSystemTimeZoneOffset = function(offset) {
			systemTimeZoneOffset = offset;
		}

		/**
		 * 获取系统时间偏移量
		 */
		fn.getSystemTimeZoneOffset = function() {
			return systemTimeZoneOffset;
		};
		var device = {
			Android: function() {
				return /Android/i.test(navigator.userAgent);
			},
			BlackBerry: function() {
				return /BlackBerry/i.test(navigator.userAgent);
			},
			iOS: function() {
				return /iPhone|iPad|iPod/i.test(navigator.userAgent);
			},
			Windows: function() {
				return /IEMobile/i.test(navigator.userAgent);
			},
			any: function() {
				return (this.Android() || this.BlackBerry() || this.iOS() || this.Windows());
			},
			pc: function() {
				return !this.any();
			},
			Screen: {
				size: noop,
				direction: noop

			}
		}
		fn.getDevice = function() {
			return device;
		}


		environment.theme = getCookie(IWEB_THEME)
		environment.locale = getCookie(IWEB_LOCALE)
		environment.timezoneOffset = (new Date()).getTimezoneOffset()
		environment.usercode = getCookie(IWEB_USERCODE)
			//init session attribute
		document.cookie.replace(/ISES_(\w*)=([^;]*);?/ig, function(a, b, c) {
			sessionAttributes[b] = c;
		})

		var Core = function() {}
		Core.prototype = fn;

		iweb.Core = new Core();

	})();




	//console logger
	(function() {
		var consoleLog;
		var level = getCookie(IWEB_USERCODE)
		if (typeof Log4js != "undefined") {
			consoleLog = new Log4js.Logger("iweb");
			consoleLog.setLevel(Log4js.Level.ERROR);
			var consoleAppender = new Log4js.ConsoleAppender(consoleLog, true);
			consoleLog.addAppender(consoleAppender);
		} else {
			consoleLog = {
				LEVEL_MAP: {
					"OFF": Number.MAX_VALUE,
					"ERROR": 40000,
					"WARN": 30000,
					"INFO": 20000,
					"DEBUG": 10000,
					"TRACE": 5000,
					"ALL": 1
				},
				level: 40000,
				setLevel: function(level) {
					if (level) {
						var l = this.LEVEL_MAP[level.toUpperCase()]
						if (l) {
							this.level = l;
						}
					}

				},
				isDebugEnabled: function() {
					return (this.LEVEL_MAP.DEBUG >= this.level && console)
				},
				isTraceEnabled: function() {
					return (this.LEVEL_MAP.TRACE >= this.level && console)
				},
				isInfoEnabled: function() {
					return (this.LEVEL_MAP.INFO >= this.level && console)
				},
				isWarnEnabled: function() {
					return (this.LEVEL_MAP.WARN >= this.level && console)
				},
				isErrorEnabled: function() {
					return (this.LEVEL_MAP.ERROR >= this.level && console)
				},
				debug: function() {
					if (this.isDebugEnabled()) {
						console.debug.call(console, arguments)
					}
				},
				warn: function() {
					if (this.isWarnEnabled()) {
						console.debug.call(console, arguments)
					}
				},
				info: function() {
					if (this.isInfoEnabled()) {
						console.debug.call(console, arguments)
					}
				},
				trace: function() {
					if (this.isTraceEnabled()) {
						console.debug.call(console, arguments)
					}
				},
				error: function() {
					if (this.isErrorEnabled()) {
						console.debug.call(console, arguments)
					}
				}
			}
		}
		consoleLog.setLevel(level);
		iweb.log = consoleLog;
		iweb.debugMode = false;
	})();

	window.iweb = iweb;

	var noop = function() {}

;

	
	var App = function(){
		this.dataTables = {}
//		this.comps = {}
	}
	
	App.fn = App.prototype
	
	App.fn.init = function(viewModel, element){
		var self = this
		this.element = element || document.body
		$(this.element).find('[u-meta]').each(function() {
			if ($(this).data('u-meta')) return
			if ($(this).parents('[u-meta]').length > 0) return 
			var options = JSON.parse($(this).attr('u-meta'))
			if(options && options['type']) {
				if (self.adjustFunc)
					self.adjustFunc.call(self, options);
				var comp = $.compManager._createComp(this, options, viewModel, self)
				if (comp)
//					this.comps[comp.getId()] = comp
					$(this).data('u-meta', comp)
			}
		})	
		_getDataTables(this, viewModel)	
//		ko.cleanNode(this.element)
		try{
			ko.applyBindings(viewModel, this.element)
		}catch(e){
			iweb.log.error(e)
		}
	}
	
	App.fn.setAdjustMetaFunc = function(adjustFunc){
		this.adjustFunc = adjustFunc
	}
	
	App.fn.addDataTable = function(dataTable){
		this.dataTables[dataTable.id] = dataTable
		return this
	}
	App.fn.getDataTable = function(id){
		return this.dataTables[id]
	}
	
	App.fn.getDataTables = function(){
		return this.dataTables
	}
	
	App.fn.getComp = function(compId){
		var returnComp = null;
		$(this.element).find('[u-meta]').each(function() {
			if ($(this).data('u-meta')){
				var comp = $(this).data('u-meta')
				if (comp.id == compId){
					returnComp = comp;
					return false;
				}
				    
			}	
		})
		return returnComp;
	}
	
	/**
	 * 获取某区域中的所有控件
	 * @param {object} element
	 */
	App.fn.getComps = function(element){
		element = element ? element : this.element
		var returnComps = [];
		$(element).find('[u-meta]').each(function() {
			if ($(this).data('u-meta')){
				var comp = $(this).data('u-meta')
				if (comp)
					returnComps.push(comp);
			}	
		})
		return returnComps;		
	}
	
	/**
	 * 根据类型获取控件
	 * @param {String} type
	 * @param {object} element
	 */
	App.fn.getCompsByType = function(type,element){
		element = element ? element : this.element
		var returnComps = [];
		$(element).find('[u-meta]').each(function() {
			if ($(this).data('u-meta')){
				var comp = $(this).data('u-meta')
				if (comp && comp.type == type)
					returnComps.push(comp);
			}	
		})
		return returnComps;			
	}
	
	
	App.fn.getEnvironment = function(){
		return window.iweb.Core.collectEnvironment()
	}
	
	App.fn.setClientAttribute = function(k,v){
		window.iweb.Core.setClientAttribute(k,v)
	}
	
	App.fn.getClientAttribute = function(k){
		window.iweb.Core.getClientAttributes()[k]
	}
	
	App.fn.serverEvent = function(){
		return new ServerEvent(this)
	}
	
	App.fn.ajax = function(params){
		params = this._wrapAjax(params) 
		$.ajax(params)		
	}
	
	App.fn._wrapAjax = function(params){
		var self = this
		var orignSuccess =  params.success
		var orignError =  params.error
		var deferred =  params.deferred;
		if(!deferred || !deferred.resolve){
			deferred = {resolve:function(){},reject:function(){}}
		} 
		params.success = function(data,state,xhr){
			if(processXHRError(self,data,state,xhr)){
				orignSuccess.call(null, data)
				self._successFunc(data, deferred)
			}else{
				deferred.reject();
			}
		}
		params.error=function(data,state,xhr){
			if(processXHRError(self,data,state,xhr)){
				orignError.call(null, data)
				self._successFunc(data, deferred)
			}else{
				deferred.reject();
			}
		}
		if(params.data)
			params.data.environment=ko.toJSON(window.iweb.Core.collectEnvironment());
		else
			params.data = {environment:ko.toJSON(window.iweb.Core.collectEnvironment())}
		return params		
	}
	
	App.fn._successFunc = function(data, deferred){
		deferred.resolve();
	}
	
	
//	window.processXHRError  = function (rsl,state,xhr) {
//		if(xhr.getResponseHeader && xhr.getResponseHeader("X-Error")){
//			$.showMessageDialog({type:"info",title:"提示",msg: rsl["message"],backdrop:true});
//			if(rsl["operate"]){
//				eval(rsl["operate"]);
//			}
//			return false;
//		}
//		return true;
//	};		
//	
	App.fn.setUserCache = function(key,value){
		var userCode = this.getEnvironment().usercode;
		if(!userCode)return;
		localStorage.setItem(userCode+key,value);
	}
	
	App.fn.getUserCache = function(key){
		var userCode = this.getEnvironment().usercode;
		if(!userCode)return;
		return localStorage.getItem(userCode+key);
	}
	
	App.fn.removeUserCache = function(key){
		var userCode = this.getEnvironment().usercode;
		if(!userCode)return;
		localStorage.removeItem(userCode+key);
	}
	
	App.fn.setCache = function(key,value){
		localStorage.setItem(key,value);
	}
	
	App.fn.getCache = function(key){
	   return localStorage.getItem(key);
	}
	
	App.fn.removeCache = function(key){
		localStorage.removeItem(key)
	}
	
	App.fn.setSessionCache = function(key,value){
		sessionStorage.setItem(key,value)
	}
	
	App.fn.getSessionCache = function(key){
		return sessionStorage.getItem(key)
	}
	
	App.fn.removeSessionCache = function(key){
		sessionStorage.removeItem(key)
	}
	
	App.fn.setEnable = function(enable){
		$(this.element).find('[u-meta]').each(function() {
			if ($(this).data('u-meta')){
				var comp = $(this).data('u-meta')
				if (comp.setEnable)
					comp.setEnable(enable)
			}	
		})		
	}
	
	var ServerEvent = function(app){
		this.app = app
		this.datas = {}			
		this.params = {}
		this.event = null
		this.ent = window.iweb.Core.collectEnvironment()
		if (window.pako && iweb.debugMode == false)
			this.compression = true
	}
	
	ServerEvent.DEFAULT = {
		async: true,
		singleton: true,
		url: (iweb.Core.getContextPath() || '/iwebap') + '/evt/dispatch'
	}
	
	ServerEvent.fn = ServerEvent.prototype
	
	ServerEvent.fn.addDataTable = function(dataTableId, rule){
		var dataTable = this.app.getDataTable(dataTableId)
		this.datas[dataTableId] = dataTable.getDataByRule(rule)
		return this
	}
	
	ServerEvent.fn.setCompression = function(compression){
		if (!window.pako && compression == true)
			iweb.log.error("can't compression, please include  pako!")
		else	
			this.compression = compression
	}
	
	/**
	 * 
	 * @param {Object} dataTabels
	 * ['dt1',{'dt2':'all'}]
	 */
	ServerEvent.fn.addDataTables = function(dataTables){
		for(var i = 0; i<dataTables.length; i++){
			var dt = dataTables[i]
			if (typeof dt == 'string')
				this.addDataTable(dt)
			else{
				for (key in dt)
					this.addDataTable(key, dt[key])
			}
		}
		return this
	}
	
	ServerEvent.fn.addAllDataTables = function(rule){
		var dts = this.app.dataTables 
		for (var i = 0; i< dts.length; i++){
			this.addDataTable(dts[i].id, rule)
		}
	}
	
	
	ServerEvent.fn.addParameter = function(key,value){
		this.params[key] = value
		return this
	}
	
	ServerEvent.fn.setEvent = function(event){
		if (true)
			this.event = event
		else
			this.event = _formatEvent(event)
		return this	
	}
	
	var _formatEvent = function(event){
		return event
	}
	
	
//	app.serverEvent().fire({
//		ctrl:'CurrtypeController',
//		event:'event1',
//		success:
//		params:
//	})	
	ServerEvent.fn.fire = function(p){
		var self = this
//		params = $.extend(ServerEvent.DEFAULT, params);
		var data = this.getData()
		data.parameters = ko.toJSON(this.params)
		var params = {
			type:"POST",
			data: p.params || {},
			url: p.url || ServerEvent.DEFAULT.url,
			async: p.async || ServerEvent.DEFAULT.async,
			singleton: p.singleton || ServerEvent.DEFAULT.singleton,
			success: p.success,
			error: p.error
		}
		params.data.ctrl = p.ctrl
		params.data.method = p.method
        if (this.event)
			params.data.event = ko.toJSON(this.event)
		var preSuccess = params.preSuccess || function(){}	
		var orignSuccess =  params.success || function(){}
		var orignError = params.error || function(){}
		this.orignError = orignError
		var deferred =  params.deferred;
		if(!deferred || !deferred.resolve){
			deferred = {resolve:function(){},reject:function(){}}
		} 
		params.success = function(data,state,xhr){
			if(processXHRError(self, data,state,xhr)){
				preSuccess.call(null, data)
				self._successFunc(data, deferred)
				orignSuccess.call(null, data.custom)
				deferred.resolve();
			}else{
				deferred.reject();
			}
		}
		params.error=function(data,state,xhr){
			if(processXHRError(self, data,state,xhr)){
				orignError.call(null, data.custom)
//				self._successFunc(data, deferred)
			}else{
				deferred.reject();
			}
		}
		params.data = $.extend(params.data,data);
		$.ajax(params)
		
	}
	
	ServerEvent.fn.getData = function(){
		var envJson = ko.toJSON(this.app.getEnvironment()),
			datasJson = ko.toJSON(this.datas)
		if (this.compression){
			envJson = encodeBase64(window.pako.gzip(envJson))
			datasJson = encodeBase64(window.pako.gzip(datasJson))
		}
		return 	{
			environment: envJson,
			dataTables: datasJson,
			compression: this.compression
		}
	}
	
	ServerEvent.fn._successFunc = function(data, deferred){
		var dataTables = data.dataTables
		var dom = data.dom
		if (dom)
			this.updateDom(JSON.parse(dom))
		if (dataTables)
			this.updateDataTables(dataTables, deferred)
	}
	
	ServerEvent.fn.updateDataTables = function(dataTables, deferred){
		for (var key in dataTables){
			var dt = this.app.getDataTable(key)
			if (dt)
				dt.setData(dataTables[key])
		}
	}
	
	ServerEvent.fn.setSuccessFunc = function(func){
		this._successFunc = func
	}
	
	ServerEvent.fn.updateDom = function(){
		$.each( dom, function(i, n){
		 	var vo = n.two
			var $key = $(n.one)
			_updateDom($key, vo)
		});
	}
	
	function _updateDom($key, vos){
		for (var i in vos){
			var vo = vos[i]
			for (var key in vo){
				var props = vo[key]
				if (key == 'trigger'){
					$key.trigger(props[0])	
				}
				else{
					if ($.isArray(props)){
						$.each(props, function(i, n){
						  	$key[i](n)		
						});
					}
					else
						try{
							$key[i](vo)
						}catch(error){
							$key[i](vo[i])
						}
				}
			}
		}
	}
		
	var processXHRError  = function (self, rsl,state,xhr) {
		if(typeof rsl ==='string')
			rsl = JSON.parse(rsl)
		if(xhr.getResponseHeader && xhr.getResponseHeader("X-Error")){
			if (self.orignError)
				self.orignError.call(self,rsl,state,xhr)
			else{
				$.showMessageDialog({type:"info",title:"提示",msg: rsl["message"],backdrop:true});
				if(rsl["operate"]){
					eval(rsl["operate"]);
				}
				return false;
			}
		}
		return true;
	};	
	
	$.createApp = function(){
		var app = new App()
		return app
	}

	var _getDataTables = function(app, viewModel){
		for(var key in viewModel){
			if (viewModel[key] instanceof $.DataTable){
				viewModel[key].id = key
				app.addDataTable(viewModel[key])
			}	
		}
	}
	
	
;
/* ========================================================================
 * UUI: dataTable.js v1.0.0
 *
 * ========================================================================
 * Copyright 2015 yonyou, Inc.
 * ======================================================================== */

	'use strict';
	
	var Events = function(){
	}
	
	Events.fn = Events.prototype
	/**
	 *绑定事件
	 */
	Events.fn.on = function(name, callback) {
		name = name.toLowerCase()
		this._events || (this._events = {})
		var events = this._events[name] || (this._events[name] = [])
		events.push({
			callback: callback
		})
		return this;
	}

	/**
	 * 触发事件
	 */
	Events.fn.trigger = function(name) {
		name = name.toLowerCase()
		if (!this._events || !this._events[name]) return this;
		var args =  Array.prototype.slice.call(arguments, 1);
		var events = this._events[name];
		for (var i = 0, count = events.length; i < count; i++) {
			events[i].callback.apply(this, args);
		}
		return this;
	}	
	
	
	Events.fn.getEvent = function(name){
		name = name.toLowerCase()
		this._events || (this._events = {})
		return this._events[name]
	}	
	
	/**===========================================================================================================
	 * 
	 * 数据模型   
	 * 
	 * ===========================================================================================================
	 */
	
	var DataTable = function(options){
		this.id = options['id']
		this.meta = options['meta']
//		this.currentMeta = ko.observable(mt)
		this.enable = options['enable'] || DataTable.DEFAULTS.enable
		this.pageSize = ko.observable(options['pageSize'] || DataTable.DEFAULTS.pageSize)
		this.pageIndex = ko.observable(options['pageIndex'] || DataTable.DEFAULTS.pageIndex)
		this.totalPages = ko.observable(options['totalPages'] || DataTable.DEFAULTS.totalPages)
		this.pageCache = DataTable.DEFAULTS.pageCache
		this.rows = ko.observableArray([])
//		this.currentRow = ko.observable()
		this.selectedIndices = []
		this.currSelectedIndex = -1 // ko.observable()
		this.cachedPages = []
		this.createDefaultEvents()
		this.metaChange = ko.observable(1)
		this.valueChange = ko.observable(1)
		this.enableChange = ko.observable(1)
		this.params = options['params'] || {}
	}
	
	DataTable.fn = DataTable.prototype = new Events()
	
	DataTable.DEFAULTS = {
		pageSize:20,
		pageIndex:0,
		totalPages:20,
		pageCache:false,
		enable: true
	}
	
	//事件类型
	DataTable.ON_ROW_SELECT = 'select'
	DataTable.ON_ROW_UNSELECT = 'unSelect'
	DataTable.ON_ROW_ALLSELECT = 'allSelect'
	DataTable.ON_ROW_ALLUNSELECT = 'allUnselect'
	DataTable.ON_VALUE_CHANGE = 'valueChange'
//	DataTable.ON_AFTER_VALUE_CHANGE = 'afterValueChange'
//	DataTable.ON_ADD_ROW = 'addRow'
	DataTable.ON_INSERT = 'insert'
	DataTable.ON_UPDATE = 'update'
	DataTable.ON_DELETE = 'delete'
	DataTable.ON_DELETE_ALL = 'deleteAll'
	DataTable.ON_LOAD = 'load'
	
	DataTable.SUBMIT = {
		current: 'current',
		all:	'all',
		select:	'select',
		change: 'change'
	}
	
	
	DataTable.fn.createDefaultEvents = function(){
		//this.on()
	}
	
	DataTable.fn.addParam = function(key, value){
			this.params[key] = value
	}
	
	DataTable.fn.addParams = function(params){
		for(var key in params){
			this.params[key] = params[key]
		}
	}
	
	DataTable.fn.getParam = function(key){
		return this.params[key]
	}
	
	/**
	 * 获取meta信息，先取row上的信息，没有时，取dataTable上的信息
	 * @param {Object} fieldName
	 * @param {Object} key
	 * @param {Object} row
	 */
	DataTable.fn.getMeta = function(fieldName, key){
		if (arguments.length == 0)
			return this.meta
		return this.meta[fieldName][key]
	}
	
	DataTable.fn.setMeta = function(fieldName, key, value){
		this.meta[fieldName][key] = value
		this.metaChange(- this.metaChange())
	}
	
	DataTable.fn.setCurrentPage = function(pageIndex){
		this.pageIndex(pageIndex)
		var cachedPage = this.cachedPages[this.pageIndex()]
		if(cachedPage) {
			this.removeAllRows()
			this.setRows(cachedPage.rows)
			this.setRowsSelect(cachedPage.selectedIndcies)
		}
	}
	
	DataTable.fn.isChanged = function(){
		var rows = this.getAllRows()
		for (var i = 0; i < rows.length; i++){
			if (rows[i].status != Row.STATUS.NORMAL)
				return true
		}
		return false
	}
	
	
	/**
	 *设置数据
	 * {pageIndex:1,pageSize:10,rows:[{status:'nrm', data:['001','a','b']},{},{}]}
	 * 
	 */
	DataTable.fn.setData = function(data){
		var newIndex = data.pageIndex, 
			newSize = data.pageSize || this.pageSize(),
			newTotalPages = data.totalPages || this.totalPages(),
			type = data.type 
//		if (newSize != this.pageSize())
//			this.cacheRows = []
//		else if (this.pageCache)
//			this.cacheRows[this.pageIndex()] = this.rows()
				
		this.setRows(data.rows)
		this.pageIndex(newIndex)
		this.pageSize(newSize)
		this.totalPages(newTotalPages)
		this.updateSelectedIndices()
		if (data.select && data.select.length > 0)
			this.setRowsSelect(data.select)

		
		// 加load事件传入参数为rows  lyk
		// 数组每一项中取data，data取每一项的value
//		this.trigger(DataTable.ON_LOAD,this.getData());
		//this.trigger(DataTable.ON_LOAD,this.rows._latestValue)
		
	}
	
	
	DataTable.fn.setRows = function(rows){
		for (var i = 0; i < rows.length; i++){
			var r = rows[i]
			if (!r.id)
				r.id = Row.getRandomRowId()
			if (r.status == Row.STATUS.DELETE){
				this.removeRowByRowId(r.id)
			}
			else{
				var row = this.getRowByRowId(r.id)
				if (row){
					row.updateRow(r)
					if (!$.isEmptyObject(r.data))
						this.trigger(DataTable.ON_UPDATE,{
							index:i,
							rows:[row]
						})		
				}	
				else{
					row = new Row({parent:this,id:r.id})
					row.setData(rows[i])
					this.addRow(row)
				}
			}
		}	
	}
	
	DataTable.fn.cacheCurrentPage = function(){
		if(this.pageCache) {
			this.cachedPages[this.pageIndex()] = {"rows":this.rows().slice(), "selectedIndcies":this.selectedIndices.slice(), "pageSize":this.pageSize()}
		}
	}
	
	DataTable.fn.hasPage = function(pageIndex){
		return (this.pageCache && this.cachedPages[pageIndex]  && this.cachedPages[pageIndex].pageSize == this.pageSize()) ? true : false
	}

	DataTable.fn.copyRow = function(index, row){
		var newRow = new Row({parent:this})
		if(row) {
			newRow.setData(row.getData())
		}
		this.insertRows(index === undefined ? this.rows().length : index, [newRow])
	}

	/**
	 *追加行 
	 */
	DataTable.fn.addRow = function(row){
		this.insertRow(this.rows().length, row)
	}
	
	DataTable.fn.insertRow = function(index, row){
		if(!row){
			row = new Row({parent:this})
		}
		this.insertRows(index, [row])
	}
	
	DataTable.fn.insertRows = function(index, rows){
//		if (this.onBeforeRowInsert(index,rows) == false)
//			return
		for ( var i = 0; i < rows.length; i++) {
			this.rows.splice(index + i, 0, rows[i])
			this.updateSelectedIndices(index + i, '+')
		}
		this.trigger(DataTable.ON_INSERT,{
			index:index,
			rows:rows
		})
	}
	
	/**
	 * 创建空行
	 */
	DataTable.fn.createEmptyRow = function(){
		var r = new Row({parent:this})
		this.addRow(r)
		return r
	}

	DataTable.fn.removeRowByRowId = function(rowId){
		var index = this.getIndexByRowId(rowId)
		if (index != -1)
			this.removeRow(index)
	}

	DataTable.fn.removeRow = function(index) {
		if (index instanceof Row){
			index = this.getIndexByRowId(index.rowId)
		}
		this.removeRows([ index ]);
	}
	
	DataTable.fn.removeAllRows = function(){
		this.rows([])
		this.selectedIndices = []
		this.trigger(DataTable.ON_DELETE_ALL)
	}
	
	DataTable.fn.removeRows = function(indices) {
		indices = this._formatToIndicesArray(indices)
		indices = indices.sort()
		var rowIds = []
		for (var i = indices.length - 1; i >= 0; i--) {
			var index = indices[i]
			var delRow = this.rows()[index];
			if (delRow == null) {
				continue;
			}
			rowIds.push(delRow.rowId)
			this.rows.splice(index, 1);
			this.updateSelectedIndices(index,'-')
		}	
		this.trigger(DataTable.ON_DELETE,{
			indices:indices,
			rowIds:rowIds
		})
	}

	DataTable.fn.setAllRowsSelect = function(){
		var rows = this.getAllRows()
		var indices = []
		for (var i in rows){
			indices.push(this.getIndexByRowId(rpws[i].rowId))
		}
		this.setRowsSelect(indices)
	}
	
	/**
	 * 设置选中行，清空之前已选中的所有行
	 */
	DataTable.fn.setRowSelect = function(index){
		if (index instanceof Row){
			index = this.getIndexByRowId(index.rowId)
		}
		this.setRowsSelect([index])
	}	
	
	DataTable.fn.setRowsSelect = function(indices){
		indices = this._formatToIndicesArray(indices)
		this.setAllRowsUnSelect()
		this.selectedIndices = indices
		var index = this.getSelectedIndex()
//		this.currSelectedIndex = index
//		this.currentRow(this.rows()[index])
		this.setCurrentRow(index)
		var rowIds = this.getRowIdsByIndices(indices)
		this.trigger(DataTable.ON_ROW_SELECT, {
			indices:indices,
			rowIds:rowIds
		})
	}

	/**
	 * 添加选中行，不会清空之前已选中的行
	 */
	DataTable.fn.addRowsSelect = function(indices){
		indices = this._formatToIndicesArray(indices)
		for (var i=0; i< indices.length; i++){
			var ind = indices[i], toAdd = true
			for(var j=0; j< this.selectedIndices.length; j++) {
				if(this.selectedIndices[j] == ind) {
					toAdd = false
				}
			}
			if(toAdd) {
				this.selectedIndices.push(indices[i])
			}
		}
		var index = this.getSelectedIndex()
//		this.currSelectedIndex = index
//		this.currentRow(this.rows()[index])
		this.setCurrentRow(index)
		var rowIds = this.getRowIdsByIndices(indices)
		this.trigger(DataTable.ON_ROW_SELECT, {
			indices:indices,
			rowIds:rowIds
		})
	}

	DataTable.fn.getRowIdsByIndices = function(indices){
		var rowIds = []
		for(var i=0; i<indices.length; i++){
			rowIds.push(this.getRow(indices[i]).rowId)
		}
		return rowIds
	}

	/**
	 * 全部取消选中
	 */
	DataTable.fn.setAllRowsUnSelect = function(){
		this.selectedIndices = []
		this.trigger(DataTable.ON_ROW_ALLUNSELECT)
	}
	
	/**
	 * 取消选中
	 */
	DataTable.fn.setRowUnSelect = function(index){
		this.setRowsUnSelect([index])
	}	
	
	DataTable.fn.setRowsUnSelect = function(indices){
		indices = this._formatToIndicesArray(indices)
		for(var i=0; i<indices.length; i++){
			var index = indices[i]
			var pos = this.selectedIndices.indexOf(index)
			if (pos != -1)
				this.selectedIndices.splice(pos,1)
		}
		var rowIds = this.getRowIdsByIndices(indices)
		this.trigger(DataTable.ON_ROW_UNSELECT, {
			indices:indices,
			rowIds:rowIds
		})
	}
	
	/**
	 * 
	 * @param {Object} index 要处理的起始行索引
	 * @param {Object} type   增加或减少  + -
	 */
	DataTable.fn.updateSelectedIndices = function(index, type){
		if (this.selectedIndices == null || this.selectedIndices.length == 0)
			return
		for (var i = 0, count= this.selectedIndices.length; i< count; i++){
			if (type == '+'){
				if (this.selectedIndices[i] >= index)
					this.selectedIndices[i] = parseInt(this.selectedIndices[i]) + 1
			}
			else if (type == '-'){
				if (this.selectedIndices[i] == index)
					this.selectedIndices.splice(i,1)
				else if(this.selectedIndices[i] > index)
					this.selectedIndices[i] = this.selectedIndices[i] -1
			}
		}
//		if (type == '+')
//			this.selectedIndices.push(index)
		var currIndex = this.getSelectedIndex()
//		this.currSelectedIndex = currIndex
//		this.currentRow(this.rows()[currIndex])
		this.setCurrentRow(currIndex)
	}
	
	/**
	 * 获取选中行索引，多选时，只返回第一个行索引
	 */
	DataTable.fn.getSelectedIndex = function(){
		if (this.selectedIndices == null || this.selectedIndices.length == 0)
			return -1
		return this.selectedIndices[0]
	}
	
	DataTable.fn.getSelectedIndexs = function(){
		if (this.selectedIndices == null || this.selectedIndices.length == 0)
			return []
		return this.selectedIndices
	}
	
	/**
	 * 根据行号获取行索引
	 * @param {String} rowId
	 */
	DataTable.fn.getIndexByRowId = function(rowId){
		for (var i=0, count = this.rows().length; i< count; i++){
			if (this.rows()[i].rowId == rowId)
				return i
		}
		return -1
	}
	
	/**
	 * 获取所有行数据
	 */
	DataTable.fn.getAllDatas = function(){
		var rows = this.getAllRows()
		var datas = []
		for (var i=0, count = rows.length; i< count; i++)
			if (rows[i])
				datas.push(rows[i].getData())
		return datas
	}

	/**
	 * 获取当前页数据
	 */
	DataTable.fn.getData = function(){
		var datas = []
		for(var i = 0; i< this.rows().length; i++){
			datas.push(this.rows()[i].getData())
		}
		return datas
	}
	 
	DataTable.fn.getDataByRule = function(rule){
		var returnData = {},
			datas = null
		returnData.meta = this.meta
		returnData.params = this.params
		rule = rule || DataTable.SUBMIT.current
		if (rule == DataTable.SUBMIT.current){
			datas = []
			for (var i =0, count = this.rows().length; i< count; i++){
				if (i == this.currSelectedIndex)
					datas.push(this.rows()[i].getData())
				else
					datas.push(this.rows()[i].getEmptyData())
			}
		}
		else if (rule == DataTable.SUBMIT.all){
			datas = this.getData()	
		}
		else if (rule == DataTable.SUBMIT.select){
			datas = this.getSelectedDatas(true)
		}
		else if (rule == DataTable.SUBMIT.change){
			datas = this.getChangedDatas()
		}
		
		returnData.rows = datas
		returnData.select= this.getSelectedIndexs()
		returnData.current = this.getSelectedIndex()
		returnData.pageSize = this.pageSize()
		returnData.pageIndex = this.pageIndex()
		returnData.isChanged = this.isChanged()
		return returnData
	}

	/**
	 * 获取选中行数据
	 */
	DataTable.fn.getSelectedDatas = function(withEmptyRow){
		var datas = []
		var sIndices = []
		for(var i = 0, count=this.selectedIndices.length; i< count; i++){
			sIndices.push(this.selectedIndices[i])
		}	
		for(var i = 0, count=this.rows().length; i< count; i++){
			if (sIndices.indexOf(i) != -1)
				datas.push(this.rows()[i].getData())
			else if (withEmptyRow == true)
				datas.push(this.rows()[i].getEmptyData())
		}
		return datas
	}
	
	/**
	 * 绑定字段值
	 * @param {Object} fieldName
	 */
	DataTable.fn.ref = function(fieldName){
		return ko.pureComputed({
			read: function(){
				this.valueChange()
				var row = this.getCurrentRow()
				if (row) 
					return row.getValue(fieldName)
				else
					return ''
			},
			write: function(value){
				var row = this.getCurrentRow()
				if (row)
					row.setValue(fieldName,value)
			},
			owner: this
		})
	}
	

	/**
	 * 绑定字段属性
	 * @param {Object} fieldName
	 * @param {Object} key
	 */
	DataTable.fn.refMeta = function(fieldName, key){
		return ko.pureComputed({
			read: function(){
				this.metaChange()
				return this.getMeta(fieldName, key)
			},
			write: function(value){
				this.setMeta(fieldName, key, value)
			},
			owner: this
		})		
	}
	
	DataTable.fn.refEnable = function(fieldName){
		return ko.pureComputed({
			read: function(){
				this.enableChange()
				//return this.enable || this.getMeta(fieldName, 'enable') || false
				return this.enable && (this.getMeta(fieldName, 'enable') || false) 
			},
			owner: this
		})		
	}
	
	DataTable.fn.isEnable = function(fieldName){
		return this.enable || this.getMeta(fieldName, 'enable') || false
	}
	
	DataTable.fn.refRowMeta = function(fieldName, key){
		return ko.pureComputed({
			read: function(){
				this.metaChange()
				var row = this.getCurrentRow()
				if (row) 
					return row.getMeta(fieldName, key)
				else
					return ''
			},
			write: function(value){
				var row = this.getCurrentRow()
				if (row)
					row.setMeta(fieldName,value)
			},
			owner: this
		})		
	}
	
	
	DataTable.fn.getValue = function(fieldName,row){
		row = row || this.getCurrentRow()
		if (row)
			return row.getValue(fieldName)
		else
		 	return ''
	}
	
	DataTable.fn.setValue = function(fieldName, value, row){
		row = row ? row : this.getCurrentRow()
		if (row)
			row.setValue(fieldName, value)
	}
	
	DataTable.fn.setEnable = function(enable){
		if (this.enable == enable) return
		this.enable = enable
		this.enableChange(- this.enableChange())
	}
	
	DataTable.fn.getCurrentRow = function(){
		if (this.currSelectedIndex == -1)
			return null
		else	
			return this.rows()[this.currSelectedIndex]
	}

	DataTable.fn.setCurrentRow = function(index){
		if(index instanceof Row) {
			index = this.getIndexByRowId(index.rowId)
		}
		this.currSelectedIndex = index
		this.valueChange(- this.valueChange())
	}
	
	DataTable.fn.getRow = function(index){
		return this.rows()[index]
	}
	
	DataTable.fn.getAllRows = function(){
		return this.rows()
	}
	
	DataTable.fn.getRowByRowId = function(rowid){
		for(var i=0, count= this.rows().length; i<count; i++){
			if (this.rows()[i].rowId == rowid)
				return this.rows()[i]
		}
		return null
	}

	/**
	 * 获取变动的数据(新增、修改)
	 */
	DataTable.fn.getChangedDatas = function(withEmptyRow){
		var datas = []
		for (var i=0, count = this.rows().length; i< count; i++){
			if (this.rows()[i] && this.rows()[i].status != Row.STATUS.NORMAL){
				datas.push(this.rows()[i].getData())
			}
			else if (withEmptyRow == true){
				datas.push(this.rows()[i].getEmptyData())
			}
		}
		return datas
	}

	DataTable.fn._formatToIndicesArray = function(indices){
		if (typeof indices == 'string' || typeof indices == 'number') {
			indices = [indices]
		} else if (indices instanceof Row){
			indices = this.getIndexByRowId(indices.rowId)
		} else if ($.type(indices) === 'array' && indices.length > 0 && indices[0] instanceof Row){
			for (var i = 0; i < indices.length; i++) {
				indices[i] = this.getIndexByRowId(indices[i].rowId)
			}
		}
		return indices;
	}
	
	
	/**===========================================================================================================
	 * 
	 * 行模型  
	 * 
	 * {id:'xxx', parent:dataTable1}
	 * 
	 * data:{value:'v1',meta:{}}
	 * 
	 * ===========================================================================================================
	 */
	var Row = function(options){
		this.rowId = options['id'] || Row.getRandomRowId()
		this.status = Row.STATUS.NEW 
		this.parent = options['parent']
		this.initValue = null
		this.data = {}
		this.metaChange = ko.observable(1)
		this.valueChange = ko.observable(1)
		this.init()
	}
	
	Row.STATUS = {
		NORMAL: 'nrm',
		UPDATE: 'upd',
		NEW: 'new',
		DELETE: 'del'
	}
	
	Row.fn = Row.prototype = new Events()
	
	/**
	 * Row初始化方法
	 * @private
	 */
	Row.fn.init = function(){
		var meta = this.parent.meta
		//添加默认值
		for (var key in meta){
			this.data[key] = {}
			if (meta[key]['default']){
				var defaults = meta[key]['default']
				for (var k in defaults){
					if (k == 'value')
						this.data[key].value = defaults[k]
					else{
						this.data[key].meta = this.data[key].meta || {}
						this.data[key].meta[k] = defaults[k]
					}
				}
			}
		}
	}
	
	Row.fn.ref = function(fieldName){
		return ko.pureComputed({
			read: function(){
				this.valueChange()
				return this.data[fieldName]['value']
			},
			write: function(value){
				this.setValue(fieldName, value)
			},
			owner: this
		})
	}
	
	Row.fn.refMeta = function(fieldName, key){
		return ko.pureComputed({
			read: function(){
				this.metaChange()
				this.getMeta(fieldName, key)
			},
			write: function(value){
				this.setMeta(fieldName, key, value)
			},
			owner: this
		})		
	}
	
	/**
	 *获取row中某一列的值 
	 */
	Row.fn.getValue = function(fieldName){
		return this.data[fieldName]['value']
	}
	
	/**
	 *设置row中某一列的值 
	 */
	Row.fn.setValue = function(fieldName, value){
		var oldValue = this.getValue(fieldName) 
		if(oldValue == value) return ;
		this.data[fieldName]['value'] = value
		this.data[fieldName].changed = true
		if (this.status != Row.STATUS.NEW)
			this.status = Row.STATUS.UPDATE
		this.valueChange(- this.valueChange())
		if (this.parent.getCurrentRow() == this)
			this.parent.valueChange(- this.valueChange())
		this.parent.trigger(DataTable.ON_VALUE_CHANGE, {
			eventType:'dataTableEvent',
			dataTable:this.parent.id,
			rowId: this.rowId,
			field:fieldName,
			oldValue:oldValue,
			newValue:value
		})
		this.parent.trigger(fieldName + "." + DataTable.ON_VALUE_CHANGE, {
			eventType:'dataTableEvent',
			dataTable:this.parent.id,
			rowId: this.rowId,
			field:fieldName,
			oldValue:oldValue,
			newValue:value
		})		
	}

	/**
	 *获取row中某一列的属性
	 */
	Row.fn.getMeta = function(fieldName, key){
		if (arguments.length == 0){
			var mt = {}
			for (var k in this.data){
				mt[k] = this.data[k].meta ?  this.data[k].meta : {}
			}
			return mt
		}
		var meta = this.data[fieldName].meta
		if (meta && meta[key])
			return meta[key]
		else	
			return this.parent.getMeta(fieldName, key)
	}
	
	/**
	 *设置row中某一列的属性
	 */
	Row.fn.setMeta = function(fieldName, key,value){
		var meta = this.data[fieldName].meta
		if (!meta)
			meta = this.data[fieldName].meta = {}
		meta[key] = value
		this.metaChange(- this.metaChange())
		if (key == 'enable')
			this.enableChange(- this.enableChange())
	}

	/**
	 *设置Row数据
	 *
	 *  data.status
	 *	data.data {'field1': {value:10,meta:{showValue:1.0,precision:2}}}
	 */
	Row.fn.setData = function(data){
		this.status = data.status
		//this.rowId = data.rowId
		for(var key in data.data){
			if (this.data[key]){
				var valueObj = data.data[key]
				if (typeof valueObj == 'string' || typeof valueObj == 'number' || valueObj === null)
					this.data[key]['value'] = this.formatValue(key, valueObj)
//					this.setValue(key, valueObj)
				else{
//					this.setValue(key, valueObj.value)

					if(valueObj.error){
						$.showMessageDialog({title:"警告",msg:valueObj.error,backdrop:true});
					}else{
						this.data[key]['value'] = this.formatValue(key, valueObj.value)
						for(var k in valueObj.meta){
							this.setMeta(key, k, valueObj.meta[k])
						}
					}
				}
			}
		}
	}
	
	/**
	 * 格式化数据值
	 * @private
	 * @param {Object} field
	 * @param {Object} value
	 */
	Row.fn.formatValue = function(field, value){
		var type = this.parent.getMeta(field,'type')
		if (!type) return value
		if (type == 'date' || type == 'datetime'){
			return _formatDate(value)			
		}
		return value
	}
	
	Row.fn.updateRow = function(row){
		this.setData(row)
	}
	
	/**
	 * @private
	 * 提交数据到后台
	 */
	Row.fn.getData = function(){
		var data = ko.toJS(this.data)
		var meta = this.parent.getMeta()
		for (var key in meta){
			if (meta[key] && meta[key].type){
				if (meta[key].type == 'date' || meta[key].type == 'datetime'){
					data[key].value = _dateToUTCString(data[key].value)
				}
			}
		}
		return {'id':this.rowId ,'status': this.status, data: data}
	}
	
	Row.fn.getEmptyData = function(){
		return {'id':this.rowId ,'status': this.status, data: {}}
	}
	
	
	/*
	 * 生成随机行id
	 * @private
	 */
	Row.getRandomRowId = function() {
		return setTimeout(1);
	};
	
	var _formatDate = function(value){
		var date = new Date();
		date.setTime(value);		
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		if (parseInt(month)<10) month = "0" + month;
		var day = date.getDate();
		if (parseInt(day)<10) day = "0" + day;
		var hours = date.getHours();
		if (parseInt(hours)<10) hours = "0" + hours;
		var minutes = date.getMinutes();
		if (parseInt(minutes)<10) minutes = "0" + minutes;
		var seconds = date.getSeconds();
		if (parseInt(seconds)<10) seconds = "0" + seconds;
		var formatString = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
		return formatString;		
	}
	
	var _dateToUTCString = function(date){
		if (date.indexOf("-") > -1)
			date = date.replace(/\-/g,"/");
		var utcString = Date.parse(date);
		if (isNaN(utcString)) return ""; 
		return utcString; 	
	}
	
//	if (exports){
		$.Row = Row
		$.DataTable = DataTable
//	}
//	else
//		return {
//			Row: Row,
//			DataTable: DataTable
//		}
//	

;
	"use strict";
	
	var CompManager = {
		plugs:{},
		apply : function(viewModel, dom){
			dom = dom || document.body
			$(dom).find('[u-meta]').each(function() {
				if ($(this).data('u-meta')) return
				if ($(this).parent('[u-meta]').length > 0) return 
				var options = JSON.parse($(this).attr('u-meta'))
				if(options && options['type']) {
					var comp = CompManager._createComp(this, options, viewModel, app)
//					var comp = new DataComponent(this, options, viewModel)
					if (comp)
						$(this).data('u-meta', comp)
				}
			})	
			ko.applyBindings(viewModel, dom)
		},
		addPlug: function(plug){
			var name = plug.getName()
			this.plugs || (this.plugs = {})
			if (this.plugs[name]){
				throw new Error('plug has exist:'+ name)
			}
			this.plugs[name] = plug
		},
		_createComp: function(element,options, viewModel){
			var type = options['type']
			var plug = this.plugs[type]
			if (!plug) return null
			return new plug(element, options, viewModel)
		}
	}
	
	var _getJSObject = function (viewModel, names){
		var nameArr = names.split('.')
		var obj = viewModel
		for (var i= 0; i< nameArr.length; i++){
			obj = obj[nameArr[i]]
		}
		return obj
	}
	
	$.compManager = CompManager
	
	
;}));