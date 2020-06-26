/**
 * The following function verifies that none of the add service client or edit service
 * clients dialogs have empty fields.
 * 
 * @param client_name
 * @returns
 */
function checkForEmptyFields(client_name) {

	var valid = true;
	var msg = "Please complete the selected fields."; // Error message
	var counter = 0; // For the invalid message/effect not to occur several times.

	/*
	 * Removes previous error messages on the fields for the add service client dialog.
	 */
	$("#addDlg_name").removeClass("is-invalid");

	/*
	 * Removes previous error messages on the fields for the edit service client dialog.
	 */
	$("#editDlg_name").removeClass("is-invalid");

	// Checks to see if the service client's name field is empty.
	if (!$(client_name).val()) {
		$(client_name).addClass("is-invalid");
		if(counter == 0) {
			counter++;
			updateTips(msg);
		}
		valid = false;
	}

	return valid;
}

/**
 * The following function replaces a HTML paragraph's text with error
 * messages to the user on the invalid fields in the add service client and
 * edit service client dialogs.
 * 
 * @param msg
 * @returns
 */
function updateTips(msg) {
	$(".ui-dialog").effect("shake");
	$(".validationTips").text(msg).addClass("alert alert-danger");
}

/**
 * Launch the action for deleting given the service client id. Present a dialog
 * to confirm. Then let the dialog callbacks do all the work.
 */
function onDeleteClick() {

	var scid = $(this).attr("scid"); // there better be a shid attr on this button 

	// value of the service client's name in the name cell of the row
	var row_name = $("#scid-" + scid + " td[name = 'sc_title']").html(); 

	// fill in the dialog with data from the current row service client.
	$("#delScId").html("Sponsor ID: " + scid);
	$("#delScName").html("Sponsor Name: " + row_name);

	// open the dialog...let it take over
	$("#deleteDlg").dialog("open");
}


/** 
 * Makes the request back to our server to delete the service client
 * whose id we extract from the confirmation dialog
 */
function ajaxDeleteClientNow() {

	// The ID of the selected service hour to be deleted extracted from the dialog
	var idStr = $("#delScId").html().substring(12);

	$.ajax({
		method: "POST",
		url: "/srv/sc/ajax/del/" + idStr,
		cache: false
	})
	/*
	 * If successful, then remove the selected service client from the table.
	 */
	.done(function(scid) {
		console.log("deleted client");

		$("#scid-"+ scid).remove(); // remove row from table
		$("#deleteDlg").dialog("close");
	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
		$("#deleteDlg").dialog("close");
	});
}

/**
 * Makes the request back to our server to fetch the selected service client
 * to present its details in the view dialog.
 */
