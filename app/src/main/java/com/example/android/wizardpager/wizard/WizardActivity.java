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

package com.example.android.wizardpager.wizard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.Interfaces.JsonApi;
import com.example.android.wizardpager.wizard.ui.PageFragmentCallbacks;
import com.example.android.wizardpager.wizard.ui.ReviewFragment;
import com.example.android.wizardpager.wizard.ui.StepPagerStrip;
import com.example.android.wizardpager.wizard.model.AbstractWizardModel;
import com.example.android.wizardpager.wizard.model.ModelCallbacks;
import com.example.android.wizardpager.wizard.model.Page;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import timber.log.Timber;

public class WizardActivity extends AppCompatActivity implements
        PageFragmentCallbacks,
        ReviewFragment.Callbacks,
        ModelCallbacks, JsonApi {

    private JSONObject mJSONObject;
    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;

    private boolean mEditingAfterReview;

    private AbstractWizardModel mWizardModel = new SandwichWizardModel(this);

    private boolean mConsumePageSelectedEvent;

    private Button mNextButton;
    protected Button mActionButton;
    protected ProgressBar mProgressBar;
    private Button mPrevButton;

    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;
    public Toolbar toolbar;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
        }

        mWizardModel.registerListener(this);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);
        mStepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(mPagerAdapter.getCount() - 1, position);
                if (mPager.getCurrentItem() != position) {
                    mPager.setCurrentItem(position);
                }
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mActionButton = (Button) findViewById(R.id.middle_button);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPrevButton = (Button) findViewById(R.id.prev_button);

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mStepPagerStrip.setCurrentPage(position);

                if (mConsumePageSelectedEvent) {
                    mConsumePageSelectedEvent = false;
                    return;
                }

                mEditingAfterReview = false;
                updateBottomBar();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
                    DialogFragment dg = new DialogFragment() {
                        @Override
                        public Dialog onCreateDialog(Bundle savedInstanceState) {
                            return new AlertDialog.Builder(getActivity())
                                    .setMessage(R.string.submit_confirm_message)
                                    .setPositiveButton(R.string.submit_confirm_button, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            finishWizard();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .create();
                        }
                    };
                    dg.show(getSupportFragmentManager(), "place_order_dialog");
                } else {
                    if (mEditingAfterReview) {
                        mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
                    } else {
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                    }
                }
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });


        onPageTreeChanged();
        updateBottomBar();

        try {
            mJSONObject = new JSONObject(getJson());
        } catch (JSONException e) {
            Log.d("salman", "Initialization error. Json passed is invalid : " + e.getMessage());
        }
    }

    private String getJson(){
        return "{\n" +
                "    \"count\":\"5\",\n" +
                "    \"step1\":{\n" +
                "        \"fields\":[\n" +
                "            {\n" +
                "                \"key\":\"salutation\",\n" +
                "                \"type\":\"spinner\",\n" +
                "                \"hint\": \"Salutation\",\n" +
                "                \"values\":[\"Mr.\", \"Mrs.\", \"Ms.\"],\n" +
                "                \"v_required\":{  \"value\" : \"true\",\n" +
                "                    \"err\" : \"Please choose a value to proceed.\"\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\" : \"autocomplete\",\n" +
                "                \"type\" : \"dropdown\",\n" +
                "                \"hint\" : \"select\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\":\"name\",\n" +
                "                \"type\":\"edit_text\",\n" +
                "                \"hint\":\"Name \",\n" +
                "                \"v_min_length\":{  \"value\" : \"3\",\n" +
                "                                    \"err\" : \"Min length should be at least 3\"\n" +
                "                                },\n" +
                "                \"v_max_length\":{  \"value\" : \"30\",\n" +
                "                    \"err\" : \"Max length can be at most 10.\"\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\":\"date_of_birth\",\n" +
                "                \"type\":\"edit_text\",\n" +
                "                \"hint\":\"Date Of Birth (Yyyy-mm-dd) \",\n" +
                "                \"edit_type\" : \"number\"\n" +
                "\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\":\"start_year\",\n" +
                "                \"type\":\"edit_text\",\n" +
                "                \"hint\":\"Practicing Start Year\",\n" +
                "                \"edit_type\" : \"number\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\":\"primary_contact_number\",\n" +
                "                \"type\":\"edit_text\",\n" +
                "                \"hint\":\"Primary Contact Number\",\n" +
                "                \"edit_type\" : \"number\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\":\"email\",\n" +
                "                \"type\":\"edit_text\",\n" +
                "                \"hint\":\"Primary Email Address\",\n" +
                "                \"v_email\":{  \"value\" : \"true\",\n" +
                "                    \"err\" : \"Not an email.\"\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\":\"street_adress\",\n" +
                "                \"type\":\"edit_text\",\n" +
                "                \"hint\":\"Stree Address\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\":\"notes\",\n" +
                "                \"type\":\"edit_text\",\n" +
                "                \"hint\":\"Notes\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\":\"summary\",\n" +
                "                \"type\":\"edit_text\",\n" +
                "                \"hint\":\"Summary\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\":\"phone\",\n" +
                "                \"type\":\"multi_edit_text\",\n" +
                "                \"hint\":\"Phone\",\n" +
                "                \"edit_type\" : \"number\"\n" +
                "            },\n" +
                "             {\n" +
                "                \"key\":\"Email\",\n" +
                "                \"type\":\"multi_edit_text\",\n" +
                "                \"hint\":\"Email\",\n" +
                "                \"edit_type\" : \"number\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"title\":\"Step 1 of 5\",\n" +
                "        \"next\":\"step2\"\n" +
                "    },\n" +
                "    \"step2\":{\n" +
                "        \"fields\":[\n" +
                "            {\n" +
                "                \"key\":\"name\",\n" +
                "                \"type\":\"edit_text\",\n" +
                "                \"hint\":\"Enter Country\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\":\"checkData\",\n" +
                "                \"type\":\"check_box\",\n" +
                "                \"label\":\"Select multiple preferences\",\n" +
                "                \"options\":[\n" +
                "                    {\n" +
                "                        \"key\":\"awesomeness\",\n" +
                "                        \"text\":\"Are you willing for some awesomeness?\",\n" +
                "                        \"value\":\"false\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\":\"newsletter\",\n" +
                "                        \"text\":\"Do you really want to opt out from my newsletter?\",\n" +
                "                        \"value\":\"false\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\":\"radioData\",\n" +
                "                \"type\":\"radio\",\n" +
                "                \"label\":\"Select one item from below\",\n" +
                "                \"options\":[\n" +
                "                    {\n" +
                "                        \"key\":\"areYouPro\",\n" +
                "                        \"text\":\"Are you pro?\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\":\"areYouAmature\",\n" +
                "                        \"text\":\"Are you amature?\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\":\"areYouNovice\",\n" +
                "                        \"text\":\"Are you novice?\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"value\":\"areYouNovice\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"title\":\"Step 2 of 5\",\n" +
                "        \"next\":\"step3\"\n" +
                "    },\n" +
                "    \"step3\":{\n" +
                "        \"fields\":[\n" +
                "            {\n" +
                "                \"key\":\"multi_spinner\",\n" +
                "                \"type\":\"edit_text_spinner\",\n" +
                "                \"hint\":\"Enter data\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"title\" : \"Step 3 of 5\",\n" +
                "        \"next\" : \"step4\"\n" +
                "    },\n" +
                "    \"step4\" : {\n" +
                "        \"fields\" : [\n" +
                "        {\n" +
                "            \"key\" : \"add_photo\",\n" +
                "            \"type\" : \"multi_image\"\n" +
                "        }\n" +
                "        ],\n" +
                "        \"title\" : \"Step 4 of 5\",\n" +
                "        \"next\" : \"step5\"\n" +
                "    },\n" +
                "    \"step5\" : {\n" +
                "        \"fields\" : [\n" +
                "        {\n" +
                "            \"key\" : \"start_time\",\n" +
                "            \"type\" : \"clock\"\n" +
                "        }\n" +
                "        ], \n" +
                "        \"title\" : \"Step 5 of 5\"\n" +
                "    }\n" +
                "}";
    }

    private void finishWizard() {
        //Finish him!!!!

    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 = review step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void updateBottomBar() {
        int position = mPager.getCurrentItem();
        if (position == mCurrentPageSequence.size()) {
            mNextButton.setText(R.string.finish);
            mActionButton.setVisibility(View.INVISIBLE);
        } else {
            mNextButton.setText(mEditingAfterReview
                    ? R.string.review
                    : R.string.next);
            mNextButton.setBackgroundResource(R.drawable.selectable_item_background);
            TypedValue v = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
            //mNextButton.setTextAppearance(this, v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());

            Page currentPage = mCurrentPageSequence.get(position);
            Timber.d("Current page: %s", currentPage.getTitle());
            if (currentPage.hasMiddleAction()) {
                mActionButton.setVisibility(View.VISIBLE);
                mActionButton.setText(com.example.android.wizardpager.R.string.validate_button);
                mActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {
                        Fragment item = mPagerAdapter.mPrimaryItem;
                        if (item != null && item instanceof MiddleActionButtonClick) {
                            MiddleActionButtonClick implementer = (MiddleActionButtonClick) item;
                            implementer.onMiddleActionButtonClick();
                        }
                    }
                });
            } else {
                mActionButton.setVisibility(View.INVISIBLE);
                mActionButton.setOnClickListener(null);
            }
        }


        mPrevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mWizardModel.save());
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(key)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    @Override
    public void onDisplayProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    @Override
    public String getStepString(String stepName) {
        synchronized (mJSONObject) {
            try {
                return mJSONObject.getJSONObject(stepName).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public JSONObject getStep(String stepName) {
        synchronized (mJSONObject) {
            try {
                return mJSONObject.getJSONObject(stepName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void writeValue(String stepName, String key, String value) throws JSONException {

        synchronized (mJSONObject) {
            JSONObject jsonObject = mJSONObject.getJSONObject(stepName);
            JSONArray fields = jsonObject.getJSONArray("fields");
            for (int i = 0; i < fields.length(); i++) {
                JSONObject item = fields.getJSONObject(i);
                String keyAtIndex = item.getString("key");
                if (key.equals(keyAtIndex)) {
                    item.put("value", value);
                    return;
                }
            }
        }
    }

    @Override
    public void writeValue(String stepName, String key, JSONArray value) throws JSONException {
        synchronized (mJSONObject) {
            JSONObject jsonObject = mJSONObject.getJSONObject(stepName);
            JSONArray fields = jsonObject.getJSONArray("fields");
            for (int i = 0; i < fields.length(); i++) {
                JSONObject item = fields.getJSONObject(i);
                String keyAtIndex = item.getString("key");
                if (key.equals(keyAtIndex)) {
                    item.put("value", value);
                    return;
                }
            }
        }
    }


    @Override
    public void writeValue(String stepName, String parentKey, String childObjectKey, String childKey, String value) throws JSONException {

        synchronized (mJSONObject) {
            JSONObject jsonObject = mJSONObject.getJSONObject(stepName);
            JSONArray fields = jsonObject.getJSONArray("fields");
            for (int i = 0; i < fields.length(); i++) {
                JSONObject item = fields.getJSONObject(i);
                String keyAtIndex = item.getString("key");
                if (parentKey.equals(keyAtIndex)) {
                    JSONArray jsonArray = item.getJSONArray(childObjectKey);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject innerItem = jsonArray.getJSONObject(j);
                        String anotherKeyAtIndex = innerItem.getString("key");
                        if (childKey.equals(anotherKeyAtIndex)) {
                            innerItem.put("value", value);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public String currentJsonState() {
        synchronized (mJSONObject) {
            return mJSONObject.toString();
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public String getCount() {
        synchronized (mJSONObject) {
            return mJSONObject.optString("count");
        }
    }

    @Override
    public JSONObject getCurrentJson() {
        synchronized (mJSONObject) {
            return mJSONObject;
        }
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {
                return new ReviewFragment();
            }

            return mCurrentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            if (mCurrentPageSequence == null) {
                return 0;
            }
            return Math.min(mCutOffPage + 1, mCurrentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }

    public interface MiddleActionButtonClick {
        void onMiddleActionButtonClick();
    }
}
