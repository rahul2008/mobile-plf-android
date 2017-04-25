package com.philips.cdp.digitalcare.faq.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.CommonRecyclerViewAdapter;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.Utils;
import com.philips.cdp.prxclient.datamodels.support.Chapter;
import com.philips.cdp.prxclient.datamodels.support.Data;
import com.philips.cdp.prxclient.datamodels.support.Item;
import com.philips.cdp.prxclient.datamodels.support.RichText;
import com.philips.cdp.prxclient.datamodels.support.RichTexts;
import com.philips.cdp.prxclient.datamodels.support.SupportModel;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by arbin on 24/04/2017.
 */

public class FaqListFragment extends DigitalCareBaseFragment {

    private static final String TAG = FaqListFragment.class.getSimpleName();
    private CommonRecyclerViewAdapter mFaqListAdapter;
    private LinkedHashMap<String, CommonRecyclerViewAdapter> mGroupAdapters;
    private RecyclerView mFaqList = null;
    private LinkedHashMap<String, List<Item>> mSupportData = null;
    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DigiCareLogger.i(TAG, "Launching FAQ Screen");
        if (view == null) {
            view = inflater.inflate(R.layout.consumercare_fragment_faq_list, container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mFaqList != null) {
            return;
        }

        final FaqListFragment fragment = this;
        mGroupAdapters = new LinkedHashMap<>();

        mFaqList = (RecyclerView) getActivity().findViewById(R.id.faq_list_recycle_view);
        mFaqList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFaqListAdapter = new CommonRecyclerViewAdapter<String>(getQuestionGroups(), R.layout.consumercare_fragment_faq_group) {
            @Override
            public void bindData(RecyclerView.ViewHolder holder, String groupName) {
                Label title = (Label) holder.itemView.findViewById(R.id.faq_list_item_header_title);
                TextView showMoreOrLess = (TextView) holder.itemView.findViewById(R.id.faq_list_item_header_show_more_less);
                RecyclerView questingList = (RecyclerView) holder.itemView.findViewById(R.id.faq_list_item_recycle_view);

                title.setText(groupName + "(" + getQuestionGroupSize(groupName) + ")");
                makeTextViewHyperlink(showMoreOrLess);
                showMoreOrLess.setTag(groupName);
                showMoreOrLess.setOnClickListener(fragment);

                questingList.setLayoutManager(new LinearLayoutManager(getContext()));
                questingList.addItemDecoration(new RecyclerViewSeparatorItemDecoration(getContext()));
                CommonRecyclerViewAdapter questingListAdapter = new CommonRecyclerViewAdapter<Item>(getQuestionItems(groupName, false), R.layout.consumercare_icon_right_button) {
                    @Override
                    public void bindData(RecyclerView.ViewHolder holder, Item question) {
                        Label title = (Label) holder.itemView.findViewById(R.id.icon_button_text);
                        ImageView readMore = (ImageView) holder.itemView.findViewById(R.id.icon_button_icon);
                        readMore.setImageResource(R.drawable.consumercare_viewproduct_videorightarrow);
                        title.setText(question.getHead());
                        holder.itemView.setOnClickListener(fragment);
                        holder.itemView.setTag(question.getAsset());
                    }
                };
                questingList.setAdapter(questingListAdapter);
                mGroupAdapters.put(groupName, questingListAdapter);
            }
        };
        mFaqList.setAdapter(mFaqListAdapter);
    }

    private void updateGroup(String groupName, boolean showAll) {
        mGroupAdapters.get(groupName).swap(getQuestionItems(groupName, showAll));
    }

    private List<Item> getQuestionItems(String groupName, boolean showAll) {
        List<Item> questions = mSupportData.get(groupName);
        if (showAll) {
            return questions;
        } else {
            //show less
            if (questions.size() > 5) {
                //top 3
                return questions.subList(0, 3);
            } else {
                return questions;
            }
        }
    }

    private ArrayList<String> getQuestionGroups() {
        return new ArrayList<>(mSupportData.keySet());
    }

    private int getQuestionGroupSize(String groupName) {
        return mSupportData.get(groupName).size();
    }

    private void makeTextViewHyperlink(TextView tv) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(tv.getText());
        ssb.setSpan(new URLSpan("#"), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.faq_list_item_header_show_more_less) {
            TextView showAllOrLess = (TextView) view;
            String groupName = (String) view.getTag();
            if (showAllOrLess.getText().toString().equals("Show all")) {
                showAllOrLess.setText("Show less");
                updateGroup(groupName, true);
            } else {
                showAllOrLess.setText("Show all");
                updateGroup(groupName, false);
            }
            makeTextViewHyperlink(showAllOrLess);
        } else if (id == R.id.icon_button) {
            String assetUrl = (String) view.getTag();
            FaqDetailedScreen faqDetailedScreen = new FaqDetailedScreen();
            faqDetailedScreen.setFaqWebUrl(assetUrl);
            showFragment(faqDetailedScreen);
        }
    }

    @Override
    public void setViewParams(Configuration config) {
    }

    @Override
    public String getActionbarTitle() {
        if (Utils.isTablet(getActivity()))
            return getResources().getString(R.string.FAQ_KEY_TABLET);
        else
            return getResources().getString(R.string.FAQ_KEY);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_FAQ;
    }

    public void setSupportModel(SupportModel supportModel) {
        mSupportData = new LinkedHashMap();
        if (supportModel == null) {
            DigiCareLogger.d(TAG, "Support Model is null");
            return;
        }

        Data supportData = supportModel.getData();
        RichTexts richTexts = supportData.getRichTexts();
        List<RichText> richText = richTexts.getRichText();
        if (richText != null && richText.size() == 0) {
            new AlertDialog.Builder(getActivity(), R.style.alertDialogStyle)
                    .setTitle(null)
                    .setMessage(getActivity().getResources().getString(R.string.NO_SUPPORT_KEY))
                    .setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    getActivity().onBackPressed();
                                }
                            }).show();
            DigiCareLogger.d(TAG, "No Support Data");
            return;
        }

        for (RichText faq : richText) {
            List<Item> engFaqItems = new ArrayList<>();
            List<Item> nonEngItems = new ArrayList<>();
            String supportType = faq.getType();
            if (supportType.equalsIgnoreCase("FAQ") || supportType.equalsIgnoreCase("TUT") || supportType.equalsIgnoreCase("FEF")) {
                Chapter chapter = faq.getChapter();
                String questionCategory = chapter.getName();
                if (questionCategory != null) {
                    List<Item> questionsList = faq.getItem();
                    for (Item item : questionsList) {
                        String langCode = item.getLang();
                        if (langCode.equalsIgnoreCase("AEN") || langCode.equalsIgnoreCase("ENG"))
                            engFaqItems.add(item);
                        else
                            nonEngItems.add(item);
                    }
                }

                if (nonEngItems.size() != 0) {
                    mSupportData.put(questionCategory, nonEngItems);
                } else {
                    mSupportData.put(questionCategory, engFaqItems);
                }
            }
        }
    }
}
