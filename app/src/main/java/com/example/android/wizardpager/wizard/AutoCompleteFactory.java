package com.example.android.wizardpager.wizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.Interfaces.*;
import com.example.android.wizardpager.wizard.Interfaces.CommonListener;
import com.example.android.wizardpager.wizard.ui.DefaultCompletionView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salman on 17/03/16.
 */
public class AutoCompleteFactory implements FormWidgetFactory {
    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JSONObject jsonObject, CommonListener listener) throws Exception {
        final List<View> views = new ArrayList<>();


        String names[] = {"abc","abd", "abg"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, names);
        ContactsCompletionView completionView;

        completionView = (ContactsCompletionView) LayoutInflater.from(context).inflate(R.layout.autocomplete, null);
        completionView.setAdapter(arrayAdapter);
        completionView.setTag(R.id.key, jsonObject.get("key"));
        completionView.setTag(R.id.type, jsonObject.get("type"));

        views.add(completionView);
        return views;
    }
}
