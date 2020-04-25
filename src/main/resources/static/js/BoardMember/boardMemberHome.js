$(document).ready(function () {
	
$('#dtDynamicVerticalScroll').DataTable({
	"paging": false,
	"searching": false,"info": false,
"scrollY": "50vh",
"scrollCollapse": true
});
$('.dataTables_length').addClass('bs-select');

});