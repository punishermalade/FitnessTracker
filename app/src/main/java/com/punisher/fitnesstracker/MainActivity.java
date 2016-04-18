package com.punisher.fitnesstracker;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.punisher.fitnesstracker.dialog.ActivitySortFragment;
import com.punisher.fitnesstracker.dto.FitnessActivity;
import com.punisher.fitnesstracker.fragment.ActivityList;
import com.punisher.fitnesstracker.task.DatabaseTask;

import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the view and the toolbar
        Log.i("fitness", "MainActivity.onCreate()");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setting the sort button
        ImageButton sortBtn = (ImageButton)findViewById(R.id.toolbar_sort_button);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout l = (LinearLayout)findViewById(R.id.layout_sort_button_list);
                if (l.getVisibility() == View.GONE) {
                    l.setVisibility(View.VISIBLE);
                }
                else if (l.getVisibility() == View.VISIBLE) {
                    l.setVisibility(View.GONE);
                }
            }
        });

        ImageButton filterBtn = (ImageButton)findViewById(R.id.toolbar_filter_button);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout l = (LinearLayout)findViewById(R.id.layout_filter_button_list);
                if (l.getVisibility() == View.GONE) {
                    l.setVisibility(View.VISIBLE);
                }
                else if (l.getVisibility() == View.VISIBLE) {
                    l.setVisibility(View.GONE);
                }
            }
        });

        ImageButton settingBtn = (ImageButton)findViewById(R.id.toolbar_settings_button);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), SettingsActivity.class);
                startActivity(i);
            }
        });

        ImageButton btnAddActivity = (ImageButton)findViewById(R.id.btnimg_add_activity);
        btnAddActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent addActivity = new Intent(view.getContext(), AddNewFitnessActivity.class);
                startActivity(addActivity);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("fitness", "MainActivity.onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("fitness", "MainActivity.onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("fitness", "MainActivity.onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("fitness", "MainActivity.onDestroy()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fitness", "MainActivity is resuming...");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("fitness", "MainAcitivty.onStop()");
    }
}