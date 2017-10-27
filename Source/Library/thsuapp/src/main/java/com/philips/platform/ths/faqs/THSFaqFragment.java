package com.philips.platform.ths.faqs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class THSFaqFragment extends THSBaseFragment{
    public static final String TAG = THSFaqFragment.class.getSimpleName();

    THSFaqPresenter mThsFaqPresenter;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<FaqBean>> listDataChild;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_faqs_expandable_list_layout, container, false);
        mThsFaqPresenter = new THSFaqPresenter(this);
        mThsFaqPresenter.getFaq();
        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lv_expand);

        return view;
    }

    protected void updateFaqs(HashMap map){
        final Set set = map.keySet();

        listDataHeader = new ArrayList<>(set);
        listDataChild = new HashMap();
        listDataChild = map;

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }
}
