package com.project.mobile.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.project.mobile.helper.ControlHelper;
import com.project.mobile.model.TodoInformation;
import com.project.mobile.myassitant.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import io.realm.Realm;

/**
 * Use to
 * Created by DzungVu on 8/12/2017.
 */

public class ToDoAddNewFragment extends Fragment {
    public static ToDoAddNewFragment newInstance(TodoInformation todoInformation) {
        ToDoAddNewFragment fragment = new ToDoAddNewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("TodoInformation", todoInformation);
        fragment.setArguments(bundle);
        return fragment;
    }

    private EditText edtTitle;
    private EditText edtDetail;
    private LinearLayout llTodoDay;
    private LinearLayout llTodoTime;
    private TextView tvDay;
    private TextView tvTime;
    private Spinner spRepeat;
    private Spinner spLabel;
    private Button btnCancel;
    private Button btnDone;

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormatDay;
    private SimpleDateFormat simpleDateFormatTime;

    private Realm realm;
    private TodoInformation todoInformation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        Bundle bundle = new Bundle();
        if (getArguments() != null) {
            todoInformation = getArguments().getParcelable("TodoInformation");
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_todo_add_new, container, false);
        edtTitle = view.findViewById(R.id.edt_todo_title);
        edtDetail = view.findViewById(R.id.edt_todo_detail);
        llTodoDay = view.findViewById(R.id.ll_todo_day);
        llTodoTime = view.findViewById(R.id.ll_todo_time);
        spLabel = view.findViewById(R.id.sp_todo_label);
        spRepeat = view.findViewById(R.id.sp_todo_repeat);
        btnCancel = view.findViewById(R.id.btn_todo_cancel);
        btnDone = view.findViewById(R.id.btn_todo_done);
        tvDay = view.findViewById(R.id.edt_todo_add_day);
        tvTime = view.findViewById(R.id.edt_todo_add_time);

        edtTitle.addTextChangedListener(new MyTextWatcher(edtTitle));
        tvDay.addTextChangedListener(new MyTextWatcher(tvDay));
        tvTime.addTextChangedListener(new MyTextWatcher(tvTime));

        calendar = Calendar.getInstance();
        simpleDateFormatDay = new SimpleDateFormat("MMM/dd/yyyy");
        simpleDateFormatTime = new SimpleDateFormat("HH:mm");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llTodoDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSelectDay();
            }
        });

        llTodoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSelectTime();
            }
        });

        ArrayAdapter<CharSequence> repeatAdapter = ArrayAdapter
                .createFromResource(getActivity()
                        , R.array.todo_repeat
                        , android.R.layout.simple_spinner_item);

        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRepeat.setAdapter(repeatAdapter);

        ArrayAdapter<CharSequence> labelAdapter = ArrayAdapter
                .createFromResource(getActivity()
                        , R.array.todo_label
                        , android.R.layout.simple_spinner_item);

        labelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLabel.setAdapter(labelAdapter);

        if (todoInformation != null) {
            edtTitle.setText(todoInformation.getTitle());
            tvDay.setText(todoInformation.getDay());
            tvTime.setText(todoInformation.getTime());
            edtDetail.setText(todoInformation.getDetail());
            spLabel.setSelection(todoInformation.getLabel());
            spRepeat.setSelection(todoInformation.getRepeat());
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToDatabase();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.todo_add_new, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_todo_addnew_done:
                addToDatabase();
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoSelectDay() {
        final DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tvDay.setText(simpleDateFormatDay.format(calendar.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity()
                , callback
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void gotoSelectTime() {
        TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                tvTime.setText(simpleDateFormatTime.format(calendar.getTime()));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity()
                , callback
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                , true);
        timePickerDialog.show();
    }

    private void addToDatabase() {
        if (!validTitle()){
            return;
        }
        if (!validDay()){
            return;
        }
        if (!validTime()){
            return;
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TodoInformation todoInformationNew = new TodoInformation();
                if (todoInformation != null) {
                    todoInformationNew.setTag(todoInformation.getTag());
                } else {
                    todoInformationNew.setTag(createTag());
                }
                todoInformationNew.setTitle(edtTitle.getText().toString());
                todoInformationNew.setDay(tvDay.getText().toString());
                todoInformationNew.setTime(tvTime.getText().toString());
                todoInformationNew.setDetail(edtDetail.getText().toString());
                todoInformationNew.setRepeat(spRepeat.getSelectedItemPosition());
                todoInformationNew.setLabel(spLabel.getSelectedItemPosition());
                try {
                    todoInformationNew.setDate(setDate(tvDay.getText().toString(), tvTime.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                realm.insertOrUpdate(todoInformationNew);
            }
        });
        if (todoInformation != null) {
            Toast.makeText(getActivity(), "Updated successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Inserted successfully", Toast.LENGTH_LONG).show();
        }
        getActivity().onBackPressed();
    }

    public Date setDate(String day, String time) throws ParseException {
        SimpleDateFormat spdf = new SimpleDateFormat("MMM/dd/yyyy - HH:mm", Locale.US);
        String tiedUp = day + " - " + time;
        return spdf.parse(tiedUp);
    }

    public static String createTag() {
        StringBuilder stringBuilder = new StringBuilder();
        Random rnd = new Random();
        while (stringBuilder.length() < 10) {
            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            stringBuilder.append(SALTCHARS.charAt(index));
        }
        return stringBuilder.toString();
    }

    private boolean validTitle() {
        if (edtTitle.getText().toString().trim().isEmpty()){
            edtTitle.setError("Title is empty");
            edtTitle.requestFocus();
            return false;
        }else {
            edtTitle.setError(null);
            return true;
        }
    }
    private boolean validDay(){
        if (tvDay.getText().toString().isEmpty()){
            tvDay.setError("Day is empty");
            return false;
        }else {
            tvDay.setError(null);
            return true;
        }
    }
    private boolean validTime(){
        if (tvTime.getText().toString().isEmpty()){
            tvTime.setError("Time is empty");
            return false;
        }else {
            tvTime.setError(null);
            return true;
        }
    }

    private class MyTextWatcher implements TextWatcher{
        private View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edt_todo_title:
                    validTitle();
                    break;
                case R.id.edt_todo_add_day:
                    validDay();
                    break;
                case R.id.edt_todo_add_time:
                    validTime();
                    break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ControlHelper.hideKeyBoard(getActivity());
    }
}
