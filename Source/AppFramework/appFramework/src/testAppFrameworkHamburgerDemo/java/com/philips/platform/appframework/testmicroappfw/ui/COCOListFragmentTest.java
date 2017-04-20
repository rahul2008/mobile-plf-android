package com.philips.platform.appframework.testmicroappfw.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appframework.testmicroappfw.models.CommonComponent;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 24)
public class COCOListFragmentTest extends TestCase {
    private HamburgerActivity hamburgerActivity = null;
    private COCOListFragment cocoListFragment;
    private CoCoAdapter.ChapterViewHolder viewHolder;


    @Before
    public void setUp() throws Exception{
        super.setUp();
        hamburgerActivity = Robolectric.buildActivity(HamburgerActivity.class).create().start().get();
        cocoListFragment = setUpCoCoListFragmentBundle();
    }

    private COCOListFragment setUpCoCoListFragmentBundle() {
        Bundle bundle=new Bundle();
        bundle.putSerializable(COCOListFragment.SELECTED_CHAPTER,TestFragmentTest.createChapterObject());
        COCOListFragment cocoListFragment=new COCOListFragment();
        cocoListFragment.setArguments(bundle);
        return cocoListFragment;
    }

    @Test
    public void testCOCOListFragment(){
        Fragment cocoListFragment = addCoCoListFragment();
        assertTrue(cocoListFragment instanceof COCOListFragment);
    }

    protected Fragment addCoCoListFragment() {
        hamburgerActivity.getSupportFragmentManager().beginTransaction().add(cocoListFragment,"CoCoListFragment").commit();
        return hamburgerActivity.getSupportFragmentManager().findFragmentByTag("CoCoListFragment");
    }

    @Test
    public void testCoCoAdapterItems(){
        Bundle bundle=new Bundle();
        bundle.putSerializable(COCOListFragment.SELECTED_CHAPTER,TestFragmentTest.createChapterObject());
        COCOListFragmentMock cocoListFragmentMock=new COCOListFragmentMock();
        cocoListFragmentMock.setArguments(bundle);
        hamburgerActivity.getSupportFragmentManager().beginTransaction().add(cocoListFragmentMock,"CoCoListFragmentMock").commit();
        Fragment fragment = hamburgerActivity.getSupportFragmentManager().findFragmentByTag("CoCoListFragmentMock");
        RecyclerView recyclerView = (RecyclerView) fragment.getView().findViewById(R.id.coco_recyclerview);
        CoCoAdapter cocoAdapter = (CoCoAdapter) recyclerView.getAdapter();
        assertTrue(cocoAdapter.getItemAtPosition(0) instanceof CommonComponent);

    }


    public static class COCOListFragmentMock extends COCOListFragment{
        private RecyclerView cocoRecyclerView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.coco_list_fragment,container,false);
            cocoRecyclerView= (RecyclerView)view.findViewById(R.id.coco_recyclerview);
            cocoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            return view;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            CommonComponent commonComponent = new CommonComponent();
            commonComponent.setCocoName("Blue Lib");
            ArrayList<CommonComponent> arrayListCommonComponent = new ArrayList<>();
            arrayListCommonComponent.add(commonComponent);
            CoCoAdapter coCoAdapter=new CoCoAdapter(getActivity(),arrayListCommonComponent);
            cocoRecyclerView.setAdapter(coCoAdapter);
            coCoAdapter.notifyDataSetChanged();

        }
    }
}
