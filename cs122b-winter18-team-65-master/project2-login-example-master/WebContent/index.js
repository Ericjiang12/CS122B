
function handleStarResult(resultData) {
	console.log("handleStarResult: populating star table from resultData");

	var starTableBodyElement = jQuery("#star_table_body");
	for (var i = 0; i < resultData.length; i++) {
		var rowHTML = "";
		rowHTML += "<a href=/project2-login-example/MovieList.html?genre="+ resultData[i]["star_name"] +"&page=1&limit=25>";
		rowHTML += resultData[i]["star_name"];
		rowHTML += "</a>, ";
		starTableBodyElement.append(rowHTML);
	}
}

function generateTitle(){
	var titleTableBodyElement = jQuery("#title_table_body");
	var collection = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9'];
	for (var i=0; i<collection.length; ++i){
		var rowHTML = "";
		rowHTML += "<a href=/project2-login-example/MovieList.html?first_char="+ collection[i] +"&page=1&limit=25>";
		rowHTML += collection[i];
		rowHTML += "</a>, ";
		titleTableBodyElement.append(rowHTML);
	}
}


function dosearch() {
	var sf=document.searchform;
	var title = encodeURIComponent(sf.search_title.value);
	var year = encodeURIComponent(sf.search_year.value);
	var director = encodeURIComponent(sf.search_director.value);
	var name = encodeURIComponent(sf.search_name.value);
	window.location.href = "/project2-login-example/MovieList.html?search=true&title="+title+"&year="+year+"&director="+director+"&name="+name+"&page=1&limit=25";
	return false;
}

// makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
	  dataType: "json",
	  method: "GET",
	  url: "/project2-login-example/index",
	  success: (resultData) => handleStarResult(resultData)
});
generateTitle();
