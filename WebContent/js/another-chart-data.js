var randomScalingFactor = function() {
    return Math.round(Math.random() * 1000)
};

function letstry() {

    var sensorId = document.getElementById("sensor").value
    var selectedDate = document.getElementById("calendar").value
    var newStr = new Array();
    newStr = selectedDate.split('/')
    var finalDate = newStr[2] + newStr[0] + newStr[1]
    console.log(finalDate)

    var dataFromAPI = httpGet("http://api.wunderground.com/api/91e0c260e0e5b86c/history_" + finalDate + "/q/pws:" + sensorId + ".json")


    function httpGet(theUrl) {
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", theUrl, false); // false for synchronous request
        xmlHttp.send(null);
        return xmlHttp.responseText;
    }
    //console.log(dataFromAPI)
    var labelArray = [];
    var tempArray = [];
    //var feelsLikeArray = [];
    var selectedVal = document.getElementById("sel1")
    json_object = JSON.parse(dataFromAPI)
    console.log("hello")
    console.log(json_object)
    //console.log(json_object.history.observations[0].tempi)
    //console.log(json_object.hourly_forecast[0].mslp.metric)
    for (i = 0; i < json_object.history.observations.length; i++) {
        if((i%7)==0) {
            labelArray.push((json_object.history.observations[i].date.pretty).substring(0,8))
        }
        else {
            labelArray.push("")
        }
        if (sel1.value == "temp") {
            tempArray.push(json_object.history.observations[i].tempi)
            //feelsLikeArray.push(json_object.hourly_forecast[i].dewpoint.english)
        } else if (sel1.value == "pressure") {
            tempArray.push(json_object.history.observations[i].pressurei)
            //feelsLikeArray.push(json_object.hourly_forecast[i].wspd.english)
        } else if (sel1.value == "humidity") {
            tempArray.push(json_object.history.observations[i].hum)
            //feelsLikeArray.push(json_object.hourly_forecast[i].humidity)
        } else if (sel1.value == "dewpoint") {
        	tempArray.push(json_object.history.observations[i].dewpti)
            //feelsLikeArray.push(Math.round(json_object.hourly_forecast[i].mslp.metric))
            //console.log("sereviosly")
        }
        else {
        	console.log("no")
        	tempArray.push(json_object.history.observations[i].tempi)
            //feelsLikeArray.push(json_object.hourly_forecast[i].dewpoint.english)
        }
    }

    var lineChartData = {
        labels: labelArray,
        datasets: [{
            label: "My First dataset",
            fillColor: "rgba(48, 164, 255, 0.2)",
            strokeColor: "rgba(48, 164, 255, 1)",
            pointColor: "rgba(48, 164, 255, 1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(48, 164, 255, 1)",
            data: tempArray
        }]

    }

    var barChartData = {
        labels: labelArray,
        datasets: [{
            fillColor: "rgba(48, 164, 255, 0.2)",
            strokeColor: "rgba(48, 164, 255, 0.8)",
            highlightFill: "rgba(48, 164, 255, 0.75)",
            highlightStroke: "rgba(48, 164, 255, 1)",
            data: tempArray
        }]

    }

    var chart1 = document.getElementById("line-chart").getContext("2d");
    window.myLine = new Chart(chart1).Line(lineChartData, {
        responsive: true
    });
    var chart2 = document.getElementById("bar-chart").getContext("2d");
    window.myBar = new Chart(chart2).Bar(barChartData, {
        responsive: true
    });

};

function onPageLoad(sensor_id) {
    console.log("Onload")
        //var username = readCookie("user")
        /*var dataFromAPI1 = httpGet("http://localhost:8080/MobileSensorCloudEngine/api/sensor/"+"shaurya104")
	function httpGet(theUrl) {
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", theUrl, false); // false for synchronous request
        xmlHttp.send(null);
        return xmlHttp.responseText;
    }
    json_object = JSON.parse(dataFromAPI1)



    var sensorId = json_object.sensors[0].sensor_id*/
    Date.prototype.yyyymmdd = function() {
   var yyyy = this.getFullYear().toString();
   var mm = (this.getMonth()+1).toString(); // getMonth() is zero-based
   var dd  = this.getDate().toString();
   return yyyy + (mm[1]?mm:"0"+mm[0]) + (dd[1]?dd:"0"+dd[0]); // padding
  };

    d = new Date();
    var todayDate = d.yyyymmdd();
    console.log("onloadtodaydate")
    console.log(todayDate)
    var newDate = todayDate - 10000;
    console.log(newDate)

    //var sensorId = "KCAMILPI18"
//    console.log("inside onloadpage");
//    console.log(sensor_id);
    var dataFromAPI = httpGet("http://api.wunderground.com/api/91e0c260e0e5b86c/history_" + todayDate + "/q/pws:" + sensor_id + ".json")
    function httpGet(theUrl) {
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", theUrl, false); // false for synchronous request
        xmlHttp.send(null);
        return xmlHttp.responseText;
    }
    //console.log(dataFromAPI)
    var labelArray = [];
    var tempArray = [];
    var feelsLikeArray = [];
    json_object = JSON.parse(dataFromAPI)
    console.log("hello")
    console.log(json_object)
    //console.log(json_object.hourly_forecast[0].temp.english)
    for (i = 0; i < json_object.history.observations.length; i++) {
        if((i%7)==0) {
            labelArray.push((json_object.history.observations[i].date.pretty).substring(0,8))
        }
        else {
            labelArray.push("")
        }
        tempArray.push(json_object.history.observations[i].tempi)
        //feelsLikeArray.push(json_object.hourly_forecast[i].dewpoint.english)
    }

    var lineChartData = {
        labels: labelArray,
        datasets: [{
            label: "My First dataset",
            fillColor: "rgba(48, 164, 255, 0.2)",
            strokeColor: "rgba(48, 164, 255, 1)",
            pointColor: "rgba(48, 164, 255, 1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(48, 164, 255, 1)",
            data: tempArray
        }]

    }
    var chart1 = document.getElementById("line-chart").getContext("2d");
    window.myLine = new Chart(chart1).Line(lineChartData, {
        responsive: true
    });
};