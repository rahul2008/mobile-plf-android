﻿// ------------------------------------------------------------------------------
//  <auto-generated>
//      This code was generated by SpecFlow (http://www.specflow.org/).
//      SpecFlow Version:1.9.0.77
//      SpecFlow Generator Version:1.9.0.0
//      Runtime Version:4.0.30319.34209
// 
//      Changes to this file may cause incorrect behavior and will be lost if
//      the code is regenerated.
//  </auto-generated>
// ------------------------------------------------------------------------------
#region Designer generated code
#pragma warning disable
namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    using TechTalk.SpecFlow;
    
    
    [System.CodeDom.Compiler.GeneratedCodeAttribute("TechTalk.SpecFlow", "1.9.0.77")]
    [System.Runtime.CompilerServices.CompilerGeneratedAttribute()]
    [TechTalk.SpecRun.FeatureAttribute("UJ02", Description="In order to avoid silly mistakes\r\nAs a math idiot\r\nI want to be told the sum of t" +
        "wo numbers", SourceFile="UJ02.feature", SourceLine=0)]
    public partial class UJ02Feature
    {
        
        private static TechTalk.SpecFlow.ITestRunner testRunner;
        
#line 1 "UJ02.feature"
#line hidden
        
        [TechTalk.SpecRun.FeatureInitialize()]
        public virtual void FeatureSetup()
        {
            testRunner = TechTalk.SpecFlow.TestRunnerManager.GetTestRunner();
            TechTalk.SpecFlow.FeatureInfo featureInfo = new TechTalk.SpecFlow.FeatureInfo(new System.Globalization.CultureInfo("en-US"), "UJ02", "In order to avoid silly mistakes\r\nAs a math idiot\r\nI want to be told the sum of t" +
                    "wo numbers", ProgrammingLanguage.CSharp, ((string[])(null)));
            testRunner.OnFeatureStart(featureInfo);
        }
        
        [TechTalk.SpecRun.FeatureCleanup()]
        public virtual void FeatureTearDown()
        {
            testRunner.OnFeatureEnd();
            testRunner = null;
        }
        
        public virtual void TestInitialize()
        {
        }
        
        [TechTalk.SpecRun.ScenarioCleanup()]
        public virtual void ScenarioTearDown()
        {
            testRunner.OnScenarioEnd();
        }
        
        public virtual void ScenarioSetup(TechTalk.SpecFlow.ScenarioInfo scenarioInfo)
        {
            testRunner.OnScenarioStart(scenarioInfo);
        }
        
        public virtual void ScenarioCleanup()
        {
            testRunner.CollectScenarioErrors();
        }
        
        [TechTalk.SpecRun.ScenarioAttribute("UserJourney-2", new string[] {
                "mytag"}, SourceLine=6)]
        public virtual void UserJourney_2()
        {
            TechTalk.SpecFlow.ScenarioInfo scenarioInfo = new TechTalk.SpecFlow.ScenarioInfo("UserJourney-2", new string[] {
                        "mytag"});
#line 7
this.ScenarioSetup(scenarioInfo);
#line 8
testRunner.Given("User is on the Home Screen", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Given ");
#line 9
testRunner.And("is a first time user", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "And ");
#line 10
testRunner.When("User selects country store and clicks on Shop Now", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "When ");
#line 11
testRunner.Then("verify user is in product catalog view", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 12
testRunner.Then("select \"HX9042/64\" from product catalog view", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 13
testRunner.Then("select \"HX8071/10\" from product catalog view", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 14
testRunner.Then("Verify shopping cart icon is \"2\" in all views", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 15
testRunner.Then("go to shopping cart", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 16
testRunner.When("User increments quantity of \"Sonicare AdaptiveClean Standard sonic toothbrush hea" +
                    "ds\" to \"2\"", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "When ");
#line 17
testRunner.Then("Verify shopping cart icon is \"3\" in all views", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 18
testRunner.Then("go to shopping cart", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 19
testRunner.When("User increments quantity of \"Sonicare TongueCare+ Tongue cleaning starter kit\" to" +
                    " \"2\"", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "When ");
#line 20
testRunner.Then("Verify shopping cart icon is \"4\" in all views", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 21
testRunner.Then("go to shopping cart", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 22
testRunner.Then("verify delivery and VAT", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 23
testRunner.Then("verify the total number of product list and the total cost", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 25
testRunner.Then("user clicks on checkout", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 26
testRunner.Then("Verify that the user is in address screen", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 27
testRunner.Then("Enter a valid shipping address and click continue", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 28
testRunner.Then("add new payment method", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 29
testRunner.When("billing address is different from shipping address", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "When ");
#line 30
testRunner.Then("enter valid billing address and click continue", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 32
testRunner.Then("Press Continue to Reach Ordersummary Screen", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 33
testRunner.Then("verify user is in Order summary page shippingaddress,Voucher discount,UPS Parcel," +
                    "Totalcost,Vat and totalquantity", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 34
testRunner.Then("User clicks on Paynow", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 36
testRunner.Then("verify user is on payment screen", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 37
testRunner.When("Invalid card data is entered", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "When ");
#line 38
testRunner.Then("verify user gets a failed message", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line 39
testRunner.When("Valid card data is entered", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "When ");
#line 40
testRunner.Then("Verify user is able to complete transaction", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line hidden
            this.ScenarioCleanup();
        }
        
        [TechTalk.SpecRun.TestRunCleanup()]
        public virtual void TestRunCleanup()
        {
            TechTalk.SpecFlow.TestRunnerManager.GetTestRunner().OnTestRunEnd();
        }
    }
}
#pragma warning restore
#endregion
