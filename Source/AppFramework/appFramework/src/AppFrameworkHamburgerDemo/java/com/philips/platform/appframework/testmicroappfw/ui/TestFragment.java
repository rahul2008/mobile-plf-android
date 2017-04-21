package com.philips.platform.appframework.testmicroappfw.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.testmicroappfw.data.TestConfigManager;
import com.philips.platform.appframework.testmicroappfw.models.Chapter;
import com.philips.platform.baseapp.base.AppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkBaseFragment;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class TestFragment extends AppFrameworkBaseFragment implements TestContract.View{

    public static final String TAG=TestFragment.class.getSimpleName();

    private RecyclerView chapterRecyclerView;


    private TestPresenter testPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        Bundle bundle=new Bundle();
        bundle.putSerializable(COCOListFragment.SELECTED_CHAPTER,chapter);
        COCOListFragment cocoListFragment=new COCOListFragment();
        cocoListFragment.setArguments(bundle);
        ((AppFrameworkBaseActivity)getActivity()).addFragment(cocoListFragment,"CoCoListFragment");
    }

}
