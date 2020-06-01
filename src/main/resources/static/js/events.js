
/*
 * open the dialog to start to create an event.
 */
function onNewClick() {
	$("#dlgNewEvent").dialog( "open" );
}


/*
 * When user clicks on contact, this method opens the modal dialog.
 */
function onContactClick() {
	//alert("contact click event "+$(this).attr("eid"));
	
	$("#dlgViewContact").dialog( "open" );
	
		var idStr = $(this).attr("eid");  // our TD better have an eid attr embedded.
		
		$.ajax({
			method: "get",
	  	    url: "/srv/events/ajax/event/"+idStr+"/contact",
	  	    cache: false
	    })
	    /*
		 * If successful, then remove the selected event row from the table.
		 */
		.done(function(htmltext) {
			//alert("done "+htmltext);
			$("#dlgViewContact").html(htmltext);

		})
		/*
		 * If unsuccessful (invalid data values), display error message and reasoning.
		 */
		.fail(function(jqXHR, textStatus) {
			alert( "Request failed: " + textStatus + " : " + jqXHR.responseText);

		});
		

}

/*
 * launch the action for deleting given the event id.  Present dialog 
 * to confirm.   Then let dialog callbacks do all the work.
 */
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
		method: "POST",
  	    url: "/srv/events/ajax/del/"+idStr,
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

/*
 * Makes the request back to server to create a new event.
 *  
 * @param etid
 * @returns
 */
function ajaxCreateEventNow(etid) {
	
	// The ID of the selected event to be deleted...from the dialog
	$.ajax({
		method: "POST",
  	    url: "/srv/events/ajax/new/"+etid,
  	    cache: false
    })
    /*
	 * If successful, then request browser to move to edit
	 * page on the newly created item.
	 */
	.done(function(eid) {
		var site = location.origin
		var path = site+"/srv/events/edit/"+eid;
		location.assign(path);
	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert( "Request failed: " + textStatus + " : " + jqXHR.responseText);
		$("#dlgNewEvent").dialog( "close" );
	});
	
}



/*
 * cause the client to request the edit page with the specified
 * event id.   Assumes that the button to which this handler is 
 * attached has a eid attribute. 
 */
function onEditClick() {
	var site = location.origin
	var path = site+"/srv/events/edit/"+$(this).attr("eid");
	location.assign(path);
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
	
	// connect the delete action to all delete buttons tagged with btnEvDel class
	$(".btnEvDel").click(onDeleteClick);

	// connect the edit action to all edit buttons 
	$(".btnEvEdit").click(onEditClick);
	
	// connect the view action to all view buttons 
	$(".btnEvView").click(onViewClick);

	// connect the view action to all view buttons 
	$("td.ev_contact").click(onContactClick);


	// Register and hide the delete dialog div until a delete button is clicked on.
	$("#dlgDelete").dialog({
		autoOpen: false,   // hide it at first
		height: 250,
		width: 400,
		position: {
			  my: "center top",
			  at: "center top",
			  of: window
			},
		modal: true,
		dialogClass: "delDlgClass",
		show: { effect: "blind", duration: 500 },
		buttons: [
			{
				text: "DELETE", 
	    			  "class": 'delBtnClass',
	    		click: function() {
	    			ajaxDeleteEventNow(); 
	    		}
			},
		
	        {	
				text: "CANCEL",
	        	      "class": 'cancBtnClass',
	        	click: function() {
	        		$(this).dialog("close");
	        	}
	        }
		]
	});	
	
	
	// setup the create new event dialog....
	$("#dlgNewEvent").dialog({
		autoOpen: false,   // hide it at first
		height: 250,
		width: 400,
		position: {
			  my: "center top",
			  at: "center top",
			  of: window
			},
		modal: true,
		dialogClass: "newDlgClass",
		show: { effect: "blind", duration: 500 },
		buttons: [
			{
				text: "CREATE NEW", 
	    			  "class": 'newBtnClass',
	    		click: function() {
	    			
	    			// harvest user's data
	    			var etype = $("#evType").val();   // what did user choose in dialog?
	    			//alert("create new button "+etype);
	    			
	    			// post to the server
	    			ajaxCreateEventNow(etype);
	    			
	    		}
			},
		
	        {	
				text: "CANCEL",
	        	      "class": 'cancBtnClass',
	        	click: function() {
	        		$(this).dialog("close");
	        	}
	        }
		]
	});
	
	// setup the create new event dialog....
	$("#dlgViewContact").dialog({
		autoOpen: false,   // hide it at first
		height: 400,
		width: 400,
		position: {
			  my: "center top",
			  at: "center top",
			  of: window
			},
		modal: true,
		//dialogClass: "newDlgClass",
		show: { effect: "blind", duration: 300 },
		buttons: [
	        {	
				text: "CANCEL",
	        	      "class": 'cancBtnClass',
	        	click: function() {
	        		$(this).dialog("close");
	        	}
	        }
		]
	});
	
}


// when the DOM is completely loaded, do final preparations by calling onPageLoad function
$(document).ready(onPageLoad);
