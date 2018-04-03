function handleLoginResult(resultDataString) {
	var obj = JSON.parse(resultDataString);
	console.log(obj.status);
	console.log("handle login response");
	
	if (obj.status == "success") {
		console.log(obj.account);
		if (obj.account == "employee")
			window.location.replace("/fabflix/_dashboard.html")
		else
			window.location.replace("/fabflix/index.html");
	} else
		jQuery("#login_error_message").text(obj.message);
}


function submitLoginForm(formSubmitEvent) {
	console.log("submit login form");
	var s = document.getElementById("accountselect");
	var account = s.options[s.selectedIndex].value;
	var username = document.getElementById('username').value;
	var password = document.getElementById('password').value;
	formSubmitEvent.preventDefault();
	jQuery.post(
			  "/fabflix/Login", 
			  jQuery("#login_form").serialize(),
			  (resultDataString) => handleLoginResult(resultDataString));
}

jQuery("#login_form").submit((event) => submitLoginForm(event));

