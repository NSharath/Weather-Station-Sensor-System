<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sensor Data Collector</title>
</head>
<body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script>
//var host = "localhost:8080";
var host = "ec2-54-187-140-163.us-west-2.compute.amazonaws.com:8080";
//http://ec2-54-201-231-101.us-west-2.compute.amazonaws.com:8080/

$(function () {
	function callPostUserServlet() {
        var myData = {
                "first": "Jelson",
                "last": "Santos",
                "password": "jelson01",
                "email": "jelson@example.com",
                "username": "jsantos01"
        };
        console.log(myData);
        //var url_post = 'http://' + host + '/AutomaticClassAttendanceSystem/sensor/class/student/' + sid;
        var url_post = 'http://' + host + '/MobileSensorCloudEngine/api/user/jsantos01';
        console.log(url_post);
        $.ajax({
            type: "POST",
            url: url_post,  //"/AjaxServletCalculator",
            contentType : 'application/json; charset=utf-8',
            data: {
                jsonData: JSON.stringify(myData) // this works but mocky.io doesn't support it
            },
            dataType: "json",

            //if received a response from the server
            success: function (json) {
                //our country code was correct so we have some information to display
                console.log(json);
                //var json = JSON.parse(data);
                alert(json.data);
                /*document.getElementById('number1').value = data.*/
            }
        });
    }
	
	function callPostSensorServlet() {
        var myData = {
                "username": "jsantos",
                "sensor_id": "KCASANFRAN177",
                "sensor_key": "jsantos01_KCASANFRAN177",
                "name": "My Sensor",
                "location": "San Jose, CA",
                "latitude": "37.410000",
                "longitude": "-121.920000",
                "status": false,
                "hub": "hub_2"
        };
        callPutSensorHubServlet();
        console.log(myData);
        //var url_post = 'http://' + host + '/AutomaticClassAttendanceSystem/sensor/class/student/' + sid;
        //var url_post = 'http://' + host + '/MobileSensorCloudEngine/api/sensor/jsantos7/KCASANJO177';
        var url_post = 'http://' + host + '/MobileSensorCloudEngine/api/sensor/jsantos7/KCASANFRAN177';
        console.log(url_post);
        $.ajax({
            type: "POST",
            url: url_post,  //"/AjaxServletCalculator",
            contentType : 'application/json; charset=utf-8',
            data: {
                jsonData: JSON.stringify(myData) // this works but mocky.io doesn't support it
            },
            dataType: "json",

            //if received a response from the server
            success: function (json) {
                //our country code was correct so we have some information to display
                console.log(json);
                //var json = JSON.parse(data);
                alert(json.data);
                /*document.getElementById('number1').value = data.*/
            }
        });
    }
	
	function callPostHubServlet() {
        var myData = {
                "username": "jsantos",
                "hub_id": "hub_1",
                "name": "Hub 1",
                "description": "This is my hub 1"
        };
        console.log(myData);
        //var url_post = 'http://' + host + '/AutomaticClassAttendanceSystem/sensor/class/student/' + sid;
        var url_post = 'http://' + host + '/MobileSensorCloudEngine/api/hub/jsantos7/hub_1';
        console.log(url_post);
        $.ajax({
            type: "POST",
            url: url_post,  //"/AjaxServletCalculator",
            contentType : 'application/json; charset=utf-8',
            data: {
                jsonData: JSON.stringify(myData) // this works but mocky.io doesn't support it
            },
            dataType: "json",

            //if received a response from the server
            success: function (json) {
                //our country code was correct so we have some information to display
                console.log(json);
                //var json = JSON.parse(data);
                alert(json.data);
                /*document.getElementById('number1').value = data.*/
            }
        });
    }
	
	function callPutHubServlet() {
        var myData = {
                "username": "jsantos",
                "hub_id": "hub_2",
                "name": "Hub 2",
                "description": "This is my hub 2"
        };
        console.log(myData);
        //var url_post = 'http://' + host + '/AutomaticClassAttendanceSystem/sensor/class/student/' + sid;
        var url_post = 'http://' + host + '/MobileSensorCloudEngine/api/hub/jsantos7/hub_2';
        console.log(url_post);
        $.ajax({
            type: "PUT",
            url: url_post,  //"/AjaxServletCalculator",
            contentType : 'application/json; charset=utf-8',
            data: {
                jsonData: JSON.stringify(myData) // this works but mocky.io doesn't support it
            },
            dataType: "json",

            //if received a response from the server
            success: function (json) {
                //our country code was correct so we have some information to display
                console.log(json);
                //var json = JSON.parse(data);
                alert(json.data);
                /*document.getElementById('number1').value = data.*/
            }
        });
    }
	
	function callPutSensorHubServlet() {
        var myData = {
                "username": "jsantos7",
                "hub_id": "hub_2",
                "name": "Hub 2",
                "description": "This is my hub 2"
        };
        console.log(myData);
        //var url_post = 'http://' + host + '/AutomaticClassAttendanceSystem/sensor/class/student/' + sid;
        var url_post = 'http://' + host + '/MobileSensorCloudEngine/api/hub/jsantos7/hub_2/add_sensor/KCASANFRAN87';
        console.log(url_post);
        $.ajax({
            type: "PUT",
            url: url_post,  //"/AjaxServletCalculator",
            contentType : 'application/json; charset=utf-8',
            data: {
                jsonData: JSON.stringify(myData) // this works but mocky.io doesn't support it
            },
            dataType: "json",

            //if received a response from the server
            success: function (json) {
                //our country code was correct so we have some information to display
                console.log(json);
                //var json = JSON.parse(data);
                alert(json.data);
                /*document.getElementById('number1').value = data.*/
            }
        });
    }
	
    function callServlet() {
    	var first = $("#first").val(); //document.getElementById('number1').value;
        var last = $("#last").val(); //document.getElementById('number2').value;
        var sid = $("#sid").val(); //document.getElementById('number2').value;
        var myData = {
                "first": first,
                "last": last,
                "sid": sid,
                "email": "example@example.com"
        };
        console.log(myData);
        var url_post = 'http://' + host + '/AutomaticClassAttendanceSystem/sensor/class/student/' + sid;
        console.log(url_post);
        $.ajax({
            type: "POST",
            url: url_post,  //"/AjaxServletCalculator",
            contentType : 'application/json; charset=utf-8',
            data: {
                jsonData: JSON.stringify(myData) // this works but mocky.io doesn't support it
            },
            dataType: "json",

            //if received a response from the server
            success: function (json) {
                //our country code was correct so we have some information to display
                console.log(json);
                //var json = JSON.parse(data);
                alert(json.data);
                /*document.getElementById('number1').value = data.*/
            }
        });
    }
    
    function callDeleteServlet() {
        $.ajax({
            type: "DELETE",
            url: "http://' + host + '/AutomaticClassAttendanceSystem/sensor/class/student/1234", //"/AjaxServletCalculator",
            contentType : 'application/json; charset=utf-8',
           /*  data: {
                jsonData: JSON.stringify(myData) // this works but mocky.io doesn't support it
            }, */
            //dataType: "json",

            //if received a response from the server
            success: function (json) {
                //our country code was correct so we have some information to display
                console.log(json);
                //var json = JSON.parse(data);
                //alert(json.data);
                //document.getElementById('number1').value = data
            } 
            
        });
    }
    
    function callDeleteUserServlet() {
    	var url_post = 'http://' + host + '/MobileSensorCloudEngine/api/user/jsantos01';
        $.ajax({
            type: "DELETE",
            //url: "http://' + host + '/AutomaticClassAttendanceSystem/sensor/class/student/1234", //"/AjaxServletCalculator",
            url: url_post,
            contentType : 'application/json; charset=utf-8',
           /*  data: {
                jsonData: JSON.stringify(myData) // this works but mocky.io doesn't support it
            }, */
            //dataType: "json",

            //if received a response from the server
            success: function (json) {
                //our country code was correct so we have some information to display
                console.log(json);
                //var json = JSON.parse(data);
                //alert(json.data);
                //document.getElementById('number1').value = data
            } 
            
        });
    }
    
    function callDeleteSensorServlet() {
    	var url_post = 'http://' + host + '/MobileSensorCloudEngine/api/sensor/KCASANJO177';
        $.ajax({
            type: "DELETE",
            //url: "http://' + host + '/AutomaticClassAttendanceSystem/sensor/class/student/1234", //"/AjaxServletCalculator",
            url: url_post,
            contentType : 'application/json; charset=utf-8',
           /*  data: {
                jsonData: JSON.stringify(myData) // this works but mocky.io doesn't support it
            }, */
            //dataType: "json",

            //if received a response from the server
            success: function (json) {
                //our country code was correct so we have some information to display
                console.log(json);
                //var json = JSON.parse(data);
                //alert(json.data);
                //document.getElementById('number1').value = data
            } 
            
        });
    }
    
    $('#calcBtn').click(function() {
        callServlet();
    });
    $('#DelBtn').click(function() {
        callDeleteServlet();
    });
    $('#postuser').click(function() {
        callPostUserServlet();
    });
    $('#deleteuser').click(function() {
        callDeleteUserServlet();
    });
    $('#postsensor').click(function() {
    	callPostSensorServlet();
    });
    $('#deletesensor').click(function() {
    	callDeleteSensorServlet();
    });
    $('#posthub').click(function() {
    	callPostHubServlet();
    });
    $('#puthub').click(function() {
    	callPutHubServlet();
    });
    $('#putsensorhub').click(function() {
    	callPutSensorHubServlet();
    });
    
});
</script>
<h3>Please enter a number to Square : </h3>

<input style="width: 33px; margin-left: 2px; " type="text" id="first" name="first">
<input style="width: 33px; margin-left: 2px;" type="text" id="last" name="last">
<input style="width: 33px; margin-left: 2px; " type="text" id="sid" name="sid">
<input type="button" id="calcBtn" value="Submit">
<input style="font-family: cursive; border:none" type="text" id="result" />
<input style="font-family: cursive; border:none; width: 100%" type="text" value="" id="resultText" />

<h3>Delete User : </h3>

<input type="button" id="DelBtn" value="Delete">
<h3>Post User : </h3>
<input type="button" id="postuser" value="Post User">
<h3>Delete User : </h3>
<input type="button" id="deleteuser" value="Delete User">

<h3>Post Sensor : </h3>
<input type="button" id="postsensor" value="Post Sensor">

<h3>Delete Sensor : </h3>
<input type="button" id="deletesensor" value="Delete Sensor">

<h3>Post Hub : </h3>
<input type="button" id="posthub" value="Post Hub">
<h3>Put Hub : </h3>
<input type="button" id="puthub" value="Put Hub">
<h3>Put Sensor Hub : </h3>
<input type="button" id="putsensorhub" value="Put Sensor Hub">
</body>
</html>