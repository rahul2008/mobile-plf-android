<!--
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 -->
<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="DeviceMonitor.aspx.cs" Inherits="Philips.H2H.Automation.Dashboard.DeviceMonitor" %>
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
    </style>
    <script src="metroUI/js/jquery-2.1.3.min.js"></script>
    <script type ="text/javascript">
    $(document).ready(function () {
        $('#projectTitle').html("<%=   WebConfigInitializer.ProjectTitle  %>");
    });

    </script>
</head>
<body>
     <% Response.WriteFile("~/header.html");%>
    <div id="trendHeader" class="CSSTableGenerator" style="text-align: center; width: 100%; margin-top: 0px">
        <h3><%= WebConfigInitializer.pageTitle %> Device Monitoring</h3>
    </div>
    <div id="deviceMonitor">
        <iframe id="monitorPage" src= "<%= WebConfigInitializer.hostForSTFApp %>" frameborder="0" style="overflow:hidden;overflow-x:hidden;overflow-y:hidden;height:100%;width:100%;position:absolute;top:100px;left:0px;right:0px;bottom:0px"></iframe>
    </div>
    <div class="navbar navbar-default navbar-fixed-bottom" style="background-image: url(../images/header.jpg)">
        <div>
            <p class="navbar-text" style="align-content: center">
                Mobile Test Platform - Strategic Innovation Group       
            </p>
        </div>
    </div>
</body>
    <head>
        <style type="text/css">
        .stf-logo{
            background:url("");
        }
        </style>
    </head>
</html>
