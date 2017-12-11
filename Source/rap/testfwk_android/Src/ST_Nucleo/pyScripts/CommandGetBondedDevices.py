import sys

(hw,fw) = GET_STACK_VERSION()
if fw[0]==6:
    # Get bonded device
    list_of_address=''
    status,data = ACI_GAP_GET_BONDED_DEVICES()
    if status !=0x00:
        ERROR('ACI_GAP_GET_BONDED_DEVICES CALL FAILED')
    elif data[0] > 0 :
        PRINT('Bonded with: ' )
        list_of_address=''
        Num_of_Addresses = data[0]
        for i in range(0, Num_of_Addresses):
            if list_of_address !='':
                list_of_address+= ', '
            list_of_address += '(Type= ' + str (data[1+i*7]) + ', Address= ' + str(hex(GET_VALUE(data[2+i*7:8+i*7])).rstrip("L")) + ')'
        PRINT(list_of_address)
        #PRINT('Bonded with: ' + str(hex(GET_VALUE(Data[2:])).rstrip("L")))
    else:
        PRINT('Not bonded')
else:
    #starting from BLueNRG GUI 1.9.0, ACI_GAP_GET_BONDED_DEVICES returns four parameters
    list_of_address=''
    status,Num_of_Addresses,Address_Type,Address=ACI_GAP_GET_BONDED_DEVICES()  
    if status !=0x00:
        ERROR('ACI_GAP_GET_BONDED_DEVICES CALL FAILED')
    elif Num_of_Addresses > 0 :
        PRINT('Bonded with: ' )
        list_of_address=''
        for i in range(0, Num_of_Addresses):
            if list_of_address !='':
                list_of_address+= ', '
            list_of_address += '(Type= ' + str (Address_Type[i]) + ', Address= ' + str(hex(Address[i]).rstrip("L")) + ')'
        PRINT(list_of_address)
    else:
        PRINT('Not bonded')
        

