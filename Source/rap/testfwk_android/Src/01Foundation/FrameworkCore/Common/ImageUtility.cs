using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing; 
using TCaptureXLib;
using System.Windows.Automation;
using SW=System.Windows;

namespace Philips.MRAutomation.Foundation.FrameworkCore.Common
{
   public class ImageUtils
    {
       /// <summary>
       /// Captures the Bitmap from an Automation Element
       /// </summary>
       /// <param name="element"></param>
       /// <returns></returns>
       public static Bitmap GetBitmap(AutomationElement element)
       {
            IntPtr hDesktopDC = NativeAPI.GetDC(new IntPtr(element.Current.NativeWindowHandle));
            IntPtr hCaptureDC = NativeAPI.CreateCompatibleDC(hDesktopDC);
            IntPtr hCaptureBitmap =NativeAPI.CreateCompatibleBitmap(hDesktopDC, 
                                    Convert.ToInt32(element.Current.BoundingRectangle.Width), 
                                    Convert.ToInt32(element.Current.BoundingRectangle.Height));
            NativeAPI.SelectObject(hCaptureDC,hCaptureBitmap); 
            NativeAPI.BitBlt(hCaptureDC,0,0,
                             Convert.ToInt32(element.Current.BoundingRectangle.Width),
                             Convert.ToInt32(element.Current.BoundingRectangle.Height),
                             hDesktopDC,0,0,NativeAPI.SRCCOPY|NativeAPI.CAPTUREBLT); 
            return  Bitmap.FromHbitmap(hCaptureBitmap);             
       }

       public static void SaveBitmapToFile(AutomationElement element, string filename)
       {
           Bitmap bmp=GetBitmap(element);

       }


    }
}

