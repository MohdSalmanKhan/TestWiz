package com.example.android.wizardpager.wizard;

import com.rengwuxian.materialedittext.validation.METValidator;

/**
 * Created by salman on 08/03/16.
 */
public class RequiredValidator extends METValidator {

    public RequiredValidator(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public boolean isValid(CharSequence charSequence, boolean isEmpty) {
        return !isEmpty;
    }
}
