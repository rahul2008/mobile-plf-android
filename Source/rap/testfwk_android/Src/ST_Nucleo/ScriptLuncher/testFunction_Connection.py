
def connection(testValue):
 HW_RESET()
 SET_MODE(3) ## 2 - master / slave large GATT

 SET_PUBLIC_ADDRESS(0x0280E100AAAA)
 ACI_HAL_SET_TX_POWER_LEVEL(En_High_Power=1,PA_Level = 4)

 ACI_GATT_INIT()

 status,_,_,_=  ACI_GAP_INIT(Role=PERIPHERAL|CENTRAL)
 if status !=0x00:
  ERROR('ACI_GAP_INIT CALL FAILED')
 else:
  PRINT('GAP INIT OK!')
 
 status= ACI_GAP_SET_IO_CAPABILITY(IO_Capability=0x02)# 0x02 Keyboard only

 if status !=0x00:
  ERROR('ACI_GAP_SET_IO_CAPABILITY CALL FAILED')
 else:
  PRINT('Set IO capability as Keyboard only')
     
 conn_handle=[]
 #for i in range (len(listOfDeviceInADV)) :    
 status=ACI_GAP_CREATE_CONNECTION(LE_Scan_Interval=0x0010,
                                  LE_Scan_Window=0x0010,
                                  Peer_Address_Type=0x00,
                                  Peer_Address=0xA05000000A, #0xA050000006
                                  Conn_Interval_Min=0x32,#0x6c,
                                  Conn_Interval_Max=0x46,#0x6c,
                                  Conn_Latency=0x00,
                                  Supervision_Timeout=0x12C,#0xc80,
                                  Minimum_CE_Length=0x000c,
                                  Maximum_CE_Length=0x000c)
 conn_handle = 0x00
 WAIT_EVENT(HCI_VENDOR_EVENT, Ecode=ACI_GAP_PROC_COMPLETE_EVENT)

 while(True):        
  event = WAIT_EVENT(HCI_LE_META_EVENT,Subevent_Code=HCI_LE_CONNECTION_COMPLETE_EVENT )
  if event.get_param('Status').val==0x00:
   PRINT('CONNECTED')
   conn_handle= event.get_param('Connection_Handle').val
   file.write("CONNECTED - Connection Handle = " +str(conn_handle)+ "\n")
   break
  else:
   PRINT('CONNECTION ERROR')
   file.write("CONNECTION ERROR!\n")
   break
 
 return testValue		

