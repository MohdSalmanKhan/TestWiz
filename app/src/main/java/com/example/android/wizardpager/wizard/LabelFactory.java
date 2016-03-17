package com.example.android.wizardpager.wizard;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.android.wizardpager.R;

import static com.example.android.wizardpager.wizard.FormUtils.*;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijay on 24-05-2015.
 */
public class LabelFactory implements FormWidgetFactory {

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JSONObject jsonObject, com.example.android.wizardpager.wizard.Interfaces.CommonListener listener) throws Exception {
        List<View> views = new ArrayList<>(1);
        LinearLayout.LayoutParams layoutParams = getLayoutParams(WRAP_CONTENT, WRAP_CONTENT, 0, 0, 0, 16);
        views.add(getTextViewWith(context, 16, jsonObject.getString("text"), jsonObject.getString("key"),
                jsonObject.getString("type"), layoutParams, FONT_BOLD_PATH));
        return views;    }
}
