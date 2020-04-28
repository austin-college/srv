/** 
 * The delete client function makes an AJAX call to remove the selected
 * service client from the table
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
		data: {ID: idStr}
	})
	/*
	 * If successful, then remove the selected service client from the table.
	 */
	.done(function(data){
		$(client_id).remove();
	}) 
	/*
	 * If unsuccessful (invalid data values), display error message and reasoning.
	 */
	.fail(function(jqXHR, textStatus) {
		alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
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

	
    /* 
     * Opens delete service client dialog and passes in the selected delete button's 
     * service client's id when a delete button is clicked on
     */
    $(".del").on("click", function() {
    	var selected_scid = $(this).attr('onDelClick');
    	$("#delDlg").data("selectedClientID", selected_scid).dialog("open");
    });
    
 
});