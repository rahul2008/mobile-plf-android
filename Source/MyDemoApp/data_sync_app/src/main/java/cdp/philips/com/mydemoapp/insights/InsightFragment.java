package cdp.philips.com.mydemoapp.insights;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;

public class InsightFragment extends DialogFragment implements DBRequestListener<Insight>, DBFetchRequestListner<Insight>, DBChangeListener {
    InsightAdapter mInsightAdapter;
    List<InsightDisplayModel> mInsightDisplayModelList = new ArrayList<>();
    RecyclerView mInsightsRecyclerView;
    ArrayList<? extends Insight> mInsightlist = new ArrayList();
    DataServicesManager mDataServicesManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insight_layout, container, false);

        mInsightDisplayModelList = new ArrayList<>();
        mDataServicesManager = DataServicesManager.getInstance();

        mInsightAdapter = new InsightAdapter(mInsightDisplayModelList);
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
        DataServicesManager.getInstance().fetchInsights(this);
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
                DSLog.i(DSLog.LOG, "http TEmperature TimeLine : UI updated");
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
        if (getActivity()!=null && insights != null ) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mInsightlist = (ArrayList<? extends Insight>) insights;
                    mInsightAdapter.setInsightList(mInsightlist);
                    mInsightAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}


