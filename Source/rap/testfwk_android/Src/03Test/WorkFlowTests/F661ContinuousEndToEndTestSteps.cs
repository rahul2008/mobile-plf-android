using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Threading;
using TechTalk.SpecFlow;
using System.Collections.Generic;
using Philips.H2H.Foundation.AutomationCore.Interface;
using System.Configuration;
using Philips.CDP.Automation.IAP.Tests.Workflows;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using System.Xml;
using System.Reflection;
using System.IO;
using Philips.SIG.Automation.Android.CDPP.AppFramework_TestPlugin;
using NUnit.Framework;
namespace Philips.CDP.Automation.IAP.Tests.Workflows
{

    [Binding]
    public class F661ContinuousEndToEndTestSteps
    {



        [Then(@"I am on end to end smoke test screen")]
        public void ThenIClickOnConnectivityFromTheHamburgerMenuList()
        {
            AppHomeScreen.Click(AppHomeScreen.Button.Connectivity);
            Thread.Sleep(2000);
        }

        [Then(@"I enter ID of Reference Device as '(.*)'")]
        public void ThenIEnterIDOfReferenceDeviceAs(string p0)
        {
            AppHomeScreen.EnterText(AppHomeScreen.EditText.ReferenceDeviceIDValue, p0);
            Thread.Sleep(2000);
        }


        [When(@"I connect to the BLE reference device and retrieve measurement value")]

        public void ThenIClickOnMeasurementValue()
        {
            AppHomeScreen.Click(AppHomeScreen.Button.MeasurementValueFromReferenceDevice);
            Thread.Sleep(10000);
           //AppHomeScreen.EnterText(AppHomeScreen.EditText.MeasurementValue, "30");
           //Thread.Sleep(2000);
            string measurement_referencedevice = AppHomeScreen.GetText(AppHomeScreen.EditText.MeasurementValue).Trim();
            Logger.Info("Measurement value info: " + measurement_referencedevice);
            IapReport.Message("Measurement value info: " + measurement_referencedevice);
        }

        [Then(@"I verify the measurement value on screen shows '(.*)'")]
        public void ThenIVerifyTheMeasurementValueOnScreenShows(int p0)
        {
            string meas_value = AppHomeScreen.GetText(AppHomeScreen.EditText.MeasurementValue).Trim();
            Assert.AreEqual(
                p0, Convert.ToInt32(meas_value),
                "Failed: Measurement values fetched from nucleous is incorrect - " +
                "Expected: " + p0 + ", but Measurement value fetched is: " + meas_value
            );
        }

        [When(@"I get the latest moment from datacore")]
        public void ThenIClickOnMomentValueFromDatacore()
        {
            AppHomeScreen.Click(AppHomeScreen.Button.MomentValueFromDatacore);
            Thread.Sleep(5000);
            string measurement_referencedevice = AppHomeScreen.GetText(AppHomeScreen.EditText.MeasurementValue).Trim();
            string moment_value = AppHomeScreen.GetText(AppHomeScreen.EditText.Moment).Trim();
            Logger.Info("Moment measurement info: " + moment_value);

            if (string.IsNullOrEmpty(moment_value))
            {
                Assert.Fail(AppHomeScreen.GetDatacoreErrorMsg());
            }

            if (moment_value.Equals(measurement_referencedevice))
            {
                Logger.Info("Passed: measurement values are the same: " + measurement_referencedevice);
                IapReport.Message("Passed: measurement values are the same: " + measurement_referencedevice);
            }
            else
            {
                Logger.Info("Failed: measurement values are not the same: " + measurement_referencedevice + " : " + moment_value);
                IapReport.Message("Failed: measurement values are not the same: " + measurement_referencedevice + " : " + moment_value);
            }
        }

        [Then(@"I verify the moment on screen shows '(.*)'")]
        public void ThenIVerifyTheMomentOnScreenShows(int p0)
        {
            string measurement_referencedevice = AppHomeScreen.GetText(AppHomeScreen.EditText.MeasurementValue).Trim();
            string moment_value = AppHomeScreen.GetText(AppHomeScreen.EditText.Moment).Trim();
            Assert.AreEqual(
                p0, Convert.ToInt32(moment_value),
                "Failed: Measurement values are not the same - " +
                "Measurment value is: " + measurement_referencedevice + ", Moment value is : " + moment_value
            );
        }


    }
}
