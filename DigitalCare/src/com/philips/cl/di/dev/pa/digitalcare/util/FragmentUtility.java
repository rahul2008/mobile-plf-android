package com.philips.cl.di.dev.pa.digitalcare.util;

import android.content.res.Configuration;
import android.view.View.OnClickListener;


/**
 * 
 * @description Utility class used in all the Fragments
 * @author naveen@philips.com
 * @since 11/Feb/2015
 *
 */
public interface FragmentUtility extends OnClickListener {

	void setViewParams(Configuration config);

}
