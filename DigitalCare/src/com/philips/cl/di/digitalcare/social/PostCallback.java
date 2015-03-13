package com.philips.cl.di.digitalcare.social;

/**
 * 
 * @author naveen@philips.com
 * @description This interfaces send callback message to Facebook & Twitter
 *              after successfully posting the messages to there respective
 *              Philips Wall.
 * @Since Mar 13, 2015
 */
public interface PostCallback {

	void onTaskCompleted();

	void onTaskFailed();
}
