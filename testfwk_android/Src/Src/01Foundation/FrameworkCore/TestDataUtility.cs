using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Reflection;
using System.Xml;
using System.Text.RegularExpressions;

namespace Philips.MRAutomation.Foundation.FrameworkCore
{
    public class TestDataUtility
    {
        public static object TestcaseInitializer(string tagName, string dataFile)
        {
            Dictionary<string, string> DataObjects = new Dictionary<string, string>();
            try
            {
                string resourceName = Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location);
                XmlDocument document = new XmlDocument();
                document.Load(resourceName + dataFile);
                XmlNodeList nList = document.GetElementsByTagName(tagName);
                XmlNode dataNode = nList[0];               
                foreach (XmlNode child in dataNode.ChildNodes)
                {
                    DataObjects.Add(child.Name, child.InnerText);
                }
            }
            catch
            {

            }

            return DataObjects;
        }

        /// <summary>
        /// Construct a test data object and returns it
        /// </summary>
        /// <param name="tagName">tag in xml file</param>
        /// <param name="dataFile">xml file name</param>
        /// <returns>test data object</returns>        
        public static object TestDataInitializer(string tagName, string dataFile)
        {
            string resourceName = Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location);
            XmlDocument document = new XmlDocument();

            if (!IsPathAbsolute(dataFile))
            {
                dataFile = resourceName + dataFile;
            }
            document.Load(dataFile);
            XmlNodeList nList = document.GetElementsByTagName(tagName);
            XmlNode dataNode = nList[0];
            return (Dictionary<string, object>)ConstructDataObject(dataNode);
        }


        /// <summary>
        /// Is the path absolute
        /// </summary>
        /// <param name="path">path</param>
        /// <returns>true if path is ablsolute, false otherwise</returns>
        public static bool IsPathAbsolute(string path)
        {
            Regex regex = new Regex("^[a-zA-Z]{1}:\\.*");
            if (regex.Match(path) == Match.Empty)
            {
                return false;
            }
            return true;

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
                        else if (child.NodeType != XmlNodeType.Comment)
                        {
                            return child.Value;
                        }
                    }
                }
                return dataObject;
            }
            catch
            {
                //FileLogger
                return null;
            }
        }

        /// <summary>
        /// Returns the value from a dataobject in the given path
        /// </summary>
        /// <param name="dob">Test case input data object</param>
        /// <param name="path">Path where the data is located in the xml</param>
        /// <returns>string value</returns>        
        public static string GetValue(Dictionary<string, object> dob, string path)
        {
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
        /// 
        /// </summary>
        /// <param name="filename">Input file name </param>
        /// <param name="path">Node element to search in the xml file</param>
        /// <param name="value">Attribute to set for a given node</param>
        /// <returns>Attribute </returns>
        /// <example>SetValue("C:\Variable.xml","//Data//Testcase//Test","Test_Id")</example>
        public static string SetValue(string filename, string path, string value)
        {
            path.LastIndexOf("//");
            int count = path.LastIndexOf("//");
            string childNode = path.Remove(0, count + 2);
            string parent = path.Remove(count);
            XmlDocument doc = new XmlDocument();
            if (File.Exists(@filename))
            {
                doc.Load(@filename);
                XmlNode RootNode = doc.SelectSingleNode("//" + path);
                if (RootNode != null)
                {
                    if (RootNode.Name == childNode)
                    {
                        RootNode.InnerText = value;
                    }
                }
                else
                {
                    RootNode = doc.SelectSingleNode("//" + parent);
                    XmlNode TestChild = doc.CreateNode(XmlNodeType.Element, childNode, null);
                    RootNode.AppendChild(TestChild);
                    TestChild.InnerText = value;
                }
                doc.Save(@filename);
                return value;
            }
            else
            {
                return string.Empty;
            }
        }
    }
}
