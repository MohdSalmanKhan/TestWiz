package com.example.android.wizardpager.wizard.ui;

import com.example.android.wizardpager.wizard.EditTextFactory;
import com.example.android.wizardpager.wizard.LengthValidator;

/**
 * Created by salman on 08/03/16.
 */
public class MaxLengthValidator extends LengthValidator {

    public MaxLengthValidator(String errorMessage, int maxLength) {
        super(errorMessage, EditTextFactory.MIN_LENGTH, maxLength);
    }
}