function onViewClick() {
	
	$("#viewDlg").dialog("open"); // opens the view dialog
	
	var selSc = $(this).attr("scid"); // The ID of the selected service client to be viewed	
	
	console.log("Selected view service client: " + selSc);
	
	// retrieve service client details
	$.ajax({
		method : "GET",
		url : "/srv/sc/ajax/sc/" + selSc,
		cache : false,
		dataType: "json"
	})
	/*
	 * If success, then prepopulate the selected service client's fields in the view dialog
	 */
	.done(function(sc) {
		
		console.log(sc);
		
		var boardMemberFullName = sc.currentBoardMember.contactInfo.firstName + " " + sc.currentBoardMember.contactInfo.lastName;
		var mainContactFullName = sc.mainContact.firstName + " " + sc.mainContact.lastName;
		var otherContactFullName = sc.otherContact.firstName + " " + sc.otherContact.lastName;
		
		$("#viewDlg_name").val(sc.name);
		$("#viewDlg_boardMemberName").val(boardMemberFullName);
		$("#viewDlg_category").val(sc.category);
		$("#viewDlg_mainContactName").val(mainContactFullName);
		$("#viewDlg_mainContactEmail").val(sc.mainContact.email);
		$("#viewDlg_mainContactWorkPhone").val(sc.mainContact.phoneNumWork);
		$("#viewDlg_mainContactMobilePhone").val(sc.mainContact.phoneNumMobile);
		$("#viewDlg_mainContactStreet").val(sc.mainContact.street);
		$("#viewDlg_mainContactCity").val(sc.mainContact.city);
		$("#viewDlg_mainContactState").val(sc.mainContact.state);
		$("#viewDlg_mainContactZip").val(sc.mainContact.zipcode);
		$("#viewDlg_mainContactID").val(sc.mainContact.contactId);
		$("#viewDlg_otherContactName").val(otherContactFullName);
		$("#viewDlg_otherContactEmail").val(sc.otherContact.email);
		$("#viewDlg_otherContactWorkPhone").val(sc.otherContact.phoneNumWork);
		$("#viewDlg_otherContactMobilePhone").val(sc.otherContact.phoneNumMobile);
		$("#viewDlg_otherContactStreet").val(sc.otherContact.street);
		$("#viewDlg_otherContactCity").val(sc.otherContact.city);
		$("#viewDlg_otherContactState").val(sc.otherContact.state);
		$("#viewDlg_otherContactZip").val(sc.otherContact.zipcode);
		$("#viewDlg_otherContactID").val(sc.otherContact.contactId);
					
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
 * opens and populates the fields of service client to be
 * updated.
 * 
 * @returns
 */
function onEditClick() {
	
	var selSc = $(this).attr("scid"); // The ID of the selected service client to be updated	
	
	console.log("Selected updated/edit service client: " + selSc);
	
	$("#editDlg").data("selectedSrvClient", selSc).dialog("open"); // opens the edit dialog
	
	// retrieve event type details
	$.ajax({
		method : "GET",
		url : "/srv/sc/ajax/sc/" + selSc,
		cache : false,
		dataType: "json"
	})
	/*
	 * If success, then prepopulate the selected service clients fields in the edit dialog
	 */
	.done(function(sc) {
		
		console.log(sc);

		var mainContactFullName = sc.mainContact.firstName + " " + sc.mainContact.lastName;
		var otherContactFullName = sc.otherContact.firstName + " " + sc.otherContact.lastName;
		
		$("#editDlg_name").val(sc.name);
		$("#editDlg_boardMemberName").val(sc.currentBoardMember.uid);
		$("#editDlg_category").val(sc.category);
		$("#editDlg_mainContactName").val(mainContactFullName);
		$("#editDlg_mainContactEmail").val(sc.mainContact.email);
		$("#editDlg_mainContactWorkPhone").val(sc.mainContact.phoneNumWork);
		$("#editDlg_mainContactMobilePhone").val(sc.mainContact.phoneNumMobile);
		$("#editDlg_mainContactStreet").val(sc.mainContact.street);
		$("#editDlg_mainContactCity").val(sc.mainContact.city);
		$("#editDlg_mainContactState").val(sc.mainContact.state);
		$("#editDlg_mainContactZip").val(sc.mainContact.zipcode);
		$("#editDlg_mainContactID").val(sc.mainContact.contactId);
		$("#editDlg_otherContactName").val(otherContactFullName);
		$("#editDlg_otherContactEmail").val(sc.otherContact.email);
		$("#editDlg_otherContactWorkPhone").val(sc.otherContact.phoneNumWork);
		$("#editDlg_otherContactMobilePhone").val(sc.otherContact.phoneNumMobile);
		$("#editDlg_otherContactStreet").val(sc.otherContact.street);
		$("#editDlg_otherContactCity").val(sc.otherContact.city);
		$("#editDlg_otherContactState").val(sc.otherContact.state);
		$("#editDlg_otherContactZip").val(sc.otherContact.zipcode);
		$("#editDlg_otherContactID").val(sc.otherContact.contactId);
			
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
 * Makes the request back to our server to update the service client
 * whose new params/values we extract from the edit dialog.
 * @returns
 */
function ajaxEditClientNow(srvClientId, srvClientNameField, mainContactId, otherContactId, boardMemberId, categoryField) {

	// get the forms values as strings
	var srvClientNameStr = $(srvClientNameField).val();
	var categoryStr = $(categoryField).val();
	
	// peek at values to verify
	console.log("srv client id: " + srvClientId + " main contact id: " + mainContactId + " other contact id: " + otherContactId + 
			" board mem id: " + boardMemberId + " name: " + srvClientNameStr + " category: " + categoryStr);
	$.ajax({
		method: "POST",
		url: "/srv/sc/ajax/editSc",
		cache: false,
		data: {scid: srvClientId, name: srvClientNameStr, cid1: mainContactId, cid2: otherContactId, bmId: boardMemberId, cat: categoryStr},
	})
	/*
	 * If successful then update the service client to the list with the new values.
	 */
	.done(function(sc) {
		console.log("updated service client");
		console.log(sc);
		
		// get the selected combo box text
		var boardMemberName = $("#editDlg_boardMemberName option:selected" ).text();
		var mainContactName = $("#editDlg_mainContactName").val();
		
		console.log(mainContactName);
		// Updates the edited event type's row with the new values
		$("#scid-" + srvClientId + " td[name = 'sc_title']").html($(srvClientNameField).val());
		$("#scid-" + srvClientId + " td[name ='sc_contact_name']").html(mainContactName);
		$("#scid-" + srvClientId + " td[name ='sc_bm_name']").html(boardMemberName);
		$("#scid-" + srvClientId + " td[name ='sc_category']").html($(categoryField).val());

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
 * opens the dialog to create a new service client
 * @returns
 */
function onNewClick() {
	$("#addDlg").dialog("open");
}

/**
 * Makes the request back to our server to create the new service client
 * whose new params/values we extractd from the add dialog
 */
function ajaxCreateClientNow(srvClientNameField, mainContactId, otherContactId, boardMemberId, categoryField) {
	
	// get the forms values as strings
	var srvClientNameStr = $(srvClientNameField).val();
	var categoryStr = $(categoryField).val();

	// peek at values to verify
	console.log("main contact id: " + mainContactId + " other contact id: " + otherContactId + 
			" board mem id: " + boardMemberId + " name: " + srvClientNameStr + " category: " + categoryStr);
	$.ajax({
		method: "POST",
		url: "/srv/sc/ajax/addSc",
		cache: false,
		data: {name: srvClientNameStr, cid1: mainContactId, cid2: otherContactId, bmId: boardMemberId, cat: categoryStr},
	})
	/*
	 * If successful then add the service client to the list with the new values.
	 */
	.done(function(sc) {
		console.log("added service client");
		console.log(sc);

		var id = $(sc)[2]; // Obtains the new service client's ID from the AJAX response

		console.log(id); // Verifies the  ID

		$('#sc_tbl_body').append(id); // Appends the new service client row to the table

		// Appends the buttons and their functionality to the new service client
		$(".btnScDel").click(onDeleteClick);

		$(".btnScView, .scRow").click(onViewClick);

		$(".btnScEdit").click(onEditClick);
		
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


///**
// * When a contact ID is selected on (or upon opening of the add dialog) in the add dialog, the addDlg takes us to this
// * function in order to make an AJAX call to the ServiceClientController. That way, from the ServiceClientController we 
// * can access the ContactDao to access the Contact database in order to obtain the information about the selected main 
// * contact information. We obtain the information from the AJAX call by the ajax_contactFields.html which contains the 
// * selected main contact's information. Note this also does the same thing for the edit dialog.
// * 
// * @param contact_id
// * @returns
// */
//function populateMCFields(contact_id) {
//
//	var idStr = contact_id; // Selected main contact's ID
//
//	console.log(idStr); // Verifying id in console
//
//	$.ajax({
//		method: "GET",
//		url: "/srv/ajax/fillMCFields",
//		cache: false,
//		data: {ID: idStr},
//		/*
//		 * If successful, populate the main contact information fields with the data received
//		 * from AJAX, which should contain the contact's information retrieved from the contact database.
//		 */
//		success: function(data) {
//
//			console.log("add main contact info");
//		
//			/*
//			 * In order to obtain the information passed back from the AJAX call, we have to 
//			 * index the data sent back (from AJAX) by every 2 and specify 'innerText' to harvest
//			 * the text in ajax_contactFields.html. There is probably a better way to do this, hopefully by selecting
//			 * a div's unique ID and so the  ajax_contactFields.html has divs for this future change.
//			 */
//			var setMcName = $(data)[0].innerText;
//			var setMcEmail = $(data)[2].innerText;
//			var setMcWorkPhone = $(data)[4].innerText;
//			var setMcMobilePhone = $(data)[6].innerText;
//			var setMcStreet = $(data)[8].innerText;
//			var setMcCity = $(data)[10].innerText;
//			var setMcState = $(data)[12].innerText;
//			var setMcZip = $(data)[14].innerText;
//
//			/*
//			 * From the values above, we can set the main contact input fields within the addDlg (in listClients.html)
//			 * with the data that the ServiceClientController gave us as a result of a successful AJAX call.
//			 */
//			$("#addDlg_mcName").val(setMcName);
//			$("#addDlg_mcEmail").val(setMcEmail);
//			$("#addDlg_mcWorkPhone").val(setMcWorkPhone);
//			$("#addDlg_mcMobilePhone").val(setMcMobilePhone);
//			$("#addDlg_mcStreet").val(setMcStreet);
//			$("#addDlg_mcCity").val(setMcCity);
//			$("#addDlg_mcState").val(setMcState);
//			$("#addDlg_mcZip").val(setMcZip);
//			
//			/*
//			 * From the values above, we can set the main contact input fields within the editDlg (in listClients.html)
//			 * with the data that the ServiceClientController gave us as a result of a successful AJAX call.
//			 */
//			$("#editDlg_mcName").val(setMcName);
//			$("#editDlg_mcEmail").val(setMcEmail);
//			$("#editDlg_mcWorkPhone").val(setMcWorkPhone);
//			$("#editDlg_mcMobilePhone").val(setMcMobilePhone);
//			$("#editDlg_mcStreet").val(setMcStreet);
//			$("#editDlg_mcCity").val(setMcCity);
//			$("#editDlg_mcState").val(setMcState);
//			$("#editDlg_mcZip").val(setMcZip);
//		},
//		/*
//		 * If unsuccessful, display error message and reasoning.
//		 */
//		error: function(jqXHR, textStatus) {
//			alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
//		}
//	});	
//}
//
///**
// * When a contact ID is selected on (or upon opening of the add dialog) in the add dialog, the addDlg takes us to this
// * function in order to make an AJAX call to the ServiceClientController. That way, from the ServiceClientController we 
// * can access the ContactDao to access the Contact database in order to obtain the information about the selected other/secondary 
// * contact information. We obtain the information from the AJAX call by the ajax_contactFields.html which contains the 
// * selected other/secondary contact's information. Note this also is the same for the edit dialog.
// * 
// * @param contact_id
// * @returns
// */
//function populateOCFields(contact_id) {
//
//	var idStr = contact_id; // Selected other contact's ID
//
//	console.log(idStr); // Verifying id in console
//
//	$.ajax({
//		method: "GET",
//		url: "/srv/ajax/fillOCFields",
//		cache: false,
//		data: {ID: idStr},
//		/*
//		 * If successful, populate the other/secondary contact information fields with the data received
//		 * from AJAX, which should contain the contact's information retrieved from the contact database.
//		 */
//		success: function(data) {
//
//			console.log("add other/secondary contact info");
//
//			/*
//			 * In order to obtain the information passed back from the AJAX call, we have to 
//			 * index the data sent back (from AJAX) by every 2 and specify 'innerText' to harvest
//			 * the text in ajax_contactFields.html. There is probably a better way to do this, hopefully by selecting
//			 * a div's unique ID and so the  ajax_contactFields.html has divs for this future change. Note we are
//			 * only interested in other/secondary contact information which is why we do not begin indexing at 0
//			 */
//			var setOcName = $(data)[16].innerText;
//			var setOcEmail = $(data)[18].innerText;
//			var setOcWorkPhone = $(data)[20].innerText;
//			var setOcMobilePhone = $(data)[22].innerText;
//			var setOcStreet = $(data)[24].innerText;
//			var setOcCity = $(data)[26].innerText;
//			var setOcState = $(data)[28].innerText;
//			var setOcZip = $(data)[30].innerText;
//
//			/*
//			 * From the values above, we can set the other/secondary contact input fields within the addDlg (in listClients.html)
//			 * with the data that the ServiceClientController gave us as a result of a successful AJAX call.
//			 */
//			$("#addDlg_ocName").val(setOcName);
//			$("#addDlg_ocEmail").val(setOcEmail);
//			$("#addDlg_ocWorkPhone").val(setOcWorkPhone);
//			$("#addDlg_ocMobilePhone").val(setOcMobilePhone);
//			$("#addDlg_ocStreet").val(setOcStreet);
//			$("#addDlg_ocCity").val(setOcCity);
//			$("#addDlg_ocState").val(setOcState);
//			$("#addDlg_ocZip").val(setOcZip);
//			
//
//			/*
//			 * From the values above, we can set the other/secondary contact input fields within the editDlg (in listClients.html)
//			 * with the data that the ServiceClientController gave us as a result of a successful AJAX call.
//			 */
//			$("#editDlg_ocName").val(setOcName);
//			$("#editDlg_ocEmail").val(setOcEmail);
//			$("#editDlg_ocWorkPhone").val(setOcWorkPhone);
//			$("#editDlg_ocMobilePhone").val(setOcMobilePhone);
//			$("#editDlg_ocStreet").val(setOcStreet);
//			$("#editDlg_ocCity").val(setOcCity);
//			$("#editDlg_ocState").val(setOcState);
//			$("#editDlg_ocZip").val(setOcZip);
//		},
//		/*
//		 * If unsuccessful, display error message and reasoning.
//		 */
//		error: function(jqXHR, textStatus) {
//			alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
//		}
//	});	
//}


/** 
 * Final preparations once the page is loaded. Here we hide stuff such
 * as dialogs and attach functions to buttons.
 */
function onPageLoad() {

	/************************************
	 * Button connections defined below *
	 ************************************/

	// connect the delete action to all delete buttons tagged with btnScDel class
	$(".btnScDel").click(onDeleteClick);

	// connect the view action to all view buttons
	$(".btnScView, .scRow").click(onViewClick);

	// connect the edit action to all edit buttons
	$(".btnScEdit").click(onEditClick);

	// connection action to create button
	$(".addBtn").click(onNewClick);
	
	
	
	/**********************************
	 * Dialog functions defined below *
	 **********************************/

	// Register and hide the delete dialog div until a delete button is clicked on.
	$("#deleteDlg").dialog({
		autoOpen: false,
		height: 250,
		width: 400,
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		modal: true,
		dialogClass: "delDlgClass",
		show : {
			effect : "blind",
			duration : 500
		},
		create: function(event, ui) { 
			$(".delBtnClass").addClass("btn btn-danger");
			$(".cancBtnClass").addClass("btn btn-secondary");
		},					
		buttons: [
			{
				text: "DELETE", 
				"class": 'delBtnClass',
				click: function() {
					ajaxDeleteClientNow();		
				}
			},
			{	
				text: "CANCEL",
				"class": 'cancBtnClass',
				click: function() {
					$(this).dialog("close");
				}
			}]
	});

	// Register and hide the view dialog div until a row or view button is clicked on.
	// displays the service client's info
	$("#viewDlg").dialog({
		autoOpen: false,
		height: 500,
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
			"class" : 'btn btn-secondary',
			click : function() {
				$(this).dialog("close");
			}
		} ]
	});
	
	// Register and hide the edit dialog div until an edit button is clicked on.
	$("#editDlg").dialog({
		autoOpen: false,
		height: 500,
		width: 700,	
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		modal: true,
		dialogClass: "editDlgClass",
		open: function(event, ui) {

			/*
			 * Removes previous error messages from the fields.
			 */
			$("#editDlg_name").removeClass("is-invalid");
			$(".validationTips" ).removeClass("alert alert-danger").text("");

		},							
		buttons: [
			{
				text: "Update", 
				"id": "addBtnDlg",
				"class": 'btn',
				click: function() {		

					var selected_shid = $(this).data('selectedSrvClient'); // The selected service client's ID
					var selected_mainContactID = $("#editDlg_mainContactID").children("option:selected").val(); // ID for main contact
					var selected_otherContactID = $("#editDlg_otherContactID").children("option:selected").val(); // ID for other/secondary contact
					var selected_boardMemberID = $("#editDlg_boardMemberName").val(); // ID for board member

					/*
					 * Validates that the fields of the edit service client dialog are not empty.
					 * If all the fields are valid, updates the service client to the table and closes the dialog.
					 */
					if(checkForEmptyFields("#editDlg_name")){

						ajaxEditClientNow(selected_shid, "#editDlg_name", selected_mainContactID,
								selected_otherContactID, selected_boardMemberID, "#editDlg_category");

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
	
	// Register and hide the add dialog div until the add button is clicked on.	
	$("#addDlg").dialog({
		autoOpen: false,
		height: 500,
		width: 700,
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		modal: true,
		dialogClass: "addDlgClass",	
		open: function(event, ui) {	

			/*
			 * Resets all the fields of the add dialog to empty.
			 */
//			$("#addDlg_name").val("");
//			$("#addDlg_category").val("Animals");
//			$('select').prop('selectedIndex', 0);
//			
//			/*
//			 *  Upon open, we populate the main and other contact information fields (name, phone numbers, etc)
//			 *  with the first contact in the list/database.
//			 */
//			var selected_mcID = $("#addDlg_mcID").children("option:selected").val();
//			populateMCFields(selected_mcID);
//
//			var selected_ocID = $("#addDlg_ocID").children("option:selected").val();
//			populateOCFields(selected_ocID);
//
//			/*
//			 * When a user changes the main or other contact ID from the drop down menu that is inside the addDlg,
//			 * we update the contact information fields (name, phone numbers, etc.) with the newly selected contact
//			 */
//			$("#addDlg_mcID").change("click", function() {
//				var selected_mcID = $(this).children("option:selected").val();
//				populateMCFields(selected_mcID);				   
//			}); 
//
//			$("#addDlg_ocID").change("click", function() {
//				var selected_ocID = $(this).children("option:selected").val();
//				populateOCFields(selected_ocID);				   
//			}); 
//
//			/*
//			 * Removes previous error messages from the fields.
//			 */
//			$("#addDlg_name").removeClass("is-invalid");
//			$(".validationTips" ).removeClass("alert alert-danger").text("");
		},							
		buttons: [
			{
				text: "Create", 
				"id": "addBtnDlg",
				"class": 'btn',
				click: function() {		

					var selected_boardMemberID = $("#addDlg_boardMemberName").val(); // ID for board member
					console.log(selected_boardMemberID); 
					
					var selected_mainContactID = $("#addDlg_mainContactID").children("option:selected").val(); // ID for main contact
					var selected_otherContactID = $("#addDlg_otherContactID").children("option:selected").val(); // ID for other/secondary contact
					
					/*
					 * Validates that the fields of the add service client dialog are not empty.
					 * If all the fields are valid, adds the new service client to the table and closes the dialog.
					 */
		//			if(checkForEmptyFields("#addDlg_name")){
						
						ajaxCreateClientNow("#addDlg_name", selected_mainContactID, selected_otherContactID, 
								selected_boardMemberID, "#addDlg_category");
						
				//		$("#addDlg").dialog("close");
		//			}
				}
			},
			{	
				text: "Cancel",
				"class": 'btn btn-secondary',
				click: function() {
					$("#addDlg").dialog("close");

				}
			}],
	});

	//  Allows for the table columns to be sorted disables the extra features such as 'searching'
	$('#client_tbl').DataTable({	
		"paging": false,
		"searching": false,
		"info": false
	});
	$('.dataTables_length').addClass('bs-select');



}

/**
 * When the DOM is completed loaded and ready, hide the dialogs and
 * create the functionality of the buttons.
 */
$(document).ready(onPageLoad);	