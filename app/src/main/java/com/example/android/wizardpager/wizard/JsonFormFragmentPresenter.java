package com.example.android.wizardpager.wizard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;


import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.Interfaces.AddInLayout;
import com.example.android.wizardpager.wizard.ui.CustomerInfoFragment;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by vijay on 5/14/15.
 */
public class JsonFormFragmentPresenter extends MvpBasePresenter<JsonFormFragmentView<JsonFormFragmentViewState>>{
    private static final String TAG = "FormFragmentPresenter";
    private static final int RESULT_LOAD_IMG = 1;
    private static final int RESULT_LOAD__MULTIPLE_IMG = 2;
    private String mStepName;
    private JSONObject mStepDetails;
    private String mCurrentKey;
    private JsonFormInteractor mJsonFormInteractor = JsonFormInteractor.getInstance();
    public static boolean isValid = false;


    public void addFormElements() {
        mStepName = getView().getArguments().getString("stepName");
        JSONObject step = getView().getStep(mStepName);

        Log.v("salman",step.toString());

        try {
            mStepDetails = new JSONObject(step.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<View> views = mJsonFormInteractor.fetchFormElements(mStepName, getView().getContext(), mStepDetails,
                getView().getCommonListener());
        getView().addFormElements(views);
    }

    @SuppressLint("ResourceAsColor")
    public void setUpToolBar() {
        if (!mStepName.equals(JsonFormConstants.FIRST_STEP_NAME)) {
            getView().setUpBackButton();
        }
        getView().setActionBarTitle(mStepDetails.optString("title"));
        if (mStepDetails.has("next")) {
            getView().updateVisibilityOfNextAndSave(true, false);
        } else {
            getView().updateVisibilityOfNextAndSave(false, true);
        }
        getView().setToolbarTitleColor(R.color.white);
    }

    public void onBackClick() {
        getView().hideKeyBoard();
        getView().backClick();
    }

    public void onNextClick(LinearLayout mainView) {
        ValidationStatus validationStatus = writeValuesAndValidate(mainView);
        if (validationStatus.isValid()) {
            isValid = true;
             //   CustomerInfoFragment next = CustomerInfoFragment.getFormFragment(mStepDetails.optString("next"));
            getView().hideKeyBoard();
            onSaveClick(mainView);
        //    getView().transactThis(next);
        } else {
            isValid = false;
            getView().showToast(validationStatus.getErrorMessage());
        }
    }

    public ValidationStatus writeValuesAndValidate(LinearLayout mainView) {
        int childCount = mainView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = mainView.getChildAt(i);
            String key = (String) childAt.getTag(R.id.key);
            if (childAt instanceof MaterialEditText) {
                MaterialEditText editText = (MaterialEditText) childAt;
                ValidationStatus validationStatus = EditTextFactory.validate(editText);
                if (!validationStatus.isValid()) {
                    return validationStatus;
                }
                getView().writeValue(mStepName, key, editText.getText().toString());
            } else if (childAt instanceof RecyclerView){
                ClinicTimingsAdapter clinicTimingsAdapter = (ClinicTimingsAdapter) ((RecyclerView)childAt).getAdapter();

                JSONArray jsonArray = new JSONArray();
                for(i = 0; i < 7 ; i++){
                    TimingsViewModel timingsViewModel = clinicTimingsAdapter.mDataSet.get(i);

                    String day = timingsViewModel.getDay();
                    Timepoint session1Start = timingsViewModel.getSession1Start();
                    Timepoint session1End = timingsViewModel.getSession1End();

                    Timepoint session2Start = timingsViewModel.getSession2Start();
                    Timepoint session2End = timingsViewModel.getSession2End();

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("day", day);
                        if(session1Start != null && session1End != null) {
                            jsonObject.put("session1_start_hour", session1Start.getHour());
                            jsonObject.put("session1_start_minute", session1Start.getMinute());
                            jsonObject.put("session1_end_hour", session1End.getHour());
                            jsonObject.put("session1_end_minute", session1End.getMinute());

                        }
                        if(session2Start != null && session2End != null) {
                            jsonObject.put("session2_start_hour", session2Start.getHour());
                            jsonObject.put("session2_start_minute", session2Start.getMinute());
                            jsonObject.put("session2_end_hour", session2End.getHour());
                            jsonObject.put("session2_end_minute", session2End.getMinute());
                        }
                        jsonArray.put(jsonObject);
                    }
                    catch (Exception e){

                    }
                    getView().writeValue(mStepName, key, jsonArray);

                }
            }
            else if (childAt instanceof ImageView) {
                ValidationStatus validationStatus = ImagePickerFactory.validate((ImageView) childAt);
                if (!validationStatus.isValid()) {
                    return validationStatus;
                }
                Object path = childAt.getTag(R.id.imagePath);
                if (path instanceof String) {
                    getView().writeValue(mStepName, key, (String) path);
                }
            }else if (childAt instanceof ContactsCompletionView){
                String type =(String) childAt.getTag(R.id.type);

                if(type.equalsIgnoreCase("edit_text_spinner")){
                    ArrayList<String> arrayList = (ArrayList<String>) childAt.getTag(R.id.searchView);
                    String result = "";
                    for ( String s : arrayList){
                        result = result + s + ",";
                    }
                    result = result.substring(0, result.length() -1) ;
                    getView().writeValue(mStepName, key, (String) result);
                }
            } else if (childAt instanceof ExpandableHeightGridView){
                String type =(String) childAt.getTag(R.id.type);

                if(type.equalsIgnoreCase("multi_image")){

                    String result = (String) childAt.getTag(R.id.est_plt_images_grid);
                    getView().writeValue(mStepName, key, (String) result);
                }

            } else if (childAt instanceof ContactsCompletionView){

                ContactsCompletionView completionView = (ContactsCompletionView) childAt;
                int count = completionView.getAdapter().getCount();
                String res = "";

                for(i =0 ; i < count ; i++ ){
                    res = res + completionView.getAdapter().getItem(i) + ",";
                }
                res = res.substring(0, res.length() - 1);
                getView().writeValue(mStepName, key, (String) res);

            } else if (childAt instanceof CheckBox) {
                String parentKey = (String) childAt.getTag(R.id.key);
                String childKey = (String) childAt.getTag(R.id.childKey);
                getView().writeValue(mStepName, parentKey, JsonFormConstants.OPTIONS_FIELD_NAME, childKey,
                        String.valueOf(((CheckBox) childAt).isChecked()));
            } else if (childAt instanceof RadioButton) {
                String parentKey = (String) childAt.getTag(R.id.key);
                String childKey = (String) childAt.getTag(R.id.childKey);
                if (((RadioButton) childAt).isChecked()) {
                    getView().writeValue(mStepName, parentKey, childKey);
                }
            } else if (childAt instanceof MaterialSpinner) {
                MaterialSpinner spinner = (MaterialSpinner) childAt;
                ValidationStatus validationStatus = SpinnerFactory.validate(spinner);
                if (!validationStatus.isValid()) {
                    spinner.setError(validationStatus.getErrorMessage());
                    return validationStatus;
                } else {
                    spinner.setError(null);
                }
            }
        }
        return new ValidationStatus(true, null);
    }

