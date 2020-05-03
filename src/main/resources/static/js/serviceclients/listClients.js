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
		url: "/srv/ajax/delServiceClient",
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
 * Function below if for updating a service client but only their name
 * and category. Since description isn't stored in service client domain class
 * I'm not updating
 * 
 * @param client_id
 * @param client_name
 * @param client_cat
 * @param client_desc
 * @returns
 */
function editClient(client_id, client_name, client_cat, client_desc) {

	// Harvests the data values from the form
	var idStr = client_id;
	var nameStr  = $(client_name).val();
	var catStr  = $(client_cat).val();
	var descStr = $(client_desc).val();


	$.ajax({
		method: "POST",
		url: "/srv/ajax/editServiceClient",
		cache: false,
		data: {ID: idStr, name: nameStr, cat: catStr, desc: descStr},
		/*
		 * If successful then update the selected service client 
		 * with the new values.
		 */
		success: function(data) {
			console.log("updated client");

			$("#scid-" + client_id + " td[name ='sc_title']").html($(client_name).val());
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
		url: "/srv/ajax/addServiceClient",
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
 * contains the selected service client's information. 
 * 
 * @param client_id
 * @returns
 */
function scInfo(client_id) {

	var idStr = client_id; // Selected client's ID

	$.ajax({
		method: "GET",
		url: "/srv/ajax/infoServiceClient",
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
			var setCat = $(data)[4].innerText;
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
 * selected main contact's information. 
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
 * selected other/secondary contact's information. 
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
 * When the DOM is completed loaded and ready, hide the dialogs and
 * create the functionality of the buttons.
 */
$(document).ready(function() {	

	//Register and hide the delete dialog div until a delete button is clicked on.
	$("#delDlg").dialog({
		autoOpen: false,
		height: 350,
		width: 400,
		modal: true,
		dialogClass: "delDlgClass",
		create: function(event, ui) { 
			$(".delBtnClass").addClass("btn btn-danger");
			$(".cancBtnClass").addClass("btn btn-secondary");
		},
		open: function(event, ui) {
			/*
			 * Prompt on the delete service client dialog, verifying if they want to delete the selected service client.
			 */ 
			$("#delMsg1").html("The following service client will be permanently deleted and cannot be recovered.");
			$("#delMsg2").html("Are you sure you want to delete?");
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
			 *  Upon open, we populate the main and other contact information fields (name, phone numbers, etc)
			 *  with the first contact in the list/database.
			 */
			var selected_mcID = $(".mcRow").children("option:selected").val();
			populateMCFields(selected_mcID);

			var selected_ocID = $(".ocRow").children("option:selected").val();
			populateOCFields(selected_ocID);
			
			
			/*
	    	 * Resets all the fields of the add dialog to empty.
	    	 */
	    	$("#addDlg_name").val("");
	    	$("#addDlg_bmName").val("");
	    	$("#addDlg_cat").val("Animals");
			/*
			 * When a user changes the main or other contact ID from the drop down menu that is inside the addDlg,
			 * we update the contact information fields (name, phone numbers, etc.) with the newly selected contact
			 */
			$(".mcRow").change("click", function() {
				var selected_mcID = $(this).children("option:selected").val();
				populateMCFields(selected_mcID);				   
			}); 

			$(".ocRow").change("click", function() {
				var selected_ocID = $(this).children("option:selected").val();
				populateOCFields(selected_ocID);				   
			}); 
		},							
		buttons: [
			{
				text: "Add Service Client", 
				"id": "addBtnDlg",
				"class": 'btn',
				click: function() {		
				
					var selected_mcID = $(".mcRow").children("option:selected").val(); // ID for main contact
					var selected_ocID = $(".ocRow").children("option:selected").val(); // ID for other/secondary contact
					
					addClient("#addDlg_name", selected_mcID, selected_ocID, "#addDlg_bmName", "#addDlg_cat");
					$("#addDlg").dialog("close");
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

	//Register and hide the add dialog div until an add button is clicked on.
	$("#editDlg").dialog({
		autoOpen: false,
		height: 500,
		width: 800,
		modal: true,
		dialogClass: "editDlgClass",
		open: function(event, ui) {

			// Harvests the selected servant client's old values from the table.
			var selected_shid = $("#editDlg").data('selectedClientID');
			var set_name = $("#scid-" + selected_shid + " td[name ='sc_title']").text();
			var set_cat = $("#scid-" + selected_shid + " td[name ='sc_category']").text().trim();

			// Sets the dialog's fields to the selected servant client's old values upon opening.
			$("#editDlg_clientName").val(set_name);
			$("#editDlg_selcCat").val(set_cat);
		},							
		buttons: [
			{
				text: "Update Service Client", 
				"id": "addBtnDlg",
				"class": 'btn',
				click: function() {		
					var selected_shid = $("#editDlg").data('selectedClientID'); // The selected service client's ID

					editClient(selected_shid, "#editDlg_clientName", "#editDlg_selcCat", "#editDlg_descOfClient");
					$("#editDlg").dialog("close");
				}
			},
			{
				text: "Update Contact(s)",
				"id" : "addContactBtn",
				"class" : 'btn btn-info',
				click: function() {
					$("#editDlg").dialog("close");
				}
			},
			{
				text: "Update Board Member",
				"class" : 'btn btn-info',
				click: function() {
					$('#editDlg').dialog("close");
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
	 * Opens add service client dialog
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