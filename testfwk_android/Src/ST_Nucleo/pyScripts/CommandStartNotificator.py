#import sys
#import os
#PRINT('\n'.join(sys.path))
#sys.path.insert(0, os.path.abspath('./')) 

import clr
from types import *
from System import Action
clr.AddReference("C:\\Dev\\bluelib\\Source\\HardwareTests\\ST_Nucleo\\bin\\Debug\\ST_Nucleo.dll")
import ST_Nucleo
#Receive Notifications

mc = ST_Nucleo.BLEStreamingProtocal()

while(True):
    timeout = True;
    e = WAIT_EVENT(HCI_VENDOR_EVENT, timeout = 60) 
    eCode = e.get_param('Ecode').val
	
    if (eCode == ACI_GATT_ATTRIBUTE_MODIFIED_EVENT):
     PRINT(str(e.get_param('Attr_Handle').val) + " " + str(e.get_param('Attr_Data').val))
     mc.CallbackTest(str(e.get_param('Attr_Data').val))
     timeout = False;
	
    if (eCode == ACI_GATT_NOTIFICATION_EVENT):
     PRINT("NOTIFICATION"+str(e.get_param('Attribute_Handle').val))
     timeout = False
	 
    if(timeout==True):
	 sys.exit()
