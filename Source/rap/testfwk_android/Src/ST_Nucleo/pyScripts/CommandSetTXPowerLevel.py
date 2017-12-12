import sys

status = ACI_HAL_SET_TX_POWER_LEVEL(En_High_Power=int(sys.argv[1]),PA_Level = int(sys.argv[2]))
PRINT("OUTPUT;"+str(status)+";"+"ACI_HAL_SET_TX_POWER_LEVEL")