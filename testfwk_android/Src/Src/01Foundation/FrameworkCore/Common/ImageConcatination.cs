using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Reflection;
using System.IO;

namespace Philips.MRAutomation.Foundation.FrameworkCore.Common
{
    public class ImageConcatination
    {
        #region ImageConcatinate
        /// <summary>
        /// Function to cancate the images and provide a single image.
        /// </summary>
        /// <param name="expectedImagePath">path  of expectedImagePath</param>
        /// <param name="actualImagePath">path  of actualImagePath</param>
        /// <param name="diffImagePath">path  of diffImagePath</param>
        /// <returns>Concatinated image</returns>
        public static Bitmap ImageConcatinate (string expectedImagePath,string actualImagePath, string diffImagePath="")
        {
            System.Drawing.Bitmap newImg = null;
            string executionPath = Path.GetDirectoryName
                (Assembly.GetExecutingAssembly().Location) + "\\Data" + "\\GoldenBitmaps";            
            try
            {
                System.Drawing.Image expectedImage =
                System.Drawing.Image.FromFile(expectedImagePath);
                int x;
                int y = expectedImage.Height + 50;
                int x1;
                int x2;
                if (diffImagePath != string.Empty)
                {
                    x = expectedImage.Width * 3;
                    x1 = x / 3;
                    x2 = x1 * 2;
                }
                else
                {
                    x = expectedImage.Width * 2;
                    x1 = x / 2;
                    x2 = x1 * 2;
                }
                System.Drawing.Graphics g;
                newImg = new Bitmap(x, y);
                System.Drawing.Image actualImage =
                    System.Drawing.Image.FromFile(actualImagePath);
                
                System.Drawing.Image expectedImageName =
                    System.Drawing.Image.FromFile(executionPath + "\\Expected.bmp");
                System.Drawing.Image actualImageName =
                    System.Drawing.Image.FromFile(executionPath + "\\Actual.bmp");                
                g = System.Drawing.Graphics.FromImage(newImg);
                g.DrawImage(expectedImage, 0, 0);
                g.DrawImage(actualImage, x1, 0);                
                g.DrawImage(expectedImageName, 0, y - 30);
                g.DrawImage(actualImageName, x1, y - 30);
                if (diffImagePath != string.Empty)
                {
                    System.Drawing.Image diffImage =
                        System.Drawing.Image.FromFile(diffImagePath);
                    System.Drawing.Image diffImageName =
                        System.Drawing.Image.FromFile(executionPath + "\\Diffrence.bmp");
                    g.DrawImage(diffImage, x2, 0);
                    g.DrawImage(diffImageName, x2, y - 30);
                }
                g.Save();
            }
            catch (Exception)
            {
               // FileLogger.Log(FileLogger.Severity.Information, e.ToString());
            }
            return newImg;
        }
        #endregion ImageConcatinate
    }
}
