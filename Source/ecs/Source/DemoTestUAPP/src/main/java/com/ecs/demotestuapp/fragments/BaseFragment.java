package com.ecs.demotestuapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ecs.demotestuapp.activity.ResultActivity;
import com.ecs.demotestuapp.jsonmodel.SubgroupItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.cdp.di.ecs.error.ECSError;

public class BaseFragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void gotoResultActivity(String result){

        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putExtra("result",result);
        startActivity(intent);
        getActivity().finish();
    }

    public  String getFailureString(Exception exception, ECSError ecsError){
        return "Error \n"+ exception.getLocalizedMessage() + "\nError type \n" + ecsError.getErrorType() + "\nError code \n" + ecsError.getErrorcode();
    }

    String getJsonStringFromObject(Object object){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return "Success\n" + gson.toJson(object);
    }


    private void inflateLayout(LinearLayout linearLayout, SubgroupItem subgroupItem) {

        int noOfEditText =  subgroupItem.getEditText();
        int noOFSpinner = subgroupItem.getSpinner();
        int noButton = subgroupItem.getButton();

        for (int i=0;i<noOfEditText;i++){

            EditText myEditText = new EditText(getActivity());
            myEditText.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(myEditText);
        }

        for (int i=0;i<noOFSpinner;i++){

            Spinner spinner = new Spinner(getActivity());
            spinner.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(spinner);
        }


        for (int i=0;i<noButton;i++){

            Button button = new Button(getActivity());
            button.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(button);
        }

    }
}
