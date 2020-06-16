// launch the action for viewing details given the event id
function onViewClick() {
	var site = location.origin
	var path = site + "/srv/events/view/" + $(this).attr("eid");
	location.assign(path);
}

// pop up to warn that the user can't rsvp/sign up for an event yet
function onSignUpClick() {
	alert("Feature is not functional yet");

}

// pop up to warn that the user can't edit their profile yet
function onEditProfileClick() {
	alert("Feature is not functional yet");
}

//Final preparations once the page is loaded. attach
//functions to buttons.
function onPageLoad() {
	
	// connect the view action to links in the list group
	$(".btnEvView").click(onViewClick);
}

//when the DOM is completely loaded, do final preparations by calling
//onPageLoad function
$(document).ready(onPageLoad);