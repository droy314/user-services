window.UserG = function (apiKey) {
	this.proto = this.prototype;
	this.client_id = apiKey;

	$(function() {
		gapi.load('auth2', function() {
			initializeAuthApi();
		});
		gapi.load('signin2', renderSigninButton);
	});

	
	
	
	proto.renderSigninButton = function (){
		gapi.signin2.render('google-signin',{
			'theme': 'dark',
			'longtitle' :true,
			'width': $('#google-signin').width()
		})
	}
	
} 
	
(function ($) {
	
	
	function renderSigninButton(){
		gapi.signin2.render('google-signin',{
			'theme': 'dark',
			'longtitle' :true,
			'width': $('#google-signin').width()
		})
	}
	
	function initializeAuthApi() {
		var auth2 = gapi.auth2.init({
			client_id : '{{apiKey}}'
		});
		auth2.attachClickHandler($('#google-signin')[0], {}, onSignIn, onError);
	}

	function onSignIn(googleUser) {
		var profile = googleUser.getBasicProfile();
		console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
		console.log('Name: ' + profile.getName());
		console.log('Image URL: ' + profile.getImageUrl());
		console.log('Email: ' + profile.getEmail());
	}
	
	function onError(error) {
		alert(JSON.stringify(error, undefined, 2));
	}
})(jQuery);