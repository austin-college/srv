// launch the action for viewing details given the event id
function onViewClick() {
	var site = location.origin
	var path = site + "/srv/events/view/" + $(this).attr("eid");
	location.assign(path);
}

//Final preparations once the page is loaded. we hide stuff and attach
//functions to buttons.
function onPageLoad() {
	
	// connect the view action to links in the list group
	$(".btnEvView").click(onViewClick);
}

//when the DOM is completely loaded, do final preparations by calling
//onPageLoad function
$(document).ready(onPageLoad);