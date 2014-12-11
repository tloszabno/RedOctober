function url(s) {
	var l = window.location;
	return ((l.protocol === "https:") ? "wss://" : "ws://") + l.hostname + (((l.port != 80) && (l.port != 443)) ? ":" + l.port : "")  +"/" +s;
}



var wsUri = url("join");
log("URL="  + wsUri);

var output;
var controller;

var data;
var root;

var websocket;

function init() {
	controller = new Controller();
	testWebSocket();
}

function testWebSocket() {
	var name = $("#userName").val();
	var team = $("#teamName").val();

	websocket = new WebSocket(wsUri+"?name="+name+"&team="+team);
	websocket.onopen = function(evt) {
		onOpen(evt)
	};
	websocket.onclose = function(evt) {
		onClose(evt)
	};
	websocket.onmessage = function(evt) {
		onMessage(evt)
	};
	websocket.onerror = function(evt) {
		onError(evt)
	};
}

function onOpen(evt) {
	writeToScreen("CONNECTED");
	//doSend({messageType:"navigation",text:"foobar"}); // currently accepts only true, false or null, otherwise exception is thrown, need to investigate
}

function onClose(evt) {
	writeToScreen("DISCONNECTED");
}

function onMessage(evt) {
	console.log("MSG from server=" + evt.data);


	controller.dispatch(JSON.parse(evt.data), doSend)
	//writeToScreen('<span style="color: blue;">RESPONSE: ' + evt.data
	//		+ '</span>');
	//websocket.close();
}

function onError(evt) {
	writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function doSend(message) {
	var stringified = JSON.stringify(message);
	writeToScreen("SENT: " + stringified);

	websocket.send(stringified);
}

function writeToScreen(message) {
	console.log(message);
//		var pre = document.createElement("p");
//		pre.style.wordWrap = "break-word";
//		pre.innerHTML = message;
//		output.appendChild(pre);
}

window.addEventListener("load", init, false);

function getParameterByName(name) {
	name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
			results = regex.exec(location.search);
	return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
