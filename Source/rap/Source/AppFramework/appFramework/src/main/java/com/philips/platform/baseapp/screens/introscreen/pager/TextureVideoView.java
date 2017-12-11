package com.philips.platform.baseapp.screens.introscreen.pager;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import com.philips.platform.baseapp.screens.utility.RALog;

import java.io.IOException;

/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2014 Danylyk Dmytro
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 *
 *   Taken from https://github.com/dmytrodanylyk/video-crop
 */

public class TextureVideoView extends TextureView implements TextureView.SurfaceTextureListener {

    // Log tag
    private static final String TAG = TextureVideoView.class.getName();

    private MediaPlayer mMediaPlayer;

    private boolean mIsViewAvailable;

    private boolean mIsVideoPrepared;

    private boolean mIsPlayCalled;

    private boolean mIsDataSourceSet;

    private State mState;

    public boolean ismIsDataSourceSet() {
        return mIsDataSourceSet;
    }

    public void setmIsVideoPrepared(boolean mIsVideoPrepared) {
        this.mIsVideoPrepared = mIsVideoPrepared;
    }

    public boolean ismIsPlayCalled() {
        return mIsPlayCalled;
    }

    public void setmIsPlayCalled(boolean mIsPlayCalled) {
        this.mIsPlayCalled = mIsPlayCalled;
    }

    public State getmState() {
        return mState;
    }

    public void setmState(State mState) {
        this.mState = mState;
    }

    public void setmIsViewAvailable(boolean mIsViewAvailable) {
        this.mIsViewAvailable = mIsViewAvailable;
    }

    public enum State {
        UNINITIALIZED, PLAY, STOP, PAUSE, END
    }

    public TextureVideoView(Context context) {
        super(context);
        initView();
    }

    public TextureVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TextureVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        initPlayer();
        setSurfaceTextureListener(this);
    }

    private void initPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        } else {
            mMediaPlayer.reset();
        }
        mIsVideoPrepared = false;
        mIsPlayCalled = false;
        mState = State.UNINITIALIZED;
    }

    /**
     * @see android.media.MediaPlayer#setDataSource(String)
     */
    public void setDataSource(String path) {
        initPlayer();

        try {
            mMediaPlayer.setDataSource(path);
            mIsDataSourceSet = true;
            prepare();
        } catch (IOException e) {
            log(e.getMessage());
        }
    }

    private void prepare() {
        try {
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mState = State.END;
                    log("Video has ended.");

                    if (mListener != null) {
                        mListener.onVideoEnd();
                    }
                }
            });

            prepareAsyncMediaPlayer();

            // Play video when the media source is ready for playback.
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mIsVideoPrepared = true;
                    if (mIsPlayCalled && mIsViewAvailable) {
                        log("Player is prepared and play() was called.");
                        play();
                    }

                    if (mListener != null) {
                        mListener.onVideoPrepared();
                    }
                }
            });

        } catch (IllegalArgumentException e) {
            log(e.getMessage());
        } catch (SecurityException e) {
            log(e.getMessage());
        } catch (IllegalStateException e) {
            log(e.toString());
        }
    }

    private void prepareAsyncMediaPlayer() {
        try {
            // don't forget to call MediaPlayer.prepareAsync() method when you use constructor for
            // creating MediaPlayer
            mMediaPlayer.prepareAsync();
        } catch (UnsatisfiedLinkError e) {
            log(e.toString());
        }
    }

    /**
     * Play or resume video. Video will be played as soon as view is available and media player is
     * prepared.
     * <p>
     * If video is stopped or ended and play() method was called, video will start over.
     */
    public void play() {
        if (!mIsDataSourceSet) {
            log("play() was called but data source was not set.");
            return;
        }

        mIsPlayCalled = true;

        if (!mIsVideoPrepared) {
            log("play() was called but video is not prepared yet, waiting.");
            return;
        }

        if (!mIsViewAvailable) {
            log("play() was called but view is not available yet, waiting.");
            return;
        }

        if (mState == State.PLAY) {
            log("play() was called but video is already playing.");
            return;
        }

        if (mState == State.END || mState == State.STOP) {
            log("play() was called but video already ended, starting over.");
            mMediaPlayer.seekTo(0);
        }

        mState = State.PLAY;
        mMediaPlayer.start();

        if (mListener != null) {
            mListener.onVideoPlay();
        }
    }

    /**
     * Pause video. If video is already paused, stopped or ended nothing will happen.
     */
    public void pause() {
        mIsPlayCalled = false;
        switch (mState) {
            case PAUSE:
                log("pause() was called but video already paused.");
                return;
            case STOP:
                log("pause() was called but video already stopped.");
                return;
            case END:
                log("pause() was called but video already ended.");
                return;
            default:
                mState = State.PAUSE;
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
        }
    }

    void log(String message) {
        RALog.d(TAG, message);
    }

    private MediaPlayerListener mListener;

    /**
     * Listener trigger 'onVideoPrepared' and `onVideoEnd` events
     */
    public void setListener(MediaPlayerListener listener) {
        mListener = listener;
    }

    public interface MediaPlayerListener {

        void onVideoPrepared();

        void onVideoEnd();

        void onVideoPlay();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (mMediaPlayer != null) {
            Surface surface = new Surface(surfaceTexture);
            mMediaPlayer.setSurface(surface);
            mIsViewAvailable = true;
            if (mIsDataSourceSet && mIsPlayCalled && mIsVideoPrepared) {
                log("View is available and play() was called.");
                play();
            }
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void release() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }

            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}