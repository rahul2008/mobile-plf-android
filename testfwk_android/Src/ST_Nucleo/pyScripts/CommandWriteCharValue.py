import sys

status = ACI_GATT_WRITE_CHAR_VALUE(Connection_Handle=int(sys.argv[1]),
                                   Attr_Handle=int(sys.argv[2]),
                                   Attribute_Val_Length=int(sys.argv[3]),#0x1,
                                   Attribute_Val=int(sys.argv[4]))#0xAA)

PRINT("OUTPUT;"+str(status)+";"+"ACI_GATT_WRITE_CHAR")

# Wait for ACI_GATT_PROC_COMPLETE_EVENT
WAIT_EVENT(timeout = 5)
