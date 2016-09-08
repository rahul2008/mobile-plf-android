def ServiceID( list , startHandle, endHandle ):
   "Report service info "
   serviceCounter = 0

   for element in list:
     add1 = element[1] << 8
     add2 = element[0]
     serviceFound = add1 + add2
     
     stringService = DecodeServiceUUID(serviceFound)
                   
     file.write(stringService
                + "\t 0x"+ format(serviceFound,'04X') + "\t"
                + "\t   0x"+ format(startHandle[serviceCounter],'04X') + "\t"
                + "    0x"+ format(endHandle[serviceCounter],'04X')+ "\t"
                + "\n")
     serviceCounter=serviceCounter+1
     
   return serviceFound

def DecodeServiceUUID(uuid):
   "Decode into string standard UUID"
   stringService=""
   if (uuid==0x1800):
    stringService = "Generic Access Service     "
   elif (uuid==0x1801):
    stringService = "Generic Attribute Service  "
   elif (uuid==0x1808):
    stringService = "Glucose Service            " 
   elif (uuid==0x180A):
    stringService = "Device Information Service " 
   elif (uuid==0x180F):
    stringService = "Battery Service            " 
   else:
    stringService = "Custom Service             " 
   return stringService

def DecodeCharacteristicUUID(uuid):
    stringCharUUID=""
    if (uuid==0x2A18):
     stringCharUUID = "(Glucose Measurement)        "
    elif (uuid==0x2A34):
     stringCharUUID = "(Glucose Measurement Context)"
    elif (uuid==0x2A51):
     stringCharUUID = "(Glucose Feature)            "
    elif (uuid==0x2A52):
     stringCharUUID = "(Record Access Control Point)"
    else:
     stringCharUUID = ""
    return stringCharUUID

def SetAuthonticationRequirement():
 
 status= ACI_GAP_SET_AUTHENTICATION_REQUIREMENT(MITM_Mode=0x01,
                                     OOB_Enable=0x00,
                                     Min_Encryption_Key_Size=0x07,
                                     Max_Encryption_Key_Size=0x10,
                                     Use_Fixed_Pin=0x01,#request pin 1
                                     Fixed_Pin=000004, #NA when Use_Fixed_Pin=0x01
                                     Bonding_Mode=0x01)
 if status !=0x00:
  ERROR('ACI_GAP_SET_AUTHENTICATION_REQUIREMENTCALL FAILED')
  file.write("'ACI_GAP_SET_AUTHENTICATION_REQUIREMENTCALL FAILED' \n" )

def DoPairing():
 
 PRINT('START PAIRING')
 Status = ACI_GAP_SEND_PAIRING_REQ(Connection_Handle=conn_handle,Force_Rebond=0x01)
 if Status!=0x00:
  PRINT('FAILED COMMAND ACI_GAP_SEND_PAIRING_REQ')
  file.write("ERROR PAIRING COMMAND REQUEST!\n" )  

 event = WAIT_EVENT(HCI_VENDOR_EVENT)

 if event.get_param('Ecode').val==ACI_GAP_PASS_KEY_REQ_EVENT:
  PassKey = INSERT_PASS_KEY()
  file.write("PASSKEY "+ str(PassKey)+ " inserted \n") 
  status = ACI_GAP_PASS_KEY_RESP(Connection_Handle=conn_handle, Pass_Key=PassKey)
 if status !=0x00:
  ERROR('ACI_GAP_PASS_KEY_RESP CALL FAILED')
  file.write("PASS KEY RESPONSE FAILED!\n")   

 PRINT('PAIRING COMMAND SENT')
 event = WAIT_EVENT(HCI_VENDOR_EVENT)

 if event.get_param('Ecode').val==ACI_GAP_PAIRING_COMPLETE_EVENT:
  if event.get_param('Status').val==0x00:
   PRINT('PAIRING SUCCESS')
   file.write("Pairing SUCCESS!\n") 
  else:
   ERROR('PAIRING FAILED')
   file.write("Pairing FAILS!\n")

 WAIT_EVENT(HCI_VENDOR_EVENT,Ecode=ACI_GAP_AUTHORIZATION_REQ_EVENT)
 ACI_GAP_AUTHORIZATION_RESP(Connection_Handle=conn_handle, Authorize=0x01) #0x01 authorizeYES
 PRINT('AUTHORIZATION_REQ_EVENT')	
	
	
