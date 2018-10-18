package com.philips.platform.dscdemo.insights;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DemoAppManager;
import com.philips.platform.dscdemo.R;
import com.philips.platform.dscdemo.database.DatabaseHelper;
import com.philips.platform.dscdemo.database.table.OrmSynchronisationData;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Collections;


public class InsightPresenter {

    private Context mContext;

    private final DBRequestListener<Insight> dbRequestListener;
    private DataServicesManager mDataServices;
//    private DatabaseHelper databaseHelper;

    private EditText mEtInsightType;
    private EditText mEtRuleId;
    private Button mSaveInsightButton;

    InsightPresenter(Context context, DBRequestListener<Insight> dbRequestListener) {
        mDataServices = DataServicesManager.getInstance();
        //databaseHelper = DemoAppManager.getInstance().getDatabaseHelper();®®
        mContext = context;
        this.dbRequestListener = dbRequestListener;
    }

    void createInsight() {
        final Dialog dialog = new Dialog(mContext);

        dialog.setContentView(R.layout.insight_create);

        dialog.setTitle(mContext.getResources().getString(R.string.create_insight));

        mEtInsightType = dialog.findViewById(R.id.et_insight_type);
        mEtRuleId = dialog.findViewById(R.id.et_insight_rule);
        mSaveInsightButton = dialog.findViewById(R.id.btn_save_insight);

        // after update

//        if (addOrUpdate == UPDATE) {®
//            final MomentHelper helper = new MomentHelper();
//            mTemperature.setText(String.valueOf(helper.getTemperature(moment)));
//            mLocation.setText(helper.getNotes(moment));
//            mPhase.setText(helper.getTime(moment));
//            mSaveInsightButton.setText(R.string.update);
//        }

        mSaveInsightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.dismiss();
                Insight insight = mDataServices.createInsight(mEtInsightType.getText().toString());
                insight.setRuleId(mEtRuleId.getText().toString());
                insight.setSynchronisationData(new OrmSynchronisationData(insight.getGUId(), false, null, 0));

                mDataServices.saveInsights(Collections.singletonList(insight), dbRequestListener);
            }
        });

//        textChageListener(mPhase);
//        textChageListener(mTemperature);
//        textChageListener(mLocation);

        dialog.show();
    }

}
