function handleStarResult(resultData) {
	console.log("handleStarResult: populating star table from resultData");
	
	var starTableBodyElement = jQuery("#msg_table_body");	
	if(resultData.length != null){
		for (var i = 0; i < resultData.length; i++) {
			var rowHTML = "<tr>";
			rowHTML+="<td>" + resultData[i]["msg"] + "</td>";
			rowHTML += "</tr>"
			starTableBodyElement.append(rowHTML);
		}
	}
	else{
		var rowHTML1="<tr> <td>Successfully added new movie</td> </tr>";
		starTableBodyElement.append(rowHTML1);
	}
}

function show_detail(table_name) {
    var x = document.getElementById(table_name);
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

function handleStarResult_addinfo(resultData) {
	console.log("handleStarResult: populating star table from resultData");
	
	var starTableBodyElement = jQuery("#msg1_table_body");	
	if(resultData.length != null){
		for (var i = 0; i < resultData.length; i++) {
			var rowHTML = "<tr>";
			rowHTML+="<td>" + resultData[i]["msg"] + "</td>";
			rowHTML += "</tr>"
			starTableBodyElement.append(rowHTML);
		}
	}
	else{
		var rowHTML1="<tr> <td>Successfully added new info to movie</td> </tr>";
		starTableBodyElement.append(rowHTML1);
	}
}

function handleStarResult_addstar(resultData) {
	console.log("handleStarResult: populating star table from resultData");
	
	var starTableBodyElement = jQuery("#msg2_table_body");	
	if(resultData.length != null){
		for (var i = 0; i < resultData.length; i++) {
			var rowHTML = "<tr>";
			rowHTML+="<td>" + resultData[i]["msg"] + "</td>";
			rowHTML += "</tr>"
			starTableBodyElement.append(rowHTML);
		}
	}
	else{
		var rowHTML1="<tr> <td>Successfully added new star</td> </tr>";
		starTableBodyElement.append(rowHTML1);
	}
}

function do_ns() {
	var nm=document.ns_form;
	var name = nm.new_name.value;
	var year = nm.new_year.value;
	
	jQuery.ajax({
		dataType: "json",
		method: "post",
		data: { name: name , year: year},
		url: "/fabflix/_AddStar",
		success: (resultData) => handleStarResult_addstar(resultData)});
	
	return false;
}


function do_nm() {
	var nm=document.nm_form;
	var title = nm.movie_title.value;
	var year = nm.movie_year.value;
	var director = nm.director_name.value;
	var star_n = nm.star_name.value;
	var genre_n = nm.genre_name.value;
	var type="new";
	
	jQuery.ajax({
		dataType: "json",
		method: "post",
		data: { title: title , year: year, director: director, star_n: star_n, genre_n:genre_n, type:type},
		url: "/fabflix/_AddMovie",
		success: (resultData) => handleStarResult(resultData)});
	
	return false;
}

function do_addinfo() {
	
	var nm=document.add_info_form;
	var title = nm.movie_title.value;
	var year = nm.movie_year.value;
	var star_n = nm.star_name.value;
	var genre_n = nm.genre_name.value;
	var director="";
	var type="add";
	jQuery.ajax({ 
		dataType: "json",
		method: "post",
		data: { title: title , year: year,director:director, star_n: star_n, genre_n:genre_n, type:type},
		url: "/fabflix/_AddMovie",
		success: (resultData) => handleStarResult_addinfo(resultData)});
	return false;
}

function handleMetaDataResult(resultData) {
	console.log("handleMetaDataResult: populating the metadate table from resultData");
	var metaDataBodyElement = jQuery("#metadata_table_body");	
	for (var i=0; i < resultData.length; i++) {	
		var table_array = resultData[i]["attributes"].split(',');
		var attribute = "";
		var x=0
		for (; x<table_array.length-1; ++x)
			attribute += "<li>" + table_array[x]+ "</li>";
		
		var rowHTML = "";
		rowHTML += "<tr>";
		rowHTML += "<td>" + resultData[i]["table"] + "</td>";
		rowHTML += "<td>" + attribute + "</td>";
		rowHTML += "</tr>";
		metaDataBodyElement.append(rowHTML);
	}
}

jQuery.ajax({
	  dataType: "json",
	  method: "GET",
	  url: "/fabflix/_DashBoard",
	  success: (resultData) => handleMetaDataResult(resultData)
});