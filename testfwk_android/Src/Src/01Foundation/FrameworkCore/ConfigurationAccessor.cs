/*  ----------------------------------------------------------------------------
 *  MR Automation : Philips Healthcare Bangalore Center of Competence
 *  ----------------------------------------------------------------------------
 *  Serval Automation
 *  ----------------------------------------------------------------------------
 *  File:       ConfigurationAccessor.cs
 *  Author:     
 *  Creation Date: dd-mm-yyyy
 *  ----------------------------------------------------------------------------
 */

using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;
using System.IO;
using System.Xml.Serialization;
using System.Xml;
using System.Reflection;
using Philips.MRAutomation.Foundation.FrameworkCore.MRConfiguration;


namespace Philips.MRAutomation.Foundation.FrameworkCore
{
    public class ConfigurationAccessor
    {
        public static Utilities Utilities = new Utilities();

        /// <summary>
        /// Construct a test data object and returns it
        /// </summary>
        /// <param name="tagName">tag in xml file</param>
        /// <param name="dataFile">xml file name</param>
        /// <returns>test data object</returns>        
        private static object DataInitializer(string tagName)
        {
            XmlDocument document = new XmlDocument();
            document.Load(GeneralConfiguration.executingAssemblyLocation + GeneralConfiguration.mrConfigurationFilename);
            XmlNodeList nList = document.GetElementsByTagName(tagName);
            XmlNode dataNode = nList[0];
            return (Dictionary<string, object>)ConstructDataObject(dataNode);
        }

        /// <summary>
        /// Returns the value from MRConfiguration.xml in the given path
        /// </summary>        
        /// <param name="path">Path where the data is located in the xml</param>
        /// <returns>string value</returns>        
        public static string GetValue(string path)
        {
            Dictionary<string, object> dob = (Dictionary<string, object>)DataInitializer("Configuration");
            string value = string.Empty;
            string[] names = path.Split('/');
            int i;
            for (i = 0; i < names.Length; i++)
            {
                if (dob.Keys.Contains(names[i]))
                {
                    if (i == names.Length - 1)
                    {
                        try
                        {
                            value = (string)dob[names[i]];
                        }
                        catch
                        {
                        }
                    }
                    else
                    {
                        dob = (Dictionary<string, object>)dob[names[i]];
                    }
                }
                else
                {
                    value = string.Empty;
                }
            }
            return value;
        }

        /// <summary>
        /// Constructs a data object and return it as an object
        /// </summary>
        /// <param name="dataNode">dataNode from the testcase input xml</param>
        /// <returns>data object</returns>
        private static object ConstructDataObject(XmlNode dataNode)
        {
            try
            {
                Dictionary<string, object> dataObject = new Dictionary<string, object>();

                if (dataNode.HasChildNodes)
                {
                    foreach (XmlNode child in dataNode.ChildNodes)
                    {
                        if (child is System.Xml.XmlElement)
                        {
                            if (dataObject.Keys.Contains(child.Name))
                            {
                                int i = 1;
                                while (dataObject.Keys.Contains(child.Name + (++i).ToString()))
                                {

                                }
                                dataObject.Add(child.Name + i.ToString(), ConstructDataObject(child));
                            }
                            else
                            {
                                dataObject.Add(child.Name, ConstructDataObject(child));
                            }
                        }
                        else
                        {
                            return child.Value;
                        }
                    }
                }
                return dataObject;
            }
            catch
            {
                return null;
            }
        }
    }


    public class Utilities
    {

        /// <summary>
        /// Gets the deserialized data from MRconfiguration.xml
        /// </summary>
        /// <returns>Configuration object</returns>
        private Configuration GetDeserializedData()
        {
            string filename =
                Directory.GetCurrentDirectory() + GeneralConfiguration.mrConfigurationFilename;
            Configuration configurationData = new Configuration();
            try
            {
                XmlSerializer serializer = new XmlSerializer(typeof(Configuration));
                FileStream fs = null;
                fs = new FileStream(filename, FileMode.Open, FileAccess.Read);
                configurationData = (Configuration)serializer.Deserialize(fs);
                fs.Close();
            }
            catch (Exception)
            {   
            }
            return configurationData;
        }