    public void onSaveClick(LinearLayout mainView) {
        ValidationStatus validationStatus = writeValuesAndValidate(mainView);
        if (validationStatus.isValid()) {

            Log.v("salman", " JSON is  " + getView().getCurrentJsonState());

          //  Intent returnIntent = new Intent();
          //  returnIntent.putExtra("json", getView().getCurrentJsonState());
           // getView().finishWithResult(returnIntent);
        } else {
            Toast.makeText(getView().getContext(), validationStatus.getErrorMessage(), Toast.LENGTH_LONG);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // No need for null check on cursor
            Cursor cursor = getView().getContext().getContentResolver()
                    .query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imagePath = cursor.getString(columnIndex);
            getView().updateRelevantImageView(ImageUtils.loadBitmapFromFile(imagePath, ImageUtils.getDeviceWidth(getView().getContext()), dpToPixels(getView().getContext(), 200)), imagePath, mCurrentKey);
            cursor.close();
        } else if (requestCode == Utils.CALL_ACTIVITY && resultCode == Activity.RESULT_OK && data != null){
            Bundle bundle = data.getExtras();
            getView().updateListView(bundle.getStringArrayList("list"));
        } else if ( requestCode == RESULT_LOAD__MULTIPLE_IMG && resultCode == Activity.RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // No need for null check on cursor
            Cursor cursor = getView().getContext().getContentResolver()
                    .query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imagePath = cursor.getString(columnIndex);
            getView().updateImageInGridView(ImageUtils.loadBitmapFromFile(imagePath, ImageUtils.getDeviceWidth(getView().getContext()), dpToPixels(getView().getContext(), 200)), imagePath, mCurrentKey);
            cursor.close();

        }
    }

    public void onItemClick(View v){

        String key = (String) v.getTag(R.id.key);
        String type = (String) v.getTag(R.id.type);
        if (JsonFormConstants.MULTI_IMAGE.equals(type)) {
            getView().hideKeyBoard();
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mCurrentKey = key;
            getView().startActivityForResult(galleryIntent, RESULT_LOAD__MULTIPLE_IMG);
        }
    }

    public void onClick(View v) {
        String key = (String) v.getTag(R.id.key);
        String type = (String) v.getTag(R.id.type);
        if (JsonFormConstants.CHOOSE_IMAGE.equals(type)) {
            getView().hideKeyBoard();
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mCurrentKey = key;
            getView().startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        } else if ( JsonFormConstants.MULTI_SPINNER.equalsIgnoreCase(type) ){
      //      getView().addFormElement(v);
            ArrayList<String> arrayList = (ArrayList<String>) v.getTag(R.id.searchView);
            Intent intent = new Intent(getView().getContext(), MutiSpinnerActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putStringArrayList("list", arrayList);
            intent.putExtras(bundle1);
            getView().startActivityForResult(intent, Utils.CALL_ACTIVITY);

        }else if(JsonFormConstants.CLOCK.equalsIgnoreCase(type)){
            Dialog dialog = new Dialog(getView().getContext());

        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton instanceof CheckBox) {
            String parentKey = (String) compoundButton.getTag(R.id.key);
            String childKey = (String) compoundButton.getTag(R.id.childKey);
            getView().writeValue(mStepName, parentKey, JsonFormConstants.OPTIONS_FIELD_NAME, childKey,
                    String.valueOf(((CheckBox) compoundButton).isChecked()));
        } else if (compoundButton instanceof RadioButton) {
            if (isChecked) {
                String parentKey = (String) compoundButton.getTag(R.id.key);
                String childKey = (String) compoundButton.getTag(R.id.childKey);
                getView().unCheckAllExcept(parentKey, childKey);
                getView().writeValue(mStepName, parentKey, childKey);
            }
        }
    }
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String parentKey = (String) parent.getTag(R.id.key);
        if (position >= 0) {
            String value = (String) parent.getItemAtPosition(position + 1);
            getView().writeValue(mStepName, parentKey, value);
        }
    }

    public static int dpToPixels(Context context, float dps) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }
}
