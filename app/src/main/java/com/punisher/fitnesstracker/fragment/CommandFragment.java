package com.punisher.fitnesstracker.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.punisher.fitnesstracker.AddNewFitnessActivity;
import com.punisher.fitnesstracker.R;

public class CommandFragment extends Fragment {

    public CommandFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_command, container, false);


        Button btnAddActivity = (Button) view.findViewById(R.id.btn_add_activity);
        btnAddActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent addActivity = new Intent(view.getContext(), AddNewFitnessActivity.class);
                startActivity(addActivity);
            }
        });

        /*
        Button btnListActivity = (Button)view.findViewById(R.id.btn_list_activity);
        btnListActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("fitness", "Show the list");
            }
        });

        Button btnStatActivity = (Button)view.findViewById(R.id.btn_stats_activity);
        btnStatActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("fitness", "Show the stats");
                Fragment frag = null;

            }
        });

        Button btnGraphActivity = (Button)view.findViewById(R.id.btn_graph_activity);
        btnGraphActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("fitness", "Show the graph");
            }
        });
        */
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
