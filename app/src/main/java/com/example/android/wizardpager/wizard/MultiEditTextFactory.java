package com.example.android.wizardpager.wizard;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.Interfaces.*;
import com.example.android.wizardpager.wizard.Interfaces.CommonListener;
import com.example.android.wizardpager.wizard.ui.MaxLengthValidator;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.rey.material.util.ViewUtil;
import com.rey.material.widget.EditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salman on 09/03/16.
 */
public class MultiEditTextFactory implements FormWidgetFactory {

    private Context mContext;




    @Override
    public List<View> getViewsFromJson(final String stepName, final Context context, final JSONObject jsonObject, CommonListener listener) throws Exception {


        final List<View> views = new ArrayList<>();
        mContext = context;

        Bundle bundle = new Bundle();

        bundle.putString("json", jsonObject.toString());
        bundle.putString("stepName", stepName);

        LinearLayout view = new MultiValueView(context, bundle);

        views.add(view);
        return views;
    }

    public static ValidationStatus validate(MaterialEditText editText) {
        boolean validate = editText.validate();
        if(!validate) {
            return new ValidationStatus(false, editText.getError().toString());
        }
        return new ValidationStatus(true, null);
    }
}