        /// <summary>
        /// Gets the deserialized data from MRconfiguration.xml
        /// </summary>
        /// <returns>Configuration object</returns>
        private void SerializeData(Configuration configurationData)
        {
            string filename =
                Directory.GetCurrentDirectory() + GeneralConfiguration.mrConfigurationFilename;            
            FileStream fs = null;
            if (File.Exists(filename))
            {
                File.Delete(filename);
            }
            try
            {
                XmlSerializer serializer = new XmlSerializer(typeof(Configuration));
                fs = new FileStream(filename, FileMode.Create, FileAccess.Write);
                serializer.Serialize(fs, configurationData);
                fs.Close();
            }
            catch
            {               
                
            }                        
        }

        /// <summary>
        /// Set the user warning mode to true or false
        /// </summary>
        /// <param name="dataNode">value</param>        
        public void SetUserWarningMode(string value)
        {            
            Configuration configurationData = GetDeserializedData();

            for (int i = 0; i < configurationData.Items.Count; i++)
            {
                if (configurationData.Items[i].GetType() == typeof(ConfigurationGenericConfiguration))
                {
                    ((ConfigurationGenericConfiguration)configurationData.Items[i]).UserWarningMode = value;
                    break;
                }
            }
            SerializeData(configurationData);            
        }

        /// <summary>
        /// Set the user warning mode to true or false
        /// </summary>
        /// <param name="dataNode">value</param>        
        public bool GetPerformanceMonitoringMode()
        {
            string value = string.Empty;            
            Configuration configurationData = GetDeserializedData();

            for (int i = 0; i < configurationData.Items.Count; i++)
            {
                if (configurationData.Items[i].GetType() == typeof(ConfigurationGenericConfiguration))
                {
                    value = ((ConfigurationGenericConfiguration)configurationData.Items[i]).PerformanceMonitoring;
                    break;
                }
            }            
            return value.ToLower() == "true" ? true : false;
        }

        /// <summary>
        /// Set the user warning mode to true or false
        /// </summary>
        /// <param name="dataNode">value</param>        
        public void SetPerformanceMonitoringMode(string value)
        {
            Configuration configurationData = GetDeserializedData();

            for (int i = 0; i < configurationData.Items.Count; i++)
            {
                if (configurationData.Items[i].GetType() == typeof(ConfigurationGenericConfiguration))
                {
                    ((ConfigurationGenericConfiguration)configurationData.Items[i]).PerformanceMonitoring = value;
                    break;
                }
            }
            SerializeData(configurationData);
        }

        /// <summary>
        /// Set the user warning mode to true or false
        /// </summary>
        /// <param name="dataNode">value</param>        
        public bool GetUserWarningMode()
        {
            string value = string.Empty;
            Configuration configurationData = GetDeserializedData();

            for (int i = 0; i < configurationData.Items.Count; i++)
            {
                if (configurationData.Items[i].GetType() == typeof(ConfigurationGenericConfiguration))
                {
                    value = ((ConfigurationGenericConfiguration)configurationData.Items[i]).UserWarningMode;
                    break;
                }
            }
            return value.ToLower() == "true" ? true : false;
        }

