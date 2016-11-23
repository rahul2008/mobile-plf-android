<!--
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 -->
<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="PerformanceMonitor.aspx.cs" Inherits="Philips.H2H.Automation.Dashboard.PerformanceMonitor" %>
<%@ Import Namespace="Philips.H2H.Automation.Dashboard" %>  

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head lang="en" runat="server">
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content="H2H Automation portal"/>
    <meta name="author" content="Philips"/>

    <link rel='shortcut icon' type='image/x-icon' href='favicon.ico' />
    <link href="metroUI/css/metro.css" rel="stylesheet">
    <link href="metroUI/css/metro-icons.css" rel="stylesheet">
    <link href="metroUI/css/docs.css" rel="stylesheet">
    <link href="css/bootstrap.min.css" rel="stylesheet"/>
    <title>Automation Portal</title>

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
        #container .content{

        }

        #container .innercontainer {
            text-align: center;
	        margin-top: 15px;
            margin-bottom:15px;
        }
         #container{
            padding-bottom:75px;
        }
        /*#container .content{
            overflow:scroll;
            height:500px;
        }*/
        #container .innercontainer h3 {
            margin:0;
	        background: #5cb85c;
            padding:10px     0px;
            text-align: center;
        }
        .panel-heading{
                color: #fff !important;
                background-color: #5cb85c !important;
                border-color: #4cae4c !important;
            }
    </style>

    <script src="metroUI/js/jquery-2.1.3.min.js"></script>
    <script src="js/libs/highcharts.js"></script>
    <script src="js/libs/exporting.js"></script>
    <script src="customJs/performancePage.js"></script>

    <script type ="text/javascript">
        $(document).ready(function () {
            $('#projectTitle').html("<%=   WebConfigInitializer.ProjectTitle  %>");
        });
    </script>
</head>
<body>
     <% Response.WriteFile("~/header.html");%>
     <div id="trendHeader" class="CSSTableGenerator" style="text-align: center; width: 100%; margin-top: 0px">
        <h3><%= WebConfigInitializer.pageTitle %> Performance Monitoring</h3>
    </div>
     <div id="container" class="container-fluid">
         <div class="row">
            <div class="col-md-1 "></div>
            <div id ="first-container" class="col-md-5 col-xs-5 innercontainer">
                    <h4 class="panel-heading">Data Usage</h4><div id="first-content" class="content"></div>
                </div>
            <div id ="second-container" class="col-md-5 col-xs-5 innercontainer">
                    <h4 class="panel-heading">Memory Consumption</h4><div id="second-content" class="content"></div>
               </div>
            <div class="col-md-1"></div>
         </div>
         <div class="row">
            <div class="col-md-1"></div>
            <div id ="third-container" class="col-md-5 col-xs-5 innercontainer">
                    <h4 class="panel-heading">Battery Temperature</h4><div id="third-content" class="content"></div>
                </div>
            <div id ="fourth-container" class="col-md-5 col-xs-5 innercontainer">
                    <h4 class="panel-heading">CPU Utilization</h4><div id="fourth-content" class="content"></div>
                </div>
            <div class="col-md-1"></div>
        </div>
    </div>
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
