import sys

ACI_GAP_TERMINATE(Connection_Handle = int(sys.argv[1]), Reason = 0x13) #other end connection termination code
event = WAIT_EVENT(HCI_DISCONNECTION_COMPLETE_EVENT, timeout = 5)
PRINT("OUTPUT;"+str(event.get_param('Status').val)+";"+"ACI_GAP_TERMINATE")