import sys

HW_RESET()
SET_MODE(3) ## 2 - master / slave large GATT
SET_PUBLIC_ADDRESS(0x0280E100AAAA)
status = ACI_HAL_SET_TX_POWER_LEVEL(En_High_Power=1,PA_Level = 4)
if status != 0x00:
  PRINT("RESULT;"+str(status)+";"+"ACI_HAL_SET_TX_POWER_LEVEL FAILED")
  sys.exit()
status = ACI_GATT_INIT()
if status !=0x00:
 PRINT("RESULT;"+str(status)+";"+"ACI_GATT_INIT FAILED")
 sys.exit()
  
status,_,_,_=  ACI_GAP_INIT(Role=PERIPHERAL|CENTRAL)
if status !=0x00:
 PRINT("RESULT;"+str(event.get_param('Status').val)+";"+"ACI_GAP_INIT FAILED")
 sys.exit()

status= ACI_GAP_SET_IO_CAPABILITY(IO_Capability=0x02)# 0x02 Keyboard only
if status !=0x00:
 PRINT("RESULT;"+str(event.get_param('Status').val)+";"+"ACI_GAP_SET_IO_CAPABILITY CALL FAILED")
 sys.exit()

PRINT("RESULT;"+str(0x00)+";"+"TEST INIT OK")