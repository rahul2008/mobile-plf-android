package com.philips.platform.core.trackers;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.*;

/**
 * Created by sangamesh on 01/12/16.
 */
public class TimerDataTest {

    public static final DateTime NOW = DateTime.now();

    @Test
    public void IsNotRunning_ByDefault() throws Exception {
        TimerData data = new TimerData();

        assertThat(data.isRunning()).isFalse();
    }

    @Test
    public void StartIsNow_ByDefault() throws Exception {
        TimerData data = new TimerData();

        Seconds seconds = Seconds.secondsBetween(data.getInitialStart(), NOW);
        assertThat(seconds.getSeconds()).isEqualTo(0);
    }

    @Test
    public void IsNotRunning_WhenNoLastTimer() throws Exception {
        TimerData data = createTimerDataWithRuns(
                new TimerRun(NOW, 20)
        );

        assertThat(data.isRunning()).isFalse();
    }

    @NonNull
    private TimerData createTimerDataWithRuns(final TimerRun... run) {
        return new TimerData(Arrays.asList(run));
    }

    @Test
    public void IsRunning_WhenHasLastTimer() throws Exception {
        TimerData data = createTimerDataWithRuns(
                new TimerRun(NOW.minusSeconds(30), 20),
                new TimerRun(NOW, 0)
        );

        assertThat(data.isRunning()).isTrue();
    }

    @Test
    public void SecondsBasedOnPastDuration_WhenNotRunning() throws Exception {
        TimerData data = createTimerDataWithRuns(
                new TimerRun(NOW, 8)
        );

        assertThat(data.getSeconds()).isEqualTo(8);
    }

    @Test
    public void SecondsBasedOnDurationAndTimer_WhenRunning() throws Exception {
        int firstRunSeconds = 7;
        int secondsAgoTimerStarted = 5;

        TimerData data = createTimerDataWithRuns(
                new TimerRun(NOW.minusSeconds(100), firstRunSeconds),
                new TimerRun(NOW.minusSeconds(secondsAgoTimerStarted), 0)
        );

        assertThat(data.getSeconds()).isEqualTo(firstRunSeconds + secondsAgoTimerStarted);
    }

    @Test
    public void ConstructsCorrectUpdatedDuration_WhenStopped() throws Exception {
        TimerData data = createTimerDataWithRuns(
                new TimerRun(NOW.minusSeconds(100), 2),
                new TimerRun(NOW.minusSeconds(9), 0)
        );

        data.stop();

        assertThat(data.getSeconds()).isEqualTo(2 + 9);
    }

    @Test
    public void NotRunning_WhenStopped() throws Exception {
        TimerData data = createTimerDataWithRuns(
                new TimerRun(NOW.minusSeconds(100), 2),
                new TimerRun(NOW.minusSeconds(9), 0)
        );

        data.stop();

        assertThat(data.isRunning()).isFalse();
    }

    @Test
    public void SameInitialStart_WhenStopped() throws Exception {
        DateTime start = NOW.minusSeconds(100);

        TimerData data = createTimerDataWithRuns(
                new TimerRun(start, 2),
                new TimerRun(NOW.minusSeconds(9), 0)
        );

        data.stop();

        assertThat(data.getInitialStart()).isEqualTo(start);
    }

    @Test
    public void HasLastTimer_WhenStarted() throws Exception {
        TimerData data = createTimerDataWithRuns(
                new TimerRun(NOW.minusSeconds(100), 2)
        );

        data.start();

        assertThat(data.isRunning()).isTrue();
    }

    @Test
    public void InitialStartSetToNow_WhenNoPastRunning() throws Exception {
        TimerData data = new TimerData();

        data.start();

        assertThat(Seconds.secondsBetween(data.getInitialStart(), NOW).getSeconds()).isEqualTo(0);
    }

    @Test
    public void ConstructsTimerDataCorrectly() throws Exception {
        TimerData data = TimerData.createTimerData(NOW, 40);

        List<TimerRun> timerRuns = data.getTimerRuns();

        assertThat(timerRuns).hasSize(1);
        TimerRun run = timerRuns.get(0);
        assertThat(run.getDurationInSeconds()).isEqualTo(40);
        assertThat(run.getStart()).isEqualTo(NOW);
    }

    @Test
    public void ShouldStopTimerCorrectly_IfDurationLessThanSecond() throws Exception {
        TimerData data = TimerData.createTimerData(NOW, 0);

        data.stop();

        assertThat(data.isRunning()).isFalse();
        assertThat(data.getSeconds()).isZero();
    }

    @Test
    public void ShouldNotEraseTimerData_IfStoppedTwoTimes() throws Exception {
        TimerData data = TimerData.createTimerData(NOW, 23);

        data.stop();

        assertThat(data.isRunning()).isFalse();
        assertThat(data.getSeconds()).isNotZero();
    }

}