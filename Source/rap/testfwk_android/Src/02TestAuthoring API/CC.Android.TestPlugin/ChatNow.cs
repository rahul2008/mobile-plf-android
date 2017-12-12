using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class ChatNow
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        public enum Button
        {
            Backto_HomeScreen,
            Start_Chat,
            Send,
            Close
        }

        public enum CheckBox
        {
            Check_box
        }
        public enum TextBox
        {
            First_Name,
            Last_Name,
            Email,
            Mobile_No,
            Query_Field
        }

        /// <summary>
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetHeaderTextChatNow()
        {
            return _instance.GetTextById(ConsumerCare.Android.ChatNow.HeaderText);
        }
        public static bool Chat_NowScreen()
        {
            bool bVisible = false;
            if (GetHeaderTextChatNow() == "Chat now")
                bVisible = true;
            return bVisible;

        }

        /// <summary>
        /// waiting for chatnow home screen
        /// </summary>
        /// <returns></returns>
        public static bool WaitforChatNow1HomeScreen()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ChatNow.HeaderText);
                loopCount++;
                if (homeScreenElement != null)
                    break;
                if (loopCount > 20)
                    break;
                Thread.Sleep(500);
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// checking whether btn visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {
            bool isVisble = false;
            try
            {
                if (btn == Button.Backto_HomeScreen)
                    isVisble = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ChatNow.BacktoHome).Displayed;
                if (btn == Button.Start_Chat)
                    isVisble = _instance.GetElement(SearchBy.Name, "Start chat").Displayed;
                if (btn == Button.Close)
                    isVisble = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ChatNow.Close).Displayed;

            }
            catch(Exception e)
            {
                isVisble = false;
            }
            return isVisble;
        }

         /// <summary>
        /// checking whether button is enabled or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            try
            {
                
                if (btn == Button.Backto_HomeScreen)
                    isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ChatNow.BacktoHome).Enabled;
                if (btn == Button.Start_Chat)
                    isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ChatNow.StartChat).Enabled;
            }
            catch (Exception)
            {
                isEnable = false;
            }
            
            return isEnable;
        }

        /// <summary>
        /// checking whether button or clickable or not
        /// </summary>
        /// <param name="btn"></param>
        public static void Click(Button btn)
        {
            if (btn == Button.Backto_HomeScreen)
                _instance.ClickById(ConsumerCare.Android.ChatNow.BacktoHome);
            if (btn == Button.Start_Chat)
                _instance.GetElement(SearchBy.Name, "Start chat").Click();
            if(btn == Button.Send)
                _instance.GetElement(SearchBy.Name, "Send").Click();
            if(btn == Button.Close)
                _instance.ClickById(ConsumerCare.Android.ChatNow.Close);
        }

        public static void Deviceback()
        {
            MobileDriver.FireKeyEvent(4);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="firstName"></param>
        /// <param name="lastName"></param>C:\TFS_Workspace\CDP_TestAutomation\Src\02TestAuthoring API\CC.Android.TestPlugin\DialogsFindPhilips.cs
        /// <param name="emailID"></param>
        /// <param name="phoneNo"></param>
        public static void Entry_Details(string firstName, string lastName, string emailID, string phoneNo)
        {
            ChatNow.WaitforChatNow1HomeScreen();
            if ((_instance.GetElement(SearchBy.ClassName, "android.widget.EditText")) != null)
            {
                int count = 0;
                List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, "android.widget.EditText");
                if(count == 0)
                {
                    foreach (IMobilePageControl cnt in control)
                    {
                        if (count == 0)
                        {
                            cnt.Click();
                            count++;
                            cnt.SetText(firstName);
                            break;
                        }
                    }
                    if(count == 1)
                    {
                        foreach (IMobilePageControl cnt in control.Skip(1))
                        {

                            cnt.Click();
                            count++;
                            cnt.SetText(lastName);
                            break;
                        }
                    
                    }
                    if (count == 2)
                    {
                        foreach (IMobilePageControl cnt in control.Skip(2))
                        {

                            cnt.Click();
                            count++;
                            cnt.SetText(emailID);
                                                
                            break;
                        }

                    }
                    if (count == 3)
                    {
                        foreach (IMobilePageControl cnt in control.Skip(3))
                        {

                            cnt.Click();
                            count++;
                            cnt.SetText(phoneNo);
                            break;
                        }

                    }
                }
            }
        }

        public static void Entry_Query(string query)
        {
            ChatNow.WaitforChatNow1HomeScreen();
            if ((_instance.GetElement(SearchBy.ClassName, "android.widget.EditText")) != null)
            {
                List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, "android.widget.EditText");
                foreach (IMobilePageControl cnt in control)
                {
                    cnt.Click();
                    cnt.SetText(query);
                    break;
                }               
            }
        }

       

        /// <summary>
        /// Clicks on desired CheckBox
        /// </summary>
        /// <param name="chkBox">chkBox represents the name of the CheckBox</param>
        public static void Terms_Conditions(CheckBox chkBox)
        {
            if (_instance.GetElement(SearchBy.ClassName, "android.widget.CheckBox") != null)
            {
                if (chkBox == CheckBox.Check_box)
                {
                    _instance.GetElement(SearchBy.ClassName, "android.widget.CheckBox").Click();
                }
            }
        }
    }
}