def SaveCharacteristicsInfo(attributeHandle, attributeValues):
   "Decode Characteristics"
  
   counter  = 0
   for handle in attributeHandle:
    
      element = attributeValues[counter]
      uuid_add1 = element[4] << 8
      uuid_add2 = element[3]
      uuidChar = uuid_add1 + uuid_add2
      PRINT(handle)
      add1_CharValueHandle = element[2] << 8
      add2_CharValueHandle = element[1]
      charValueHandle = add1_CharValueHandle + add2_CharValueHandle
      attribute = element[0]
      counter = counter + 1  
      file.write("\t 0x"+format(handle,'04X')+ "\t\t\t"
                 + " 0x"+format(uuidChar,'04X')
                 + " " +DecodeCharacteristicUUID(uuidChar)
                 + "  \t   0x"+ format(charValueHandle,'04X') + "\t"
                 + "\t "+ DecodeCharProperty(attribute) + "\t"
                 + "\n")
       
   
   return

def DecodeCharProperty(property):
   "Decode Characteristic Property"
   propertyString=""
   if (property==0x0010):
    propertyString = "Notify         "
   elif (property==0x0002):
    propertyString = "Read           "
   elif (property==0x0028):
    propertyString = "Write Indicate "
   else:
    propertyString = "0x"+str(format(property,'04X'))
     
   return propertyString
  
file = open("ReadAllEncryptedCharTest.txt", "w")
HW_RESET()
SET_MODE(3) ## 2 - master / slave large GATT

file.write("-----------------------------------\n" )
file.write("- MASTER INFO\n" )
file.write("-----------------------------------\n" )
 
SET_PUBLIC_ADDRESS(0x0280E100AAAA)
file.write("My Address is 0x0280E100AAAA\n" )
ACI_HAL_SET_TX_POWER_LEVEL(En_High_Power=1,PA_Level = 4)

ACI_GATT_INIT()

status,_,_,_=  ACI_GAP_INIT(Role=PERIPHERAL|CENTRAL)
if status !=0x00:
    ERROR('ACI_GAP_INIT CALL FAILED')
else:
	PRINT('GAP INIT OK!')

status= ACI_GAP_SET_IO_CAPABILITY(IO_Capability=0x02)# 0x02 Keyboard only
file.write("IO_Capability Keyboard only\n" )

if status !=0x00:
    ERROR('ACI_GAP_SET_IO_CAPABILITY CALL FAILED')
else:
    PRINT('Set IO capability as Keyboard only')
 
SetAuthonticationRequirement()
conn_handle=[]
#for i in range (len(listOfDeviceInADV)) :    
status=ACI_GAP_CREATE_CONNECTION(LE_Scan_Interval=0x0010,
                              LE_Scan_Window=0x0010,
                              Peer_Address_Type=0x00,
                              Peer_Address=0xA05000000A, #0xA050000006
                              Conn_Interval_Min=0x32,#0x6c,
                              Conn_Interval_Max=0x46,#0x6c,
                              Conn_Latency=0x00,
                              Supervision_Timeout=0x12C,#0xc80,
                              Minimum_CE_Length=0x000c,
                              Maximum_CE_Length=0x000c)
                              
file.write("--------------------------------------\n" )
file.write("CONNECTION PARAMETERS\n" )     
file.write("--------------------------------------\n" )                         
file.write("Scan Interval           = 0x0010 \n")
file.write("Scan Window             = 0x0010 \n")
file.write("Peer Address Type       = 0x00 \n")
file.write("Peer Address            = 0xA05000000A \n")
file.write("Min Connection Interval = 0x32 \n")
file.write("Max Connection Interval = 0x46\n")
file.write("Connectioin Latency     = 0x00\n")
file.write("Supervision_Timeout     = 0x12C\n")
file.write("Minimum CE Length       = 0x000c\n")
file.write("Maximum CE Length       = 0x000c\n")

file.write("\nTrying to Connect...\n")

conn_handle= 0x00
WAIT_EVENT(HCI_VENDOR_EVENT, Ecode=ACI_GAP_PROC_COMPLETE_EVENT)

