const status = {
    SUCCESS: 0
};

function send_request_sync(url, method, callback) {
    send_request_sync(url, method, callback, undefined);
}

function send_request_sync(url, method, callback, data) {
    send_request(url, method, callback, false, data);
}

function send_request_async(url, method, callback) {
    send_request_async(url, method, callback, undefined);
}

function send_request_async(url, method, callback, data) {
    send_request(url, method, callback, true, data);
}

function send_request(url, method, callback, async, data) {
    let xmlhttp;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    } else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            if (callback) {
                const data = JSON.parse(xmlhttp.responseText);
                if (data.status.code !== status.SUCCESS) {
                    alert(data.status.message);
                } else {
                    callback(data.body);
                }
            }
        }
    };
    xmlhttp.open(method, url, async);
    if (data === undefined) {
        xmlhttp.send();
    } else {
        xmlhttp.setRequestHeader("Content-type", "application/json");
        xmlhttp.send(data)
    }
}
