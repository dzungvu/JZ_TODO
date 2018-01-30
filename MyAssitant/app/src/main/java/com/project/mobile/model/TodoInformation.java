package com.project.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Use to
 * Created by DzungVu on 8/12/2017.
 */

public class TodoInformation extends RealmObject implements Parcelable{
    @PrimaryKey
    private String tag;
    private String title;
    private String day;
    private String time;
    private String detail;
    private int repeat;
    private int label;
    private Date date;

    public TodoInformation() {
    }

    protected TodoInformation(Parcel in) {
        tag = in.readString();
        title = in.readString();
        day = in.readString();
        time = in.readString();
        detail = in.readString();
        repeat = in.readInt();
        label = in.readInt();
    }

    public static final Creator<TodoInformation> CREATOR = new Creator<TodoInformation>() {
        @Override
        public TodoInformation createFromParcel(Parcel in) {
            return new TodoInformation(in);
        }

        @Override
        public TodoInformation[] newArray(int size) {
            return new TodoInformation[size];
        }
    };

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);
        parcel.writeString(title);
        parcel.writeString(day);
        parcel.writeString(time);
        parcel.writeString(detail);
        parcel.writeInt(repeat);
        parcel.writeInt(label);
    }

//    public void Date setDate(String day, String time) throws ParseException {
//        SimpleDateFormat spdf = new SimpleDateFormat("MMM/dd/yyyy - HH:mm", Locale.US);
//        String tiedUp = day + " - " + time;
//        return spdf.parse(tiedUp);
//    }

}
