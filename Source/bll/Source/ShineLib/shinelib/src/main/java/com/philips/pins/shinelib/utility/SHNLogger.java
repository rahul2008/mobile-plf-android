package com.philips.pins.shinelib.utility;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 * This class acts as a wrapper around logging and allows registering of new logging receivers.
 * </p>
 *
 * <p>
 * In order to turn on LogCat output you can use the {@link LogCatLogger} class,
 * by putting the following line into your application: (e.g. in onCreate)
 * </p>
 *
 * <code>
 *     SHNLogger.registerLogger(new LogCatLogger());
 * </code>
 */
public class SHNLogger {

    private static final DelegatingLogger ROOT_LOGGER = new DelegatingLogger();

    public interface LoggerImplementation {
        void logLine(int priority, String tag, String msg, Throwable tr);
    }

    /**
     * Allows registering a LoggerImplementation instance.
     *
     * @param logger The logger instance that should receive log calls
     */
    public static void registerLogger(LoggerImplementation logger) {
        synchronized (ROOT_LOGGER.loggers) {
            ROOT_LOGGER.loggers.add(logger);
        }
    }

    /**
     * Allows unregistering a LoggerImplementation instance.
     *
     * @param logger The logger instance that should no longer receive log calls
     */
    public static void unregisterLogger(LoggerImplementation logger) {
        synchronized (ROOT_LOGGER.loggers) {
            ROOT_LOGGER.loggers.remove(logger);
        }
    }

    /* ---------------------------------------------------------------------------- */
    /* ----------------------- Logging calls -------------------------------------- */
    /* ---------------------------------------------------------------------------- */

    /**
     * Send a {@link android.util.Log#VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        triggerLoggers(Log.VERBOSE, tag, msg);
    }

    /**
     * Send a {@link android.util.Log#VERBOSE} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void v(String tag, String msg, Throwable tr) {
        triggerLoggers(Log.VERBOSE, tag, msg, tr);
    }

    /**
     * Send a {@link android.util.Log#DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        triggerLoggers(Log.DEBUG, tag, msg);
    }

    /**
     * Send a {@link android.util.Log#DEBUG} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void d(String tag, String msg, Throwable tr) {
        triggerLoggers(Log.DEBUG, tag, msg, tr);
    }

    /**
     * Send an {@link android.util.Log#INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        triggerLoggers(Log.INFO, tag, msg);
    }

    /**
     * Send a {@link android.util.Log#INFO} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void i(String tag, String msg, Throwable tr) {
        triggerLoggers(Log.INFO, tag, msg, tr);
    }

    /**
     * Send a {@link android.util.Log#WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        triggerLoggers(Log.WARN, tag, msg);
    }

    /**
     * Send a {@link android.util.Log#WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void w(String tag, String msg, Throwable tr) {
        triggerLoggers(Log.WARN, tag, msg, tr);
    }

    /**
     * Send a {@link android.util.Log#WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param tr  An exception to log
     */
    public static void w(String tag, Throwable tr) {
        triggerLoggers(Log.WARN, tag, "", tr);
    }

    /**
     * Send an {@link android.util.Log#ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        triggerLoggers(Log.ERROR, tag, msg);
    }

    /**
     * Send a {@link android.util.Log#ERROR} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void e(String tag, String msg, Throwable tr) {
        triggerLoggers(Log.ERROR, tag, msg, tr);
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen.
     * The error will always be logged at level {@link android.util.Log#ASSERT} with the call stack.
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     */
    public static void wtf(String tag, String msg) {
        triggerLoggers(Log.ASSERT, tag, msg);
    }

    /**
     * What a Terrible Failure: Report an exception that should never happen.
     * Similar to {@link #wtf(String, String)}, with an exception to log.
     * @param tag Used to identify the source of a log message.
     * @param tr An exception to log.
     */
    public static void wtf(String tag, Throwable tr) {
        triggerLoggers(Log.ASSERT, tag, "", tr);
    }

    /**
     * What a Terrible Failure: Report an exception that should never happen.
     * Similar to {@link #wtf(String, Throwable)}, with a message as well.
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     * @param tr An exception to log.  May be null.
     */
    public static void wtf(String tag, String msg, Throwable tr) {
        triggerLoggers(Log.ASSERT, tag, msg, tr);

    }

    private static void triggerLoggers(int priority, String tag, String msg) {
        ROOT_LOGGER.logLine(priority, tag, msg, null);
    }

    private static void triggerLoggers(int priority, String tag, String msg, Throwable tr) {
        ROOT_LOGGER.logLine(priority, tag, msg, tr);
    }

    /* ---------------------------------------------------------------------------- */
    /* ----------------------- Logger implementations ----------------------------- */
    /* ---------------------------------------------------------------------------- */

    private static class DelegatingLogger implements LoggerImplementation {

        private final List<LoggerImplementation> loggers = new CopyOnWriteArrayList<>();

        @Override
        public void logLine(final int priority, final String tag, final String msg, final Throwable tr) {
            for (final LoggerImplementation logger : loggers) {
                logger.logLine(priority, tag, msg, tr);
            }
        }
    }

    public static class LogCatLogger implements LoggerImplementation {

        @Override
        public void logLine(int priority, String tag, String msg, Throwable tr) {
            if (priority == Log.ASSERT) {
                Log.wtf(tag, msg, tr);
            }
            else {
                Log.println(priority, tag, msg + '\n' + Log.getStackTraceString(tr));
            }
        }
    }
}