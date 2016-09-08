## @file Security_PassKeyEntry_Slave_Role.py
## @brief This script implements a security scenario with PassKey Entry method: one BlueNRG/BlueNRG-MS Central connects to a BlueNRG/BlueNRG-MS Peripheral which starts security process by sending a Slave Security Request. This script implements the Peripheral role which configures a BlueNRG/BlueNRG-MS device as Peripheral, with I/O capability  "Display only", security authentication requirements with no fixed pin and then enter in discoverable mode.  Once connected with the BlueNRg Master device,  BlueNRG/BlueNRG-MS Peripheral starts ACI_GAP_SLAVE_SECURITY_REQ procedure. The BlueNRG/BlueNRG-MS Peripheral generates a random key and it sets it through ACI_GAP_PASS_KEY_RESP command after the ACI_GAP_PASS_KEY_REQ_EVENT event is generated. At this stage, a input window is diplayed on Central device for allowing user to insert the key displayed on Peripheral device. The Pairing is completed and the bonded device is displayed.
## @li The script is implemented using the new BlueNRG GUI language scripts based on the BlueNRG/BlueNRG-MS ACI APIs. The BlueNRG GUI with script engine is delivered within BlueNRG DK 1.7.0 SW package and coming next versions.
## @li A PC running Windows Vista/7 operating systems (64 and 32 bit version) or Windows XP operating systems (32 bit edition), with available USB ports is required for installing the BlueNRG/BlueNRG-MS DK SW package.
##
## <!-- Copyright 2015 by STMicroelectronics.  All rights reserved.       *80*-->
##
##  @section S6 Security_PassKeyEntry_Slave_Role.py script, preliminary setup steps: configure a GAP Central device using the Security_PassKeyEntry_Master_Role.py script
##
##  @li Open BlueNRG GUI available within BlueNRG DK 1.7.0 SW package (and coming next versions)
##  @li Connect a BlueNRG/BlueNRG-MS platform to a PC USB port
##  @li Put the BlueNRG/BlueNRG-MS platform in DFU mode and download the "C:\Program Files (x86)\STMicroelectronics\BlueNRG DK 1.x.0\Firmware\STM32L1_prebuilt_images\BlueNRG_VCOM_1_x.hex" using the BlueNRG GUI, Tools, Flash Motherboard FW... utility
##  @li On BlueNRG GUI, open the USB Port associated to the plugged BlueNRG/BlueNRG-MS platform
##  @li On BlueNRG GUI, select the Scripts window 
##  @li On BlueNRG GUI, Scripts window, Scripts Engine section, click on ... tab and browse to the Security_PassKeyEntry_Master_Role.py script location and select  it
##  @li On BlueNRG GUI, click on  Run Script tab for running the selected script
##  @li BlueNRG/BlueNRG-MS Central device starts direct connection procedure.
##
##  @section S61 Security_PassKeyEntry_Slave_Role.py script execution steps through BlueNRG GUI Script Engine
##
##  @li Open BlueNRG GUI available within BlueNRG DK 1.7.0 SW package (and coming next versions)
##  @li Connect a BlueNRG/BlueNRG-MS platform to a PC USB port
##  @li Put the BlueNRG/BlueNRG-MS platform in DFU mode and download the "C:\Program Files (x86)\STMicroelectronics\BlueNRG DK 1.x.0\Firmware\STM32L1_prebuilt_images\BlueNRG_VCOM_1_x.hex" using the BlueNRG GUI, Tools, Flash Motherboard FW... utility
##  @li On BlueNRG GUI, open the USB Port associated to the plugged BlueNRG/BlueNRG-MS platform
##  @li On BlueNRG GUI, select the Scripts window 
##  @li On BlueNRG GUI, Scripts window, Scripts Engine section, click on ... tab and browse to the Security_PassKeyEntry_Slave_Role.py script location and select  it
##  @li On BlueNRG GUI, click on  Run Script tab for running the selected script
##  @li BlueNRG/BlueNRG-MS Peripheral device enters in discoverable mode and then it connects once it receives the connection request from the BlueNRG/BlueNRG-MS Central device
##  @li BlueNRG/BlueNRG-MS Peripheral device starts the security process by calling ACI_GAP_SLAVE_SECURITY_REQ procedure. Then ACI_GAP_PASS_KEY_REQ_EVENT event is raised and BlueNRG/BlueNRG-MS Peripheral generates a random key and it sets it through ACI_GAP_PASS_KEY_RESP command
##  @li Once the displayed BlueNRG/BlueNRG-MS Peripheral random key is inserted on BlueNRG/BlueNRG-MS Master device, the pairing process is completed and the bonded device is displayed.
##
##  @section S62 Supported BlueNRG/BlueNRG-MS platforms
##
##  @li BlueNRG development platform (order code: STEVAL-IDB002V1)
##  @li BlueNRG USB dongle (order code: STEVAL-IDB003V1)
##  @li BlueNRG-MS development platform (order code: STEVAL-IDB005V1) 
##  @li BlueNRG-MS Daughter Board (order code: STEVAL-IDB005V1D)
##  @li BlueNRG-MS USB dongle (order code: STEVAL-IDB006V1)
## 
##  @section S63 Supported BLE devices and stack versions
##
##  @li BlueNRG stack v6.4
##  @li BlueNRG-MS stack v7.x
##

## \addtogroup BlueNRG_and_BlueNRG_MS_scripts
## @{
## \see Security_PassKeyEntry_Slave_Role.py for documentation.
## 
## @} 

