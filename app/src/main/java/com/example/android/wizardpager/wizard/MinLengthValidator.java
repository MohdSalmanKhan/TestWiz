package com.example.android.wizardpager.wizard;

/**
 * Created by salman on 08/03/16.
 */
public class MinLengthValidator extends LengthValidator {

    public MinLengthValidator(String errorMessage, int minLength) {
        super(errorMessage, minLength, EditTextFactory.MAX_LENGTH);
    }
}
