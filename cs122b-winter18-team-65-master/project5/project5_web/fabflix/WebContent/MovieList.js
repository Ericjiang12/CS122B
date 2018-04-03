var url_string =  window.location.href;
var url = new URL(url_string);
var curr_page = url.searchParams.get("page");
var max_page;
var limit_per_page = url.searchParams.get("limit");
var limit = parseInt(limit_per_page);
function handleStarResult(resultData) {
	console.log("handleStarResult: populating star table from resultData");
	var max_pages = Math.ceil(resultData.length/limit);
	max_page = max_pages;
	
	var starTableBodyElement = jQuery("#star_table_body");	
	if(resultData.length != null){
		for (var i = (curr_page-1)*limit; i < Math.min(limit*curr_page, resultData.length); i++) {
			var rowHTML = "<tr>";
			rowHTML += "<td><input type=\"button\" onclick=\"window.location.href='/fabflix/ShoppingCart.html?title="
				+ encodeURIComponent(resultData[i]["movie_title"])+"'\" value=\"Add to Cart\">";
			rowHTML += resultData[i]["movie_id"] + "</td>";
			rowHTML += "<td><a href=/fabflix/SingleMovie.html?title=" + encodeURIComponent(resultData[i]["movie_title"]) + ">"
				+ resultData[i]["movie_title"]+ "</a></td>";
			rowHTML += "<td>" + resultData[i]["movie_year"] + "</td>";
			rowHTML += "<td>" + resultData[i]["movie_director"] + "</td>";
			rowHTML += "<td>" + resultData[i]["movie_genres"] + "</td>";
			var star_array = resultData[i]["movie_stars"].split(',');
			var starHTML = "<td>";
			var x=0
			for (; x<star_array.length-1; ++x)
				starHTML += "<a href=/fabflix/SingleStar.html?name=" + encodeURIComponent(star_array[x]) + ">" + star_array[x] + "</a>, ";
			starHTML += "<a href=/fabflix/SingleStar.html?name=" + encodeURIComponent(star_array[x]) + ">" + star_array[x] + "</a>";
			rowHTML += starHTML + "</td>";
			rowHTML += "</tr>"
			starTableBodyElement.append(rowHTML);
		}
	}
	else{
		var rowHTML1="<tr> <td>There is not matching movie </td> </tr>";
		starTableBodyElement.append(rowHTML1);
	}
}

function change(){
	var select = document.getElementById("limit_select");
	var new_limit = select.options[select.selectedIndex].value;
	if (new_limit.localeCompare(limit_per_page) != 0 && new_limit.localeCompare("") != 0){
		var token= url_string.split("&limit=");
		var front_url=token[0];
		window.location.href=front_url + "&limit=" + new_limit;
	}
	return false;
}

function next_page(){ 
	if(curr_page >= max_page ){
		alert("There is the last page of your movie list");
		curr_page=max_page;
	}else{
		curr_page++;
		var new_url=url_string.split("&page=");
		var new_page=new_url[0]+"&page=" + curr_page + "&limit=" + limit_per_page; 
		window.location.href=new_page;
	}
}

function prev_page(){
	var page_index=curr_page-1;
	if(page_index==0){
		alert("There is the first page of your movie list");
		curr_page=1;
	}else{
		curr_page--;
		var new_url=url_string.split("&page=");
		var new_page=new_url[0]+"&page="+curr_page + "&limit=" + limit_per_page;
		window.location.href=new_page;
	}
}

function sortTable(n) {
	var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
	table = document.getElementById("star_table");
	switching = true;
	dir = "asc"; 
	while (switching) {
		switching = false;
		rows = table.getElementsByTagName("TR");
		for (i = 1; i < (rows.length - 1); i++) {
			shouldSwitch = false;
			x = rows[i].getElementsByTagName("TD")[n];
			y = rows[i + 1].getElementsByTagName("TD")[n];
			if (dir == "asc") {
				if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
				shouldSwitch= true;
				break;
	        }
			} else if (dir == "desc") {
				if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
					shouldSwitch= true;
					break;
				}
			}
		}
		if (shouldSwitch) {
			rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
			switching = true;
			switchcount ++; 
		} else {
			if (switchcount == 0 && dir == "asc") {
				dir = "desc";
				switching = true;
			}
		}
	}
}

if(url_string.indexOf("?genre=") != -1){
	var genre_name = url.searchParams.get("genre");
	jQuery.ajax({
	dataType: "json",
	method: "GET",
	data: { genre: genre_name}, 
	url: "/fabflix/MovieList_G",
	success: (resultData) => handleStarResult(resultData)});
}

if(url_string.indexOf("?first_char=") != -1){
	var first_char = url.searchParams.get("first_char");
	jQuery.ajax({
	dataType: "json",
	method: "GET",
	data: { first_char: first_char}, 
	url: "/fabflix/MovieList_T",
	success: (resultData) => handleStarResult(resultData)});
}

if(url_string.indexOf("?search=") != -1){
	var title    = url.searchParams.get("title");
	var year     = url.searchParams.get("year");
	var director = url.searchParams.get("director");
	var name       = url.searchParams.get("name");
	jQuery.ajax({
		dataType: "json",
		method: "GET",
		data: { title: title , year: year, director: director, name: name},
		url: "/fabflix/MovieList_S",
		success: (resultData) => handleStarResult(resultData)});
}

if(url_string.indexOf("?nf=") != -1){
	var query    = url.searchParams.get("query");
	
	jQuery.ajax({
		dataType: "json",
		method: "GET",
		data: { query: query},
		url: "/fabflix/NFT_search",
		success: (resultData) => handleStarResult(resultData)});
}