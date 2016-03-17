package com.example.android.wizardpager.wizard;

import android.content.Context;
import android.view.View;

import com.example.android.wizardpager.wizard.Interfaces.CommonListener;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by salman on 08/03/16.
 */
public interface FormWidgetFactory {
    List<View> getViewsFromJson(String stepName, Context context, JSONObject jsonObject, CommonListener listener) throws Exception;

}
