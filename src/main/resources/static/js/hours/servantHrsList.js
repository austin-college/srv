/**
 * Verifies that none of the add or edit service hour dialogs have empty fields
 */
function checkForEmptyFields(hrField) {

	var valid = true;
	var msg = "Please complete the selected fields."; // error message
	var counter = 0; // for the invalid message/effect not to occur several times.

	/*
	 * removes previous error messages on the fields for the add service hour dialog
	 */
	$("#hrsSrvd").removeClass("is-invalid");
	$("#editDlgHrsSrvd").removeClass("is-invalid");

	// Checks to see if the event type's name field is empty.
	if (!$(hrField).val()) {
		$(hrField).addClass("is-invalid");
		if(counter == 0) {
			counter++;
			updateTips(msg);
		}
		valid = false;
	}

	return valid;
}

/**
 * The following function verifies that the fields for the add and edit
 * service horus dialogs are numeric.
 * 
 * @param hrField
 * @returns
 */
function validateFields(hrField) {

	var valid = true;

	// harvests the data values from the form
	var hrsStr = $(hrField).val();

	// Validates that the service hours' hours served are numeric and positive
	if (!$.isNumeric(hrsStr) || parseFloat(hrsStr) <= 0) {

		$(hrsStr).addClass("is-invalid");
		updateTips("Hours Served must be a positive double/decimal (0.0)")

		valid = false;
	}

	return valid;
}

/**
 * The following function replaces a HTML paragraph's text with error
 * messages to the user on the invalid fields in the add and edit
 * edit event type dialogs.
 * 
 * @param msg
 * @returns
 */
function updateTips(msg) {
	$(".ui-dialog").effect("shake");
	$(".validationTips").text(msg).addClass("alert alert-danger");
}


/**
 * Resets the url to be /srv/hours without query parameters. 
 * Occurs when 'Clear All Filters' button is selected.
 */
function baseUrl() {
	location.href = location.href.split('?')[0];
}

/**
 * When a combo box selected to 'List All', this function removes 
 * that query/filter's string parameter in the URL.
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
	
	
	// If the service client combo box is selected to 'List All'
	if (filter == 'scComboBox') {
		
		deleteQuery = findQuery(currentUrlArray, 1);
	}
	
	// If the month combo box is selected to 'List All'
	else if (filter == 'monthComboBox') {
		
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
 * Returns the string of the entire query i.e. "sc=1"
 * 
 * @param urlArray
 * @param flag
 * @returns
 */
function findQuery(urlArray, flag) {
	
	var query;
	
	for (var index = 0; index < urlArray.length; index++) {	
		
		// Specified query is for service client
		if (flag == 1) {
			if (urlArray[index].includes("sc="))
				query = urlArray[index];
		}
		
		// Specified query is for month
		else if (flag == 2) {
			if(urlArray[index].includes("month="))
				query = urlArray[index];
		}
	}
	
	return query;
}

/**
 * When combo box is selected, this function is 
 * responsible for creating the new URL with the specified query based
 * on the combo box selected. After the URL is created,
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
	
	console.log(comboBoxSelectedId);
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

		// If the specified query is for service clients
		if (filter == 'scComboBox')
			currentUrl += 'sc=' + comboBoxSelectedId;
		
		// If the specified query is for months
		if (filter == 'monthComboBox')
			currentUrl += 'month=' + comboBoxSelectedId;
	}

	console.log(currentUrl);
	
	location.href = currentUrl;
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
	 * If the specified query is for service client and it has been selected before,
	 * find its location in the URL and replace it with the newly selected service
	 * client id.
	 */
	if ((filter == 'scComboBox') && (url.includes("sc="))) {
		
		oldQuery = findQuery(location.href.split(/[\&,?]+/), 1);
		url = url.replace(oldQuery, 'sc=' + comboBoxSelectedId);
		contains = true;
	}
	
	/*
	 * If the specified query is for month and it has been selected before,
	 * find its location in the URL and replace it with the newly selected 
	 * month name
	 */
	if ((filter == 'monthComboBox') && (url.includes("month="))) {
		
		oldQuery = findQuery(location.href.split(/[\&,?]+/), 2);
		url = url.replace(oldQuery, 'month=' + comboBoxSelectedId);
		contains = true;
	}
		
	return [url, contains];

}


