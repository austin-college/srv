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
			  * Prompt on the delete committee dialog, specifying the name of the selected committee to the user,
			  * verifying if they want to delete the selected committee.
			  */ 
			 $("#delMsg1").html("The following organization will be permanently deleted and cannot be recovered.");
			 $("#delMsg2").html("Are you sure you want to delete?");
      },							
      buttons: [
    	  {
    		  text: "DELETE COMMITTEE", 
    		  "class": 'delBtnClass',
    		   click: function() {
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
	 
	 

		//Register and hide the add dialog div until an add button is clicked on.
		 $("#addDlg").dialog({	
		    autoOpen: false,
		    width: 350,
		    modal: true,
		    show: 'slide',
		    hide: 'blind',
		    open: function(event, ui){
		    	
		    	/*
		    	 * Resets all the fields of the add dialog to empty.
		    	 */
		    	$("#addDlg_commID").val("");
		    	$("#addDlg_commTitle").val("");
		    	$("#addDlg_commNumber").val("");
		    	$("#addDlg_commMember").val("");

		    	
		     },
		    buttons: [
		      {
		    	  text: "Create New Committee",
		    	  "class": 'addBtnClass',
		    	   click: function() {
		    		   $("#addDlg").dialog("close");	
		    		  
		    	  }
		      },
		      {
		        	text: "Cancel",
		        	"class": 'cancelBtnClass',
		        	click: function() { 	
		        		$("#addDlg").dialog("close");	
		        	}
		      }]
		 });	 

  
    /* 
     * Opens delete organization dialog when a delete button is clicked on.
     */
    $(".del").on("click", function() {
    	$("#delDlg").dialog("open");
    });
    
 	/* 
  	 * Opens add committee dialog when user clicks an add button.
  	 */
  	$(".addBtn").on("click", function() {
  		$("#addDlg").dialog("open");
  	});
 
});