import sys

#0x00: Display Only
#0x01: Display yes/no
#0x02: Keyboard Only
#0x03: No Input, no output
#0x04: Keyboard display

status= ACI_GAP_SET_IO_CAPABILITY(IO_Capability=int(sys.argv[1]))
PRINT("OUTPUT;"+str(status)+";"+"ACI_GAP_SET_IO_CAPABILITY")