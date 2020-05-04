/**
 * The following function verifies that none of the add service client or edit service
 * clients dialogs have empty fields.
 * 
 * @param client_name
 * @param bm_Name
 * @returns
 */
function checkForEmptyFields(client_name, bm_Name) {

	var valid = true;
	var msg = "Please complete the selected fields."; // Error message
	var counter = 0; // For the invalid message/effect not to occur several times.

	/*
	 * Removes previous error messages on the fields for the add service client dialog.
	 */
	$("#addDlg_name").removeClass("is-invalid");
	$("#addDlg_bmName").removeClass("is-invalid");
	
	/*
	 * Removes previous error messages on the fields for the edit service client dialog.
	 */
	$("#editDlg_name").removeClass("is-invalid");
	$("#editDlg_bmName").removeClass("is-invalid");

	// Checks to see if the service client's name field is empty.
	if (!$(client_name).val()) {
		$(client_name).addClass("is-invalid");
		if(counter == 0) {
			counter++;
			updateTips(msg);
		}
		valid = false;
	}

	// Checks to see if the service client's board member name field is empty.
	if (!$(bm_Name).val()) {
		$(bm_Name).addClass("is-invalid");
		if(counter == 0) {
			counter++;
			updateTips(msg);
		}
		valid = false;
	}

	return valid;
}

/** 
 * The delete client function makes an AJAX call to remove the selected
 * service client from the table
 * 
 * TODO
 * handle when an exception is thrown
 * 
 * @param client_id - the service client ID to be deleted
 */
function delClient(client_id) {
	// The ID of the selected service hour to be deleted is harvested from the table
	var idStr = client_id;

	$.ajax({
		method: "POST",
		url: "/srv/ajax/delSc",
		cache: false,
		data: {ID: idStr},
		//dataType: "text",
		/*
		 * If successful, then remove the selected service client from the table.
		 */
		success: function(data) {
			console.log("deleted client");

			$("#scid-"+ client_id).remove();
		},
		/*
		 * If unsuccessful (invalid data values), display error message and reasoning.
		 */
		error: function(jqXHR, textStatus) {
			alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
		}
	});
}


/**
 * Updates an existing service client in the table with the new values. The parameters follow the update method
 * in the ServiceClientDao except that the main contact's name is also passed in, in order to update the table 
 * immediately (upon a successful response). 
 * 
 * @param client_id
 * @param client_name
 * @param mc_ID
 * @param oc_ID
 * @param mc_name
 * @param bm_Name
 * @param client_cat
 * @returns
 */
function editClient(client_id, client_name, mc_ID, oc_ID, mc_name, bm_Name, client_cat) {

	// Harvests the information (id, service client's name, board member's name, and category) from the edit dialog form
	var idStr = client_id;
	var nameStr  = $(client_name).val();
	var bmStr = $(bm_Name).val();
	var catStr = $(client_cat).val();

	$.ajax({
		method: "POST",
		url: "/srv/ajax/editSc",
		cache: false,
		data: {ID: idStr, clientName: nameStr, mcID: mc_ID, ocID: oc_ID, bmName: bmStr, cat: catStr},
		/*
		 * If successful then update the selected service client 
		 * with the new values.
		 */
		success: function(data) {
			console.log("updated client");
			
			console.log($(mc_name).val()); // Verifies the new service client's main contact's name

			// Updates the edited service client's row with the new values
			$("#scid-" + client_id + " td[name ='sc_title']").html($(client_name).val());
			$("#scid-" + client_id + " td[name ='sc_contact_name']").html($(mc_name).val());
			$("#scid-" + client_id + " td[name ='sc_bm_name']").html($(bm_Name).val());
			$("#scid-" + client_id + " td[name ='sc_category']").html($(client_cat).val());
			
		},
		/*
		 * If unsuccessful, display error message and reasoning.
		 */
		error: function(jqXHR, textStatus) {
			alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
		}
	});
}

/**
 * Add a new service client to the service client table. The parameters correspond with the
 * create method of the ServiceClientDao.
 * 
 * @param client_name
 * @param mc_ID
 * @param oc_ID
 * @param bm_Name
 * @param client_cat
 * @returns
 */
