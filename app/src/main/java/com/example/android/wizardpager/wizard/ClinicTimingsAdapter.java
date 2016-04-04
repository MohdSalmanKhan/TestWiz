package com.example.android.wizardpager.wizard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.wizardpager.R;
import com.wdullaer.materialdatetimepicker.time.PractoTimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nishantshah on 10/03/16.
 */
public class ClinicTimingsAdapter extends RecyclerView.Adapter<ClinicTimingsAdapter.ViewHolder> {

    private static final int STATE_DIFFERENT_WEEKDAYS = 0;
    private static final int STATE_SAME_WEEKDAYS = 1;

    public ArrayList<TimingsViewModel> mDataSet;
    private int mWeekdaysToggleSwitchStatus = STATE_DIFFERENT_WEEKDAYS;

    private static final String MONDAY = "Mon";
    private static final String TUESDAY = "Tue";
    private static final String WEDNESDAY = "Wed";
    private static final String THURSDAY = "Thu";
    private static final String FRIDAY = "Fri";
    private static final String SATURDAY = "Sat";
    private static final String SUNDAY = "Sun";
    private static final String MONDAY_TO_FRIDAY = "Mon-Fri";
    public static final String[] DIFFERENT_WEEKDAYS = {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY};
    public static final String[] SAME_WEEKDAYS = {MONDAY_TO_FRIDAY, SATURDAY, SUNDAY};
    public static final String DAY = "day";

    private FragmentManager mFragmentManager;

    public ClinicTimingsAdapter(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        mDataSet = createTimings(DIFFERENT_WEEKDAYS);
    }

    public ClinicTimingsAdapter(FragmentManager fragmentManager, @NonNull ClinicTimings clinicTimings) {
        mFragmentManager = fragmentManager;
        mDataSet = createTimings(clinicTimings);
    }

    private ArrayList<TimingsViewModel> createTimings(String[] days) {
        ArrayList<TimingsViewModel> dataSet = new ArrayList<>(days.length);
        int length = days.length;
        for (int i = 0; i < length; i++) {
            dataSet.add(i, new TimingsViewModel(days[i]));
        }
        return dataSet;
    }

