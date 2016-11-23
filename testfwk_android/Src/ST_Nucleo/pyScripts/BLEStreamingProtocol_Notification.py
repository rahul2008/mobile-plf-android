 import sys
 event=WAIT_EVENT(HCI_VENDOR_EVENT,Ecode=ACI_GATT_ATTRIBUTE_MODIFIED_EVENT, timeout = 10)
 dataReceived = event.get_param('Attr_Data').val
 PRINT("OUTPUT_DATA;"+str(dataReceived)+";"+"ACI_GATT_ADD_SERVICE")
 PRINT("OUTPUT;"+"0"+";"+"ATTRIBUTE_MODIFIED_EVENT")