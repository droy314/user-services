var Messager = function (providerName){
	
	var $ = jQuery;
	
	$(function() {
		window.onerror = errorHandler;
	});
	
	function errorHandler(message, source, lineno, colno, error) {
		console.log(message, source, lineno, colno, error);
		postMessage('error');
	}
	
	function postMessage(status, message) {
		var obj = {};
		obj.message = message;
		obj.status = status;
		obj.provider = providerName;
		window.parent.postMessage(obj, '*');
	}
	
	
	function error(message) {
		postMessage('error', message);
	}
	
	function loaded() {
		postMessage('loaded', 'loaded');
	}
	
	return {
		loaded:loaded,
		error:error,
	};
	
	
};