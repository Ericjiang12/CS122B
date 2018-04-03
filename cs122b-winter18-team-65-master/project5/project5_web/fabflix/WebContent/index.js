var cache = {};
function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	
	console.log(cache)
	// TODO: if you want to check past query results first, you can do it here
	for (var cache_query in cache){
		if (cache_query == query){
			console.log("using cache");
			handleLookupAjaxSuccess(cache[cache_query], query, doneCallback);
			return;
		}
	}
	console.log("sending AJAX request to backend Java Servlet")
	// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
	// with the query data
	jQuery.ajax({
		"method": "GET",
		// generate the request url from the query.
		// escape the query string to avoid errors caused by special characters 
		"url": "ft_search?query=" + escape(query),
		"success": function(data) {
			// pass the data, query, and doneCallback function into the success handler
			handleLookupAjaxSuccess(data, query, doneCallback) 
		},
		"error": function(errorData) {
			console.log("lookup ajax error")
			console.log(errorData)
		}
	})
}

function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	
	// parse the string into JSON
	//var jsonData = JSON.parse(data);
	console.log(data)
	cache[query] = data
	// TODO: if you want to cache the result into a global variable you can do it here

	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback( { suggestions: data } );
}

function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	console.log("you select " + suggestion["value"]);
	if(suggestion["data"]["category"]=="movie")
		{
	var url = "/fabflix/SingleMovie.html?title="+encodeURIComponent(suggestion["value"]) ;
	console.log(url);
	window.location.href=url;
		}
	else
		{
		var url = "/fabflix/SingleStar.html?name="+encodeURIComponent(suggestion["value"]) ;
		console.log(url);
		window.location.href=url;
		
		}
}

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

$('#autocomplete').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set the groupby name in the response json data field
    groupBy: "category",
    minChars: 3,
    // set delay time
    deferRequestBy: 300,
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as mininum characters
});

function handleNormalSearch() {
	var sf=document.nfs_form;
	//console.log("doing normal search with query: " + query);
	// TODO: you should do normal search here
	var query_ns = encodeURIComponent(sf.movies.value);
	window.location.href = "/fabflix/MovieList.html?nf=true&query="+query_ns+"&page=1&limit=25";
	return false;
}

// bind pressing enter key to a hanlder function
$('#autocomplete').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the hanlder function
		handleNormalSearch($('#autocomplete').val())
	}
})

