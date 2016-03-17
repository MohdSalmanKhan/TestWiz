package com.example.android.wizardpager.wizard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.Interfaces.*;
import com.example.android.wizardpager.wizard.Interfaces.CommonListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salman on 10/03/16.
 */
public class MultiSpinnerFactory implements FormWidgetFactory {

    private ArrayAdapter<String> adapter;
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


    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JSONObject jsonObject, CommonListener listener) throws Exception {
        List<View> views = new ArrayList<>();

        dataList = new ArrayList<String>();
        dataList.add("apple");
        dataList.add("banana");
        dataList.add("appy");

        tempDataList = (ArrayList<String>) dataList.clone();
        listView = (ListView) LayoutInflater.from(context).inflate(
                R.layout.layout_chip, null);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, tempDataList);

        listView.setAdapter(adapter);
        listView.setTag(R.id.list, tempDataList);
        listView.setTag(R.id.key, jsonObject.getString("key"));
        listView.setTag(R.id.type, jsonObject.getString("type"));

     //   setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(listener);

        views.add(listView);

        return views;
    }
}
