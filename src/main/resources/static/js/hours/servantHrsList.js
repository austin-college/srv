function prepopulateAddDialogue(){
	
	//step1 which event did they select
	
	var eid= $("#newEvId").val();
	
	// step2 retrieve event info
	$.ajax({
		method: "GET",
  	    url: "/srv/events/ajax/event/"+ eid,
  	    cache: false
  	    
    })
    /*
	 * If successful, then remove the selected service hour from the table.
	 */
	.done(function(ev) {
		$("#txtEvTitle").val(ev); //change name to ev.title to get events title field
		//ToDo finish populating all other fields
	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert( "Request failed: " + textStatus + " : " + jqXHR.responseText);	
	});
}
/**
 * The delete hour function makes an AJAX call to remove
 * the selected service hour from the table.
 * 
 * @param hours_id - to be deleted/removed service hour ID
 * @returns
 */
function delHr(hours_id){
	// The ID of the selected service hour to be deleted
	var idStr = hours_id;

	$.ajax({
		method: "POST",
  	    url: "/srv/ajax/delHour",
  	    cache: false,
  	    data: {ID: idStr}
    })
    /*
	 * If successful, then remove the selected service hour from the table.
	 */
	.done(function(data) {
		$("#row" + hours_id).remove();
	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert( "Request failed: " + textStatus + " : " + jqXHR.responseText);	
	});
}
/**
 * The edit service hour function makes an AJAX call to update
 * the selected service hour to the table from the input fields of the
 * edit dialog. 
 * 
 * Note: Can not change the ID of the service hour.
 * 
 * @returns
 */
function editHr(hours_id, eName, date, org, hrs, desc, address, city, zip, st, conName, conNumber, conEmail) {
	
	// Harvests the data values from the form
	var idStr = hours_id;
	var eNameStr  = $(eName).val();
	var dateStr  = $(date).val();
	var orgStr = $(org).val();
	var hrsStr = $(hrs).val();
	var descStr = $(desc).val();
	var addressStr = $(address).val();
	var cityStr = $(city).val();
	var zipStr = $(zip).val();
	var stStr = $(st).val();
	var conNameStr = $(conName).val();
	var conNumberStr = $(conNumber).val();
	var conEmailStr = $(conEmail).val();

	$.ajax({
		method: "POST",
		url: "/srv/ajax/editHour",
		cache: false,
		//data: {ID: idStr, eventName: eNameStr, org: orgStr, hrsServed: hrsStr}
	    data: {ID: idStr, eventName: eNameStr, hrDate: dateStr, org: orgStr, hrsServed: hrsStr,
			   desc: descStr, address: addressStr, city: cityStr, zipCode: zipStr, state: stStr,
			   contactName: conNameStr, contactNumber: conNumberStr, contactEmail: conEmailStr}
	})
	/*
	 * If successful (no invalid data values), then update the selected service hour 
	 * with the new values.
	 */
	.done(function(data) {
		$("#row" + hours_id + " td[name ='hrs_eventName']").html($(eName).val());
		$("#row" + hours_id + " td[name ='hrs_hrsServed']").html($(hrs).val());

	})
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Request failed: " + textStatus + " : "+jqXHR.responseText);
	});
}
/* When the DOM is completed loaded and ready, hide the dialogs and
 * create the functionality of the buttons.
 */
