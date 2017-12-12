import sys


#Add a Characteristic (Notify properties)
if int(sys.argv[2])==0x01:
 Status,CharHandle=ACI_GATT_ADD_CHAR(Service_Handle = int(sys.argv[1]), #ServHandle 
                                    Char_UUID_Type=int(sys.argv[2]), #0x01, 
                                    Char_UUID_16=int(sys.argv[3]), #0xA002,
                                    Char_Value_Length=int(sys.argv[4]), #20, 
                                    Char_Properties=int(sys.argv[5]), #0x10, #Only Notification
                                    Security_Permissions=int(sys.argv[6]), #0x00,
                                    GATT_Evt_Mask=int(sys.argv[7]),#0x01, 
                                    Enc_Key_Size=int(sys.argv[8]),#0x07,
                                    Is_Variable=int(sys.argv[9]))#0x01)      

elif int(sys.argv[2])==0x02:	
 UUID128string=str(sys.argv[3])
 UUID128 = eval('[' + UUID128string + ']')
 Status,CharHandle=ACI_GATT_ADD_CHAR(Service_Handle = int(sys.argv[1]), #ServHandle 
                                    Char_UUID_Type=int(sys.argv[2]), #0x01, 
                                    Char_UUID_128=UUID128, #0xA002,
                                    Char_Value_Length=int(sys.argv[4]), #20, 
                                    Char_Properties=int(sys.argv[5]), #0x10, #Only Notification
                                    Security_Permissions=int(sys.argv[6]), #0x00,
                                    GATT_Evt_Mask=int(sys.argv[7]),#0x01, 
                                    Enc_Key_Size=int(sys.argv[8]),#0x07,
                                    Is_Variable=int(sys.argv[9]))#0x01)   
									
PRINT("OUTPUT_DATA;"+str(CharHandle)+";"+"charHandler")									   
PRINT("OUTPUT;"+str(Status)+";"+"ACI_GATT_ADD_CHARACTERISTIC")

#0x01 uuid16 0x02 uuid128
#0x01 broadcast, 0x02 read, 0x04 write w/o resp, 0x08 write, 0x10 notify, 0x20 indicate, 0x40, auth signed writes, 0x80 extended prop

#0x00 None, 0x01 Authen_read,0x02 Author_read, 0x04 Encry_read, 0x08 Authen_write, 0x10 Author_write, 0x20 encry_write
#0x01 GATT_NOTIFY_ATTRIBUTE_WRITE,  0x02 GATT_NOTIFY_READ_REQ_AND_WAIT_FOR_APP_RESPONSE 0x04 GATT_NOTIFY_WRITE_REQ_AND_WAIT_FOR_APP_RESPONSE

#0x00 fixed_length 0x01 variable_length