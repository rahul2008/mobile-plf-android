using OpenQA.Selenium;
using OpenQA.Selenium.Appium;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using System.Threading;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    public class Payment:IAP_Common
    {
       
        /// <summary>
        /// Collection of all EditBoxes
        /// </summary>
        public enum EditBox
        {
            CardNumber,
            CardHolderName,
            SecurityCode
        }
        /// <summary>
        /// Collection of all Buttons
        /// </summary>
        public enum Button
        {
           
            MakePayment,
            CancelPayment,
            UpButton,
            Use_this_Payment,
            Add_new_Payment,
            Cancel
        }
        /// <summary>
        /// Provides colletion of constant values representing swipe direction in the Order Summary page.
        /// </summary>
       
        /// <summary>
        /// Collection of all DropDowns
        /// </summary>
        public enum DropDown
        {
           
            ExpiryMonth,
            ExpiryYear
        }
        /// <summary>
        /// Collection of constant values Representing Swipe Direction
        /// </summary>
        public enum Direction
        {
            
            up,
            down
        }

        private static void GetDisplaySize()
        {
            int[] arr = _instance.GetElement(SearchBy.ClassName, "android.webkit.WebView").Size;
            MobileDriver.SwitchToContext(MobileDriver.ViewType.Webview);
        }



        /// <summary>
        /// Clicks the desired Button
        /// </summary>
        /// <param name="btn">btn represents the name of the Button</param>
        public static void Click(Button btn)
        {
            string desiredText = string.Empty;
            switch (btn)
            {
                case Button.MakePayment:
                    desiredText = ObjectRepository.MakePayment;
                    break;
                case Button.UpButton:
                    desiredText = ObjectRepository.UpButton;
                    break;
                case Button.CancelPayment:
                    desiredText = ObjectRepository.CancelPayment;
                    break;
                case Button.Cancel:
                    desiredText = ObjectRepository.CancelButton;
                    break;
                case Button.Use_this_Payment:
                    desiredText = ObjectRepository.UseThisPaymentMethod;
                    break;
                case Button.Add_new_Payment:
                    desiredText = ObjectRepository.AddNewPayment;
                    IMobilePageControl addNewPayment = _instance.GetElement(SearchBy.Id, ObjectRepository.UPSParcel);
                    if (null != addNewPayment)
                        addNewPayment.Click();
                    return;
            }

            Thread.Sleep(3000);
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(MobileDriver._Mdriver.PageSource);
            XmlNodeList nList = doc.GetElementsByTagName("android.widget.Button");
            nList = doc.GetElementsByTagName("android.widget.Button");
            foreach (XmlNode n1 in nList)
            {
                if (n1.Attributes["resource-id"].InnerText == "submitButton")
                {
                    string desiredBounds = n1.Attributes["bounds"].InnerText;
                    string[] a = (GetPoint(desiredBounds)).Split(' ');
                    int a1 = Convert.ToInt32(a[0]) + 5;
                    int a2 = Convert.ToInt32(a[1]) +5;
                    Logger.Info("Tap1: " + a1 + ":" + a2);
                    UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + a1 + " " + a2);
                    //UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + GetPoint(desiredBounds));
                }
                else if (n1.Attributes["resource-id"].InnerText == desiredText)
                {
                    string desiredBounds = n1.Attributes["bounds"].InnerText;
                    string[] a = (GetPoint(desiredBounds)).Split(' ');
                    int a1 = Convert.ToInt32(a[0]) + 5;
                    int a2 = Convert.ToInt32(a[1]) +5;
                    Logger.Info("Tap2: "+a1+":"+a2);
                    UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + a1 + " " + a2);
                    Thread.Sleep(1000);
                }
            }
            nList = doc.GetElementsByTagName("android.view.View");
            foreach (XmlNode n1 in nList)
            {
                if (n1.Attributes["resource-id"].InnerText == desiredText)
                {
                    string desiredBounds = n1.Attributes["bounds"].InnerText;
                    string[] a = (GetPoint(desiredBounds)).Split(' ');
                    int a1 = Convert.ToInt32(a[0]) + 5;
                    int a2 = Convert.ToInt32(a[1]) + 5;
                    UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + a1 + " " + a2);
                    //UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + GetPoint(desiredBounds));
                }
            }

        }

        /// <summary>
        /// Enters text in the desired EditText field
        /// </summary>
        /// <param name="ed">ed represents the name of the EditText field</param>
        /// <param name="txtValue">txtValue represents the string value that the user wants to enter</param>
        public static void EnterText(EditBox ed, string txtValue)
        {
            string desiredText = string.Empty;

            switch (ed)
            {
                case EditBox.CardNumber:
                    SetText("Card number *", txtValue);
                    Thread.Sleep(1000);
                    break;
                case EditBox.CardHolderName:
                    SetSpecialText(txtValue);
                    break;
                case EditBox.SecurityCode:
                    SetText("Security code *", txtValue);
                    //MobileDriver.HideKeyBoard();
                    break;
            }

        }

        private static void SetText(string content_desc, string txtValue)
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(MobileDriver._Mdriver.PageSource);
            Thread.Sleep(1000);
            if (content_desc.Equals("Card number *"))
                _instance.GetElement(SearchBy.Xpath, "//android.widget.EditText[contains(@content-desc,'Card number')]").SetText(txtValue);
            else if(content_desc.Equals("Security code *"))
            {
                _instance.GetElement(SearchBy.Xpath, "//android.widget.EditText[contains(@content-desc,'Security code')]").SetText(txtValue);
            }
            Thread.Sleep(1000);
            MobileDriver.HideKeyBoard();
          
        }

        private static void SetSpecialText(string txtValue)
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(MobileDriver._Mdriver.PageSource);
            XmlNodeList nList = doc.GetElementsByTagName("android.widget.EditText");
            if (nList[1].Attributes["content-desc"].InnerText != "")
            {
                string desiredBounds = nList[1].Attributes["bounds"].InnerText;
                string _desiredBounds = desiredBounds.Trim(new char[] { '[', ']' });
                _desiredBounds = _desiredBounds.Replace("][", ",");
                string retString = _desiredBounds.Split(',')[2] + " " + _desiredBounds.Split(',')[3];
                string test = nList[1].Attributes["content-desc"].InnerText;
                int count = test.ToCharArray().Count();
                UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + retString);
                for (int i = 0; i < count; i++)
                    UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input keyevent  KEYCODE_DEL");
            }
            doc.LoadXml(MobileDriver._Mdriver.PageSource);          
            nList = doc.GetElementsByTagName("android.widget.EditText");
            if (nList[1].Attributes["content-desc"].InnerText == "Cardholder's name *")
            {
                string desiredBounds = nList[1].Attributes["bounds"].InnerText;
                UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + GetPoint(desiredBounds));
                UtilityAuto.ExecuteAdbCommand(AutomationConstants.AdbShellCommand.InputText, txtValue, mobileDriverConfig.DEVICE_ID);
                MobileDriver.HideKeyBoard();
            }

        }

        private static string GetPoint(string desiredBounds)
        {
            string test = desiredBounds.Trim(new char[] { '[', ']' });
            test = test.Replace("][", ",");
            string retString = test.Split(',')[0] + " " + test.Split(',')[1];
            return retString;
        }

        /// <summary>
        /// Clicks on the DropDown and selects the specified value
        /// </summary>
        /// <param name="dropDown">dropDown represent the name of the dropdown</param>
        /// <param name="index">index represents the value which needs to be selected in the dropdown</param>
        public static void Select(DropDown dropDown, string index)
        {
            string desiredText = string.Empty;

            switch (dropDown)
            {
                case DropDown.ExpiryMonth:
                    desiredText = ObjectRepository.expiryMonth;
                    break;
                case DropDown.ExpiryYear:
                    desiredText = ObjectRepository.expiryYear;
                    break;
            }
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(MobileDriver._Mdriver.PageSource);
            XmlNodeList nList = doc.GetElementsByTagName("android.widget.Spinner");
            foreach (XmlNode n1 in nList)
            {
                if ((n1.Attributes["index"].Value.Equals("0") && desiredText.Contains("Month")) || (n1.Attributes["index"].Value.Equals("1") && desiredText.Contains("Year")))
                {
                    string desiredBounds = n1.Attributes["bounds"].InnerText;
                    string[] a = (GetPoint(desiredBounds)).Split(' ');
                    int a1 = Convert.ToInt32(a[0]) + 5;
                    int a2 = Convert.ToInt32(a[1]) + 5;
                    UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + a1 + " " + a2);
                    Thread.Sleep(3000);
                    //UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + GetPoint(desiredBounds));
                    break;
                }
            }
            // _instance.ClickById(desiredText);

            List<IMobilePageControl> elements = _instance.GetElements(SearchBy.Id, "android:id/text1");

            IMobilePageControl desiredElement = elements.Find(p => p.Text == index);
            if (desiredElement != null)
                desiredElement.Click();
            else
            {
                bool bVisible = false;
                while (!bVisible)
                {
                    Swipe(Direction.up, 200);
                    Thread.Sleep(1000);
                    elements = _instance.GetElements(SearchBy.Id, "android:id/text1");
                    desiredElement = elements.Find(p => p.Text == index);
                    if (desiredElement != null)
                        bVisible = true;
                }
                desiredElement.Click();

            }
        }

        /// <summary>
        /// Swipes in the desired direction by desired amount
        /// </summary>
        /// <param name="direction">Represents the direction</param>
        /// <param name="value">Represents the amount by which User wants to Swipe</param>
        public static void Swipe(Direction direction, int value)
        {
           // IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.support.v7.widget.RecyclerView");
            IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.widget.RelativeLayout");
            Point pt = new Point(control.Coordinates[0], control.Coordinates[1]);
            Size sz = new Size(control.Size[0], control.Size[1]);
            Point srcPoint = new Point((pt.X + sz.Width / 2), (pt.Y + sz.Height / 2));
            Point dstPoint = new Point(srcPoint.X, (srcPoint.Y - value));
            MobileDriver.Swipe(srcPoint.X, srcPoint.Y, dstPoint.X, dstPoint.Y);
        }

        public static void SwipePage(Direction direction, int value)
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, ObjectRepository.PaymentScroll);
            Point pt = new Point(control.Coordinates[0], control.Coordinates[1]);
            Size sz = new Size(control.Size[0], control.Size[1]);
            Point srcPoint = new Point((pt.X + sz.Width / 2), (pt.Y + sz.Height / 2));
            Point dstPoint;
            if (direction == Direction.up)
            {
                dstPoint = new Point(srcPoint.X, (srcPoint.Y - 400));
                MobileDriver.Swipe(srcPoint.X, srcPoint.Y, dstPoint.X, dstPoint.Y);
            }
            else
            {
                dstPoint = new Point(srcPoint.X, (srcPoint.Y + 400));
                MobileDriver.Swipe(srcPoint.X, srcPoint.Y, dstPoint.X, dstPoint.Y);
            }
        }

        /// <summary>
        /// Returns a string containing Screen Title
        /// </summary>
        /// <returns>a string value</returns>
        public static string GetPageTitle()
        {
            return _instance.GetElement(SearchBy.Id, ObjectRepository.ScreenTitle).Text;
        }

       
        /// <summary>
        /// Waits for the Shopping Cart Screen
        /// Returns false if the Shopping Cart Screen doesn't appear after loopcount exceeds 10
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool WaitforPaymentScreen()
        {
            IMobilePageControl element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Id, ObjectRepository.ScreenTitle);
                loopcount++;
                if (element != null)
                    break;
                if (loopcount > 10)
                    break;
            }
            Thread.Sleep(3000);
            if (element != null)
                return true;
            else
                return false;
        }


        /// <summary>
        /// Returns a string containing the Header of the page
        /// </summary>
        /// <returns>string value representing the header value</returns>
        public static string GetHeader()
        {
            string text = "";
            try
            {
                text = _instance.GetElement(SearchBy.Id, ObjectRepository.PaymentHeader).Text;
            }
            catch (Exception e)
            {
            }
            return text;
        }
        /// <summary>
        /// Provides a construct for accessing Payment methods in the Payment page
        /// </summary>
        public struct Payment_Method
        {
            public IWebElement radiobutton;
            public string card_name;
            public string card_holder_name;
            public string card_validity;
        }

        /// <summary>
        /// Returns a list of Payment Methods
        /// </summary>
        /// <returns>list of Payment methods</returns>
        public static List<Payment_Method> GetListofPaymentMethods()
        {
            List<Payment_Method> MethodList = new List<Payment_Method>();
            int count = _instance.GetElement(SearchBy.Id, ObjectRepository.RecyclerPaymentMethod).ElementInstance.FindElements(ByAndroidUIAutomator.ClassName("android.widget.RelativeLayout")).Count;
            IWebElement[] elements = new IWebElement[count];
            _instance.GetElement(SearchBy.Id, ObjectRepository.RecyclerPaymentMethod).ElementInstance.FindElements(ByAndroidUIAutomator.ClassName("android.widget.RelativeLayout")).CopyTo(elements, 0);
            for (int i = 0; i < count; i++)
            {
                Payment_Method payment = new Payment_Method();
                payment.radiobutton = elements[i].FindElement(By.Id(ObjectRepository.RadioButtonPayment));
                payment.card_name = elements[i].FindElement(By.Id(ObjectRepository.CardName)).ToString();
                payment.card_holder_name = elements[i].FindElement(By.Id(ObjectRepository.cardholdername)).Text;
                payment.card_validity = elements[i].FindElement(By.Id(ObjectRepository.CardValidity)).Text;
                MethodList.Add(payment);
            }
            return MethodList;
        }

        /// <summary>
        /// Accepts a string containing the Card Name and selects the corresponding Payment Method
        /// </summary>
        /// <param name="name">name represents the card name</param>
        public static void SelectPaymentMethod(string name)
        {
            List<Payment_Method> MethodList = GetListofPaymentMethods();
            MethodList.Find(p => p.card_name == name).radiobutton.Click();
        }
       
        public static bool IsEnabled(Button btn)
        {
            bool isEnabled = false;
            if (btn == Button.MakePayment)
                isEnabled = _instance.GetElement(SearchBy.Id, ObjectRepository.MakePayment).Enabled;
            if (btn == Button.CancelPayment)
                isEnabled = _instance.GetElement(SearchBy.Id, ObjectRepository.CancelPayment).Enabled;
            return isEnabled;
        }
        /// <summary>
        /// User enters the Payment Details
        /// </summary>
        public static void EnterPaymentDetails(string cardnumber, string expirymonth, string expiryYear,string securitycode)
        {
            Thread.Sleep(5000);
            Payment.EnterText(Payment.EditBox.CardNumber, cardnumber);
            Thread.Sleep(2000);
            Payment.Select(Payment.DropDown.ExpiryMonth, expirymonth);
            Thread.Sleep(1000);
            Payment.Select(Payment.DropDown.ExpiryYear, expiryYear);
            Thread.Sleep(1000);
            Payment.EnterText(Payment.EditBox.SecurityCode, securitycode);
            Thread.Sleep(1000);
            //UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + 64 + " " + 1740);
        }

        public static List<String> FindValidationErrors()
        {
            String src = _instance.GetSource();
            List<String> errors=new List<string>();
            errors.Add("Enter a valid card number");
            errors.Add("Enter a valid security code");
            List<String> validationErrs = new List<string>();

            foreach (String e in errors)
                if (src.Contains(e)) validationErrs.Add(e);
            return validationErrs;
        }

      /// <summary>
      /// PATTABHI: TO BE RE_VISITED
      /// </summary>
      /// <param name="tv"></param>
      /// <returns></returns>
        public static void MakeVisible(EditBox tv)
        {

           
        }


        public static void Back()
        {
            MobileDriver._Mdriver.Navigate().Back();
        }

        public static void LogSource()
        {
            Logger.Info(_instance.GetSource());
        }
    }

    public class CancelPayment
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        /// <summary>
        /// Collection of all Buttons
        /// </summary>
        public enum Button
        {
            Yes,
            No
        }

        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        private static string GetPoint(string desiredBounds)
        {
            string test = desiredBounds.Trim(new char[] { '[', ']' });
            test = test.Replace("][", ",");
            string retString = test.Split(',')[0] + " " + test.Split(',')[1];
            return retString;
        }

        /// <summary>
        /// Clicks the desired Button
        /// </summary>
        /// <param name="btn">btn represents the desired Button</param>
        public static void Click(Button btn)
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(MobileDriver._Mdriver.PageSource);
            XmlNodeList nList = doc.GetElementsByTagName("android.widget.Button");
            foreach (XmlNode n1 in nList)
            {
                if (n1.Attributes["content-desc"].InnerText == btn.ToString())
                {
                    string desiredBounds = n1.Attributes["bounds"].InnerText; 
                    string[] a = (GetPoint(desiredBounds)).Split(' ');
                    int a1 = Convert.ToInt32(a[0]) + 5;
                    int a2 = Convert.ToInt32(a[1]) + 5;
                    UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + a1 + " " + a2);

                    break;
                }
            }
        }

        /// <summary>
        /// Returns true if the Cancel Payment View is present
        /// </summary>
        /// <returns>boolean value</returns>
        public static bool IsExist()
        {
            bool bExist = false;
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(MobileDriver._Mdriver.PageSource);
            XmlNodeList nList = doc.GetElementsByTagName("android.view.View");
            foreach (XmlNode n1 in nList)
            {
                if (n1.Attributes["resource-id"].InnerText == "lb-back")
                {
                    bExist = true;
                    break;
                }
            }
            return bExist;
        }
    }
}