    private ArrayList<TimingsViewModel> createTimings(ClinicTimings clinicTimings) {
        ArrayList<TimingsViewModel> dataSet = new ArrayList<>();
        List<String> daysList = Arrays.asList(DIFFERENT_WEEKDAYS);
        for (String day : daysList) {
            switch (day) {
                case MONDAY:
                    dataSet.add(daysList.indexOf(day), TimingsViewModel
                            .fromClinicTiming(day, clinicTimings.mondayTimings));
                    break;
                case TUESDAY:
                    dataSet.add(daysList.indexOf(day), TimingsViewModel
                            .fromClinicTiming(day, clinicTimings.tuesdayTimings));
                    break;
                case WEDNESDAY:
                    dataSet.add(daysList.indexOf(day), TimingsViewModel
                            .fromClinicTiming(day, clinicTimings.wednesdayTimings));
                    break;
                case THURSDAY:
                    dataSet.add(daysList.indexOf(day), TimingsViewModel
                            .fromClinicTiming(day, clinicTimings.thursdayTimings));
                    break;
                case FRIDAY:
                    dataSet.add(daysList.indexOf(day), TimingsViewModel
                            .fromClinicTiming(day, clinicTimings.fridayTimings));
                    break;
                case SATURDAY:
                    dataSet.add(daysList.indexOf(day), TimingsViewModel
                            .fromClinicTiming(day, clinicTimings.saturdayTimings));
                    break;
                case SUNDAY:
                    dataSet.add(daysList.indexOf(day), TimingsViewModel
                            .fromClinicTiming(day, clinicTimings.sundayTimings));
                    break;
            }
        }
        return dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session_timings, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TimingsViewModel model = mDataSet.get(position);
        holder.position = position;
        holder.dayTextView.setText(model.getDay());
        holder.resetView();
        if (model.getSession1Start() != null) {
            holder.renderSession1Layout(model.getSession1Start(), model.getSession1End());
        }
        if (model.getSession2Start() != null) {
            holder.renderSession2Layout(model.getSession2Start(), model.getSession2End());
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public int position;
        public TextView dayTextView;
        public TextView addSessionTextView;
        public ViewGroup multiSessionLayout;
        public TextView session1StartTextView;
        public TextView session1EndTextView;
        public TextView addSession2TextView;
        public ViewGroup session2Layout;
        public TextView session2StartTextView;
        public TextView session2EndTextView;

        private ClinicSessionTimePickerDialog.OnSessionPickedListener mSession1Listener
                = new ClinicSessionTimePickerDialog.OnSessionPickedListener() {
            @Override
            public void onSessionPicked(Timepoint start, Timepoint end) {
                TimingsViewModel model = mDataSet.get(position);
                model.setSession1Start(start);
                model.setSession1End(end);
                renderSession1Layout(start, end);
            }
        };

        public void renderSession1Layout(Timepoint start, Timepoint end) {
            session1StartTextView.setText(PractoTimePickerDialog.timepointToDisplayString(start, false));
            session1EndTextView.setText(PractoTimePickerDialog.timepointToDisplayString(end, false));
            addSessionTextView.setVisibility(View.GONE);
            multiSessionLayout.setVisibility(View.VISIBLE);
        }

        private ClinicSessionTimePickerDialog.OnSessionPickedListener mSession2Listener
                = new ClinicSessionTimePickerDialog.OnSessionPickedListener() {
            @Override
            public void onSessionPicked(Timepoint start, Timepoint end) {
                TimingsViewModel model = mDataSet.get(position);
                model.setSession2Start(start);
                model.setSession2End(end);
                renderSession2Layout(start, end);
            }
        };

        public void renderSession2Layout(Timepoint start, Timepoint end) {
            session2StartTextView.setText(PractoTimePickerDialog.timepointToDisplayString(start, false));
            session2EndTextView.setText(PractoTimePickerDialog.timepointToDisplayString(end, false));
            addSession2TextView.setVisibility(View.GONE);
            session2Layout.setVisibility(View.VISIBLE);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            dayTextView = (TextView) itemView.findViewById(R.id.text_view_day);
            addSessionTextView = (TextView) itemView.findViewById(R.id.text_view_add_session);
            addSessionTextView.setOnClickListener(this);
            multiSessionLayout = (ViewGroup) itemView.findViewById(R.id.layout_multi_session);
            session1StartTextView = (TextView) itemView.findViewById(R.id.text_view_start_session_1);
            session1EndTextView = (TextView) itemView.findViewById(R.id.text_view_end_session_1);
            addSession2TextView = (TextView) itemView.findViewById(R.id.text_view_add_session_2);
            addSession2TextView.setOnClickListener(this);
            session2Layout = (ViewGroup) itemView.findViewById(R.id.layout_session_2);
            session2StartTextView = (TextView) itemView.findViewById(R.id.text_view_start_session_2);
            session2EndTextView = (TextView) itemView.findViewById(R.id.text_view_end_session_2);
            itemView.findViewById(R.id.layout_timings_1).setOnClickListener(this);
            itemView.findViewById(R.id.layout_timings_2).setOnClickListener(this);
            itemView.findViewById(R.id.image_view_discard_session_1).setOnClickListener(this);
            itemView.findViewById(R.id.image_view_discard_session_2).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text_view_add_session: {
                    ClinicSessionTimePickerDialog dialogFragment = ClinicSessionTimePickerDialog
                            .newSession1Instance(mSession1Listener, null, false);
                    dialogFragment.show(mFragmentManager, DAY);
                    break;
                }
                case R.id.text_view_add_session_2: {
                    TimingsViewModel model = mDataSet.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ClinicSessionTimePickerDialog.EXTRA_SESSION_1_START, model.getSession1Start());
                    bundle.putParcelable(ClinicSessionTimePickerDialog.EXTRA_SESSION_1_END, model.getSession1End());
                    ClinicSessionTimePickerDialog dialogFragment = ClinicSessionTimePickerDialog
                            .newSession2Instance(mSession2Listener, bundle, false);
                    dialogFragment.show(mFragmentManager, DAY);
                    break;
                }
                case R.id.image_view_discard_session_1: {
                    TimingsViewModel model = mDataSet.get(position);
                    if (session2Layout.getVisibility() == View.VISIBLE) {
                        session1StartTextView.setText(session2StartTextView.getText());
                        session1EndTextView.setText(session2EndTextView.getText());
                        session2Layout.setVisibility(View.GONE);
                        addSession2TextView.setVisibility(View.VISIBLE);
                        model.setSession1Start(model.getSession2Start());
                        model.setSession1End(model.getSession2End());
                        model.setSession2Start(null);
                        model.setSession2End(null);
                    } else {
                        resetView();
                        model.setSession1Start(null);
                        model.setSession1End(null);
                    }
                    break;
                }
                case R.id.image_view_discard_session_2: {
                    TimingsViewModel model = mDataSet.get(position);
                    model.setSession2Start(null);
                    model.setSession2End(null);
                    session2Layout.setVisibility(View.GONE);
                    addSession2TextView.setVisibility(View.VISIBLE);
                    break;
                }
                case R.id.layout_timings_1: {
                    Bundle bundle = new Bundle();
                    TimingsViewModel model = mDataSet.get(position);
                    bundle.putParcelable(ClinicSessionTimePickerDialog.EXTRA_START, model.getSession1Start());
                    bundle.putParcelable(ClinicSessionTimePickerDialog.EXTRA_END, model.getSession1End());
                    ClinicSessionTimePickerDialog dialogFragment = ClinicSessionTimePickerDialog
                            .newSession1Instance(mSession1Listener, bundle, false);
                    dialogFragment.show(mFragmentManager, DAY);
                    break;
                }
                case R.id.layout_timings_2: {
                    Bundle bundle = new Bundle();
                    TimingsViewModel model = mDataSet.get(position);
                    bundle.putParcelable(ClinicSessionTimePickerDialog.EXTRA_START, model.getSession2Start());
                    bundle.putParcelable(ClinicSessionTimePickerDialog.EXTRA_END, model.getSession2End());
                    bundle.putParcelable(ClinicSessionTimePickerDialog.EXTRA_SESSION_1_START, model.getSession1Start());
                    bundle.putParcelable(ClinicSessionTimePickerDialog.EXTRA_SESSION_1_END, model.getSession1End());
                    ClinicSessionTimePickerDialog dialogFragment = ClinicSessionTimePickerDialog
                            .newSession2Instance(mSession2Listener, bundle, false);
                    dialogFragment.show(mFragmentManager, DAY);
                    break;
                }
            }
        }

