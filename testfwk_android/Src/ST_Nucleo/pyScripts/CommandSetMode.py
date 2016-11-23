import sys

status = SET_MODE(int(sys.argv[1])) ## 2 - master / slave large GATT
PRINT("OUTPUT;"+str(status)+";"+"SET_MODE")