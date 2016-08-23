import sys

status1=0
status2=0
status3=0

status = ACI_GAP_SEND_PAIRING_REQ(Connection_Handle=int(sys.argv[1]),Force_Rebond=int(sys.argv[3]))

if status1!=0x00:
 PRINT("OUTPUT;"+str(status)+";"+"PAIRING_RESULT_REQUEST")
 sys.exit()

event = WAIT_EVENT(HCI_VENDOR_EVENT,timeout = 5)

if event.get_param('Ecode').val==ACI_GAP_PASS_KEY_REQ_EVENT:
 #PassKey = INSERT_PASS_KEY()
 status1 = ACI_GAP_PASS_KEY_RESP(Connection_Handle=int(sys.argv[1]), Pass_Key=int(sys.argv[2]))

event = WAIT_EVENT(HCI_VENDOR_EVENT, timeout = 5)

if event.get_param('Ecode').val==ACI_GAP_PAIRING_COMPLETE_EVENT:
 status2 = event.get_param('Status').val

#WAIT_EVENT(HCI_VENDOR_EVENT, timeout = 5, Ecode=ACI_GAP_AUTHORIZATION_REQ_EVENT)
PRINT("OUTPUT;"+str(status1+status2)+";"+"ACI_GAP_AUTHORIZATION_RESP RESULT")
status3 = ACI_GAP_AUTHORIZATION_RESP(Connection_Handle=int(sys.argv[1]), Authorize=int(sys.argv[4])) #0x01 authorizeYES
