import sys

status = ACI_GAP_START_CONNECTION_UPDATE (Connection_Handle=int(sys.argv[1]),
                                   Conn_Interval_Min=int(sys.argv[2]),
                                   Conn_Interval_Max=int(sys.argv[3]),
                                   Conn_Latency = int(sys.argv[4]),
                                   Supervision_Timeout=int(sys.argv[5]),
                                   Minimum_CE_Length = int(sys.argv[6]),
                                   Maximum_CE_Length = int(sys.argv[7]))
   
                            
event = WAIT_EVENT(HCI_LE_META_EVENT, timeout = 5)
if event.get_param('Subevent_Code').val==HCI_LE_CONNECTION_UPDATE_COMPLETE_EVENT:
 PRINT("OUTPUT;"+str(event.get_param('Status').val)+";"+"CONNECTION_UPDATED")
