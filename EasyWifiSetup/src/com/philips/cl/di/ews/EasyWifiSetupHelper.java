package com.philips.cl.di.ews;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.math.BigInteger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import java.math.BigInteger;
import java.security.*;

import javax.crypto.Cipher;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class EasyWifiSetupHelper {
	public static String pakckage = "com.philips.cl.di.ews"; 
	public static int get(String name) {
		   Class<?> r = null;
	       Class<?>[] classes;
	       Field f;
	       int id = 0;
	        try {
				r = Class.forName(pakckage + ".R");
			

	        classes = r.getClasses();
	        for (int i = 0; i < classes.length; i++) {
	            f = classes[i].getField(name);
	        	if( f !=null)
	        		return f.getInt(classes[i]);
	            }
	        }catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    return id;

	    }
	
	
	public static int getResource(String type ,String name) {
	       Class<?> r = null;
	       int id = 0;
	    try {
	        r = Class.forName(pakckage+ type);
	            id = r.getField(name).getInt(r);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return id;

	    }
	public static int getLayout(String name) {
		return getResource(".R$layout", name);
    }

	public static int getId(String name) {
		return getResource(".R$id", name);
	}
	
	
	

    /**
     * Convert byte array to hex string
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for(int idx=0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array.
     * @param str
     * @return  array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try { return str.getBytes("UTF-8"); } catch (Exception ex) { return null; }
    }

    /**
     * Load UTF8withBOM or any ansi text file.
     * @param filename
     * @return  
     * @throws java.io.IOException
     */
    public static String loadFileAsString(String filename) throws java.io.IOException {
        final int BUFLEN=1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8=false;
            int read,count=0;           
            while((read=is.read(bytes)) != -1) {
                if (count==0 && bytes[0]==(byte)0xEF && bytes[1]==(byte)0xBB && bytes[2]==(byte)0xBF ) {
                    isUTF8=true;
                    baos.write(bytes, 3, read-3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count+=read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try{ is.close(); } catch(Exception ex){} 
        }
    }

    /**
     * Returns MAC address of the given interface name.
     * @param interfaceName eth0, wlan0 or NULL=use first interface 
     * @return  mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx=0; idx<mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));       
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    /**
     * Get IP address from first non-localhost interface
     * @param ipv4  true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                        if (useIPv4) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    public static String deviceScanner() {
    String ip =getIPAddress(true); 
    String iIPv4 = ip.substring(0, ip.lastIndexOf(".")+1);
    Log.i("Device Scanner", "iIPv4= " +iIPv4);
	HttpClient httpclient = new DefaultHttpClient();
	HttpResponse response ;
    // Loop to scan each address on the local subnet
    for (int i = 1; i < 255; i++) {
        try {
        	 //System.out.println("address count"+i);
        	 HttpGet getRequest = new HttpGet("http://"+iIPv4+i+EasyWifiSetupInfo.WIFI_URI);
        	 response = httpclient.execute(getRequest);
        	 if (response.getStatusLine().getStatusCode() == 200) {
        		 Log.i("Device Scanner", "http iIPv4= " +"https://"+iIPv4+i);
        		 return "https://"+iIPv4+i;
        		 
        	}

        } catch (Exception e) {
        }
    }
    return null;
}


}
