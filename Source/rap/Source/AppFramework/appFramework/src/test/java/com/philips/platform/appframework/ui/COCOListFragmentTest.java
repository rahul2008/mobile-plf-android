/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appframework.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appframework.models.CommonComponent;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class COCOListFragmentTest extends TestCase {
    private HamburgerActivity hamburgerActivity = null;
    private COCOListFragmentMock cocoListFragment;
    private ActivityController<TestActivity> activityController;

    @After
    public void tearDown() {
        cocoListFragment = null;
        activityController.pause().stop().destroy();
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        activityController = Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity = activityController.create().start().get();
        cocoListFragment = setUpCoCoListFragmentBundle();
        hamburgerActivity.getSupportFragmentManager().beginTransaction().add(cocoListFragment, "CoCoListFragmentMock").commit();
    }

    private COCOListFragmentMock setUpCoCoListFragmentBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(COCOListFragment.SELECTED_CHAPTER, TestFragmentTest.createChapterObject("Connectivity", "Blue Lib"));
        COCOListFragmentMock cocoListFragment = new COCOListFragmentMock();
        cocoListFragment.setArguments(bundle);
        return cocoListFragment;
    }

    @Test
    public void testCOCOListFragment() {
        Fragment cocoListFragment = hamburgerActivity.getSupportFragmentManager().findFragmentByTag("CoCoListFragmentMock");
        assertTrue(cocoListFragment instanceof COCOListFragmentMock);
    }

    @Test
    public void testCoCoAdapterItems() {
        Fragment cocoListFragment = hamburgerActivity.getSupportFragmentManager().findFragmentByTag("CoCoListFragmentMock");
        RecyclerView recyclerView = (RecyclerView) cocoListFragment.getView().findViewById(R.id.coco_recyclerview);
        assertNotNull(recyclerView);
        CoCoAdapter cocoAdapter = (CoCoAdapter) recyclerView.getAdapter();
        CoCoAdapter.ChapterViewHolder viewHolder = cocoAdapter.onCreateViewHolder(new FrameLayout(RuntimeEnvironment.application), 0);
        cocoAdapter.onBindViewHolder(viewHolder, 0);
        assertTrue(cocoAdapter.getItemCount() > 0);
    }

    @Test
    public void testCoCoListItem() {
        Fragment cocoListFragment = hamburgerActivity.getSupportFragmentManager().findFragmentByTag("CoCoListFragmentMock");
        RecyclerView recyclerView = (RecyclerView) cocoListFragment.getView().findViewById(R.id.coco_recyclerview);
        CoCoAdapter cocoAdapter = (CoCoAdapter) recyclerView.getAdapter();
        CoCoAdapter.ChapterViewHolder viewHolder = cocoAdapter.onCreateViewHolder(new FrameLayout(RuntimeEnvironment.application), 0);
        cocoAdapter.onBindViewHolder(viewHolder, 0);
        assertEquals("Blue Lib", viewHolder.chapterTextView.getText().toString());
    }

    public static class COCOListFragmentMock extends Fragment {
        private RecyclerView cocoRecyclerView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.coco_list_fragment, container, false);
            cocoRecyclerView = (RecyclerView) view.findViewById(R.id.coco_recyclerview);
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
            COCOListPresenter cocoListPresenter = mock(COCOListPresenter.class);
            CoCoAdapter coCoAdapter = new CoCoAdapter(getActivity(), arrayListCommonComponent, cocoListPresenter);
            cocoRecyclerView.setAdapter(coCoAdapter);
            coCoAdapter.notifyDataSetChanged();

        }
    }
}
