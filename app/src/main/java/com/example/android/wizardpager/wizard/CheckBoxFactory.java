package com.example.android.wizardpager.wizard;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.example.android.wizardpager.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by vijay on 24-05-2015.
 */
public class CheckBoxFactory implements FormWidgetFactory {

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JSONObject jsonObject, com.example.android.wizardpager.wizard.Interfaces.CommonListener listener) throws Exception {
        List<View> views = new ArrayList<>(1);
        views.add(FormUtils.getTextViewWith(context, 16, jsonObject.getString("label"), jsonObject.getString("key"),
                jsonObject.getString("type"), FormUtils.getLayoutParams(FormUtils.MATCH_PARENT, FormUtils.WRAP_CONTENT, 0, 0, 0, 0),
                FormUtils.FONT_BOLD_PATH));
        JSONArray options = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
        for (int i = 0; i < options.length(); i++) {
            JSONObject item = options.getJSONObject(i);
            CheckBox checkBox = (CheckBox) LayoutInflater.from(context).inflate(R.layout.item_checkbox, null);
            checkBox.setText(item.getString("text"));
            checkBox.setTag(R.id.key, jsonObject.getString("key"));
            checkBox.setTag(R.id.type, jsonObject.getString("type"));
            checkBox.setTag(R.id.childKey, item.getString("key"));
            checkBox.setGravity(Gravity.CENTER_VERTICAL);
            checkBox.setTextSize(16);
            //checkBox.setTypeface(Typeface.createFromAsset(context.getAssets(), FormUtils.FONT_REGULAR_PATH));
            checkBox.setOnCheckedChangeListener(listener);
            if (!TextUtils.isEmpty(item.optString("value"))) {
                checkBox.setChecked(Boolean.valueOf(item.optString("value")));
            }
            if (i == options.length() - 1) {
                checkBox.setLayoutParams(FormUtils.getLayoutParams(FormUtils.MATCH_PARENT, FormUtils.WRAP_CONTENT, 0, 0, 0, 16));
            }
            views.add(checkBox);
        }
        return views;    }
}
