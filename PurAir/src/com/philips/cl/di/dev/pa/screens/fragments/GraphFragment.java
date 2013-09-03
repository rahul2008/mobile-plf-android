package com.philips.cl.di.dev.pa.screens.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.screens.customviews.CustomGraphView;
import com.philips.cl.di.dev.pa.screens.customviews.CustomGraphView.GraphViewData;

public class GraphFragment extends Fragment {
	private List<GraphViewData> listGraphViewDataIndoor;
	private List<GraphViewData> listGraphViewDataOutdoor;
	private CustomGraphView graph;
	private View vMain;
	private Bundle bundle ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		bundle = getArguments();
		listGraphViewDataIndoor= bundle.getParcelableArrayList(AppConstants.INDOOR);
		listGraphViewDataOutdoor = bundle.getParcelableArrayList(AppConstants.OUTDOOR);
		
		vMain = inflater.inflate(R.layout.activity_graph, container, false);
		graph = (CustomGraphView) vMain.findViewById(R.id.graph);

		/*listGraphViewDataIndoor = new ArrayList<GraphViewData>();
		listGraphViewDataOutdoor = new ArrayList<GraphViewData>();*/
/*
		for (int i = 0; i < 24; i++) {
			listGraphViewDataIndoor.add(new GraphViewData(i,
					Math.random() * 500));
			listGraphViewDataOutdoor.add(new GraphViewData(i,
					Math.random() * 500));
		}
*/
		graph.setGraphData(listGraphViewDataIndoor, listGraphViewDataOutdoor);
		return vMain;
	}

}
