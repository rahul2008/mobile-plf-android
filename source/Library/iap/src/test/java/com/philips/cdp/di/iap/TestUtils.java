/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class TestUtils {

    public static String readFile(Class<?>cls, String fileName) {
        BufferedReader br = null;
        String path = prepareCurrentPath(cls, fileName);
        try {
            br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String prepareCurrentPath(Class<?> cls, String fileName) {
        String pckg = cls.getName();
        String[] paths = pckg.split("\\.");
        StringBuilder sb = new StringBuilder();
        sb.append("src").append(File.separator);
        sb.append("test").append(File.separator);
        sb.append("java").append(File.separator);
        for (int index = 0; index < paths.length-1; index++) {

            sb.append(paths[index]).append(File.separator);
        }
        sb.append(fileName);
        return sb.toString();
    }
}
