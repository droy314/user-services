var render = function($) {
	$(window).on('message onmessage', function(e){
		var data = e.originalEvent.data;
		process(data);
	});
	
	function process(data) {
		if (data && data.provider) {
			if (data.status === 'error') {
				processError(data.provider);
			} else if (data.status === 'loaded') {
				processLoaded(data.provider);
			}
		}
	}
	
	function processError(provider) {
		removeFrame(provider);
	}
	
	function removeFrame(provider) {
		getFrame(provider).remove();
	}
	
	function getFrame(provider) {
		return $("iframe#" + provider);
	}
	
	function processLoaded(provider) {
		getFrame(provider).attr('data-loaded', 'loaded');
	}
	

	setTimeout(removeUnloadedProviders, 2000);
	
	function removeUnloadedProviders() {
		$('iframe.panel:not([data-loaded])').remove();
	}
	
	
}(jQuery)