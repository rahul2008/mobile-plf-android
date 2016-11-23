using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Threading;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class ProductInformation
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
         Download_Product_Manual,
         Product_Information,
         Back_to_supportScreen,
         VideoPlay,
         OK
     }
     
    /// <summary>
    /// Waiting for product imformation home screen
    /// </summary>
    /// <returns></returns>
     public static bool WaitforProductInformationScreen()
     {
         IMobilePageControl homeScreenElement = null;
         while (homeScreenElement == null)
         {
             homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ProductInformationScreen.ProductInfoTitle);
             if (homeScreenElement != null)
                 break;
         }
         if (homeScreenElement != null)
             return true;
         else
             return false;
     }

     /// <summary>
     /// Checking whether button visible or not
     /// </summary>
     /// <param name="btn"></param>
     /// <returns></returns>
     public static bool IsVisible(Button btn)
     {
         bool isVisible = false;
         try
         {
             if (btn == Button.Download_Product_Manual)
                 isVisible = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.ProductInformationScreen.ProductManual).Displayed;
             else if (btn == Button.Product_Information)
                 isVisible = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.ProductInformationScreen.ProductInformation).Displayed;
             else if (btn == Button.Back_to_supportScreen)
                 isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ProductInformationScreen.BacktoSupport).Displayed;
             
         }
         catch(Exception e)
         {
             isVisible = false;
             Logger.Info(e.ToString());
         }
         return isVisible;
     }

     public static bool manual_opened()
     {
         bool web = false;
         web = _instance.GetElement(SearchBy.Id, "com.adobe.reader:id/pageView").Displayed;
         return web;
     }

     public static bool VideoPlayButton(Button btn)
     {
         bool isVisible = true;
         IMobilePageControl control = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ProductInformationScreen.VideoTitle);
         if (control != null)
         {
             try
             {
                 if (btn == Button.VideoPlay)
                     isVisible = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.ProductInformationScreen.ProductManual).Displayed;
             }
             catch (Exception e)
             {
                 isVisible = false;
             }
         }
         return isVisible;
     }
     
     /// <summary>
     /// checking whether button enabled or not
     /// </summary>
     /// <param name="btn"></param>
     /// <returns></returns>
     public static bool IsEnable(Button btn)
     {
         bool isEnabled = false;
         if (btn == Button.Download_Product_Manual)
             isEnabled = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.ProductInformationScreen.ProductManual).Enabled;
         else if (btn == Button.Product_Information)
             isEnabled = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.ProductInformationScreen.ProductInformation).Enabled;
         else if (btn == Button.Back_to_supportScreen)
             isEnabled = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ProductInformationScreen.BacktoSupport).Enabled;
         return isEnabled;
     }
     
     public static bool IsVideoPlayButtonEnable1(Button btn)
     {
         bool isEnabled = true;
         IMobilePageControl control = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ProductInformationScreen.VideoTitle);
         if (control != null)
             return true;
         else
             return false;
             
             //try
             //{
             //    if (btn == Button.VideoPlay)
             //        isEnabled = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.ProductInformationScreen.VideoPlayButton).Enabled;
             //    isEnabled = true;
             //}
             //catch (Exception e)
             //{
             //    isEnabled = false;
             //}
            
         //}
         //return isEnabled;
     }
     /// <summary>
     /// Checking whether button is clickable or not
     /// </summary>
     /// <param name="btn"></param>
     public static void ClickTest(Button btn)
     {
         if (btn == Button.Download_Product_Manual)
             _instance.ClickByName(ConsumerCare.Android.ProductInformationScreen.ProductManual);
         else if (btn == Button.Product_Information)
             _instance.ClickByName(ConsumerCare.Android.ProductInformationScreen.ProductInformation);
         else if (btn == Button.Back_to_supportScreen)
             _instance.ClickById(ConsumerCare.Android.ProductInformationScreen.BacktoSupport);
     }

     /// <summary>
     /// Getting header text
     /// </summary>
     /// <returns></returns>
     public static string GetHeaderText()
     {
         return _instance.GetTextById(ConsumerCare.Android.ProductInformationScreen.ProductInfoTitle);
     }
     public static bool IsVisible1()
     {
         bool bVisible = false;
         if (GetHeaderText() == "Product Information")
             bVisible = true;
         return bVisible;
     }
     
     /// <summary>
     /// getting product detail
     /// </summary>
     /// <returns>boolean value</returns>
     public static string GetProductName()
     {
         return _instance.GetTextById(ConsumerCare.Android.ProductInformationScreen.ProductDetail);
         
     }
     public static bool GetProductDetail()
     {
         bool bvisible = false;
         if (GetProductName() == "BlueTouch App-controlled pain relief patch")
             bvisible = true;
         return bvisible;
     }
     
      //<summary>
      //Capturing bitmap of productimage
      //</summary>
      //<returns>Svaed Image</returns>
     public static void ProductImage()
     {
         //IMobilePageControl control = _instance.GetElement(SearchBy.Id, "com.philips.cdp.sampledigitalcare:id/productimage");
         //MobileDriver.GetImage(control, "CroppedProductImage");
     }

    /// <summary>
    /// Web information
    /// </summary>
    /// <returns></returns>
    public static bool WebProductInformation()
     {
         IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.webkit.WebView");
         if (control != null)
             return true;
         else
             return false;
     }

    /// <summary>
    /// Clicking OK button
    /// </summary>
    /// <param name="btn"></param>
    public static void ClickOK(Button btn)
    {
        if (btn == Button.OK)
            _instance.ClickById(ConsumerCare.Android.ProductInformationScreen.PopUpview);
    }

    /// <summary>
    /// Getting variant text
    /// </summary>
    /// <returns></returns>
    public static string GetVariantText()
    {
        string variantText = _instance.GetTextById(ConsumerCare.Android.ProductInformationScreen.ProductVariant);
        return variantText;
    }
    
    /// <summary>
    /// Clicking back button of mobile
    /// </summary>
    public static void Back()
    {
        Thread.Sleep(2000);
        MobileDriver.FireKeyEvent(4);
        Thread.Sleep(2000);
        //ProductInformation.WaitforProductInformationScreen();
    }

    /// <summary>
    /// Clicking video play button
    /// </summary>
    /// <param name="btn"></param>
    /// <returns></returns>
    public static bool VideoClick(Button btn)
    {
        bool isVisible = true;
        IMobilePageControl control = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ProductInformationScreen.VideoTitle);
        if (control != null)
        {
            try
            {
                if (btn == Button.VideoPlay)
                    _instance.ClickById(ConsumerCare.Android.ProductInformationScreen.VideoPlayButton);
                Thread.Sleep(9000);
                //MobileDriver.FireKeyEvent(4);
                //MobileDriver.FireKeyEvent(4);                
            }
            catch(Exception e)
            {
                isVisible = false;
            }
        }
        return isVisible;
    }

   }
}
