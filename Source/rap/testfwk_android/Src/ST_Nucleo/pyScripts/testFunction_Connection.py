import sys
conn_handle=[]
 

status=ACI_GAP_CREATE_CONNECTION(LE_Scan_Interval=int(sys.argv[1]),
                                 LE_Scan_Window=int(sys.argv[2]),
                                 Peer_Address_Type=int(sys.argv[3]),
                                 Peer_Address=long(sys.argv[4]), #0xA050000006
                                 Conn_Interval_Min=int(sys.argv[5]),#0x6c,
                                 Conn_Interval_Max=int(sys.argv[6]),#0x6c,
                                 Conn_Latency=int(sys.argv[7]),
                                 Supervision_Timeout=int(sys.argv[8]),#0xc80,
                                 Minimum_CE_Length=int(sys.argv[9]),
                                 Maximum_CE_Length=int(sys.argv[10]))

conn_handle = 0x00
event = WAIT_EVENT(HCI_VENDOR_EVENT,timeout=5, Ecode=ACI_GAP_PROC_COMPLETE_EVENT)
event = WAIT_EVENT(HCI_LE_META_EVENT,timeout=5, Subevent_Code=HCI_LE_CONNECTION_COMPLETE_EVENT )
if event.get_param('Status').val==0x00:
 conn_handle= event.get_param('Connection_Handle').val
 PRINT("OUTPUT_DATA;"+str(conn_handle)+";"+"connectionHandler")

PRINT("OUTPUT;"+str(event.get_param('Status').val)+";"+"HCI_LE_CONNECTION_COMPLETE_EVENT")
 


