// alert the user that this feature is dead
function onNewClick() {
	alert("Feature is not functional yet");
}

//alert the user that this feature is dead
function onEditClick() {
	alert("Feature is not functional yet");
}

//alert the user that this feature is dead
function onDeleteClick() {
	alert("Feature is not functional yet");
}

//alert the user that this feature is dead
function onViewClick() {
	alert("Feature is not functional yet");
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
}


// when the DOM is completely loaded, do final preparations by calling
// onPageLoad function
$(document).ready(onPageLoad)