## @cond DOXYGEN_SHOULD_SKIP_THIS

HW_RESET()
SET_MODE(2)
SET_PUBLIC_ADDRESS(0x0280E1223344)
ACI_HAL_SET_TX_POWER_LEVEL(En_High_Power=1,PA_Level = 4)
ACI_GATT_INIT()
ACI_GAP_INIT(Role=PERIPHERAL)

## Clear security database: this implies that each time the script is executed the full bonding process is executed (with PassKey generation and setting).
## After bonding is done(first scripts execution on 2 unbonded devices), comment this line for just encrypting the link without no PassKey generation and setting.
ACI_GAP_CLEAR_SECURITY_DB()

#IO_CAP_DISPLAY_ONLY			(0x00)
#IO_CAP_DISPLAY_YES_NO			(0x01)
#IO_CAP_KEYBOARD_ONLY	                (0x02)
#IO_CAP_NO_INPUT_NO_OUTPUT	        (0x03)
#IO_CAP_KEYBOARD_DISPLAY		(0x04)

## Set I/O capability for using PassKey 

# Set Display I/O capability on Slave side for using PassKey entry method: Display only (it displays the selected pin to be inserted on the Master device)
# On Master side set proper I/O capability in order to use PassKey entry method: Keyboard only.
status= ACI_GAP_SET_IO_CAPABILITY(IO_Capability=0x00) #Display only
if status !=0x00:
    ERROR('ACI_GAP_SET_IO_CAPABILITY CALL FAILED')
else:
    PRINT('Set IO capability as Display only')

status= ACI_GAP_SET_AUTHENTICATION_REQUIREMENT(MITM_Mode=0x01, #PassKey Entry requires authentication 
                                     OOB_Enable=0x00,
                                     Min_Encryption_Key_Size=0x07,
                                     Max_Encryption_Key_Size=0x10,
                                     Use_Fixed_Pin=0x01, #0x01: It requests a  pin
                                     Fixed_Pin=123456,#NA when Use_Fixed_Pin=0x01
                                     Bonding_Mode=0x01)
if status !=0x00:
    ERROR('ACI_GAP_SET_AUTHENTICATION_REQUIREMENTCALL FAILED')
else:
    PRINT('Set authentication requirements with no fixed pin and bonding mode')

num_service=1
num_char=1
#ADD Service/s
count=0
for i in range(num_service):

        
    Status,ServHandle=ACI_GATT_ADD_SERVICE(Service_UUID_Type=0x01, 
                 Service_UUID_16=0xA001+(int(str(count),16)), 
                 Service_Type=1, 
                 Max_Attribute_Records=5)
    if status !=0x00:
        ERROR('ACI_GATT_ADD_SERVICE CALL FAILED')
    count +=1

    #Add Characteristic/s (Read, Write, Notify properties)
    for j in range(num_char):
            Status,CharHandle=ACI_GATT_ADD_CHAR( 
                         Service_Handle =ServHandle,
                         Char_UUID_Type=0x01, 
                         Char_UUID_16= 0xA001+int(str(count),16), 
                         Char_Value_Length=1, 
                         Char_Properties=0x1A,
                         Security_Permissions=0x01,#0x08,
                         GATT_Evt_Mask=0x01,   
                         Enc_Key_Size=0x07, 
                         Is_Variable=0x00)   
            if status !=0x00:
                ERROR('ACI_GATT_ADD_CHAR CALL FAILED') 
            count +=1


status =ACI_GAP_SET_DISCOVERABLE(Advertising_Interval_Min=0x100, Advertising_Interval_Max=0x200)
if status!=0x00:
    ERROR('ACI_GAP_SET_DISCOVERABLE CALL FAILED') 


event=WAIT_EVENT(HCI_LE_META_EVENT,Subevent_Code=HCI_LE_CONNECTION_COMPLETE_EVENT )
if event.get_param('Status').val==0x00:
    conn_handle= event.get_param('Connection_Handle').val

## Peripheral device sends slave security request to bond with Central device
status = ACI_GAP_SLAVE_SECURITY_REQ(Connection_Handle=conn_handle, Bonding=0x01,MITM_Protection=0x01)
if status!=0x00:
    ERROR('ACI_GAP_SLAVE_SECURITY_REQ CALL FAILED')

while (True):
    event = WAIT_EVENT(HCI_VENDOR_EVENT)
    if event.get_param('Ecode').val==ACI_GAP_PASS_KEY_REQ_EVENT:
        ## A pin has to be provided to the BlueNRG/BlueNRG-MS: select 12345.
        pin = 123456 # GET_RAND_KEY()
        PRINT('Selected Random Pin is: ' + str(pin))

        status = ACI_GAP_PASS_KEY_RESP(Connection_Handle=conn_handle, Pass_Key=pin)
        if status !=0x00:
            ERROR('ACI_GAP_PASS_KEY_RESP CALL FAILED')

    #wait until received the event ACI_GAP_PAIRING_COMPLETE_EVENT        
    if event.get_param('Ecode').val==ACI_GAP_PAIRING_COMPLETE_EVENT:
        if event.get_param('Status').val==0x00:
            PRINT('PAIRING SUCCESS')
        else:
            PRINT('PAIRING FAILED')
            break

while (True):
    event = WAIT_EVENT(HCI_VENDOR_EVENT)
           
  
## Wait for ACI_GATT_ATTRIBUTE_MODIFIED_EVENT
WAIT_EVENT() 

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

## @endcond
        
