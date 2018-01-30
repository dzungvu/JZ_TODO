package com.project.mobile.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.project.mobile.adapter.TodoAdapter;
import com.project.mobile.helper.AutoAddItem;
import com.project.mobile.helper.ControlHelper;
import com.project.mobile.helper.RealmHelper;
import com.project.mobile.helper.TodoCommunication;
import com.project.mobile.model.TodoInformation;
import com.project.mobile.myassitant.R;
import com.project.mobile.utils.MyReceiver;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

import io.realm.Realm;

/**
 * Use to
 * Created by DzungVu on 8/8/2017.
 */

public class ToDoFragment extends Fragment {
    private MaterialCalendarView materialCalendarView;
    private MenuItem itemOutside;
    private RealmHelper realmHelper;
    private TodoAdapter todoAdapter;
    private RecyclerView rclReview;
    private ArrayList<TodoInformation> todoInformationArrayList;
    private ControlHelper controlHelper;
    private EventDecorator eventDecorator;

    //Auto add item to list
    private AutoAddItem autoAddItem;

    private AlarmManager alarmManager;
    private Intent intent;
    private PendingIntent pendingIntent;
    private ArrayList<TodoInformation>notificationStack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm);
        todoInformationArrayList = retrieveTodoList();
        notificationStack = new ArrayList<>();
        createNotificationList();
        Collection<CalendarDay> eventDates = new HashSet<>();
        for (TodoInformation information : todoInformationArrayList) {
            CalendarDay calDay = CalendarDay.from(information.getDate());
            eventDates.add(calDay);
        }
        eventDecorator = new EventDecorator(ContextCompat.getColor(getActivity(), R.color.color_event), eventDates);
        todoAdapter = new TodoAdapter(getActivity(), todoInformationArrayList, communication);
        //Auto add item to list data
        autoAddItem = new AutoAddItem();
        controlHelper = ControlHelper.getInstance();

        intent = new Intent(getActivity(), MyReceiver.class);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < notificationStack.size(); i++) {
            cal.setTime(notificationStack.get(i).getDate());
            intent.putExtra("TITLE", notificationStack.get(i).getTitle());
            intent.putExtra("MESSAGE", notificationStack.get(i).getDetail());
            pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
        }
    }

    public ArrayList<TodoInformation> retrieveTodoList() {
        return realmHelper.readTodoListFromRealm();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle(null);
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        materialCalendarView = view.findViewById(R.id.clv_review);
        materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
        materialCalendarView.setTopbarVisible(true);
        materialCalendarView.setDateSelected(materialCalendarView.getCurrentDate(), true);
        materialCalendarView.addDecorator(eventDecorator);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_todo_add);
        rclReview = view.findViewById(R.id.rcv_todo_list);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAddNew();
            }
        });
        todoInformationArrayList.clear();
        todoInformationArrayList.addAll(realmHelper.readTodoListFromRealm());
        Collections.sort(todoInformationArrayList, new todoObjectCompare());
        todoAdapter.notifyDataSetChanged();
        rclReview.setLayoutManager(new LinearLayoutManager(getActivity()));
        rclReview.setAdapter(todoAdapter);

        enableScrollListener(rclReview, todoInformationArrayList, materialCalendarView);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        itemOutside = menu.findItem(R.id.mnu_todo_filter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.to_do_action_bar, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnu_todo_setting:
                return true;
            case R.id.mnu_todo_search:
                return true;
            case R.id.mnu_todo_notification:
                return true;
            case R.id.mnu_todo_next:
                itemOutside.setTitle("Next");
                return true;
            case R.id.mnu_todo_pass:
                itemOutside.setTitle("Pass");
                return true;
            case R.id.mnu_todo_all:
                itemOutside.setTitle("All");
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNotificationList(){
        for (TodoInformation information : todoInformationArrayList){
            if (convertTime(information.getDate()) > System.currentTimeMillis()){
                notificationStack.add(information);
            }
        }
    }
// wait for the time
    private class todoObjectCompare implements Comparator<TodoInformation> {
        @Override
        public int compare(TodoInformation lhs, TodoInformation rhs) {
            return lhs.getDate().compareTo(rhs.getDate());
        }
    }

    private void enableScrollListener(RecyclerView recyclerView, ArrayList<TodoInformation> list, MaterialCalendarView view) {
        controlHelper.customScrollListener(recyclerView, list, view);
    }

    private long convertTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getTimeInMillis();
    }

    private void gotoAddNew() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ToDoAddNewFragment toDoAddNewFragment = new ToDoAddNewFragment();
        fragmentTransaction.replace(R.id.main_container, toDoAddNewFragment)
                .addToBackStack(null).commit();
    }

    private TodoCommunication communication = new TodoCommunication() {
        @Override
        public void transformTodoInfor(TodoInformation todoInformation) {
            ToDoAddNewFragment toDoAddNewFragment = ToDoAddNewFragment.newInstance(todoInformation);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container, toDoAddNewFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

    private class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }
}
