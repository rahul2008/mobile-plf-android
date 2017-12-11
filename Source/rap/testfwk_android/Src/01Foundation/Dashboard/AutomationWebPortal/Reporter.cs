using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Xml.Serialization;

namespace Philips.H2H.Automation.Dashboard
{
    [XmlRoot(ElementName = "SystemConfiguration")]
    public class SystemConfiguration
    {
        [XmlElement(ElementName = "ProductModel")]
        public string ProductModel { get; set; }
        [XmlElement(ElementName = "MainSystemType")]
        public string MainSystemType { get; set; }
        [XmlElement(ElementName = "SystemType")]
        public string SystemType { get; set; }
        [XmlElement(ElementName = "MagnetType")]
        public string MagnetType { get; set; }
        [XmlElement(ElementName = "AcquisitionSystemType")]
        public string AcquisitionSystemType { get; set; }
        [XmlElement(ElementName = "Stream")]
        public string Stream { get; set; }
        [XmlElement(ElementName = "Swid")]
        public string Swid { get; set; }
        [XmlElement(ElementName = "Release")]
        public string Release { get; set; }
        [XmlElement(ElementName = "Version")]
        public string Version { get; set; }
        [XmlElement(ElementName = "Level")]
        public string Level { get; set; }
        [XmlElement(ElementName = "SystemName")]
        public string SystemName { get; set; }
        [XmlElement(ElementName = "IPAddress")]
        public string IPAddress { get; set; }
        [XmlElement(ElementName = "PerformanceMonitoringFolder")]
        public string PerformanceMonitoringFolder { get; set; }
    }

    [XmlRoot(ElementName = "Descriptions")]
    public class Descriptions
    {
        [XmlElement(ElementName = "Description")]
        public string Description { get; set; }
    }

    [XmlRoot(ElementName = "Messages")]
    public class Messages
    {
        [XmlElement(ElementName = "Message")]
        public List<string> Message { get; set; }
    }

    [XmlRoot(ElementName = "TestCase")]
    public class TestCase
    {
        [XmlElement(ElementName = "TestCaseType")]
        public string TestCaseType { get; set; }
        [XmlElement(ElementName = "TCID")]
        public string TCID { get; set; }
        [XmlElement(ElementName = "Status")]
        public string Status { get; set; }
        [XmlElement(ElementName = "Descriptions")]
        public Descriptions Descriptions { get; set; }
        [XmlElement(ElementName = "FunctionalArea")]
        public string FunctionalArea { get; set; }
        [XmlElement(ElementName = "Product")]
        public string Product { get; set; }
        [XmlElement(ElementName = "ProductVersion")]
        public string ProductVersion { get; set; }
        [XmlElement(ElementName = "VerificationType")]
        public string VerificationType { get; set; }
        [XmlElement(ElementName = "TestCaseState")]
        public string TestCaseState { get; set; }
        [XmlElement(ElementName = "RunStatus")]
        public string RunStatus { get; set; }
        [XmlElement(ElementName = "StackTrace")]
        public string StackTrace { get; set; }
        [XmlElement(ElementName = "TestStepDetails")]
        public string TestStepDetails { get; set; }
        [XmlElement(ElementName = "Messages")]
        public Messages Messages { get; set; }
        [XmlElement(ElementName = "HasTimedOut")]
        public string HasTimedOut { get; set; }
        [XmlElement(ElementName = "HasCrashed")]
        public string HasCrashed { get; set; }
        [XmlElement(ElementName = "StartTime")]
        public string StartTime { get; set; }
        [XmlElement(ElementName = "EndTime")]
        public string EndTime { get; set; }
        [XmlElement(ElementName = "PerformanceOperations")]
        public string PerformanceOperations { get; set; }
        [XmlElement(ElementName = "PerformanceReadings")]
        public string PerformanceReadings { get; set; }
        [XmlAttribute(AttributeName = "Name")]
        public string Name { get; set; }
    }

    [XmlRoot(ElementName = "Reporter")]
    public class Reporter
    {
        [XmlElement(ElementName = "SystemConfiguration")]
        public SystemConfiguration SystemConfiguration { get; set; }
        [XmlElement(ElementName = "TestCase")]
        public List<TestCase> TestCase { get; set; }
        [XmlAttribute(AttributeName = "xsi", Namespace = "http://www.w3.org/2000/xmlns/")]
        public string Xsi { get; set; }
        [XmlAttribute(AttributeName = "xsd", Namespace = "http://www.w3.org/2000/xmlns/")]
        public string Xsd { get; set; }
    }
}