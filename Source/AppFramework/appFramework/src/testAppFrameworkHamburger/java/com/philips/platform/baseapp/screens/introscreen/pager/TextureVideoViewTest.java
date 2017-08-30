package com.philips.platform.baseapp.screens.introscreen.pager;

import android.content.Context;
import android.graphics.SurfaceTexture;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

/**
 * Created by philips on 8/17/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class TextureVideoViewTest {

    private TextureVideoView textureVideoView;

    private final String videoUrl = "https://images.philips.com/skins/PhilipsConsumer/CDP2_reference_app_vid_short";

    private TextureVideoView.MediaPlayerListener mediaPlayerListener;

    @Before
    public void setUp() {
        Context context = mock(Context.class);
        textureVideoView = new TextureVideoView(context);
        mediaPlayerListener = mock(TextureVideoView.MediaPlayerListener.class);
        textureVideoView.setListener(mediaPlayerListener);
    }

    @Test
    public void verifyDataSourceSetCalled() {
        textureVideoView.setDataSource(videoUrl);
        Assert.assertTrue(textureVideoView.ismIsDataSourceSet());
    }

    @Test
    public void verifyPlayCalled() {
        textureVideoView.setDataSource(videoUrl);
        textureVideoView.play();
        Assert.assertTrue(textureVideoView.ismIsPlayCalled());
    }

    @Test
    public void testPauseWithPauseState() {
        textureVideoView.setmState(TextureVideoView.State.PAUSE);
        textureVideoView.pause();
        Assert.assertEquals(textureVideoView.getmState(), TextureVideoView.State.PAUSE);
    }

    @Test
    public void testPauseWithStopState() {
        textureVideoView.setmState(TextureVideoView.State.STOP);
        textureVideoView.pause();
        Assert.assertEquals(textureVideoView.getmState(), TextureVideoView.State.STOP);
    }

    @Test
    public void testPauseWithEndState() {
        textureVideoView.setmState(TextureVideoView.State.END);
        textureVideoView.pause();
        Assert.assertEquals(textureVideoView.getmState(), TextureVideoView.State.END);
    }

    @Test
    public void testPlayWithoutDataSourceSet() {
        textureVideoView.play();
        Assert.assertFalse(textureVideoView.ismIsPlayCalled());
    }

    @Test
    public void testPlayWithViewAvailableFalse() {
        textureVideoView.setDataSource(videoUrl);
        textureVideoView.setmIsVideoPrepared(true);
        textureVideoView.play();
        Assert.assertFalse(textureVideoView.getmState() == TextureVideoView.State.PLAY);
    }

    @Test
    public void testPlayWithPauseState() {
        textureVideoView.setDataSource(videoUrl);
        textureVideoView.setmIsVideoPrepared(true);
        textureVideoView.setmIsViewAvailable(true);
        textureVideoView.setmState(TextureVideoView.State.PAUSE);
        textureVideoView.play();
        Assert.assertTrue(textureVideoView.getmState() == TextureVideoView.State.PLAY);
    }

    @Test
    public void testonSurfaceTextureAvailable() {
        SurfaceTexture surfaceTexture = mock(SurfaceTexture.class);
        textureVideoView.setDataSource(videoUrl);
        textureVideoView.setmIsVideoPrepared(true);
        textureVideoView.setmIsPlayCalled(true);
        textureVideoView.onSurfaceTextureAvailable(surfaceTexture, 0, 0);
        Assert.assertEquals(textureVideoView.getmState(), TextureVideoView.State.PLAY);
    }

    @After
    public void tearDown() {
        mediaPlayerListener = null;
        textureVideoView = null;
    }
}
