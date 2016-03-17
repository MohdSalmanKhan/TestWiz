package com.example.android.wizardpager.wizard;


import org.json.JSONException;

import android.support.v7.internal.widget.TintContextWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.Interfaces.JsonApi;

import java.util.ArrayList;


public class SpinnerTextWatcher implements TextWatcher {

    private View   mView;
    private String mStepName;
    private ArrayList<String> dataList;
    private ArrayAdapter<String> adapter;
    Button button;
    ListView listView;



    public SpinnerTextWatcher(String stepName, View view, ArrayList<String> dataList, ArrayAdapter<String> adapter, Button button, ListView listView) {
        mView = view;
        mStepName = stepName;
        this.dataList = dataList;
        this.adapter = adapter;
        this.button = button;
        this.listView = listView;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {

    }
}