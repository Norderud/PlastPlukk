
var mymap = L.map('mapid').setView([59.41, 9.06], 13);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	maxZoom: 18,
	attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
	'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
	'Imagery © <a href="http://mapbox.com">Mapbox</a>',
	id: 'mapbox.streets'
}).addTo(mymap);

//Deklarer stedTabell som henter data fra databasen
var stedTabell = [];
oppstart();

//Henter data
function oppstart(){
	method = "GET",
	url = "getMarker.php";
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		if(this.readyState == 4 && this.status == 200) {
			eval(xhr.responseText);
			tegnMarkers();
		}
	}
	xhr.open(method, url, true);
	xhr.send();
}
function tegnMarkers(){
	for (var i = 0; i < stedTabell.length; i++){
		var marker = L.marker([stedTabell[i].lat, stedTabell[i].lng])
		marker.addTo(mymap);
	}
}
