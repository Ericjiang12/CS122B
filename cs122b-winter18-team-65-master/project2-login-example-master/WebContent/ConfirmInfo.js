function handleLoginResult(resultDataJson) {
	//resultDataJson = JSON.parse(resultDataString);
	
	console.log("handle login response");
	console.log(resultDataJson);
	console.log(resultDataJson["status"]);

	if (resultDataJson["status"] == "success") {
		window.location.replace("/project2-login-example/trans_complete.html");
	} else {
		console.log("show error message");
		console.log(resultDataJson["message"]);
		jQuery("#info_error_message").text(resultDataJson["message"]);
	}
}

function check_info() {
	var info=document.info_form;
	var fn = info.fn.value;
	var ln = info.ln.value;
	var ccid = info.ccid.value;
	var exp_date = info.exp_date.value;

	jQuery.ajax({
		method:	"GET",
		dataType : "json",	
		url :"/project2-login-example/ConfirmInfo", 
		data : { fn : fn, ln : ln, ccid:ccid, exp_date:exp_date}, 
		success: (resultDataString) => handleLoginResult(resultDataString)
		});
	return false;
}