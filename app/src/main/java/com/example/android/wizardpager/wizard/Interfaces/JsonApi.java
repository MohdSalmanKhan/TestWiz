package com.example.android.wizardpager.wizard.Interfaces;

/**
 * Created by salman on 08/03/16.
 */
import org.json.JSONException;
import org.json.JSONObject;


public interface JsonApi {
    JSONObject getStep(String stepName);

    void writeValue(String stepName, String key, String value) throws JSONException;

    void writeValue(String stepName, String prentKey, String childObjectKey, String childKey, String value)
            throws JSONException;

    String currentJsonState();

    String getCount();

    JSONObject getCurrentJson();
}