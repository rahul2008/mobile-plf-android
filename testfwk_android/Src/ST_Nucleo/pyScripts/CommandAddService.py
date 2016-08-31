import sys

if int(sys.argv[1])==0x01:
 Status,ServHandle=ACI_GATT_ADD_SERVICE(Service_UUID_Type=int(sys.argv[1]), #0x01
                                       Service_UUID_16=int(sys.argv[2]),   #0xA001
                                       Service_Type=int(sys.argv[3]),      #1
                                       Max_Attribute_Records=int(sys.argv[4])) #5    
 PRINT("OUTPUT_DATA;"+str(ServHandle)+";"+"serviceHandler")									   
 PRINT("OUTPUT;"+str(Status)+";"+"ACI_GATT_ADD_SERVICE")
elif int(sys.argv[1])==0x02:
 UUID128string=str(sys.argv[2])
 UUID128 = eval('[' + UUID128string + ']')
 Status,ServHandle=ACI_GATT_ADD_SERVICE(Service_UUID_Type=int(sys.argv[1]),    #0x02
                                       Service_UUID_128=UUID128,      
                                       Service_Type=int(sys.argv[3]),          #1
                                       Max_Attribute_Records=int(sys.argv[4]))#5 	
 PRINT("OUTPUT_DATA;"+str(ServHandle)+";"+"serviceHandler")									   
 PRINT("OUTPUT;"+str(Status)+";"+"ACI_GATT_ADD_SERVICE")
 



#0x01 16UUID / 0x02 128UUID
#0x01 primary service / 0x02 secondary service