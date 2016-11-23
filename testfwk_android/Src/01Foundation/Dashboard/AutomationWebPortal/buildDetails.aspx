<!--
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 -->
<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="buildDetails.aspx.cs" Inherits="Philips.H2H.Automation.Dashboard.buildDetails" %>
<%@ Import Namespace="Philips.H2H.Automation.Dashboard" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

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
    </style>
</head>

<body onload="init()">
    <% Response.WriteFile("~/header.html");%>

    <form runat="server">
       
        <br />
        <div class="container" >
            <asp:LinkButton ID="btnDownload"
                runat="server"
                class="btn btn-success"
                Text="Download Test Report"
                OnClick="btnDownload_Click" style="float:right" title="Download Test Report">
                <span class="glyphicon glyphicon-download-alt" style="top: 2px;" aria-hidden="true"></span>  Test Report
            </asp:LinkButton>
        </div>
        <div id="demo" class="container"></div>

        <script src="metroUI/js/docs.js"></script>
        <script type="text/javascript" charset="utf8" src="metroUI/js/jquery-2.1.3.min.js"></script>
        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>
        <!-- DataTables -->
        <script type="text/javascript" charset="utf8" src="js/dataTables/jquery.dataTables.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#projectTitle').html("<%=  WebConfigInitializer.ProjectTitle %>");
                    $.ajax({
                        url: './buildDetails.aspx/GeBuildTestRunData',
                        type: 'POST',
                        async: false,
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        success: function (dataR) {
                            var dataT = $.parseJSON(dataR.d);
                            var buildFinishTime = (dataT.buildFinishTime);
                            $('#demo').html('<div id="trendHeader" class="CSSTableGenerator" style="text-align: center; width: 100%; margin-top: 0px"><b>Build Execution Time: ' + buildFinishTime + '</b>'
                                + '<table cellpadding="0" cellspacing="0" border="1" class="display" id="example" style="text-align: left"></table></div>');

                            var table = $('#example').DataTable({
                                "data": dataT.TestRunDetails.TestCaseDetails,
                                "columns": [
                                {
                                    "className": 'details-control',
                                    "orderable": false,
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
            });            

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

