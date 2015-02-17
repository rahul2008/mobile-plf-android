package com.philips.cl.di.dev.pa.digitalcare.listners;

import android.content.res.Configuration;
import android.view.View.OnClickListener;


/**
 * 
 * @description Utility class used in all the Fragments
 * @author naveen@philips.com
 * @since 11/Feb/2015
 *
 */
public interface FragmentUtilityInterface extends OnClickListener {

	void setViewParams(Configuration config);

}
