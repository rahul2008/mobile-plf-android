'  ----------------------------------------------------------------------------
' *  MR Automation : Philips Healthcare Bangalore Center of Competence
' *  --------------------------------------------------------------------------
' *  Serval Automation
' *  --------------------------------------------------------------------------
' *  File:          MapNetworkDrive.vbs
' *  Author:        Prashanth H S
' *  Creation Date: 17-01-2012
' *  --------------------------------------------------------------------------

Option Explicit

'Variables declared.

Dim WSHShell
Dim objNTInfo
Dim computerName
Dim filePath
Dim objXML, Root, node,objNodeList,childNode,nodeNames
DIm bstTstSrvLocation,blrTstSvrLocation,tstSrvMapDrive,bstSvrUID,bstSvrPsd,blrSvrUID,blrSvrPsd
Dim hostName 
Dim command

'Set the system Object information

Set objNTInfo = CreateObject("WinNTSystemInfo")
set objXML = CreateObject("Microsoft.XMLDOM")
Set WSHShell = WScript.CreateObject("WSCript.shell")

'Get the HostName / ComputerName

computerName = lcase(objNTInfo.ComputerName)
Dim strTarget, strPingResults
strTarget = computerName ' hostname
'WScript.Echo computerName
Dim WshExec
'Get the file full path
Dim oFS : Set oFS = CreateObject( "Scripting.FileSystemObject" )
filePath = oFS.GetParentFolderName( WScript.ScriptFullName ) & "\Data\MRConfiguration.xml"

'Load the XML file
 
objXML.async = "false"
objXML.load(filePath)
Set objNodeList = objXML.getElementsByTagName("GenericConfiguration")

'Get the ChildNodes 
For Each childNode In objNodeList
      Set nodeNames = childNode.ChildNodes
Next


'Select the desired nodes from the xml and featch the inner text from that node.

For each  node in nodeNames 
 Select case node.nodename

  case "BST_TestServer_Location"   
        bstTstSrvLocation = node.Text

  case "BLR_TestServer_Location" 
        blrTstSvrLocation = node.Text

  case "TestServer_MapDrive" 
        tstSrvMapDrive= node.Text

  case "BST_TestServer_UID" 
        bstSvrUID = node.Text

  case "BST_TestServer_PWD"  
        bstSvrPsd= node.Text

  case "BLR_TestServer_UID" 
        blrSvrUID = node.Text

  case "BLR_TestServer_PWD" 
        blrSvrPsd = node.Text  
               
 End Select
Next

'Get the computer name / Hostname (only first 5 charecters). 

hostName = Mid(computerName, 1, 5) 

'Select the Desired command (based on the machine hostname)



Select case hostName

 Case "mrban" 
        if (StrComp (blrSvrUID, "") = 0) Then
            command = "net use " & tstSrvMapDrive & " "& blrTstSvrLocation
        Else
            command = "net use " & tstSrvMapDrive & " "& blrTstSvrLocation &  " /USER:" & blrSvrUID &" "& blrSvrPsd & " /persistent:yes" 
        End If
 Case "mrbst"
        if (StrComp (bstSvrUID, "") = 0) Then
            command = "net use " & tstSrvMapDrive & " "& bstTstSrvLocation
        Else
            command = "net use " & tstSrvMapDrive & " "& bstTstSrvLocation &  " /USER:" & bstSvrUID &" "& bstSvrPsd & " /persistent:yes"
        End If       
End Select

'Execute the command.
If len(command) > 0 Then
WSHShell.Exec(command)
end if
WScript.Quit