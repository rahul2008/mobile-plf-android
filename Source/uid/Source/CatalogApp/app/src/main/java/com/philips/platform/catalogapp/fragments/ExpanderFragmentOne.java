package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.platform.catalogapp.R;
import com.philips.platform.uid.view.widget.Expander;
import com.philips.platform.uid.view.widget.Label;

public class ExpanderFragmentOne extends  BaseFragment{
    Expander expander;

    @Override
    public int getPageTitle() {
        return R.string.page_title_expander;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_expander_one, container, false);
        expander = (Expander)view.findViewById(R.id.catalog_expander);
        expander=(Expander)view.getChildAt(0);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // expander.setDefaultExpander(getContext());
        expander.setTitle("My Expander");
      //  expander.setTitleImage(getActivity().getResources().getDrawable(R.drawable.alert_dialog_icon));
       // RelativeLayout relativeLayout = inflater.inflate(R.layout.fragment_expander_content_default_layout, null, false);



      /*  ViewStub viee = expander.findViewById(R.id.uid_expander_view_content);
        viee.setLayoutResource(R.layout.fragment_expander_content_default_layout);
        RelativeLayout rl= (RelativeLayout) viee.inflate();*/

       //View contentView = getLayoutInflater().inflate(R.layout.fragment_expander_content_default_layout, relativeLayout, false);
       expander.setContentLayout(R.layout.fragment_expander_content_default_layout, getActivity());


      /*  LayoutInflater layoutInflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rl.addView(1, layoutInflater.inflate(R.layout.fragment_expander_content_default_layout, this, false) );*/
    }
}
