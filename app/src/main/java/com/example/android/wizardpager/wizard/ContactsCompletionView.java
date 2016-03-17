package com.example.android.wizardpager.wizard;

/**
 * Created by salman on 15/03/16.
 */

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.wizardpager.R;
import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Sample token completion view for basic contact info
 *
 * Created on 9/12/13.
 * @author mgod
 */
public class ContactsCompletionView extends TokenCompleteTextView<String> {

    public ContactsCompletionView(Context context) {
        super(context);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(String person) {

        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.contact_token, (ViewGroup)ContactsCompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.name)).setText(person);

        return view;
    }

    @Override
    protected String defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        int index = completionText.indexOf('@');
        if (index == -1) {
            return completionText;
        } else {
            return completionText;
        }
    }
}