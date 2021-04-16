const status = {
    SUCCESS: 0
}

function send_request_sync(url, method, callback) {
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
    }
    xmlhttp.open(method, url, false);
    xmlhttp.send();
}

function send_request_async(url, method, callback) {
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
    }
    xmlhttp.open(method, url, true);
    xmlhttp.send();
}
