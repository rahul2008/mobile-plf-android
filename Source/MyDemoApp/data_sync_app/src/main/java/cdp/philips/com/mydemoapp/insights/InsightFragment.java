package cdp.philips.com.mydemoapp.insights;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.R;

public class InsightFragment extends Fragment implements DBRequestListener<Insight>,DBFetchRequestListner<Insight>,DBChangeListener {
    InsightAdapter mInsightAdapter;
    List<InsightDisplayModel> mInsightDisplayModelList;
    RecyclerView mInsightsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insight_layout, container, false);

        mInsightDisplayModelList = new ArrayList<>();

        //For testing UI
        for (int i = 0; i < 10; i++) {
            InsightDisplayModel insightDisplayModel = new InsightDisplayModel();
            insightDisplayModel.setLastModified("lastModified " + i);
            insightDisplayModel.setTimeStamp("timeStamp " + i);
            insightDisplayModel.setRuleID("ruleID " + i);
            insightDisplayModel.setMomentType("momentType " + i);
            mInsightDisplayModelList.add(insightDisplayModel);
        }

        mInsightAdapter = new InsightAdapter(mInsightDisplayModelList);
        mInsightsRecyclerView = (RecyclerView) view.findViewById(R.id.insight_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mInsightsRecyclerView.setLayoutManager(layoutManager);
        mInsightsRecyclerView.setAdapter(mInsightAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void dBChangeSuccess(SyncType type) {

    }

    @Override
    public void dBChangeFailed(Exception e) {

    }

    @Override
    public void onFetchSuccess(List<? extends Insight> data) {
        mInsightAdapter.notifyDataSetChanged();
        mInsightsRecyclerView.setAdapter(mInsightAdapter);
    }

    @Override
    public void onFetchFailure(Exception exception) {

    }

    @Override
    public void onSuccess(List<? extends Insight> data) {

    }

    @Override
    public void onFailure(Exception exception) {

    }
}


