
/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Philips.H2H.Foundation.AutomationCore
{
    public class AutomationConstants
    {
        public static Dictionary<string, string> dict = new Dictionary<string, string>();

        public enum AdbShellCommand
        {
            InputText,
            InputTabKey,
            InputEnterKey,
            InputKeyCode
        }


        public const int WAIT_TIME = 6; //in seconds
        public const int WAIT_ACTIVITY = 20000; //in miliseconds
        public const int PING_TIME = 500;

        public const int KEYCODE_A = 29;
        public const int KEYCODE_B = 30;
        public const int KEYCODE_C = 31;
        public const int KEYCODE_D = 32;
        public const int KEYCODE_E = 33;
        public const int KEYCODE_F = 34;
        public const int KEYCODE_G = 35;
        public const int KEYCODE_H = 36;
        public const int KEYCODE_I = 37;
        public const int KEYCODE_K = 38;
        public const int KEYCODE_J = 39;
        public const int KEYCODE_L = 40;
        public const int KEYCODE_M = 41;
        public const int KEYCODE_N = 42;
        public const int KEYCODE_O = 43;
        public const int KEYCODE_P = 44;
        public const int KEYCODE_Q = 45;
        public const int KEYCODE_R = 46;
        public const int KEYCODE_S = 47;
        public const int KEYCODE_T = 48;
        public const int KEYCODE_U = 49;
        public const int KEYCODE_V = 50;
        public const int KEYCODE_W = 51;
        public const int KEYCODE_X = 52;
        public const int KEYCODE_Y = 53;
        public const int KEYCODE_Z = 54;
        public const int KEYCODE_0 = 7;
        public const int KEYCODE_1 = 8;
        public const int KEYCODE_2 = 9;
        public const int KEYCODE_3 = 10;
        public const int KEYCODE_4 = 11;
        public const int KEYCODE_5 = 12;
        public const int KEYCODE_6 = 13;
        public const int KEYCODE_7 = 14;
        public const int KEYCODE_8 = 15;
        public const int KEYCODE_9 = 16;
        public const int KEYCODE_DOT = 56;
        public const int KEYCODE_SUBTRACT = 156;

        public static readonly string BIN_PATH = Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location);
        public static readonly string HOME_PATH = Directory.GetParent(Directory.GetParent(BIN_PATH).FullName).FullName;
        public static readonly string LOG_PATH = HOME_PATH + @"\TestReports\" + DateTime.Now.ToString("yyyy_MM_dd_HH_mm") + @"\";
        public static readonly string APK_PATH = HOME_PATH + @"\ApkUnderTest\";
        public static readonly string UI_AUTOMATION = BIN_PATH + @"\UIAutomation";      
        public static string GRID_SETUP = ConfigurationManager.AppSettings["GridSetup"];       
    }
}
