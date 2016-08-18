using System;
using TechTalk.SpecFlow;
using ST_Nucleo;
using NUnit.Framework;
using System.Globalization;
using System.Configuration;

namespace Test
{
    [Binding]
    public class BleApplianceSteps
    {
        string commPort = ConfigurationManager.AppSettings["COMPort"];
        [Given(@"the BLE reference device is configured and operational with battery level equals '(.*)'%")]
        public void GivenABLEApplianceConfiguredAndOperationaWithBatteryLevelEquals(int batteryLevel)
        {
            NucleoBLE ble = new NucleoBLE(commPort);
            string manifactureurName = "galileo";
            string modelNumber = "1234-AA";
            string systemID = "AA42";


            string address = "0605040302AB";
            Assert.AreEqual(ble.ResetHw(), 0);
            Assert.AreEqual(ble.ResetHw(), 0);
            Assert.AreEqual(ble.SetMode((int)ModeGap.MODE2), 0);
            Assert.AreEqual(ble.SetMyAddress(Int64.Parse(address, NumberStyles.AllowHexSpecifier)), 0);
            Assert.AreEqual(ble.SetGattRole((int)RoleForBlueNRG.GAP_PERIPHERAL_ROLE), 0);
            ble.SetIOCapability((int)IOCapabilities.IO_CAP_NO_INPUT_NO_OUTPUT);
            ble.SetAuthenticationRequirement(0x00, 0x01, 0x07, 0x10, 1, 0000, 0x00);

            Assert.AreEqual(ble.AddBatteryService(batteryLevel), 0);
            Assert.AreEqual(ble.AddThermometerService(37), 0);
            Assert.AreEqual(ble.AddDeviceInformationService(manifactureurName, modelNumber, systemID), 0);

            Assert.AreEqual(ble.SetDiscoverable(0x00, 0x20, 0x1000, 0x00, 0x00,
                                                "GaliThermometer", 0x0, new int[] { }, 0x0006, 0x0008), 0);

        }
        
    }
}

