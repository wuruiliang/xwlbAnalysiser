function sent_request_sync(url, method, callback) {
    let xmlhttp;
    if (window.XMLHttpRequest)
    {
        xmlhttp=new XMLHttpRequest();
    }
    else
    {
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState===4 && xmlhttp.status===200)
        {
            if (callback) {
                callback(JSON.parse(xmlhttp.responseText));
            }
        }
    }
    xmlhttp.open(method,url,false);
    xmlhttp.send();
}

function sent_request_async(url, method, callback) {
    let xmlhttp;
    if (window.XMLHttpRequest)
    {
        xmlhttp=new XMLHttpRequest();
    }
    else
    {
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState===4 && xmlhttp.status===200)
        {
            if (callback) {
                callback(JSON.parse(xmlhttp.responseText));
            }
        }
    }
    xmlhttp.open(method,url,true);
    xmlhttp.send();
}