/**
 * prepopulates the edit dialog given the selected service hour
 */
function prepopulateEditDialog(selShid) {

	console.log(selShid);

	// retrieve service hour info
	$.ajax({
		method: "GET",
		url: "/srv/hours/ajax/hour/" + selShid,
		cache: false,
		dataType: "json"
	})
	/*
	 * If successful, then prepopulate the selected service hour's fields in the edit dialog.
	 */
	.done(function(sh) {

		console.log(sh);

		$("#editDlgTxtEvTitle").val(sh.event.title);
		$("#editDlgcontact-email").val(sh.event.contact.email);
		$("#editDlgContact-phone").val(sh.event.contact.phoneNumWork);
		$("#editDlgContact-name").val(sh.event.contact.firstName + " " + sh.event.contact.lastName);
		$("#editDlgEvDate").val(sh.event.date);;
		$("#editDlgHrsSrvd").val(sh.hours);
		$("#editDlgAddress").val(sh.event.contact.street);
		$("#editDlgZip-code").val(sh.event.contact.zipcode);
		$("#editDlgCity").val(sh.event.contact.city);
		$("#editDlgState").val(sh.event.contact.state);
		$("#editDlgEvSrvClient").val(sh.event.serviceClient.name).change();
		$("#editDlgScId").val(sh.event.serviceClient.scid);
		$("#editDlgEvId").val(sh.event.eid);
		$("#editDlgDescription").val(sh.description);
		$("#editDlgReflection").val(sh.reflection);

		// if true, the user must use the default service hours, otherwise they can edit
		if (sh.event.type.pinHours) {
			console.log("true");
			document.getElementById("editDlgHrsSrvd").setAttribute("readonly", true);
		}
		else {
			console.log("false");
			document.getElementById("editDlgHrsSrvd").removeAttribute("readonly");
		}

	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert( "Request failed: " + textStatus + " : " + jqXHR.responseText);	
	});

}

/**
 * prepopulates the view dialog given the selected service hour
 */
function prepopulateViewDialog(selShid) {

	console.log(selShid);

	// retrieve service hour info
	$.ajax({
		method: "GET",
		url: "/srv/hours/ajax/hour/" + selShid,
		cache: false,
		dataType: "json"
	})
	/*
	 * If successful, then prepopulate the selected service hour's fields in the edit dialog.
	 */
	.done(function(sh) {

		console.log(sh);

		$("#viewDlgTxtEvTitle").val(sh.event.title);
		$("#viewDlgcontact-email").val(sh.event.contact.email);
		$("#viewDlgContact-phone").val(sh.event.contact.phoneNumWork);
		$("#viewDlgContact-name").val(sh.event.contact.firstName + " " + sh.event.contact.lastName);
		$("#viewDlgEvDate").val(sh.event.date);;
		$("#viewDlgHrsSrvd").val(sh.hours);
		$("#viewDlgAddress").val(sh.event.contact.street);
		$("#viewDlgZip-code").val(sh.event.contact.zipcode);
		$("#viewDlgCity").val(sh.event.contact.city);
		$("#viewDlgState").val(sh.event.contact.state);
		$("#viewDlgEvSrvClient").val(sh.event.serviceClient.name).change();
		$("#viewDlgScId").val(sh.event.serviceClient.scid);
		$("#viewDlgEvId").val(sh.event.eid);
		$("#viewDlgDescription").val(sh.description);
		$("#viewDlgReflection").val(sh.reflection);

		
		if (sh.status == "Approved")
			$("#viewDlgHrStatus").html("Status:  <strong>" + sh.status + "</strong>!").addClass("alert alert-success");
		else if(sh.status == "Pending")
			$("#viewDlgHrStatus").html("Status:  <strong>" + sh.status + "</strong>").addClass("alert alert-info");
		else 
			$("#viewDlgHrStatus").html("Status:  <strong>" + sh.status + "</strong>!").addClass("alert alert-danger");
	
		document.getElementById("viewDlgHrsSrvd").setAttribute("readonly", true);
	
	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert( "Request failed: " + textStatus + " : " + jqXHR.responseText);	
	});

}

