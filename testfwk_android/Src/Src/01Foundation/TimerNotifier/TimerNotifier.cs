/*  ----------------------------------------------------------------------------
 *  MR Automation : Philips Healthcare Bangalore Center of Competence
 *  ----------------------------------------------------------------------------
 *  Timer Notifier
 *  ----------------------------------------------------------------------------
 *  File:       TimerNotifier.cs
 *  Author:     Girish Prasad
 *  Creation Date: 1-30-2011
 *  ----------------------------------------------------------------------------
 */

using System;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Threading;
using System.Timers;
using System.Windows.Forms;
using System.Reflection;
using Philips.MRAutomation.Foundation.LoggingUtility;
using Philips.MRAutomation.Foundation.FrameworkCore;

namespace Philips.MRAutomation.Foundation.TimerNotifier
{
    public class MyTimer
    {
        // Create a new timer object
        private int minutes;
        private System.Timers.Timer timer = new System.Timers.Timer();
        public delegate void timeOutHandler();
        public event timeOutHandler timeOver;
        private static ManualResetEvent workerWait = new ManualResetEvent(false);              
        public MyTimer(int min)
        {
            minutes = min;
        }
        /// <summary>
        /// Starts the timer
        /// </summary>
        public void StartTimerNotifier()
        {
            try
            {
                workerWait.Reset();
                StartTimer();
            }
            catch (ThreadInterruptedException threadex)
            {
                timer.Elapsed -= new ElapsedEventHandler(timer_tick);
                FileLogger.Log(FileLogger.Severity.Information,
                    "Gracefully terminating the Notifier thread, interrupt exception caught. " + threadex.Message);
                workerWait.Set();
            }
        }

        /// <summary>
        /// Stops the timer notifier
        /// </summary>
        public static void StopTimerNotifier()
        {
            workerWait.Set();
        }

        /// <summary>
        /// Starts the timer
        /// </summary>
        private void StartTimer()
        {            
            DateTime d = DateTime.Now;            
            TimeSpan currTimeSpan = d.TimeOfDay;
            TimeSpan reqdTimespan = new TimeSpan(currTimeSpan.Hours, currTimeSpan.Minutes + minutes, currTimeSpan.Seconds);

            FileLogger.Log(FileLogger.Severity.Information,
                "Time span set is: " + reqdTimespan.Hours + ":" + 
                reqdTimespan.Minutes + ":" + reqdTimespan.Seconds);

            DateTime nextTrigger = d;

            // eg: time is 07:00:00, next trigger should be after 1*60*60*1000 ms
            if (currTimeSpan <= reqdTimespan)
            {
                TimeSpan diffInTimeSpan = reqdTimespan - currTimeSpan;                
                nextTrigger = nextTrigger.Add(diffInTimeSpan);
            }
            else
            {
                TimeSpan diffInTimeSpan = new TimeSpan(24, 0, 0) - (currTimeSpan - reqdTimespan);
                nextTrigger = nextTrigger.Add(diffInTimeSpan);
            }

            timer.Interval = (int)(nextTrigger - d).Hours * 60 * 60 * 1000 +
                (int)(nextTrigger - d).Minutes * 60 * 1000 +
                (int)(nextTrigger - d).Seconds * 1000;
            timer.Elapsed += new ElapsedEventHandler(timer_tick);
            // start the timer
            timer.Start();
            workerWait.WaitOne();            
            timer.Elapsed -= new ElapsedEventHandler(timer_tick);
            FileLogger.Log(FileLogger.Severity.Information, "Gracefully coming out of StartTimer function");
        }

        /// <summary>
        /// Evenet hanlder call back for timer elapsed event
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void timer_tick(Object sender, EventArgs e)
        {
            timer.Elapsed -= new ElapsedEventHandler(timer_tick);

            FileLogger.Log(FileLogger.Severity.Error, "Time Over for execution, now raising event to stop current execution");
            CapturErrorScreen();
            timeOver();
            workerWait.Set();
        }

        /// <summary>
        /// Captures the error screen
        /// </summary>
        private static void CapturErrorScreen()
        {
            Bitmap Bitmap;
            Graphics Graps;

            Bitmap = new Bitmap(Screen.PrimaryScreen.Bounds.Width,
                Screen.PrimaryScreen.Bounds.Height,
                PixelFormat.Format32bppArgb);
            Graps = Graphics.FromImage(Bitmap);
            Graps.CopyFromScreen(Screen.PrimaryScreen.Bounds.X,
                Screen.PrimaryScreen.Bounds.Y,
                0,
                0,
                Screen.PrimaryScreen.Bounds.Size,
                CopyPixelOperation.SourceCopy);
            string dir = Path.GetDirectoryName(Assembly.GetExecutingAssembly().GetModules()[0].FullyQualifiedName) + 
                @"\Reports\MRAutomationLogs\CrashAndTimeOut";
            if (!Directory.Exists(dir))
            {
                Directory.CreateDirectory(dir);
            }
            Bitmap.Save(dir + "\\TimeOut" + DateTime.Now.Ticks.ToString() + ".bmp", ImageFormat.Bmp);
        }

        /// <summary>
        /// Sets the test timeout flag, flag is set from Console Runner
        /// </summary>
        /// <param name="value">value to set</param>
        public static void SetTimeoutFlag(bool value)
        {
            GeneralConfiguration.applicationTimeout = value;
        }

        /// <summary>
        /// Get the test timeout flag
        /// </summary>
        /// <returns></returns>
        public static bool GetTimeoutFlag()
        {
            return GeneralConfiguration.applicationTimeout;
        }
    }
}

#region Revision History
/*
 * 02-13-2011  : Created by Girish for timer notifier component
 */

#endregion
