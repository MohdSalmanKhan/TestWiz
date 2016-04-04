package com.example.android.wizardpager.wizard;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.wizardpager.R;
import com.wdullaer.materialdatetimepicker.time.PractoTimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

/**
 * Created by nishantshah on 13/03/16.
 */
public class ClinicSessionTimePickerDialog extends PractoTimePickerDialog implements
        View.OnClickListener {

    public static final String EXTRA_START = "start";
    public static final String EXTRA_END = "end";
    public static final String EXTRA_SESSION_1_START = "session_1_start";
    public static final String EXTRA_SESSION_1_END = "session_1_end";
    private TextView mStartTextView;
    private TextView mStartTimeTextView;
    private TextView mEndTextView;
    private TextView mEndTimeTextView;
    private TextView mToastTextView;
    private Timepoint mStartTime;
    private Timepoint mEndTime;
    private Timepoint mSession1StartTime;
    private Timepoint mSession1EndTime;
    private boolean isInStartMode = true;
    private OnSessionPickedListener mListener;
    private static final int DEFAULT_SESSION_1_START_HOUR = 9;
    private static final int DEFAULT_SESSION_1_END_HOUR = 13;
    private static final int DEFAULT_SESSION_2_START_HOUR = 17;
    private static final int DEFAULT_SESSION_2_END_HOUR = 21;

    public interface OnSessionPickedListener {
        void onSessionPicked(Timepoint start, Timepoint end);
    }

    public static ClinicSessionTimePickerDialog newSession1Instance(OnSessionPickedListener callback,
                                                                    Bundle extras, boolean is24HourMode) {
        ClinicSessionTimePickerDialog dialog = new ClinicSessionTimePickerDialog();
        if (extras == null) {
            dialog.setStartTime(new Timepoint(DEFAULT_SESSION_1_START_HOUR, 0));
            dialog.setEndTime(new Timepoint(DEFAULT_SESSION_1_END_HOUR, 0));
        } else {
            Timepoint startTimepoint = extras.getParcelable(EXTRA_START);
            if (startTimepoint == null) {
                startTimepoint = new Timepoint(DEFAULT_SESSION_1_START_HOUR, 0);
            }
            dialog.setStartTime(startTimepoint);

            Timepoint endTimepoint = extras.getParcelable(EXTRA_END);
            if (endTimepoint == null) {
                endTimepoint = new Timepoint(DEFAULT_SESSION_1_END_HOUR, 0);
            }
            dialog.setEndTime(endTimepoint);
        }
        dialog.initialize(null, dialog.mStartTime.getHour(), dialog.mStartTime.getMinute(),
                dialog.mStartTime.getSecond(), is24HourMode);
        dialog.setTimeInterval(1, 5);
        dialog.mListener = callback;
        return dialog;
    }

    public static ClinicSessionTimePickerDialog newSession2Instance(OnSessionPickedListener callback,
                                                                    Bundle extras, boolean is24HourMode) {
        ClinicSessionTimePickerDialog dialog = new ClinicSessionTimePickerDialog();
        if (extras == null) {
            dialog.setStartTime(new Timepoint(DEFAULT_SESSION_2_START_HOUR, 0));
            dialog.setEndTime(new Timepoint(DEFAULT_SESSION_2_END_HOUR, 0));
        } else {
            Timepoint startTimepoint = extras.getParcelable(EXTRA_START);
            if (startTimepoint == null) {
                startTimepoint = new Timepoint(DEFAULT_SESSION_2_START_HOUR, 0);
            }
            dialog.setStartTime(startTimepoint);

            Timepoint endTimepoint = extras.getParcelable(EXTRA_END);
            if (endTimepoint == null) {
                endTimepoint = new Timepoint(DEFAULT_SESSION_2_END_HOUR, 0);
            }
            dialog.setEndTime(endTimepoint);

            dialog.mSession1StartTime = extras.getParcelable(EXTRA_SESSION_1_START);
            dialog.mSession1EndTime = extras.getParcelable(EXTRA_SESSION_1_END);
        }
        dialog.initialize(null, dialog.mStartTime.getHour(), dialog.mStartTime.getMinute(),
                dialog.mStartTime.getSecond(), is24HourMode);
        dialog.setTimeInterval(1, 5);
        dialog.mListener = callback;
        return dialog;
    }

    private void setStartTime(Timepoint timepoint) {
        mStartTime = new Timepoint(timepoint);
    }

    private void setEndTime(Timepoint timepoint) {
        mEndTime = new Timepoint(timepoint);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clinic_session_time_picker, container, false);
        initView(view, savedInstanceState);
        view.findViewById(com.wdullaer.materialdatetimepicker.R.id.time_picker_dialog)
                .setOnKeyListener(null);
        mTimePicker.setOnKeyListener(null);
        view.findViewById(R.id.layout_start).setOnClickListener(this);
        view.findViewById(R.id.layout_end).setOnClickListener(this);
        mStartTextView = (TextView) view.findViewById(R.id.text_view_start);
        mStartTimeTextView = (TextView) view.findViewById(R.id.text_view_start_time);
        mEndTextView = (TextView) view.findViewById(R.id.text_view_end);
        mEndTimeTextView = (TextView) view.findViewById(R.id.text_view_end_time);
        mToastTextView = (TextView) view.findViewById(R.id.toast_text_view);
        mOkButton.setText(R.string.next);

        if (mStartTime == null) {
            mStartTime = new Timepoint(mInitialTime.getHour(), mInitialTime.getMinute(), 0);
        }
        if (mEndTime == null) {
            mEndTime = new Timepoint(mInitialTime.getHour(), mInitialTime.getMinute(), 0);
        }
        mStartTimeTextView.setText(timepointToDisplayString(mStartTime, mIs24HourMode));
        mEndTimeTextView.setText(timepointToDisplayString(mEndTime, mIs24HourMode));
        return view;
    }

    @Override
    public void onValueSelected(Timepoint newValue) {
        mToastTextView.setVisibility(View.GONE);
        setHour(newValue.getHour(), false);
        mTimePicker.setContentDescription(mHourPickerDescription + ": " + newValue.getHour());
        setMinute(newValue.getMinute());
        mTimePicker.setContentDescription(mMinutePickerDescription + ": " + newValue.getMinute());
        setSecond(newValue.getSecond());
        mTimePicker.setContentDescription(mSecondPickerDescription + ": " + newValue.getSecond());
        mTimePicker.updateAmPmView(newValue.isAM() ? AM : PM);
        if (!mIs24HourMode) {
            updateAmPmDisplay(newValue.isAM() ? AM : PM);
        }
        if (isInStartMode) {
            mStartTime = newValue;
            mStartTimeTextView.setText(timepointToDisplayString(newValue, mIs24HourMode));
        } else {
            mEndTime = newValue;
            mEndTimeTextView.setText(timepointToDisplayString(newValue, mIs24HourMode));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_start:
                isInStartMode = true;
                Context context = getContext();
                Resources resources = getResources();
                mStartTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
                mStartTimeTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
                mEndTextView.setTextColor(ContextCompat.getColor(context, R.color.grey));
                mEndTimeTextView.setTextColor(ContextCompat.getColor(context, R.color.grey));
                mOkButton.setText(R.string.next);
                setCurrentItemShowing(HOUR_INDEX, true, true, true);
                mTimePicker.setTime(mStartTime);
                onValueSelected(mStartTime);
                break;
            case R.id.layout_end:
                switchToModeTo();
                break;
            case com.wdullaer.materialdatetimepicker.R.id.ok:
                if (isInStartMode) {
                    switchToModeTo();
                } else {
                    int comparison = mEndTime.compareTo(mStartTime);
                    if (comparison == 0) {
                        showError(R.string.start_end_same_time);
                    } else if (comparison < 0) {
                        showError(R.string.start_end_greater_time);
                    } else if (isOverlappingWithOtherSession(mStartTime)) {
                        showError(R.string.start_session2_error);
                    } else if (isOverlappingWithOtherSession(mEndTime)) {
                        showError(R.string.end_session2_error);
                    } else {
                        mToastTextView.setVisibility(View.GONE);
                        if (mListener != null) {
                            mListener.onSessionPicked(mStartTime, mEndTime);
                        }
                        dismiss();
                    }
                }
                break;
        }
    }

    private void showError(int resId) {
        mToastTextView.setText(resId);
        mToastTextView.setVisibility(View.VISIBLE);
    }

    private boolean isOverlappingWithOtherSession(Timepoint timepoint) {
        return !(mSession1StartTime == null || mSession1EndTime == null)
                && timepoint.compareTo(mSession1StartTime) >= 0
                && timepoint.compareTo(mSession1EndTime) <= 0;
    }

    private void switchToModeTo() {



        isInStartMode = false;
        Context context = getContext();
        Resources resources = getResources();
        mStartTextView.setTextColor(ContextCompat.getColor(context, R.color.grey));
        mStartTimeTextView.setTextColor(ContextCompat.getColor(context, R.color.grey));
        mEndTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
        mEndTimeTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
        mOkButton.setText(R.string.mdtp_ok);
        setCurrentItemShowing(HOUR_INDEX, true, true, true);
        mTimePicker.setTime(mEndTime);
        onValueSelected(mEndTime);
    }
}