using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.IO;
using System.Reflection;

namespace Philips.MRAutomation.Foundation.FrameworkCore
{
    public class GeneralConfiguration
    {
        public class GraphicalVerification
        {
            private static string bitmapCapturePath; 
            public static string BitmapCapturePath
            {
                get
                {
                    return GetString("AppCapturePath");
                }
                set
                {
                    bitmapCapturePath = value; 
                }
            }

            private static string bitmapCapture;
            public static string isBitmapCapture
            {
                get
                {
                    return GetString("isBitmapCapture");
                }
                set
                {
                    bitmapCapture = value;  
                }
            }

            private static string diffbitmapPath;
            public static string DiffBitmapPath
            {
                get
                {
                    return GetString("DiffBitmapPath");
                }
                set
                {
                    diffbitmapPath = value; 
                }
            }
        }

        private static string GetString(string xmlNode)
        {
            XmlDocument doc = new XmlDocument();
            doc.Load("AppConfig.xml");
            return doc.GetElementsByTagName(xmlNode).Item(0).InnerText;   
        }

        public static string testPickerFileLocation
        {
            get
            {
                return Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location) + "\\TestPicker.xml";
            }
        }

        public static string transientTestPickerFileLocation
        {
            get
            {
                return Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location) + "\\TransientTestPicker.xml";
            }
        }

        public static string executingAssemblyLocation
        {
            get
            {
                return Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location);
            }
        }

        public static string tempDirectoryForConnectivityImages
        {
            get
            {
                return Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location) + "\\TempCapturedImages";
            }
        }

        public static string crashNotifierConfigirationFileLocation
        {
            get
            {
                return Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location) + "\\CrashNotifierConfig.xml";
            }
        }

        public static string mrConfigurationFilename = "\\Data\\MRConfiguration.xml";

        public static string restartIndicator = executingAssemblyLocation + "\\isRestartedFlag";

        public static DateTime testExecutionStartTime = DateTime.Now;

        public static DateTime testExecutionEndTime = DateTime.Now;

        public static bool applicationCrashed = false;

        public static bool applicationTimeout = false;

        public static string testCaseType = string.Empty;

        public static List<string> reportSources = new List<string>();

        public static string patientName = string.Empty;

        public static string registrationId = string.Empty;
    }
}