function addClient(client_name, mc_ID, oc_ID, bm_Name, client_cat) {

	// Harvests the information (service client's name, board member's name, and category) from the add dialog form
	var nameStr =$(client_name).val();
	var bmStr = $(bm_Name).val();
	var catStr = $(client_cat).val();

	$.ajax({
		method: "POST",
		url: "/srv/ajax/addSc",
		cache: false,
		data: {clientName: nameStr, mcID: mc_ID, ocID: oc_ID, bmName: bmStr, cat: catStr},
		/*
		 * If successful then add the service client to the list with the new values.
		 */
		success: function(data) {
			console.log("added client");

			var id = $(data)[2]; // Obtains the new service client's ID from the AJAX response

			console.log(id); // Verifies the new service client's ID

			$('#sc_tbl_body').append(id); // Appends the new service client row to the table

			// Appends the buttons and their functionality to the new service client
			$(".edit ").on("click", function() {
				var selected_scid = $(this).attr('onEditClick');
				$("#editDlg").data("selectedClientID", selected_scid).dialog("open");
			});

			$(".del").on("click", function() {
				var selected_scid = $(this).attr('onDelClick');
				$("#delDlg").data("selectedClientID", selected_scid).dialog("open");
			});

			$(".scRow").on("click", function() {
				var selected_scid = $(this).attr('onRowClick');
				$("#scInfoDlg").data("selectedClientID", selected_scid).dialog("open");
			});
		},
		/*
		 * If unsuccessful, display error message and reasoning.
		 */
		error: function(jqXHR, textStatus) {
			alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
		}
	});	
}

/**
 * When a row is selected on in the service client's table, the scInfoDlg takes us to this
 * function in order to make an AJAX call to the ServiceClientController. That way, from the
 * ServiceClientController we can access the ServiceClientDao to access the ServiceClient database
 * in order to obtain the information about the selected service client's contact information as this
 * is not displayed in the table. We obtain the information from the AJAX call by the ajax_scInfo.html which
 * contains the selected service client's information. Note this also does the same thing for the editDlg
 * 
 * @param client_id
 * @returns
 */
function scInfo(client_id) {

	var idStr = client_id; // Selected client's ID

	$.ajax({
		method: "GET",
		url: "/srv/ajax/infoSc",
		cache: false,
		data: {ID: idStr},
		/*
		 * If successful then populate the scInfoDlg with the selected service client's values 
		 */
		success: function(data) {

			console.log("client info");

			/*
			 * In order to obtain the information passed back from the AJAX call, we have to 
			 * index the data sent back (from AJAX) by every 2 and specify 'innerText' to harvest
			 * the text in ajax_scInfo.html. There is probably a better way to do this, hopefully by selecting
			 * a div's unique ID and so the ajax_scInfo.html has divs for this future change.
			 */
			var setClientName = $(data)[0].innerText;
			var setBmName =  $(data)[2].innerText; 
			var setCat = $(data)[4].innerText.trim();
			var setMcName = $(data)[6].innerText;
			var setMcEmail = $(data)[8].innerText;
			var setMcWorkPhone = $(data)[10].innerText;
			var setMcMobilePhone = $(data)[12].innerText;
			var setMcStreet = $(data)[14].innerText;
			var setMcCity = $(data)[16].innerText;
			var setMcState = $(data)[18].innerText;
			var setMcZip = $(data)[20].innerText;
			var setOcName = $(data)[22].innerText;
			var setOcEmail = $(data)[24].innerText;
			var setOcWorkPhone = $(data)[26].innerText;
			var setOcMobilePhone = $(data)[28].innerText;
			var setOcStreet = $(data)[30].innerText;
			var setOcCity = $(data)[32].innerText;
			var setOcState = $(data)[34].innerText;
			var setOcZip = $(data)[36].innerText;
			var setMcID = $(data)[38].innerText.trim();
			var setOcID = $(data)[40].innerText.trim();
			
			/*
			 * From the values above, we can set the input fields within the scInfoDlg (in listClients.html)
			 * with the data that the ServiceClientController gave us as a result of a successful AJAX call.
			 */
			$("#scInfoDlg_name").val(setClientName);
			$("#scInfoDlg_bmName").val(setBmName);
			$("#scInfoDlg_cat").val(setCat);
			$("#scInfoDlg_mcName").val(setMcName);
			$("#scInfoDlg_mcEmail").val(setMcEmail);
			$("#scInfoDlg_mcWorkPhone").val(setMcWorkPhone);
			$("#scInfoDlg_mcMobilePhone").val(setMcMobilePhone);
			$("#scInfoDlg_mcStreet").val(setMcStreet);
			$("#scInfoDlg_mcCity").val(setMcCity);
			$("#scInfoDlg_mcState").val(setMcState);
			$("#scInfoDlg_mcZip").val(setMcZip);
			$("#scInfoDlg_ocName").val(setOcName);
			$("#scInfoDlg_ocEmail").val(setOcEmail);
			$("#scInfoDlg_ocWorkPhone").val(setOcWorkPhone);
			$("#scInfoDlg_ocMobilePhone").val(setOcMobilePhone);
			$("#scInfoDlg_ocStreet").val(setOcStreet);
			$("#scInfoDlg_ocCity").val(setOcCity);
			$("#scInfoDlg_ocState").val(setOcState);
			$("#scInfoDlg_ocZip").val(setOcZip);
			$("#scInfoDlg_mcID").val(setMcID);
			$("#scInfoDlg_ocID").val(setOcID);
			
			/*
			 * From the values above, we can set the input fields within the editDlg (in listClients.html)
			 * with the data that the ServiceClientController gave us as a result of a successful AJAX call.
			 */
			$("#editDlg_name").val(setClientName);
			$("#editDlg_bmName").val(setBmName);
			$("#editDlg_cat").val(setCat);
			$("#editDlg_mcName").val(setMcName);
			$("#editDlg_mcEmail").val(setMcEmail);
			$("#editDlg_mcWorkPhone").val(setMcWorkPhone);
			$("#editDlg_mcMobilePhone").val(setMcMobilePhone);
			$("#editDlg_mcStreet").val(setMcStreet);
			$("#editDlg_mcCity").val(setMcCity);
			$("#editDlg_mcState").val(setMcState);
			$("#editDlg_mcZip").val(setMcZip);
			$("#editDlg_ocName").val(setOcName);
			$("#editDlg_ocEmail").val(setOcEmail);
			$("#editDlg_ocWorkPhone").val(setOcWorkPhone);
			$("#editDlg_ocMobilePhone").val(setOcMobilePhone);
			$("#editDlg_ocStreet").val(setOcStreet);
			$("#editDlg_ocCity").val(setOcCity);
			$("#editDlg_ocState").val(setOcState);
			$("#editDlg_mcID").val(setMcID);
			$("#editDlg_ocID").val(setOcID);

		},
		/*
		 * If unsuccessful, display error message and reasoning.
		 */
		error: function(jqXHR, textStatus) {
			alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
		}
	});
}

