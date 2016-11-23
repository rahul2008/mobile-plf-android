import sys

## Start general discovery procedure
Status =  ACI_GAP_START_GENERAL_DISCOVERY_PROC(LE_Scan_Interval=0x10,
											   LE_Scan_Window=0x10)
if Status !=0x00:
     PRINT("OUTPUT;"+str(event.get_param('Status').val)+";"+"START_GENERAL_DISCOVERY_PROC")
     sys.exit()	 

listOfDeviceInADV=[]
while (True):
    
    event = WAIT_EVENT()
    if event.event_code==HCI_VENDOR_EVENT and event.get_param('Ecode').val==ACI_GAP_PROC_COMPLETE_EVENT:
        break
    elif (event.event_code==HCI_VENDOR_EVENT and event.get_param('Ecode').val==ACI_GAP_DEVICE_FOUND_EVENT and event.get_param('Event_Type').val != 0x4) :  #SCAN_RSP    :
            name=GET_NAME(event.get_param('Data').val)
            if name==None:
                continue

            #Check Device name in advertising message
            if name=='test':
                #type_address, address = GET_ADDRESS(event.get_param('Data').val)               
                type_address =event.get_param('Address_Type').val
                address =event.get_param('Address').val                
                if type_address!=None and address!=None:
                    #If the device is not already inserted, insert it in the list of device in advertising
                    if ([type_address,address] in listOfDeviceInADV)==False:
                        listOfDeviceInADV.insert(0,[type_address,address])
                        
    elif event.event_code==HCI_LE_META_EVENT and event.get_param('Subevent_Code').val==HCI_LE_ADVERTISING_REPORT_EVENT and event.get_param('Event_Type').val[0] != 0x4:  #SCAN_RSP    :
        name=GET_NAME(event.get_param('Data').val[0])
        if name==None:
            continue

        #Check Device name in advertising message
        if name=='test':               
            type_address =event.get_param('Address_Type').val[0]
            address =event.get_param('Address').val[0]              
            if type_address!=None and address!=None:
                #If the device is not already inserted, insert it in the list of device in advertising
                if ([type_address,address] in listOfDeviceInADV)==False:
                    listOfDeviceInADV.insert(0,[type_address,address])
        
conn_handle=[]
PRINT('Nun. Device in ADV= ' + str(len(listOfDeviceInADV)))
	 
	 
	 
 PRINT("OUTPUT;"+str(event.get_param('Status').val)+";"+"START_GENERAL_DISCOVERY_PROC")
