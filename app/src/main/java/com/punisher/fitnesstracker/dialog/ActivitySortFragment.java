package com.punisher.fitnesstracker.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.punisher.fitnesstracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitySortFragment extends DialogFragment {

    private ActivitySortDialogListener _listener = null;

    public ActivitySortFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_activity_sort_title));
        builder.setCancelable(true);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        // create the adapter with the list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);
        adapter.add(getString(R.string.dialog_activity_sort_average_desc));
        adapter.add(getString(R.string.dialog_activity_sort_average_asc));
        adapter.add(getString(R.string.dialog_activity_sort_distance_desc));
        adapter.add(getString(R.string.dialog_activity_sort_distance_asc));
        adapter.add(getString(R.string.dialog_activity_sort_duration_desc));
        adapter.add(getString(R.string.dialog_activity_sort_duration_asc));
        adapter.add(getString(R.string.dialog_activity_sort_date_desc));
        adapter.add(getString(R.string.dialog_activity_sort_date_asc));

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _listener.onSortOptionSelected(which);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Activity c) {
        super.onAttach(c);

        try {
            _listener = (ActivitySortDialogListener)c;
        }
        catch (Exception ex) {
            Log.w("fitness", "ActivitySortFragment.onAttach() needs an instance of ActivitySortDialogListener", ex);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _listener = null;
    }

    public interface ActivitySortDialogListener {
        void onSortOptionSelected(int which);
    }
}
