import sys

ACI_GATT_READ_CHAR_VALUE(Connection_Handle=int(sys.argv[1]),Attr_Handle=int(sys.argv[2]) ) #0x0014
#testDone = False
while True:
 event = WAIT_EVENT(HCI_VENDOR_EVENT,timeout=5)
 
 if event.get_param('Ecode').val==ACI_ATT_READ_RESP_EVENT:
  PRINT("OUTPUT_DATA;"+str(event.get_param('Attribute_Value').val)+";"+"READ_VALUE")
 
 if event.get_param('Ecode').val==ACI_GATT_ERROR_RESP_EVENT:
  PRINT("OUTPUT;"+str(event.get_param('Error_Code').val)+";"+"ACI_GATT_ERROR_RESP_EVENT_READCHAR")
  break
   
 if event.get_param('Ecode').val==ACI_GATT_PROC_COMPLETE_EVENT:
  PRINT("OUTPUT;"+"0"+";"+"ACI_GATT_PROC_COMPLETE_EVENT")
  break