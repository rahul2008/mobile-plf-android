/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.data.TestConfigManager;
import com.philips.platform.appframework.models.Chapter;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;
import com.philips.platform.baseapp.screens.cocoversion.CocoVersionFragment;
import com.philips.platform.baseapp.screens.utility.RALog;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class TestFragment extends AbstractAppFrameworkBaseFragment implements TestContract.View{

    public static final String TAG=TestFragment.class.getSimpleName();

    private RecyclerView chapterRecyclerView;


    private TestPresenter testPresenter;

    private final String APP_INFO_CHAPTER = "App Info";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RALog.d(TAG, " OncreateView called ");
        View view=inflater.inflate(R.layout.test_fragment,container,false);

        chapterRecyclerView=(RecyclerView) view.findViewById(R.id.chapter_recyclerview);
        chapterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        testPresenter=new TestPresenter(this, TestConfigManager.getInstance(),getActivity().getApplicationContext());
        testPresenter.loadChapterList();

    }

    @Override
    public String getActionbarTitle() {
        return null;
    }

    @Override
    public void displayChapterList(ArrayList<Chapter> chaptersList) {
        RALog.d(TAG, " Display Chapter List");
        ChapterAdapter chapterAdapter=new ChapterAdapter(getActivity(), chaptersList, new ChapterAdapter.ChapterListCallback() {
            @Override
            public void onChapterItemClicked(Chapter chapter) {
                showCoCoList(chapter);
            }
        });
        chapterRecyclerView.setAdapter(chapterAdapter);
        chapterAdapter.notifyDataSetChanged();
    }


    @Override
    public void showCoCoList(Chapter chapter) {
        AbstractAppFrameworkBaseFragment baseFragment;

        switch (chapter.getChapterName()) {
            case APP_INFO_CHAPTER:
                baseFragment = new CocoVersionFragment();
                break;
            default:
                Bundle bundle=new Bundle();
                bundle.putSerializable(COCOListFragment.SELECTED_CHAPTER,chapter);
                baseFragment = new COCOListFragment();
                baseFragment.setArguments(bundle);
                break;
        }
        ((AbstractAppFrameworkBaseActivity)getActivity()).addFragment(baseFragment, baseFragment.getClass().getSimpleName());
    }

}
