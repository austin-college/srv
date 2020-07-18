//TODO alert user feature not functional yet
function onNewClick() {
	alert("Create New feature is not functional yet.");
}

/*
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

//TODO alert user feature not functional yet
function onCheckBoxClick() {
	alert("Promote board member feature is not functional yet.");
}

/*
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
	
	// connect the co-chair action to all checkboxes
	$(".boxSel").click(function() {
		
		// revert checkbox to original state
		var state = $(this).prop("checked");
		console.log(state);
		$(this).prop("checked", !state);

		onCheckBoxClick();
	});
	
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
}

// when the DOM is completely loaded, do final preparations by calling
// onPageLoad function
$(document).ready(onPageLoad)