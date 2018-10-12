package com.philips.platform.catalogapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.uid.view.widget.Expander;

public class ExpanderFragmentOne extends  BaseFragment{
    Expander expanderOne;
    Expander expanderTwo;
    Expander expanderThree;
    @Override
    public int getPageTitle() {
        return R.string.page_title_expander;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_expander_one, container, false);
        expanderOne = (Expander)view.findViewById(R.id.catalog_expander_one);
        expanderOne =(Expander)view.getChildAt(0);

        expanderTwo = (Expander)view.findViewById(R.id.catalog_expander_two);
        expanderTwo =(Expander)view.getChildAt(1);


        expanderThree = (Expander)view.findViewById(R.id.catalog_expander_three);
        expanderThree =(Expander)view.getChildAt(2);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        expanderOne.setTitleText("Short title");
        expanderOne.setTitleIcon(getActivity().getResources().getString(R.string.dls_calendar));
        expanderOne.setContentLayout(R.layout.fragment_expander_content_default_layout);
      //  expanderOne.setTitleImage(getActivity().getResources().getDrawable(R.drawable.alert_dialog_icon));
       // RelativeLayout relativeLayout = inflater.inflate(R.layout.fragment_expander_content_default_layout, null, false);



      /*  ViewStub viee = expanderOne.findViewById(R.id.uid_expander_view_content);
        viee.setLayoutResource(R.layout.fragment_expander_content_default_layout);
        RelativeLayout rl= (RelativeLayout) viee.inflate();*/

       //View contentView = getLayoutInflater().inflate(R.layout.fragment_expander_content_default_layout, relativeLayout, false);
        expanderTwo.setTitleText("Long title: Life has got all those twists and turns. You've got to hold on tight and off you go. With the right kind of coaching and determination you can accomplish anything. ");
        expanderTwo.setTitleIcon(getActivity().getResources().getString(R.string.dls_circleplay));
        expanderTwo.setContentLayout(R.layout.fragment_expander_content_default_layout);


      /*  LayoutInflater layoutInflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rl.addView(1, layoutInflater.inflate(R.layout.fragment_expander_content_default_layout, this, false) );*/
        expanderThree.setTitleText("title with empty content");
    }
}
