import sys

data = map(int,sys.argv[5].split(","))

status = ACI_GATT_UPDATE_CHAR_VALUE(Service_Handle=int(sys.argv[1]), #ServHandle, 
                           Char_Handle=int(sys.argv[2]),# CharHandle, 
                           Val_Offset=int(sys.argv[3]),
                           Char_Value_Length = int(sys.argv[4]), 
                           Char_Value=data)	   
						   
PRINT("OUTPUT;"+str(status)+";"+"ACI_GATT_UPDATE_CHAR_VALUE")