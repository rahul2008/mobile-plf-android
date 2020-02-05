/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.introscreen.pager;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.platform.appframework.R;



/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeVideoPagerFragment extends Fragment implements WelcomeVideoFragmentContract.View,
        TextureVideoView.MediaPlayerListener, View.OnClickListener {

    private TextureVideoView videoView;

    private ProgressBar progressBar;

    private boolean isVideoPlaying = false;

    private boolean isDataSourceSet = false;

    private ImageView onboarding_play_button, thumbNail;

    private WelcomeVideoFragmentContract.Presenter presenter;

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

        presenter = getWelcomeVideoPagerPresenter();
        return rootView;
    }

    protected WelcomeVideoFragmentContract.Presenter getWelcomeVideoPagerPresenter() {
        return new WelcomeVideoPresenter(this, getActivity());
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setVideoViewHeight();
        }
    }

    public void fetchVideoDataSource() {


        presenter.fetchVideoDataSource();
    }

    private void loadInitialState() {
        thumbNail.setVisibility(View.VISIBLE);
        onboarding_play_button.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        isDataSourceSet = false;
        isVideoPlaying = false;
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
    public void onDestroy() {
        super.onDestroy();
        if(videoView!=null) {
            videoView.release();
        }
    }

    public boolean isVideoPlaying() {
        return isVideoPlaying;
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
                    fetchVideoDataSource();
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

    @Override
    public void setVideoDataSource(String videoUrl) {
        videoView.setDataSource(videoUrl);
        isDataSourceSet = true;
    }

    @Override
    public void onFetchError() {
        Toast.makeText(getActivity(), getString(R.string.RA_DLS_check_internet_connectivity), Toast.LENGTH_LONG).show();
        loadInitialState();
    }
}
