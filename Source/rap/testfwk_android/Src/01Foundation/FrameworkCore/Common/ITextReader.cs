using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;

namespace Philips.MRAutomation.Foundation.FrameworkCore.Common
{
    interface ITextReader
    {
        string GetTextFromWindow(IntPtr ptr);
        string GetTextFromWindow(IntPtr ptr, Rectangle r);
        Bitmap GetBitmap(Rectangle r);
        Bitmap GetBitmap(IntPtr ptr);
    }
}

