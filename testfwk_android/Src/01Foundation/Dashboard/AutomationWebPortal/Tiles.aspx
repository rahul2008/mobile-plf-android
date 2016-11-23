<!--
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 -->
<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Tiles.aspx.cs" Inherits="Philips.H2H.Automation.Dashboard.Tiles" %>
<%@ Import Namespace="Philips.H2H.Automation.Dashboard" %>  

<!DOCTYPE html>
<html>
<head lang="en" runat="server">
    <meta charset="UTF-8">
    <link rel='shortcut icon' type='image/x-icon' href='favicon.ico' />
    <title>Automation Portal</title>
<%--    <meta http-equiv="Refresh" content="3600" />--%>
         <link href="css/bootstrap.min.css" rel="stylesheet">

    <link href="metroUI/css/metro.css" rel="stylesheet">
    <link href="metroUI/css/metro-icons.css" rel="stylesheet">
    <link href="metroUI/css/docs.css" rel="stylesheet">

    <style>
        .iconStatus {
            font-size: 34px;
            height: 64px;
            left: 50%;
            margin-left: 100px;
            margin-top: 35px;
            position: absolute;
            text-align: center;
            top: 50%;
            width: 64px;
        }

        .firstTileFont {
            font-size: 18px;
            font-weight: 300;
        }

        .secondTileFont {
            font-size: 18px;
            font-weight: 300;
        }
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
    </style>
</head>
<body onload="init()">
    <% Response.WriteFile("~/header.html");%> 
   <%-- <div class="container page-content">
    </div>--%>
    <div id="trendHeader" class="CSSTableGenerator" style="text-align: center; width: 100%; margin-top: 0px">
        <h3><%= WebConfigInitializer.pageTitle %> <%=suiteName%> Execution Trend</h3>
    </div>
    <div id="testRunDetailSection" class="tile-container" style="margin-left: 1%;align-content:space-between"></div>
    <div id="container" class="tile-container" style="width:100%;height:0px"></div>
    
    <script src="metroUI/js/jquery-2.1.3.min.js"></script>
    <script src="metroUI/js/metro.js"></script>
    <script src="metroUI/js/docs.js"></script>
    <script src="js/libs/highcharts.js"></script>
    <script src="js/libs/exporting.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            loadTileData();
            setInterval(function(){
                loadTileData() // this will run after every 1 hour
            }, 3600000);
        });

        function loadTileData(){
            var dateJsonObj = [];
            var totalTimeJsonObj = [];
            var totalPassedTestCount = [];

            $('#projectTitle').html("<%=  WebConfigInitializer.ProjectTitle %>");
            $.ajax({
                url: './Tiles.aspx/GetTestRunDetails',
                //data: JSON.stringify(obj),
                type: 'POST',
                async: false,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (dataR) {
                    var dataJ = $.parseJSON(dataR.d);
                    $.each(dataJ, function (i, v) {
                        if (i == 10) {
                            return false;
                        }
                        dateJsonObj.push(v.DateOfTestExecution);
                        var totalTimeArray = {};
                        totalTimeArray["y"] = v.TotalPassedTests;//total passed test cases count
                        totalTimeArray["testCount"] = v.TotalTestCases;
                        totalTimeArray["failed"] = v.TotalFailedTests;
                        totalTimeArray["totalExecutionTime"] = v.TotalBuildExecutionTime;
                        //totalTimeJsonObj.push(totalTimeArray);
                        totalPassedTestCount.push(totalTimeArray);
                    });

                    $('#testRunDetailSection').html("");

                    $.each(dataJ, function (i, v) {
                        if (i == (<%=  WebConfigInitializer.TileCount %>)) {
                            return false;
                        }

                        var colorClass;
                        var mifIcon;                       

                        if (v.TotalFailedTests > 0) {
                            colorClass = 'bg-red';
                            mifIcon = 'mif-thumbs-down';
                        } else {
                            colorClass = 'bg-lightGreen';
                            mifIcon = 'mif-thumbs-up';
                        }
                        $('#testRunDetailSection').append(' <div class="tile-wide ' + colorClass + ' fg-white" data-role="tile" >'
        + '<div class="tile-content iconic" data-effect="slideUpDown" data-bounce="true" id="' + v.BuildNumber + "," + v.Uri + "," + v.FileName + ", "+ v.BuildRunId+ '" onclick="TileDetails(this.id)">'
        + '<span class="iconStatus ' + mifIcon + '"></span>'
            + '<div class="live-slide">'
            + '<span class="tile-label"><h4>Execution Date - ' + v.DateOfTestExecution + '</h4></span><br />'
            + '<span class="tile-label secondTileFont">Build Number - ' + v.BuildConfigNumber + '</span><br />'
            + '<span class="tile-label secondTileFont">Build Duration - ' + v.TotalBuildExecutionTime + ' min</span>'

        + '</div>'
          + '  <div class="live-slide firstTileFont" style="top:0%">'
          + '<span class="tile-label"><h4>Execution Date - ' + v.DateOfTestExecution + '</h4></span><br />'
          + ' <span class="tile-label secondTileFont">Test Run Id -&nbsp;' + v.BuildRunId + '</span><br />'
          + '  <span class="tile-label secondTileFont">Total Scenarios -&nbsp;' + v.TotalTestCases + '</span><br />'
          + ' <span class="tile-label secondTileFont">' + v.TotalPassedTests + ' Passed  and ' + v.TotalFailedTests + ' Failed</span>'
        + '</div>'
        + '    </div>'
    + ' </div>');
                    });

                    var options = {
                        title: {
                            text: "Graphical Trend(Last 10 Executions)"
                        },
                        chart: {
                            renderTo: 'container'
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
                                text: 'Total Scenarios'
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
                            pointFormat: "Passed : {point.y} <br/> Failed: {point.failed} <br/> Total Execution Time : {point.totalExecutionTime}",
                            crosshairs: {
                                width: 1,
                                color: 'gray',
                                dashStyle: 'shortdot'
                            }
                        },
                        series: [{
                            data: totalPassedTestCount.reverse(),
                            name: "Passed",
                            color: "green"
                        }
                        ]
                    };
                    options.xAxis.categories = dateJsonObj.reverse();
                    var chart = new Highcharts.Chart(options);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert(textStatus);
                }
            });
        }

        function TileDetails(id_Path_fileName) {
            var content = id_Path_fileName.split(',');
            var result = confirm("Do you want to see all scenario results of this build? It may take sometime to fetch the details");
            if (result == true) {
                window.location = "/buildDetails.aspx?buildNumber=" + content[0] + "&reportFileUri=" + encodeURIComponent(content[1]) + "&reportFileName=" + content[2] + "&BuildRunId=" + content[3];
            } else {
                return false;
            }
        }
    </script>

<%--    <br />
    <br />
    <br />
    <div class="navbar navbar-default navbar-fixed-bottom" style="background-image: url(../images/header.jpg)">
        <div>
            <p class="navbar-text" style="align-content: center">
                Mobile Test Platform - Strategic Innovation Group       
            </p>
        </div>
    </div> --%>
</body>
</html>
