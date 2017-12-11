
import sys


status= ACI_GAP_SET_AUTHENTICATION_REQUIREMENT(MITM_Mode=int(sys.argv[1]),
                                     OOB_Enable=int(sys.argv[2]),
                                     Min_Encryption_Key_Size=int(sys.argv[3]),
                                     Max_Encryption_Key_Size=int(sys.argv[4]),
                                     Use_Fixed_Pin=int(sys.argv[5]),#request pin 1
                                     Fixed_Pin=int(sys.argv[6]), #NA when Use_Fixed_Pin=0x01
                                     Bonding_Mode=int(sys.argv[7]))

PRINT("OUTPUT;"+str(status)+";"+"ACI_GAP_SET_AUTHENTICATION_REQUIREMENT")