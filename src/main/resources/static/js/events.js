
// open the dialog or launch the action related to 
// creating a new event
function onNewClick() {
	alert("new event");
}

//launch the action for deleting given the event id.  Present dialog
//to confirm.   Then let dialog callbacks do all the work.
function onDeleteClick() {
	
	var eid = $(this).attr("eid");  // assume eid attr exists on button.
	var row_title = $("#eid-"+eid+" td.ev_title" ).html();  // value of title cell in row
	var row_date = $("#eid-"+eid+" td.ev_date" ).html();  // value of date cell in row
	//alert("delete event "+row_title+ " on "+row_date);
	
	// fill in the dialog with data from the current row event.
	$("#delEvId").html(eid);
	$("#delEvTitle").html(row_title);
	$("#delEvDate").html(row_date);

	// open the dialog...let it take over 
	$("#dlgDelete").dialog( "open" );
}

/*
 * makes the request back to our server to delete the event whose 
 * id we extract from the confirmation dialog
 */
function ajaxDeleteEventNow() {
	
	// The ID of the selected event to be deleted...from the dialog
	var idStr = $("#delEvId").html();
	
	// alert(idStr);
	
	$.ajax({
		method: "DELETE",
  	    url: "/srv/events/"+idStr,
  	    cache: false
    })
    /*
	 * If successful, then remove the selected event row from the table.
	 */
	.done(function(eid) {
		//alert("done "+eid);
		$("#eid-"+eid).remove();  // remove row from table.  
		$("#dlgDelete").dialog( "close" );
	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert( "Request failed: " + textStatus + " : " + jqXHR.responseText);
		$("#dlgDelete").dialog( "close" );
	});
	
}


// open the dialog or launch the action related to
// editing the current row/event
function onEditClick() {
	alert("edit event "+$(this).attr("eid"));
	// TODO use javascript 
}

//launch the action for viewing details given the event id
function onViewClick() {
	alert("view event "+$(this).attr("eid"));
	// TODO  use jquery to open event details viewing dialog
}


// Final preparations once the page is loaded.  we hide stuff and attach functions to buttons. 
function onPageLoad() {
	
	// connection action to unique create button
	$("#btnEvNew").click(onNewClick);
	
	// connect the delete action to all delete buttons 
	$(".btnEvDel").click(onDeleteClick);

	// connect the edit action to all edit buttons 
	$(".btnEvEdit").click(onEditClick);
	
	// connect the view action to all view buttons 
	$(".btnEvView").click(onViewClick);


	// Register and hide the delete dialog div until a delete button is clicked on.
	$("#dlgDelete").dialog({
		autoOpen: false,   // hide it at first
		height: 250,
		width: 400,
		position: {
			  my: "center top",
			  at: "center top",
			  of: "#srv-page"
			},
		modal: true,
		dialogClass: "delDlgClass",
		show: { effect: "blind", duration: 500 },
		buttons: [
			{
				text: "DELETE", 
	    			  "class": 'delBtnClass',
	    		click: function() {
	    			// alert("now really delete it");
	    			// make ajax call...let ajax callback refresh DOM
	    			ajaxDeleteEventNow();
	    		}
			},
		
	        {	
				text: "CANCEL",
	        	      "class": 'cancBtnClass',
	        	click: function() {
	        		$("#dlgDelete").dialog("close");
	        	}
	        }
		]
	});	
	
	
}


// when the DOM is completely loaded, do final preparations by calling onPageLoad function
$(document).ready(onPageLoad);
