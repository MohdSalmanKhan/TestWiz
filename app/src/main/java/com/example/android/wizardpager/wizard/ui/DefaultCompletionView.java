package com.example.android.wizardpager.wizard.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.ContactsCompletionView;
import com.tokenautocomplete.TokenCompleteTextView;



/**
 * Created by AjayPracto on 28/05/15.
 */
public class DefaultCompletionView extends TokenCompleteTextView<String> {

    public DefaultCompletionView(Context context) {
        super(context);
        setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);
    }

    public DefaultCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);
    }

    public DefaultCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_COMMA) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected View getViewForObject(String object) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.contact_token, (ViewGroup)DefaultCompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.name)).setText(object);

        return view;
    }

    @Override
    protected String defaultObject(String s) {
        return s;
    }
}

