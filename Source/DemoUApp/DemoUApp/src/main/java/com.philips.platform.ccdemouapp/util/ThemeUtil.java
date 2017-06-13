package com.philips.platform.ccdemouapp.util;

import android.content.SharedPreferences;

/**
 * Created by arbin on 27/02/2017.
 */

public class ThemeUtil {
    private static int[] themes = {
            com.philips.cdp.uikit.R.style.Theme_Philips_DarkBlue_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_BrightOrange_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_BrightAqua_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_BrightGreen_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_BrightPink_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_DarkPurple_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_DarkAqua_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_DarkGreen_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_DarkOrange_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_DarkPink_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_BrightBlue_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_BrightPurple_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_LightBlue_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_LightOrange_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_LightAqua_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_LightGreen_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_LightPink_WhiteBackground,
            com.philips.cdp.uikit.R.style.Theme_Philips_LightPurple_WhiteBackground
    };
   private SharedPreferences sharedPreferences;

    public ThemeUtil(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public  int getNextTheme(){
       int current_theme_index = sharedPreferences.getInt("CURRENT_THEME",0);
        if(current_theme_index == themes.length -1){
            current_theme_index =0;
        }else {
            current_theme_index++;
        }
        sharedPreferences.edit().putInt("CURRENT_THEME",current_theme_index).commit();
        return themes[current_theme_index];
    }

    public  int getCurrentTheme() {
        int current_theme_index = sharedPreferences.getInt("CURRENT_THEME",0);
        return themes[current_theme_index];
    }
}
