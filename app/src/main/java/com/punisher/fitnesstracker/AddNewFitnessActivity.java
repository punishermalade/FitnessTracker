package com.punisher.fitnesstracker;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.punisher.fitnesstracker.dialog.ActivityDistance;
import com.punisher.fitnesstracker.dialog.ActivityDurationFragment;
import com.punisher.fitnesstracker.dialog.ActivityTypeDialog;
import com.punisher.fitnesstracker.dto.FitnessActivity;
import com.punisher.fitnesstracker.task.DatabaseTask;
import com.punisher.fitnesstracker.util.DistanceUtil;
import com.punisher.fitnesstracker.util.FormatUtil;
import com.punisher.fitnesstracker.util.TimeUtil;
import com.punisher.fitnesstracker.validator.FitnessValidator;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;

public class AddNewFitnessActivity extends AppCompatActivity implements
                        ActivityTypeDialog.ActivityTypeListener,
                        ActivityDurationFragment.ActivityDurationListener,
                        ActivityDistance.ActivityDistanceListener {

    private static final int DIALOG_ADD_DATE = 400;
    private static final int DIALOG_ADD_TIME = 500;
    private static final int DIALOG_ADD_ACT_TYPE = 600;
    private static final int DIALOG_ADD_ACT_DURATION = 700;
    private static final int DIALOG_ADD_ACT_DISTANCE = 800;

    private Calendar _currentCalendar = null;
    private SimpleDateFormat _dateFormat = null;
    private SimpleDateFormat _timeFormat = null;
    private Date _currentDate = null;

    private TextView _btnSetDate = null;
    private TextView _btnSetTime = null;
    private TextView _btnSetActivityType = null;
    private TextView _btnSetDuration = null;
    private TextView _btnSetDistance = null;
    private ImageButton _btnCancel = null;
    private ImageButton _btnOk = null;
    private ImageView _errorDayIcon = null;
    private ImageView _errorTimeIcon = null;
    private ImageView _errorType = null;
    private ImageView _errorDuration = null;
    private ImageView _errorDistance = null;

    private FitnessActivity _currentFitness = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_fitness);

        // setting up the toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_add);
        setSupportActionBar(toolbar);

        // getting the error icon resource
        _errorDayIcon = (ImageView)findViewById(R.id.txt_add_date_error_icon);
        _errorTimeIcon = (ImageView)findViewById(R.id.txt_add_time_error_icon);
        _errorType = (ImageView)findViewById(R.id.txt_add_type_error_icon);
        _errorDuration = (ImageView)findViewById(R.id.txt_add_duration_error_icon);
        _errorDistance = (ImageView)findViewById(R.id.txt_add_distance_error_icon);

        // creating a new DTO to hold information
        _currentFitness = new FitnessActivity();

        // set the dateFormat objects and the current time
        _dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        _timeFormat = new SimpleDateFormat("HH:mm");
        _currentDate = new Date(System.currentTimeMillis());
        _currentCalendar = new GregorianCalendar();
        _currentCalendar.setTimeZone(TimeZone.getDefault());
        Log.i("fitness", "Timezone default: " + _currentCalendar.getTimeZone().getDisplayName());

        LinearLayout layoutDate = (LinearLayout)findViewById(R.id.layout_date);
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(DIALOG_ADD_DATE);
            }
        });
        // set the date button and text with the date
        _btnSetDate = (TextView)findViewById(R.id.txt_add_date);
        _btnSetDate.setText(_dateFormat.format(_currentDate));


        LinearLayout layoutTime = (LinearLayout)findViewById(R.id.layout_time);
        layoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(DIALOG_ADD_TIME);
            }
        });
        // set the time button and text with the current time
        _btnSetTime = (TextView)findViewById(R.id.txt_add_time);
        _btnSetTime.setText(_timeFormat.format(_currentDate));


        LinearLayout layoutActType = (LinearLayout)findViewById(R.id.layout_act_type);
        layoutActType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(DIALOG_ADD_ACT_TYPE);
            }
        });
        _btnSetActivityType = (TextView)findViewById(R.id.btn_add_act_type);


        LinearLayout layoutDuration = (LinearLayout)findViewById(R.id.layout_duration);
        layoutDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(DIALOG_ADD_ACT_DURATION);
            }
        });
        _btnSetDuration = (TextView)findViewById(R.id.btn_add_act_duration);


        LinearLayout layoutDistance = (LinearLayout)findViewById(R.id.layout_distance);
        layoutDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(DIALOG_ADD_ACT_DISTANCE);
            }
        });
        _btnSetDistance = (TextView)findViewById(R.id.btn_add_act_distance);

        // Setting the cancel and save button
        _btnCancel = (ImageButton)findViewById(R.id.btn_add_activity_cancel);
        _btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _btnOk = (ImageButton)findViewById(R.id.btn_add_activity_ok);
        _btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // resetting the default
                resetErrorIcons();

                FitnessValidator validator = new FitnessValidator();
                Map<String, Integer> errors = validator.validate(_currentFitness);

                if (!errors.isEmpty()) {
                    Log.i("fitness", "Validation failed when adding a new fitness acitivty. " + errors.size() + " errors detected");
                    showFieldInErrors(errors);
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

    private void resetErrorIcons() {
        _errorDayIcon.setVisibility(View.INVISIBLE);
        _errorTimeIcon.setVisibility(View.INVISIBLE);
        _errorType.setVisibility(View.INVISIBLE);
        _errorDuration.setVisibility(View.INVISIBLE);
        _errorDistance.setVisibility(View.INVISIBLE);
    }

    private void showFieldInErrors(Map<String, Integer> errors) {

        if (errors.containsKey("Day")) {
            _errorDayIcon.setVisibility(View.VISIBLE);
        }

        if (errors.containsKey("Duration")) {
           _errorDuration.setVisibility(View.VISIBLE);
        }

        if (errors.containsKey("Distance")) {
            _errorDistance.setVisibility(View.VISIBLE);
        }

        if (errors.containsKey("FitnessType")) {
            _errorType.setVisibility(View.VISIBLE);
        }
    }

    private void persistFitness() {
        AsyncTask<Void, Void, Void> task = new DatabaseTask(this,
                                            getString(R.string.progress_bar_adding_msg)) {

            @Override
            protected void doTask() {
                dbManager.insertNewFitnessActivity(_currentFitness);
            }

            @Override
            protected void refreshUI() {
                // nothing, end of activity
            }
        }.execute();
    }

    private void showInputDialog(int id) {

        if (id == DIALOG_ADD_DATE) {

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(_currentDate);
            calendar.setTimeZone(TimeZone.getDefault());

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            Dialog dateDialog = new DatePickerDialog(this, myDateListener, year, month, day);
            dateDialog.show();
        }

        if (id == DIALOG_ADD_TIME) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(_currentDate);
            calendar.setTimeZone(TimeZone.getDefault());

            Dialog timeDialog = new TimePickerDialog(this, myTimeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timeDialog.show();

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
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

            _currentCalendar.set(arg1, arg2, arg3);
            _btnSetDate.setText(_dateFormat.format(new Date(_currentCalendar.getTimeInMillis())));

            // adding to the FitnessActivity
            _currentFitness.setDayOfActivity(new Date(_currentCalendar.getTimeInMillis()));
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {

            _currentCalendar.set(Calendar.HOUR_OF_DAY, arg1);
            _currentCalendar.set(Calendar.MINUTE, arg2);
            _btnSetTime.setText(_timeFormat.format(new Date(_currentCalendar.getTimeInMillis())));

            // adding to the FitnessActivity
            _currentFitness.setDayOfActivity(new Date(_currentCalendar.getTimeInMillis()));
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

        int seconds = TimeUtil.getSecondsFromPicker(h1, m1, m2, s1, s2);
        _btnSetDuration.setText(FormatUtil.formatDuration(seconds));

        // adding to the FitnessActivity
        _currentFitness.setDuration(seconds);
    }

    @Override
    public void onActivityDistanceSet(int k1, int k2, int m1, int m2, int m3) {

        int distance = DistanceUtil.getMeters(k1, k2, m1, m2, m3);
        _btnSetDistance.setText(FormatUtil.getDistance(distance));

        // adding to the FitnessActivity
        _currentFitness.setDistance(DistanceUtil.getMeters(k1, k2, m1, m2, m3));
    }
}