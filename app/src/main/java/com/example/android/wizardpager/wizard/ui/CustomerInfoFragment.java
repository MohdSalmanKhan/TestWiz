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

package com.example.android.wizardpager.wizard.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.AddImageFactory;
import com.example.android.wizardpager.wizard.ChipTextView;
import com.example.android.wizardpager.wizard.ContactsCompletionView;
import com.example.android.wizardpager.wizard.Interfaces.CommonListener;
import com.example.android.wizardpager.wizard.Interfaces.JsonApi;
import com.example.android.wizardpager.wizard.JsonFormFragmentPresenter;
import com.example.android.wizardpager.wizard.JsonFormFragmentView;
import com.example.android.wizardpager.wizard.JsonFormFragmentViewState;
import com.example.android.wizardpager.wizard.MvpFragment;
import com.example.android.wizardpager.wizard.WizardActivity;
import com.example.android.wizardpager.wizard.model.CustomerInfoPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerInfoFragment extends MvpFragment<JsonFormFragmentPresenter, JsonFormFragmentViewState> implements
        WizardActivity.MiddleActionButtonClick, JsonFormFragmentView<JsonFormFragmentViewState>, CommonListener {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private CustomerInfoPage mPage;
    private TextView mEmailView;
    private TextView mPasswordView;
    private TextInputLayout mEmailInputLayout;
    private TextInputLayout mPasswordInputLayout;

    private View rootView;
    private LinearLayout mMainView;
    private JsonApi mJsonApi;
    private int buttonLocation;


    public void setNull(){

    }


    public static CustomerInfoFragment getFormFragment(String stepName) {
        CustomerInfoFragment jsonFormFragment = new CustomerInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stepName", stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    public static CustomerInfoFragment create(String key, String stepName) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putString("stepName", stepName);

        CustomerInfoFragment fragment = new CustomerInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CustomerInfoFragment() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (CustomerInfoPage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_page_customer_info, container, false);

        mMainView = (LinearLayout) rootView.findViewById(R.id.main_layout);

        return rootView;

    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
        mJsonApi = (JsonApi) activity;

    }


    @Override
    public void onStop() {
        super.onStop();
    //    mCallbacks = null;
     //   mJsonApi = null;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mJsonApi = null;
        mCallbacks = null;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.addFormElements();

    }

    @Override
    protected JsonFormFragmentViewState createViewState() {
        return new JsonFormFragmentViewState();
    }

    @Override
    protected JsonFormFragmentPresenter createPresenter() {
        return new JsonFormFragmentPresenter();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (mEmailView != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    private boolean isValidEmail(String text) {
        return text != null && text.length() > 0 && (Patterns.EMAIL_ADDRESS.matcher(text).matches());
    }

    public void assertValidCredentials() {
        presenter.onNextClick(mMainView);
        mPage.notifyDataChanged();
    }

    @Override
    public void onMiddleActionButtonClick() {
        assertValidCredentials();
    }

    @Override
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public CommonListener getCommonListener() {
        return this;
    }

    @Override
    public void addFormElement(View view) {
        mMainView.addView(view, buttonLocation);
        buttonLocation++;
    }

    @Override
    public void addFormElements(List<View> views) {
        int loc = 0;
        for (View view : views) {
            if(view instanceof Button && view.getId() == R.id.addButton){
                buttonLocation = loc;
            }
            loc++;
            mMainView.addView(view);
        }
    }

    @Override
    public ActionBar getSupportActionBar() {
        return ((WizardActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public Toolbar getToolbar() {
        return ((WizardActivity) getActivity()).getToolbar();
    }

    @Override
    public void setToolbarTitleColor(int white) {

        getToolbar().setTitleTextColor(getContext().getResources().getColor(white));

    }

    @Override
    public void updateVisibilityOfNextAndSave(boolean next, boolean save) {
    }

    @Override
    public void hideKeyBoard() {

        super.hideSoftKeyboard();
    }

    @Override
    public void transactThis(CustomerInfoFragment next) {

        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(next.getClass().getSimpleName()).commit();
    }

    @Override
    public void updateRelevantImageView(Bitmap bitmap, String imagePath, String currentKey) {

        int childCount = mMainView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mMainView.getChildAt(i);
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                String key = (String) imageView.getTag(R.id.key);
                if (key.equals(currentKey)) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setTag(R.id.imagePath, imagePath);
                }
            }
        }
    }

    @Override
    public void updateImageInGridView(Bitmap bitmap, String imagePath, String currentKey) {

        int childCount = mMainView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mMainView.getChildAt(i);
            if (view instanceof GridView) {
                GridView gridView = (GridView) view;

                String key = (String) gridView.getTag(R.id.key);
                if (key.equals(currentKey)) {

                    String totalImages = (String) gridView.getTag(R.id.est_plt_images_grid);
                    if( !totalImages.isEmpty()){
                        totalImages = totalImages + ",";
                    }
                    totalImages = totalImages + imagePath;
                    gridView.setTag(R.id.est_plt_images_grid, totalImages);
                    AddImageFactory.GridAdapter gridAdapter = (AddImageFactory.GridAdapter) gridView.getAdapter();
                    gridAdapter.add(imagePath);
                }
            }
        }
    }

    @Override
    public void updateListView(ArrayList<String> arrayList){
        int childCount = mMainView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mMainView.getChildAt(i);
            if (view instanceof ContactsCompletionView) {

                ContactsCompletionView contactsCompletionView = (ContactsCompletionView) view;
                contactsCompletionView.setTag(R.id.list, arrayList);

                contactsCompletionView.setTag(R.id.searchView, arrayList);

                ArrayAdapter arrayAdapter = (ArrayAdapter) contactsCompletionView.getAdapter();
                arrayAdapter.clear();
                contactsCompletionView.clear();

                for(String s: arrayList) {
                    arrayAdapter.add(s);
                    contactsCompletionView.addObject(s);
                }
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void writeValue(String stepName, String key, String value) {

        try {
            mJsonApi.writeValue(stepName, key, value);
        } catch (JSONException e) {
            // TODO - handle
            e.printStackTrace();
        }
    }

    @Override
    public void writeValue(String stepName, String key, JSONArray value) {
        try {
            mJsonApi.writeValue(stepName, key, value);
        } catch (JSONException e) {
            // TODO - handle
            e.printStackTrace();
        }
    }

    @Override
    public void writeValue(String stepName, String prentKey, String childObjectKey, String childKey, String value) {

        try {
            mJsonApi.writeValue(stepName, prentKey, childObjectKey, childKey, value);
        } catch (JSONException e) {
            // TODO - handle
            e.printStackTrace();
        }
    }

    @Override
    public String getStepString(String stepName){
        return mJsonApi.getStepString(stepName);
    }

    @Override
    public JSONObject getStep(String stepName) {
        if(mJsonApi != null)
            return mJsonApi.getStep(stepName);
        else
            return null;

    }

    @Override
    public String getCurrentJsonState() {
        return mJsonApi.currentJsonState();
    }

    @Override
    public JSONObject getCurrentJson() {
        return mJsonApi.getCurrentJson();
    }

    @Override
    public void finishWithResult(Intent returnIntent) {

        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }

    @Override
    public void setUpBackButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void backClick() {

        getActivity().onBackPressed();

    }

    @Override
    public void unCheckAllExcept(String parentKey, String childKey) {

        int childCount = mMainView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mMainView.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radio = (RadioButton) view;
                String parentKeyAtIndex = (String) radio.getTag(R.id.key);
                String childKeyAtIndex = (String) radio.getTag(R.id.childKey);
                if (parentKeyAtIndex.equals(parentKey) && !childKeyAtIndex.equals(childKey)) {
                    radio.setChecked(false);
                }
            }
        }
    }

    @Override
    public String getCount() {
        return mJsonApi.getCount();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.onCheckedChanged(buttonView, isChecked);

    }

    @Override
    public void onClick(View v) {

        presenter.onClick(v);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        presenter.onItemSelected(parent, view, position, id);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0) {
            presenter.onItemClick(parent);
        }
    }
}
