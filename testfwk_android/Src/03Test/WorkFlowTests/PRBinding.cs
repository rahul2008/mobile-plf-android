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
using Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin;
using System.Xml;
using System.Reflection;
using System.IO;
using Philips.CDP.Automation.IAP.Tests.Workflows;
using Philips.SIG.Automation.Android.CDPP.AppFramework_TestPlugin;
using ProdReg.Android.TestPlugin;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]
    public class PRBinding
    {

        [Then(@"Verify that the user should land to home screen")]
        public void ThenVerifyThatTheUserShouldLandToHomeScreen()
        {
            AppHomeScreen.WaitforAppFrameworkHomeScreen(60);
            string title = AppHomeScreen.GetScreenTitleAppFameworkHomeScreen();
            if (title != "Mobile App Home")
            {
                Logger.Fail("Error: The user is not in the App Framework Home Screen.");
            }
        }

        [Then(@"select Register your Product from support screen")]
        public void ThenSelectRegisterYourProductFromSupportScreen()
        {
            LaunchPR.Click(LaunchPR.Button.ProductReg);
            PRHome.WaitforProdRegHomeScreen();

        }


        [Then(@"I click on Date of Purchase")]
        public void ThenIClickOnDateOfPurchase()
        {
            Thread.Sleep(3000);
            PRHome.Click(PRHome.Button.DatePurchase);
            Thread.Sleep(3000);
        }


        [Then(@"I select the date of purchase as ""(.*)"",""(.*)"",""(.*)""")]
        public void ThenISelectTheDateOfPurchaseAs(string p0, string p1, string p2)
        {
            PRHome.SelectDate(p0, p1, p2);

        }

        [Then(@"I verify that the user is able to see  successfull product message")]
        public void ThenIVerifyThatTheUserIsAbleToSeeSuccessfullProductMessage()
        {
            PRSuccess.SuccessMessage();
            PRSuccess.Click(PRSuccess.Button.PR_Continue);
        }

        //[Then(@"I Register the Product from Support screen")]
        //public void ThenIRegisterTheProductFromSupportScreen()
        //{
        //    Then(string.Format("select Register your Product from support screen"));
        //    Then(string.Format("I click on Date of Purchase"));
        //    Then (string.Format("I select the date of purchase as \"1\",\"April\",\"2016\""));
        //    Then(string.Format("I verify that the user is able to see  successfull product message"));
        //}

        [Then(@"I click on Register")]
        public void ThenIClickOnRegister()
        {
            PRHome.Click(PRHome.Button.PRRegister);
            Thread.Sleep(2000);

        }

        [Then(@"I verify that the user is navigated to Support screen on clicking continue")]
        public void ThenIVerifyThatTheUserIsNavigatedToSupportScreenOnClickingContinue()
        {
            PRSuccess.Click(PRSuccess.Button.PR_Continue);
            Support.IsVisible1();
        }


        [Then(@"I check for behaviour if the product is already registered")]
        public void ThenICheckForBehaviourIfTheProductIsAlreadyRegistered()
        {
            string Text = PRSuccess.RepeatProdRegisterMessage();
            if (Text != "All the products that are currently connected to this app have been registered successfully!")
            {
                IapReport.Fail("The user is not in Support Screen ");

            }

        }

    }
}
