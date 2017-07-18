function letstry() {

	var sensorId = document.getElementById("sensor").value
	// var selectedDate = document.getElementById("calendar").value
	Date.prototype.yyyymmdd = function() {
		var yyyy = this.getFullYear().toString();
		var mm = (this.getMonth() + 1).toString(); // getMonth() is zero-based
		var dd = this.getDate().toString();
		return yyyy + (mm[1] ? mm : "0" + mm[0]) + (dd[1] ? dd : "0" + dd[0]); // padding
	};

	d = new Date();
	var finalDate = d.yyyymmdd();
	console.log(finalDate)
	var newStr = new Array();
	var finalArray = []
	var maxArray = []
	var minArray = []
	var avgArray = []
	var newDate = parseInt(finalDate)
	for (j = 0; j <= 12; j++) {
		if (j == 5) {
			newDate = newDate - 8900;
		} else
			newDate = newDate - 100
		var dataFromAPI = httpGet("http://api.wunderground.com/api/91e0c260e0e5b86c/history_"
				+ newDate + "/q/pws:" + sensorId + ".json")

		function httpGet(theUrl) {
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("GET", theUrl, false); // false for synchronous request
			xmlHttp.send(null);
			return xmlHttp.responseText;
		}
		// console.log(dataFromAPI)
		var labelArray = [];
		var tempArray = [];
		// var feelsLikeArray = [];
		var selectedVal = document.getElementById("sel1")
		json_object = JSON.parse(dataFromAPI)
		// console.log(json_object.history.observations[0].tempi)
		// console.log(json_object.hourly_forecast[0].mslp.metric)
		for (i = 0; i < json_object.history.observations.length; i++) {
			if ((i % 7) == 0) {
				labelArray
						.push((json_object.history.observations[i].date.pretty)
								.substring(0, 8))
			} else {
				labelArray.push("")
			}
			if (sel1.value == "temp") {
				tempArray.push(json_object.history.observations[i].tempi)
				// feelsLikeArray.push(json_object.hourly_forecast[i].dewpoint.english)
			} else if (sel1.value == "pressure") {
				tempArray.push(json_object.history.observations[i].pressurei)
				// feelsLikeArray.push(json_object.hourly_forecast[i].wspd.english)
			} else if (sel1.value == "humidity") {
				tempArray.push(json_object.history.observations[i].hum)
				// feelsLikeArray.push(json_object.hourly_forecast[i].humidity)
			} else if (sel1.value == "dewpoint") {
				tempArray.push(json_object.history.observations[i].dewpti)
				// feelsLikeArray.push(Math.round(json_object.hourly_forecast[i].mslp.metric))
				// console.log("sereviosly")
			} else {
				console.log("no")
				tempArray.push(json_object.history.observations[i].tempi)
				// feelsLikeArray.push(json_object.hourly_forecast[i].dewpoint.english)
			}
		}
		var max = tempArray[0];
		var min = tempArray[0];
		var sum = 0
		sum = sum + parseFloat(tempArray[0]); // changed from original post
		var avg = 0
		for (var i = 1; i < tempArray.length; i++) {
			if (tempArray[i] > max) {
				max = tempArray[i];
			}
			if (tempArray[i] < min) {
				min = tempArray[i];
			}
			sum = sum + parseFloat(tempArray[i]);
		}
		avg = sum / tempArray.length
		maxArray.push(max)
		minArray.push(min)
		avgArray.push(avg)
	}
	finalArray.push(maxArray)
	finalArray.push(minArray)
	finalArray.push(avgArray)
	console.log("please")
	console.log(finalArray)

};

function onPageLoad(sensor_id) {
	console.log("Onload")
	Date.prototype.yyyymmdd = function() {
		var yyyy = this.getFullYear().toString();
		var mm = (this.getMonth() + 1).toString(); // getMonth() is zero-based
		var dd = this.getDate().toString();
		return yyyy + (mm[1] ? mm : "0" + mm[0]) + (dd[1] ? dd : "0" + dd[0]); // padding
	};

	d = new Date();
	var todayDate = d.yyyymmdd();
	console.log(todayDate)

	var finalArray = []
	var maxArray = []
	var minArray = []
	var avgArray = []
	var newDate = parseInt(todayDate)
	for (j = 0; j <= 12; j++) {
		if (j == 5) {
			newDate = newDate - 8900;
		} else
			newDate = newDate - 100
		var dataFromAPI = httpGet("http://api.wunderground.com/api/91e0c260e0e5b86c/history_"
				+ newDate + "/q/pws:" + sensor_id + ".json")
		function httpGet(theUrl) {
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("GET", theUrl, false); // false for synchronous request
			xmlHttp.send(null);
			return xmlHttp.responseText;
		}
		// console.log(dataFromAPI)
		var labelArray = [];
		var tempArray = [];
		var feelsLikeArray = [];
		json_object = JSON.parse(dataFromAPI)
		// console.log(json_object.hourly_forecast[0].temp.english)
		for (i = 0; i < json_object.history.observations.length; i++) {
			if ((i % 7) == 0) {
				labelArray
						.push((json_object.history.observations[i].date.pretty)
								.substring(0, 8))
			} else {
				labelArray.push("")
			}
			tempArray.push(json_object.history.observations[i].tempi)
			// feelsLikeArray.push(json_object.hourly_forecast[i].dewpoint.english)
		}
		var max = tempArray[0];
		var min = tempArray[0];
		var sum = 0
		sum = sum + parseFloat(tempArray[0]); // changed from original post
		var avg = 0
		for (var i = 1; i < tempArray.length; i++) {
			if (tempArray[i] > max) {
				max = tempArray[i];
			}
			if (tempArray[i] < min) {
				min = tempArray[i];
			}
			sum = sum + parseFloat(tempArray[i]);
		}
		avg = sum / tempArray.length
		maxArray.push(max)
		minArray.push(min)
		avgArray.push(avg)
	}
	finalArray.push(maxArray)
	finalArray.push(minArray)
	finalArray.push(avgArray)
	console.log("please")
	console.log(finalArray)
};