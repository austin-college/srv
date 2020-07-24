/**
 * The following function replaces a HTML paragraph's text with error
 * messages to the user on the invalid fields in the add and edit
 * dialogs.
 * 
 * @param msg
 * @returns
 */
function updateTips(msg) {
	$(".ui-dialog").effect("shake");
	$(".validationTips").text(msg).addClass("alert alert-danger");
}

/**
 * opens the dialog for selecting a user to promote a servant to 
 * a board member
 */
function onNewClick() {
	$("#dlgUserSel").dialog("open");
}

/**
 * Ajax call to create/promote a servant user to a board member
 */
function createBmNow(usernameField) {
	
	// get the string value of the username
	var usernameStr = $(usernameField).val();
	
	$.ajax({
		method: "POST",
		url: "/srv/boardmembers/ajax/new",
		cache: false,
		data: {username: usernameStr},
	})
	/*
	 * If successful then add the board member user to the list
	 */
	.done(function(bm) {
		console.log("added board member");
		console.log(bm);
		
		var id = $(bm)[0]; // obtains the new bm's id from the ajax response
		
		console.log(id); // verifies id
		
		$("#bmTblBody").append(id);
		
		// Append the buttons and their functionality to the new board member
		$(".btnBmDel").click(onDeleteClick);

		$(".btnBmEdit").click(onEditClick);

		$(".btnBmView").click(onViewClick);
	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Error");
		updateTips(jqXHR.responseText);
	});
	
}
/**
 * launch the action for deleting given the board member user id.
 * Present dialog to confirm. Then let the dialog callbacks do all the
 * work.
 */
function onDeleteClick() {

	// assume user id attribute exists on button
	var uid = $(this).attr("uid");
	
	// ...get the value of name cell in row
	var row_name = $("#row" + uid + " td[name = 'bm_fullName']").html();
	
	// ...and get the value of username cell in row
	var row_username = $("#row" + uid + " td[name = 'bm_username").html();
	
	// fill in the dialog with data from the current row board member
	$("#delBmId").html(uid);
	$("#delBmName").html(row_name);
	$("#delBmUserName").html(row_username);
	
	// open the dialog...let it take over
	$("#dlgDelete").dialog("open");
}

//TODO alert user feature not functional yet
function onEditClick() {
	alert("Edit feature is not functional yet.");
}

//TODO alert user feature not functional yet
function onViewClick() {
	alert("View feature is not functional yet.");
}

/**
 * makes the request back to our server to delete the board member whose
 * id we extract from the confirmation dialog
 */
function ajaxDeleteBoardMemberNow() {
	
	// The ID of the selected board member to be deleted...from the dialog
	var idStr = $("#delBmId").html();
	
	$.ajax({
		method : "POST",
		url : "/srv/boardmembers/ajax/del/" + idStr,
		cache : false
	})
	/*
	 * If successful, then remove the selected board member row from the table.
	 */
	.done(function(uid) {

		$("#row" + uid).remove(); // remove row from table.
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

// Final preparations once the page is loaded. we hide stuff and attach
// functions to buttons.
function onPageLoad() {
	
	// connection action to unique create button
	$("#btnBmNew").click(onNewClick);

	// connect the delete action to all delete buttons tagged with btnBmDel
	// class
	$(".btnBmDel").click(onDeleteClick);

	// connect the edit action to all edit buttons
	$(".btnBmEdit").click(onEditClick);

	// connect the view action to all view buttons
	$(".btnBmView").click(onViewClick);
		
	// Register and hide the delete dialog div until a delete button is clicked on.
	$("#dlgDelete").dialog({
		autoOpen : false, 
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
		buttons : [ {
			text : "DELETE",
			"class" : 'btn btn-danger',
			click : function() {
				ajaxDeleteBoardMemberNow();
			}
		},

		{
			text : "CANCEL",
			"class" : 'btn btn-secondary',
			click : function() {
				$(this).dialog("close");
			}
		} ]
	});
	
	// dialog for selecting a servant user when creating/promoting a new board member
	$("#dlgUserSel").dialog({
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
			
			// clear all checkboxes upon open
			$(".boxSel").prop("checked", false);
			
			// remove previous error messages
			$(".validationTips").removeClass("alert alert-danger").text("");
		},
		buttons: [
			{
				text: "Submit",
				"class": 'btn addBtn',
				click: function() {
					console.log("submit on select dialog");
					
					// count the number of checked boxes
					var userChecked = $('input[class=boxSel]:checked').length;
					
					// verify a checkbox was selected, cannot submit until one is,
					// throw error to user stating so
					if (userChecked == 1) {
						console.log("a checkbox is checked.");
						
						// get the selected servant user's username to pass to function
						console.log($("#newBmUsername").val());
						
						createBmNow("#newBmUsername");
						
						$(this).dialog("close");
					}
					else if(userChecked == 0) {
						console.log("all checkboxes are unchecked.");
						updateTips("A user must be selected.");
					}
				}
			},
			{
				text: "Cancel",
				"class": "btn btn-secondary",
				click: function() {
					console.log("cancel on select dialog");
					$(this).dialog("close");
				}
			}
		]
	});
	
	// Allows for searching and sorting when selecting a servant user to promote
	$('#tblUsers').DataTable({	
		"paging": false,
		"searching": true,
		"info": false
	});
	
	$(".boxSel").click( function() {

		var state = $(this).prop("checked");

		if (state) {
			var username = $(this).attr("value");
			$("#newBmUsername").val(username);
		}

		$(".boxSel").prop("checked",false);  // clear all others		
		$(this).prop("checked",state);  // reassert current state on current button  
	});
	
}

// when the DOM is completely loaded, do final preparations by calling
// onPageLoad function
$(document).ready(onPageLoad)