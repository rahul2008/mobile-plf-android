package cdp.philips.com.mydemoapp.blob;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.blob.BlobMetaData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.consents.ConsentDialogPresenter;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class BlobMetaDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private ArrayList<? extends BlobMetaData> blobMetaDatas;
    private final BlobPresenter blobPresenter;

    public BlobMetaDataAdapter(final Context context, ArrayList<? extends BlobMetaData> blobMetaDatas, BlobPresenter blobPresenter) {
        mContext = context;
        this.blobMetaDatas = blobMetaDatas;
        this.blobPresenter = blobPresenter;
    }

    @Override
    public ConsentDetailViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blob_downlod_item, parent, false);
        return new ConsentDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ConsentDetailViewHolder) {
            ConsentDetailViewHolder mConsentViewHolder = (ConsentDetailViewHolder) holder;
            mConsentViewHolder.tv_blob_id.setText("BLOB ID :" +blobMetaDatas.get(position).getBlobID());

            mConsentViewHolder.btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blobPresenter.download(blobMetaDatas.get(position).getBlobID());
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return blobMetaDatas.size();
    }


    public class ConsentDetailViewHolder extends RecyclerView.ViewHolder {

        TextView tv_blob_id;
        Button btn_download;

        public ConsentDetailViewHolder(final View itemView) {
            super(itemView);
            tv_blob_id = (TextView) itemView.findViewById(R.id.tv_blob_id);
            btn_download=(Button) itemView.findViewById(R.id.btn_downlod);
        }
    }


    public void setData(ArrayList<? extends BlobMetaData> blobMetaDatas) {
        this.blobMetaDatas = blobMetaDatas;
    }

}