/**
 * When a contact ID is selected on (or upon opening of the add dialog) in the add dialog, the addDlg takes us to this
 * function in order to make an AJAX call to the ServiceClientController. That way, from the ServiceClientController we 
 * can access the ContactDao to access the Contact database in order to obtain the information about the selected main 
 * contact information. We obtain the information from the AJAX call by the ajax_contactFields.html which contains the 
 * selected main contact's information. Note this also does the same thing for the edit dialog.
 * 
 * @param contact_id
 * @returns
 */
function populateMCFields(contact_id) {

	var idStr = contact_id; // Selected main contact's ID

	console.log(idStr); // Verifying id in console

	$.ajax({
		method: "GET",
		url: "/srv/ajax/fillMCFields",
		cache: false,
		data: {ID: idStr},
		/*
		 * If successful, populate the main contact information fields with the data received
		 * from AJAX, which should contain the contact's information retrieved from the contact database.
		 */
		success: function(data) {

			console.log("add main contact info");

			/*
			 * In order to obtain the information passed back from the AJAX call, we have to 
			 * index the data sent back (from AJAX) by every 2 and specify 'innerText' to harvest
			 * the text in ajax_contactFields.html. There is probably a better way to do this, hopefully by selecting
			 * a div's unique ID and so the  ajax_contactFields.html has divs for this future change.
			 */
			var setMcName = $(data)[0].innerText;
			var setMcEmail = $(data)[2].innerText;
			var setMcWorkPhone = $(data)[4].innerText;
			var setMcMobilePhone = $(data)[6].innerText;
			var setMcStreet = $(data)[8].innerText;
			var setMcCity = $(data)[10].innerText;
			var setMcState = $(data)[12].innerText;
			var setMcZip = $(data)[14].innerText;

			/*
			 * From the values above, we can set the main contact input fields within the addDlg (in listClients.html)
			 * with the data that the ServiceClientController gave us as a result of a successful AJAX call.
			 */
			$("#addDlg_mcName").val(setMcName);
			$("#addDlg_mcEmail").val(setMcEmail);
			$("#addDlg_mcWorkPhone").val(setMcWorkPhone);
			$("#addDlg_mcMobilePhone").val(setMcMobilePhone);
			$("#addDlg_mcStreet").val(setMcStreet);
			$("#addDlg_mcCity").val(setMcCity);
			$("#addDlg_mcState").val(setMcState);
			$("#addDlg_mcZip").val(setMcZip);
			
			/*
			 * From the values above, we can set the main contact input fields within the editDlg (in listClients.html)
			 * with the data that the ServiceClientController gave us as a result of a successful AJAX call.
			 */
			$("#editDlg_mcName").val(setMcName);
			$("#editDlg_mcEmail").val(setMcEmail);
			$("#editDlg_mcWorkPhone").val(setMcWorkPhone);
			$("#editDlg_mcMobilePhone").val(setMcMobilePhone);
			$("#editDlg_mcStreet").val(setMcStreet);
			$("#editDlg_mcCity").val(setMcCity);
			$("#editDlg_mcState").val(setMcState);
			$("#editDlg_mcZip").val(setMcZip);
		},
		/*
		 * If unsuccessful, display error message and reasoning.
		 */
		error: function(jqXHR, textStatus) {
			alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
		}
	});	
}

