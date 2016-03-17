package com.example.android.wizardpager.wizard;

import com.rengwuxian.materialedittext.validation.METValidator;

/**
 * Created by salman on 08/03/16.
 */
public class LengthValidator extends METValidator {

    int minLength = 0;
    int maxLength = Integer.MAX_VALUE;
    int fixedLength = 10;

    public LengthValidator(String errorMessage, int fixedLength){
        super(errorMessage);
        this.fixedLength = fixedLength;
    }

    public LengthValidator(String errorMessage, int minLength, int maxLength) {
        super(errorMessage);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public boolean isValid(CharSequence charSequence, boolean isEmpty) {
        if(!isEmpty) {
            if(charSequence.length() >= minLength && charSequence.length() <= maxLength) {
                return true;
            }
        }
        return false;
    }
}
