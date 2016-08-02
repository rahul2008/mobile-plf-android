<!--
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 * Developed by Hospital to Home Automation Team

 -->

<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="index.aspx.cs" Inherits="Philips.H2H.Automation.Dashboard.index" %>

<%@ Import Namespace="Philips.H2H.Automation.Dashboard" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="H2H Automation portal">
    <meta name="author" content="Philips">
<%--    <meta name="viewport" content="width=device-width, initial-scale=1">--%>
    <title>Automation Portal</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- datatables -->
    <!-- DataTables CSS -->
    <link rel="stylesheet" type="text/css" href="css/dataTables/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="css/expanddetails.css">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="js/html5shiv.js"></script>
        <script src="js/respond.min.js"></script>
        <![endif]-->
    <style type="text/css">
        .divback {
            background-image: url(images/header.jpg);
            background-position: center center;
            background-repeat: no-repeat;
            background-attachment: fixed;
            background-size: cover;
        }
        .navbar-inversee {
            border-color: #ffffff;
        }
        .menuoptions{
            padding-left:5px;
            padding-right:5px;
        }
        .btn-success{
            background-color:#303641;
            border-color:#303641;
        }
    </style>
</head>

<body onload="init()">
    <% Response.WriteFile("~/header.html");%>

    <form runat="server">
        <!-- Page Content -->
        <div class="container">
            <div class="modal fade" id="myModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title"></h4>
                        </div>
                        <div class="modal-body">
                            <p id="body"></p>
                        </div>
                    </div>
                    <!-- /.modal-content -->
                </div>
                <!-- /.modal-dialog -->
            </div>
            <!-- /.modal -->
            <br>
            <div class="row" style="align-content: center">
                <div class="col-lg-2" style="float: left; width: auto; text-align: center">
                    <h4>Select test suites :</h4>
                </div>
                <div class="col-lg-2" style=" margin-left:0px; width: auto; float: left">
                    <select class="form-control" id="suite" runat="server" style="border-color: black">
                    </select>
                </div>                
            </div>
            <br />
            <div class="row" style="align-content: center">
                <div class="col-lg-4 menuoptions" style="width: auto; float: left">
                    <asp:LinkButton ID="btnDownload"
                        runat="server"
                        class="btn btn-success"
                        Text="Download Test Report"
                        OnClick="btnDownload_Click" OnClientClick="CheckBuildURI()">
                <span class="glyphicon glyphicon-download-alt" style="top: 2px;" aria-hidden="true"></span>&nbspTest Report
                    </asp:LinkButton>
                    <button type="button" class="btn btn-success" id="trend">
                        <span class="glyphicon glyphicon-signal" style="top: 2px;" aria-hidden="true"></span>&nbspBuild Status Trend</button>
                    <%--<button class="btn btn-success" id="execution" runat="server" onserverclick="execution_Click" type="button">
                        <span class="glyphicon glyphicon-play" style="top: 2px;" aria-hidden="true"></span>&nbspStart Execution</button>--%>
                    <asp:LinkButton ID="execution"
                        runat="server"
                        class="btn btn-success"
                        Text="Start Execution"
                        OnClick="execution_Click">
                <span class="glyphicon glyphicon-download-alt" style="top: 2px;" aria-hidden="true"></span>&nbspStart Execution
                    </asp:LinkButton>
                    <button type="button" class="btn btn-success" id="Button1" onclick="CheckBuildStatus('Running Build Details')">
                        <span class="iconStatus mif-thumbs-down"></span><span class="glyphicon glyphicon-eye-open" style="top: 2px;" aria-hidden="true"></span>&nbspRunning Build Status</button>
                    <button class="btn btn-success" id="btnCoverage" onclick="CoverageDetails()" type="button">
                        <span class="glyphicon glyphicon-refresh" style="top: 2px;" aria-hidden="true"></span>&nbspCoverage</button>
                    <button class="btn btn-success" id="btnPerformance" type="button">
                        <span class="glyphicon glyphicon-signal" style="top: 2px;" aria-hidden="true"></span>&nbspPerformance</button>
                    <button class="btn btn-success" id="btnDevMonitor" type="button">
                        <span class="glyphicon glyphicon-search" style="top: 2px;" aria-hidden="true"></span>&nbspDevice Monitoring</button>
                     <button class="btn btn-success" id="btnTestSummary" type="button">
                        <span class="glyphicon glyphicon-signal" style="top: 2px;" aria-hidden="true"></span>&nbspTest Summary</button>
                </div>
            </div>
        </div>
        <br>
       
        <div class="loading" style="text-align: center">
            Fetching data from Team Foundation Server, Please wait.....<br />
            <img src="images/loading.gif" alt="" />
        </div>
        
        <div id="demo" class="container" style="display:none">
        </div>
        <br />
        <div id="relContainer" class="container" style="display:none">
            <div class="row">
                <div class="col-md-1"></div>
                <div class="col-md-10 chartContainer" id="relDemo" style="overflow:auto">
                </div>
                <div class="col-md-1"></div>
            </div>
            <br /><br />
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6 chartContainer">
                    <h4 class="panel-heading text-center">Execution time chart for test cases</h4>
                    <div id="exec-time-chart">
                    </div>
                </div>
                <div class="col-md-3"></div>
            </div>   
        </div>
        <script src="metroUI/js/docs.js"></script>
        <script type="text/javascript" charset="utf8" src="metroUI/js/jquery-2.1.3.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <!-- DataTables -->
        <script type="text/javascript" charset="utf8" src="js/dataTables/jquery.dataTables.min.js"></script>
        <script src="js/libs/highcharts.js"></script>
        <script src="js/libs/exporting.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                var suites = "<%=  WebConfigInitializer.SuiteName %>";
                var suite = suites.split(',');
                var reliabilityConst = "<%=  WebConfigInitializer.reliabilityConst %>";
                var buildVersion = "<%=lastBuildVersion%>";

                $.each(suite, function (i, value) {
                    var suiteName_friendlyName = value.split(':');
                    $("#suite").append($("<option></option>").val(suiteName_friendlyName[0]).html(suiteName_friendlyName[1]));
                });

                $('#trend').click(function () {
                    window.location = "/Tiles.aspx?buildDefinition=" + $('#suite').val();
                });

                $('#graph').click(function () {
                    window.location = "/graphs.aspx?buildDefinition=" + $('#suite').val();
                });

                $('#btnPerformance').click(function () {
                    window.location = "/PerformanceMonitor.aspx";
                });

                $('#btnTestSummary').click(function () {
                    window.location = "/TestSummary.aspx";
                });
                $('#btnDevMonitor').click(function () {
                    window.location = "/DeviceMonitor.aspx";
                });
                //$('#execution').click(function () {
                //    var buildDef = $('#suite').val();
                //    alert(buildDef);
                //    $.ajax({
                //        url: './index.aspx/QueueNewBuild?buildDefinitionName='+buildDef,
                //        type: 'POST',
                //        async: false,
                //        contentType: "application/json; charset=utf-8",
                //        dataType: "json",
                //        success: function () {
                //            alert('success');
                //        }
                //    });
                //})

                $("#suite").change(function () {
                    $('.loading').css('display', 'block');
                    $('#demo').css('display', 'none');
                    if ($(this).find('option:selected').text() == reliabilityConst) {
                        var data = fetchRunDataFromXML();
                        fillDataTableForReliability(data);
                        $('#demo').hide();
                        $('#relContainer').show();
                    }
                    else {
                        fillDataTable(this.value);
                        $('#demo').show();
                        $('#relContainer').hide();
                    }
                });

                if ($('#suite').find().text() == reliabilityConst) {
                    var data = fetchRunDataFromXML();
                    fillDataTableForReliability(data);
                    $('#demo').hide();
                    $('#relContainer').show();
                }
                else {
                    fillDataTable($('#suite').val());
                    $('#demo').show();
                    $('#relContainer').hide();
                }

                $('#projectTitle').html("<%=   WebConfigInitializer.ProjectTitle  %>");

                function fillDataTableForReliability(data) {
                    $('.loading').css('display', 'none');
                    $('#relDemo').css('display', 'block');
                    $('#relDemo').html('<div id="relTrendHeader" class="CSSTableGenerator" style="text-align: center; width: 100%; margin-top: 0px"><b>APK Build Version: ' + buildVersion +'<table cellpadding="0" cellspacing="0" border="1" class="display" id="relexample" style="text-align: left"></table></div>');

                    $('#relexample').DataTable({
                        data: rowValues(data[3], data[2]),
                        columns: column_loader(data[1]),
                        columnDefs: [{
                            "targets": "_all",
                            "createdCell": function (td, cellData, rowData, row, col) {
                                if (cellData == "Fail") {
                                    $(td).css("background-color", "red");
                                    $(td).css("color", "white");
                                    $(td).html('F');
                                }
                                else if (cellData == "Pass") {
                                    $(td).css("background-color", "green");
                                    $(td).css("color", "white");
                                    $(td).html('P');
                                }
                            }

                        }],
                        order: [[1, 'asc']]
                    });


                    function column_loader(columns) {
                        var columns2 = [];
                        columns2[0] = { title: "Test Case Name" };
                        $.each(columns, function (index, value) {
                            columns2[columns2.length] = { title: "Itr " + value.toString() };
                        });
                        return columns2;
                    }

                    function rowValues(testcaseName, rows) {
                        var row2 = [];
                        var manual = [];
                        manual[0] = testcaseName[0];
                        $.each(rows, function (index, value) {
                            manual[manual.length] = value;
                        });
                        row2.push(manual);
                        return row2;
                    }

                }

                function fillDataTable(buildDefinition) {
                    var obj = {};
                    obj.name = buildDefinition;
                    $.ajax({
                        url: './index.aspx/GetLastTestRunData',
                        data: JSON.stringify(obj),
                        type: 'POST',
                        async: false,
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        success: function (dataR) {
                            $('.loading').css('display', 'none');
                            $('#demo').css('display', 'block');
                            var dataT = $.parseJSON(dataR.d);
                            var buildFinishTime = (dataT.buildFinishTime);
                            $('#demo').html('<div id="trendHeader" class="CSSTableGenerator" style="text-align: center; width: 100%; margin-top: 0px"><b>APK Build Version: ' + buildVersion + '&nbsp;&nbsp;</b><b>Last Build Execution Time: ' + buildFinishTime + '</b>'
                                + '<table cellpadding="0" cellspacing="0" border="1" class="display" id="example" style="text-align: left"></table></div>');

                            var table = $('#example').DataTable({
                                "data": dataT.TestRunDetails.TestCaseDetails,
                                "columns": [
                                {
                                    "title": "Details",
                                    "className": 'details-control',
                                    // "orderable": false,
                                    "data": null,
                                    "defaultContent": '', "sWidth": "5%"
                                },
                                {
                                    "title": "Scenario Name", "data": "TestCaseTitle"
                                },
                                {
                                    "title": "Status", "sWidth": "3%",
                                    "data": "Outcome"
                                },
                                { "title": "Duration (Minute)", "sWidth": "10%", "data": "ShortDate" },
                                ],
                                "order": [[1, 'asc']],
                                "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                                    $('td:eq(2)', nRow).css("color", "Black");
                                    switch (aData.Outcome) {
                                        case 'Passed':
                                            $('td:eq(2)', nRow).css("background-color", "green");
                                            $('td:eq(2)', nRow).css("color", "White");
                                            break;
                                        case 'Failed':
                                            $('td:eq(2)', nRow).css("background-color", "red");
                                            $('td:eq(2)', nRow).css("color", "white");
                                            break;
                                    }
                                }
                            });

                            // Add event listener for opening and closing details
                            $('#example tbody').on('click', 'td.details-control', function () {
                                var tr = $(this).closest('tr');
                                var row = table.row(tr);
                                if (row.child.isShown()) {
                                    // This row is already open - close it
                                    row.child.hide();
                                    tr.removeClass('shown');
                                }
                                else {
                                    // Open this row
                                    row.child(format(row.data())).show();
                                    tr.addClass('shown');
                                }
                            });

                        }
                    });
                }

            });

            function fetchRunDataFromXML() {
                var testDuration = [];
                var testIteration = [];
                var finalData = [];
                var testcaseName = [];
                var runStatus = [];

                $.ajax({
                    url: './index.aspx/GetReliabilityTestRunDetails',
                    type: 'POST',
                    async: false,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (response) {
                        var responseData = $.parseJSON(response.d);
                        //var systemConfig = responseData.SystemConfiguration;
                        //alert(systemConfig.SystemName);
                        var iter = 1;
                        $.each(responseData.TestCase, function (i, testData) {
                            //alert(testData.Status);
                            var startDate = new Date(testData.StartTime.replace(/([+\-]\d\d)(\d\d)$/, "$1:$2"));
                            var endDate = new Date(testData.EndTime.replace(/([+\-]\d\d)(\d\d)$/, "$1:$2"));
                            var timeDiff = Math.abs(endDate.getTime() - startDate.getTime()) / 1000;
                            testDuration.push(timeDiff);
                            testIteration.push(iter++);
                            runStatus.push(testData.Status);
                            testcaseName.push(testData.Name);
                        });

                        finalData.push(testDuration);
                        finalData.push(testIteration);
                        finalData.push(runStatus);
                        finalData.push(testcaseName);

                        plotChartForExecTime(testDuration, testIteration);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(textStatus);
                    }
                });
                return finalData;
            }

            function plotChartForExecTime(testDuration, testIteration) {
                var options = {
                    title: {
                        text: "Graphical Trend"
                    },
                    chart: {
                        renderTo: 'exec-time-chart'
                    },
                    xAxis: {
                        title: {
                            text: 'Run Iteration'
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    yAxis: {
                        title: {
                            text: 'Duration of run'
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
                    series: [{
                        data: testDuration,
                        name: "Exec trend for latest run",
                        color: "green"
                    }
                    ]
                };
                options.xAxis.categories = testIteration;
                var chart = new Highcharts.Chart(options);
            }

            function CheckBuildStatus(msg) {
                $(".modal-title").html(msg);

                //  $('.loading').css('display', 'block');

                $.ajax({
                    url: './index.aspx/GetRunningBuildDetailsFromTFS',
                    type: 'POST',
                    async: false,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (dataR) {
                        //  $('.loading').css('display', 'none');
                        $('#body').css('display', 'block');
                        var dataT = $.parseJSON(dataR.d);
                        var suiteName = (dataT.buildName);
                        $('#body').html('<div id="trendHeader" class="CSSTableGenerator" style="text-align: center; width: 100%; margin-top: 0px"><b>Suite Name: ' + suiteName + '</b>' + '<b>APK Build Version: ' + buildVersion
                            + '</b><table cellpadding="0" cellspacing="0" border="1" class="display" id="running" style="text-align: left"></table></div>');

                        var table = $('#running').DataTable({
                            "data": dataT.TestRunDetails.TestCaseDetails,
                            "columns": [
                            {
                                "title": "Scenario Name", "data": "TestCaseTitle"
                            },
                            {
                                "title": "Status", "sWidth": "3%",
                                "data": "Outcome"
                            },
                            { "title": "Duration (Minute)", "sWidth": "10%", "data": "ShortDate" },
                            ],
                            "aaSorting": [],
                            //"order": [[1, 'null']],
                            "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                                $('td:eq(2)', nRow).css("color", "Black");
                                switch (aData.Outcome) {
                                    case 'Passed':
                                        $('td:eq(2)', nRow).css("background-color", "green");
                                        $('td:eq(2)', nRow).css("color", "White");
                                        break;
                                    case 'Failed':
                                        $('td:eq(2)', nRow).css("background-color", "red");
                                        $('td:eq(2)', nRow).css("color", "white");
                                        break;
                                }
                            }
                        });
                    }
                });
                $("#myModal").modal({
                    show: true
                });
            }

            function StartANewBuild() {
                $(".modal-title").html("Start Automation Execution");
                $("#body").html("Under developement");
                $("#myModal").modal({
                    show: true
                });
            }

            function CheckBuildURI() {
                var buildStatus = "<%=WebConfigInitializer.ReportPath%>";
                if (buildStatus == null) {
                    $(".modal-title").html("Message from TFS");
                    $("#body").html("No Build Report Available");
                    $("#myModal").modal({
                        show: true
                    });
                }
                else return false;
            }

            function CoverageDetails() {
                $(".modal-title").html("Coverage Details");
                $("#body").html("Under developement");
                $("#myModal").modal({
                    show: true
                });
            }

            function PerformanceDetails() {
                $(".modal-title").html("Performance Summary");
                $("#body").html("Under developement");
                $("#myModal").modal({
                    show: true
                });
            }

            function DeviceMonitoring() {
                $(".modal-title").html("Device Monitoring Report");
                $("#body").html("Under developement");
                $("#myModal").modal({
                    show: true
                });
            }

            function TestSummary() {
                $(".modal-title").html("Test Summary Report");
                $("#body").html("Under developement");
                $("#myModal").modal({
                    show: true
                });
            }

        </script>
    </form>
    <br />
    <br />
    <br />
    <div class="navbar navbar-default navbar-fixed-bottom" style="background-image: url(../images/header.jpg)">
        <div>
            <p class="navbar-text" style="align-content: center">
                Mobile Test Platform - Strategic Innovation Group       
            </p>
        </div>
    </div>

</body>

</html>