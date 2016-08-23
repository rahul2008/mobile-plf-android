import sys

#0x01 Peripheral
#0x02 Broadcaster
#0x03 Central
#0x04 Observer

result = ACI_GATT_INIT()
status,_,_,_=  ACI_GAP_INIT(Role=int(sys.argv[1]))


PRINT("OUTPUT;"+str(status)+";"+"ACI_GAP_INIT")
