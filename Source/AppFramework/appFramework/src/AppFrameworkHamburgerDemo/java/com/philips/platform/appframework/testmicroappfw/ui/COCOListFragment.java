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
import com.philips.platform.appframework.testmicroappfw.models.CommonComponent;
import com.philips.platform.baseapp.base.AppFrameworkBaseFragment;
import com.philips.platform.baseapp.screens.utility.RALog;

import java.util.ArrayList;

/**
 * Created by philips on 13/02/17.
 */

public class COCOListFragment extends AppFrameworkBaseFragment implements COCOListContract.View {


    public static final String SELECTED_CHAPTER = "selected_chapter";

    public static final String TAG=COCOListFragment.class.getSimpleName();
    private RecyclerView cocoRecyclerView;

    private COCOListPresenter cocoListPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.coco_list_fragment,container,false);
        cocoRecyclerView=(RecyclerView)view.findViewById(R.id.coco_recyclerview);
        cocoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        RALog.d(TAG," OnActivityCreated");
        super.onActivityCreated(savedInstanceState);
        cocoListPresenter=new COCOListPresenter(this, TestConfigManager.getInstance(),getActivity());
        Bundle bundle=getArguments();
        Chapter chapter=(Chapter) bundle.getSerializable(SELECTED_CHAPTER);
        cocoListPresenter.loadCoCoList(chapter);

    }

    @Override
    public String getActionbarTitle() {
        return null;
    }

    @Override
    public void displayCoCoList(ArrayList<CommonComponent> commonComponentsList) {
        RALog.d(TAG," Display Coco List ");
        CoCoAdapter coCoAdapter=new CoCoAdapter(getActivity(),commonComponentsList);
        cocoRecyclerView.setAdapter(coCoAdapter);
        coCoAdapter.notifyDataSetChanged();
    }
}
