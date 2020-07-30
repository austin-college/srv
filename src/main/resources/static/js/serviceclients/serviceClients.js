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

function fillContactFields(ct, pre) {
	//$("#viewDlg_mainContactID").val(.contactId);
	var fullAddr = ct.street + ", " + ct.city + ", " + ct.state + " " + ct.zipcode;
	
	$("#ct"+pre+"-fname").html(ct.firstName);
	$("#ct"+pre+"-lname").html(ct.lastName);
	
	$("#ct"+pre+"-email").html(ct.email);
	$("#ct"+pre+"-phone1").html(ct.primaryPhone);
	$("#ct"+pre+"-phone2").html(ct.secondaryPhone);
	
	$("#ct"+pre+"-addr").html(fullAddr);
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
		
		$("#viewDlg_name").val(sc.name);
		$("#viewDlg_boardMemberName").val(boardMemberFullName);
		$("#viewDlg_category").val(sc.category);
		
		fillContactFields(sc.mainContact,"1");

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

		$("#editDlg_name").val(sc.name);
		$("#editDlg_boardMemberName").val(sc.currentBoardMember.uid);
		$("#editDlg_category").val(sc.category);
		
		fillContactFields(sc.mainContact,"3");
		
		$("#btnContact1").attr("cid",sc.mainContact.contactId);

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
function ajaxEditClientNow(srvClientId, srvClientNameField, mainContactId, boardMemberId, categoryField) {

	// get the forms values as strings
	var srvClientNameStr = $(srvClientNameField).val();
	var categoryStr = $(categoryField).val();

	// peek at values to verify
	console.log("srv client id: " + srvClientId + " main contact id: " + mainContactId + 
			" board mem id: " + boardMemberId + " name: " + srvClientNameStr + " category: " + categoryStr);
	$.ajax({
		method: "POST",
		url: "/srv/sc/ajax/editSc",
		cache: false,
		data: {scid: srvClientId, name: srvClientNameStr, cid1: mainContactId, bmId: boardMemberId, cat: categoryStr},
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
function ajaxCreateClientNow(srvClientNameField, mainContactId, boardMemberId, categoryField) {

	// get the forms values as strings
	var srvClientNameStr = $(srvClientNameField).val();
	var categoryStr = $(categoryField).val();

	// peek at values to verify
	console.log("main contact id: " + mainContactId +  
			" board mem id: " + boardMemberId + " name: " + srvClientNameStr + " category: " + categoryStr);
	$.ajax({
		method: "POST",
		url: "/srv/sc/ajax/addSc",
		cache: false,
		data: {name: srvClientNameStr, cid1: mainContactId, bmId: boardMemberId, cat: categoryStr},
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

/** 
 * Makes the request back to our server to fetch the selected contact and 
 * populate the contact fields in the add and edit dialogs.
 * 
 * Passing in the id of the selected contact as well as a boolean flag
 * on whether the contact is main contact or other/secondary contact. Thus, we 
 * can make the appropriate function call after the request to populate the correct
 * contact fields.
 */
function ajaxFetchContact(contactId) {
	
	console.log(contactId); // verify the id passed
	
	// retrieve contact details
	$.ajax({
		method : "GET",
		url : "/srv/sc/ajax/contact/" + contactId,
		cache : false,
		dataType: "json"
	})
	/*
	 * If success, then redirect to the appropriate function based on main or other contact
	 */
	.done(function(contactDetails) {
		
		populateMainContactFields(contactDetails);

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
 * Populates the fields for main contacts for both add/create and edit dialogs.
 */
function populateMainContactFields(contactDetails) {
	
	console.log("populate main contact fields");
	
	var mainContactFullName = contactDetails.firstName + " " + contactDetails.lastName;

	$("#addDlg_mainContactName").val(mainContactFullName);
	$("#addDlg_mainContactEmail").val(contactDetails.email);
	$("#addDlg_mainContactPrimaryPhone").val(contactDetails.primaryPhone);
	$("#addDlg_mainContactSecondaryPhone").val(contactDetails.secondaryPhone);
	$("#addDlg_mainContactStreet").val(contactDetails.street);
	$("#addDlg_mainContactCity").val(contactDetails.city);
	$("#addDlg_mainContactState").val(contactDetails.state);
	$("#addDlg_mainContactZip").val(contactDetails.zipcode);
	$("#addDlg_mainContactID").val(contactDetails.contactId);
	
	$("#editDlg_mainContactName").val(mainContactFullName);
	$("#editDlg_mainContactEmail").val(contactDetails.email);
	$("#editDlg_mainContactPrimaryPhone").val(contactDetails.primaryPhone);
	$("#editDlg_mainContactSecondaryPhone").val(contactDetails.secondaryPhone);
	$("#editDlg_mainContactStreet").val(contactDetails.street);
	$("#editDlg_mainContactCity").val(contactDetails.city);
	$("#editDlg_mainContactState").val(contactDetails.state);
	$("#editDlg_mainContactZip").val(contactDetails.zipcode);
	$("#editDlg_mainContactID").val(contactDetails.contactId);
}


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
		width: $(document).width() * 0.5,
		height: $(document).height() * 0.2,
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
		width: $(document).width() * 0.7,
		height: $(document).height() * 0.2,	
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		modal: true,
		dialogClass: "editDlgClass",
		open: function(event, ui) {

			/*
			 * When a user changes the main or other contact ID from the drop down menu that is inside the addDlg,
			 * we update the contact information fields (name, phone numbers, etc.) with the newly selected contact
			 */
			$("#editDlg_mainContactID").change("click", function() {
				var selected_mainContactID = $(this).children("option:selected").val();
				ajaxFetchContact(selected_mainContactID);				   
			}); 


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
					var selected_boardMemberID = $("#editDlg_boardMemberName").val(); // ID for board member

					/*
					 * Validates that the fields of the edit service client dialog are not empty.
					 * If all the fields are valid, updates the service client to the table and closes the dialog.
					 */
					if(checkForEmptyFields("#editDlg_name")){

						ajaxEditClientNow(selected_shid, "#editDlg_name", selected_mainContactID,
								 selected_boardMemberID, "#editDlg_category");

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
			$("#addDlg_name").val("");
			$("#addDlg_category").val("Animals");
			$('select').prop('selectedIndex', 0);

			/*
			 *  Upon open, we populate the main and other contact information fields (name, phone numbers, etc)
			 *  with the first contact in the list/database.
			 */
			var selected_mainContactID = $("#addDlg_mainContactID").children("option:selected").val();
			ajaxFetchContact(selected_mainContactID);


			/*
			 * When a user changes the main or other contact ID from the drop down menu that is inside the addDlg,
			 * we update the contact information fields (name, phone numbers, etc.) with the newly selected contact
			 */
			$("#addDlg_mainContactID").change("click", function() {
				var selected_mainContactID = $(this).children("option:selected").val();
				ajaxFetchContact(selected_mainContactID, true);				   
			}); 


			/*
			 * Removes previous error messages from the fields.
			 */
			$("#addDlg_name").removeClass("is-invalid");
			$(".validationTips" ).removeClass("alert alert-danger").text("");
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

					/*
					 * Validates that the fields of the add service client dialog are not empty.
					 * If all the fields are valid, adds the new service client to the table and closes the dialog.
					 */
					if(checkForEmptyFields("#addDlg_name")){

						ajaxCreateClientNow("#addDlg_name", selected_mainContactID, 
								selected_boardMemberID, "#addDlg_category");

						$("#addDlg").dialog("close");
					}
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
	
	
	/*
	 * enable page to invoke contact editor dialog
	 * when user clicks on button with id="btnContact".
	 * refreshes contact view on successful return
	 */
	dlgEdit = new ContactManager({
		btn: "#btnContact1", 
		task: "edit",
		success: function(ct) {
			console.log(ct);
			alert(1);
		}
		});


}

/**
 * When the DOM is completed loaded and ready, hide the dialogs and
 * create the functionality of the buttons.
 */
$(document).ready(onPageLoad);	