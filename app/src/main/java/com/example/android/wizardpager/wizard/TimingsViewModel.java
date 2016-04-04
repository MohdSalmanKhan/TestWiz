package com.example.android.wizardpager.wizard;

import com.wdullaer.materialdatetimepicker.time.PractoTimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

/**
 * Created by salman on 22/03/16.
 */
public class TimingsViewModel {
    private String mDay;
    private Timepoint mSession1Start;
    private Timepoint mSession1End;
    private Timepoint mSession2Start;
    private Timepoint mSession2End;

    public TimingsViewModel(TimingsViewModel model) {
        this.mDay = model.mDay;
        if (model.mSession1Start != null) {
            this.mSession1Start = new Timepoint(model.mSession1Start);
        }
        if (model.mSession1End != null) {
            this.mSession1End = new Timepoint(model.mSession1End);
        }
        if (model.mSession2Start != null) {
            this.mSession2Start = new Timepoint(model.mSession2Start);
        }
        if (model.mSession2End != null) {
            this.mSession2End = new Timepoint(model.mSession2End);
        }
    }

    public TimingsViewModel(String day) {
        this.mDay = day;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        this.mDay = day;
    }

    public Timepoint getSession1Start() {
        return mSession1Start;
    }

    public void setSession1Start(Timepoint session1From) {
        this.mSession1Start = session1From;
    }

    public Timepoint getSession1End() {
        return mSession1End;
    }

    public void setSession1End(Timepoint session1To) {
        this.mSession1End = session1To;
    }

    public Timepoint getSession2Start() {
        return mSession2Start;
    }

    public void setSession2Start(Timepoint session2From) {
        this.mSession2Start = session2From;
    }

    public Timepoint getSession2End() {
        return mSession2End;
    }

    public void setSession2End(Timepoint session2To) {
        this.mSession2End = session2To;
    }

    public ClinicTimings.Timing toClinicTiming() {
        ClinicTimings.Timing timing = new ClinicTimings.Timing();
        timing.session1StartTime = PractoTimePickerDialog.timepointToDisplayString(mSession1Start, true);
        timing.session1EndTime = PractoTimePickerDialog.timepointToDisplayString(mSession1End, true);
        timing.session2StartTime = PractoTimePickerDialog.timepointToDisplayString(mSession2Start, true);
        timing.session2EndTime = PractoTimePickerDialog.timepointToDisplayString(mSession2End, true);
        return timing;
    }

    public static TimingsViewModel fromClinicTiming(String day, ClinicTimings.Timing timings) {
        TimingsViewModel model = new TimingsViewModel(day);
        if (timings != null) {
            model.setSession1Start(PractoTimePickerDialog
                    .displayStringToTimepoint(timings.session1StartTime, true));
            model.setSession1End(PractoTimePickerDialog
                    .displayStringToTimepoint(timings.session1EndTime, true));
            model.setSession2Start(PractoTimePickerDialog
                    .displayStringToTimepoint(timings.session2StartTime, true));
            model.setSession2End(PractoTimePickerDialog
                    .displayStringToTimepoint(timings.session2EndTime, true));
        }
        return model;
    }
}