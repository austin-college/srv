/*
 * open the dialog to start to create an event.
 */
function onNewClick() {
	$("#dlgNewEvent").dialog("open");
}

/*
 * Resets the url to be /srv/events without query parameters. 
 * Occurs when 'Clear All Filters' button is selected.
 */
function baseUrl() {
	location.href = location.href.split('?')[0];
}

function removeQueryUrl(filter) {
	
	var newUrl = location.href;
	var currentUrlArray = location.href.split(/[\&,?]+/);
	
	console.log(currentUrlArray.length);
	
	if (filter == 'beforeToggle') {	
		
		if (newUrl.includes("before=now-1M")) {
		
			if (currentUrlArray.length == 2)
				newUrl = newUrl.replace('?before=now-1M', "");
			else if (currentUrlArray[1] == "before=now-1M")
				newUrl = newUrl.replace('before=now-1M&', "");
			else
				newUrl = newUrl.replace('&before=now-1M', "");	
			
		}
		
		else {
			
			if (currentUrlArray.length == 2)
				newUrl = newUrl.replace('?before=now', "");
			else if (currentUrlArray[1] == "before=now")
				newUrl = newUrl.replace('before=now&', "");
			else
				newUrl = newUrl.replace('&before=now', "");
		}
	}
	
	else if (filter == 'afterToggle') {
	
		if (newUrl.includes("after=now%2B1M")) {
	
			if (currentUrlArray.length == 2)
				newUrl = newUrl.replace('?after=now%2B1M', "");
			else if (currentUrlArray[1] == "after=now%2B1M")
				newUrl = newUrl.replace('after=now%2B1M&', "");
			else
				newUrl = newUrl.replace('&after=now%2B1M', "");				
		}
		
		else {

			if (currentUrlArray.length == 2)
				newUrl = newUrl.replace('?after=now', "");
			else if (currentUrlArray[1] == "after=now")
				newUrl = newUrl.replace('after=now&', "");
			else
				newUrl = newUrl.replace('&after=now', "");
		}
	}
	
	else if (filter == 'oneMonthBeforeToggle') {
		if (currentUrlArray.length == 2)
			newUrl = newUrl.replace('?before=now-1M', "?before=now");
		else if (currentUrlArray[1] == "before=now-1M")
			newUrl = newUrl.replace('before=now-1M&', "before=now&");
		else
			newUrl = newUrl.replace('&before=now-1M', "&before=now");	
	}
	
	else if (filter == 'oneMonthAfterToggle') {
		if (currentUrlArray.length == 2)
			newUrl = newUrl.replace('?after=now%2B1M', "?after=now");
		else if (currentUrlArray[1] == "after=now%2B1M")
			newUrl = newUrl.replace('after=now2%B1M&', "after=now&");
		else
			newUrl = newUrl.replace('&after=now%2B1M', "&after=now");	
	}
	
	else if (filter == 'eTypeComboBox') {
		
		deleteQuery = findQuery(currentUrlArray, 1);
		
		if (currentUrlArray.length == 2)
			newUrl = newUrl.replace('?' + deleteQuery, "");
		else if (currentUrlArray[1].includes('?eType='))
			newUrl = newUrl.replace(deleteQuery + '&', "");
		else
			newUrl = newUrl.replace('&' + deleteQuery, "");	
	}

	else if (filter == 'scComboBox') {
		
		deleteQuery = findQuery(currentUrlArray, 2);

		if (currentUrlArray.length == 2)
			newUrl = newUrl.replace('?' + deleteQuery, "");
		else if (currentUrlArray[1].includes('?sc='))
			newUrl = newUrl.replace(deleteQuery + '&', "");
		else
			newUrl = newUrl.replace('&' + deleteQuery, "");	
	}
	
	console.log(newUrl);
	
	location.href = newUrl;
	
}

function findQuery(urlArray, flag) {
	
	var query;
	
	for (var index = 0; index < urlArray.length; index++) {
		
		if(flag == 1) {
			if (urlArray[index].includes("eType="))
				query = urlArray[index];
		}
		
		else if (flag == 2) {
			if (urlArray[index].includes("sc="))
				query = urlArray[index];
		}
		
		else if (flag == 3) {
			if (urlArray[index].includes("before="))
				query = urlArray[index];
		}
		
		else if (flag == 4) {
			if (urlArray[index].includes("after="))
				query = urlArray[index];		
		}	
	}
	
	return query;
}

