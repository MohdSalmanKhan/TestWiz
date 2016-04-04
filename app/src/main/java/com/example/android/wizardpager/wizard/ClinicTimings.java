package com.example.android.wizardpager.wizard;


import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by nishantshah on 18/03/16.
 */
public class ClinicTimings implements Parcelable {

    public Timing mondayTimings;
    public Timing tuesdayTimings;
    public Timing wednesdayTimings;
    public Timing thursdayTimings;
    public Timing fridayTimings;
    public Timing saturdayTimings;
    public Timing sundayTimings;

    public ClinicTimings() {
    }

    protected ClinicTimings(Parcel in) {
        mondayTimings = in.readParcelable(Timing.class.getClassLoader());
        tuesdayTimings = in.readParcelable(Timing.class.getClassLoader());
        wednesdayTimings = in.readParcelable(Timing.class.getClassLoader());
        thursdayTimings = in.readParcelable(Timing.class.getClassLoader());
        fridayTimings = in.readParcelable(Timing.class.getClassLoader());
        saturdayTimings = in.readParcelable(Timing.class.getClassLoader());
        sundayTimings = in.readParcelable(Timing.class.getClassLoader());
    }

    public static final Creator<ClinicTimings> CREATOR = new Creator<ClinicTimings>() {
        @Override
        public ClinicTimings createFromParcel(Parcel in) {
            return new ClinicTimings(in);
        }

        @Override
        public ClinicTimings[] newArray(int size) {
            return new ClinicTimings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mondayTimings, flags);
        dest.writeParcelable(tuesdayTimings, flags);
        dest.writeParcelable(wednesdayTimings, flags);
        dest.writeParcelable(thursdayTimings, flags);
        dest.writeParcelable(fridayTimings, flags);
        dest.writeParcelable(saturdayTimings, flags);
        dest.writeParcelable(sundayTimings, flags);
    }

    public static class Timing implements Parcelable {
        public String session1StartTime;
        public String session1EndTime;
        public String session2StartTime;
        public String session2EndTime;

        public Timing() {
        }

        protected Timing(Parcel in) {
            session1StartTime = in.readString();
            session1EndTime = in.readString();
            session2StartTime = in.readString();
            session2EndTime = in.readString();
        }

        public static final Creator<Timing> CREATOR = new Creator<Timing>() {
            @Override
            public Timing createFromParcel(Parcel in) {
                return new Timing(in);
            }

            @Override
            public Timing[] newArray(int size) {
                return new Timing[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(session1StartTime);
            dest.writeString(session1EndTime);
            dest.writeString(session2StartTime);
            dest.writeString(session2EndTime);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || !Timing.class.isInstance(o)) {
                return false;
            }
            Timing timing = (Timing) o;
            return this.session1StartTime.equalsIgnoreCase(timing.session1StartTime)
                    && this.session1EndTime.equalsIgnoreCase(timing.session1EndTime)
                    && this.session2StartTime.equalsIgnoreCase(timing.session2StartTime)
                    && this.session2EndTime.equalsIgnoreCase(timing.session2EndTime);
        }
    }
}