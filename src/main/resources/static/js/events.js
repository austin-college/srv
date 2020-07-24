/*
 * open the dialog to start to create an event.
 */
function onNewClick() {
	$("#dlgNewEvent").dialog("open");
}

/**
 * Resets the url to be /srv/events without query parameters. 
 * Occurs when 'Clear All Filters' button is selected.
 */
function baseUrl() {
	location.href = location.href.split('?')[0];
}

/**
 * When a toggle button is turned off or a combo box selected to
 * 'List All', this function removes that query/filter's string
 * parameter in the URL.
 * 
 * @param filter
 * @returns
 */
function removeQueryUrl(filter) {
	
	var newUrl = location.href;
	var deleteQuery;
	var newQuery = "";
	var currentUrlArray = location.href.split(/[\&,?]+/);
	
	console.log(currentUrlArray.length);
	
	// If the past toggle button is turned off
	if (filter == 'beforeToggle') {	
		
		// If the duration button was also selected
		if (newUrl.includes("before=now-1M")) 
			deleteQuery = 'before=now-1M';

		else 
			deleteQuery = "before=now"; 	
	}
	
	// If the future toggle button is turned off
	else if (filter == 'afterToggle') {
	
		// If the duration button was also selected
		if (newUrl.includes("after=now%2B1M")) 
			deleteQuery = 'after=now%2B1M';
		else
			deleteQuery = 'after=now';
	}
	
	// If the duration for last month toggle button is turned off
	else if (filter == 'oneMonthBeforeToggle') {
	
		deleteQuery = 'before=now-1M';
		
		if (currentUrlArray.length == 2)
			newQuery = "?before=now";
		else
			newQuery = "before=now";
	}
	
	// If the duration for next month toggle button is turned off
	else if (filter == 'oneMonthAfterToggle') {
		deleteQuery = 'after=now%2B1M';
		
		if (currentUrlArray.length == 2)
			newQuery = "?after=now";
		else
			newQuery = "after=now";
	}
	
	// If the event type combo box is selected to 'List All'
	else if (filter == 'eTypeComboBox') {
		
		deleteQuery = findQuery(currentUrlArray, 1);
	}

	// If the service client combo box is selected to 'List All'
	else if (filter == 'scComboBox') {
		
		deleteQuery = findQuery(currentUrlArray, 2);
	}
	
	// If the query string was the one and only query in the URL
	if (currentUrlArray.length == 2)
		newUrl = newUrl.replace('?'+deleteQuery, newQuery);
	
	// If the query string was the first in the URL
	else if (currentUrlArray[1] == deleteQuery)
		newUrl = newUrl.replace(deleteQuery+'&', newQuery);
	
	// If the query string was elsewhere in the URL
	else
		newUrl = newUrl.replace('&'+deleteQuery, newQuery);
	
	console.log(newUrl);
	console.log(deleteQuery);
	console.log(newQuery);

	location.href = newUrl;
	
}

/**
 * Helper method that finds and returns the specified query in the url.
 * This is helpful to find the query strings in order to replace them.
 * Returns the string of the entire query i.e. "before=now-1M"
 * 
 * @param urlArray
 * @param flag
 * @returns
 */
function findQuery(urlArray, flag) {
	
	var query;
	
	for (var index = 0; index < urlArray.length; index++) {
		
		// Specified query is for event type
		if(flag == 1) {
			if (urlArray[index].includes("eType="))
				query = urlArray[index];
		}
		
		// Specified query is for service client
		else if (flag == 2) {
			if (urlArray[index].includes("sc="))
				query = urlArray[index];
		}
		
		// Specified query is for past events
		else if (flag == 3) {
			if (urlArray[index].includes("before="))
				query = urlArray[index];
		}
		
		// Specified query is for future events
		else if (flag == 4) {
			if (urlArray[index].includes("after="))
				query = urlArray[index];		
		}	
	}
	
	return query;
}

/**
 * Helper method that verifies if the specified filter/query is already
 * contained in the URL. This is helpful to avoid repeats in the URL.
 * 
 * Returns an array where the first index is the new URL and the second
 * is a boolean flag for whether or not the query/filter was already in 
 * the URL.
 * 
 * @param filter
 * @param url
 * @param comboBoxSelectedId
 * @returns
 */
