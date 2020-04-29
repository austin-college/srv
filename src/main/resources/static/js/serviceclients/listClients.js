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
			console.log("done");
			
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
	var descStr = $(client_desc).val();
	
	$.ajax({
		method: "POST",
		url: "/srv/ajax/addServiceClient",
		cache: false,
		data: {name: nameStr, cat: catStr, desc: descStr},
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
				text: "Add Contact(s)",
					  "id" : "addContactBtn",
					  "class" : 'btn btn-info',
				click: function() {
					$("#addDlg").dialog("close");
				}
			},
			{
				text: "Assign Board Member",
					  "class" : 'btn btn-info',
				click: function() {
					$('#addDlg').dialog("close");
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
			$("#editDlg").dialog("open");
		});


	});