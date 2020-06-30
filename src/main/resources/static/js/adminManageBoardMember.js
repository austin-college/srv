//TODO alert user feature not functional yet
function onNewClick() {
	alert("Create New feature is not functional yet.");
}

//TODO alert user feature not functional yet
function onDeleteClick() {
	alert("Delete feature is not functional yet.");
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
}

// when the DOM is completely loaded, do final preparations by calling
// onPageLoad function
$(document).ready(onPageLoad)