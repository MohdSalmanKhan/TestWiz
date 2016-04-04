/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.wizardpager.wizard.model;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;

import com.example.android.wizardpager.wizard.JsonFormFragmentPresenter;
import com.example.android.wizardpager.wizard.ui.CustomerInfoFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A page asking for a name and an email.
 */
public class CustomerInfoPage extends Page {
    public static final String EMAIL_DATA_KEY = "email";
    public static final String PASSWORD_DATA_KEY = "name";
    private String stepName = "";
    CustomerInfoFragment customerInfoFragment;
    public CustomerInfoPage(ModelCallbacks callbacks, String title, String stepName) {
        super(callbacks, title);
        this.stepName = stepName;
    }

    @Override
    public Fragment createFragment() {
        customerInfoFragment = CustomerInfoFragment.create(getKey(), stepName);
        return customerInfoFragment;
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

        JSONObject jsonObject = customerInfoFragment.getStep(stepName);

        try {
            JSONArray fieldsArray = jsonObject.getJSONArray("fields");
            for(int i = 0 ; i < fieldsArray.length() ; i++){
                JSONObject fieldsObject = fieldsArray.getJSONObject(i);

                String value = null;

                if(fieldsObject.has("value")) {
                    value = fieldsObject.getString("value");
                }
                String key = null;

                if(fieldsObject.has("key")) {
                    key = fieldsObject.getString("key");
                }
                if(value != null && key != null && !value.isEmpty()){
                    dest.add(new ReviewItem(key, value, getKey(), -1));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
   //     customerInfoFragment.setNull();
 //       dest.add(new ReviewItem("Your email", mData.getString(EMAIL_DATA_KEY), getKey(), -1));
//        dest.add(new ReviewItem("Your password", mData.getString(PASSWORD_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        //Validation is enforced in UI
        return JsonFormFragmentPresenter.isValid;
      //  return !TextUtils.isEmpty(mData.getString(PASSWORD_DATA_KEY)) && !TextUtils.isEmpty(mData.getString(EMAIL_DATA_KEY));
    }
}
