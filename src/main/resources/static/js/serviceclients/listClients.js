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


/*
 * function below is for adding a new service client to the list.
 * only passing minimal information
 *  not handling address information from the form, need to ask if client wants this
 *  not handling board member or contact information
 */
function addClient(client_name, client_cat, client_desc) {
	
	// Harvests the information from the add dialog form
	var nameStr =$(client_name).val();
	var catStr = $(client_cat).val();
	var descStr = $(client_desc).val(); //Don't use now but maybe we will in future
	
	$.ajax({
		method: "POST",
		url: "/srv/ajax/addServiceClient",
		cache: false,
		data: {name: nameStr, cat: catStr, desc: descStr},
		/*
		 * If successful then add the selected service client 
		 * with the new values.
		 */
		success: function(data) {
			console.log("added client");
			
			var id = $(data)[2];
			
			console.log(id);
			$('#sc_tbl_body').append(id);
			
			$(".del").on("click", function() {
				var selected_scid = $(this).attr('onDelClick');
				$("#delDlg").data("selectedClientID", selected_scid).dialog("open");
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
 * is not displayed in the table. We obtain the information from the AJAX call by the scInfo.html which
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
			 * the text in scInfo.html. There is probably a better way to do this, hopefully by selecting
			 * a div's unique ID and so the scInfo.html has divs for this future change.
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
	
	//Register and hide the add dialog div until an add button is clicked on.
	$("#addDlg").dialog({
		autoOpen: false,
		height: 500,
		width: 800,
		modal: true,
		dialogClass: "addDlgClass",	
		open: function(event, ui) {
			$("#addDlg_clientName").val("");
	    	$("#addDlg_descOfClient").val("");
	    	$("#addDlg_selcCat").val("Animals");	
		},							
		buttons: [
			{
				text: "Add Service Client", 
				"id": "addBtnDlg",
				"class": 'btn',
				click: function() {		
					addClient("#addDlg_clientName", "#addDlg_selcCat", "#addDlg_descOfClient");
					$("#addDlg").dialog("close");
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