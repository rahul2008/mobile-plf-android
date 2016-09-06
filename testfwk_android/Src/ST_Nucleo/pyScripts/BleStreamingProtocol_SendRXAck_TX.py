import sys

data = map(int,sys.argv[4].split(","))

status = ACI_GATT_UPDATE_CHAR_VALUE(Service_Handle=int(sys.argv[1]), #ServHandle, 
                           Char_Handle=int(sys.argv[2]),# CharHandle, 
                           Val_Offset=0x00,
                           Char_Value_Length = int(sys.argv[3]), 
                           Char_Value=data)	   

event=WAIT_EVENT(HCI_VENDOR_EVENT,Ecode=ACI_GATT_ATTRIBUTE_MODIFIED_EVENT, timeout = 10) 
PRINT("OUTPUT_DATA;"+str(event.get_param('Attr_Data').val)+";"+"DATA") 
						   
PRINT("OUTPUT;"+str(status)+";"+"ACI_GATT_UPDATE_CHAR_VALUE")
