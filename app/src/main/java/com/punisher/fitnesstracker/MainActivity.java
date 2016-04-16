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

    private ActivityList _listFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the view and the toolbar
        Log.i("fitness", "MainActivity.onCreate()");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get a reference on the list fragment
        _listFragment = (ActivityList)getFragmentManager().findFragmentById(R.id.fragment_personal_list);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fitness", "MainActivity is resuming...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_export_db) {

            AsyncTask<Void, Void, Void> task = new DatabaseTask(this,
                                                        getString(R.string.progress_bar_exporting_msg)) {
                @Override
                protected void doTask() {
                    dbManager.exportDatabaseToStorage();
                }

                @Override
                protected void refreshUI() {
                    // no UI to update!
                }
            }.execute();
        }

        if (id == R.id.action_import_db) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.action_import_db));
            builder.setMessage(getString(R.string.dialog_import_db_confirmation));
            builder.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // creating an async task
                    AsyncTask<Void, Void, Void> task = new DatabaseTask(MainActivity.this,
                                                                getString(R.string.progress_bar_loading_msg)) {

                        @Override
                        protected void doTask() {
                            dbManager.importDatabaseFromStorage();
                        }

                        @Override
                        protected void refreshUI() {
                            // call the fragment reload here
                            Fragment frag = getFragmentManager().findFragmentById(R.id.fragment_personal_list);
                            frag.onResume();
                        }
                    }.execute();
                }
            });

            builder.setNegativeButton(getString(android.R.string.no), null);
            builder.show();
        }

        if (id == R.id.action_clear_db) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.action_clear_db));
            builder.setCancelable(true);
            builder.setMessage(getString(R.string.dialog_clear_db_confirmation));
            builder.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AsyncTask<Void, Void, Void> task = new DatabaseTask(MainActivity.this,
                                                                getString(R.string.progress_bar_clearing_msg)) {
                        @Override
                        protected void doTask() {
                            dbManager.clearDatabase();
                        }

                        @Override
                        protected void refreshUI() {
                            Fragment frag = getFragmentManager().findFragmentById(R.id.fragment_personal_list);
                            frag.onResume();
                        }
                    }.execute();
                }
            });
            builder.setNegativeButton(getString(android.R.string.no), null);
            builder.show();
        }

        return super.onOptionsItemSelected(item);
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