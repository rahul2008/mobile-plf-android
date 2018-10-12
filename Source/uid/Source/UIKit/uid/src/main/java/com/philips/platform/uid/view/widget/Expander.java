package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.UIDHelper;

public class Expander extends LinearLayout implements View.OnClickListener {
    private RelativeLayout ExpanderViewTitle;
    private Label ExpanderViewTitleDefaultLabel;


    private Label ExpanderViewTitleDefaultIcon;
    private RelativeLayout ExpanderViewContent;
    private Label chevronLabel;
    private Context mContext;
    private View titleBottomDivider;
    private View contentBottomDivider;





    public Expander(Context context, @Nullable AttributeSet attrs) {
        super(context);
        mContext=context;
        initializeViews(context, attrs);
    }

    public Expander( @NonNull final Context context){
        this(context,null);

    }
    private void initializeViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context).cloneInContext(UIDHelper.getPopupThemedContext(context));
        ViewGroup expanderLayout = (ViewGroup) inflater.inflate(R.layout.uid_expander, this);
        ExpanderViewTitle = (RelativeLayout) expanderLayout.findViewById(R.id.uid_expander_view_title);
        ExpanderViewTitle.setOnClickListener(this);
        ExpanderViewContent =(RelativeLayout)expanderLayout.findViewById(R.id.uid_expander_view_content);

        titleBottomDivider = (View) expanderLayout.findViewById(R.id.uid_expander_title_bottom_divider);
        contentBottomDivider = (View) expanderLayout.findViewById(R.id.uid_expander_content_bottom_divider);

        setTitleLayout(R.layout.uid_expander_title_layout_default);  // set default title
        ExpanderViewTitleDefaultLabel = (Label) expanderLayout.findViewById(R.id.uid_expander_title_text);
        ExpanderViewTitleDefaultIcon = (Label) expanderLayout.findViewById(R.id.uid_expander_title_image);
        chevronLabel = (Label)  expanderLayout.findViewById(R.id.uid_expander_title_chevron);

    }



     // title layout
    public void setTitleLayout(int resource){
        setLayout(resource,ExpanderViewTitle);

    }

    // content layout
     public void setContentLayout(int resource){
         setLayout(resource,ExpanderViewContent);

    }

    private void setLayout(int resource, RelativeLayout layout){
        LayoutInflater layoutInflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = layoutInflater.inflate(resource, layout, false);
        layout.addView(contentView);

    }

    public void setTitleText(String title) {
        if(null!=ExpanderViewTitleDefaultLabel) {
            ExpanderViewTitleDefaultLabel.setText(title);
        }
    }

    public void setTitleIcon(String fontIcon) {
        ExpanderViewTitleDefaultIcon.setText(fontIcon);
        ExpanderViewTitleDefaultIcon.setVisibility(VISIBLE);
    }




    boolean isExpanded(){
        return true;
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.uid_expander_view_title) {
            expandOrCollapse();

        }
    }

    private void expandOrCollapse(){
        if(ExpanderViewContent.isShown()){
           hideContentView();
        }else{
            if(null!=ExpanderViewContent && null!=ExpanderViewContent.getChildAt(0)) {
                showContentView();
            }
        }
    }

    private void showContentView(){
        titleBottomDivider.setVisibility(GONE);
        ExpanderViewContent.setVisibility(View.VISIBLE);
        contentBottomDivider.setVisibility(View.VISIBLE);
        chevronLabel.setText(mContext.getResources().getString(R.string.dls_navigationup));
    }

    private void hideContentView(){
        ExpanderViewContent.setVisibility(View.GONE);
        contentBottomDivider.setVisibility(View.GONE);
        titleBottomDivider.setVisibility(VISIBLE);
        chevronLabel.setText(mContext.getResources().getString(R.string.dls_navigationdown));
    }
}