function urlContains(filter, url, comboBoxSelectedId) {
	
	comboBoxSelectedId = comboBoxSelectedId || -1; 
	contains = false;
	
	if ((filter == 'eTypeComboBox') && (url.includes("eType="))) {
		
		oldQuery = findQuery(location.href.split(/[\&,?]+/), 1);
		url = url.replace(oldQuery, 'eType=' + comboBoxSelectedId);
		contains = true;
	}
	
	else if ((filter == 'scComboBox') && (url.includes("sc="))) {
		
		oldQuery = findQuery(location.href.split(/[\&,?]+/), 2);
		url = url.replace(oldQuery, 'sc=' + comboBoxSelectedId);
		contains = true;
	}
	
	else if (filter == 'oneMonthBeforeToggle') {
		
		oldQuery = findQuery(location.href.split(/[\&,?]+/), 3);
		url = url.replace(oldQuery, 'before=now-1M');
		contains = true;
	}
	
	else if (filter == 'oneMonthAfterToggle') {

		oldQuery = findQuery(location.href.split(/[\&,?]+/), 4);
		url = url.replace(oldQuery, 'after=now%2B1M');
		contains = true;
	}
	
	else if ((filter == 'beforeToggle') && (url.includes("after="))){

		if (url.includes("after=now%2B1M")) {
			oldQuery = findQuery(location.href.split(/[\&,?]+/), 4);
			url = url.replace(oldQuery, 'before=now-1M');			
		}
		else {
			oldQuery = findQuery(location.href.split(/[\&,?]+/), 4);
			url = url.replace(oldQuery, 'before=now');
		}
		contains = true;
	}
	
	else if ((filter == 'afterToggle') && (url.includes("before="))) {
		
		if (url.includes("before=now-1M")) {
			oldQuery = findQuery(location.href.split(/[\&,?]+/), 3);
			url = url.replace(oldQuery, 'after=now%2B1M');
		}
		
		else {
			oldQuery = findQuery(location.href.split(/[\&,?]+/), 3);
			url = url.replace(oldQuery, 'after=now');
		}		
		
		contains = true;
	}
		
	return [url, contains];

}
function queryUrl(filter, comboBoxSelectedId) {
	
	comboBoxSelectedId = comboBoxSelectedId || -1; 
	
	var currentUrl = location.href;
		
	containsArray = urlContains(filter, currentUrl, comboBoxSelectedId); 
	currentUrl =  containsArray[0];
	
	console.log(currentUrl);
	
	if (!containsArray[1]) {	
		
		if (currentUrl.includes("?")) 
			currentUrl += "&";

		else 
			currentUrl += "?";
		
		if (filter == 'beforeToggle')
			currentUrl += 'before=now';		
	
		else if (filter == 'afterToggle')
			currentUrl += 'after=now';
		
		else if (filter == 'eTypeComboBox')
			currentUrl += 'eType=' + comboBoxSelectedId;
	
		else if (filter == 'scComboBox')
			currentUrl += 'sc=' + comboBoxSelectedId;
	}

	console.log(currentUrl);
	
	location.href = currentUrl;
}

/*
 * When user clicks on contact, this method opens the modal dialog.
 */
