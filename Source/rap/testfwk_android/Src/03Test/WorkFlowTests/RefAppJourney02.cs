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
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using UserRegistration_TestPlugin;
using Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]

    public class RefAppJourney02
    {
        [Given(@"the User is able to view Welcome screens of the application")]
        public void GivenTheUserIsAbleToViewWelcomeScreensOfTheApplication()
        {
            WelcomeScreen.WelcomeScreenImage();
            Thread.Sleep(10000);
        }

        [Then(@"the user can see option to Skip the Introduction screens")]
        public void ThenTheUserCanSeeOptionToSkipTheIntroductionScreens()
        {
            bool isVisible = false;
            isVisible = WelcomeScreen.IsVisible(WelcomeScreen.Button.Skip);
            if (!isVisible)
            {
                IapReport.Fail("The Skip button is not visible");
            }
        }

        [Then(@"verify that the user can see Skip but  Done should not be visible")]
        public void ThenVerifyThatTheUserCanSeeSkipButDoneShouldNotBeVisible()
        {
            try
            {
                bool x = WelcomeScreen.IsVisible(WelcomeScreen.Button.Skip);
                bool y = WelcomeScreen.IsVisible(WelcomeScreen.Button.Done);

                if ((x == true) && (y == false))
                {
                    Logger.Info("The skip and done button are not visible together");
                }

            }
            catch (Exception e)
            {
                Logger.Fail("Test case : The skip and done button are not visible together is not validated");
            }
        }

        [Then(@"validate that the user do not see next arrow when he is  on the last screen")]
        public void ThenValidateThatTheUserDoNotSeeNextArrowWhenHeIsOnTheLastScreen()
        {
            WelcomeScreen.Click(WelcomeScreen.Button.RightArrow);

            try
            {
                bool a1 = WelcomeScreen.IsVisible(WelcomeScreen.Button.Skip);
                bool b2 = WelcomeScreen.IsVisible(WelcomeScreen.Button.LeftArrow);
                int c3 = WelcomeScreen.CarouselHighlighted();
                bool d4 = WelcomeScreen.IsVisible(WelcomeScreen.Button.RightArrow);
                bool e5 = WelcomeScreen.IsVisible(WelcomeScreen.Button.Done);

                if ((a1 == false) && (e5 == true))
                {
                    Logger.Info("verified that the user is on \"Third\" screen of the welcome screen");
                }
                if ((b2 == true) && (d4 == false) && (c3 == 2))
                {
                    Logger.Info("verified that the user is on \"Third\" screen of the welcome screen");
                }
            }
            catch (Exception e)
            {
                Logger.Fail("The user is not on the last screen of the welcome screen ");
            }

        }


        [Then(@"validate that the  dots indicate me how many screens are available\.")]
        public void ThenValidateThatTheDotsIndicateMeHowManyScreensAreAvailable_()
        {
            int screenCount = WelcomeScreen.CarouselNumber();
            if (screenCount != 3)
            {
                IapReport.Fail("The total number screen are not 3");
            }
        }

        [Then(@"the user click on Done")]
        public void ThenTheUserClickOnDone()
        {
            try
            {
                WelcomeScreen.Click(WelcomeScreen.Button.RightArrow);
                WelcomeScreen.Click(WelcomeScreen.Button.Done);

            }
            catch (Exception e)
            {
                IapReport.Fail("Fail : Done button is not visible to click", e);
            }

        }

        [Then(@"I verify that the Receive promotional is not enabled in the Account Settings")]
        public void ThenIVerifyThatTheReceivePromotionalIsNotEnabledInTheAccountSettings()
        {
            string isPresent = Settings.ReceivePromotional();
            if (isPresent == "Receive Philips'")
            {
                bool toggleStatus = false;
                if (toggleStatus = Settings.ReceiveProStatus())
                {
                    IapReport.Message("The receive promotional is enabled");
                }
                else
                {
                    IapReport.Message("The receive promotional is disabled");
                }
            }
        }

        [Then(@"the user click Phone Back button")]
        public void ThenTheUserClickPhoneBackButton()
        {
            MobileDriver.FireKeyEvent(4);
        }

        [Given(@"I launch the Account Settings")]
        public void GivenILaunchTheAccountSettings()
        {
            AppHomeScreen.Click(AppHomeScreen.Button.HamburgerIcon);
            AppHomeScreen.Click(AppHomeScreen.Button.Settings);
            Thread.Sleep(3000);
            bool Verify = Settings.IsVisible();
            if (Verify != true)
            {
                IapReport.Fail("The user is not in  Account Setting Screen");
            }
        }

        [Given(@"the user clicks on Login button")]
        public void GivenTheUserClicksOnLoginButton()
        {
            try
            {
                Settings.Click(Settings.Button.Login);
            }
            catch (Exception e)
            {
                IapReport.Fail("Fail : The login Button was clicked in Account Setting", e);
            }
        }

        [Then(@"user verifies that User Registration home screen is successfully opened")]
        public void ThenUserVerifiesThatUserRegistrationHomeScreenIsSuccessfullyOpened()
        {
            Registration.Wait();
            bool logVerify = Registration.Login_Screen();
            if (!logVerify)
            {
                IapReport.Fail("The User is not in user registration screen");
            }
        }

        [Then(@"the user can see an option to mark Done when he is on the last screen\.")]
        public void ThenTheUserCanSeeAnOptionToMarkDoneWhenHeIsOnTheLastScreen_()
        {
            try
            {
                bool x = WelcomeScreen.IsVisible(WelcomeScreen.Button.Done);
                int z = WelcomeScreen.CarouselHighlighted();

                if ((x == true) && (z == 2))
                {
                    Logger.Info("verified that the user can see an option to mark Done when he is on the last screen");
                }

            }
            catch (Exception e)
            {
                Logger.Fail("The done option is not seen the last screen");
            }
        }

        [Then(@"the user should be able to move to next screen when he swipe left\.")]
        public void ThenTheUserShouldBeAbleToMoveToNextScreenWhenHeSwipeLeft_()
        {
            WelcomeScreen.SwipeLeft(480);
        }

        [Then(@"the user should be able to move to the previous screen when he swipe right\.")]
        public void ThenTheUserShouldBeAbleToMoveToThePreviousScreenWhenHeSwipeRight_()
        {
            WelcomeScreen.SwipeRight(480);
        }



        [Then(@"validate that  the dot which is highlighted indicates me on which screen number I am at\.")]
        public void ThenValidateThatTheDotWhichIsHighlightedIndicatesMeOnWhichScreenNumberIAmAt_()
        {
            try
            {
                bool x = WelcomeScreen.IsVisible(WelcomeScreen.Button.Skip);
                bool y = WelcomeScreen.IsVisible(WelcomeScreen.Button.RightArrow);
                int z = WelcomeScreen.CarouselHighlighted();

                if ((x == true) && (y == true) && (z == 0))
                {
                    Logger.Info("verified that the dot which is highlighted indicates me on which screen number I am at");
                }

            }
            catch (Exception e)
            {
                Logger.Fail("The dot which is highlighted is not validated");
            }

        }


        [Then(@"the user can see dots \(navigation/page control\) on the bottom\.")]
        public void ThenTheUserCanSeeDotsNavigationPageControlOnTheBottom_()
        {
            int screenCount = WelcomeScreen.CarouselNumber();
            if (screenCount == -1)
            {
                IapReport.Fail("The the user cannot see dots on the bottom");
            }
            else
            {
                IapReport.Message("The the user can see dots on the bottom");
            }
        }


        [Then(@"validate that the user can see the next arrow if there are more than one screens")]
        public void ThenValidateThatTheUserCanSeeTheNextArrowIfThereAreMoreThanOneScreens()
        {
            bool isVisible = false;
            isVisible = WelcomeScreen.IsVisible(WelcomeScreen.Button.RightArrow);
            if (!isVisible)
            {
                IapReport.Fail("The right arrow is not visible");
            }
        }

        [Then(@"validate that the user can see Previous arrow if he  have moved to next screen")]
        public void ThenValidateThatTheUserCanSeePreviousArrowIfHeHaveMovedToNextScreen()
        {
            bool isVisible = false;
            isVisible = WelcomeScreen.IsVisible(WelcomeScreen.Button.LeftArrow);
            if (!isVisible)
            {
                IapReport.Fail("The previous arrow is not visible");
            }
        }

        [Then(@"verify that the user can see Done but skip should not be visible")]
        public void ThenVerifyThatTheUserCanSeeDoneButSkipShouldNotBeVisible()
        {
            try
            {
                bool x = WelcomeScreen.IsVisible(WelcomeScreen.Button.Skip);
                bool y = WelcomeScreen.IsVisible(WelcomeScreen.Button.Done);

                if ((x == false) && (y == true))
                {
                    Logger.Info("The skip and done button are not visible together");
                }

            }
            catch (Exception e)
            {
                Logger.Fail("Test case : The skip and done button are not visible together is not validated");
            }
        }


        [Then(@"validate that the user do not see Previous arrow when he is on the first screen")]
        public void ThenValidateThatTheUserDoNotSeePreviousArrowWhenHeIsOnTheFirstScreen()
        {
            bool isVisible = false;
            isVisible = WelcomeScreen.IsVisible(WelcomeScreen.Button.LeftArrow);
            if (isVisible)
            {
                IapReport.Fail("The left arrow is not visible");
            }
        }

        [Then(@"validate that the user can click on the previous button and go back to the previous screen")]
        public void ThenValidateThatTheUserCanClickOnThePreviousButtonAndGoBackToThePreviousScreen()
        {
            WelcomeScreen.Click(WelcomeScreen.Button.LeftArrow);
        }

        [Given(@"the user can see option to Skip the Introduction screens")]
        public void GivenTheUserCanSeeOptionToSkipTheIntroductionScreens()
        {
            bool isVisible = false;
            isVisible = WelcomeScreen.IsVisible(WelcomeScreen.Button.Skip);
            if (!isVisible)
            {
                IapReport.Fail("The previous arrow is not visible");
            }
        }


        [Then(@"validate that the user can click on the next arrow and move to next screen")]
        public void ThenValidateThatTheUserCanClickOnTheNextArrowAndMoveToNextScreen()
        {

            bool x = WelcomeScreen.IsVisible(WelcomeScreen.Button.Skip);
            bool y = WelcomeScreen.IsVisible(WelcomeScreen.Button.RightArrow);
            int z = WelcomeScreen.CarouselHighlighted();

            if ((x == true) && (y == true) && (z == 0))
            {
                Logger.Info("verified that the user is on \"first\" screen of the welcome screen");
                WelcomeScreen.Click(WelcomeScreen.Button.RightArrow);

                bool a = WelcomeScreen.IsVisible(WelcomeScreen.Button.Skip);
                bool b = WelcomeScreen.IsVisible(WelcomeScreen.Button.RightArrow);
                int c = WelcomeScreen.CarouselHighlighted();
                bool d = WelcomeScreen.IsVisible(WelcomeScreen.Button.LeftArrow);

                if ((a == true) && (b == true) && (c == 1) && (d == true))
                {
                    Logger.Info("verified that the user is on \"Second\" screen of the welcome screen");
                }
                else
                {
                    IapReport.Fail("The user is not navigated to the next screen");
                }

            }
            else
            {
                IapReport.Fail("The test case : Then validate that the user can click on the next arrow and move to next screen is not verified");
            }

        }

        [Given(@"user is in the support screen")]
        public void GivenUserIsInTheSupportScreen()
        {
            bool isPresent = false;
            isPresent = Support.IsVisible1();
            if (!isPresent)
            {
                IapReport.Fail("The user is not in support screen");
            }

        }


    }
}
