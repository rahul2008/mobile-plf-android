/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.homescreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.appframework.AppFrameworkBaseFragment;
import com.philips.platform.appframework.R;


public class HomeScreenFragment extends AppFrameworkBaseFragment {
    private TextView textView;
    private ImageView imageView;

    public HomeScreenFragment() {
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.app_home_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_screen, container, false);
        //   textView = (TextView)rootView.findViewById(R.id.txtLabel);
        //  imageView = (ImageView) rootView.findViewById(R.id.frag_icon);

        setDateToView();


        return rootView;
    }

    private void setDateToView() {
        Bundle bundle = getArguments();
//        textView.setText(bundle.getString("data"));
//        imageView.setImageDrawable(VectorDrawable.create(getActivity().getApplicationContext(), bundle.getInt("resId")));
    }
}