/**
 * prepopulates the add dialog given the selected event
 * @returns
 */
function prepopulateAddDialogue(){

	//step1 which event did they select
	var eid= $("#newEvId").val();

	// step2 retrieve event info
	$.ajax({
		method: "GET",
		url: "/srv/events/ajax/event/"+ eid,
		cache: false,
		dataType: "json"

	})
	/*
	 * If successful, then prepopulate the selected event's fields in the add dialog.
	 */
	.done(function(ev) {

		console.log(ev);

		$("#txtEvTitle").val(ev.title);
		$("#contact-email").val(ev.contact.email);
		$("#contact-phone").val(ev.contact.phoneNumWork);
		$("#contact-name").val(ev.contact.firstName + " " + ev.contact.lastName);
		$("#evDate").val(ev.date);;
		$("#hrsSrvd").val(ev.type.defHours);
		$("#address").val(ev.contact.street);
		$("#zip-code").val(ev.contact.zipcode);
		$("#city").val(ev.contact.city);
		$("#state").val(ev.contact.state);
		$("#evSrvClient").val(ev.serviceClient.name).change();
		$("#scId").val(ev.serviceClient.scid);
		$("#newEvId").val(eid);


		// if true, the user must use the default service hours, otherwise they can edit
		if (ev.type.pinHours) {
			console.log("true");
			document.getElementById("hrsSrvd").setAttribute("readonly", true);
		}
		else {
			console.log("false");
			document.getElementById("hrsSrvd").removeAttribute("readonly");
		}

	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert( "Request failed: " + textStatus + " : " + jqXHR.responseText);	
	});
}


/** Ajax call to add a new service hour
 * 
 */					//	 addServiceHr("#scId", "#newEvId", "#hrsSrvd", "#reflection", "#description");

function addServiceHr(hrScid, hrEid, hrServed, hrReflection, hrDescription) {

	// get the forms values as strings
	var hrScidStr = $(hrScid).val();
	var hrEidStr = $(hrEid).val();
	var hrServedStr = $(hrServed).val();
	var hrReflectionStr = $(hrReflection).val();
	var hrDescriptionStr = $(hrDescription).val();

	// peek at values to verify
	console.log("scid: " + hrScidStr + " eid: " + hrEidStr + " served: " + hrServedStr + " " +
			"ref: " + hrReflectionStr + " descr: " + hrDescriptionStr);

	$.ajax({
		method: "POST",
		url: "/srv/hours/ajax/addHr",
		cache: false,
		data: {scid: hrScidStr, eid: hrEidStr, hrServed: hrServedStr, reflect: hrReflectionStr, descr: hrDescriptionStr},
	})
	/*
	 * If successful then add the service hour to the list with the new values
	 */
	.done(function(srvHr) {

		console.log("added srv hr");
		console.log(srvHr);

		var id = $(srvHr)[0] // obtains the new srv hr's id from the ajax response

		console.log(id); // verifies the id

		$("#hrs_tbl_body").append(id);


		// Append the buttons and their functionality to the new service hour
		$(".del").on("click", function() {
			var selShid = $(this).attr('onDelClick');
			$("#delDlg").data("selHoursId", selShid).dialog("open");
		});

				
		$(".hrRow").on("click", function() {
			var selShid = $(this).attr('onRowClick');    	
			$("#viewDlg").data("selHoursId", selShid).dialog("open");
		});

		$(".edit").on("click", function() {
			var selShid = $(this).attr('onEditClick');    	
			$("#editDlg").data("selHoursId", selShid).dialog("open");
		});   

		$("#addDlg").dialog("close");
	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Error");
		updateTips(jqXHR.responseText);
	});

}

