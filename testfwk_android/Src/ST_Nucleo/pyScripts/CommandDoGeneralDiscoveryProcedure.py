import sys

#HW_RESET()
#SET_MODE(3)
#SET_PUBLIC_ADDRESS()
#ACI_HAL_SET_TX_POWER_LEVEL(En_High_Power=1,PA_Level = 4)

#ACI_GATT_INIT()

#status,_,_,_=  ACI_GAP_INIT(Role=CENTRAL)
#if status !=0x00:
#    ERROR('ACI_GAP_INIT CALL FAILED')

## Start general discovery procedure
Status =  ACI_GAP_START_GENERAL_DISCOVERY_PROC(LE_Scan_Interval=int(sys.argv[1]),#0x10,
                                               LE_Scan_Window=int(sys.argv[2]),#0x10,
											   Own_Address_Type = int(sys.argv[3]),#0x00,
											   Filter_Duplicates = int(sys.argv[4]))#,0x01)  #0x01 Filter enabled - 0x00 filter disabled
if Status !=0x00:
    ERROR('ACI_GAP_START_GENERAL_DISCOVERY_PROC CALL FAILED')    

## Wait for device found event and store the devices addresses and types:
## Expected advertising device name is "test": refer to serverScript.py


#file = open("tmpDebug.txt", "w")
counter = 0
while (True):
    
    event = WAIT_EVENT(timeout = 5)
    if event.event_code==HCI_VENDOR_EVENT and event.get_param('Ecode').val==ACI_GAP_PROC_COMPLETE_EVENT:
     PRINT("OUTPUT;"+"0"+";"+"ACI_GAP_PROC_COMPLETE_EVENT")
     break
    elif event.event_code==HCI_VENDOR_EVENT and event.get_param('Ecode').val==ACI_GAP_DEVICE_FOUND_EVENT:
     address =event.get_param('Address').val
     name=GET_NAME(event.get_param('Data').val)
     type_address =event.get_param('Address_Type').val
     type_event =event.get_param('Event_Type').val
     lenghtData =event.get_param('Length_Data').val
     data = event.get_param('Data').val
     RSSI = event.get_param('RSSI').val
     if(name!= None):
      PRINT("OUTPUT_DATA;"+name+";"+"NameDevice"+str(counter))
     else:
      PRINT("OUTPUT_DATA;"+"Peer Device;"+"NameDevice"+str(counter))
     
     PRINT("OUTPUT_DATA;"+str(address)+";"+"Address"+str(counter))
     PRINT("OUTPUT_DATA;"+str(type_address)+";"+"AddressType"+str(counter))
     PRINT("OUTPUT_DATA;"+str(type_event)+";"+"EventType"+str(counter))
     PRINT("OUTPUT_DATA;"+str(lenghtData)+";"+"DataLenght"+str(counter))
     PRINT("OUTPUT_DATA;"+str(data)+";"+"Data"+str(counter))
     PRINT("OUTPUT_DATA;"+str(RSSI)+";"+"RSSI"+str(counter))
     counter = counter + 1
     #file.write("name: "+ str(name)+"\n"+
     #            "type_address: "+ str(type_address)+"\n"+
     #            "type_event: "+ str(type_event)+"\n"+
     #            "address: "+ str(address)+"\n"+
     #            "lenghtData: "+ str(lenghtData)+"\n"+
     #            "data: "+ str(data)+"\n"+
     #            "RSSI: "+ str(RSSI)+"\n\n")
     

#import subprocess
#subprocess.call(['notepad.exe', 'tmpDebug.txt'])