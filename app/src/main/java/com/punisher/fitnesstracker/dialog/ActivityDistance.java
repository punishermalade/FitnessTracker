package com.punisher.fitnesstracker.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.punisher.fitnesstracker.AddNewFitnessActivity;
import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.dto.FitnessActivity;
import com.punisher.fitnesstracker.util.DistanceUtil;
import com.punisher.fitnesstracker.util.FormatUtil;

public class ActivityDistance extends DialogFragment {

    private ActivityDistanceListener mListener;
    private NumberPicker _kmPicker1 = null;
    private NumberPicker _kmPicker2 = null;
    private NumberPicker _mPicker1 = null;
    private NumberPicker _mPicker2 = null;
    private NumberPicker _mPicker3 = null;
    private Button _btnCancel = null;
    private Button _btnOK = null;
    private FitnessActivity _fitness = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.lbl_title_dialog_distance));
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onActivityDistanceSet(_kmPicker1.getValue(), _kmPicker2.getValue(),
                                                _mPicker1.getValue(), _mPicker2.getValue(),
                                                _mPicker3.getValue());
                dismiss();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_activity_distance, null);
        builder.setView(view);
        _kmPicker1 = (NumberPicker)view.findViewById(R.id.act_distance_np_km1);
        _kmPicker2 = (NumberPicker)view.findViewById(R.id.act_distance_np_km2);
        _mPicker1 = (NumberPicker)view.findViewById(R.id.act_distance_np_m1);
        _mPicker2 = (NumberPicker)view.findViewById(R.id.act_distance_np_m2);
        _mPicker3 = (NumberPicker)view.findViewById(R.id.act_distance_np_m3);

        setPicker(_kmPicker1, 0, 9, 1);
        setPicker(_kmPicker2, 0, 9, 1);
        setPicker(_mPicker1, 0, 9, 1);
        setPicker(_mPicker2, 0, 9, 1);
        setPicker(_mPicker3, 0, 9, 1);

        if (_fitness != null) {
            setValueInUI(_fitness.getDistance());
        }

        return builder.create();
    }

    private void setValueInUI(int dist) {
        int picker[] = DistanceUtil.getPickersFromMeter(dist);
        _kmPicker1.setValue(picker[0]);
        _kmPicker2.setValue(picker[1]);
        _mPicker1.setValue(picker[2]);
        _mPicker2.setValue(picker[3]);
        _mPicker3.setValue(picker[4]);
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        if (a instanceof ActivityDistanceListener) {
            mListener = (ActivityDistanceListener)a;
        } else {
            throw new RuntimeException(a.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        if (a instanceof AddNewFitnessActivity) {
            _fitness = ((AddNewFitnessActivity)a).getCurrentFitness();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        _fitness = null;
    }

    private void setPicker(NumberPicker np, int min, int max, int digit) {
        if (np != null) {
            np.setMinValue(min);
            np.setMaxValue(max);
            np.setWrapSelectorWheel(false);
            np.setValue(0);
            np.setFormatter(FormatUtil.getDigitFormatter(digit));
        }
    }

    public interface ActivityDistanceListener {
        void onActivityDistanceSet(int k1, int k2, int m1, int m2, int m3);
    }
}