function urlContains(filter, url, comboBoxSelectedId) {
	
	comboBoxSelectedId = comboBoxSelectedId || -1; 
	contains = false;
	
	/*
	 * If the specified query is for event type and it has been selected before,
	 * find its location in the URL and replace it with the newly selected event
	 * type id.
	 */
	if ((filter == 'eTypeComboBox') && (url.includes("eType="))) {
		
		oldQuery = findQuery(location.href.split(/[\&,?]+/), 1);
		url = url.replace(oldQuery, 'eType=' + comboBoxSelectedId);
		contains = true;
	}
	
	/*
	 * If the specified query is for service client and it has been selected before,
	 * find its location in the URL and replace it with the newly selected service
	 * client id.
	 */
	else if ((filter == 'scComboBox') && (url.includes("sc="))) {
		
		oldQuery = findQuery(location.href.split(/[\&,?]+/), 2);
		url = url.replace(oldQuery, 'sc=' + comboBoxSelectedId);
		contains = true;
	}
	
	/*
	 * If the duration toggle for the last month has been selected, find its
	 * location in the URL and replace it with 'before=now-1M' 
	 */
	else if (filter == 'oneMonthBeforeToggle') {
		
		oldQuery = findQuery(location.href.split(/[\&,?]+/), 3);
		url = url.replace(oldQuery, 'before=now-1M');
		contains = true;
	}
	
	/*
	 * If the duration toggle for the next month has been selected, find 
	 * its location in the URL and replace it with 'after=now+1M'
	 */
	else if (filter == 'oneMonthAfterToggle') {

		oldQuery = findQuery(location.href.split(/[\&,?]+/), 4);
		url = url.replace(oldQuery, 'after=now%2B1M');
		contains = true;
	}
	
	/*
	 * If the past toggle has been selected and the URL previously was
	 * showing future events, replace the query for future events with the
	 * query for past events. 
	 */
	else if ((filter == 'beforeToggle') && (url.includes("after="))){

		/*
		 * If the duration toggle was also selected, replace with query for
		 * last month's events
		 */
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
	
	/*
	 * If the future toggle has been selected and the URL previously was
	 * showing past events, replace the query for past events with the 
	 * query for future events.
	 */
	else if ((filter == 'afterToggle') && (url.includes("before="))) {
		
		/*
		 * If the duration toggle was also selected, replace with query for
		 * next month's events
		 */
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

/**
 * When toggle button or combo box is selected, this function is 
 * responsible for creating the new URL with the specified query based
 * on the toggle button/combo box selected. After the URL is created,
 * reloads the page with that URL. 
 * 
 * @param filter
 * @param comboBoxSelectedId
 * @returns
 */
function queryUrl(filter, comboBoxSelectedId) {
	
	comboBoxSelectedId = comboBoxSelectedId || -1; 
	
	var currentUrl = location.href;
		
	containsArray = urlContains(filter, currentUrl, comboBoxSelectedId); 
	currentUrl =  containsArray[0];
	
	console.log(currentUrl);
	
	/*
	 * Verifies if the specified query/filter was already in the URL. If so,
	 * the new URL is already completed due to the helper urlContains() method.
	 * If not, creates new URL.
	 */
	if (!containsArray[1]) {	
		
		// If the query is first in the URL
		if (currentUrl.includes("?")) 
			currentUrl += "&";
		
		// If the query is not first in the URL
		else 
			currentUrl += "?";

		// If the specified query is for past events
		if (filter == 'beforeToggle')
			currentUrl += 'before=now';		
		 
		// If the specified query is for future events
		else if (filter == 'afterToggle')
			currentUrl += 'after=now';

		// If the specified query is for event types
		else if (filter == 'eTypeComboBox')
			currentUrl += 'eType=' + comboBoxSelectedId;
	
		// If the specified query is for service clients
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
		//alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
		$("#dlgViewContact").html("Unable to retrieve contact "+idStr);
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
	
	$("#dlgView").dialog("open"); // opens the view dialog for admin users
	
	var idStr = $(this).attr("eid"); // The ID of the selected event to be viewed	
		
	$.ajax({
		method : "GET",
		url : "/srv/events/ajax/event/" + idStr + "/html",
		cache : false
	})
	/*
	 * If successful, then request browser to move to view event page on the 
	 * selected event if the user is a board member or servant. If user is an admin,
	 * shows the view dialog with the event's details.
	 */
	.done(function(eventDetailsHtml) {
		
		console.log(eventDetailsHtml);
	
	
		var site = location.origin
		var path = site + "/srv/events/view/" + idStr;
		console.log(path);
		
		$("#dlgView").html(eventDetailsHtml);
		
		if (document.getElementById("dlgView") == null) 
			location.assign(path);
		
		
	})
	/*
	 * If unsuccessful, display error message and
	 * reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
	});

}

/**
 * When the back button is clicked on returns the user to the previous page.
 * If the previous page is the login page, the user is directed to their home page.
 * @returns
 */ 
function goBack() {

	if (document.referrer.includes("/srv/login"))
		location.href = "/srv/home";
	else
		window.history.back();
}

// pop up to inform the user that they can't sign up/rsvp for an event
function onSignUpClick() {
	alert("Feature is not functional yet");

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

	// connect the view action to all view buttons/row click
	$(".btnEvView, .evView").click(onViewClick);

	// connect the view action to all view buttons
	$("td.ev_contact").click(onContactClick);
	
	// tooltip for RSVP button for servant users
	$('[data-toggle="tooltip"]').tooltip();   

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
		width : 300,
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
	
	// setup the create event details dialog....
	$("#dlgView").dialog({
		autoOpen : false, // hide it at first
		height : 550,
		width : 950,
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		modal : true,		show : {
			effect : "blind",
			duration : 300
		},
		buttons : [ {
			text : "CANCEL",
			"id" : 'cancel',
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
		"info": false,
		"order":  [ 4, 'asc' ] // allows us to sort by date not id		
	});
	$('.dataTables_length').addClass('bs-select');

}
// when the DOM is completely loaded, do final preparations by calling
// onPageLoad function
$(document).ready(onPageLoad);



