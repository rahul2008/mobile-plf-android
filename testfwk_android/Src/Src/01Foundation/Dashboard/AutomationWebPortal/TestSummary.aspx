<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="TestSummary.aspx.cs" Inherits="Philips.H2H.Automation.Dashboard.SummaryPage" %>

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
    <link href="css/bootstrap.min.css" rel="stylesheet"/>
    <title>Automation Portal</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="metroUI/js/jquery-2.1.3.min.js"></script>
    <script src="js/libs/highcharts.js"></script>
    <script src="js/libs/exporting.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <!-- datatables -->

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

        .summary-left-value {
            font-size: 28px;
            font-weight: 700;
        }

        .table > tbody > tr > td, .table > tbody > tr > th, .table > tfoot > tr > td,
        .table > tfoot > tr > th, .table > thead > tr > td, .table > thead > tr > th {
            border-top: 1px solid #ddd;
            line-height: 1.42857;
            padding: 4px;
            vertical-align: middle;
        }

        td:nth-child(1) {
            text-align: left;
        }

        td:nth-child(2) {
            text-align: right;
        }

        td > span {
            background-color: #181717;
            border-radius: 50px;
            color: #fff;
            display: inline-block;
            font-size: 18px;
            font-weight: 700;
            line-height: 1;
            min-width: 10px;
            padding: 2px 20px;
            text-align: center;
            vertical-align: baseline;
            white-space: nowrap;
        }

        .panel-heading {
            color: #fff !important;
            background-color: #5cb85c !important;
            border-color: #4cae4c !important;
        }
        .featureContainer{
            border-color: #ddd
        }
    </style>
    <script type="text/javascript">

        $(document).ready(function () {
            loadDataFromXML();
            setInterval(function () {
                loadDataFromXML();// this will run after every 1 hour
            }, 3600000);

            $(document).delegate(".table tr", "click", function () {
                var featureText = $(this).find('td .features').text();
                $("#featureContent").show();
                $("#featureContent").html(featureText);
                $("#homeContent").hide();
            });

            $("#resetButton").click(function () {
                $("#featureContent").hide();
                $("#homeContent").show();
            });
        });

        function loadDataFromXML(planned, actuals) {
            plannedList = [];
            actualsList = [];
            categoryList = [];
            testCountList = [];
            reqCountList = [];
            estimateCountList = [];
            $.ajax({
                type: "GET",
                url: "chartData/summary.xml",
                dataType: "xml",
                success: xmlParser
            });

            function xmlParser(xml) {

                $(xml).find("Suite").each(function () {
                    $('#suiteInfo').append('<tr><td>' + $(this).attr("name") + '<div class="features" style="display:none">' + $(this).find("Description").text() + '</div></td><td class="text-center"><span>' + $(this).find("TestCaseCount").text() + '</span></td></tr>');

                });

                $(xml).find("Test").each(function () {
                    $('#coverageInfo').append('<tr><td>' + $(this).attr("name") + '<div class="features" style="display:none">' + $(this).find("Description").text() + '</div></td><td class="text-center"><span>' + $(this).find("Count").text() + '</span></td></tr>');
                    testCountList.push(parseInt($(this).find("Count").text()));
                });

                $(xml).find("Requirement").each(function () {
                    $('#coverageInfo').append('<tr><td>' + $(this).attr("name") + '<div class="features" style="display:none">' + $(this).find("Description").text() + '</div></td><td class="text-center"><span>' + $(this).find("Count").text() + '</span></td></tr>');
                    reqCountList.push(parseInt($(this).find("Count").text()));
                });

                $(xml).find("Run").each(function () {
                    $('#runInfo').append('<tr><td>' + $(this).attr("name") + '<div class="features" style="display:none">' + $(this).find("Description").text() + '</div></td><td class="text-center"><span>' + $(this).find("Planned").text() + '</span></td><td class="text-center"><span>' + $(this).find("Actuals").text() + '</span></td></tr>');
                    plannedList.push(parseInt($(this).find("Planned").text()));
                    actualsList.push(parseInt($(this).find("Actuals").text()));
                    categoryList.push($(this).attr("name"));
                });

                $(xml).find("Effort").each(function () {
                    $('#effortInfo').append('<tr><td>' + $(this).attr("name") + '<div class="features" style="display:none">' + $(this).find("Description").text() + '</div></td><td class="text-center"><span>' + $(this).find("Count").text() + '</span></td></tr>');
                    estimateCountList.push(parseInt($(this).find("Count").text()));
                });

                var reqChartTitle = ['Automated Requirement Count', 'Manual Requirement Count'];
                var reqAutomationValues = calculateValuesForPieChart(reqCountList, reqChartTitle);
                var testChartTitle = ['Automated Testcase Count', 'Manual Testcase Count'];
                var testAutomationValues = calculateValuesForPieChart(testCountList, testChartTitle);

                loadChartForRunTypes(plannedList, actualsList, categoryList);
                loadChartForTestCaseSummary(testAutomationValues);
                loadChartForRequirementSummary(reqAutomationValues);
            }
        }

        function calculateValuesForPieChart(elementsList, chartTitle) {
            var total = 0;
            var manual = 0;
            var auto = 0;
            var resultset = [];
            var final = [];
            var percentageList = [];
            //Gets manual and automated values
            if (elementsList[0] > elementsList[1]) {
                total = parseInt(elementsList[0]);
                auto = parseInt(elementsList[1]);
                manual = total - parseInt(elementsList[1]);
            }
            else {
                total = parseInt(elementsList[1]);
                auto = parseInt(elementsList[0]);
                manual = total - parseInt(elementsList[0])
            }
            resultset.push(auto);
            resultset.push(manual);

            $.each(resultset, function (key, value) {
                percentageList.push((value * 100) / total);
            });

            for (var i = 0; i < chartTitle.length; i++) {
                final.push({
                    name: chartTitle[i],
                    y: percentageList[i]
                });
            }
            return final;
        }

        function loadChartForTestCaseSummary(resultset) {
            //Highcharts.getOptions().colors = Highcharts.map(['#5cb85c', '#ce352c'], function (color) {
            //    return {
            //        radialGradient: {
            //            cx: 0.5,
            //            cy: 0.5,
            //            r: 0.5
            //        },
            //        stops: [
            //            [0, color],
            //            [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
            //        ]
            //    };
            //});

            Highcharts.setOptions({
                colors: ['#5cb85c', '#ce352c']
            });
            chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'NumTests',
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false
                },
                title: {
                    text: 'Test Cases'
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                credits: {
                    enabled: false
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: false,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: false,
                        },
                        point: {
                            events: {
                                legendItemClick: function () {
                                    return false;
                                }
                            }
                        },
                        showInLegend: true
                    }
                },
                series: [{
                    type: 'pie',
                    name: 'Percentage',
                    colorByPoint: true,
                    data: resultset
                }]
            });
        }

        function loadChartForRequirementSummary(resultset) {
            chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'NumReq',
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false
                },
                title: {
                    text: 'Requirements'
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                credits: {
                    enabled: false
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: false,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: false,
                        },
                        point: {
                            events: {
                                legendItemClick: function () {
                                    return false;
                                }
                            }
                        },
                        showInLegend: true
                    }
                },
                series: [{
                    type: 'pie',
                    name: 'Percentage',
                    colorByPoint: true,
                    data: resultset
                }]
            });
        }

        function loadChartForRunTypes(planned, actuals, categoryList) {
            chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'NumRuns',
                    type: 'column'
                },
                credits: {
                    enabled: false
                },
                title: {
                    text: 'Comparison - Regresion Vs Smoke'
                },
                xAxis: {
                    categories: categoryList,
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Number of test cases per run type'
                    }
                },
                tooltip: {
                    pointFormat: '<table><tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.1f}</b></td></tr></table>',
                    shared: false,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0,
                        events: {
                            legendItemClick: function () {
                                return false;
                            }
                        }
                    }
                },
                series: [{
                    name: 'Planned',
                    data: planned,
                    color: '#5cb85c',

                }, {
                    name: 'Actuals',
                    data: actuals,
                    color: '#ce352c'
                }]
            });
        }

    </script>
