package com.philips.platform.catalogapp;

import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ContentColor;
import com.philips.platform.uit.thememanager.NavigationColor;

public interface ThemeSettingsChanged {

    public void onContentColorChanged(ContentColor contentColor);

    public void onNavigationColorChanged(NavigationColor contentColor);

    public void onColorRangeChanged(ColorRange contentColor);
}
