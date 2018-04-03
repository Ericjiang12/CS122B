function handleStarResult(resultData) {
	console.log(resultData["star_name"]);
	console.log("handleSingleMovieResult: populating the movie table from resultData");
	var movie_array = resultData["star_movies"].split(',');
	var movieHTML = "";
	for (var i=0; i<movie_array.length; ++i){
		movieHTML += "<a href=/project2-login-example/SingleMovie.html?title=" + encodeURIComponent(movie_array[i]) + ">" + movie_array[i] + "</a>, ";
	}
	// populate the star table
	var starTableBodyElement = jQuery("#single_star_table_body");	
	var rowHTML = "";
	rowHTML += "<tr>";
	rowHTML += "<th>" + resultData["star_name"] + "</th>";
	rowHTML += "<th>" + resultData["star_year"] + "</th>";
	rowHTML += "<th>" + movieHTML + "</th>";
	rowHTML += "</tr>";
	starTableBodyElement.append(rowHTML);
}


var url_string =  window.location.href;
var url = new URL(url_string);
var star_name = url.searchParams.get("name");
// makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
	  dataType: "json",
	  method: "GET",
	  data: { name: star_name}, 
	  url: "/project2-login-example/SingleStar",
	  success: (resultData) => handleStarResult(resultData)
});