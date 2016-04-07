package com.punisher.fitnesstracker.fragment;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.adapter.FitnessActivityAdapterList;
import com.punisher.fitnesstracker.database.DatabaseManager;
import com.punisher.fitnesstracker.dto.FitnessActivity;

import java.sql.Array;
import java.util.Collection;
import java.util.List;

/**
 *   Represents the list of fitness activity that are displayed.
 */
public class ActivityList extends Fragment {

    private ListView _listView = null;
    private ArrayAdapter<FitnessActivity> _adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        Log.i("fitness", "ActivityList Fragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_main_list, container);
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
        _listView = (ListView)view.findViewById(R.id.list_activity);
        _listView.setAdapter(_adapter);
    }
}
