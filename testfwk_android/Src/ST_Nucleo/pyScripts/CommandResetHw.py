import sys

#In which mode BlueNRG started. 
#0x01: Firmware started properly 
#0x02: Updater mode entered with ACI command 
#0x03: Updater mode entered due to bad Blue Flag 
#0x04: Updater mode entered due to IRQ pin 

event= HW_RESET()
PRINT("OUTPUT;"+str(event.get_param('Reason_Code').val)+";"+"HW_RESET")