package com.philips.platform.appframework.aboutscreen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.appframework.AppFrameworkBaseFragment;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;

/**
 * Created by 310213373 on 8/10/2016.
 */
public class AboutScreenFragment extends AppFrameworkBaseFragment
{
    private String TAG = getClass().toString();

    @Override
    public String getActionbarTitle() {
        return "About";
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uikit_about_screen, container, false);
        TextView  version =(TextView)view.findViewById(R.id.about_version);
        version.setText("App Version" +BuildConfig.VERSION_NAME);
       view.setBackgroundColor(getResources().getColor(R.color.uikit_philips_dark_blue));
        TextView  content =(TextView)view.findViewById(R.id.about_content);
        content.setText(R.string.about_screen_description);
        return view;
    }
}
