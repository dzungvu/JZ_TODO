package com.project.mobile.helper;

import com.project.mobile.fragment.ToDoAddNewFragment;
import com.project.mobile.model.TodoInformation;

import java.util.Date;
import java.util.Random;

import io.realm.Realm;

/**
 * Use to
 * Created by DzungVu on 8/23/2017.
 */

public class AutoAddItem {
    private Realm realm;
    private RealmHelper realmHelper;
    private TodoInformation todoInformation;

    public void add() {
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm);
        realmHelper.addNewObject(genarateObject());
    }

    private TodoInformation genarateObject() {
//        String[] list = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] list = {"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] listDay = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
                , "13", "14", "15", "16", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27"
                , "28"};
        String[] listHour = {"00","01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "18", "19", "20", "21", "22", "23"};
        String[] listMinute = {"00","01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"
                , "12", "13", "14", "15", "16", "18", "19", "20", "21", "22", "23", "24", "25","26"
                , "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37"
                , "38", "39", "40", "41", "42", "43", "44", "45", "46", "47"};
        Random random = new Random();
        todoInformation = new TodoInformation();
        todoInformation.setDate(new Date());
        todoInformation.setTitle(ToDoAddNewFragment.createTag());
        todoInformation.setTag(ToDoAddNewFragment.createTag());
        String month = list[random.nextInt(list.length)];
        String day = listDay[random.nextInt(listDay.length)];
        String year = String.valueOf(2017);
        String hour = listHour[random.nextInt(listHour.length)];
        String minute = listMinute[random.nextInt(listMinute.length)];
        todoInformation.setDay(String.format("%s/%s/%s", month, day, year));
        todoInformation.setTime(String.format("%s:%s", hour, minute));
        todoInformation.setLabel(random.nextInt(4));
        todoInformation.setRepeat(random.nextInt(5));
        return todoInformation;
    }
}
