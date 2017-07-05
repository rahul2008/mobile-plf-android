package com.philips.amwelluapp.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * Created by philips on 7/4/17.
 */

public class THSNoppFragment extends PTHBaseFragment {
    public static final String TAG = THSNoppFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    private WebView noppWebView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_nopp_fragment, container, false);
       /* noppWebView = (WebView)view.findViewById(R.id.ths_nopp_content_webview);
        noppWebView.setVerticalScrollBarEnabled(true);

        noppWebView.loadData(getActivity().getApplicationContext().getResources().getString(R.string.pth_webview_test), "text/html", "UTF-8");*/
                return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
    }
}
