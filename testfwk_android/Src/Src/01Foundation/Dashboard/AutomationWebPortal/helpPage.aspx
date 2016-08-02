<!--
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 -->

<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Tiles.aspx.cs" Inherits="Philips.H2H.Automation.Dashboard.helpPage" %>

<%@ Import Namespace="Philips.H2H.Automation.Dashboard" %>

<!DOCTYPE html>
<html>
<head lang="en" runat="server">
    <meta charset="UTF-8">
    <link rel='shortcut icon' type='image/x-icon' href='favicon.ico' />
    <title>Automation Portal</title>    
    <link href="metroUI/css/metro.css" rel="stylesheet">
    <link href="metroUI/css/metro-icons.css" rel="stylesheet">
    <link href="metroUI/css/docs.css" rel="stylesheet">
    <link rel='shortcut icon' type='image/x-icon' href='favicon.ico' />
    <link href="css/bootstrap.min.css" rel="stylesheet"/>

    <style></style>
</head>
<body>
    <% Response.WriteFile("~/header.html");%>
    <h3><%= WebConfigInitializer.pageTitle %> Help</h3>
    <h1>PAGE IS UNDER DEVELOPMENT</h1>
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
