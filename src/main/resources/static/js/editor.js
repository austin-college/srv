/**
 * 
 */

function onSubmit() {
	console.log("hello");
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
}

$(document).ready(onPageLoad);