/*
 * makes the request back to our server to delete the service id whose id we extract
 * from the confirmation dialog
 */
function ajaxDeleteEventNow() {

	// The ID of the selected service id to be deleted...from the dialog
	var idStr = $("#delShId").html();

	// alert(idStr);

	$.ajax({
		method : "POST",
		url : "/srv/hours/ajax/del/" + idStr,
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
/**
 * The edit service hour function makes an AJAX call to update
 * the selected service hour to the table from the input fields of the
 * edit dialog. 
 * 
 * Note: Can not change the ID of the service hour.
 * 
 * @returns
 */

//editServiceHr(selShid, "#editDlgScId", "#editDlgEvId", "#editDlgHrsSrvd", "#editDlgReflection", "#editDlgDescription");								

function editServiceHr(selShid, hrScid, hrEvid, hrSrvedField, reflectField, descrField) {

	// Harvests the data values from the form
	var shidStr = selShid;
	var scidStr = $(hrScid).val();
	var evidStr = $(hrEvid).val();
	var hrSrvedStr = $(hrSrvedField).val();
	var reflectStr = $(reflectField).val();
	var descrStr = $(descrField).val();

	// peek at values to verify
	console.log("shid: " + selShid +  " scid: " + scidStr + " eid: " + evidStr + " served: " + hrSrvedStr + " " +
			"ref: " + reflectStr + " descr: " + descrStr);


	$.ajax({
		method: "POST",
		url: "/srv/hours/ajax/editHr",
		cache: false,
		data: {shid: shidStr, scid: scidStr, eid: evidStr, hrSrved: hrSrvedStr, reflect: reflectStr, descr: descrStr}
	})
	/*
	 * If successful (no invalid data values), then update the selected service hour 
	 * with the new values.
	 */
	.done(function(sh) {
		console.log("updated service hour");

		console.log(sh);

		// Updates the edited service hour's row with the new values
		$("#row" + selShid + " td[name ='hrs_hrsServed']").html($(hrSrvedField).val());
		$("#row" + selShid + " td[name ='hrs_status']").html("Pending");

		$("#editDlg").dialog("close");


	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Request failed: " + textStatus + " : "+jqXHR.responseText);
	});
}

/*
 * makes the request back to our server to delete the service hour whose id we extract
 * from the confirmation dialog
 */
function ajaxDeleteEventNow() {

	// The ID of the selected service hour to be deleted...from the dialog
	var idStr = $("#delShId").html();
	
	console.log("srv hr id is: " + idStr);

	$.ajax({
		method : "POST",
		url : "/srv/hours/ajax/del/" + idStr,
		cache : false
	})
	/*
	 * If successful, then remove the selected service hour row from the table.
	 */
	.done(function(shid) {
		
		console.log("deleting service hour " + shid);
		
		$("#row" + shid).remove(); // remove row from table.
		$("#delDlg").dialog("close");
	})
	/*
	 * If unsuccessful (invalid data values), display error message and
	 * reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
		$("#delDlg").dialog("close");
	});

}

/* 
 * Prepares the month combo box
 */
function setMonthComboBox() {
	
	const monthNames = ["List All", "January", "February", "March", "April", "May", "June",
		  "July", "August", "September", "October", "November", "December"
		];

	/*
	 * Populates the month combo box with the above month names
	 */
	var selectMonth = $("#monthComboBox");

	for(m = 0; m <= 12; m++) {
	    var optn = document.createElement("OPTION");
	    optn.text = monthNames[m];
	    document.getElementById('monthComboBox').options.add(optn);
	}
	
	var selectedMonth = $("#monthComboBox").attr("data-prev-selected");
	
	// set the selected month as selected
	document.getElementById('monthComboBox').value = selectedMonth;
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

/* When the DOM is completed loaded and ready, hide the dialogs and
 * create the functionality of the buttons.
 */
$(document).ready(function() {	
	
	setMonthComboBox();

	// Register and hide the delete dialog div until a delete button is clicked on.
	$("#delDlg").dialog({
		autoOpen : false, // hide it at first
		height : 300,
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
		open: function(event, ui) {
			console.log("open delete hour dialog");

			var selShid = $("#delDlg").data('selHoursId'); // Harvests the selected service hour's id from the table to pass to js function
			
			console.log(selShid);
			
			var row_evTitle = $("#row" + selShid + " td[name = hrs_eventName]").html(); // value of event title cell in row
			var row_hrsSrved = $("#row" + selShid + " td[name = hrs_hrsServed]").html(); 
			var row_status = $("#row" + selShid + " td[name = hrs_status]").html(); 
			
			// fill in the dialog with data from the current row event.
			$("#delShId").html(selShid);
			$("#delEvTitle").html("Event Title: " + row_evTitle);
			$("#delHrsSrved").html("Hours Served: " + row_hrsSrved);
			$("#delStatus").html("Status: " + row_status);


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

	// Register and hide the service hour information dialog div until a row is clicked on
	$("#viewDlg").dialog({
		autoOpen: false,
		width: $(window).width() * 0.6,
		height: $(window).height() * 0.9,
		modal: true,
		position: {
			my: "center top",
			at: "center top",
			of: window
		},
		open: function(event, ui) {
			console.log("open view hour dialog");

			var selShid = $("#viewDlg").data('selHoursId'); // Harvests the selected service hour's id from the table to pass to js function

			prepopulateViewDialog(selShid);

		},
		buttons: [
			{	
				text: "Cancel",
				"class": 'btn btn-secondary',
				click: function() {
					$(this).dialog("close");

				}
			}]


	});


	// Register and hide the edit dialog div until an edit button is clicked on.
	$("#editDlg").dialog({
		autoOpen: false,
		width: $(window).width() * 0.6,
		height: $(window).height() * 0.9,
		modal: true,
		position: {
			my: "center top",
			at: "center top",
			of: window
		},
		open: function(event, ui) {  

			console.log("edit dialog open");

			var selShid = $("#editDlg").data('selHoursId'); // Harvests the selected service hour's id from the table to pass to js function

			prepopulateEditDialog(selShid);

			/*
			 * Removes previous error messages from the fields.
			 */
			$("#editDlgHrsSrvd").removeClass("is-invalid");
			$(".validationTips" ).removeClass("alert alert-danger").text("");

		},
		buttons: [
			{
				text: "Update", 
				"id": "addBtnDlg",
				"class": 'btn',
				click: function() {		

					console.log("submit edit dialog");
					var selShid = $("#editDlg").data('selHoursId'); // Harvests the selected service hour's id from the table to pass to js function

					/*
					 * Validates that the fields of the edit service hour dialog are not empty and valid.
					 * If all the fields are valid, adds the new service hour to the table and closes the dialog.
					 */
					if (checkForEmptyFields("#editDlgHrsSrvd")) {

						if (validateFields("#editDlgHrsSrvd")) {

							editServiceHr(selShid, "#editDlgScId", "#editDlgEvId", "#editDlgHrsSrvd", "#editDlgReflection", "#editDlgDescription");								

						}
					}

				}
			},
			{	
				text: "Cancel",
				"class": 'btn btn-secondary',
				click: function() {
					$(this).dialog("close");

				}
			}]

	});

	$("#addDlg").dialog({
		autoOpen: false,
		width: $(window).width() * 0.6,
		height: $(window).height() * 0.9,
		modal: true,
		position: {
			my: "center top",
			at: "center top",
			of: window
		},
		open: function(event, ui) {			
			console.log("populating dialogue");	//replace with a javascript function that will put data into the add dialogue
			prepopulateAddDialogue();

			/*
			 * Resets all the fields of the add dialog to empty.
			 */
			$("#hrsSrvd").val("");
			$("#reflection").val("");				
			$("#description").val("");

			/*
			 * Removes previous error messages from the fields.
			 */
			$("#hrsSrvd").removeClass("is-invalid");
			$(".validationTips" ).removeClass("alert alert-danger").text("");

		},
		buttons: [
			{
				text: "Submit", 
				"id": "addBtnDlg",
				"class": 'btn',
				click: function() {		

					console.log("submit add dialog");
					/*
					 * Validates that the fields of the add service hour dialog are not empty and valid.
					 * If all the fields are valid, adds the new service hour to the table and closes the dialog.
					 */
					if (checkForEmptyFields("#hrsSrvd")) {

						if (validateFields("#hrsSrvd")) {

							addServiceHr("#scId", "#newEvId", "#hrsSrvd", "#reflection", "#description");								

						}
					}

				}
			},
			{	
				text: "Cancel",
				"class": 'btn btn-secondary',
				click: function() {
					$("#addDlg").dialog("close");

				}
			}]
	});


	// dialog for selecting the events when creating new.
	$("#dlgEvSel").dialog({
		autoOpen: false,
		width: $(window).width() * 0.6,
		height: $(window).height() * 0.6,
		modal: true,
		position: {
			my: "center top",
			at: "center top",
			of: window
		},
		open: function(event, ui) {			
			console.log("open select dialog");	
		},
		buttons: [
			{
				text: "Submit", 
				"id": "addBtnDlg",
				"class": 'btn',
				click: function() {		
					console.log("submit on select dialog");


					$("#addDlg").dialog("open");

					$(this).dialog("close");

				}
			},
			{	
				text: "Cancel",
				"class": 'btn btn-secondary',
				click: function() {
					console.log("cancel on select dialog");
					$(this).dialog("close");

				}
			}]
	});



	/* 
	 * Opens delete service hour dialog and passes in the selected delete button's service hour's id
	 * when a user clicks a delete button.
	 */
	$(".del").on("click", function() {
		var selShid = $(this).attr('onDelClick');
		$("#delDlg").data("selHoursId", selShid).dialog("open");
	});

	/*
	 * Opens a service hour dialog and passes in the selected row's service hour's id when a user
	 * clicks on a row in the service hours table.
	 */
	$(".hrRow").on("click", function() {
		var selShid = $(this).attr('onRowClick');    	
		$("#viewDlg").data("selHoursId", selShid).dialog("open");
	});

	/* 
	 * Opens edit service hour dialog and passes in the selected edit button's service hour's id
	 * when user clicks an edit button.
	 */    
	$(".edit").on("click", function() {
		var selShid = $(this).attr('onEditClick');    	
		$("#editDlg").data("selHoursId", selShid).dialog("open");
	});   

	$(".addBtn").on("click", function() {

		$("#dlgEvSel").dialog("open");
	});


	$('#tblEvents').DataTable({	
		"paging": false,
		"searching": true,
		"info": false
	});


	$('#tblEvents').on( 'search.dt', function () {
		$(".boxSel").prop("checked",false);  // clear all others
	} );

	$(".boxSel").click( function() {

		var state = $(this).prop("checked");

		if (state) {
			var eid = $(this).attr("eid");
			$("#newEvId").val(eid);
		}

		$(".boxSel").prop("checked",false);  // clear all others		
		$(this).prop("checked",state);  // reassert current state on current button  
	});
	
	/*
	 *  Allows for the table columns to be sorted disables the extra features such as 'searching'
	 */
	$('#hrs_tbl').DataTable({	
		"paging": false,
		"searching": false,
		"info": false
	});
	$('.dataTables_length').addClass('bs-select');


});