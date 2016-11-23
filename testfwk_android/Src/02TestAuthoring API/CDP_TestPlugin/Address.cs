using OpenQA.Selenium.Appium.MultiTouch;
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
using System.Threading;
using System.Threading.Tasks;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Address.
    /// </summary>
    public class Address: IAP_Common
    {
        //static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();

        /// <summary>
        /// Collection of all EditTexts
        /// </summary>
        public  enum EditText
        {
            FirstName,
            LastName,
            Salutation,
            AddressLine1,
            AddressLine2,
            City,
            PostalCode,
            Country,
            State,
            Email,
            Phone
        }

        /// <summary>
        /// Collection of constant values Representing Swipe Direction
        /// </summary>
        public enum Direction
        {
            Up,
            Down
        }

        /// <summary>
        /// Provides collection of all the buttons
        /// </summary>
        public  enum Button
        {
            Continue,
            Cancel,
            UpButton,
            Switch,
            Deliver_to_This_Address,
            Add_a_new_Address,
        }
        
        /// <summary>
        /// Enters text into the corresponding EditText field
        /// </summary>
        /// <param name="et">et represents the name of the EditText </param>
        /// <param name="txtValue">txtValue represents the string to be entered</param>
        public static void EnterText(EditText et,string txtValue)
        {
            string desiredText=string.Empty;
            IMobilePageControl desiredElement = null;
            List<IMobilePageControl> elements = new List<IMobilePageControl>();

            switch (et)
            {
                case EditText.FirstName:
                    desiredText = ObjectRepository.FirstName;
                    break;
                case EditText.LastName:
                    desiredText = ObjectRepository.LastName;
                    break;
                case EditText.Salutation:
                    desiredText = ObjectRepository.Salutation;
                    break;
                case EditText.AddressLine1:
                    desiredText = ObjectRepository.AddressLine1;
                    break;
                case EditText.AddressLine2:
                    desiredText = ObjectRepository.AddressLine2;
                    break;
                case EditText.City:
                    desiredText = ObjectRepository.City;
                    break;
                case EditText.PostalCode:
                    desiredText = ObjectRepository.PostalCode;
                    break;
                case EditText.Country:
                    desiredText = ObjectRepository.Country;
                    break;
                case EditText.State:
                    desiredText = ObjectRepository.State;
                    break;
                case EditText.Email:
                    desiredText = ObjectRepository.Email;
                    break;
                case EditText.Phone:
                    desiredText = ObjectRepository.Phone;
                    break;
            }
            if(et == EditText.Salutation)
            {
                 _instance.ClickById(desiredText);
                 elements = _instance.GetElements(SearchBy.Id, "com.philips.cdp.di.iapdemo:id/listtextview");
             
                 desiredElement = elements.Find(p => p.Text == txtValue);
                 if (desiredElement != null)
                 {
                     desiredElement.Click();
                     return;
                 }
                 else
                 {
                     bool bVisible = false;
                     while (!bVisible)
                     {
                         Swipe(Direction.Up, 200);
                         elements = _instance.GetElements(SearchBy.Id, "com.philips.cdp.di.iapdemo:id/listtextview");
                         desiredElement = elements.Find(p => p.Text == txtValue);
                         if (desiredElement != null)
                             bVisible = true;
                     }
                     desiredElement.Click();
                     return;
                 }

            }
            else if(et == EditText.State)
            {
                _instance.ClickById(desiredText);
                 elements = _instance.GetElements(SearchBy.Id, "com.philips.cdp.di.iapdemo:id/listtextview");
             
                 desiredElement = elements.Find(p => p.Text == txtValue);
                 if (desiredElement != null)
                 {
                     desiredElement.Click();
                     return;
                 }
                 else
                 {
                     bool bVisible = false;
                     while (!bVisible)
                     {
                         Swipe(Direction.Up, 200);
                         elements = _instance.GetElements(SearchBy.Id, "com.philips.cdp.di.iapdemo:id/listtextview");
                         desiredElement = elements.Find(p => p.Text == txtValue);
                         if (desiredElement != null)
                             bVisible = true;
                     }
                     desiredElement.Click();
                     return;
                 }
            }
            //in case the desired element is not in visible area
            desiredElement = _instance.GetElement(SearchBy.Id, desiredText);
            if(desiredElement == null)
            {
                bool bVisible = false;
                int count = 10;
                while (!bVisible && count > 0)
                {
                    Swipe(Direction.Up, 200);
                    elements = _instance.GetElements(SearchBy.Id, desiredText);
                    desiredElement = elements.Find(p => p.Text == txtValue);
                    if (desiredElement != null)
                        bVisible = true;
                    count--;
                }
            }
            if (desiredElement != null)
            {
                desiredElement.SetText(txtValue+"\t");
            }
            Address.HideKeyboard("Android");
        }

        /// <summary>
        /// Swipes across the Screen by the desired amount
        /// </summary>
        /// <param name="direction">direction represents the Direction of Swipe</param>
        /// <param name="value">value represents the amount by which the screen needs to be swiped</param>
        public static void Swipe(Direction direction, int value)
        {
            Thread.Sleep(3000);
            //IMobilePageControl control = _instance.GetElement(SearchBy.Id, ObjectRepository.ScrollView);
            IMobilePageControl control = _instance.GetElementNoWait(null,SearchBy.Id, ObjectRepository.ScrollView);
            if (null == control) return;
            Point pt = new Point(control.Coordinates[0], control.Coordinates[1]);
            Size sz = new Size(control.Size[0], control.Size[1]);
            Point srcPoint = new Point((pt.X + sz.Width / 2), (pt.Y + sz.Height / 2));
            Point dstPoint;
            if (direction == Direction.Up)
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
        /// Returns the text present in the EditText field
        /// </summary>
        /// <param name="et">et represents the name of the EditText, whose text value is required</param>
        /// <returns>a string value</returns>
        public static string GetText(EditText et)
        {
            string desiredText=string.Empty;

            switch (et)
            {
                case EditText.FirstName:
                    desiredText = ObjectRepository.FirstName;
                    break;
                case EditText.LastName:
                    desiredText = ObjectRepository.LastName;
                    break;
                case EditText.AddressLine1:
                    desiredText = ObjectRepository.AddressLine1;
                    break;
                case EditText.AddressLine2:
                    desiredText = ObjectRepository.AddressLine2;
                    break;
                case EditText.City:
                    desiredText = ObjectRepository.City;
                    break;
                case EditText.PostalCode:
                    desiredText = ObjectRepository.PostalCode;
                    break;
                case EditText.Country:
                    desiredText = ObjectRepository.Country;
                    break;
                case EditText.Email:
                    desiredText = ObjectRepository.Email;
                    break;
                case EditText.Phone:
                    desiredText = ObjectRepository.Phone;
                    break;
            }
            return _instance.GetElement(SearchBy.Id, desiredText).Text;
        }

       /// <summary>
        /// Returns a string containing the Title of the Page
        /// </summary>
        /// <returns>a string value</returns>
        public static string GetHeader()
        {
            return _instance.GetElement(SearchBy.Id, ObjectRepository.Title).Text;
        }

        /// <summary>
        /// Clicks the desired button
        /// </summary>
        /// <param name="btn">btn represents the name of the Button to be clicked</param>
        public static void Click(Button btn)
        {
            string desiredText = string.Empty;
            switch (btn)
            {
                case Button.Cancel:
                    desiredText = ObjectRepository.CancelButton;
                    break;
                case Button.UpButton:
                    desiredText = ObjectRepository.UpButton;
                    break;
                case Button.Continue:
                    desiredText = ObjectRepository.ContinueButton;
                    break;
                case Button.Switch:
                    desiredText = ObjectRepository.Switch;
                    break;
                case Button.Add_a_new_Address:
                    desiredText = ObjectRepository.NewAddressButton;
                    break;
                case Button.Deliver_to_This_Address:
                    desiredText = ObjectRepository.DeliverButton;
                    break;
                
            }

            IMobilePageControl desiredElement = _instance.GetElement(SearchBy.Id, desiredText);

            if (desiredElement != null)
            {
                desiredElement.Click();
                return;
            }
            else
            {
                bool bVisible = false;
                while (!bVisible)
                {
                    Swipe(Direction.Up, 200);
                    desiredElement = _instance.GetElement(SearchBy.Id, desiredText);
                    if (desiredElement != null)
                        bVisible = true;
                }
            }

            desiredElement.Click();
        }

        /// <summary>
        /// Returns true of the desired button is present
        /// </summary>
        /// <param name="btn">btn represents the name of the Button</param>
        /// <returns>a boolean value</returns>
        public static bool IsExist(Button btn)
        {
            bool bExist=false;
             string desiredText = string.Empty;
            switch (btn)
            {
                case Button.Cancel:
                    desiredText = ObjectRepository.CancelButton;
                    break;
                case Button.UpButton:
                    desiredText = ObjectRepository.UpButton;
                    break;
                case Button.Continue:
                    desiredText = ObjectRepository.ContinueButton;
                    break;
                case Button.Switch:
                    desiredText = ObjectRepository.Switch;
                    break;
                case Button.Add_a_new_Address:
                    desiredText = ObjectRepository.NewAddressButton;
                    break;
                case Button.Deliver_to_This_Address:
                    desiredText = ObjectRepository.DeliverButton;
                    break;
            }
            if (_instance.GetElement(SearchBy.Id, desiredText) != null)
                bExist = true;
            else
                bExist = false;
            return bExist;
        }

        /// <summary>
        /// Returns true if the desired button is Enabled
        /// </summary>
        /// <param name="btn">btn represents the name of the Button</param>
        /// <returns>a boolean value</returns>
        public static bool IsEnabled(Button btn)
        {
            string desiredText = string.Empty;
            switch (btn)
            {
                case Button.Cancel:
                    desiredText = ObjectRepository.CancelButton;
                    break;
                case Button.UpButton:
                    desiredText = ObjectRepository.UpButton;
                    break;
                case Button.Continue:
                    desiredText = ObjectRepository.ContinueButton;
                    break;
                case Button.Switch:
                    desiredText = ObjectRepository.Switch;
                    break;
            }
            return _instance.GetElement(SearchBy.Id, desiredText).Enabled;
              
        }

        /// <summary>
        /// Returns true if the desired EditText field is Enabled
        /// </summary>
        /// <param name="et">btn represents the name of the EditText field</param>
        /// <returns>a boolean value</returns>
        public static bool IsEnabled(EditText et)
        {
            string desiredText = string.Empty;
            switch (et)
            {
                case EditText.FirstName:
                    desiredText = ObjectRepository.FirstName;
                    break;
                case EditText.LastName:
                    desiredText = ObjectRepository.LastName;
                    break;
                case EditText.AddressLine1:
                    desiredText = ObjectRepository.AddressLine1;
                    break;
                case EditText.AddressLine2:
                    desiredText = ObjectRepository.AddressLine2;
                    break;
                case EditText.City:
                    desiredText = ObjectRepository.City;
                    break;
                case EditText.PostalCode:
                    desiredText = ObjectRepository.PostalCode;
                    break;
                case EditText.Country:
                    desiredText = ObjectRepository.Country;
                    break;
                case EditText.Email:
                    desiredText = ObjectRepository.Email;
                    break;
                case EditText.Phone:
                    desiredText = ObjectRepository.Phone;
                    break;
            }
            return _instance.GetElement(SearchBy.Id, desiredText).Enabled;

        }

        /// <summary>
        /// Returns true of the desired button is visible on the current screen
        /// </summary>
        /// <param name="btn">btn represents the name of the Button</param>
        /// <returns>a boolean value</returns>
        public static bool IsVisible(Button btn)
        {
            bool bVisible = false;
            if (_instance.GetTextById(ObjectRepository.CancelButton)!=null)
                bVisible = true;
            else if (_instance.GetTextById(ObjectRepository.ContinueButton) != null)
                bVisible = true;

            return bVisible;
        }

        /// <summary>
        /// Swipes in the desired direction
        /// </summary>
        /// <param name="direction">direction represents the swipe direction</param>
        public static void Swipe(Direction direction)
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.widget.FrameLayout");
            Point pt = new Point(control.Coordinates[0], control.Coordinates[1]);
            Size sz = new Size(control.Size[0], control.Size[1]);
            Point srcPoint = new Point((pt.X + sz.Width / 2), (pt.Y + sz.Height / 2));
            Point dstPoint ;
            if (direction == Direction.Up)
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
        /// This Function Enters a Text and then captures the Validation Text.
        /// The User is expected to send a "" in the argument.
        /// User need not call EnterText Function.
        /// User needs to send the next field, for the current text field to be de-focussed.
        /// </summary>
        /// <param name="txtFieldToEnterValidationText">txtFieldToEnterValidationText represents the field name whose validation needs to be performed</param>
        /// <param name="nextTxtField">nextTxtField represent the name of any other field</param>
        /// <param name="txtValue">txtValue represents the null string with which validation can be checked</param>
        /// <returns></returns>
        public static string ValidateTextFields(EditText txtFieldToEnterValidationText, EditText nextTxtField,string txtValue)
        {
            EnterText(txtFieldToEnterValidationText, txtValue);
            return _instance.GetElement(SearchBy.Id, ObjectRepository.ErrorText).Text;
        }
        public static string ValidateTextFields(EditText txtFieldToEnterValidationText, string txtValue)
        {
                EnterText(txtFieldToEnterValidationText, txtValue);
                String errMsg = null;
                try
                {
                    errMsg = _instance.GetElement(SearchBy.Id, ObjectRepository.ErrorText).Text;
                    Logger.Info("ValidateTextFields: validation errors");
                }
                catch(Exception e)
                {
                    Logger.Info("ValidateTextFields: no validation errors");
                }
                return errMsg;
        }

       /// <summary>
       /// Waits till the Address Screen appears
       /// </summary>
       /// <returns>a boolean value</returns>
       /*
        public static bool WaitforAddressScreen()
        {
            IMobilePageControl element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Id, ObjectRepository.Title);
                loopcount++;
                if (element != null)
                    break;
                if (loopcount > 10)
                    break;
            }
            if (element != null)
                return true;
            else
                return false;
        }
        */
        public static String WaitforAddressScreen()
        {
            String element = "";
            int loopcount = 0;
            for (loopcount = 0; !element.Equals("Address") && loopcount < 40; loopcount++)
            {
                Thread.Sleep(500);
                element = GetScreenTitle();
            }
            if (loopcount == 40)
                Logger.Info("Failed: Address screen not loaded");
            else
                Logger.Info("Pass: Address screen title seen");

            for (; loopcount < 40; loopcount++)
            {
                Thread.Sleep(500);
                IMobilePageControl salute = _instance.GetElementNoWait(null, SearchBy.Id, "com.philips.cdp.di.iapdemo:id/et_salutation");
                if (null != salute)
                {
                    Logger.Info("First time user address entry: " + salute.Text);
                    
                    return salute.Text;
                }
            }
            Logger.Info("Not in Select address OR New address screen");
            return "";
        }


        public static String WaitforBillingAddressScreen()
        {
            String element = "";
            int loopcount = 0;
            for (loopcount = 0; !element.Equals("Address") && loopcount < 40; loopcount++)
            {
                Thread.Sleep(500);
                element = GetScreenTitle();
            }
            if (loopcount == 40)
                Logger.Info("Failed: Address screen not loaded");
            else
                Logger.Info("Pass: Address screen title seen");

            for (; loopcount < 40; loopcount++)
            {
                Thread.Sleep(500);
                //GetPageTitle();
                
                IMobilePageControl BillingAddress = _instance.GetElementNoWait(null, SearchBy.Id, "com.philips.cdp.di.iapdemo:id/tv_title");
                if (null != BillingAddress && BillingAddress.Text.Equals("Billing address"))
                {

                    //IMobilePageControl select_address = _instance.GetElementNoWait(null, SearchBy.Id, "com.philips.cdp.di.iapdemo:id/tv_select_address");
                    Logger.Info("Billing Address exists: found " + BillingAddress.Text);
                    //Logger.Info("Select address found: " + select_address.Text);
                    return BillingAddress.Text;
                }
            }
            Logger.Info("Not in billing address screen");
            return "";
            /*if (element != null)
                return true;
            else
                return false;*/
            //String btn = FindElement(new List<String> { "com.philips.cdp.di.iapdemo:id/tv_salutation", "com.philips.cdp.di.iapdemo:id/btn_new_address" }).Text;
            //return btn;
        }

        /// <summary>
        /// Hides the virtual keyboard
        /// User must enter the OS type in "", with the first letter in capital
        /// </summary>
        /// <param name="osType">osType represents the OS Type</param>
        public static void HideKeyboard(string osType)
        {
            switch(osType)
            {
                case "Android":
                    MobileDriver._Mdriver.Navigate().Back();
                    //UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input keyevent" + " 111");
                    break;
                case "iOS":
                    break;
            }
        }
    
        /// <summary>
        /// Creates a new Address 
        /// CreateAddress("abhi","ram",ContextMenu.MenuItem.Mr,"Software Tech park","St Loius","560045","US","99988887770",true);
        /// </summary>
        public static void CreateAddress(string firstname, string lastname,ContextMenu.MenuItem mi, string AddressLine1,string City,
            string postalcode,string country, bool isemailID,string emailID="sai.lokesh@Philips.com")
        {
            Address.WaitforAddressScreen();
            try
            {
                Address.ValidateTextFields(Address.EditText.FirstName, firstname);
                
                 Address.EnterText(Address.EditText.FirstName,firstname);
            }
            catch (Exception)
            {
                Address.HideKeyboard("Android");  
            }
            Address.EnterText(Address.EditText.LastName, lastname);
            Address.ContextMenu.Select(Address.ContextMenu.Fields.Salutation, mi);
            Address.EnterText(Address.EditText.AddressLine1, AddressLine1);
            Address.EnterText(Address.EditText.City, City);
            Address.EnterText(Address.EditText.PostalCode, postalcode);
            
            if(Address.GetText(Address.EditText.Country)=="Enter your country")
            {
                Address.EnterText(Address.EditText.Country, country);
            }
            if (Address.GetText(Address.EditText.Country) == "US")
            {
                Address.ContextMenu.Select(Address.ContextMenu.Fields.State, Address.ContextMenu.MenuItem.Alabama);
                Address.EnterText(Address.EditText.Phone, "15417543010");
            }
            else
            {
                Address.EnterText(Address.EditText.Phone, "442071234567");
            }
            if (isemailID)
            {
                Address.EnterText(EditText.Email, emailID);
            }
        }
      
        /// <summary>
        /// Returns a string containing the screen title.
        /// </summary>
        /// <returns>string value representing the page title</returns>
        public static bool IsVisible()
        {
            bool bVisible = false;
            if (_instance.GetTextById(ObjectRepository.ScreenTitle) == "Shipping address")
                bVisible = true;

            return bVisible;

        }
        /// <summary>
        /// This class provides all the functionalities and constants for features related to Context menu.
        /// </summary>
        //SAME THING IS DONE IN EnterText METHOD IN THE THE ADDREE CLASS
        public static class ContextMenu
        {
            /// <summary>
            /// Provides collection of all menu items.
            /// </summary>
            public enum MenuItem
            {
                Mr,
                Ms,
                Alabama
            }

            /// <summary>
            /// Provides collection of salutation and state fields.
            /// </summary>
            public enum Fields
            {
                Salutation,
                State
            }

            /// <summary>
            /// Selects a field in the menu item
            /// </summary>
            /// <param name="flds">flds represents the context menu for state and salutation</param>
            /// <param name="mI">mI represents the menu item</param>
            public static void Select( Fields flds, MenuItem mI)
            {
                if (flds == Fields.Salutation)
                {
                    _instance.ClickById(ObjectRepository.Salutation);
                    if (mI == MenuItem.Mr)
                    {
                        _instance.ClickByText("Mr");
                    }
                    if (mI == MenuItem.Ms)
                    {
                        _instance.ClickByText("Ms");
                    }
                }

                if (flds == Fields.State)
                {
                    _instance.ClickById(ObjectRepository.State);
                    if (mI == MenuItem.Alabama)
                    {
                        _instance.ClickByText("Alabama");
                    }
                  
                }
                
            }
        }


        public static void SwipeToTop()
        {
            int check = 0;
            do
            {
                Swipe(Direction.Down, 200);
                check++;
            } while (check<10 && _instance.GetElement(SearchBy.Id, ObjectRepository.Title) == null);
        }
    }
}
