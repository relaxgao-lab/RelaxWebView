var demojs = {};
demojs.os = {};
demojs.os.isIOS = /iOS|iPhone|iPad|iPod/i.test(navigator.userAgent);
demojs.os.isAndroid = !demojs.os.isIOS;
demojs.callbacks = {}

demojs.takeNativeAction = function(commandname, parameters){
    console.log("demojs takenativeaction")
    var request = {};
    // 为了保持ios和android的一致性，所以将请求封装成一个String
    request.name = commandname;
    request.param = parameters;
    if(window.demojs.os.isAndroid){
        console.log("android take native action" + JSON.stringify(request));
        window.relaxgaowebview.takeNativeAction(JSON.stringify(request));
    } else {
        window.webkit.messageHandlers.relaxgaowebview.postMessage(JSON.stringify(request))
    }
}

demojs.takeNativeActionWithCallback = function(commandname, parameters, callback) {
    var callbackname = "nativetojs_callback_" +  (new Date()).getTime() + "_" + Math.floor(Math.random() * 10000);
    demojs.callbacks[callbackname] = callback;

    var request = {};
    request.name = commandname;
    request.param = parameters;
    request.param.callbackname = callbackname;
    if(window.demojs.os.isAndroid){
        window.relaxgaowebview.takeNativeAction(JSON.stringify(request));
    } else {
        window.webkit.messageHandlers.relaxgaowebview.postMessage(JSON.stringify(request))
    }
}

demojs.callback = function (callbackname, response) {
   var callbackobject = demojs.callbacks[callbackname];
   if (callbackobject !== undefined){
       var ret = callbackobject(response);
       if(ret === false){
           return
       }
       delete demojs.callbacks[callbackname];
   }
}