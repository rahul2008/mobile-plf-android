package com.philips.cl.di.digitalcare.social;

/**
 * This interfaces send callback message to Facebook & Twitter
 *              after successfully posting the messages to there respective
 *              Philips Wall.
 *
 * @author naveen@philips.com
 *               
 * @since Mar 13, 2015
 */
public interface PostCallback {

	void onTaskCompleted();

	void onTaskFailed();
}