        /// <summary>
        /// Gets the warning message for the test case if any from MRConfiguration
        /// </summary>
        /// <param name="assembly">assmebly name of the test</param>
        /// <param name="className">test class name</param>
        /// <param name="messageBox">list with message, title and buttons in order</param>
        /// <returns>true if a warning message is configured, false otherwise</returns>
        public bool GetWarningMessage(string assembly, string className, out List<string> messageBox)
        {
            bool success = false;            
            messageBox = new List<string>();
            Configuration configurationData = GetDeserializedData();

            for (int i = 0; i < configurationData.Items.Count; i++)
            {
                if (configurationData.Items[i].GetType() == typeof(ConfigurationUserWarningMessages))
                {
                    for (int j = 0; j < ((ConfigurationUserWarningMessages)configurationData.Items[i]).Testcase.Count; j++)
                    {
                        if (assembly == ((ConfigurationUserWarningMessagesTestcase)
                            ((ConfigurationUserWarningMessages)configurationData.Items[i]).Testcase[j]).Assembly &&
                            className == ((ConfigurationUserWarningMessagesTestcase)
                            ((ConfigurationUserWarningMessages)configurationData.Items[i]).Testcase[j]).ClassName)
                        {
                            messageBox.Add(((ConfigurationUserWarningMessagesTestcase)
                                ((ConfigurationUserWarningMessages)configurationData.Items[i]).Testcase[j]).Message);
                            messageBox.Add(((ConfigurationUserWarningMessagesTestcase)
                                ((ConfigurationUserWarningMessages)configurationData.Items[i]).Testcase[j]).Title);
                            messageBox.Add(((ConfigurationUserWarningMessagesTestcase)
                                ((ConfigurationUserWarningMessages)configurationData.Items[i]).Testcase[j]).Buttons);
                            success = true;
                            break;
                        }
                    }
                    if (success == true)
                    {
                        break;
                    }
                }
            }            
            return success;
        }

        /// <summary>
        /// Configuration Items
        /// </summary>
        public enum ConfigurationItems
        {
            DeletePatientFromDB,
            EnableiSiteClientAutomation,
            ISiteClientMachine,
            ISiteClientMachinePassword,
            ISiteClientMachineUID,
            PerformanceMonitoring,
            UserWarningMode,
            PackageIntegratorDataStore,
            ConnectivityDataStore,
            DiskFileExportDataStore,
            DICOMDVDOutputType_Classic,
            DICOMDVDOutputType_Enhanced,
            DICOMFILEOutputType_Classic,
            DICOMFILEOutputType_Enhanced,
            TestServer_MapDrive,
            ProductType,
            UseWindowWatcher,
            SaveStateInformation,
            BeforeExportFolderName,
            AandCRegressionDataStore,
            AandCPerformanceDataStore,
            InterfaceFile,
            ISiteClassicNode,
            ISiteEnhancedNode,
            EWSClassicNode,
            EWSEnhancedNode,
            USBDrivePath,
            CopyTestCaseLog,
            CopyReportToServer,
            MRApplicationLogPath,
            CopyTestSuiteLog,
            CopyServerIp,
            UserName,
            Password,
            ScanAutomationHostName
        };


        /// <summary>
        /// Get the value of a particular configuration item
        /// </summary>
        /// <param name="ci">ConfigurationItems enum</param>
        /// <returns>string value</returns>
        public string GetGenericConfigurationValue(ConfigurationItems ci)
        {
            string value = string.Empty;
            Configuration configurationData = GetDeserializedData();

            for (int i = 0; i < configurationData.Items.Count; i++)
            {
                if (configurationData.Items[i].GetType() == typeof(ConfigurationGenericConfiguration))
                {
                    value = ((ConfigurationGenericConfiguration)configurationData.Items[i]).
                        GetType().
                        GetProperty(ci.ToString()).
                        GetValue(configurationData.Items[i], null) as string;
                    break;
                }
            }
            return value;
        }


        /// <summary>
        /// Set the value of a particular configuration item
        /// </summary>
        /// <param name="ci">ConfigurationItems enum</param>
        /// <param name="s">string value</param>
        public void SetGenericConfigurationValue(ConfigurationItems ci, string s)
        {
            Configuration configurationData = GetDeserializedData();

            for (int i = 0; i < configurationData.Items.Count; i++)
            {
                if (configurationData.Items[i].GetType() == typeof(ConfigurationGenericConfiguration))
                {
                    ((ConfigurationGenericConfiguration)configurationData.Items[i]).
                        GetType().
                        GetProperty(ci.ToString()).SetValue(configurationData.Items[i], s, null);                    
                    break;
                }
            }
            SerializeData(configurationData);
        }
    }
}
