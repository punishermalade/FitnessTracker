package com.punisher.fitnesstracker.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.punisher.fitnesstracker.AddNewFitnessActivity;
import com.punisher.fitnesstracker.dto.FitnessActivity;
import com.punisher.fitnesstracker.R;


public class ActivityTypeDialog extends DialogFragment {

    private ActivityTypeListener _activityTypeListener = null;
    private FitnessActivity _fitness = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int selectedPos = -1;
        int count = 0;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);
        for (FitnessActivity.FitnessType ft : FitnessActivity.FitnessType.values()) {
            adapter.add(ft.name());


            if (_fitness != null && _fitness.getFitnessType() != null && _fitness.getFitnessType().equals(ft)) {
                selectedPos = count;
            }
            count++;

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

        builder.setSingleChoiceItems(adapter, selectedPos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _activityTypeListener.onActivityTypeSelected(which);
                dialog.dismiss();
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

        if (activity instanceof AddNewFitnessActivity) {
            _fitness = ((AddNewFitnessActivity)activity).getCurrentFitness();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _activityTypeListener = null;
        _fitness = null;
    }

    public interface ActivityTypeListener {
        void onActivityTypeSelected(int i);
    }
}
