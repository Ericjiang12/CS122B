function handleStarResult(resultData) {
	console.log("handleSingleMovieResult: populating the movie table from resultData");
	var star_array = resultData["movie_stars"].split(',');
	var starHTML = "";
	var i=0
	for (; i<star_array.length-1; ++i)
		starHTML += "<a href=/fabflix/SingleStar.html?name=" + encodeURIComponent(star_array[i]) + ">" + star_array[i] + "</a>, ";
	starHTML += "<a href=/fabflix/SingleStar.html?name=" + encodeURIComponent(star_array[i]) + ">" + star_array[i] + "</a>";
	// populate the star table
	var starTableBodyElement = jQuery("#single_movie_table_body");	
	var rowHTML = "";
	rowHTML += "<tr>";
	rowHTML += "<th><input type=\"button\" onclick=\"window.location.href='/fabflix/ShoppingCart.html?title="+encodeURIComponent(resultData["movie_title"])+"'\" value=\"Add to Cart\">";
	rowHTML += resultData["movie_id"] + "</th>";
	rowHTML += "<td>" + resultData["movie_title"] + "</td>";
	rowHTML += "<td>" + resultData["movie_year"] + "</td>";
	rowHTML += "<td>" + resultData["movie_director"] + "</td>";
	rowHTML += "<td>" + resultData["movie_genre"] + "</td>";
	rowHTML += "<td>" + starHTML + "</td>";
	rowHTML += "</tr>";
	starTableBodyElement.append(rowHTML);
}


var url_string =  window.location.href;
var url = new URL(url_string);
var title_name = url.searchParams.get("title");
// makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
	  dataType: "json",
	  method: "GET",
	  data: { title: title_name}, 
	  url: "/fabflix/SingleMovie",
	  success: (resultData) => handleStarResult(resultData)
});