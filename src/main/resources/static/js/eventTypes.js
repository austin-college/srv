/**
 * The following function verifies that none of the add or edit event type dialogues 
 * have empty fields
 * 
 * @param etName
 * @param etDescr
 * @param etDefHrs
 * @returns
 */
function checkForEmptyFields(etName, etDescr) {
	
	var valid = true;
	var msg = "Please complete the selected fields."; // error message
	var counter = 0; // for the invalid message/effect not to occur several times.
	
	/*
	 * removes previous error messages on the fields for the add event type dialogue
	 */
	$("#etName").removeClass("is-invalid");
	$("#etDescr").removeClass("is-invalid");
	
	/*
	 * removes previous error messages on the fields for the edit event type dialogue
	 */
	$("#editEtName").removeClass("is-invalid");
	$("#editEtDescr").removeClass("is-invalid");
	
	// Checks to see if the event type's name field is empty.
	if (!$(etName).val()) {
		$(etName).addClass("is-invalid");
		if(counter == 0) {
			counter++;
			updateTips(msg);
		}
		valid = false;
	}
	
	// Checks to see if the event type's full name/description field is empty.
	if (!$(etDescr).val()) {
		$(etDescr).addClass("is-invalid");
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
 * event type dialogs are numeric.
 * 
 * @param etDefHrs
 * @returns
 */
function validateFields(etDefHrs) {
	
	var valid = true;
	
	// harvests the data values from the form
	var defHrsStr = $(etDefHrs).val();
	
	// Validates that the event type's default hours are numeric and positive
	if (!$.isNumeric(defHrsStr) || parseFloat(defHrsStr) <= 0) {
		
		$(defHrsStr).addClass("is-invalid");
		updateTips("Default Hours must be a positive double/decimal (0.0)")
		
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
 * pre-populate the add dialog by setting the selected
 * service client from the previous dialog value 
 */
function prepopulateAddDialogue() {
	
	var scid= $("#newScId").val(); // get the selected service client 
	
	// retrieve the service client's info
	$.ajax({
		method: "GET",
		url: "/srv/eventTypes/ajax/serviceClient/"+scid,
		cache: false,
		dataType: "json"
	})
	/*
	 * If successful, then prepopulate the service client's name field in the add
	 * event type dialog and hides its id
	 */
	.done(function(sc) {
		
		$("#etSc").val(sc.name);
		$("#scId").val(sc.scid);
	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert( "Request failed: " + textStatus + " : " + jqXHR.responseText);	
	});
}

/**
 * Updates the client name and id for the edit dialog
 * @returns
 */
function updateClient(scid) {
	
	console.log(scid);
	
	// retrieve the service client's info
	$.ajax({
		method: "GET",
		url: "/srv/eventTypes/ajax/serviceClient/"+scid,
		cache: false,
		dataType: "json"
	})
	/*
	 * If successful, then prepopulate the service client's name field in the edit
	 * event type dialog and hides its id
	 */
	.done(function(sc) {
		
		console.log(sc);
		
		$("#editEtSc").val(sc.name);
		$("#editScId").val(sc.scid);
	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert( "Request failed: " + textStatus + " : " + jqXHR.responseText);	
	});
}
/** 
 * Ajax call to add a new event type
 */
function addEventType(etName, etDescr, etDefHrs, etPinHrs, etScid) {
	
	// get the forms values as strings
	var etNameStr = $(etName).val();
	var etDescrStr = $(etDescr).val();
	var etDefHrsStr = $(etDefHrs).val();
	var etScidStr = $(etScid).val();

	// peek at values to verify
	console.log("Name:" + etNameStr + " Descr: " + etDescrStr + " hrs: " + etDefHrsStr + " pin: " + etPinHrs + " scid: " + etScidStr)

	$.ajax({
		method: "POST",
		url: "/srv/eventTypes/ajax/addEt",
		cache: false,
		data: {name: etNameStr, descr: etDescrStr, defHrs: etDefHrsStr, pinHrs: etPinHrs, scid: etScidStr},
	})
	/*
	 * If successful then add the event type to the list with the new values.
	 */
	.done(function(et) {
		console.log("added event type");
		console.log(et);
		
		var id = $(et)[0]; // Obtains the new event type's ID from the AJAX response

		console.log(id); // Verifies the  ID


		$("#etTblBody").append(id);
		
		// Appends the buttons and their functionality to the new service client
		$(".btnEtDel").click(onDeleteClick);

		$(".btnEtEdit").click(onEditClick);

		$(".btnEtView, .etView").click(onViewClick);
		
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


/** 
 * Ajax call to edit an event type
 */
function editEventType(etid, etName, etDescr, etDefHrs, etPinHrs, etScid, etScName) {
	
	// get the forms values as strings
	var etidStr = $(etid).val();
	var etNameStr = $(etName).val();
	var etDescrStr = $(etDescr).val();
	var etDefHrsStr = $(etDefHrs).val();
	var etScidStr = $(etScid).val();

	// peek at values to verify
	console.log("Id:" + etidStr, "Name:" + etNameStr + " Descr: " + etDescrStr + " hrs: " + etDefHrsStr + " pin: " + etPinHrs + " scid: " + etScidStr)

	$.ajax({
		method: "POST",
		url: "/srv/eventTypes/ajax/editEt",
		cache: false,
		data: {etid: etidStr, name: etNameStr, descr: etDescrStr, defHrs: etDefHrsStr, pinHrs: etPinHrs, scid: etScidStr},
	})
	/*
	 * If successful then update the event type to the list with the new values.
	 */
	.done(function(et) {
		console.log("udpated event type");
		console.log(et);
		
		// get the id of the updated event
		var id = $(etid).val();
		
		console.log(id);

		// Updates the edited event type's row with the new values
		$("#etid-" + id + " td[name = 'et_name_descr']").html("(" + $(etName).val() + ") " + $(etDescr).val());
		$("#etid-" + id + " td[name ='et_hr']").html($(etDefHrs).val());
		$("#etid-" + id + " td[name ='et_sc']").html($(etScName).val());
	
		
  	    $("#editDlg").dialog("close");

	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Error");
		updateTips(jqXHR.responseText);
	});
	
}

/**
 * open the dialog to start to create an event type.
 * 
 * @returns
 */
function onNewClick() {
	// here we pass in data to specify what function made the call
	// for the dialog to be opened
	$("#dlgScSel").data("functionCall", "onNewClick").dialog("open");
}

/**
 * opens and populates the fields of event type to be
 * updated.
 * 
 * @returns
 */
function onEditClick() {
	
	$("#editDlg").dialog("open"); // opens the edit dialog for admin users
	
	var selEvType = $(this).attr("etid"); // The ID of the selected event type to be updated	
	
	console.log("Selected updated/edit event type: " + selEvType);
	
	// retrieve event type details
	$.ajax({
		method : "GET",
		url : "/srv/eventTypes/ajax/eventType/" + selEvType,
		cache : false,
		dataType: "json"
	})
	/*
	 * If success, then prepopulate the selected event type's fields in the edit dialog
	 */
	.done(function(evType) {
		
		console.log(evType);
		
		$("#editDlgEtid").val(evType.etid);				
		$("#editEtName").val(evType.name);
		$("#editEtDescr").val(evType.description);
		$("#editDefHrs").val(evType.defHours);
		$("#editEtSc").val(evType.defClient.name);
		$("#editScId").val(evType.defClient.scid);
		
		// toggles the radio button based on value of pin hours
		if (evType.pinHours) {
			radiobtn = document.getElementById("editYesPinHrs");
			radiobtn.checked = true;
		}
		else {
			radiobtn = document.getElementById("editNoPinHrs");
			radiobtn.checked = true;
		}
			
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
 * when editing/updating an event type and the user wishes to change the
 * service client, reopen the dialog for selecting the service client.
 */ 
function onChangeClientClick() {
	console.log("here");

	// here we pass in data to specify what function made the call
	// for the dialog to be opened
	$("#dlgScSel").data("functionCall", "onChangeClientClick").dialog("open");
	
	// return false otherwise the dialog above will immediately close.
	return false;
}

//alert the user that this feature is dead
function onDeleteClick() {
	alert("Feature is not functional yet");
}

//alert the user that this feature is dead
function onViewClick() {
	
	$("#viewDlg").dialog("open"); // opens the view dialog for admin users
	
	var selEvType = $(this).attr("etid"); // The ID of the selected event type to be viewed	
	
	console.log("Selected view event type: " + selEvType);
	
	// retrieve event type details
	$.ajax({
		method : "GET",
		url : "/srv/eventTypes/ajax/eventType/" + selEvType,
		cache : false,
		dataType: "json"
	})
	/*
	 * If success, then prepopulate the selected event type's fields in the view dialog
	 */
	.done(function(evType) {
		
		console.log(evType);
		
		$("#viewEtName").val(evType.name);
		$("#viewEtDescr").val(evType.description);
		$("#viewDefHrs").val(evType.defHours);
		$("#viewEtSc").val(evType.defClient.name);
		$("#viewScId").val(evType.defClient.scid);
		
		// toggles the radio button based on value of pin hours
		if (evType.pinHours) {
			radiobtn = document.getElementById("viewYesPinHrs");
			radiobtn.checked = true;
		}
		else {
			radiobtn = document.getElementById("viewNoPinHrs");
			radiobtn.checked = true;
		}
			
	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Error");
		updateTips(jqXHR.responseText);
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

// Final preparations once the page is loaded. we hide stuff and attach
// functions to buttons.
function onPageLoad() {
		
	// connection action to unique create button
	$("#btnEvNew").click(onNewClick);
	
	// connect the delete action to all delete buttons tagged with btnEvDel
	// class
	$(".btnEtDel").click(onDeleteClick);

	// connect the edit action to all edit buttons
	$(".btnEtEdit").click(onEditClick);

	// connect the view action to all view buttons
	$(".btnEtView, .etView").click(onViewClick);
	
	// connect the change service client button to its action
	$("#updateClientBtn").click(onChangeClientClick);
	
	// dialog for selecting the service client when creating new.
	$("#dlgScSel").dialog({
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
					
					var functionCall = $(this).data("functionCall");
					console.log(functionCall);
					
					/* 
					 * find the function that made the call in order to
					 * open the right dialog
					 */					
					if (functionCall == "onNewClick")
						$("#addDlg").dialog("open");
					
					else if (functionCall = "onChangeClientClick") 
						updateClient($("#newScId").val());
					
					
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

	// Add event type dialog
	$("#addDlg").dialog({
		autoOpen: false,
		height: 525,
		width: 700,
		modal: true,
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		open: function(event, ui) {			
			console.log("open add dialog");

			prepopulateAddDialogue(); // sets the service client's name

			/*
			 * Resets all the fields of the add dialog to empty.
			 */
			$("#etName").val("");
			$("#etDescr").val("");
			$("#defHrs").val("");

			/*
			 * Removes previous error messages from the fields.
			 */
			$("#etName").removeClass("is-invalid");
			$("#etDescr").removeClass("is-invalid");
			$("#defHrs").removeClass("is-invalid");
			$(".validationTips" ).removeClass("alert alert-danger").text("");
		},
		buttons: [
			{
				text: "Submit", 
				"id": "addBtnDlg",
				"class": 'btn',
				click: function() {		

					console.log("submit on add dialog");

					var pinHrs;

					// get the radio button values
					if($('#editYesPinHrs').is(':checked'))
						pinHrs = true;
					else
						pinHrs = false;


					/*
					 * Validates that the fields of the add event type dialogue are not empty and valid.
					 * If all the fields are valid, adds the new event type to the table and closes the dialog.
					 */
					if (checkForEmptyFields("#etName", "#etDescr")) {

						// since default hours is not required and can be left blank, don't need to verify it's
						// non-numeric if empty/blank
						if ($("#defHrs").val() == "") { 	addEventType("#etName", "#etDescr", "#defHrs", pinHrs, "#scId");  }

						else {

							if (validateFields("#defHrs")) {	addEventType("#etName", "#etDescr", "#defHrs", pinHrs, "#scId"); }

						}

					}
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

	// View event type dialog
	$("#viewDlg").dialog({
		autoOpen: false,
		height: 525,
		width: 700,
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		modal: true,
		open: function(event, ui) {			
			console.log("open view dialog");
		},
		buttons : [ {
			text : "CANCEL",
			"class" : 'cancBtnClass',
			click : function() {
				$(this).dialog("close");
			}
		} ]
	});


	// Edit event type dialog
	$("#editDlg").dialog({
		autoOpen: false,
		height: 525,
		width: 700,
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		modal: true,
		open: function(event, ui) {			
			console.log("open view dialog");
			
			/*
			 * Removes previous error messages from the fields.
			 */
			$("#editEtName").removeClass("is-invalid");
			$("#editEtDescr").removeClass("is-invalid");
			$("#editDefHrs").removeClass("is-invalid");
			$(".validationTips" ).removeClass("alert alert-danger").text("");
		},
		buttons: [
			{
				text: "Update", 
				"id": "addBtnDlg",
				"class": 'btn',
				click: function() {		

					console.log("submit on edit dialog");

					var pinHrs;

					// get the radio button values
					if($('#editYesPinHrs').is(':checked'))
						pinHrs = true;
					else
						pinHrs = false;


					/*
					 * Validates that the fields of the edit event type dialogue are not empty and valid.
					 * If all the fields are valid, updates the event type to the table and closes the dialog.
					 */
					if (checkForEmptyFields("#editEtName", "#editEtDescr")) {

						// since default hours is not required and can be left blank, don't need to verify it's
						// non-numeric if empty/blank
						if ($("#editDefHrs").val() == "") { editEventType("#editDlgEtid", "#editEtName", "#editEtDescr", "#editDefHrs", pinHrs, "#editScId", "#editEtSc"); }

						else {

							if (validateFields("#editDefHrs")) { editEventType("#editDlgEtid", "#editEtName", "#editEtDescr", "#editDefHrs", pinHrs, "#editScId", "#editEtSc"); }

						}

					}
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

	// Allows for sorting service client table in dialog
	$('#tblSrvClients').DataTable({	
		"paging": false,
		"searching": true,
		"info": false
	});

	// Allows for searching the service client table in dialog
	$('#tblSrvClients').on( 'search.dt', function () {
		$(".boxSel").prop("checked",false);  // clear all others
	} );

	 // gets the selected service client's id
	 $(".boxSel").click( function() {
			
			var state = $(this).prop("checked");
			
			if (state) {
				var scid = $(this).attr("scid");
				$("#newScId").val(scid);
			}
			
			$(".boxSel").prop("checked",false);  // clear all others		
			$(this).prop("checked",state);  // reassert current state on current button  
		});
}


// when the DOM is completely loaded, do final preparations by calling
// onPageLoad function
$(document).ready(onPageLoad)