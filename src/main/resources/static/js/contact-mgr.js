function fillContactFields(ct, pre) {
	
	var fullAddr = ct.street?ct.street:"?" + ", " + 
			ct.city?ct.city:"?" + ", " + 
			ct.state?ct.state:"?" + " " + 
			ct.zipcode?ct.zipcode:"";
	
	$("#ct"+pre+"-cid").html(ct.contactId);
	

	$("#ct"+pre+"-fname").html(ct.firstName?ct.firstName:"");

	$("#ct"+pre+"-lname").html(ct.lastName?ct.lastName:"");
	
	$("#ct"+pre+"-email").html(ct.email?ct.email:"");
	$("#ct"+pre+"-phone1").html(ct.primaryPhone?ct.primaryPhone:"");
	$("#ct"+pre+"-phone2").html(ct.secondaryPhone?ct.secondaryPhone:"");
	
	$("#ct"+pre+"-addr").html(fullAddr);
}

function ContactManager(options) {

	if (options == null) 
		options = {
			btn: "#btnContact", 
			task: "edit",
			dlgdiv: "#dlgEditContact"
			}
	
	if (options.btn == null) {
		options.btn = "#btnContact";  
	}
	
	if (options.task == null) {
		options.task = "edit";
	}
	
	if (options.dlgdiv == null) {
		options.dlgdiv = "#dlgEditContact";
	}

	
	/*
	 * Make sure the specified element (btn) is hooked up to 
	 * populate and open the dialog.  Based on options, we 
	 * configure for selecting one of many or for editing one.
	 * 
	 * We assume the anchor element (button) has a cid attribute.
	 */
	$(options.btn).click(function(e) {
		var cid = $(this).attr("cid");
		
		options.contactId = cid;
		
		var urlStr = "/srv/ajax/contacts";
		var templateStr = "contact_selection_dialog";
			
		if (options.task == "edit") {
			urlStr = "/srv/ajax/contacts/" + cid + "/html";
			templateStr = "contact_dialog"
		} 
		
		$.ajax({
			method : "get",
			url : urlStr,
			cache : false,
			data: {template: templateStr }
		})
		.done(function(htmltext) {

			$(options.dlgdiv).html(htmltext);
			$(options.dlgdiv).dialog("open");	
			
			
		})
		.fail(function(jqXHR, textStatus) {
			//alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
			$(options.dlgdiv).html("Unable to retrieve contact "+cid);
			$(options.dlgdiv).dialog("open");	
		});
		
		
	});
	
	
	/*
	 * Define the initialization function...to at least append a container
	 * div for the dialog on the current page (if absent).  If user provides
	 * and additional init function, call it also...for extra custom initialization.
	 */
	this.init = function() {
		
		/*
		 * If page does not contain a div for the 
		 * dialog, we'll make one.
		 */
		if ($(options.dlgdiv).length == 0) {
			$(document.body).append(
			'<div id="dlgEditContact" title="..." class="ui-widget" style="display: none"> </div>'
			);
		}
		
		
		if (options.init != null) {
			options.init();
		}
	}
	
	// call init function before we establish the dialog
	this.init();
	
	/*
	 * Defines the dialog for editing/updating a specified contact. 
	 */
	this.buildEditDialog = function() {

		return $(options.dlgdiv).dialog({
			autoOpen : false, // hide it at first
			width: $(document).width() * 0.7,
			height: $(document).height() * 0.3,
			position : {
				my : "center top",
				at : "center top",
				of : window
			},
			modal : true,
			// dialogClass: "newDlgClass",
			show : {
				effect : "blind",
				duration : 300
			},
			open: function(event, ui) {
				
				console.log("open edit dialog");
				$("button.ui-dialog-titlebar-close").hide();
				$("#dlgContact-error").hide();
				$(".ui-dialog-title").html("EDIT CONTACT");
				
				// make sure all of the fields can be edited.
				$(".form-control").attr("readonly", false); 
				
			},
			// button include submit and cancel
			buttons : [
				{
					text : "SUBMIT",
					"class" : 'submitBtnClass',
					click : function() {

						/*
						 * When submitting....gather up the form data.
						 */
						$("#dlgContact-error").hide();
						var cid = $("#evContact_id").val().trim();
						var fname = $("#evContact_firstName").val().trim();
						var lname = $("#evContact_lastName").val().trim();
						var pphone = $("#evContact_primaryPhone").val().trim();
						var sphone = $("#evContact_secondaryPhone").val().trim();
						var email = $("#evContact_email").val().trim();
						var street = $("#evContact_street").val().trim();
						var city = $("#evContact_city").val().trim();
						var state = $("#evContact_state").val().trim();
						var zip = $("#evContact_zipcode").val().trim();
						
						/*
						 * validate:  user must provide first or last name along
						 * with email or phone.
						 */
						if ((fname+lname).length == 0) {
							$("#dlgContact-error").html("first or last name required");
							$("#dlgContact-error").show();
							return;
						}
						
						if ((pphone + sphone + email).length == 0) {
							$("#dlgContact-error").html("email or phone required");
							$("#dlgContact-error").show();
							return;
						}
						
						
						/*
						 * post update to server
						 */
						$.ajax({
							method : "post",
							url : "/srv/ajax/contacts/" + cid,
							cache : false,
							data: {
								"fname": fname,
								"lname": lname,
								"pphone": pphone,
								"sphone": sphone,
								"email": email,
								"street": street,
								"city": city,
								"state": state,
								"zip": zip
							}
						})
						.done(function(ct) {
							/*
							 * When update is successful, call the specified 
							 * "after" success function provided by the programmer
							 * via options.
							 */
							$(options.dlgdiv).dialog("close");
							
							if (options.success != null)
								options.success(ct);
														
						})
						.fail(function(jqXHR, textStatus) {
							//alert("Request failed: " + textStatus + " : " + jqXHR.responseText);
							$(options.dlgdiv).html("Unable to retrieve contact "+cid);
						});
						
					}
				},
				
				{
				text : "CANCEL",
				"class" : 'cancBtnClass',
				click : function() {
					$(this).dialog("close");
					}
				} 
				]
		});
	};
	
	
	/*
	 * Defines the dialog for selecting/choosing an existing contact. 
	 */
	this.buildSelectDialog = function() {

		return $(options.dlgdiv).dialog({
			autoOpen : false, // hide it at first
			width: $(document).width() * 0.7,
			height: $(document).height() * 0.3,
			position : {
				my : "center top",
				at : "center top",
				of : window
			},
			modal : true,
			// dialogClass: "newDlgClass",
			show : {
				effect : "blind",
				duration : 300
			},
			open: function(event, ui) {
				
				console.log("open select dialog");	
				
				$("button.ui-dialog-titlebar-close").hide();
				$("#dlgContact-error").hide();
				$(".ui-dialog-title").html("SELECT CONTACT");
				
				// start will all deselected.
				$('input[class=boxSel]').prop("checked", false);
				
				
				$('input[class=boxSel]').on("change", function() {
					console.log("boxsel click");
					var state = $(this).prop("checked");
					var cid = $(this).attr("cid");
					
					options.contactId = cid;
					
					if (state) {
						console.log(cid+" is selected");
					} else {
						console.log(cid+" is deselected");
					}

					$(".boxSel").prop("checked",false);  // clear all others
					
					$(this).prop("checked",state);  // reassert current state on current button  
				});
				
				console.log("foo");
			},
			buttons : [
				{
					text : "SELECT",
					"class" : 'submitBtnClass',
					click : function() {
						console.log("select button");

						$("#dlgContact-error").hide();
						
						// count the number of checked boxes
						var contactsChecked = $('input[class=boxSel]:checked');
						
						// verify a checkbox was selected, cannot submit until one is, 
						// throw error to user stating so
						if (contactsChecked.length  == 1){
							console.log("a checkbox is checked.");
							
							var cid = contactsChecked[0].getAttribute("cid");
							console.log("Contact ["+cid+"] has been selected. ");
							
							$.ajax({
								method : "get",
								url : "/srv/ajax/contacts/" + cid + "/json",
								cache : false
							})
							.done(function(ct) {
								
								console.log(ct);
								
								if (options.success != null)
									options.success(ct);

								$(options.dlgdiv).dialog("close");
							})
							.fail(function(jqXHR, textStatus) {
								// report error on page
								$("#dlgContact-error").html("Unable to retrieve selected contact ");
							});
							
						}
						else if(contactsChecked.length == 0){
							console.log("all checkboxes are unchecked.");
							$("#dlgContact-error").html("Please select a contact. ");
							$("#dlgContact-error").show();

						} else {
							console.log("many checkboxes are checked.");
							$("#dlgContact-error").html("Only 1 contact should be selected. ");
							$("#dlgContact-error").show();
							
						}
						
					}
				},
				
				{
				text : "CANCEL",
				"class" : 'cancBtnClass',
				click : function() {
					$(this).dialog("close");
					}
				} 
				]
		});
	};
	
	
	/*
	 * Which dialog should we use?  Depends on the 
	 * options provided.  Either way, we fill the
	 * same dlgdiv container.
	 */
	if (options.task == 'edit') 
		this.dlg = this.buildEditDialog()
	else 
		this.dlg = this.buildSelectDialog();
	
}



