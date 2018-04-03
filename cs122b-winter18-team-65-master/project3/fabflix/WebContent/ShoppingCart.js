var url_string =  window.location.href;
var url = new URL(url_string);

function handleCartResult(resultData) {
	console.log("handleCartResult: populating cart table from resultData");
	console.log(resultData);
	var cartTableBodyElement = jQuery("#cart_table_body");	
	
	for (var i=0; i < resultData.length; i++) {
		  var rowHTML = "";
		  rowHTML += "<tr>";
		  rowHTML += "<td>" + resultData[i]["title"] + "</td> ";
		  rowHTML += "<td> <form name=\"input_field\">  <input type=\"text\" name=\"quantity\" value = " + resultData[i]["quantity"] + "> </form></td>";
		  rowHTML += "<td><button type=\"submit\" onkeypress='return event.charCode >= 48 && event.charCode <= 57' onclick=\"update('" + resultData[i]["title"] + "')\">Update</button> ";
		  rowHTML += "<button type=\"submit\" onclick=\"remove('" + resultData[i]["title"] + "')\">Remove</button></td>";
		  rowHTML += "</tr>";
		  cartTableBodyElement.append(rowHTML);
	}
}

function update(title){
	var num = document.input_field;
	var number = num.quantity.value;
	jQuery.ajax({
		dataType: "json",
		method: "GET",
		data: { title:title, quantity: number}, 
		url: "/fabflix/ShoppingCart"});
}

function remove(title){
	jQuery.ajax({
		dataType: "json",
		method: "GET",
		data: { title:title, quantity: "0"}, 
		url: "/fabflix/ShoppingCart"});
}

function jump_to_payment(){
	if(url_string.indexOf("?title=") == -1)
		window.location.href='/fabflix/ConfirmInfo.html';
	else
		window.location.href='/fabflix/ShoppingCart.html';
	return false;
}


if(url_string.indexOf("?title=") != -1){
	var title = url.searchParams.get("title");
	jQuery.ajax({
		dataType: "json",
		method: "GET",
		data: {title: title}, 
		url: "/fabflix/ShoppingCart",
		success: (resultData) => handleCartResult(resultData)});
}

if(url_string.indexOf("?title=") == -1){
	var title = url.searchParams.get("title");
	jQuery.ajax({
		dataType: "json",
		method: "GET",
		url: "/fabflix/ShoppingCart",
		success: (resultData) => handleCartResult(resultData)});
}