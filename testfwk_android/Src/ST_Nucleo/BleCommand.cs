using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;
using System.Globalization;
using System.Numerics;
using System.Threading;

namespace ST_Nucleo
{
    public class NucleoBLE
    {
        private ScriptExecutor scriptExecutor;
        private string port;


        public NucleoBLE(string serialPort)
        {
            scriptExecutor = new ScriptExecutor(serialPort);
            port = serialPort;
        }


        public int ResetHw()
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandResetHw.py");
            return EvaluateKeys("HW_RESET", 0x01, operationResultOutput, out dataOutput);
        }

        public int SetMode(int mode)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandSetMode.py " + mode.ToString());
            return EvaluateKeys("SET_MODE ", 0, operationResultOutput, out dataOutput);
        }

        public int SetMyAddress(long address)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandSetPublicAddress.py " + address.ToString());
            return EvaluateKeys("SET_PUBLIC_ADDRESS", 0, operationResultOutput, out dataOutput);
        }

        public int SetTXPowerLevel(int enHighPower, int paLevel)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandSetTXPowerLevel.py " + enHighPower.ToString() + " " + paLevel.ToString());
            return EvaluateKeys("ACI_HAL_SET_TX_POWER_LEVEL", 0, operationResultOutput, out dataOutput);
        }

        public int SetGattRole(int role)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandGattInit.py " + role.ToString());
            return EvaluateKeys("ACI_GAP_INIT", 0, operationResultOutput, out dataOutput);
        }

        public int SetDiscoverable(int advType, int minIntervalAdv, int maxIntervalAdv, int ownAddrType, int filterPolicy,
                                      string localName, int serviceUUIDLength, int[] serviceList, int slaveMinConnInterval, int slaveMaxConnInterval)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            string localNameData = ArrayToPythonString(GenerateLocalNameData(localName));
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandSetDiscoverable.py " + advType.ToString() + " " +
                                                                                                                  minIntervalAdv.ToString() + " " +
                                                                                                                  maxIntervalAdv.ToString() + " " +
                                                                                                                  ownAddrType.ToString() + " " +
                                                                                                                  filterPolicy.ToString() + " " +
                                                                                                                  (localName.Count() + 1).ToString() + " " +
                                                                                                                  localNameData + " " +
                                                                                                                  serviceUUIDLength.ToString() + " " +
                                                                                                                  serviceList.ToString() + " " +
                                                                                                                  slaveMinConnInterval.ToString() + " " +
                                                                                                                  slaveMaxConnInterval.ToString());

            return EvaluateKeys("ACI_GAP_SET_DISCOVERABLE", 0, operationResultOutput, out dataOutput);
        }


        public int AddCharDescriptor(int serviceHandler, int charHandler, int charDescType,
                                     int charUUID, int charDescValueMaxLength, int charDescValueLength,
                                     int charDescValue, int securityPermission, int accessPermission,
                                     int GATTEvtMask, int EncrypKeySize, int IsVariable)
        {

            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandAddCharDescriptor.py " + serviceHandler.ToString() + " " +
                                                                                                                  charHandler.ToString() + " " +
                                                                                                                  charDescType.ToString() + " " +
                                                                                                                  charUUID.ToString() + " " +
                                                                                                                  charDescValueMaxLength.ToString() + " " +
                                                                                                                  charDescValueLength.ToString() + " " +
                                                                                                                  charDescValue.ToString() + " " +
                                                                                                                  securityPermission.ToString() + " " +
                                                                                                                  accessPermission.ToString() + " " +
                                                                                                                  GATTEvtMask.ToString() + " " +
                                                                                                                  EncrypKeySize.ToString() + " " +
                                                                                                                  IsVariable.ToString());
            return EvaluateKeys("ACI_GATT_ADD_CHAR_DESC", 0, operationResultOutput, out dataOutput);

        }

        public int InititializeBleStreamingProtocol(out Dictionary<string, string> dataOutput)
        {
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("BLEStreamingProtocol_Initialization.py ");
            return EvaluateKeys("STREAMING_PROTOCAL_INIT", 0, operationResultOutput, out dataOutput);
        }

        public int InititializeBleStreamingProtocol()
        {
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("BLEStreamingProtocol_Initialization.py ");
            return 0;
        }

        public int InititializeBleStreamingProtocol_Log()
        {
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("BLEStreamingProtocol_Script_Log.py ");
            return 0;
        }

        public int WaitForEventBleStreamingProtocol(out Dictionary<string, string> dataOutput)
        {
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("BLEStreamingProtocol_WaitAttribNotification.py ");
            return EvaluateKeys("ATTRIBUTE_MODIFIED_EVENT", 0, operationResultOutput, out dataOutput);
        }

        public int SendAndWaitBleStreamingProtocol(int serviceHandle, int charHandle, int length, int[] data,  out Dictionary<string, string> dataOutput)
        {
            string dataPython = ArrayToPythonString(data);
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("BLEStreamingProtocol_WaitAttribNotification.py "+
                                                                                               serviceHandle.ToString()+
                                                                                               charHandle.ToString()+
                                                                                               length.ToString()+
                                                                                               dataPython);
            return EvaluateKeys("ACI_GATT_UPDATE_CHAR_VALUE", 0, operationResultOutput, out dataOutput);
        }

        public int InitAndRunBleStreamingProtocol()
        {
            new Thread(() => 
            {
                Thread.CurrentThread.IsBackground = true; 
                Dictionary<string, string> dataOutput = new Dictionary<string, string>();
                Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("BLEStreamingProtocol.py ");
            }).Start();

            System.Threading.Thread.Sleep(10000);
            return 0;
            
        }
    
        public int AddBleStreamingProtocolService()
        {
            int operationResult = 0;
            int serviceHandler, charHandler = 0;
            operationResult += AddService(0x02, "0xB1,0xC7,0x1B,0x26,0xD4,0x56,0xE9,0xBC,0x31,0x41,0x74,0x40,0xF1,0xFF,0x51,0xA6", 0x01, 15, out serviceHandler);
            operationResult += AddCharacteristic(serviceHandler, 0x02, "0xB1,0xC7,0x1B,0x26,0xD4,0x56,0xE9,0xBC,0x31,0x41,0x74,0x40,0x01,0x00,0x51,0xA6", 19, (int)CharProperties.CHAR_PROP_WRITE_WITHOUT_RESP, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += AddCharacteristic(serviceHandler, 0x02, "0xB1,0xC7,0x1B,0x26,0xD4,0x56,0xE9,0xBC,0x31,0x41,0x74,0x40,0x02,0x00,0x51,0xA6", 19, (int)CharProperties.CHAR_PROP_NOTIFY, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += AddCharacteristic(serviceHandler, 0x02, "0xB1,0xC7,0x1B,0x26,0xD4,0x56,0xE9,0xBC,0x31,0x41,0x74,0x40,0x03,0x00,0x51,0xA6", 19, (int)CharProperties.CHAR_PROP_NOTIFY, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += AddCharacteristic(serviceHandler, 0x02, "0xB1,0xC7,0x1B,0x26,0xD4,0x56,0xE9,0xBC,0x31,0x41,0x74,0x40,0x04,0x00,0x51,0xA6", 19, (int)CharProperties.CHAR_PROP_WRITE_WITHOUT_RESP, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += AddCharacteristic(serviceHandler, 0x02, "0xB1,0xC7,0x1B,0x26,0xD4,0x56,0xE9,0xBC,0x31,0x41,0x74,0x40,0x05,0x00,0x51,0xA6", 19, (int)CharProperties.CHAR_PROP_READ, 0x00, 0x01, 0x07, 0x01, out charHandler);                
            return operationResult; 
        }

        public int AddBatteryService(int batteryLevel)
        {
            int serviceHandler, charHandler;
            int operationResult = (int)BLEStatus.BLE_STATUS_SUCCESS;
            operationResult += AddService(0x01, 0x180F, 0x01, 5, out serviceHandler);
            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A19, 20, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 1, (ulong)batteryLevel);
            operationResult += AddCharDescriptor(serviceHandler, charHandler, 0x01, 0x2904, 0x07, 0x07, 0x000033270100, 0x00, 0x03, 0x00, 0x07, 0x00);
            operationResult += AddCharDescriptor(serviceHandler, charHandler, 0x01, 0x2904, 0x02, 0x02, 0x0000, 0x00, 0x03, 0x00, 0x07, 0x00);
            return operationResult;

        }

        //public int AddCurrentTimeService()
        //{
        //    int serviceHandler, charHandler;
        //    int operationResult = (int)BLEStatus.BLE_STATUS_SUCCESS;
        //    operationResult += AddService(0x01, 0x1805, 0x01, 20, out serviceHandler);
        //    operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A29, 20, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);

        //}

        public int AddDeviceInformationService(string manufactureurName, string modelNumber, string systemID)
        {
            int serviceHandler, charHandler;
            int operationResult = (int)BLEStatus.BLE_STATUS_SUCCESS;


            operationResult += AddService(0x01, 0x1805, 0x01, 10, out serviceHandler);
            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A2B, 10, 0x12, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 1, 0xE0);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 1, 1, 0x07);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 2, 1, 0x02);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 3, 1, 0x1A);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 4, 1, 0x0B);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 5, 1, 0x1D);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 6, 1, 0x20);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 7, 1, 0x05);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 8, 1, 0x00);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 9, 1, 0x00);
            operationResult += AddCharDescriptor(serviceHandler, charHandler, 0x01, 0x2902, 0x09, 0x08, 0x0000, 0x00, 0x03, 0x00, 0x07, 0x00);

            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A0F, 2, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 2, 0xD000);

            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A14, 4, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 4, 0x00000000);

            //operationResult += AddService(0x01, 0x1800, 0x01, 10, out serviceHandler);
            //operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A00, 14, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            //operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 14, 0x424344);
            //operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A01, 2, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            //operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 2, 0x4243);
            //operationResult += AddCharDescriptor(serviceHandler, charHandler, 0x01, 0x2A04, 0x09, 0x08, 0x42, 0x00, 0x03, 0x00, 0x07, 0x00);


            operationResult += AddService(0x01, 0x180A, 0x01, 20, out serviceHandler);
            // Manufactureur Name MANDATORY *
            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A29, 40, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, manufactureurName.Count(), UInt64.Parse(ConvertStringToAsciiString(manufactureurName), NumberStyles.AllowHexSpecifier));
            // Model Number String MANDATORY *
            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A24, 40, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, modelNumber.Count(), UInt64.Parse(ConvertStringToAsciiString(modelNumber), NumberStyles.AllowHexSpecifier));
            //System ID MANDATORY *
            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A23, 5, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, systemID.Count() / 2, UInt64.Parse(systemID, NumberStyles.AllowHexSpecifier));


            //Serial Number String optional
            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A25, 7, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 7, 0x42);
            //Hardware revision String optional
            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A27, 7, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 7, 0x42);
            //Firmware Revision String optional
            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A26, 7, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 7, 0x42);
            //Software Revision String optional
            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A28, 7, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 7, 0x42);
            //IEEE 11073-20601 Regulatory Certification Data List Opt
            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A2A, 7, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 1, 0x42);
            //PnP ID optional
            operationResult += AddCharacteristic(serviceHandler, 0x01, 0x2A50, 7, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            operationResult += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 1, 0x42);


            return operationResult;

        }


        public int AddThermometerService(int temperatureValue)
        {
            int serviceHandler, charHandler;
            int res = (int)BLEStatus.BLE_STATUS_SUCCESS;
            const int temperatureType = 2;
            const int measurementInterval = 100;
            res += AddService(0x01, 0x1809, 0x01, 20, out serviceHandler);
            res += AddCharacteristic(serviceHandler, 0x01, 0x2A1C, 1, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            res += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 1, (ulong)temperatureValue);
            res += AddCharacteristic(serviceHandler, 0x01, 0x2A1D, 1, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            res += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 1, temperatureType);
            res += AddCharacteristic(serviceHandler, 0x01, 0x2A21, 1, 0x02, 0x00, 0x01, 0x07, 0x01, out charHandler);
            res += UpdateCharacteristicValue(serviceHandler, charHandler, 0, 1, measurementInterval);
            return res;
        }

        public int UpdateCharacteristicValue(int serviceHandle, int charHandler, int offset, int valueLength, ulong charValue)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandUpdateCharValue.py " + serviceHandle.ToString() + " " +
                                                                                                                              charHandler.ToString() + " " +
                                                                                                                              offset.ToString() + " " +
                                                                                                                              valueLength.ToString() + " " +
                                                                                                                              charValue.ToString());
            return EvaluateKeys("ACI_GATT_UPDATE_CHAR_VALUE", 0, operationResultOutput, out dataOutput);
        }

        public int UpdateCharacteristicValue(int serviceHandle, int charHandler, int offset, int valueLength, int[] charValue)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            string data = ArrayToPythonString(charValue);
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("BLEStreamingProtocol_UpdateCharValue.py " + serviceHandle.ToString() + " " +
                                                                                                                                         charHandler.ToString() + " " +
                                                                                                                                         offset.ToString() + " " +
                                                                                                                                         valueLength.ToString() + " " +
                                                                                                                                         data);
            return EvaluateKeys("ACI_GATT_UPDATE_CHAR_VALUE", 0, operationResultOutput, out dataOutput);
        }



        public int AddCharacteristic(int serviceHandler, int charUUIDType, int charUUID, int valueLength, int properties, int securityPermission, int GATTevtMask, int encKeySize, int isVariable, out int charHandler)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandAddCharacteristic.py " + serviceHandler.ToString() + " " +
                                                                                                                              charUUIDType.ToString() + " " +
                                                                                                                              charUUID.ToString() + " " +
                                                                                                                              valueLength.ToString() + " " +
                                                                                                                              properties.ToString() + " " +
                                                                                                                              securityPermission.ToString() + " " +
                                                                                                                              GATTevtMask.ToString() + " " +
                                                                                                                              encKeySize.ToString() + " " +
                                                                                                                              isVariable.ToString());

            int result = EvaluateKeys("ACI_GATT_ADD_CHARACTERISTIC", 0, operationResultOutput, out dataOutput);
            return ParseDataOutput(result, "charHandler", dataOutput, out charHandler);
        }

        public int AddCharacteristic(int serviceHandler, int charUUIDType, string charUUID, int valueLength, int properties, int securityPermission, int GATTevtMask, int encKeySize, int isVariable, out int charHandler)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandAddCharacteristic.py " + serviceHandler.ToString() + " " +
                                                                                                                              charUUIDType.ToString() + " " +
                                                                                                                              charUUID + " " +
                                                                                                                              valueLength.ToString() + " " +
                                                                                                                              properties.ToString() + " " +
                                                                                                                              securityPermission.ToString() + " " +
                                                                                                                              GATTevtMask.ToString() + " " +
                                                                                                                              encKeySize.ToString() + " " +
                                                                                                                              isVariable.ToString());

            int result = EvaluateKeys("ACI_GATT_ADD_CHARACTERISTIC", 0, operationResultOutput, out dataOutput);
            return ParseDataOutput(result, "charHandler", dataOutput, out charHandler);
        }



        public int AddService(int serviceUUIDType, int UUIDService, int serviceType, int maxAttributeRecords, out int serviceHandler)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandAddService.py " + serviceUUIDType.ToString() + " " +
                                                                                                                      UUIDService.ToString() + " " +
                                                                                                                      serviceType.ToString() + " " +
                                                                                                                      maxAttributeRecords.ToString());
            int result = EvaluateKeys("ACI_GATT_ADD_SERVICE", 0, operationResultOutput, out dataOutput);
            return ParseDataOutput(result, "serviceHandler", dataOutput, out serviceHandler);
        }

        public int AddService(int serviceUUIDType, string UUIDService, int serviceType, int maxAttributeRecords, out int serviceHandler)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandAddService.py " + serviceUUIDType.ToString() + " " +
                                                                                                                      UUIDService  + " " +
                                                                                                                      serviceType.ToString() + " " +
                                                                                                                      maxAttributeRecords.ToString());
            int result = EvaluateKeys("ACI_GATT_ADD_SERVICE", 0, operationResultOutput, out dataOutput);
            return ParseDataOutput(result, "serviceHandler", dataOutput, out serviceHandler);
        }

        public int DoGeneralDiscoveryProcedure(int LE_ScanInterval, int LE_ScanWindow, int ownAddressType, int filterDuplicate, out Discovery dataOutput)
        {
            //0x10,0x10,0x00,0x01
            Dictionary<string, string> data = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandDoGeneralDiscoveryProcedure.py " +
                                                                                                LE_ScanInterval.ToString() + " " +
                                                                                                LE_ScanWindow.ToString() + " " +
                                                                                                ownAddressType.ToString() + " " +
                                                                                                filterDuplicate.ToString());

            int result = EvaluateKeys("DISCOVERY DEVICES", 0, operationResultOutput, out data);
            dataOutput = null;
            if (result == (int)BLEStatus.BLE_STATUS_SUCCESS)
            {
                dataOutput = new Discovery(data);
            }
            return result;
        }


        public int SetIOCapability(int IOCapability)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandSetIoCapability.py " + IOCapability.ToString());
            return EvaluateKeys("ACI_GAP_SET_IO_CAPABILITY", 0, operationResultOutput, out dataOutput);
        }

        public int SetAuthenticationRequirement(int modeMITM,
                                                    int enable_OOB,
                                                    int minEncryptionKeySize,
                                                    int maxEncryptionKeySize,
                                                    int useFixedPin,
                                                    int fixedPin,
                                                    int bondingMode)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandSetAuthenticationRequirement.py " +
                                                                                             modeMITM.ToString() + " " +
                                                                                             enable_OOB.ToString() + " " +
                                                                                             minEncryptionKeySize.ToString() + " " +
                                                                                             maxEncryptionKeySize.ToString() + " " +
                                                                                             useFixedPin.ToString() + " " +
                                                                                             fixedPin.ToString() + " " +
                                                                                             bondingMode.ToString());

            return EvaluateKeys("ACI_GAP_SET_AUTHENTICATION_REQUIREMENT", 0, operationResultOutput, out dataOutput);
        }




        public int DoPairing(int connectionHandler, int pinCode, int forceRebond, int authorize)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandDoPairing.py" + " " +
                                                                                             connectionHandler.ToString() + " " +
                                                                                             pinCode.ToString() + " " +
                                                                                             forceRebond.ToString() + " " +
                                                                                             authorize.ToString());

            return EvaluateKeys("ACI_GAP_SET_AUTHENTICATION_REQUIREMENT", 0, operationResultOutput, out dataOutput);
        }

        public void GetBondedDevices()
        {
            //ToBeReviewed
            scriptExecutor.ExecuteScript("CommandGetBondedDevices.py");
        }



        public int Connect(int peerAddressType, long peerAddressSlave, out int handler)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandCreateConnection.py " +
                                                                                             peerAddressType.ToString() + " " +
                                                                                             peerAddressSlave.ToString());

            int result = EvaluateKeys("CONNECTION", 0, operationResultOutput, out dataOutput);
            return ParseDataOutput(result, "connectionHandler", dataOutput, out handler);
        }

        public int DiscoverAllPrimaryServices(int connectionHandler, out Services servicesDiscovered)
        {
            List<int> attributeValue = new List<int>();
            List<int> startHandle = new List<int>();
            List<int> endHandle = new List<int>();

            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("testFunction_DiscoverServices.py" + " " + connectionHandler.ToString());
            int result = EvaluateKeys("SERVICES DISCOVERY", 0, operationResultOutput, out dataOutput);

            if (result == 0)
            {
                int totalServices = dataOutput.Count / 3;
                for (int i = 0; i < totalServices; i++)
                {
                    attributeValue.Add(Int32.Parse(dataOutput["SERVICE" + i.ToString()]));
                    startHandle.Add(Int32.Parse(dataOutput["SERVICE_START_HANDLER" + i.ToString()]));
                    endHandle.Add(Int32.Parse(dataOutput["SERVICE_END_HANDLER" + i.ToString()]));
                }
            }

            servicesDiscovered = new Services(attributeValue, startHandle, endHandle);
            return result;
        }

        public int DiscoverCharacteristics(int connectionHandler, int startHandler, int endHandler,
                                            out Characteristics characteristicsDiscovered)
        {
            List<int> handler = new List<int>();
            List<int> UUID = new List<int>();
            List<int> charValueHandler = new List<int>();
            List<int> attribute = new List<int>();

            Dictionary<string, string> dataOutput = new Dictionary<string, string>();

            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("testFunction_DiscoverCharacteristics.py" + " " +
                                                                                             connectionHandler.ToString() + " " +
                                                                                             startHandler.ToString() + " " +
                                                                                             endHandler.ToString());

            int result = EvaluateKeys("Characteristic DISCOVERY", 0, operationResultOutput, out dataOutput);

            if (result == 0)
            {
                int totalCharacteristics = dataOutput.Count / 4;
                for (int i = 0; i < totalCharacteristics; i++)
                {
                    handler.Add(Int32.Parse(dataOutput["HANDLE" + i.ToString()]));
                    UUID.Add(Int32.Parse(dataOutput["UUID" + i.ToString()]));
                    charValueHandler.Add(Int32.Parse(dataOutput["CHAR_VALUE_HANDLER" + i.ToString()]));
                    attribute.Add(Int32.Parse(dataOutput["ATTRIBUTE" + i.ToString()]));
                }
            }

            characteristicsDiscovered = new Characteristics(handler, UUID, charValueHandler, attribute);

            return result;

        }


        public int ConnectionUpdate(int connectionHandler,
                                    int minIntervalConnection,
                                    int maxIntervalConnection,
                                    int connectionLatency,
                                    int supervisionTimeout,
                                    int minCELength,
                                    int maxCELength)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandConnectionUpdate.py " +
                                                                                             connectionHandler.ToString() + " " +
                                                                                             minIntervalConnection.ToString() + " " +
                                                                                             maxIntervalConnection.ToString() + " " +
                                                                                             connectionLatency.ToString() + " " +
                                                                                             supervisionTimeout.ToString() + " " +
                                                                                             minCELength.ToString() + " " +
                                                                                             maxCELength.ToString() + " ");

            return EvaluateKeys("Update Connection", 0, operationResultOutput, out dataOutput);
        }



        public int WriteCharValue(int connectionHandler, int attributeHandler, int valueLenght, int value)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandWriteCharValue.py" + " " +
                                                                                            connectionHandler.ToString() + " " +
                                                                                            attributeHandler.ToString() + " " +
                                                                                            valueLenght.ToString() + " " +
                                                                                            value.ToString());

            return EvaluateKeys("ACI_GATT_WRITE_CHAR", 0, operationResultOutput, out dataOutput);
        }

        public int ReadCharacteristicValue(int connectionHandler, int characteristicHandler, out int value)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("testFunction_ReadCharacteristicValue.py" + " " +
                                                                                            connectionHandler.ToString() + " " +
                                                                                            characteristicHandler.ToString());

            int result = EvaluateKeys("READ_VALUE", 0, operationResultOutput, out dataOutput);
            return ParseDataOutput(result, "READ_VALUE", dataOutput, out value);
        }

        public int TerminateGapConnection(int connectionHandler)
        {
            Dictionary<string, string> dataOutput = new Dictionary<string, string>();
            Dictionary<string, string> operationResultOutput = scriptExecutor.ExecuteScript("CommandGapTerminate.py" + " " + connectionHandler.ToString());
            return EvaluateKeys("ACI_GAP_TERMINATE", 0, operationResultOutput, out dataOutput);
        }

        public int Connect(int leScanInterval,
                            int leScanWindow,
                            int peerAddressType,
                            long peerAddress,
                            int minIntervalConnection,
                            int maxIntervalConnection,
                            int connectionLatency,
                            int supervisionTimeout,
                            int minCELength,
                            int maxCELength)
        {

            //ToBeReviewed
            Dictionary<string, string> output = scriptExecutor.ExecuteScript("testFunction_Connection.py " +
                                                                             leScanInterval.ToString() + " " +
                                                                             leScanWindow.ToString() + " " +
                                                                             peerAddressType.ToString() + " " +
                                                                             peerAddress.ToString() + " " +
                                                                             minIntervalConnection.ToString() + " " +
                                                                             maxIntervalConnection.ToString() + " " +
                                                                             connectionLatency.ToString() + " " +
                                                                             supervisionTimeout.ToString() + " " +
                                                                             minCELength.ToString() + " " +
                                                                             maxCELength.ToString() + " ");

            string connectionHandler = "";

            if (output.ContainsKey("Exception"))
                return -1;

            if (output.TryGetValue("connectionHandler", out connectionHandler))
            {
                return Int32.Parse(connectionHandler);
            }
            else
            {
                return -1;
            }

        }

        private void UpdateGui(string operationName, string result)
        {
            Debug.WriteLine(operationName + " " + result);
            //mainWindow.UpdateOperationBox(operationName);
            //mainWindow.UpdateResultBox(result);
        }

        private string ArrayToPythonString(int[] data)
        {
            string convertedData = "";

            for (int i = 0; i < data.Length; i++)
            {
                convertedData = convertedData + data[i].ToString();
                if (i != data.Length - 1)
                {
                    convertedData = convertedData + ",";
                }
            }
            return convertedData;
        }


        public void StartSlave()
        {
            scriptExecutor.ExecuteScript("Slave_Role.py");
        }

        private int EvaluateKeys(string key, int expectedKeyValue, Dictionary<string, string> outputForProcessing, out Dictionary<string, string> dataOutput)
        {

            dataOutput = new Dictionary<string, string>();
            //Check for Exception
            if (outputForProcessing.ContainsKey("Exception"))
            {
                UpdateGui(key, "NOK");
                return -1;
            }

            // outputData
            foreach (var item in outputForProcessing)
            {
                if (item.Key.CompareTo("OUTPUT") != 0)
                {
                    dataOutput.Add(item.Key, item.Value);
                }
            }

            // check for operation result function
            if (outputForProcessing.ContainsKey("OUTPUT"))
            {
                int value;
                if (Int32.TryParse(outputForProcessing["OUTPUT"], out value))
                {
                    if (value == expectedKeyValue)
                    {
                        UpdateGui(key, "OK");
                        return 0;
                    }
                    else
                    {
                        UpdateGui(key, "ERR 0x" + value.ToString("X"));
                        return -1;
                    }
                }
            }

            UpdateGui(key, "FAILED");
            return -1;
        }
        private int ParseDataOutput(int operationResult, string key, Dictionary<string, string> dataOutput, out int outputDataValue)
        {
            outputDataValue = -1;

            if (operationResult != 0)
            {
                return -1;
            }

            string dataValue = "";
            dataOutput.TryGetValue(key, out dataValue);
            if (Int32.TryParse(dataValue, out outputDataValue))
            {
                return (int)BLEStatus.BLE_STATUS_SUCCESS;
            }
            else
            {
                return -1;
            }
        }

        private int[] GenerateLocalNameData(string localName)
        {
            int localNameDataLength = localName.Count() + 1;
            int[] localNameData = new int[localNameDataLength];
            localNameData[0] = 0x08; //DataType

            for (int i = 1; i < localNameDataLength; i++)
            {
                localNameData[i] = localName[i - 1];
            }

            return localNameData;
        }

        private string ConvertStringToAsciiString(string stringName)
        {
            string outputString = "";
            byte[] asciiBytes = Encoding.ASCII.GetBytes(stringName);
            foreach (var value in asciiBytes)
            {
                outputString += value.ToString("X");
            }
            return outputString;
        }

    }
}

