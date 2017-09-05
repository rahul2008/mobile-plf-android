package com.philips.platform.appframework.connectivitypowersleep.insights;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by philips on 9/5/17.
 */

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class InsightsAdapterTest {

    private InsightsAdapter insightsAdapter;
    private Context context;
    private InsightsAdapter.InsightsInfoViewHolder holder;
    private View listItemView;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        initializeTipsList();
        insightsAdapter = new InsightsAdapter(context, insightsTitleItemList, insightsDescItemList);
        insightsAdapter.onAttachedToRecyclerView(new RecyclerView(context));
        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //We have a layout especially for the items in our recycler view. We will see it in a moment.
        listItemView = inflater.inflate(R.layout.fragment_insights_item, null, false);
        holder = insightsAdapter.new InsightsInfoViewHolder(listItemView);
        insightsAdapter.onBindViewHolder(holder, 0);
    }

    @Test
    public void testListItemDataAreValid() {
        assertEquals(holder.tvTitle.getText().toString(), context.getString(R.string.sleep_tip_title_1));
        assertEquals(holder.tvDetail.getText().toString(), context.getString(R.string.sleep_tip_desc_1));
    }

    private ArrayList<String> insightsTitleItemList;
    private ArrayList<String> insightsDescItemList;

    public void initializeTipsList() {

        insightsTitleItemList = new ArrayList<String>();
        insightsTitleItemList.add(context.getString(R.string.sleep_tip_title_1));
        insightsTitleItemList.add(context.getString(R.string.sleep_tip_title_2));
        insightsTitleItemList.add(context.getString(R.string.sleep_tip_title_3));

        insightsDescItemList = new ArrayList<String>();
        insightsDescItemList.add(context.getString(R.string.sleep_tip_desc_1));
        insightsDescItemList.add(context.getString(R.string.sleep_tip_desc_2));
        insightsDescItemList.add(context.getString(R.string.sleep_tip_desc_3));


    }
}
