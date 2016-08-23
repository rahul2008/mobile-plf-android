import sys


# "Decode Characteristic Property"
   
   #0x02 - Read
   #0x04 - Write Without Response
   #0x08 - Write
   #0x10 - Notify
   #0x20 - Indicate

   
def SaveCharacteristicsInfo(attributeHandle, attributeValues,counter):
   "Decode Characteristics"
  
   index = 0
   for handle in attributeHandle:
      
      element = attributeValues[index]
      uuid_add1 = element[4] << 8
      uuid_add2 = element[3]
      uuidChar = uuid_add1 + uuid_add2
      add1_CharValueHandle = element[2] << 8
      add2_CharValueHandle = element[1]
      charValueHandle = add1_CharValueHandle + add2_CharValueHandle
      attribute = element[0]

      PRINT("OUTPUT_DATA;"+str(handle)+";"+"HANDLE"+str(counter))
      PRINT("OUTPUT_DATA;"+str(uuidChar)+";"+"UUID"+str(counter))
      PRINT("OUTPUT_DATA;"+str(charValueHandle)+";"+"CHAR_VALUE_HANDLER"+str(counter))
      PRINT("OUTPUT_DATA;"+str(attribute)+";"+"ATTRIBUTE"+str(counter))
      counter = counter + 1 
      index = index + 1
   return counter


ACI_GATT_DISC_ALL_CHAR_OF_SERVICE(Connection_Handle=int(sys.argv[1]),
                                  Start_Handle = int(sys.argv[2]),# 0x000C, 
                                  End_Handle = int(sys.argv[3]) ) #0x0017 Glucose
counter =0
while(1):                                  
 event = WAIT_EVENT(HCI_VENDOR_EVENT, timeout = 5)
 
 if event.get_param('Ecode').val==ACI_ATT_READ_BY_TYPE_RESP_EVENT:

  counter = SaveCharacteristicsInfo(event.get_param('Attribute_Handles').val,
                            event.get_param('Attribute_Values').val,counter)
  
 if event.get_param('Ecode').val==ACI_GATT_PROC_COMPLETE_EVENT:
  PRINT("OUTPUT;0;COMPLETE_CHARACTERISTICS_DISCOVERTY")
  break
