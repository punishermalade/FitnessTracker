package com.punisher.fitnesstracker;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.punisher.fitnesstracker.dialog.ActivityDistance;
import com.punisher.fitnesstracker.dialog.ActivityDurationFragment;
import com.punisher.fitnesstracker.dialog.ActivityTypeDialog;
import com.punisher.fitnesstracker.dto.FitnessActivity;
import com.punisher.fitnesstracker.util.DistanceUtil;
import com.punisher.fitnesstracker.util.TimeUtil;
import com.punisher.fitnesstracker.validator.FitnessValidator;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import com.punisher.fitnesstracker.database.DatabaseManager;

public class AddNewFitnessActivity extends AppCompatActivity implements
                        ActivityTypeDialog.ActivityTypeListener,
                        ActivityDurationFragment.ActivityDurationListener,
                        ActivityDistance.ActivityDistanceListener {

    private static final int DIALOG_ADD_DATE = 400;
    private static final int DIALOG_ADD_TIME = 500;
    private static final int DIALOG_ADD_ACT_TYPE = 600;
    private static final int DIALOG_ADD_ACT_DURATION = 700;
    private static final int DIALOG_ADD_ACT_DISTANCE = 800;

    private SimpleDateFormat _dateFormat = null;
    private SimpleDateFormat _timeFormat = null;
    private Button _btnSetDate = null;
    private Date _currentDate = null;
    private Button _btnSetTime = null;
    private Button _btnSetActivityType = null;
    private Button _btnSetDuration = null;
    private Button _btnSetDistance = null;
    private Button _btnCancel = null;
    private Button _btnOk = null;

    private FitnessActivity _currentFitness = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_fitness);

        // creating a new DTO to hold information
        _currentFitness = new FitnessActivity();

        // set the dateFormat objects and the current time
        _dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        _timeFormat = new SimpleDateFormat("hh:mm");
        _currentDate = new Date(System.currentTimeMillis());

        // set the date button and text with the date
        _btnSetDate = (Button)findViewById(R.id.txt_add_date);
        _btnSetDate.setText(_dateFormat.format(_currentDate));
        _btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ADD_DATE);
            }
        });

        // set the time button and text with the current time
        _btnSetTime = (Button)findViewById(R.id.txt_add_time);
        _btnSetTime.setText(_timeFormat.format(_currentDate));
        _btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ADD_TIME);
            }
        });

        _btnSetActivityType = (Button)findViewById(R.id.btn_add_act_type);
        _btnSetActivityType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ADD_ACT_TYPE);
            }
        });

        _btnSetDuration = (Button)findViewById(R.id.btn_add_act_duration);
        _btnSetDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ADD_ACT_DURATION);
            }
        });

        _btnSetDistance = (Button)findViewById(R.id.btn_add_act_distance);
        _btnSetDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ADD_ACT_DISTANCE);
            }
        });

        _btnCancel = (Button)findViewById(R.id.btn_add_activity_cancel);
        _btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _btnOk = (Button)findViewById(R.id.btn_add_activity_ok);
        _btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FitnessValidator validator = new FitnessValidator();
                validator.validate(_currentFitness);

                if (validator.hasErrors()) {
                    Toast.makeText(v.getContext(), getString(R.string.errors_validation_failed), Toast.LENGTH_LONG).show();
                }
                else {
                    persistFitness();
                    finish();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // scraping the old fitness activity
        _currentFitness = new FitnessActivity();
    }

    private void persistFitness() {
        DatabaseManager manager = new DatabaseManager(this);
        manager.insertNewFitnessActivity(_currentFitness);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_ADD_DATE) {

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(_currentDate);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(this, myDateListener, year, month, day);
        }

        if (id == DIALOG_ADD_TIME) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(_currentDate);

            return new TimePickerDialog(this, myTimeListener, Calendar.HOUR, Calendar.MINUTE, true);

        }

        if (id == DIALOG_ADD_ACT_TYPE) {

            DialogFragment newFragment = new ActivityTypeDialog();
            newFragment.onAttach(this);
            newFragment.show(getFragmentManager(), "dialog");

        }

        if (id == DIALOG_ADD_ACT_DURATION) {


            DialogFragment actDurationFrag = new ActivityDurationFragment();
            actDurationFrag.onAttach(this);
            actDurationFrag.show(getFragmentManager(), "dialog");
        }

        if (id == DIALOG_ADD_ACT_DISTANCE) {

            DialogFragment actDistance = new ActivityDistance();
            actDistance.onAttach(this);
            actDistance.show(getFragmentManager(), "dialog");
        }

        return null;
    }



    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

            Calendar calendar = new GregorianCalendar();
            calendar.set(arg1, arg2, arg3);
            _btnSetDate.setText(_dateFormat.format(new Date(calendar.getTimeInMillis())));

            // adding to the FitnessActivity
            _currentFitness.setDayOfActivity(new Date(calendar.getTimeInMillis()));
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {

            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.HOUR, arg1);
            calendar.set(Calendar.MINUTE, arg2);
            _btnSetTime.setText(_timeFormat.format(new Date(calendar.getTimeInMillis())));

            // adding to the FitnessActivity
            _currentFitness.setTimeOfActivity(new Date(calendar.getTimeInMillis()));

        }

    };

    @Override
    public void onActivityTypeSelected(int i) {

        if (i >= 0 && i < FitnessActivity.FitnessType.values().length) {
            FitnessActivity.FitnessType[] ft = FitnessActivity.FitnessType.values();
            _btnSetActivityType.setText(String.valueOf(ft[i]));

            // adding to the FitnessActivity
            _currentFitness.setFitnessType(ft[i]);
        }
        else {
            Toast.makeText(this, "Could not retreive the activity type", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityDurationSet(int h1, int m1, int m2, int s1, int s2) {
        _btnSetDuration.setText(String.valueOf(TimeUtil.getSecondsFromPicker(h1, m1, m2, s1, s2)) + " SECONDS");

        // adding to the FitnessActivity
        _currentFitness.setDuration(TimeUtil.getSecondsFromPicker(h1, m1, m2, s1, s2));
    }

    @Override
    public void onActivityDistanceSet(int k1, int k2, int m1, int m2, int m3) {
        _btnSetDistance.setText(String.valueOf(DistanceUtil.getMeters(k1, k2, m1, m2, m3)) + " METERS");

        // adding to the FitnessActivity
        _currentFitness.setDistance(DistanceUtil.getMeters(k1, k2, m1, m2, m3));
    }
}

