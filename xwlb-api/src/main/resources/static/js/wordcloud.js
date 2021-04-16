let option = {};

function initOption(keywords) {
    const data = [];
    for (const key in keywords) {
        data.push({
            name: key,
            value: Math.sqrt(keywords[key])
        })
    }
    option = {
        tooltip: {},
        series: [{
            type: 'wordCloud',
            gridSize: 2,
            sizeRange: [12, 50],
            rotationRange: [-90, 90],
            shape: 'pentagon',
            width: 600,
            height: 400,
            drawOutOfBound: true,
            textStyle: {
                normal: {
                    color: function () {
                        return 'rgb(' + [
                            Math.round(Math.random() * 160),
                            Math.round(Math.random() * 160),
                            Math.round(Math.random() * 160)
                        ].join(',') + ')';
                    }
                },
                emphasis: {
                    shadowBlur: 10,
                    shadowColor: '#333'
                }
            },
            data: data
        }]
    };
}


function _initChart(data) {
    initOption(data);
    const myChart = echarts.init(document.getElementById('wordCloud'));
    myChart.setOption(option);
    myChart.on('click', function (params) {
        window.open("/newsText?word=" + params.data.name + "&" + window.location.search.substring(1), "_blank");
    });
    window.onresize = function () {
        myChart.resize();
    }
}

function initChart() {
    send_request_sync("/news/keywords?" + window.location.search.substring(1), "GET", _initChart)
}


