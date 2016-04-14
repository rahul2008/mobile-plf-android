package com.philips.cdp.digitalcare.faq;

import android.os.Environment;

import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GetURLContent {
	public static void getContent(String android) {

		URL url;

		try {
			// get URL content
			url = new URL(android);
			URLConnection conn = url.openConnection();

			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));

			String inputLine;

			File externalFilePath = Environment.getExternalStorageDirectory();

			//String fileName = "/users/mkyong/test.html";
			File file = new File(externalFilePath.getAbsolutePath() + "faq.html");

			if (!file.exists()) {
				file.createNewFile();
				DigiCareLogger.d("FaqDetail", "File already Created");
			}else
			{
				DigiCareLogger.d("FaqDetail", "File not created");
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			while ((inputLine = br.readLine()) != null) {
				bw.write(inputLine);
			}

			bw.close();
			br.close();

			System.out.println("Done");

		} catch (MalformedURLException e) {
			DigiCareLogger.d("FaqDetail", " " + e);
		} catch (IOException e) {
			DigiCareLogger.d("FaqDetail", " " + e);
		}

	}
}