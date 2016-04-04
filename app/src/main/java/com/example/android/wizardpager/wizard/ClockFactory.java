package com.example.android.wizardpager.wizard;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.Interfaces.CommonListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salman on 21/03/16.
 */
public class ClockFactory implements FormWidgetFactory, CompoundButton.OnCheckedChangeListener {


    private ClinicTimingsAdapter mAdapter;
    private RecyclerView mRecyclerView;


    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JSONObject jsonObject, CommonListener listener) throws Exception {

        List<View> views = new ArrayList<View>();

        mAdapter = new ClinicTimingsAdapter(((AppCompatActivity)context).getSupportFragmentManager());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((AppCompatActivity)context);
        mRecyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.item_clock, null);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 400));
        mRecyclerView.setLayoutParams(lp);
        mRecyclerView.addItemDecoration(new DividerDecoration((AppCompatActivity)context));
        mRecyclerView.setTag(R.id.key, jsonObject.getString("key"));
        mRecyclerView.setTag(R.id.type, jsonObject.getString("type"));
        views.add(mRecyclerView);

        return views;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
