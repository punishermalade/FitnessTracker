package com.punisher.fitnesstracker.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.punisher.fitnesstracker.dto.FitnessActivity;
import com.punisher.fitnesstracker.R;


public class ActivityTypeDialog extends DialogFragment {

    private ActivityTypeListener _activityTypeListener = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);
        for (FitnessActivity.FitnessType ft : FitnessActivity.FitnessType.values()) {
            adapter.add(ft.name());
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.act_type_dialog_title));
        builder.setCancelable(true);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _activityTypeListener.onActivityTypeSelected(which);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("fitness", "ActivityTypeDialog.onAttached called with Activity " + activity.toString());

        try {
            _activityTypeListener = (ActivityTypeListener)activity;
        }
        catch (Exception ex) {
            Log.e("fitness", "ActivityTypeListener expected, activity found was: " + activity.toString());
        }
    }

    public interface ActivityTypeListener {
        void onActivityTypeSelected(int i);
    }
}
