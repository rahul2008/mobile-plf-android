
HW_RESET()
SET_MODE(2)
SET_PUBLIC_ADDRESS(0x0280E1223344)
ACI_HAL_SET_TX_POWER_LEVEL(En_High_Power=1,PA_Level = 4)
ACI_GATT_INIT()
ACI_GAP_INIT(Role=PERIPHERAL)

ACI_GAP_CLEAR_SECURITY_DB()

status= ACI_GAP_SET_IO_CAPABILITY(IO_Capability=0x00) #Display only
if status !=0x00:
    ERROR('ACI_GAP_SET_IO_CAPABILITY CALL FAILED')
else:
    PRINT('Set IO capability as Display only')


#ADD Service/s	
Status,ServHandle=ACI_GATT_ADD_SERVICE(Service_UUID_Type=0x01, 
                 Service_UUID_16=0x180F, 
                 Service_Type=1, 
                 Max_Attribute_Records=5)
				 

if status !=0x00:
 ERROR('ACI_GATT_ADD_SERVICE CALL FAILED')

Status,CharHandle=ACI_GATT_ADD_CHAR( 
             Service_Handle =ServHandle,
             Char_UUID_Type=0x01, 
             Char_UUID_16= 0x2A19, 
             Char_Value_Length=20, 
             Char_Properties=0x02,
             Security_Permissions=0x00,
             GATT_Evt_Mask=0x01,   
             Enc_Key_Size=0x07, 
             Is_Variable=0x01)   
if status !=0x00:
 ERROR('ACI_GATT_ADD_CHAR CALL FAILED') 


Status= ACI_GATT_ADD_CHAR_DESC(Service_Handle = ServHandle,  
                                    Char_Handle=CharHandle, 
                                    Char_Desc_Uuid_Type=0x01,  
                                    Char_UUID_16=0x2904,
                                    Char_Desc_Value_Max_Len=0x0A,
                                    Char_Desc_Value_Length=0x01, 
                                    Char_Desc_Value=0x06,
                                    Security_Permissions=0x00,
                                    Access_Permissions=0x03,
                                    GATT_Evt_Mask = 0x00,
                                    Enc_Key_Size = 0x07,   
                                    Is_Variable = 0x00)  
#if Status!=0x00:
#    ERROR('ACI_GATT_ADD_CHAR_DESC FAILED') 									
Status = ACI_GATT_ADD_CHAR_DESC(Service_Handle = ServHandle,  
                                    Char_Handle=CharHandle, 
                                    Char_Desc_Uuid_Type=0x01,  
                                    Char_UUID_16=0x2903,
                                    Char_Desc_Value_Max_Len=0x0A,
                                    Char_Desc_Value_Length=0x01, 
                                    Char_Desc_Value=0x06,
                                    Security_Permissions=0x00,
                                    Access_Permissions=0x03,
                                    GATT_Evt_Mask = 0x00,
                                    Enc_Key_Size = 0x07,   
                                    Is_Variable = 0x00)  
#if Status!=0x00:
#    ERROR('ACI_GATT_ADD_CHAR_DESC FAILED') 
	
status =ACI_GAP_SET_DISCOVERABLE(Advertising_Interval_Min=0x100, 
                                 Advertising_Interval_Max=0x200,
                                 Local_Name_Length=0x05,
                                 Local_Name=[0x08,0x74,0x65,0x73,0x74])
if status!=0x00:
    ERROR('ACI_GAP_SET_DISCOVERABLE CALL FAILED') 

