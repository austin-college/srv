/**
 * open the dialog to start to create an event type.
 * 
 * @returns
 */
function onNewClick() {
	$("#dlgScSel").dialog("open")
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
	
	 // dialog for selecting the service client when creating new.
	 $("#dlgScSel").dialog({
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
		 },
		 buttons: [
			 {
				 text: "Submit", 
				 "id": "addBtnDlg",
				 "class": 'btn',
				 click: function() {		
					 console.log("submit on select dialog");
					
					 
					 $("#addDlg").dialog("open");
					 
					 $(this).dialog("close");

				 }
			 },
			 {	
				 text: "Cancel",
				 "class": 'btn btn-secondary',
				 click: function() {
					 console.log("cancel on select dialog");
					 $(this).dialog("close");

				 }
			 }]
	 });
	 
	 // Add event type dialog
	 $("#addDlg").dialog({
			autoOpen: false,
			height: 525,
			width: 700,
			modal: true,
			open: function(event, ui) {			
				console.log("open add dialog");	
			},
			 buttons: [
				 {
					 text: "Submit", 
					 "id": "addBtnDlg",
					 "class": 'btn',
					 click: function() {		
						 console.log("submit on select dialog");
						
						 
						 $("#addDlg").dialog("open");
						 
						 $(this).dialog("close");

					 }
				 },
				 {	
					 text: "Cancel",
					 "class": 'btn btn-secondary',
					 click: function() {
						 console.log("cancel on select dialog");
						 $(this).dialog("close");

					 }
				 }]
		});
	 
	 // Allows for sorting service client table in dialog
	 $('#tblSrvClients').DataTable({	
		 "paging": false,
		 "searching": true,
		 "info": false
	 });

	 // Allows for searching the service client table in dialog
	 $('#tblSrvClients').on( 'search.dt', function () {
		 $(".boxSel").prop("checked",false);  // clear all others
	 } );
}


// when the DOM is completely loaded, do final preparations by calling
// onPageLoad function
$(document).ready(onPageLoad)