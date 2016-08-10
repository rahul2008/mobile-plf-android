/*  ----------------------------------------------------------------------------
 *  MR Automation : Philips Healthcare Bangalore Center of Competence
 *  ----------------------------------------------------------------------------
 *  Status Indicator Launcher
 *  ----------------------------------------------------------------------------
 *  File:       StatusIndicator.xaml.cs
 *  Author:     Girish Prasad
 *  Creation Date: 14-June-2011
 *  ----------------------------------------------------------------------------
 */
using System;
using System.ComponentModel;
using System.Threading;
using System.Windows;
using System.Windows.Input;
using System.Windows.Threading;
using System.Runtime.InteropServices;
using Philips.MRAutomation.Foundation.FrameworkCore.Common;

namespace Philips.MRAutomation.Foundation.StatusIndicator
{

    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class StatusIndicator : Window
    {
        private System.Timers.Timer timer = new System.Timers.Timer();
        private static Action EmptyDelegate = delegate() { };
        public delegate void StopExecution();
        public static event StopExecution stopExecution;
        
        BackgroundWorker bw;
        public StatusIndicator()
        {
            InitializeComponent();
            this.WindowStartupLocation = WindowStartupLocation.Manual;
            this.Left = System.Windows.SystemParameters.WorkArea.Width - this.Width;
            this.Top = System.Windows.SystemParameters.WorkArea.Height - this.Height;
            this.Show();

            bw = new BackgroundWorker();

            // Allow Worker to Report Progress
            bw.WorkerReportsProgress = true;

            // Can be cancelled
            bw.WorkerSupportsCancellation = true;

            // Method to do during Execution
            bw.DoWork += bw_DoWork;

             //Method to call when Progress has changed
            bw.ProgressChanged += bw_ProgressChanged;

            //// Method to run after BackgroundWorker has completed?
            //bw.RunWorkerCompleted += bw_RunWorkerCompleted;

            //// Call RunWorkerAsync to start, optionally with an object argument.
            //// Any argument passed to RunWorkerAsync will be forwarded to
            //// DoWork's event handler
            bw.RunWorkerAsync(1);
        }

        private void bw_DoWork(object sender, DoWorkEventArgs e)
        {   
            while (true)
            {
                bw.ReportProgress(1);
                Thread.Sleep(500);
            }
        }

        private void bw_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            this.testCase.Content = StatusIndicatorApplication.tCase;
            this.testStep.Content = StatusIndicatorApplication.tStep;
            this.PassLabel.Content = StatusIndicatorApplication.pass.ToString();
            this.FailLabel.Content = StatusIndicatorApplication.fail.ToString();
            this.UnexpectedErrorLabel.Content = StatusIndicatorApplication.unexpectedError.ToString();
            if (StatusIndicatorApplication.hide)
            {
                //this.Topmost = false;
                this.Hide();
            }
            else
            {
                if (!this.Topmost)
                {
                    this.Topmost = true;
                }
                this.Show();
            }
            if (StatusIndicatorApplication.showMessageBox == true && 
                StatusIndicatorApplication.isMessageBoxShown == false)
            {
                StatusIndicatorApplication.isMessageBoxShown = true;
                MessageBoxResult lmr = MessageBox.Show(this,StatusIndicatorApplication.messageBoxText,
                    StatusIndicatorApplication.caption,
                    StatusIndicatorApplication.button);
                StatusIndicatorApplication.mr = lmr;
                StatusIndicatorApplication.showMessageBox = false;
                StatusIndicatorApplication.isMessageBoxShown = false;
            }
        }
       
        public void DragWindow(object sender, MouseButtonEventArgs args)
        {
            DragMove();
        }

        private void StopButton_Click(object sender, RoutedEventArgs e)
        {
            bw.CancelAsync();
            this.Close();
            stopExecution();
        }
    }

    public static class ExtensionMethods
    {
        private static Action EmptyDelegate = delegate() { };

        public static void Refresh(this UIElement uiElement)
        {
            uiElement.Dispatcher.Invoke(DispatcherPriority.Render, EmptyDelegate);
        }
    }

    public class StatusIndicatorApplication : Application
    {
        public static string tCase = string.Empty;
        public static string tStep = string.Empty;
        public static int pass = 0;
        public static int fail = 0;
        public static int unexpectedError = 0;
        public static bool hide = false;
        public static bool showMessageBox = false;
        public static bool isMessageBoxShown = false;
        public static string messageBoxText = string.Empty;
        public static string caption = string.Empty;
        public static MessageBoxButton button;
        public static MessageBoxResult mr;

        StatusIndicator window;

        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);
            window = new StatusIndicator();
        }

        public static void SetTestCase(string s)
        {
            tCase = s;

        }
        public static void SetTestStep(string s)
        {
            tStep = s;
        }
        public static void UpdateCount(int passCount, int failCount, int unexpectedErrorCount)
        {
            pass = passCount;
            fail = failCount;
            unexpectedError = unexpectedErrorCount;
        }
        public static void Hide(bool b)
        {
            hide = b;
        }

        /// <summary>
        /// Show a message box that has a text, caption, that returns a result
        /// </summary>
        /// <param name="lmessageBoxText">message box text</param>
        /// <param name="lcaption">caption of the message box</param>
        /// <param name="lbutton">buttons to be shown</param>
        /// <returns>MessageBoxResult</returns>
        public static MessageBoxResult ShowMessage(string lmessageBoxText, string lcaption, MessageBoxButton lbutton)
        {
            messageBoxText = lmessageBoxText;
            caption = lcaption;
            button = lbutton;
            showMessageBox = true;
            while (showMessageBox)
            {
                Thread.Sleep(500);
            }
            MaximizeWindow(lcaption);
            return mr;
        }

        #region MaximizeWindow
        public static void MaximizeWindow(string caption)
        {
            try
            {
                IntPtr mainWin = NativeAPI.FindWindowByCaption(0, caption);
                int hwnd = (int)(mainWin);
                NativeMethods.ShowWindow(hwnd, 9);
            }
            catch
            {

            }
        }
        #endregion
    }

    internal class NativeMethods
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="hwnd"></param>
        /// <param name="nCmdShow"></param>
        /// <returns></returns>
        [DllImport("User32.dll")]
        public static extern int ShowWindow(int hwnd, int nCmdShow);
    }
}
