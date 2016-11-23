import sys


Status= ACI_GATT_ADD_CHAR_DESC(Service_Handle = int(sys.argv[1]),  
                                    Char_Handle=int(sys.argv[2]), 
                                    Char_Desc_Uuid_Type=int(sys.argv[3]),  
                                    Char_UUID_16=0x2850,#int(sys.argv[4]),
                                    Char_Desc_Value_Max_Len=int(sys.argv[5]),
                                    Char_Desc_Value_Length=int(sys.argv[6]), 
                                    Char_Desc_Value=int(sys.argv[7]),
                                    Security_Permissions=int(sys.argv[8]),
                                    Access_Permissions=int(sys.argv[9]),
                                    GATT_Evt_Mask = int(sys.argv[10]),
                                    Enc_Key_Size = int(sys.argv[11]),   
                                    Is_Variable = int(sys.argv[12]))     

#if(Status==0x00):
# PRINT("OUTPUT_DATA;"+str(CharDescHandle[0])+";"+"CHAR_DESC_HANDLER")	
PRINT("OUTPUT;"+str(Status)+";"+"ACI_GATT_ADD_CHAR_DESC")
									
'''
Status,Char_Desc_Handle = ACI_GATT_ADD_CHAR_DESC(Service_Handle = int(sys.argv[1]),  
                                    Char_Handle=int(sys.argv[2]), 
                                    Char_Desc_Uuid_Type=int(sys.argv[3]),  
                                    Char_UUID_16=int(sys.argv[4]),
                                    Char_Desc_Value_Max_Len=int(sys.argv[5]),
                                    Char_Desc_Value_Length=int(sys.argv[6]), 
                                    Char_Desc_Value=int(sys.argv[7]),
                                    Security_Permissions=int(sys.argv[8]),
                                    Access_Permissions=int(sys.argv[9]),
                                    GATT_Evt_Mask = int(sys.argv[10]),
                                    Enc_Key_Size = int(sys.argv[11]),   
                                    Is_Variable = int(sys.argv[12]))     


PRINT("DONE DEBUG");	
if(Status==0x00):
 PRINT("OUTPUT_DATA;"+str(Char_Desc_Handle)+";"+"CHAR_DESC_HANDLER")									   
PRINT("OUTPUT;"+str(Status)+";"+"ACI_GATT_ADD_CHARACTERISTIC")
'''