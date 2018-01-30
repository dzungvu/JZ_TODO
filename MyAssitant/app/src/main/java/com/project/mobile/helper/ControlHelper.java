package com.project.mobile.helper;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.project.mobile.model.TodoInformation;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Use to
 * Created by DzungVu on 8/19/2017.
 */

public class ControlHelper {
    public static ControlHelper getInstance() {
        return new ControlHelper();
    }

    public static void hideKeyBoard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private static void focusDate(MaterialCalendarView view, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long time = cal.getTimeInMillis() + 604800000;
        cal.setTimeInMillis(time);
        view.setCurrentDate(cal);
    }

    public void customScrollListener(RecyclerView recyclerView
            , final ArrayList<TodoInformation> list, final MaterialCalendarView view) {

        final LinearLayoutManager manager;
        final TodoInformation[] firstItemCurrentDisplay = {new TodoInformation()};
        manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (list.size() > 0 && manager.findFirstVisibleItemPosition() != -1) {
                    firstItemCurrentDisplay[0] = list
                            .get(manager.findFirstVisibleItemPosition());
                    if (firstItemCurrentDisplay[0] != null) {
                        Date date = firstItemCurrentDisplay[0].getDate();
                        focusDate(view, date);
                        view.setDateSelected(view.getSelectedDates().get(0), false);
                        view.setDateSelected(list
                                        .get(manager
                                                .findFirstVisibleItemPosition())
                                        .getDate()
                                , true);
                    }
                    Log.i("Position: ", "" + manager.findFirstVisibleItemPosition());
                }
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);
    }
}
