import sys

name = map(int,sys.argv[7].split(","))
PRINT("OUTPUT_2;"+str(name)+";"+"ARRAY_TEST")

#Put in Discoverable Mode with Name = 'Test' = [0x08,0x74,0x65,0x73,0x74](Add prefix 0x08 to indicate the AD type Name)
Status = ACI_GAP_SET_DISCOVERABLE(Advertising_Type= int(sys.argv[1]),#0x00,
                                 Advertising_Interval_Min= int(sys.argv[2]),#0x20, N*0.625ms
                                 Advertising_Interval_Max=int(sys.argv[3]),#0x100,
                                 Own_Address_Type=int(sys.argv[4]),
                                 Advertising_Filter_Policy = int(sys.argv[5]),
                                 Local_Name_Length=int(sys.argv[6]),#0x05,
                                 Local_Name=name,#0x08,0x74,0x65,0x73,0x74],
                                 #Service_Uuid_length=int([sys.argv[8]),	
                                 #Service_Uuid_List = int([sys.argv[9]),								 
                                 Slave_Conn_Interval_Min = int(sys.argv[10]),#0x0006,
                                 Slave_Conn_Interval_Max = int(sys.argv[11]))#0x0008) 
								 
PRINT("OUTPUT;"+str(Status)+";"+"ACI_GAP_SET_DISCOVERABLE")

#Advertising_Type: 0x00 ADV_IND 0x02 ADV_SCAN_IND  0x03 ADV_NONCONN_IND
#Own_Address_Type: 0x00 public  0x01 random
#Advertising_Filter_Policy: 0x00 from any