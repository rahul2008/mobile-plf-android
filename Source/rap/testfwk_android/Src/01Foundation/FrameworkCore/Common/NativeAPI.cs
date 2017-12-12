using System;
using System.Collections.Generic;
using System.Text;
using System.Runtime.InteropServices;
using System.Diagnostics;
using System.Threading;
using System.Drawing;

namespace Philips.MRAutomation.Foundation.FrameworkCore.Common
{
    public class NativeAPI
    {
        #region Constants
        internal const UInt32 MF_BYCOMMAND = 0x00000000;
        internal const UInt32 MF_BYPOSITION = 0x00000400;
        public const UInt32 FLASHW_ALL = 3;
        public const uint LVM_GETITEMSTATE = 0x1000 + 44;
        public const uint LVIF_STATE = 0x8;
        public const uint WM_SETTEXT = 0xC;
        public const uint LVM_HITTEST = 0x1000 + 18;
        public const uint LVIS_STATEIMAGEMASK = 0xF000;
        public const uint AW_HOR_POSITIVE = 0x1;
        public const uint AW_HOR_NEGATIVE = 0x2;
        public const uint AW_VER_POSITIVE = 0x4;
        public const uint AW_VER_NEGATIVE = 0x8;
        public const uint AW_CENTER = 0x10;
        public const uint AW_HIDE = 0x10000;
        public const uint SRCCOPY = 0xCC0020;
        public const uint CAPTUREBLT = 0x40000000;
        public const uint GW_HWNDNEXT = 2;
        public const uint GW_Child = 5;
        public const uint GW_HWNDFIRST = 0;
        public const uint GW_HWNDLAST = 1;
        public const uint GW_HWNDPREV = 3;
        public const uint GW_OWNER = 4;
        public const uint WM_MOUSEMOVE = 0x200;
        public const uint WM_LBUTTONDOWN = 0x201;
        public const uint WM_LBUTTONUP = 0x202;
        public const uint MK_LBUTTON = 0x0001;
        public const uint TBM_GETTHUMBRECT = 0x419;
        public const uint TBM_GETPOS = 0x0400;
        public const uint TBM_SETPOS = 0x0405;
        public const int SW_MAXIMISE = 9;
        #endregion


        #region Delegates
        public delegate bool EnumWindowsProc(IntPtr hWnd, IntPtr lParam);
        public delegate bool CallBackPtr(int hwnd, int lParam);
        public delegate bool EnumWindowProc(IntPtr hWnd, IntPtr parameter);
        #endregion

        #region Variables
        public static string condition = string.Empty;
        #endregion

        #region Enums
        public enum MouseCursor
        {
            IShapedCursor = 65555,
            Pointer = 65553,
            DefaultAndWait = 65575,
            Wait = 65557
        }
        #endregion

