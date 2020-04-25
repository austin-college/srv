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

/* When the DOM is completed loaded and ready, hide the dialogs and
 * create the functionality of the buttons.
 */
$(document).ready(function() {	
	 
	$("#hrInfoDlg").dialog({
		autoOpen: false,
		height: 500,
		width: 1100,
		modal: true,
		open: function(event, ui) {
			
			// Harvests the selected service hour's values from the table.
			var selected_shid = $("#hrInfoDlg").data('selectedHoursID');
			var set_eventName =  $("#row" + selected_shid + " td[name = 'hrs_eventName']").text();
			var set_hrs = $("#row" + selected_shid + " td[name = 'hrs_hrsServed']").text();
			
			$("#hrInfoDlg_eventName").val(set_eventName);
			$("#hrInfoDlg_hrsServed").val(set_hrs);
		}
		
	});
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
	
	/* 
     * Opens delete service hour dialog and passes in the selected delete button's service hour's id
     * when a user clicks a delete button.
     */
    $(".del").on("click", function() {
    	var selected_shid = $(this).attr('onDelClick');
    	$("#delDlg").data("selectedHoursID", selected_shid).dialog("open");
    });
    
    $(".hrRow").on("click", function() {
    	var selected_shid = $(this).attr('onRowClick');
    	$("#hrInfoDlg").data("selectedHoursID", selected_shid).dialog("open");
    });
});