
function handleStarResult(resultData) {
	console.log("handleStarResult: populating star table from resultData");

	var starTableBodyElement = jQuery("#star_table_body");
	var x = 0;
	var linebreak = 0;
	for (; x < resultData.length-1; x++) {
		var rowHTML = "";
		rowHTML += "<a class=\"genre\" href=/fabflix/MovieList.html?genre="+ resultData[x]["star_name"] +"&page=1&limit=25>";
		++linebreak;
		if (linebreak == 6){
			rowHTML += resultData[x]["star_name"] + "</a> <br/>";
			linebreak = 0;
		} else
			rowHTML += resultData[x]["star_name"] + "</a>, ";
		starTableBodyElement.append(rowHTML);
	}
	var rowHTML = "";
	rowHTML += "<a class=\"genre\" href=/fabflix/MovieList.html?genre="+ resultData[x]["star_name"] +"&page=1&limit=25>";
	rowHTML += resultData[x]["star_name"] + "</a>";
	starTableBodyElement.append(rowHTML);
}

function generateTitle(){
	var titleTableBodyElement = jQuery("#title_table_body");
	var collection = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9'];
	var i=0;
	var linebreak = 0;
	for (; i<collection.length-1; ++i){
		var rowHTML = "";
		rowHTML += "<a class=\"title\" href=/fabflix/MovieList.html?first_char="+ collection[i] +"&page=1&limit=25>";
		++linebreak;
		if (linebreak == 18){
			rowHTML += collection[i] + "</a> <br/>";
			linebreak = 0;
		} else
			rowHTML += collection[i] + "</a>, ";
		titleTableBodyElement.append(rowHTML);
	}
	var rowHTML = "";
	rowHTML += "<a class=\"title\" href=/fabflix/MovieList.html?first_char="+ collection[i] +"&page=1&limit=25>";
	rowHTML += collection[i] + "</a>";
	titleTableBodyElement.append(rowHTML);
}


function dosearch() {
	var sf=document.searchform;
	var title = encodeURIComponent(sf.search_title.value);
	var year = encodeURIComponent(sf.search_year.value);
	var director = encodeURIComponent(sf.search_director.value);
	var name = encodeURIComponent(sf.search_name.value);
	window.location.href = "/fabflix/MovieList.html?search=true&title="+title+"&year="+year+"&director="+director+"&name="+name+"&page=1&limit=25";
	return false;
}

// makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
	  dataType: "json",
	  method: "GET",
	  url: "/fabflix/index",
	  success: (resultData) => handleStarResult(resultData)
});
generateTitle();
