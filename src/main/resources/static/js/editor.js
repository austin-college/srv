/**
 * 
 */

function onSubmit(e) {
	//	e.preventDefault();
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
	
	$('#evContinuous').click(function(e){
		if (e.target.checked) {
	  	localStorage.checked = true;
	  } else {
	  	localStorage.checked = false;
	  }
	})
	//	$(function(){
	//		$("#evTitle").on('change', function(){
	//			console.log("hello");
	//		})
	//	})

	$("#btnEvEdit").click(function(e) {
		onSubmit(e);
	});
}

$(document).ready(onPageLoad);