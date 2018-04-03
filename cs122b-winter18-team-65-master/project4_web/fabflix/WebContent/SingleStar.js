function handleStarResult(resultData) {
	console.log("handleSingleMovieResult: populating the movie table from resultData");
	var movie_array = resultData["star_movies"].split(',');
	var movieHTML = "";
	var i=0
	for (; i<movie_array.length-1; ++i)
		movieHTML += "<a href=/fabflix/SingleMovie.html?title=" + encodeURIComponent(movie_array[i]) + ">" + movie_array[i] + "</a>, ";
	console.log(movie_array[i]);
	movieHTML += "<a href=/fabflix/SingleMovie.html?title=" + encodeURIComponent(movie_array[i]) + ">" + movie_array[i] + "</a>";

	// populate the star table
	var starTableBodyElement = jQuery("#single_star_table_body");	
	var rowHTML = "";
	rowHTML += "<tr>";
	rowHTML += "<td>" + resultData["star_name"] + "</td>";
	rowHTML += "<td>" + resultData["star_year"] + "</td>";
	rowHTML += "<td>" + movieHTML + "</td>";
	rowHTML += "</tr>";
	starTableBodyElement.append(rowHTML);
}


var url_string =  window.location.href;
var url = new URL(url_string);
var star_name = url.searchParams.get("name");
jQuery.ajax({
	  dataType: "json",
	  method: "GET",
	  data: { name: star_name}, 
	  url: "/fabflix/SingleStar",
	  success: (resultData) => handleStarResult(resultData)
});