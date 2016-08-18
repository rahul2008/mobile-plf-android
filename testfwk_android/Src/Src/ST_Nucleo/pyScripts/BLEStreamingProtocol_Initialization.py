
HW_RESET()

status = SET_PUBLIC_ADDRESS(0x00112233445566)
if status!=0x00:
    ERROR('SET_PUBLIC_ADDRESS FAILED')

#Set TX Power
status = ACI_HAL_SET_TX_POWER_LEVEL(En_High_Power=0x01,PA_Level=0x4)
if status!=0x00:
    ERROR('ACI_HAL_SET_TX_POWER_LEVEL FAILED')


#  ACI_GATT_INIT 
if  ACI_GATT_INIT()!=0x00:
    ERROR('ACI_GATT_INIT FAILED')

## ACI_GAP_INIT returns four parameters (refer to UM1755 for detailed API description)
status,_,_,_=  ACI_GAP_INIT(Role=PERIPHERAL)

Status,HCI_Version,HCI_Revision,LMP_PAL_Version,Manufacturer_Name,LMP_PAL_Subversion= HCI_READ_LOCAL_VERSION_INFORMATION()
if Status!=0x00:
	PRINT('FAILED COMMAND HCI_READ_LOCAL_VERSION_INFORMATION')

Status,HCI_Version,HCI_Revision,LMP_PAL_Version,Manufacturer_Name,LMP_PAL_Subversion= HCI_READ_LOCAL_VERSION_INFORMATION()
if Status!=0x00:
	PRINT('FAILED COMMAND HCI_READ_LOCAL_VERSION_INFORMATION')

Status,HCI_Version,HCI_Revision,LMP_PAL_Version,Manufacturer_Name,LMP_PAL_Subversion= HCI_READ_LOCAL_VERSION_INFORMATION()
if Status!=0x00:
	PRINT('FAILED COMMAND HCI_READ_LOCAL_VERSION_INFORMATION')

Status,Service_Handle_BLEStreaming= ACI_GATT_ADD_SERVICE(Service_UUID_Type=0x02,Service_UUID_128=[0xB1,0xC7,0x1B,0x26,0xD4,0x56,0xE9,0xBC,0x31,0x41,0x74,0x40,0xF1,0xFF,0x51,0xA6],Service_Type=0x01,Max_Attribute_Records=0x0F)
if Status!=0x00:
	PRINT('FAILED COMMAND ACI_GATT_ADD_SERVICE')

Status1,Char_HandleRX= ACI_GATT_ADD_CHAR(Service_Handle=Service_Handle_BLEStreaming,Char_UUID_Type=0x02,Char_UUID_128=[0xB1,0xC7,0x1B,0x26,0xD4,0x56,0xE9,0xBC,0x31,0x41,0x74,0x40,0x01,0x00,0x51,0xA6],Char_Value_Length=19,Char_Properties=0x04,Security_Permissions=0x00,GATT_Evt_Mask=0x01,Enc_Key_Size=0x07,Is_Variable=0x01)
PRINT(str(Char_HandleRX)+" -> 0x001")
Status2,Char_HandleRXAck= ACI_GATT_ADD_CHAR(Service_Handle=Service_Handle_BLEStreaming,Char_UUID_Type=0x02,Char_UUID_128=[0xB1,0xC7,0x1B,0x26,0xD4,0x56,0xE9,0xBC,0x31,0x41,0x74,0x40,0x02,0x00,0x51,0xA6],Char_Value_Length=19,Char_Properties=0x10,Security_Permissions=0x00,GATT_Evt_Mask=0x01,Enc_Key_Size=0x07,Is_Variable=0x01)
PRINT(str(Char_HandleRXAck)+" -> 0x002")
Status3,Char_HandleTX= ACI_GATT_ADD_CHAR(Service_Handle=Service_Handle_BLEStreaming,Char_UUID_Type=0x02,Char_UUID_128=[0xB1,0xC7,0x1B,0x26,0xD4,0x56,0xE9,0xBC,0x31,0x41,0x74,0x40,0x03,0x00,0x51,0xA6],Char_Value_Length=19,Char_Properties=0x10,Security_Permissions=0x00,GATT_Evt_Mask=0x01,Enc_Key_Size=0x07,Is_Variable=0x01)
PRINT(str(Char_HandleTX)+" -> 0x003")
Status4,Char_HandleTXAck= ACI_GATT_ADD_CHAR(Service_Handle=Service_Handle_BLEStreaming,Char_UUID_Type=0x02,Char_UUID_128=[0xB1,0xC7,0x1B,0x26,0xD4,0x56,0xE9,0xBC,0x31,0x41,0x74,0x40,0x04,0x00,0x51,0xA6],Char_Value_Length=19,Char_Properties=0x04,Security_Permissions=0x00,GATT_Evt_Mask=0x01,Enc_Key_Size=0x07,Is_Variable=0x01)
PRINT(str(Char_HandleTXAck)+" -> 0x004")
Status5,Char_HandleProtCFG= ACI_GATT_ADD_CHAR(Service_Handle=Service_Handle_BLEStreaming,Char_UUID_Type=0x02,Char_UUID_128=[0xB1,0xC7,0x1B,0x26,0xD4,0x56,0xE9,0xBC,0x31,0x41,0x74,0x40,0x05,0x00,0x51,0xA6],Char_Value_Length=3,Char_Properties=0x02,Security_Permissions=0x00,GATT_Evt_Mask=0x01,Enc_Key_Size=0x07,Is_Variable=0x01)
PRINT(str(Char_HandleProtCFG)+" -> 0x005")

