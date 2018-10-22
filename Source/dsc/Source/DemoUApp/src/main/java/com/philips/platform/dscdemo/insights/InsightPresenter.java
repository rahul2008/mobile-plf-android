package com.philips.platform.dscdemo.insights;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.R;
import com.philips.platform.uid.view.widget.Label;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Collections;


public class InsightPresenter {

    private Context mContext;

    private final DBRequestListener<Insight> dbRequestListener;
    private DataServicesManager mDataServices;

    private Label insightId;
    private Label lastModified;
    private Label expirationDate;
    private Label momentId;
    private EditText mEtRuleId;
    private Button mSaveInsightButton;

    InsightPresenter(Context context, DBRequestListener<Insight> dbRequestListener) {
        mDataServices = DataServicesManager.getInstance();
        mContext = context;
        this.dbRequestListener = dbRequestListener;
    }

    void createOrUpdate(final Insight insight) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.insight_create);

        dialog.setTitle(mContext.getResources().getString(R.string.create_insight));

        insightId = dialog.findViewById(R.id.label_insight_id_value);
        lastModified = dialog.findViewById(R.id.label_insight_last_modified_value);
        expirationDate = dialog.findViewById(R.id.label_insight_expiration_date_value);
        momentId = dialog.findViewById(R.id.label_insight_moment_id_value);
        mEtRuleId = dialog.findViewById(R.id.et_insight_rule_value);
        mSaveInsightButton = dialog.findViewById(R.id.btn_save_insight);

        insightId.setText(insight.getSynchronisationData().getGuid());
        final DateTime lastModified = insight.getSynchronisationData().getLastModified();
        this.lastModified.setText(lastModified != null ? lastModified.toString(ISODateTimeFormat.dateTime()) : "");
        this.momentId.setText(insight.getMomentId());
        final DateTime expirationDate = insight.getExpirationDate();
        this.expirationDate.setText(expirationDate != null ? expirationDate.toString(ISODateTimeFormat.dateTime()) : "never expires");

        mSaveInsightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.dismiss();
                insight.setRuleId(mEtRuleId.getText().toString());

                mDataServices.saveInsights(Collections.singletonList(insight), dbRequestListener);
            }
        });

        dialog.show();
    }

}
