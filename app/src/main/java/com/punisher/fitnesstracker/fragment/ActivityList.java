package com.punisher.fitnesstracker.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.adapter.FitnessActivityAdapterList;
import com.punisher.fitnesstracker.database.DatabaseManager;
import com.punisher.fitnesstracker.dto.FitnessActivity;
import com.punisher.fitnesstracker.task.DatabaseTask;

import java.util.List;

/**
 *   Represents the list of fitness activity that are displayed.
 */
public class ActivityList extends Fragment  {

    private ListView _listView = null;
    private ArrayAdapter<FitnessActivity> _adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        Log.i("fitness", "ActivityList Fragment onCreateView");

        View view = inflater.inflate(R.layout.fragment_main_list, container);
        _listView = (ListView)view.findViewById(R.id.list_activity);

        // set the long click listener
        _listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, final long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.dialog_activity_action_delete_title));
                builder.setMessage(R.string.dialog_activity_action_delete);
                builder.setNegativeButton(android.R.string.no, null);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AsyncTask<Void, Void, Void> task = new DatabaseTask(getActivity(),
                                getString(R.string.progress_bar_deleting_msg)) {
                            @Override
                            protected void doTask() {
                                FitnessActivity activity = (FitnessActivity) parent.getItemAtPosition(position);
                                dbManager.deleteActivity(activity);
                            }

                            @Override
                            protected void refreshUI() {
                                Fragment frag = getFragmentManager().findFragmentById(R.id.fragment_personal_list);
                                frag.onResume();
                            }
                        }.execute();
                    }
                });

                builder.create().show();

                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fitness", "ActivityList Fragment onResume");
        loadList(getView());
    }

    private void loadList(View view) {

        AsyncTask<Void, Void, Void> task = new DatabaseTask(getActivity()) {

            private List<FitnessActivity> list = null;
            private FitnessActivity[] arrList = null;

            @Override
            protected void doTask() {
                list = dbManager.getFitnessActivityList();
                arrList = list.toArray(new FitnessActivity[0]);
                Log.i("fitness", "Loading database in DatabaseTask");
            }

            @Override
            protected void refreshUI() {
                _adapter = new FitnessActivityAdapterList(getActivity(), R.layout.fragment_main_list_row, arrList);
                _listView.setAdapter(_adapter);
            }
        }.execute();
    }
}