$(document).ready(function() {	
	
	// Register and hide the delete dialog div until a delete button is clicked on.
	$("#delDlg").dialog({
		autoOpen: false,
		width: $(window).width() * 0.5,
		height: $(window).height() * 0.5,
		position: {
			  my: "center top",
			  at: "center top",
			  of: window //"#srv-page"
			},
		show: { effect: "blind", duration: 500 },
		modal: true,
		dialogClass: "delDlgClass",
		create: function(event, ui) { 
			$(".delBtnClass").addClass("btn btn-danger");
			$(".cancBtnClass").addClass("btn btn-secondary");
		},
		open: function(event, ui) {
			/*
			 * Prompt on the delete service hour dialog, verifying if they want to delete the selected service hour.
			*/ 
			$("#delMsg1").html("The following service hours will be permanently deleted and cannot be recovered.");
			$("#delMsg2").html("Are you sure you want to delete?");
		},							
		buttons: [
		{
			text: "DELETE", 
    			  "class": 'delBtnClass',
    		click: function() {
    			delHr($("#delDlg").data('selectedHoursID'));
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
	
	// Register and hide the service hour information dialog div until a row is clicked on
	$("#hrInfoDlg").dialog({
		autoOpen: false,
		 width: $(window).width() * 0.8,
		 height: $(window).height() * 0.9,
		modal: true,
		position: {
			  my: "center top",
			  at: "center top",
			  of: window
			},
		show: { effect: "blind", duration: 500 },
		open: function(event, ui) {
			
			// Harvests the selected service hour's values from the table.
			var selected_shid = $("#hrInfoDlg").data('selectedHoursID');
			var set_eventName =  $("#row" + selected_shid + " td[name = 'hrs_eventName']").text();
			var set_hrs = $("#row" + selected_shid + " td[name = 'hrs_hrsServed']").text();
			
			$("#hrInfoDlg_eventName").val(set_eventName);
			$("#hrInfoDlg_hrsServed").val(set_hrs);
		}
		
	});
	
	
	// Register and hide the edit dialog div until an edit button is clicked on.
	 $("#editDlg").dialog({
		 autoOpen: false,
		 width: $(window).width() * 0.8,
		 height: $(window).height() * 0.8,
         modal: true,
	 		position: {
				  my: "center top",
				  at: "center top",
				  of: window
				},
		 show: { effect: "blind", duration: 500 },				
	     open: function(event, ui) {  
	    	  
	    	 // Harvests the selected service hour's old values from the table.
	    	 var selected_shid = $("#editDlg").data('selectedHoursID');
	    	 var set_eventName = $("#row" + selected_shid + " td[name ='hrs_eventName']").text();
	    	 var set_hrs = $("#row" + selected_shid + " td[name ='hrs_hrsServed']").text();
	  
	    	// Sets the dialog's fields to the selected service hour's old values upon opening.
	    	 $("#editDlg_eventName").val(set_eventName);
	    	 $("#editDlg_hrsServed").val(set_hrs);
	      },
	      buttons: [
	      {
	    	  text: "Update",
	    		   "class": "editBtnClass",
	    	  click: function() {
	    			  
	           var selected_shid = $("#editDlg").data('selectedHoursID'); // The selected service hour's ID
	           
	           editHr(selected_shid, "#editDlg_eventName", "#editDlg_date", "#editDlg_org", "#editDlg_hrsServed", "#editDlg_desc",
	        		   "#editDlg_address", '#editDlg_city', '#editDlg_zip', '#editDlg_state', '#editDlg_contactName', '#editDlg_contactNumber',
	        		   '#editDlg_contactEmail');
		    	        	     
		    	$("#editDlg").dialog("close");
	    	  }	
	      },
	      {
	    		text: "Cancel",
	    		      "class": "cancelBtnClass",
	    		click: function() {	
	    			$("#editDlg").dialog("close");  
	    		}
	    	
	     }]
	  });
	 
	 $("#addDlg").dialog({
			autoOpen: false,
			height: 500,
			width: 700,
			modal: true,
			open: function(event, ui) {			
				console.log("populating dialogue");	//replace with a javascript function that will put data into the add dialogue
				prepopulateAddDialogue();
			}
		});
	 $("#etDlg").dialog({
		 autoOpen: false,
		 width: $(window).width() * 0.6,
		 height: $(window).height() * 0.7,
		 modal: true,
		 position: {
			  my: "center top",
			  at: "center top",
			  of: window
			},
		 open: function(event, ui) {			
			 console.log("boop");	
		 },
		 buttons: [
			 {
				 text: "Submit", 
				 "id": "addBtnDlg",
				 "class": 'btn',
				 click: function() {		
					 console.log("FIE");
					
					 $("#etDlg").dialog("close");
					 $("#addDlg").dialog("open");

				 }
			 },
			 {	
				 text: "Cancel",
				 "class": 'btn btn-secondary',
				 click: function() {
					 $("#etDlg").dialog("close");

				 }
			 }]
	 });

	 // dialog for selecting the events when creating new.
	 $("#dlgEvSel").dialog({
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
	 
	 
	 
	/* 
     * Opens delete service hour dialog and passes in the selected delete button's service hour's id
     * when a user clicks a delete button.
     */
    $(".del").on("click", function() {
    	var selected_shid = $(this).attr('onDelClick');
    	$("#delDlg").data("selectedHoursID", selected_shid).dialog("open");
    });
    
    /*
     * Opens a service hour dialog and passes in the selected row's service hour's id when a user
     * clicks on a row in the service hours table.
     */
    $(".hrRow").on("click", function() {
    	var selected_shid = $(this).attr('onRowClick');
    	$("#hrInfoDlg").data("selectedHoursID", selected_shid).dialog("open");
    });
    
    /* 
     * Opens edit service hour dialog and passes in the selected edit button's service hour's id
     * when user clicks an edit button.
     */    
    $(".edit").on("click", function() {
    	var selected_shid = $(this).attr('onEditClick');    	
    	$("#editDlg").data("selectedHoursID", selected_shid).dialog("open");
    });   
    
    $(".addBtn").on("click", function() {
    	console.log("HERE");
    	//$("#etDlg").dialog("open");
    	
    	$("#dlgEvSel").dialog("open");
    });
    
    
	$('#tblEvents').DataTable({	
		"paging": false,
		"searching": true,
		"info": false
	});
	
	
	$('#tblEvents').on( 'search.dt', function () {
		$(".boxSel").prop("checked",false);  // clear all others
	} );
	
	
	$(".boxSel").click( function() {
		
		var state = $(this).prop("checked");
		
		if (state) {
			var eid = $(this).attr("eid");
			$("#newEvId").val(eid);
		}
		
		$(".boxSel").prop("checked",false);  // clear all others		
		$(this).prop("checked",state);  // reassert current state on current button  
	});
	
});