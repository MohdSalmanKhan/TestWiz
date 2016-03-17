package com.example.android.wizardpager.wizard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;
import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.Interfaces.*;
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by salman on 15/03/16.
 */
public class ChipTextView extends LinearLayout {

    private Bundle bundle;
    private LinearLayout linearLayout;
    private ArrayAdapter<String> adapter;
    private String stepName;
    private ArrayList<String> dataList, tempDataList;
    ListView listView;


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }



    public ChipTextView(final Context context, Bundle bundle, com.example.android.wizardpager.wizard.Interfaces.CommonListener listener) throws Exception{
        super(context);
        this.bundle = bundle;

        listView = (ListView) LayoutInflater.from(context).inflate(R.layout.layout_chip, null);

        String jsonApi = bundle.getString("json");
        final JSONObject jsonObj = new JSONObject(jsonApi);
       // stepName = bundle.getString("stepName");
        dataList = bundle.getStringArrayList("list");
        tempDataList = (ArrayList<String>) dataList.clone();

        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, tempDataList);

        listView.setAdapter(adapter);

        setListViewHeightBasedOnChildren(listView);

//

        listView.setOnItemClickListener(listener);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent = new Intent(context, MutiSpinnerActivity.class);
//                Bundle bundle1 = new Bundle();
//                bundle1.putStringArrayList("list", tempDataList);
//                bundle1.putString("json", jsonObj.toString());
//                bundle1.putString("stepName", stepName);
//                intent.putExtras(bundle1);
//                ((WizardActivity) context).startActivityForResult(intent, Utils.CALL_ACTIVITY);
//            }
//        });

        this.addView(linearLayout);
    }
}
