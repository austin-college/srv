$(document).ready(function () {
//Vertical Scroll table
$('#dtDynamicVerticalScroll').DataTable({
	"paging": false,
	"searching": false,"info": false,
"scrollY": "50vh",
"scrollCollapse": true
});
$('.dataTables_length').addClass('bs-select');

});