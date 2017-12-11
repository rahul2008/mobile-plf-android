import sys
conn_handle=0
 
status = ACI_GAP_CREATE_CONNECTION(Peer_Address_Type=int(sys.argv[1]),
                          Peer_Address=long(sys.argv[2]))
if status !=0x00:
    ERROR('ACI_GAP_CREATE_CONNECTION CALL FAILED')
    
WAIT_EVENT(HCI_VENDOR_EVENT,timeout = 5,Ecode=ACI_GAP_PROC_COMPLETE_EVENT )

event = WAIT_EVENT(HCI_LE_META_EVENT,timeout = 5, Subevent_Code=HCI_LE_CONNECTION_COMPLETE_EVENT )
if event.get_param('Status').val==0x00:
 conn_handle= event.get_param('Connection_Handle').val
 PRINT("OUTPUT_DATA;"+str(conn_handle)+";"+"connectionHandler")
PRINT("OUTPUT;"+str(event.get_param('Status').val)+";"+"HCI_LE_CONNECTION_COMPLETE_EVENT")