        #region Structs
        [StructLayout(LayoutKind.Sequential)]
        public struct WindowPlacement
        {
            public uint Length;
            public uint Flags;
            public uint ShowCMD;
            public Point MinmizedPosition;
            public Point MaximizedPosition;
            public Rectangle NormalPosition;
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct WindowInfo
        {
            public uint size;
            public Rectangle layout;
            public Rectangle clientLayout;
            public uint style;
            public uint exStyle;
            public uint activeStatus;
            public uint borderWidth;
            public uint borderHeight;
            public ushort atom;
            public ushort windowVersion;
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct MouseInput
        {
            private int dx;
            private int dy;
            private int mouseData;
            private uint dwFlags;
            private int time;
            private IntPtr dwExtraInfo;

            public MouseInput(uint dwFlags, IntPtr dwExtraInfo)
            {
                this.dwFlags = dwFlags;
                this.dwExtraInfo = dwExtraInfo;
                dx = 0;
                dy = 0;
                time = 0;
                mouseData = 0;
            }
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct KeyboardInput
        {
            private short wVk;
            private short wScan;
            private KeyUpDown dwFlags;
            private int time;
            private IntPtr dwExtraInfo;

            public KeyboardInput(short wVk, KeyUpDown dwFlags, IntPtr dwExtraInfo)
            {
                this.wVk = wVk;
                wScan = 0;
                this.dwFlags = dwFlags;
                time = 0;
                this.dwExtraInfo = dwExtraInfo;
            }

            public enum KeyUpDown
            {
                KEYEVENTF_KEYDOWN = 0x0000,
                KEYEVENTF_EXTENDEDKEY = 0x0001,
                KEYEVENTF_KEYUP = 0x0002,
            }

            public enum SpecialKeys
            {
                SHIFT = 0x10,
                CONTROL = 0x11,
                ALT = 0x12,
                LEFT_ALT = 0xA4,
                RIGHT_ALT = 0xA5,
                RETURN = 0x0D,
                RIGHT = 0x27,
                BACKSPACE = 0x08,
                LEFT = 0x25,
                ESCAPE = 0x1B,
                TAB = 0x09,
                HOME = 0x24,
                END = 0x23,
                UP = 0x26,
                DOWN = 0x28,
                INSERT = 0x2D,
                DELETE = 0x2E,
                CAPS = 0x14,
                F1 = 0x70,
                F2 = 0x71,
                F3 = 0x72,
                F4 = 0x73,
                F5 = 0x74,
                F6 = 0x75,
                F7 = 0x76,
                F8 = 0x77,
                F9 = 0x78,
                F10 = 0x79,
                F11 = 0x7A,
                F12 = 0x7B,
            }
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct HardwareInput
        {
            private int uMsg;
            private short wParamL;
            private short wParamH;
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct CursorInfo
        {
            public uint size;
            public uint flags;
            public IntPtr handle;
            public Point point;

            public static CursorInfo New()
            {
                CursorInfo info = new CursorInfo();
                info.size = (uint)Marshal.SizeOf(typeof(CursorInfo));
                return info;
            }
        }

        [StructLayout(LayoutKind.Explicit)]
        public struct Input
        {
            [FieldOffset(0)]
            private int type;
            [FieldOffset(4)]
            private MouseInput mi;
            [FieldOffset(4)]
            private KeyboardInput ki;
            [FieldOffset(4)]
            private HardwareInput hi;

            public static Input Mouse(MouseInput mouseInput)
            {
                Input input = new Input();
                input.type = WindowsConstants.INPUT_MOUSE;
                input.mi = mouseInput;
                return input;
            }

            public static Input Keyboard(KeyboardInput keyboardInput)
            {
                Input input = new Input();
                input.type = WindowsConstants.INPUT_KEYBOARD;
                input.ki = keyboardInput;
                return input;
            }
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct FLASHWINFO
        {
            public UInt32 cbSize;
            public IntPtr hwnd;
            public UInt32 dwFlags;
            public UInt32 uCount;
            public UInt32 dwTimeout;
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct RECT
        {
            public int Left;
            public int Top;
            public int Right;
            public int Bottom;
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct _LVHITTESTINFO
        {

            public Point pt;
            public uint flags;
            public int iItem;
            public int iSubItem;
        }

        #region stuct POINT
        /// <summary>
        /// Structure POINT
        /// </summary>
        /// <remarks></remarks>
        [StructLayout(LayoutKind.Sequential)]
        public struct POINT
        {
            /// <summary>
            /// X co-ordinate
            /// </summary>
            public int X;
            /// <summary>
            /// Y co-ordinate
            /// </summary>
            public int Y;

            /// <summary>
            /// Initializes a new instance of the <see cref="POINT"/> struct.
            /// </summary>
            /// <param name="x">The x.</param>
            /// <param name="y">The y.</param>
            /// <remarks></remarks>
            public POINT(int x, int y)
            {
                this.X = x;
                this.Y = y;
            }

            /// <summary>
            /// Performs an implicit conversion from <see cref="Philips.MRAutomation.ApplicationPlugins.Serval.SystemUI.POINT"/> to <see cref="System.Drawing.Point"/>.
            /// </summary>
            /// <param name="p">The p.</param>
            /// <returns>The result of the conversion.</returns>
            /// <remarks></remarks>
            public static implicit operator System.Drawing.Point(POINT p)
            {
                return new System.Drawing.Point(p.X, p.Y);
            }

            /// <summary>
            /// Performs an implicit conversion from <see cref="System.Drawing.Point"/> to <see cref="Philips.MRAutomation.ApplicationPlugins.Serval.SystemUI.POINT"/>.
            /// </summary>
            /// <param name="p">The p.</param>
            /// <returns>The result of the conversion.</returns>
            /// <remarks></remarks>
            public static implicit operator POINT(System.Drawing.Point p)
            {
                return new POINT(p.X, p.Y);
            }
        }
        #endregion stuct POINT

        #endregion

        #region Dll Imports
        [DllImport("user32.dll")]
        [return: MarshalAs(UnmanagedType.Bool)]
        public static extern bool GetWindowRect(IntPtr hWnd, out RECT lpRect);

        [DllImport("user32.dll")]
        public static extern bool BringWindowToTop(IntPtr hWnd);

        [DllImport("user32.dll")]
        public static extern bool SetForegroundWindow(IntPtr hWnd);

        [DllImport("user32.dll")]
        public static extern bool IsWindowVisible(IntPtr hWnd);

        [DllImport("user32.dll")]
        public static extern IntPtr GetDlgItem(IntPtr hDlg, int nIDDlgItem);

        [DllImport("user32.dll", SetLastError = true, CharSet = CharSet.Auto)]
        public static extern int GetClassName(IntPtr hWnd, StringBuilder lpClassName, int nMaxCount);

        [DllImport("user32.dll")]
        public static extern int GetDlgCtrlID(IntPtr hWnd);


        [DllImport("user32.dll")]
        [return: MarshalAs(UnmanagedType.Bool)]
        public static extern bool IsWindowEnabled(IntPtr hWnd);


        [DllImport("user32.dll")]
        public static extern Int32 FlashWindowEx(ref FLASHWINFO pwfi);

        [DllImport("user32.dll")]
        [return: MarshalAs(UnmanagedType.Bool)]
        public static extern bool EnumChildWindows(IntPtr hwndParent, EnumWindowProc callback, IntPtr lParam);

        [DllImport("user32.dll")]
        [return: MarshalAs(UnmanagedType.SysUInt)]
        public static extern IntPtr GetMenu(IntPtr hWnd);

        [DllImport("user32.dll")]
        public static extern uint GetMenuItemCount(IntPtr hMenu);

        [DllImport("user32.dll")]
        public static extern IntPtr GetWindow(IntPtr hWnd, uint wCmd);

        [DllImport("user32.dll")]
        public static extern IntPtr GetWindowLong(IntPtr hWnd, uint wCmd);

        [DllImport("user32.dll")]
        public static extern int GetMenuString(IntPtr hMenu, uint uIDItem,
           [Out, MarshalAs(UnmanagedType.LPStr)] StringBuilder lpString, int nMaxCount, uint uFlag);


        [DllImport("user32.dll")]
        public static extern bool ClientToScreen(IntPtr hwnd, ref Point lpPoint);


        [DllImport("user32.dll")]
        public static extern bool GetMenuItemRect(IntPtr hWnd, IntPtr hMenu, uint uItem,
           out RECT lprcItem);

        [DllImport("user32.dll")]
        public static extern int EnumWindows(CallBackPtr callPtr, int lPar);

        [DllImport("user32.dll", EntryPoint = "FindWindow", SetLastError = true)]
        public static extern IntPtr FindWindowByCaption(int ZeroOnly, string lpWindowName);

        [DllImport("user32.dll", SetLastError = true)]
        static extern IntPtr FindWindow(string lpClassName, string lpWindowName);

        [DllImport("user32.dll", SetLastError = true)]
        public static extern IntPtr FindWindowEx(IntPtr parentHandle, IntPtr childAfter, string className, string windowTitle);

        [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        public static extern int GetWindowText(IntPtr hWnd, StringBuilder lpString, int nMaxCount);

        [DllImport("user32.dll", SetLastError = true, CharSet = CharSet.Auto)]
        public static extern int GetWindowTextLength(IntPtr hWnd);

        [DllImport("user32.dll")]
        public static extern uint GetMenuItemID(IntPtr hMenu, int nPos);

        // [DllImport("user32.dll")]
        //public static extern Int32 SendMessage(IntPtr hWnd, uint Msg, int wParam, uint lParam);

        [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = false)]
        public static extern Int32 SendMessage(IntPtr hWnd, uint msg, int wParam, uint lParam);

        [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = false)]
        public static extern Int32 PostMessage(IntPtr hWnd, uint msg, int wParam, uint lParam);

        [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = false)]
        public static extern IntPtr GetParent(IntPtr hWnd);

        [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = false)]
        public static extern IntPtr GetAncestor(IntPtr hWnd, uint id);


        [DllImport("user32.dll")]
        public static extern bool SetWindowText(IntPtr hWnd, string lpString);

        [DllImport("user32.dll")]
        public static extern bool SendMessage(IntPtr hWnd, uint Msg, int wParam, _LVHITTESTINFO hitTest);

        [DllImport("user32.dll")]
        public static extern IntPtr GetDesktopWindow();

        [DllImport("User32.dll")]
        public static extern bool AnimateWindow(IntPtr hWnd, UInt32 time, UInt32 flags);


        [DllImport("user32.dll")]
        public static extern IntPtr GetDC(IntPtr hWnd);

        [DllImport("Gdi32.dll")]
        public static extern IntPtr CreateCompatibleDC(IntPtr hDC);

        [DllImport("Gdi32.dll")]
        public static extern IntPtr CreateCompatibleBitmap(IntPtr hDC, int width, int Height);

        [DllImport("Gdi32.dll")]
        public static extern IntPtr SelectObject(IntPtr hCaptureDC, IntPtr hBitmap);

        [DllImport("Gdi32.dll")]
        public static extern IntPtr BitBlt(IntPtr hCaptureDC, int left, int top, int width, int height, IntPtr hDC, int xSrc, int ySrc, uint dwROP);

        [DllImport("user32.dll")]
        public static extern void SendMessage(IntPtr hwnd, uint msg, IntPtr wp, ref RECT lp);

        [DllImport("user32.dll", CharSet = CharSet.Auto)]
        public static extern IntPtr SendMessage(IntPtr hWnd, uint Msg, IntPtr wParam, IntPtr lParam);

        [DllImport("user32.dll")]
        public static extern IntPtr WindowFromPoint(Point Point);
        [DllImport("user32.dll")]
        public static extern bool GetCursorPos(out Point lpPoint);

        [DllImport("User32.dll")]
        public static extern int ShowWindow(IntPtr hwnd, int nCmdShow);

        #endregion

        #region public Functions

        public static uint MakeLParam(int LoWord, int HiWord)
        {
            return (uint)((HiWord << 16) | (LoWord & 0xffff));
        }


        public static string GetText(IntPtr hWnd)
        {
            // Allocate correct string length first
            int length = GetWindowTextLength(hWnd);
            StringBuilder sb = new StringBuilder(length + 1);
            GetWindowText(hWnd, sb, sb.Capacity);
            return sb.ToString();
        }

        public static List<IntPtr> GetChildWindows(IntPtr parent)
        {
            List<IntPtr> result = new List<IntPtr>();
            GCHandle listHandle = GCHandle.Alloc(result);
            try
            {
                EnumWindowProc childProc = new EnumWindowProc(EnumWindow);
                EnumChildWindows(parent, childProc, GCHandle.ToIntPtr(listHandle));
            }
            finally
            {
                if (listHandle.IsAllocated)
                    listHandle.Free();
            }
            return result;
        }

        public static IntPtr FindWindow(string windowName, bool wait)
        {
            IntPtr hWnd = FindWindow(null, windowName);
            while (wait && hWnd == IntPtr.Zero)
            {
                Thread.Sleep(500);
                hWnd = FindWindow(null, windowName);
            }

            return hWnd;
        }

        public static string GetWindowClassName(IntPtr handle)
        {
            StringBuilder sb = new StringBuilder(255);
            GetClassName(handle, sb, sb.Length);
            return sb.ToString();
        }
        public static IntPtr GetProcesshandle(string ApplicationName)
        {
            Process[] instances = Process.GetProcessesByName(ApplicationName);
            if (instances.GetLength(0) > 0)
            {
                return instances[0].MainWindowHandle;
            }

            return IntPtr.Zero;
        }

        public static IntPtr GetTopLevelWindow(IntPtr MainWindowHandle)
        {
            List<IntPtr> childWindows = GetChildWindows(MainWindowHandle);
            return childWindows[0];
        }

        public static Point GetLocation(IntPtr handle)
        {
            return GetRectangle(handle).Location;
        }

        public static Size GetSize(IntPtr handle)
        {
            return GetRectangle(handle).Size;
        }

        public static Point GetCenter(IntPtr handle)
        {
            Rectangle windowrect = GetRectangle(handle);
            Point location = windowrect.Location;
            location.X = location.X + windowrect.Size.Width / 2;
            location.Y = location.Y + windowrect.Size.Height / 2;
            return location;
        }

        #endregion

        #region Private Functions

        private static Rectangle GetRectangle(IntPtr handle)
        {
            RECT rc;
            GetWindowRect(handle, out rc);
            Rectangle rectangle = new Rectangle();
            rectangle.Location = new Point(rc.Left, rc.Top);
            int width = rc.Right - rc.Left;
            int height = rc.Bottom - rc.Top;
            rectangle.Size = new Size(width, height);
            return rectangle;
        }

        private static bool EnumWindow(IntPtr handle, IntPtr pointer)
        {
            GCHandle gch = GCHandle.FromIntPtr(pointer);
            List<IntPtr> list = gch.Target as List<IntPtr>;
            if (list == null)
            {
                throw new InvalidCastException("GCHandle Target could not be cast as List<IntPtr>");
            }
            //if (GetText(handle)==condition)
            //{
            list.Add(handle);
            //}
            return true;
        }

        #endregion
    }

    public static class Win32Helper
    {
        /// <summary>
        /// C-Style Function to Invoke Information Remotely
        /// </summary>
        /// <param name="Path"></param>
        /// <param name="ProcId"></param>
        [DllImport("Injector.dll")]
        public static extern void InvokeInfo(string Path, ulong ProcId);


        /// <summary>
        /// C-Style Function to Retreive Information in the form of String
        /// </summary>
        /// <returns></returns>
        [DllImport("Injector.dll")]
        public static extern string ExtractData();

        /// <summary>
        /// C-Style Function to Open Shared Space
        /// </summary>
        /// <param name="ShelfIndex"></param>
        /// <param name="ThumbNailIndex"></param>
        /// <param name="PropName"></param>
        /// <param name="PropValue"></param>
        /// <returns></returns>
        [DllImport("Injector.dll")]
        public static extern int OpenSharedMemory(int ShelfIndex, int ThumbNailIndex, string PropName, string PropValue);

        /// <summary>
        /// C-Style Function to Close Shared Space
        /// </summary>
        /// <returns></returns>
        [DllImport("Injector.dll")]
        public static extern int CloseSharedMemory();

    }

    public static class Injector
    {

        private static string m_driverPath="Win32GenProxy.dll";
 
        public static ulong GetProcessId(string ProcessName)
        {
            ulong ProcId = 0;
            foreach (Process p in Process.GetProcesses())
            {
                if (p.ProcessName == ProcessName)
                    ProcId = (ulong)p.Id;
            }
            return ProcId;
        }

        public static string InvokeInfoFromApplication(int nShelfIndex, int nIndex, string FunctionName)
        {
            string ReturnValue = string.Empty;
            ulong pId = GetProcessId("iSiteRadiology");
            if (pId != 0)
            {
                Win32Helper.OpenSharedMemory(nShelfIndex, nIndex, FunctionName, "");
                Win32Helper.InvokeInfo(m_driverPath, pId);
                ReturnValue= "";
                ReturnValue = Win32Helper.ExtractData();
                Win32Helper.CloseSharedMemory();
                
            }
            else
            {
                throw new Exception(String.Format("Application {0} Not Running", "iSiteRadiology.exe"));
            }

            return ReturnValue;
        }
    }

}
#region Revision History
/// 3-Sep-2009  Pattabi
///             0002793: Cannot find the bone dicom folder : test case NM -03 
#endregion Revision History