/**
 * When a contact ID is selected on (or upon opening of the add dialog) in the add dialog, the addDlg takes us to this
 * function in order to make an AJAX call to the ServiceClientController. That way, from the ServiceClientController we 
 * can access the ContactDao to access the Contact database in order to obtain the information about the selected other/secondary 
 * contact information. We obtain the information from the AJAX call by the ajax_contactFields.html which contains the 
 * selected other/secondary contact's information. Note this also is the same for the edit dialog.
 * 
 * @param contact_id
 * @returns
 */
function populateOCFields(contact_id) {

	var idStr = contact_id; // Selected other contact's ID

	console.log(idStr); // Verifying id in console

	$.ajax({
		method: "GET",
		url: "/srv/ajax/fillOCFields",
		cache: false,
		data: {ID: idStr},
		/*
		 * If successful, populate the other/secondary contact information fields with the data received
		 * from AJAX, which should contain the contact's information retrieved from the contact database.
		 */
		success: function(data) {

			console.log("add other/secondary contact info");

			/*
			 * In order to obtain the information passed back from the AJAX call, we have to 
			 * index the data sent back (from AJAX) by every 2 and specify 'innerText' to harvest
			 * the text in ajax_contactFields.html. There is probably a better way to do this, hopefully by selecting
			 * a div's unique ID and so the  ajax_contactFields.html has divs for this future change. Note we are
			 * only interested in other/secondary contact information which is why we do not begin indexing at 0
			 */
			var setOcName = $(data)[16].innerText;
			var setOcEmail = $(data)[18].innerText;
			var setOcWorkPhone = $(data)[20].innerText;
			var setOcMobilePhone = $(data)[22].innerText;
			var setOcStreet = $(data)[24].innerText;
			var setOcCity = $(data)[26].innerText;
			var setOcState = $(data)[28].innerText;
			var setOcZip = $(data)[30].innerText;

			/*
			 * From the values above, we can set the other/secondary contact input fields within the addDlg (in listClients.html)
			 * with the data that the ServiceClientController gave us as a result of a successful AJAX call.
			 */
			$("#addDlg_ocName").val(setOcName);
			$("#addDlg_ocEmail").val(setOcEmail);
			$("#addDlg_ocWorkPhone").val(setOcWorkPhone);
			$("#addDlg_ocMobilePhone").val(setOcMobilePhone);
			$("#addDlg_ocStreet").val(setOcStreet);
			$("#addDlg_ocCity").val(setOcCity);
			$("#addDlg_ocState").val(setOcState);
			$("#addDlg_ocZip").val(setOcZip);
			

			/*
			 * From the values above, we can set the other/secondary contact input fields within the editDlg (in listClients.html)
			 * with the data that the ServiceClientController gave us as a result of a successful AJAX call.
			 */
			$("#editDlg_ocName").val(setOcName);
			$("#editDlg_ocEmail").val(setOcEmail);
			$("#editDlg_ocWorkPhone").val(setOcWorkPhone);
			$("#editDlg_ocMobilePhone").val(setOcMobilePhone);
			$("#editDlg_ocStreet").val(setOcStreet);
			$("#editDlg_ocCity").val(setOcCity);
			$("#editDlg_ocState").val(setOcState);
			$("#editDlg_ocZip").val(setOcZip);
		},
		/*
		 * If unsuccessful, display error message and reasoning.
		 */
		error: function(jqXHR, textStatus) {
			alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
		}
	});	
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
 * When the DOM is completed loaded and ready, hide the dialogs and
 * create the functionality of the buttons.
 */
$(document).ready(function() {	

	//Register and hide the delete dialog div until a delete button is clicked on.
	$("#delDlg").dialog({
		autoOpen: false,
		height: 300,
		width: 625,
		modal: true,
		dialogClass: "delDlgClass",
		create: function(event, ui) { 
			$(".delBtnClass").addClass("btn btn-danger");
			$(".cancBtnClass").addClass("btn btn-secondary");
		},
		open: function(event, ui) {
			
			 var selected_scid = $("#delDlg").data('selectedClientID'); // The selected service client's ID
			 var set_name = $("#scid-" + selected_scid + " td[name ='sc_title']").text(); // The selected service client's title/name
	
			/*
			 * Prompt on the delete service client dialog, specifying the name of the selected service client to the user,
			 *  verifying if they want to delete the selected service client.
			 */ 
			$("#delMsg1").html(set_name + " will be permanently deleted and cannot be recovered.");
			$("#delMsg2").html("Are you sure you want to delete " + set_name + "?");
		},							
		buttons: [
			{
				text: "DELETE SERVICE CLIENT", 
				"class": 'delBtnClass',
				click: function() {
					delClient($("#delDlg").data('selectedClientID'));
					$("#delDlg").dialog("close");
					$("#delMsg1").empty();
					$("#delMsg2").empty();   			
				}
			},
			{	
				text: "CANCEL",
				"class": 'cancBtnClass',
				click: function() {
					$("#delDlg").dialog("close");
					$("#delMsg1").empty();
					$("#delMsg2").empty();
				}
			}]
	});

	/*
	 * Register and hide the add dialog div until an add button is clicked on.
	 */
	$("#addDlg").dialog({
		autoOpen: false,
		height: 500,
		width: 700,
		modal: true,
		dialogClass: "addDlgClass",	
		open: function(event, ui) {	

			/*
			 * Resets all the fields of the add dialog to empty.
			 */
			$("#addDlg_name").val("");
			$("#addDlg_bmName").val("");
			$("#addDlg_cat").val("Animals");
			$('select').prop('selectedIndex', 0);
			
			/*
			 *  Upon open, we populate the main and other contact information fields (name, phone numbers, etc)
			 *  with the first contact in the list/database.
			 */
			var selected_mcID = $("#addDlg_mcID").children("option:selected").val();
			populateMCFields(selected_mcID);

			var selected_ocID = $("#addDlg_ocID").children("option:selected").val();
			populateOCFields(selected_ocID);

			/*
			 * When a user changes the main or other contact ID from the drop down menu that is inside the addDlg,
			 * we update the contact information fields (name, phone numbers, etc.) with the newly selected contact
			 */
			$("#addDlg_mcID").change("click", function() {
				var selected_mcID = $(this).children("option:selected").val();
				populateMCFields(selected_mcID);				   
			}); 

			$("#addDlg_ocID").change("click", function() {
				var selected_ocID = $(this).children("option:selected").val();
				populateOCFields(selected_ocID);				   
			}); 

			/*
			 * Removes previous error messages from the fields.
			 */
			$("#addDlg_name").removeClass("is-invalid");
			$("#addDlg_bmName").removeClass("is-invalid");
			$(".validationTips" ).removeClass("alert alert-danger").text("");
		},							
		buttons: [
			{
				text: "Add Service Client", 
				"id": "addBtnDlg",
				"class": 'btn',
				click: function() {		

					var selected_mcID = $("#addDlg_mcID").children("option:selected").val(); // ID for main contact
					var selected_ocID = $("#addDlg_ocID").children("option:selected").val(); // ID for other/secondary contact

					/*
					 * Validates that the fields of the add service client dialog are not empty.
					 * If all the fields are valid, adds the new service client to the table and closes the dialog.
					 */
					if(checkForEmptyFields("#addDlg_name", "#addDlg_bmName")){
						
						addClient("#addDlg_name", selected_mcID, selected_ocID, "#addDlg_bmName", "#addDlg_cat");
						
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

	// Register and hide the add dialog div until an edit button is clicked on.
	$("#editDlg").dialog({
		autoOpen: false,
		height: 500,
		width: 700,
		modal: true,
		dialogClass: "editDlgClass",
		open: function(event, ui) {
			
			var selected_scid = $("#editDlg").data('selectedClientID'); // Harvests the selected service client's id from the table to pass to js function
			
			// Populates the selected service client's contact information for the edit service client dialog box.
			scInfo(selected_scid); 
			
			/*
			 * When a user changes the main or other contact ID from the drop down menu that is inside the editDlg,
			 * we update the contact information fields (name, phone numbers, etc.) with the newly selected contact
			 */
			$("#editDlg_mcID").change("click", function() {
				var selected_mcID = $(this).children("option:selected").val();
				populateMCFields(selected_mcID);				   
			}); 

			$("#editDlg_ocID").change("click", function() {
				var selected_ocID = $(this).children("option:selected").val();
				populateOCFields(selected_ocID);				   
			}); 
			
			/*
			 * Removes previous error messages from the fields.
			 */
			$("#editDlg_name").removeClass("is-invalid");
			$("#editDlg_bmName").removeClass("is-invalid");
			$(".validationTips" ).removeClass("alert alert-danger").text("");
		},							
		buttons: [
			{
				text: "Update Service Client", 
				"id": "addBtnDlg",
				"class": 'btn',
				click: function() {		
					
					var selected_shid = $("#editDlg").data('selectedClientID'); // The selected service client's ID
					var selected_mcID = $("#editDlg_mcID").children("option:selected").val(); // ID for main contact
					var selected_ocID = $("#editDlg_ocID").children("option:selected").val(); // ID for other/secondary contact

					/*
					 * Validates that the fields of the edit service client dialog are not empty.
					 * If all the fields are valid, updates the service client to the table and closes the dialog.
					 */
					if(checkForEmptyFields("#editDlg_name", "#editDlg_bmName")){
						
						editClient(selected_shid, "#editDlg_name", selected_mcID, selected_ocID, "#editDlg_mcName", "#editDlg_bmName", "#editDlg_cat");
						
						$("#editDlg").dialog("close");
					}					
				}
			},
			{	
				text: "Cancel",
				"class": 'btn btn-secondary',
				click: function() {
					$("#editDlg").dialog("close");

				}
			}]
	});

	/*
	 * Register and hide the service hour information dialog div until a row is clicked on.
	 * The selected service client's ID is passed into this function when the row is clicked on, which is
	 * then harvested in order to pass the ID into the JavaScript function to be used further for
	 * selecting the correct service client information from the database in order to populate the dialog's fields.
	 */ 
	$("#scInfoDlg").dialog({
		autoOpen: false,
		height: 500,
		width: 700,
		modal: true,
		open: function(event, ui) {			
			var selected_scid = $("#scInfoDlg").data('selectedClientID'); // Harvests the selected service client's id from the table to pass to js function
			scInfo(selected_scid);		
		}
	});

	/* 
	 * Opens delete service client dialog and passes in the selected delete button's 
	 * service client's id when a delete button is clicked on
	 */
	$(".del").on("click", function() {
		var selected_scid = $(this).attr('onDelClick');
		$("#delDlg").data("selectedClientID", selected_scid).dialog("open");
	});

	/* 
	 * Opens add service client dialog
	 */
	$(".addBtn").on("click", function() {
		$("#addDlg").dialog("open");
	});

	/* 
	 * Opens a edit service client dialog and passes in the selected row's service client's id when a user
	 * clicks on an edit button.
	 */
	$(".edit ").on("click", function() {
		var selected_scid = $(this).attr('onEditClick');
		$("#editDlg").data("selectedClientID", selected_scid).dialog("open");
	});

	/*
	 * Opens a service client dialog and passes in the selected row's service client's id when a user
	 * clicks on a row in the service client table in order to view the information about the selected
	 * service client such as their main and other contact information (phone numbers, address, etc)
	 */
	$(".scRow").on("click", function() {
		var selected_scid = $(this).attr('onRowClick');
		$("#scInfoDlg").data("selectedClientID", selected_scid).dialog("open");
	});

	/*
	 *  Allows for the table columns to be sorted disables the extra features such as 'searching'
	 */
	$('#client_tbl').DataTable({	
		"paging": false,
		"searching": false,
		"info": false
	});
	$('.dataTables_length').addClass('bs-select');

});