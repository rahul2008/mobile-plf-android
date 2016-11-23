        $(document).ready(function () {
            loadCharts();
            setInterval(function () {
                loadCharts(); // this will run after every 1 hour
            }, 3600000);
        });

function loadCharts() {
    loadBatteryProfile();
    loadCPUUtilization();
    loadMemoryInformation();
    loadDataUsage();
};

function loadDataUsage() {
    var sentDataList = [];
    var recvDataList = [];
    var dateList = [];
    $.ajax({
        url: 'chartData/DataUsageDump_monitor.csv',
        type: 'GET',
        async: false,
        contentType: "application/plaintext; charset=utf-8",
        dataType: "text",
        success: function (data) {
            //alert(data);
            var lines = data.split('\n');
            $.each(lines, function (lineNo, line) {
                var tempDate = "";
                //alert(lineNo +" : "+ line);
                if (line.indexOf("No of bytes recevied") >= 0){
                    //alert("inside if");
                    return true;
                }
                else if(line == ""){
                    return true;
                }
                else {
                    //alert("inside else");
                //if (lineNo > 0) {
                    var items = line.split(',');
                    $.each(items, function (itemNo, item) {
                        //alert(itemNo +" : "+ item);
                        //item = item.substring(1, item.length - 1);
                        //alert(itemNo + " after update : " + item);
                        if (itemNo == 0)
                            recvDataList.push(parseInt(item.substring(1, item.length - 1)));
                        else if (itemNo == 1)
                            sentDataList.push(parseInt(item.substring(1, item.length - 1)));
                        else if (itemNo == 3)
                            tempDate = item + " ";
                        else
                            tempDate += item.substring(0, item.length - 1);
                    });
                    //alert(tempDate);
                    dateList.push(tempDate);
                }
            });
            var options = {
                title: {
                    text: "Graphical Trend of Data Usage"
                },
                chart: {
                    renderTo: 'first-content'
                },
                xAxis: {
                    title: {
                        text: 'Date of execution'
                    }
                },
                credits: {
                    enabled: false
                },
                yAxis: {
                    title: {
                        text: 'Data sent and recieved(in bytes)'
                    },
                    min: 0
                },
                legend: {
                    align: 'right',
                    verticalAlign: 'top',
                    layout: 'vertical',
                    x: 0,
                    y: 100
                },
                tooltip: {
                    crosshairs: {
                        width: 1,
                        color: 'gray',
                        dashStyle: 'shortdot'
                    }
                },
                series: [{
                    data: recvDataList,
                    name: "Number of bytes recieved",
                    color: "green"
                },
                {
                    data: sentDataList,
                    name: "Number of bytes transmitted",
                    color: "blue"
                }]
            };
            //alert(sentDataList);
            options.xAxis.categories = dateList;
            var chart = new Highcharts.Chart(options);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
};

function loadMemoryInformation() {
    var memoryDataList = [];
    var dateList = [];
    $.ajax({
        url: 'chartData/MemoryDump_monitor.csv',
        type: 'GET',
        async: false,
        contentType: "application/plaintext; charset=utf-8",
        dataType: "text",
        success: function (data) {
            //alert(data);
            var lines = data.split('\n');
            $.each(lines, function(lineNo, line) {
                var tempDate = {};
                var tempList = {};
               // alert(lineNo +" : "+ line);
                if (line.indexOf("Total application RAM usage") >= 0) {
                    //alert("inside if");
                    return true;
                }
                else if (line == "") {
                    return true;
                }
                else {
                //if(lineNo>0){
                    var items = line.split(',');
                    $.each(items, function(itemNo, item) {
                        //alert(itemNo +" : "+ item);
                        //item = item.substring(1, item.length - 1);
                        if(itemNo == 0)
                            tempList["y"] = parseFloat(item.substring(1, item.length - 1));
                        else if(itemNo == 1)
                            tempList["exclusiveUsage"] = parseFloat(item.substring(1, item.length - 1));
                        else if(itemNo == 2)
                            tempList["sharedUsage"] = parseFloat(item.substring(1, item.length - 1));
                        else if(itemNo == 4)
                            tempDate = item+" ";
                        else if(itemNo == 5)
                            tempDate += item.substring(0, item.length - 1);
                    });
                    //alert(tempDate);
                    //alert(tempList);
                    dateList.push(tempDate);
                    memoryDataList.push(tempList);
                }
            });
            var options = {
                title: {
                    text: "Graphical Trend of Memory Utilization"
                },
                chart: {
                    renderTo: 'second-content'
                },
                xAxis: {
                    title: {
                        text: 'Date of execution'
                    }
                },
                credits: {
                    enabled: false
                },
                yAxis: {
                    title: {
                        text: 'Memory Usage'
                    },
                    min: 0
                },
                legend: {
                    align: 'right',
                    verticalAlign: 'top',
                    layout: 'vertical',
                    x: 0,
                    y: 100
                },
                tooltip: {
                    pointFormat: "Total CPU Usage : {point.y} <br/> Shared CPU Usage: {point.sharedUsage} <br/> Exclusive CPU Usage : {point.exclusiveUsage}",
                    crosshairs: {
                        width: 1,
                        color: 'gray',
                        dashStyle: 'shortdot'
                    }
                },
                series: [{
                    data: memoryDataList,
                    name: "Memory stats",
                    color: "green"
                }]
            };
            options.xAxis.categories = dateList;
            var chart = new Highcharts.Chart(options);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown + textStatus);
        }
    });
};

function loadBatteryProfile(){
    var batteryDataList = [];
    var dateList = [];
    $.ajax({
        url: 'chartData/BatteryDump_monitor.csv',
        type: 'GET',
        async: false,
        contentType: "application/plaintext; charset=utf-8",
        dataType: "text",
        success: function (data) {
            //alert(data);
            var lines = data.split('\n');
            $.each(lines, function (lineNo, line) {
                var tempDate = '';
                var tempList = {};
                //alert(lineNo +" : "+ line);
                if (line.indexOf("Battery Level") >= 0) {
                    //alert("inside if");
                    return true;
                }
                else if (line == "") {
                    return true;
                }
                else {
                //if (lineNo > 0) {
                    var items = line.split(',');
                    $.each(items, function (itemNo, item) {
                        //alert(itemNo +" : "+ item);
                        //item = item.substring(1, item.length - 1);
                        if (itemNo == 0)
                            tempList["level"] = item.substring(1, item.length - 1);
                        else if (itemNo == 1)
                            tempList["scale"] = item.substring(1, item.length - 1);
                        else if (itemNo == 2)
                            tempList["plugged"] = item.substring(1, item.length - 1);
                        else if (itemNo == 3)
                            tempList["health"] = item.substring(1, item.length - 1);
                        else if (itemNo == 4)
                            tempList["status"] = item.substring(1, item.length - 1);
                        else if (itemNo == 5)
                            tempList["y"] = parseInt(item.substring(1, item.length - 1));
                        else if (itemNo == 6)
                            tempList["voltage"] = item.substring(1, item.length - 1);
                        else if (itemNo == 7)
                            tempList["technology"] = item.substring(1, item.length - 1);
                        else if (itemNo == 8)
                            tempList["present"] = item.substring(1, item.length - 1);
                        else if (itemNo == 10)
                            tempDate = item + " ";
                        else if (itemNo == 11)
                            tempDate += item.substring(0, item.length - 1);
                    });
                    dateList.push(tempDate);
                    batteryDataList.push(tempList);
                }
            });
            var options = {
                title: {
                    text: "Graphical Trend"
                },
                chart: {
                    renderTo: 'third-content'
                },
                xAxis: {
                    title: {
                        text: 'Date of execution'
                    }
                },
                credits: {
                    enabled: false
                },
                yAxis: {
                    title: {
                        text: 'Battery Temperature'
                    },
                    min: 0
                },
                legend: {
                    align: 'right',
                    verticalAlign: 'top',
                    layout: 'vertical',
                    x: 0,
                    y: 100
                },
                tooltip: {
                    pointFormat: "Temperature : {point.y} <br/> Voltage: {point.voltage} <br/> Battery Technology : {point.technology}",
                    crosshairs: {
                        width: 1,
                        color: 'gray',
                        dashStyle: 'shortdot'
                    }
                },
                series: [{
                    data: batteryDataList,
                    name: "Battery stats",
                    color: "green"
                }]
            };
            options.xAxis.categories = dateList;
            var chart = new Highcharts.Chart(options);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown + textStatus);
        }
    });
};

function loadCPUUtilization() {
    var cpuDataList = [];
    var dateList = [];
    $.ajax({
        url: 'chartData/CpuDump_monitor.csv',
        type: 'GET',
        async: false,
        contentType: "application/plaintext; charset=utf-8",
        dataType: "text",
        success: function (data) {
            //alert(data);
            var lines = data.split('\n');
            $.each(lines, function (lineNo, line) {
                var tempDate = '';
                var tempList = {};
                //alert(lineNo +" : "+ line);
                
                if (line.indexOf("PID") >= 0) {
                    //alert("inside if");
                    return true;
                }
                else if (line == "") {
                    return true;
                }
                else if (line.indexOf("User") >= 0) {
                    return true;
                }
                else {
                    if (line.startsWith("\"\","))
                        line = line.substring(3, line.length);
                //if (lineNo > 0) {
                    var items = line.split(',');

                    $.each(items, function (itemNo, item) {
                        //item = item.substring(1, item.length - 1);

                        //alert(itemNo +" : "+ item);
                        if (itemNo == 0)
                            tempList["pid"] = item.substring(1, item.length - 1);
                        else if (itemNo == 1)
                            tempList["pr"] = item.substring(1, item.length - 1);
                        else if (itemNo == 2)
                            tempList["y"] = parseInt(item.substring(1, item.length - 1));
                        else if (itemNo == 3)
                            tempList["s"] = item.substring(1, item.length - 1);
                        else if (itemNo == 4)
                            tempList["thr"] = item.substring(1, item.length - 1);
                        else if (itemNo == 5)
                            tempList["vss"] = item.substring(1, item.length - 1);
                        else if (itemNo == 6)
                            tempList["rss"] = item.substring(1, item.length - 1);
                        else if (itemNo == 7)
                            tempList["pcy"] = item.substring(1, item.length - 1);
                        else if (itemNo == 8)
                            tempList["uid"] = item.substring(1, item.length - 1);
                        else if (itemNo == 9)
                            tempList["appname"] = item.substring(1, item.length - 1);
                        else if (itemNo == 11)
                            tempDate = item + " ";
                        else if (itemNo == 12)
                            tempDate += item.substring(0, item.length-1);
                    });
                    //alert("Test : " + tempDate);
                    dateList.push(tempDate);
                    cpuDataList.push(tempList);
                }
            });
            var options = {
                title: {
                    text: "Graphical Trend"
                },
                chart: {
                    renderTo: 'fourth-content'
                },
                xAxis: {
                    title: {
                        text: 'Date of execution'
                    }
                },
                credits: {
                    enabled: false
                },
                yAxis: {
                    title: {
                        text: 'CPU Consumption in %'
                    },
                    min: 0
                },
                legend: {
                    align: 'right',
                    verticalAlign: 'top',
                    layout: 'vertical',
                    x: 0,
                    y: 100
                },
                tooltip: {
                    pointFormat: "Process ID : {point.pid} <br/> PR: {point.pr} <br/> CPU % : {point.y}  <br/> S: {point.s} <br/> Thread count: {point.thr} <br/> VSS: {point.vss} <br/> RSS: {point.rss} <br/> PCY: {point.pcy} <br/> UID: {point.uid} <br/> Name: {point.appname} <br/>",
                    crosshairs: {
                        width: 1,
                        color: 'gray',
                        dashStyle: 'shortdot'
                    }
                },
                series: [{
                    data: cpuDataList,
                    name: "CPU Consumption stats",
                    color: "green"
                }]
            };
            options.xAxis.categories = dateList;
            var chart = new Highcharts.Chart(options);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown + textStatus);
        }
    });
};
