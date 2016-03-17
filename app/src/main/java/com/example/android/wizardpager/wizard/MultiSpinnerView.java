package com.example.android.wizardpager.wizard;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.internal.widget.TintContextWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.Interfaces.JsonApi;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by salman on 10/03/16.
 */
public class MultiSpinnerView extends LinearLayout {

    LinearLayout linearLayout;
    MaterialEditText materialEditText;
    ListView listView;
    Button button;
    ArrayList<String> dataList, tempDataList;
    ArrayAdapter<String> adapter;
    String stepName;



    private final TextWatcher testWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {


            String text = editable.toString();
            if(text.isEmpty())
                return;

            ArrayList<String> finalData  = new ArrayList<String>();
            for (String data : dataList){
                if(data.contains(text)){
                    finalData.add(data);
                }
            }
            if(text.isEmpty()){
                finalData = (ArrayList<String>)dataList.clone();
            }
            if(finalData.size() == 0){
                button.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                button.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            adapter.clear();
            adapter.addAll(finalData);
            adapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(listView);

        }
    };



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

    private void addData(View mView, String mStepName, String text){

        JsonApi api = null;
        if(mView.getContext() instanceof JsonApi) {
            api = (JsonApi) mView.getContext();
        } else if(mView.getContext() instanceof TintContextWrapper) {
            TintContextWrapper tintContextWrapper = (TintContextWrapper) mView.getContext();
            api = (JsonApi) tintContextWrapper.getBaseContext();
        } else {
            throw new RuntimeException("Could not fetch context");
        }

        String key = (String) mView.getTag(R.id.key);
        try {
            api.writeValue(mStepName, key, text);
        } catch (JSONException e) {
            // TODO- handle
            e.printStackTrace();
        }

    }

    private void addDatainEditText(MaterialEditText editText, JSONObject jsonObject, String stepName) throws Exception {
        editText.setHint(jsonObject.getString("hint"));
        editText.setFloatingLabelText(jsonObject.getString("hint"));
        editText.setId(ViewUtil.generateViewId());
        editText.setTag(R.id.key, jsonObject.getString("key"));
        editText.setTag(R.id.type, jsonObject.getString("type"));
        editText.addTextChangedListener(testWatcher);

    }

    public MultiSpinnerView(Context context, Bundle bundle ) throws Exception {
        super(context);
        linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_multi_spinner, null);
        materialEditText = (MaterialEditText) linearLayout.findViewById(R.id.editText);
        listView = (ListView) linearLayout.findViewById(R.id.list);
        button = (Button) linearLayout.findViewById(R.id.addInList);



        String jsonApi = bundle.getString("json");
        JSONObject jsonObj = new JSONObject(jsonApi);
        stepName = bundle.getString("stepName");
        dataList = bundle.getStringArrayList("list");

        tempDataList = (ArrayList<String>) dataList.clone();


        adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, android.R.id.text1, tempDataList);

        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
        addDatainEditText(materialEditText, jsonObj, stepName);



        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(materialEditText, stepName, materialEditText.getText().toString());
                dataList.add(materialEditText.getText().toString());
                adapter.clear();
                adapter.addAll(( ArrayList<String> )dataList.clone());

                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(listView);

                listView.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                materialEditText.setText("");

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                addData(materialEditText, stepName, item);
                materialEditText.setText(item);
            }
        });



        this.addView(linearLayout);
    }
}