        public void resetView() {
            multiSessionLayout.setVisibility(View.GONE);
            addSessionTextView.setVisibility(View.VISIBLE);
        }
    }

    public void toggleDays(boolean keepWeekdaysSame) {
        if (keepWeekdaysSame && mWeekdaysToggleSwitchStatus != STATE_SAME_WEEKDAYS) {
            mWeekdaysToggleSwitchStatus = STATE_SAME_WEEKDAYS;
            TimingsViewModel mondayModel = mDataSet.get(0);
            mondayModel.setDay(SAME_WEEKDAYS[0]);
            for (int i = 0; i < 5; i++) {
                mDataSet.remove(0);
            }
            mDataSet.add(0, mondayModel);
            notifyDataSetChanged();
        } else if (mWeekdaysToggleSwitchStatus != STATE_DIFFERENT_WEEKDAYS) {
            mWeekdaysToggleSwitchStatus = STATE_DIFFERENT_WEEKDAYS;
            TimingsViewModel mondayModel = mDataSet.get(0);
            mDataSet.remove(0);
            for (int i = 0; i < 5; i++) {
                TimingsViewModel model = new TimingsViewModel(mondayModel);
                model.setDay(DIFFERENT_WEEKDAYS[i]);
                mDataSet.add(i, model);
            }
            notifyDataSetChanged();
        }
    }

    public ClinicTimings getClinicTimings() {
        ClinicTimings clinicTimings = new ClinicTimings();
        for (TimingsViewModel model : mDataSet) {
            switch (model.getDay()) {
                case MONDAY:
                    clinicTimings.mondayTimings = model.toClinicTiming();
                    break;
                case TUESDAY:
                    clinicTimings.tuesdayTimings = model.toClinicTiming();
                    break;
                case WEDNESDAY:
                    clinicTimings.wednesdayTimings = model.toClinicTiming();
                    break;
                case THURSDAY:
                    clinicTimings.thursdayTimings = model.toClinicTiming();
                    break;
                case FRIDAY:
                    clinicTimings.fridayTimings = model.toClinicTiming();
                    break;
                case SATURDAY:
                    clinicTimings.saturdayTimings = model.toClinicTiming();
                    break;
                case SUNDAY:
                    clinicTimings.sundayTimings = model.toClinicTiming();
                    break;
                case MONDAY_TO_FRIDAY:
                    ClinicTimings.Timing timing = model.toClinicTiming();
                    clinicTimings.mondayTimings = timing;
                    clinicTimings.tuesdayTimings = timing;
                    clinicTimings.wednesdayTimings = timing;
                    clinicTimings.thursdayTimings = timing;
                    clinicTimings.fridayTimings = timing;
                    break;
            }
        }
        return clinicTimings;
    }

}