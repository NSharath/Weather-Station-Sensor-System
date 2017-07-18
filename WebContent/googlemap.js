

function initializeMaps() {
    var latlng = new google.maps.LatLng(37.3382, -121.8863);
    var myOptions = {
        zoom: 12,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        mapTypeControl: false
    };
    var map = new google.maps.Map(document.getElementById("googleMap"),myOptions);

//    var infowindow = new google.maps.InfoWindow(), marker, i;
//    for (i = 0; i < markers.length; i++) {  
//        marker = new google.maps.Marker({
//            position: new google.maps.LatLng(markers[i][1], markers[i][2]),
//            map: map
//        });
//        google.maps.event.addListener(marker, 'click', (function(marker, i) {
//            return function() {
//                infowindow.setContent(markers[i][0]);
//                infowindow.open(map, marker);
//            }
//        })(marker, i));
//    }
}
google.maps.event.addDomListener(window, 'load', initializeMaps);

function reloadMaps() {
	console.log(document.getElementById('locality').value);
	var res = loadDoc();
	var markers = myFunction(res);
//	var markers = [
//	               ['Bondi Beach', -33.890542, 151.274856],
//	               ['Coogee Beach', -33.923036, 151.259052],
//	               ['Cronulla Beach', -34.028249, 151.157507],
//	               ['Manly Beach', -33.80010128657071, 151.28747820854187],
//	               ['Maroubra Beach', -33.950198, 151.259302]
//	           ];

    var latlng = new google.maps.LatLng(-33.92, 151.25);
    var myOptions = {
        zoom: 12,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        mapTypeControl: false
    };
    var map = new google.maps.Map(document.getElementById("googleMap"),myOptions);
    var infowindow = new google.maps.InfoWindow(), marker, i;
    for (i = 0; i < markers.length; i++) {  
        marker = new google.maps.Marker({
            position: new google.maps.LatLng(markers[i][1], markers[i][2]),
            map: map
        });
        google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                infowindow.setContent(markers[i][0]);
                infowindow.open(map, marker);
            }
        })(marker, i));
    }
}

function reloadCityMaps() {
	//console.log(document.getElementById('locality').value);
	//console.log(document.getElementById('administrative_area_level_1').value);
	var city = document.getElementById('locality').value;
	city = city.replace(/\s/g, '_');
	console.log(city);
	var state = document.getElementById('administrative_area_level_1').value
	var url = "/MobileSensorCloudEngine/api/data/geolookup/city/" + state +"/"+city;
	console.log(url);
	loadDoc(url);
	
}
function loadDoc(url){
	var xmlhttp = new XMLHttpRequest();
	//var url = "http://localhost:8080/MobileSensorCloud/sensor/data/geolookup/city/CA/San_Jose";

	xmlhttp.onreadystatechange = function() {
	    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
	        var arr = JSON.parse(xmlhttp.responseText);
	        console.log(arr);
	        console.log(arr.nearby_weather_stations.pws.station.length)
	    
	        myFunction(arr.nearby_weather_stations.pws.station);
	        //return myArr;
	    }
	};
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function myFunction(arr) {
	//var out = "";
	var i;
	var markers = new Array();
	for(i = 0; i < arr.length; i++) {
		//out += '<a href="' + arr[i].url + '">' + 
		//arr[i].display + '</a><br>';
		var station = new Array();
		station.push(arr[i].id);
		station.push(arr[i].lat);
		station.push(arr[i].lon);
		console.log(station);
		markers.push(station);
		
	}
	//return markers
	console.log(markers)
	//document.getElementById("id01").innerHTML = out;
	var latlng = new google.maps.LatLng(37.3382, -121.8863);
	var bounds = new google.maps.LatLngBounds();
    var myOptions = {
        zoom: 12,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        mapTypeControl: false
    };
    var map = new google.maps.Map(document.getElementById("googleMap"),myOptions);
    var infowindow = new google.maps.InfoWindow(), marker, i;
    for (i = 0; i < markers.length; i++) {  
        marker = new google.maps.Marker({
            position: new google.maps.LatLng(markers[i][1], markers[i][2]),
            map: map
        });
        bounds.extend(marker.position);
        google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                infowindow.setContent(markers[i][0]);
                infowindow.open(map, marker);
                document.getElementById("lat").value = markers[i][1];
                document.getElementById("lon").value = markers[i][2];
                document.getElementById("station_id").value = markers[i][0];
            }
        })(marker, i));
    }
    map.fitBounds(bounds);
}



//This example displays an address form, using the autocomplete feature
// of the Google Places API to help users fill in the information.

// This example requires the Places library. Include the libraries=places
// parameter when you first load the API. For example:
// <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places">

var placeSearch, autocomplete;
var componentForm = {
  //street_number: 'short_name',
  //route: 'long_name',
  locality: 'long_name',
  administrative_area_level_1: 'short_name',
  country: 'long_name',
  postal_code: 'short_name'
};

function initAutocomplete() {
  // Create the autocomplete object, restricting the search to geographical
  // location types.
  autocomplete = new google.maps.places.Autocomplete(
      /** @type {!HTMLInputElement} */(document.getElementById('autocomplete')),
      {types: ['geocode']});

  // When the user selects an address from the dropdown, populate the address
  // fields in the form.
  autocomplete.addListener('place_changed', fillInAddress);
}

function fillInAddress() {
  // Get the place details from the autocomplete object.
  var place = autocomplete.getPlace();

  for (var component in componentForm) {
	 console.log(component);
    document.getElementById(component).value = '';
    document.getElementById(component).disabled = false;
  }

  // Get each component of the address from the place details
  // and fill the corresponding field on the form.
  for (var i = 0; i < place.address_components.length; i++) {
    var addressType = place.address_components[i].types[0];
    if (componentForm[addressType]) {
      var val = place.address_components[i][componentForm[addressType]];
      document.getElementById(addressType).value = val;
    }
  }
}

// Bias the autocomplete object to the user's geographical location,
// as supplied by the browser's 'navigator.geolocation' object.
function geolocate() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
      var geolocation = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };
      var circle = new google.maps.Circle({
        center: geolocation,
        radius: position.coords.accuracy
      });
      autocomplete.setBounds(circle.getBounds());
    });
  }
}


function createCookie(name,value,days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}

function eraseCookie(name) {
	createCookie(name,"",-1);
}