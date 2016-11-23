/*  ----------------------------------------------------------------------------
 *  MR Automation : Philips Healthcare Bangalore Center of Competence
 *  ----------------------------------------------------------------------------
 *  Status Indicator Launcher
 *  ----------------------------------------------------------------------------
 *  File:       Launcher.cs
 *  Author:     Girish Prasad
 *  Creation Date: 14-June-2011
 *  ----------------------------------------------------------------------------
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Threading;

namespace Philips.MRAutomation.Foundation.StatusIndicator
{    
    
    public class Indicator
    {
        StatusIndicatorApplication statusIndicator = new StatusIndicatorApplication();
        public delegate void stopExecutionHandler();           
        public event stopExecutionHandler abortExecution;        
        [STAThread]
        public void Launch() 
        {            
            StatusIndicator.stopExecution += new StatusIndicator.StopExecution(StatusIndicator_stopExecution);
            try
            {   
                statusIndicator.Run();                
            }
            catch (Exception)
            {

            }
        }

        void StatusIndicator_stopExecution()
        {
            abortExecution();
        }

        public enum MessageBoxButtons
        {
            // Summary:
            //     The message box displays an OK button.
            OK = 0,
            //
            // Summary:
            //     The message box displays OK and Cancel buttons.
            OKCancel = 1,
            //
            // Summary:
            //     The message box displays Yes, No, and Cancel buttons.
            YesNoCancel = 3,
            //
            // Summary:
            //     The message box displays Yes and No buttons.
            YesNo = 4,

        };

        public enum MyMessageBoxResult
        {
            // Summary:
            //     The message box returns no result.
            None = 0,
            //
            // Summary:
            //     The result value of the message box is OK.
            OK = 1,
            //
            // Summary:
            //     The result value of the message box is Cancel.
            Cancel = 2,
            //
            // Summary:
            //     The result value of the message box is Yes.
            Yes = 6,
            //
            // Summary:
            //     The result value of the message box is No.
            No = 7,
        }

        /// <summary>
        /// Displays the testcase name in the status indicator
        /// </summary>
        /// <param name="testCaseName">test case name</param>
        public static void SetTestCase(string testCaseName)
        {
            StatusIndicatorApplication.SetTestCase(testCaseName);            
        }
        
        /// <summary>
        /// Displays the teststep name in the status indicator
        /// </summary>
        /// <param name="testStepName">test step name</param>
        public static void SetTestStep(string testStepName)
        {
            StatusIndicatorApplication.SetTestStep(testStepName);         
        }
        
        /// <summary>
        /// Updates the pass, fail and unknown error count in the status indicator
        /// </summary>
        /// <param name="passCount">count of pass tests</param>
        /// <param name="failCount">count of failed tests</param>
        /// <param name="unexpectedErrorCount">count of unexpectedError tests</param>
        public static void UpdateCount(int passCount, int failCount, int unexpectedErrorCount)
        {
            StatusIndicatorApplication.UpdateCount(passCount, failCount, unexpectedErrorCount);
        }
        
        /// <summary>
        /// Hide the status indicator
        /// </summary>
        public static void Hide()
        {
            StatusIndicatorApplication.Hide(true);
        }
        
        /// <summary>
        /// Show the status indicator if it is hidden
        /// </summary>
        public static void Show()
        {
            StatusIndicatorApplication.Hide(false);
        }

        /// <summary>
        /// Show a message box that has a text, caption, that returns a result
        /// </summary>
        /// <param name="lmessageBoxText">message box text</param>
        /// <param name="lcaption">caption of the message box</param>
        /// <param name="lbutton">buttons to be shown</param>
        /// <returns>MessageBoxResult</returns>
        public static MyMessageBoxResult ShowMessageBox(
            string lmessageBoxText, 
            string lcaption, 
            MessageBoxButtons lbutton)
        {            
            MessageBoxResult mr = StatusIndicatorApplication.ShowMessage(lmessageBoxText, lcaption, (MessageBoxButton)((int)lbutton));
            return ((MyMessageBoxResult)((int)mr));
        }
    }
   
}
