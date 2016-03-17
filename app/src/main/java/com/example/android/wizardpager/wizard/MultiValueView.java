package com.example.android.wizardpager.wizard;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.internal.widget.TintContextWrapper;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.Interfaces.JsonApi;
import com.example.android.wizardpager.wizard.ui.MaxLengthValidator;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.rey.material.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by salman on 09/03/16.
 */
public class MultiValueView extends LinearLayout {

    private LinearLayout linearLayout;
    private Button addMoreButton;
    private MaterialEditText materialEditText;


    public static final int MIN_LENGTH = 0;
    public static final int MAX_LENGTH = 100;
    public static final int FIXED_LENGTH = 10;
    private int countMulti = 0;

    private void validationForEditText(MaterialEditText editText, String stepName, Context context, JSONObject jsonObject) throws Exception {

        int minLength = MIN_LENGTH;
        int maxLength = MAX_LENGTH;
        int fixedLength = FIXED_LENGTH;

        if (!TextUtils.isEmpty(jsonObject.optString("value"))) {
            editText.setText(jsonObject.optString("value"));
        }

        //add validators
        JSONObject requiredObject = jsonObject.optJSONObject("v_required");
        if (requiredObject != null) {
            String requiredValue = requiredObject.getString("value");
            if (!TextUtils.isEmpty(requiredValue)) {
                if (Boolean.TRUE.toString().equalsIgnoreCase(requiredValue)) {
                    editText.addValidator(new RequiredValidator(requiredObject.getString("err")));
                }
            }
        }

        JSONObject minLengthObject = jsonObject.optJSONObject("v_min_length");
        if (minLengthObject != null) {
            String minLengthValue = minLengthObject.optString("value");
            if (!TextUtils.isEmpty(minLengthValue)) {
                minLength = Integer.parseInt(minLengthValue);
                editText.addValidator(new MinLengthValidator(minLengthObject.getString("err"), Integer.parseInt(minLengthValue)));
            }
        }

        JSONObject maxLengthObject = jsonObject.optJSONObject("v_max_length");
        if (maxLengthObject != null) {
            String maxLengthValue = maxLengthObject.optString("value");
            if (!TextUtils.isEmpty(maxLengthValue)) {
                maxLength = Integer.parseInt(maxLengthValue);
                editText.addValidator(new MaxLengthValidator(maxLengthObject.getString("err"), Integer.parseInt(maxLengthValue)));
            }
        }


        editText.setMaxCharacters(maxLength);
        editText.setMinCharacters(minLength);


        JSONObject regexObject = jsonObject.optJSONObject("v_regex");
        if (regexObject != null) {
            String regexValue = regexObject.optString("value");
            if (!TextUtils.isEmpty(regexValue)) {
                editText.addValidator(new RegexpValidator(regexObject.getString("err"), regexValue));
            }
        }

        JSONObject emailObject = jsonObject.optJSONObject("v_email");
        if (emailObject != null) {
            String emailValue = emailObject.optString("value");
            if (!TextUtils.isEmpty(emailValue)) {
                if (Boolean.TRUE.toString().equalsIgnoreCase(emailValue)) {
                    editText.addValidator(new RegexpValidator(emailObject.getString("err"), android.util.Patterns.EMAIL_ADDRESS.toString()));
                }
            }
        }

        JSONObject urlObject = jsonObject.optJSONObject("v_url");
        if (urlObject != null) {
            String urlValue = urlObject.optString("value");
            if (!TextUtils.isEmpty(urlValue)) {
                if (Boolean.TRUE.toString().equalsIgnoreCase(urlValue)) {
                    editText.addValidator(new RegexpValidator(urlObject.getString("err"), Patterns.WEB_URL.toString()));
                }
            }
        }

        JSONObject numericObject = jsonObject.optJSONObject("v_numeric");
        if (numericObject != null) {
            String numericValue = numericObject.optString("value");
            if (!TextUtils.isEmpty(numericValue)) {
                if (Boolean.TRUE.toString().equalsIgnoreCase(numericValue)) {
                    editText.addValidator(new RegexpValidator(numericObject.getString("err"), "[0-9]+"));
                }
            }
        }

        // edit type check
        String editType = jsonObject.optString("edit_type");
        if (!TextUtils.isEmpty(editType)) {
            if (editType.equals("number")) {
                editText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
        }

        editText.addTextChangedListener(new GenericTextWatcher(stepName, editText));
    }


    private void addDatainEditText(MaterialEditText editText, JSONObject jsonObject) throws Exception {
        editText.setHint(jsonObject.getString("hint"));
        editText.setFloatingLabelText(jsonObject.getString("hint"));
        editText.setId(ViewUtil.generateViewId());
        editText.setTag(R.id.key, jsonObject.getString("key"));
        editText.setTag(R.id.type, jsonObject.getString("type"));
    }

    public MultiValueView(final Context context) {
        super(context);

    }

    public MultiValueView(final Context context, Bundle bundle) throws Exception {
        super(context);
        linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_multi_edit_text, null);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addMoreButton = (Button) linearLayout.findViewById(R.id.addButton);
        materialEditText = (MaterialEditText) linearLayout.findViewById(R.id.multiValue);

        String jsonApi = bundle.getString("json");
        final JSONObject jsonObj = new JSONObject(jsonApi);
        final String stepName = bundle.getString("stepName");

        addDatainEditText(materialEditText, jsonObj);

        validationForEditText(materialEditText, stepName, context, jsonObj);

        addMoreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialEditText met = (MaterialEditText) LayoutInflater.from(context).inflate(R.layout.item_edit_text, null);
                linearLayout.addView(met, linearLayout.getChildCount() - 1);

                countMulti++;
                try {
                    String key = jsonObj.getString("key") + countMulti;

                    JsonApi api = null;
                    if (materialEditText.getContext() instanceof JsonApi) {
                        api = (JsonApi) materialEditText.getContext();
                    } else if (materialEditText.getContext() instanceof TintContextWrapper) {
                        TintContextWrapper tintContextWrapper = (TintContextWrapper) materialEditText.getContext();
                        api = (JsonApi) tintContextWrapper.getBaseContext();
                    } else {
                        throw new RuntimeException("Could not fetch context");
                    }

                    JSONObject completeJson = api.getCurrentJson();
                    JSONObject stepJsonObject = completeJson.getJSONObject(stepName);

                    JSONArray fieldsJsonArray = stepJsonObject.getJSONArray("fields");

                    JSONObject inputJson = new JSONObject(jsonObj.toString());
                    inputJson.remove("key");
                    if(inputJson.has("value")){
                        inputJson.remove("value");
                    }

                    inputJson.put("key", key);

                    fieldsJsonArray.put(inputJson);

                    addDatainEditText(met, inputJson);
                    validationForEditText(met, stepName, context, inputJson);




                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        this.addView(linearLayout);

    }


}