function onContactClick() {
	// alert("contact click event "+$(this).attr("eid"));

	$("#dlgViewContact").dialog("open");

	var idStr = $(this).attr("eid"); // our TD better have an eid attr
										// embedded.

	$.ajax({
		method : "get",
		url : "/srv/events/ajax/event/" + idStr + "/contact",
		cache : false
	})
	/*
	 * If successful, then remove the selected event row from the table.
	 */
	.done(function(htmltext) {
		// alert("done "+htmltext);
		$("#dlgViewContact").html(htmltext);

	})
	/*
	 * If unsuccessful (invalid data values), display error message and
	 * reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Request failed: " + textStatus + " : " + jqXHR.responseText);

	});

}

/*
 * launch the action for deleting given the event id. Present dialog to confirm.
 * Then let dialog callbacks do all the work.
 */
function onDeleteClick() {

	var eid = $(this).attr("eid"); // assume eid attr exists on button.
	var row_title = $("#eid-" + eid + " td.ev_title").html(); // value of
																// title cell in
																// row
	var row_date = $("#eid-" + eid + " td.ev_date").html(); // value of date
															// cell in row
	// alert("delete event "+row_title+ " on "+row_date);

	// fill in the dialog with data from the current row event.
	$("#delEvId").html(eid);
	$("#delEvTitle").html(row_title);
	$("#delEvDate").html(row_date);

	// open the dialog...let it take over
	$("#dlgDelete").dialog("open");
}

/*
 * makes the request back to our server to delete the event whose id we extract
 * from the confirmation dialog
 */
function ajaxDeleteEventNow() {

	// The ID of the selected event to be deleted...from the dialog
	var idStr = $("#delEvId").html();

	// alert(idStr);

	$.ajax({
		method : "POST",
		url : "/srv/events/ajax/del/" + idStr,
		cache : false
	})
	/*
	 * If successful, then remove the selected event row from the table.
	 */
	.done(function(eid) {
		// alert("done "+eid);
		$("#eid-" + eid).remove(); // remove row from table.
		$("#dlgDelete").dialog("close");
	})
	/*
	 * If unsuccessful (invalid data values), display error message and
	 * reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
		$("#dlgDelete").dialog("close");
	});

}

/*
 * Makes the request back to server to create a new event.
 * 
 * @param etid @returns
 */
function ajaxCreateEventNow(etid) {

	// The ID of the selected event to be deleted...from the dialog
	$.ajax({
		method : "POST",
		url : "/srv/events/ajax/new/" + etid,
		cache : false
	})
	/*
	 * If successful, then request browser to move to edit page on the newly
	 * created item.
	 */
	.done(function(eid) {
		var site = location.origin
		var path = site + "/srv/events/edit/" + eid;
		location.assign(path);
	})
	/*
	 * If unsuccessful (invalid data values), display error message and
	 * reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
		$("#dlgNewEvent").dialog("close");
	});

}

/*
 * cause the client to request the edit page with the specified event id.
 * Assumes that the button to which this handler is attached has a eid
 * attribute.
 */
function onEditClick() {
	var site = location.origin
	var path = site + "/srv/events/edit/" + $(this).attr("eid");
	location.assign(path);
}

// launch the action for viewing details given the event id
function onViewClick() {
	alert("view event " + $(this).attr("eid"));
	// TODO use jquery to open event details viewing dialog
}

// Final preparations once the page is loaded. we hide stuff and attach
// functions to buttons.
function onPageLoad() {

	// connection action to unique create button
	$("#btnEvNew").click(onNewClick);

	// connect the delete action to all delete buttons tagged with btnEvDel
	// class
	$(".btnEvDel").click(onDeleteClick);

	// connect the edit action to all edit buttons
	$(".btnEvEdit").click(onEditClick);

	// connect the view action to all view buttons
	$(".btnEvView").click(onViewClick);

	// connect the view action to all view buttons
	$("td.ev_contact").click(onContactClick);

	// Register and hide the delete dialog div until a delete button is clicked
	// on.
	$("#dlgDelete").dialog({
		autoOpen : false, // hide it at first
		height : 250,
		width : 400,
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		modal : true,
		dialogClass : "delDlgClass",
		show : {
			effect : "blind",
			duration : 500
		},
		buttons : [ {
			text : "DELETE",
			"class" : 'delBtnClass',
			click : function() {
				ajaxDeleteEventNow();
			}
		},

		{
			text : "CANCEL",
			"class" : 'cancBtnClass',
			click : function() {
				$(this).dialog("close");
			}
		} ]
	});

	// setup the create new event dialog....
	$("#dlgNewEvent").dialog({
		autoOpen : false, // hide it at first
		height : 250,
		width : 400,
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		modal : true,
		dialogClass : "newDlgClass",
		show : {
			effect : "blind",
			duration : 500
		},
		buttons : [ {
			text : "CREATE NEW",
			"class" : 'newBtnClass',
			click : function() {

				// harvest user's data
				var etype = $("#evType").val(); // what did user choose in
												// dialog?
				// alert("create new button "+etype);

				// post to the server
				ajaxCreateEventNow(etype);

			}
		},

		{
			text : "CANCEL",
			"class" : 'cancBtnClass',
			click : function() {
				$(this).dialog("close");
			}
		} ]
	});

	// setup the create new event dialog....
	$("#dlgViewContact").dialog({
		autoOpen : false, // hide it at first
		height : 400,
		width : 400,
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		modal : true,
		// dialogClass: "newDlgClass",
		show : {
			effect : "blind",
			duration : 300
		},
		buttons : [ {
			text : "CANCEL",
			"class" : 'cancBtnClass',
			click : function() {
				$(this).dialog("close");
			}
		} ]
	});
	/*
	 *  Allows for the table columns to be sorted disables the extra features such as 'searching'
	 */
	$('#tblEvents').DataTable({	
		"paging": false,
		"searching": false,
		"info": false
	});
	$('.dataTables_length').addClass('bs-select');

}
// when the DOM is completely loaded, do final preparations by calling
// onPageLoad function
// This wil sort the events table by column
$(document).ready(onPageLoad);