</head>
<body>
    <% Response.WriteFile("~/header.html");%>
    <br />
    <div class="container-fluid">
        <div class="col-md-4">
            <div class="panel panel-primary">
                <div class="panel-heading panel-title"><span class="glyphicon glyphicon-stats"></span>&nbsp;<strong>Suites Information</strong></div>
                <div class="panel-body">
                    <table class="table" style="text-align: center">
                        <tbody id="suiteInfo">
                        </tbody>
                    </table>
                </div>
                <%-- </div>
            <div class="panel panel-primary">--%>
                <div class="panel-heading panel-title"><span class="glyphicon glyphicon-stats"></span>&nbsp;<strong>Test Coverage</strong></div>
                <div class="panel-body">
                    <table class="table">
                        <tbody id="coverageInfo">
                        </tbody>
                    </table>
                </div>
                <%--  </div>
            <div class="panel panel-primary">--%>
                <div class="panel-heading panel-title"><span class="glyphicon glyphicon-stats"></span>&nbsp;<strong>Total number of Runs</strong></div>
                <div class="panel-body">
                    <table class="table">
                        <tbody id="runInfo">
                            <tr>
                                <td>Type</td>
                                <td class="text-center">Planned</td>
                                <td class="text-center">Actual</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="panel-heading panel-title"><span class="glyphicon glyphicon-stats"></span>&nbsp;<strong>Effort Savings(in days)</strong></div>
                <div class="panel-body">
                    <table class="table">
                        <tbody id="effortInfo">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="col-md-6">
                <div id="NumTests" class="col-md-6">
                </div>
                <div id="NumReq" class="col-md-6">
                </div>
                <div id="NumRuns" class="col-md-12">
                </div>
            </div>
            <div class="col-md-6">
                <h4 class="panel-heading">Description<span id="resetButton" class=" indicator glyphicon glyphicon-refresh pull-right"></span></h4>
                <div id="homeContent">List of new features, along with future enhancements which have been planned.</div>
                <div id="featureContent"></div>
            </div>
        </div>
    </div>
    <script src="js/bootstrap.min.js"></script>
    <script>
        $(document).ready(function () {
            $('#projectTitle').html("<%=   WebConfigInitializer.ProjectTitle  %>");

            });

    </script>
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
