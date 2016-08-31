using System;
using System.Reflection;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Text;

namespace ST_Nucleo
{
    public class ScriptExecutor
    {

        private string commPort;
        
        string fileName = Path.Combine(
            @"C:\dev\reference_app_androidDev\testfwk_android\Src\Src\ST_Nucleo\ScriptLuncher", 
            "BlueNRG_Script_Launcher.exe"
        );
        string scriptRoot = @"C:\dev\reference_app_androidDev\testfwk_android\Src\Src\ST_Nucleo\pyScripts\";
        private Process process;


        public ScriptExecutor(string serialPort)
        {
            process = new Process();
            process.StartInfo.FileName = fileName;
            process.StartInfo.UseShellExecute = false;
            process.StartInfo.RedirectStandardOutput = true;
            process.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;
            process.StartInfo.UseShellExecute = false;
            process.StartInfo.RedirectStandardOutput = true;
            commPort = serialPort;
        }

        public Dictionary<string, string> ExecuteScript(string scriptName)
        {
            string scriptPath = scriptRoot + scriptName;
            process.StartInfo.Arguments = "-l " + "-p " + commPort + "  -s " + scriptPath;
            process.Start();
            string strOutput = process.StandardOutput.ReadToEnd();
            Debug.WriteLine(strOutput);
            process.WaitForExit();
            return EvaluateOutput(strOutput);
        }

        public Dictionary<string, string> EvaluateOutput(string log)
        {
            string[] linesNewLine = log.Split(new string[] { "\r\n", "\n", "\n\r", "*" }, StringSplitOptions.None);
            Dictionary<string, string> output = new Dictionary<string, string>();

            foreach (var line in linesNewLine)
            {
                if (line.Contains("LOG: "))
                {
                    StringBuilder builder = new StringBuilder();
                    foreach (var value in line)
                    {
                        builder.Append(value);
                    }

                    builder.Append("\n");
                    continue;
                }

                if (line.Contains("Exception:"))
                {
                    output.Add("Exception", "-1");
                    return output;
                }


                string[] data = line.Split(';');
                for (int i = 0; i < data.Length; i++)
                {
                    if (data[i].CompareTo("OUTPUT") == 0)
                    {
                        output.Add("OUTPUT", data[i + 1]);
                        i = i + 2;
                    }

                    if (data[i].CompareTo("OUTPUT_DATA") == 0)
                    {
                        output.Add(data[i + 2], data[i + 1]);
                        i = i + 2;
                    }
                }
            }

            return output;
        }
        private static string GetAbsolutePath()
        {
            return ((new Uri(Assembly.GetExecutingAssembly().CodeBase)).AbsolutePath);
        }
    }

}
