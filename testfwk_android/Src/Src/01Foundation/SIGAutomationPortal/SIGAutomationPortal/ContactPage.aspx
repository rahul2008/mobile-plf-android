<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="ContactPage.aspx.cs" Inherits="SIGAutomationPortal.ContactPage" %>
<asp:Content ID="Content1" ContentPlaceHolderID="headContent" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
    <div class="content-wrapper">
        <div class="nav-tabs-custom">
            <ul class="nav nav-tabs">
                <li class="active"><a href="#usquad_info" data-toggle="tab">About SIG</a> </li>
                <li class=""><a href="#version_info" data-toggle="tab">Version Info</a> </li>
                <li class=""><a href="#contact_info" data-toggle="tab">Contact Us</a> </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="sig_info">
                    <br />
                    <p>
                        &nbsp;<b>Unified Software Quality Dashboard </b>(<b> U-SQuaD</b>, in short) is designed
                        to be the single point of source for program teams to provide <i>One View</i> of
                        multiple quality metrics of a program
                        <br />
                        &nbsp;such as...
                        <ul>
                            <li>PR Status (Source: Ton Mass's Defect metrics workbook)</li>
                            <li>Verification Status</li>
                            <li>Documentation Status</li>
                            <li>Risks/Key Issues</li>
                            <li>Resource Availability</li>
                            <li>Planning/Milestones</li>
                            <li>STT, Auto-Teima and PDFCmp Results (Source: Tom Geraedts's <a href = "http://nlybstqvp4ms177/mr-regression-dashboard/" target="_blank">Regression Dashboard</a> )</li>
                            <li>MEBEF</li>
                            <li>Automation Results</li>
                        </ul>
                    </p>
                    <p>
                        &nbsp;The dashboard is intended to provide real time update on various aspects at
                        SW and Program level.  <a href="http://mrdevwiki.best.ms.philips.com/mrwiki/index.php/USQuaD" target="_blank">USQuaD Wiki Page</a>
                        <br />
                    </p>
                    <iframe width="600" height="430" src="//www.cincopa.com/media-platform/iframe.aspx?fid=AYJA0gMHBrQD"
                        frameborder="0" allowfullscreen scrolling="yes"></iframe>
                    <noscript>
                        <span>Dashboard</span><span>Usquad Dashboard pics</span><span>USQuaD Summay Page</span><span>originaldate</span><span>
                            1/1/0001 6:00:00 AM</span><span>width</span><span> 1903</span><span>height</span><span>
                                1022</span><span>PR Detatils view</span><span>originaldate</span><span> 1/1/0001 6:00:00
                                    AM</span><span>width</span><span> 1899</span><span>height</span><span> 1077</span><span>Verification
                                        Details view</span><span>originaldate</span><span> 1/1/0001 6:00:00 AM</span><span>width</span><span>
                                            1726</span><span>height</span><span> 688</span><span>Risk Details view</span><span>originaldate</span><span>
                                                1/1/0001 6:00:00 AM</span><span>width</span><span> 927</span><span>height</span><span>
                                                    203</span><span>Documentation Details view</span><span>originaldate</span><span> 1/1/0001
                                                        6:00:00 AM</span><span>width</span><span> 990</span><span>height</span><span> 1028</span><span>Resource
                                                            Details View</span><span>originaldate</span><span> 1/1/0001 6:00:00 AM</span><span>width</span><span>
                                                                957</span><span>height</span><span> 658</span><span>Scope, Schedule and Delivery milestones
                                                                    Details</span><span>originaldate</span><span> 1/1/0001 6:00:00 AM</span><span>width</span><span>
                                                                        812</span><span>height</span><span> 923</span><span>STT, Auto-Teima and PDFCMP Test
                                                                            Details</span><span>originaldate</span><span> 1/1/0001 6:00:00 AM</span><span>width</span><span>
                                                                                1096</span><span>height</span><span> 615</span><span>MEBEF Dashboard</span><span>originaldate</span><span>
                                                                                    1/1/0001 6:00:00 AM</span><span>width</span><span> 1534</span><span>height</span><span>
                                                                                        632</span><span>Automation Test Results per Suite</span><span>originaldate</span><span>
                                                                                            1/1/0001 6:00:00 AM</span><span>width</span><span> 1690</span><span>height</span><span>
                                                                                                1066</span><span>Automation Test Results per Swid</span><span>originaldate</span><span>
                                                                                                    1/1/0001 6:00:00 AM</span><span>width</span><span> 1096</span><span>height</span><span>
                                                                                                        531</span></noscript>
                    <br />
                    &nbsp;For any questions on USQuaD or any other feedback/comments, please feel free
                    to write to <a href="mailto:Savitha.puttaswamy.gowda@philips.com">Savitha P</a>.
                    We would be happy to hear from you!
                </div>
                <div class="tab-pane" id="version_info">
                    <h3>
                        &nbsp;USQuaD Version Info
                    </h3>
                    <p>
                        &nbsp;<b>USQuaD V1.0 :</b>
                        <br />
                        &nbsp;&nbsp;&nbsp;&nbsp;First version of Unified Software Quality Dashboard contains following key features:
                        <ul>
                            <li>Dashboard has Lion2, Lion3, Atlantis cdas and Atlantis ddas  program results</li>
                            <li>Each program has Automation, STT, Teima, PDF Compare, MEBEF and Defects Status results</li>
                            <li>There are four blocks, which holds the summary their corresponding program results</li>
                            <li>On clicking checkout results more details of the execution results is shown</li>
                            <li>For Automation results, there are suite and swid based results</li>
                            <li>STT, Teima and PDF Compare results are shown in separate tabs</li>
                            <li>MEBEF Dashboard is shown on selecting “Checkout MEBEF numbers” button</li>
                            <li>Defect status block shows graphs related to PMI score, FCWiseOpen defects, Defects inflow Vs Outflow and FCWiseData charts</li>
                        </ul>
                        <br />
                        &nbsp;<b>USQuaD V1.1 :</b>
                        <br />
                        &nbsp;&nbsp;&nbsp;&nbsp;Aspects incorporated into Program Dashboard v1.1 are:
                        <ul>
                            <li>Better UI ( solid block implementation of the previous version is now converted into a more user friendly version.)</li>
                            <li>Summarises important data of the program in the summary grids of the dashboard</li>
                            <li>Additional bug fixes identified during usage.</li>
                        </ul>
                        <br />
                        &nbsp;<b>USQuaD V1.2 :</b>
                        <br />
                        &nbsp;&nbsp;&nbsp;&nbsp;Aspects incorporated into Program Dashboard v1.2 are:
                        <ul>
                            <li>Performance enhancements</li>
                            <li>Additional bug fixes identified during usage.</li>
                        </ul>
                    </p>
                </div>
                <div class="tab-pane" id="contact_info">
                    <h3>
                        &nbsp;Contact Details
                    </h3>
                    <table>
                        <tr>
                            <td>
                            </td>
                            <td>
                                Savitha P, Technical Specialist
                                <br />
                                <a href="mailto:Savitha.puttaswamy.gowda@philips.com">savitha.puttaswamy.gowda@philips.com</a>
                                <br />
                                <br />
                            </td>
                        </tr>
                        <tr>
                            <td>
                            </td>
                            <td>
                                Santosh Yalawar, Manager
                                <br />
                                <a href="mailto:Santosh.Yalawar@philips.com">santosh.yalawar@philips.com</a>
                                <br />
                                <br />
                            </td>
                        </tr>
                        <tr>
                            <td>
                            </td>
                            <td>
                                Kishore Vinod, Architect
                                <br />
                                <a href="mailto:kishore.vinod@philips.com">kishore.vinod@philips.com</a>
                            </td>
                        </tr>
                    </table>
                    <h3>
                        &nbsp;Contributors</h3>
                    <table>
                        <tr>
                            <td>
                            </td>
                            <td>
                                Tom Geraedts
                                <br />
                                <a href="mailto:tom.geraedts@philips.com">tom.geraedts@philips.com</a>
                                <br />
                                <br />
                            </td>
                        </tr>
                        <tr>
                            <td>
                            </td>
                            <td>
                                Ton Maas
                                <br />
                                <a href="mailto:ton.maas@philips.com">ton.maas@philips.com</a>
                                <br />
                                <br />
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</asp:Content>
