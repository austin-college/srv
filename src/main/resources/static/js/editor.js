/**
 * 
 */

function onSubmit() {
	console.log("hello");
}



/*
 * Updates the current event's JSON contact which
 * holds the current contact id.
 */
function refreshContact(c) {
	var cid = c.contactId;
	
	$.ajax({
		method : "get",
		url : "/srv/ajax/contacts/" + cid + "/html",
		cache : false,
		data: {template:"contact_compact"}
	})
	.done(function(htmltext) {
		// replace HTML view of contact on page
		$("#evContactView").html(htmltext);
		$("#evContactId").val(cid);
	})
	.fail(function(jqXHR, textStatus) {
		// report error on page
		$("#page-error").html("Unable to retrieve contact "+cid);
	});
}


function onPageLoad() {
	startDate = $("#evDate").attr("value");
	console.log(startDate);
	$("#evDate").datetimepicker({
		formatDate : 'Y-m-d',
		startDate : startDate,
		lazyInit : true,
		mask : true,
		formatTime : "h:i a",
		step : 15
	});	

	$("#btnEvEdit").click(function(e) {
		onSubmit(e);
	});
	
	/*
	 * enable page to invoke contact editor dialog
	 * when user clicks on button with id="btnContact".
	 * refreshes contact view on successful return
	 */
	dlgEdit = new ContactManager({
		btn: "#btnContact", 
		task: "select",
		success: refreshContact
		});

	
}


$(document).ready(onPageLoad);