import sys

status = ACI_GATT_UPDATE_CHAR_VALUE(Service_Handle=int(sys.argv[1]), #ServHandle, 
                           Char_Handle=int(sys.argv[2]),# CharHandle, 
                           Val_Offset=int(sys.argv[3]),#0x00, 
                           Char_Value_Length = int(sys.argv[4]),#0x01, 
                           Char_Value=int(sys.argv[5]))#0xaa )	   
						   
PRINT("OUTPUT;"+str(status)+";"+"ACI_GATT_UPDATE_CHAR_VALUE")