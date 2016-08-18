import sys
def ServiceID( list , startHandle, endHandle, index ):
   "Report service info "
   serviceCounter = 0

   for element in list:
     add1 = element[1] << 8
     add2 = element[0]
     serviceFound = add1 + add2
     PRINT("OUTPUT_DATA;"+str(serviceFound)+";"+"SERVICE"+str(index))
     PRINT("OUTPUT_DATA;"+str(startHandle[serviceCounter])+";"+"SERVICE_START_HANDLER"+str(index))
     PRINT("OUTPUT_DATA;"+str(endHandle[serviceCounter])+";"+"SERVICE_END_HANDLER"+str(index))
     serviceCounter=serviceCounter+1
     index = index + 1
   return index

status = ACI_GATT_DISC_ALL_PRIMARY_SERVICES(Connection_Handle=int(sys.argv[1]))
services=[]
index = 0
while (1):
 event = WAIT_EVENT(HCI_VENDOR_EVENT)
 if event.get_param('Ecode').val==ACI_ATT_READ_BY_GROUP_TYPE_RESP_EVENT:
  index = ServiceID(event.get_param('Attribute_Values').val,
                      event.get_param('Attribute_Handles').val,
                      event.get_param('End_Group_Handles').val, index)
  
 if event.get_param('Ecode').val==ACI_GATT_PROC_COMPLETE_EVENT:
  PRINT("OUTPUT;"+str(status)+";"+"DISCOVERED_SERVICES_ENDED")
  break

