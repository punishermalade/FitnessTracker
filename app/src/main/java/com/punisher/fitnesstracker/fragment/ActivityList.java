package com.punisher.fitnesstracker.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
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
                        FitnessActivity activity = (FitnessActivity)parent.getItemAtPosition(position);
                        DatabaseManager dbManager = new DatabaseManager(getActivity());
                        dbManager.deleteActivity(activity);

                        // reload the fragment
                        getActivity().recreate();

                    }
                });

                builder.create().show();

                return false;
            }
        });

        // load the data
        loadList(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fitness", "ActivityList Fragment onResume");
        loadList(getView());
    }

    private void loadList(View view) {
        DatabaseManager manager = new DatabaseManager(getActivity());
        List<FitnessActivity> list = manager.getFitnessActivityList();
        FitnessActivity[] arrList = list.toArray(new FitnessActivity[0]);

        _adapter = new FitnessActivityAdapterList(getActivity(), R.layout.fragment_main_list_row, arrList);

        _listView.setAdapter(_adapter);
    }

}
