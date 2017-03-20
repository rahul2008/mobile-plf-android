package cdp.philips.com.mydemoapp.insights;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.TestMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.R;
public class InsightFragment extends DialogFragment implements DBRequestListener<Insight>, DBFetchRequestListner<Insight>, DBChangeListener {
    InsightAdapter mInsightAdapter;
    List<InsightDisplayModel> mInsightDisplayModelList = new ArrayList<>();
    RecyclerView mInsightsRecyclerView;
    ArrayList<? extends Insight> mInsightlist = new ArrayList();
    DataServicesManager mDataServicesManager;
    private TextView mNoInsights;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insight_layout, container, false);

        mInsightDisplayModelList = new ArrayList<>();
        mDataServicesManager = DataServicesManager.getInstance();

        mInsightAdapter = new InsightAdapter(mInsightDisplayModelList);

        mNoInsights = (TextView) view.findViewById(R.id.tv_no_insights);
        mInsightsRecyclerView = (RecyclerView) view.findViewById(R.id.insight_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mInsightsRecyclerView.setLayoutManager(layoutManager);
        mInsightsRecyclerView.setAdapter(mInsightAdapter);

        mDataServicesManager.fetchInsights(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void dBChangeSuccess(SyncType type) {
        if (type == SyncType.INSIGHT) {
            DataServicesManager.getInstance().fetchInsights(this);
            Log.d(this.getClass().getName(), "Insight is changed");
        }
    }

    @Override
    public void dBChangeFailed(Exception e) {

    }

    @Override
    public void onFetchSuccess(final List<? extends Insight> data) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI(data);
            }
        });
    }

    @Override
    public void onFetchFailure(Exception exception) {

    }

    @Override
    public void onSuccess(List<? extends Insight> insights) {
        updateUI(insights);
    }

    @Override
    public void onFailure(Exception exception) {

    }

    private void updateUI(final List<? extends Insight> insights) {
        if (getActivity() != null && insights != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mInsightlist = (ArrayList<? extends Insight>) insights;

                    if(mInsightlist.size() > 0) {
                        mInsightsRecyclerView.setVisibility(View.VISIBLE);
                        mNoInsights.setVisibility(View.GONE);

                        mInsightAdapter.setInsightList(mInsightlist);
                        mInsightAdapter.notifyDataSetChanged();
                    }else{
                        mInsightsRecyclerView.setVisibility(View.GONE);
                        mNoInsights.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        DataServicesManager.getInstance().unRegisterDBChangeListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mDataServicesManager.registerDBChangeListener(this);
    }
}


