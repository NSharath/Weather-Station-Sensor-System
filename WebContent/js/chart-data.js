var randomScalingFactor = function() {
    return Math.round(Math.random() * 1000)
};

function letstry() {

    var sensorId = document.getElementById("sensor").value

    var dataFromAPI = httpGet("http://api.wunderground.com/api/91e0c260e0e5b86c/hourly/q/pws:" + sensorId + ".json")

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
    console.log(json_object.hourly_forecast[0].temp.english)
    console.log(json_object.hourly_forecast[0].mslp.metric)
    for (i = 0; i < json_object.hourly_forecast.length; i++) {
        labelArray.push(json_object.hourly_forecast[i].FCTTIME.civil)
        if (sel1.value == "temp") {
            tempArray.push(json_object.hourly_forecast[i].temp.english)
            //feelsLikeArray.push(json_object.hourly_forecast[i].dewpoint.english)
        } else if (sel1.value == "wspd") {
            tempArray.push(json_object.hourly_forecast[i].wspd.english)
            //feelsLikeArray.push(json_object.hourly_forecast[i].wspd.english)
        } else if (sel1.value == "humidity") {
            tempArray.push(json_object.hourly_forecast[i].humidity)
            //feelsLikeArray.push(json_object.hourly_forecast[i].humidity)
        } else if (sel1.value == "mslp") {
        	tempArray.push(Math.round(json_object.hourly_forecast[i].mslp.metric))
            //feelsLikeArray.push(Math.round(json_object.hourly_forecast[i].mslp.metric))
            console.log("sereviosly")
        }
        else {
        	console.log("no")
        	tempArray.push(json_object.hourly_forecast[i].temp.english)
            feelsLikeArray.push(json_object.hourly_forecast[i].dewpoint.english)
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


    var chart1 = document.getElementById("line-chart").getContext("2d");
    window.myLine = new Chart(chart1).Line(lineChartData, {
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
    //var sensorId = "KCAMILPI18"

    var dataFromAPI = httpGet("http://api.wunderground.com/api/91e0c260e0e5b86c/hourly/q/pws:" + sensor_id + ".json")

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
    console.log(json_object.hourly_forecast[0].temp.english)
    for (i = 0; i < json_object.hourly_forecast.length; i++) {
        labelArray.push(json_object.hourly_forecast[i].FCTTIME.civil)
        tempArray.push(json_object.hourly_forecast[i].temp.english)
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