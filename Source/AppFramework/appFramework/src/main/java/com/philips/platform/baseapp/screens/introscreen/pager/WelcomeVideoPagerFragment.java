/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.introscreen.pager;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.dataservices.utility.Utility;
import com.philips.platform.baseapp.screens.utility.Constants;

import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeVideoPagerFragment extends Fragment implements TextureVideoView.MediaPlayerListener, View.OnClickListener {

    private TextureVideoView videoView;

    private ProgressBar progressBar;

    private boolean isVideoPlaying = false;

    private boolean isDataSourceSet = false;

    private ImageView onboarding_play_button, thumbNail;

    public WelcomeVideoPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_welcome_video_pager, container, false);

        videoView = (TextureVideoView) rootView.findViewById(R.id.onboarding_video);
        thumbNail = (ImageView) rootView.findViewById(R.id.thumb_nail);
        onboarding_play_button = (ImageView) rootView.findViewById(R.id.onboarding_play_button);

        thumbNail.setOnClickListener(this);
        setVideoViewHeight();

        videoView.setOnClickListener(this);
        progressBar = (ProgressBar) rootView.findViewById(R.id.onboarding_video_progress_bar);

        return rootView;
    }

    private void setVideoViewHeight() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        videoView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (size.x * 204) / 288));
        thumbNail.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (size.x * 204) / 288));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        videoView.setListener(this);
    }

    private void setVideoDataSource() {

        if (!new Utility().isOnline(getActivity())) {
            loadInitialState();
            Toast.makeText(getActivity(), "Internet connection is not available", Toast.LENGTH_LONG).show();
            return;
        }
        ServiceDiscoveryInterface serviceDiscoveryInterface = getApplicationContext().getAppInfra().getServiceDiscovery();

        serviceDiscoveryInterface.getServiceUrlWithLanguagePreference(Constants.SERVICE_DISCOVERY_SPLASH_VIDEO,
                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onSuccess(URL url) {
                        videoView.setDataSource(url.toString()+"_iPad_640x480_1000K");
                        isDataSourceSet = true;
                    }

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        Toast.makeText(getActivity(), "Check Internet Connectivity", Toast.LENGTH_LONG).show();
                        loadInitialState();
                    }
                });
    }

    private void loadInitialState() {
        thumbNail.setVisibility(View.VISIBLE);
        onboarding_play_button.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        isDataSourceSet = false;
        isVideoPlaying = false;
    }

    public AppFrameworkApplication getApplicationContext(){
        return (AppFrameworkApplication) getActivity().getApplicationContext();
    }
    @Override
    public void onVideoPrepared() {
        playVideo();
        progressBar.setVisibility(View.GONE);
    }

    private void playVideo() {
        if (null == videoView) {
            return;
        }
        videoView.play();
    }

    @Override
    public void onVideoEnd() {
        isVideoPlaying = false;
        thumbNail.setVisibility(View.VISIBLE);
        onboarding_play_button.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVideoPlay() {
        isVideoPlaying = true;
        onboarding_play_button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);
        thumbNail.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseVideo();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVideoPlaying) {
            playVideo();
        } else {
            pauseVideo();
        }
    }

    private void pauseVideo() {
        if (null != videoView) {
            videoView.pause();
            onboarding_play_button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoView.release();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.thumb_nail:
                isVideoPlaying = true;
                onboarding_play_button.setVisibility(View.GONE);
                thumbNail.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if (isDataSourceSet) {
                    videoView.play();
                } else {
                    setVideoDataSource();
                }
            break;
            case R.id.onboarding_video:
                if (onboarding_play_button.getVisibility() == View.VISIBLE) {
                    isVideoPlaying = true;
                    playVideo();
                } else {
                    isVideoPlaying = false;
                    pauseVideo();
                }
                break;
        }
    }
}