Status6 = ACI_GATT_UPDATE_CHAR_VALUE(Service_Handle=Service_Handle_BLEStreaming, Char_Handle=Char_HandleProtCFG, Val_Offset=0x00, Char_Value_Length = 3, Char_Value=[0x03,20,20])
if Status!=0x00:
	PRINT('FAILED COMMAND ACI_GATT_ADD_CHAR')

#Advertising_Type: 0x00 ADV_IND 0x02 ADV_SCAN_IND  0x03 ADV_NONCONN_IND
#Own_Address_Type: 0x00 public  0x01 random
#Advertising_Filter_Policy: 0x00 from any	
#Put in Discoverable Mode with Name = 'Test' = [0x08,0x74,0x65,0x73,0x74](Add prefix 0x08 to indicate the AD type Name)
Status7 = ACI_GAP_SET_DISCOVERABLE(Advertising_Type= 0x00,
                                 Advertising_Interval_Min= 0x20,#0x20, N*0.625ms
                                 Advertising_Interval_Max=0x1000,
                                 Own_Address_Type=0x00,
                                 Advertising_Filter_Policy = 0x00,
                                 Local_Name_Length=0x05,#0x05,
                                 Local_Name=[0x08,0x74,0x65,0x73,0x74],							 
                                 Slave_Conn_Interval_Min = 0x0006,
                                 Slave_Conn_Interval_Max = 0x0008)


if(Status1==0 and Status2==0 and Status3==0 and Status4==0 and Status5==0 and Status6==0 and Status7==0):
 PRINT("OUTPUT_DATA;"+str(Service_Handle_BLEStreaming)+";"+"BLE_STREAMING_SERVICE")
 PRINT("OUTPUT_DATA;"+str(Char_HandleRX)+";"+"HANDLE_RX")
 PRINT("OUTPUT_DATA;"+str(Char_HandleRXAck)+";"+"HANDLE_RXACK")
 PRINT("OUTPUT_DATA;"+str(Char_HandleTX)+";"+"HANDLE_TX")
 PRINT("OUTPUT_DATA;"+str(Char_HandleTXAck)+";"+"HANDLE_TXACK")
 PRINT("OUTPUT_DATA;"+str(Char_HandleProtCFG)+";"+"HANDLE_PROTCFG")
 PRINT("OUTPUT;"+"0"+";"+"STREAMING_PROTOCAL_INIT")
else:
 PRINT("OUTPUT;"+"1"+";"+"STREAMING_PROTOCAL_INIT")						 


event=WAIT_EVENT(HCI_VENDOR_EVENT,Ecode=ACI_GATT_ATTRIBUTE_MODIFIED_EVENT)
dataReceived = event.get_param('Attr_Data').val
PRINT("OUTPUT_DATA;"+str(dataReceived)+";"+"DATA_1") 
 
event=WAIT_EVENT(HCI_VENDOR_EVENT,Ecode=ACI_GATT_ATTRIBUTE_MODIFIED_EVENT, timeout=10)
dataReceived = event.get_param('Attr_Data').val
PRINT("OUTPUT_DATA;"+str(dataReceived)+";"+"DATA_2")

event=WAIT_EVENT(HCI_VENDOR_EVENT,Ecode=ACI_GATT_ATTRIBUTE_MODIFIED_EVENT, timeout=10)
dataReceived = event.get_param('Attr_Data').val
PRINT("OUTPUT_DATA;"+str(dataReceived)+";"+"DATA_3")

event=WAIT_EVENT(HCI_VENDOR_EVENT,Ecode=ACI_GATT_ATTRIBUTE_MODIFIED_EVENT, timeout=10)
dataReceived = event.get_param('Attr_Data').val
PRINT("OUTPUT_DATA;"+str(dataReceived)+";"+"DATA_4")
'''	
status = ACI_GATT_UPDATE_CHAR_VALUE(Service_Handle=Service_Handle_BLEStreaming, Char_Handle=Char_HandleRXAck,Val_Offset=0x00, Char_Value_Length = 1, Char_Value=0x40)
status = ACI_GATT_UPDATE_CHAR_VALUE(Service_Handle=Service_Handle_BLEStreaming, Char_Handle=Char_HandleRXAck,Val_Offset=0x00, Char_Value_Length = 1, Char_Value=1)
status = ACI_GATT_UPDATE_CHAR_VALUE(Service_Handle=Service_Handle_BLEStreaming, Char_Handle=Char_HandleTX, Val_Offset=0x00, Char_Value_Length = 10, Char_Value=[0x40,0xFE, 0xFF, 0x07, 0x00, 0x04, 0x00, 0x7B, 0x7D, 0x00])	

packetId = 0
while(True):
 event=WAIT_EVENT(HCI_VENDOR_EVENT,Ecode=ACI_GATT_ATTRIBUTE_MODIFIED_EVENT, timeout = 10)
 dataReceived = event.get_param('Attr_Data').val
 PRINT("PRE-DATA "+str(dataReceived))
 if(event.get_param('Data_Length').val>2):
  status = ACI_GATT_UPDATE_CHAR_VALUE(Service_Handle=Service_Handle_BLEStreaming, Char_Handle=Char_HandleRXAck,Val_Offset=0x00, Char_Value_Length = 1, Char_Value=(dataReceived[0]))
  if(dataReceived[0]==0):
   packetId = 63
  else:
   packetId = dataReceived[0]-1
  status = ACI_GATT_UPDATE_CHAR_VALUE(Service_Handle=Service_Handle_BLEStreaming, Char_Handle=Char_HandleTX, Val_Offset=0x00, Char_Value_Length = 10, Char_Value=[packetId,0xFE, 0xFF, 0x07, 0x00, 0x04, 0x00, 0x7B, 0x7D, 0x00])	
'''