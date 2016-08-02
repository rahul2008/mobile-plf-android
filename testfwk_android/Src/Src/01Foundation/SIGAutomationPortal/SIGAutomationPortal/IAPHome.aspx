<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="IAPHome.aspx.cs" Inherits="SIGAutomationPortal.IAPHome" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeadContent" runat="server">
     <script src="Scripts/jquery.min.js" type="text/javascript"></script>
    <script src="Scripts/jquery-ui.js" type="text/javascript"></script>
    <style type="text/css">
        .divClass {
            display: none;
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
    <div class="content-wrapper">
            <section class="col-lg-12 connectedSortable">
                <div class="box box-primary">

                    <div class="box-header">
<%--                        <i class="fa fa-gears"></i>--%>
                        <h3 class="box-title">In App Purchase
                        </h3>
                       <%-- <div class="pull-right box-tools">
                            <button class="btn btn-default btn-sm" data-widget="collapse">
                                <i class="fa fa-minus"></i>
                            </button>
                        </div>--%>
                    </div>
                    <div class="box-body">
                        <iframe width="100%" height="800" frameborder="0" scrolling="auto" src="http://161.85.30.146"></iframe>

                        Source: Tom Geraedts
                    </div>
                </div>
            </section>
     </div>
</asp:Content>