while(True):        
    event = WAIT_EVENT(HCI_LE_META_EVENT,Subevent_Code=HCI_LE_CONNECTION_COMPLETE_EVENT )
    if event.get_param('Status').val==0x00:
        PRINT('CONNECTED')
        conn_handle= event.get_param('Connection_Handle').val
        file.write("CONNECTED - Connection Handle = " +str(conn_handle)+ "\n")
        break
    else:
        PRINT('CONNECTION ERROR')
        file.write("CONNECTION ERROR!\n")
        break


DoPairing()
		
file.write("\n--------------------------------------\n" )
file.write("DISCOVERY ALL PRIMARY SERVICES\n" )     
file.write("--------------------------------------\n\n" )  

file.write("\tSERVICEs\t"+
           "\t  UUID\t"+
           "\tSTART HANDLE"+
           "\t  END HANDLE\t"+
           "\n\n")


status = ACI_GATT_DISC_ALL_PRIMARY_SERVICES(Connection_Handle=conn_handle)
services=[]
while (1):
 event = WAIT_EVENT(HCI_VENDOR_EVENT)
 if event.get_param('Ecode').val==ACI_ATT_READ_BY_GROUP_TYPE_RESP_EVENT:
  service = ServiceID(event.get_param('Attribute_Values').val,
                      event.get_param('Attribute_Handles').val,
                      event.get_param('End_Group_Handles').val)
  
 if event.get_param('Ecode').val==ACI_GATT_PROC_COMPLETE_EVENT:
  PRINT("DISCOVERED SERVICES OVER")
  break

file.write("\n--------------------------------------\n" )
file.write("DISCOVERY ALL CHARACTERISTICS  \n" )  
file.write("FOR SERVICE Glucose Service  \n" )    
file.write("--------------------------------------\n\n" )

file.write("\tAttr.Handle\t"+
           "\t  UUID\t\t\t\t"+
           "\tCHAR ATTR. HANDLE "+
           "\tCHAR PROPERTY\t"+
           "\n\n")

ACI_GATT_DISC_ALL_CHAR_OF_SERVICE(Connection_Handle=conn_handle,
                                  Start_Handle = 0x000C, 
                                  End_Handle = 0x0017 ) #Glucose

while(1):                                  
 event = WAIT_EVENT(HCI_VENDOR_EVENT)
 
 if event.get_param('Ecode').val==ACI_ATT_READ_BY_TYPE_RESP_EVENT:

  SaveCharacteristicsInfo(event.get_param('Attribute_Handles').val,
                            event.get_param('Attribute_Values').val)
  
 if event.get_param('Ecode').val==ACI_GATT_PROC_COMPLETE_EVENT:
  PRINT("READ ALL CHARACTERISTICS for service is OVER")
  break

## Request Data reading from characteristic
file.write("\n--------------------------------------\n" )
file.write("TEST READ CHAR VALUE  \n" )  
file.write("--------------------------------------\n\n" )


status= ACI_GATT_READ_CHAR_VALUE(Connection_Handle=conn_handle,Attr_Handle=0x0012)
file.write("Reading 0x0012\t  " )
testDone = False

while(1):
 event = WAIT_EVENT(HCI_VENDOR_EVENT)
 if event.get_param('Ecode').val==ACI_ATT_READ_RESP_EVENT:
  #PRINT('READ VALUE ' + str(event.get_param('Attribute_Value').val))
  #file.write("Reading value : +" + str(event.get_param('Attribute_Value').val))
  file.write("TEST PASSED!  .... OK    \n" )

 if event.get_param('Ecode').val==ACI_GATT_ERROR_RESP_EVENT: 
  file.write("TEST FAILED!  " )
  if(event.get_param('Error_Code').val==0x0F):
   file.write(".... NOK -> REASON: Insufficient Encryption!  \n" )
 if event.get_param('Ecode').val==ACI_GATT_PROC_COMPLETE_EVENT:
  if(testDone==True):
   break
  else:
   ACI_GATT_READ_CHAR_VALUE(Connection_Handle=conn_handle,Attr_Handle=0x0014)
   file.write("Reading 0x0014\t  " )
   testDone = True

file.close()

ACI_GAP_TERMINATE(Connection_Handle = conn_handle, Reason = 0x13) #other end connection termination code

import subprocess
subprocess.call(['notepad.exe', 'ReadAllEncryptedCharTest.txt'])



