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

import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.util.FormatUtil;

public class ActivityDurationFragment extends DialogFragment {

    private ActivityDurationListener mListener;
    private NumberPicker _hourPicker1 = null;
    private NumberPicker _minPicker1 = null;
    private NumberPicker _secPicker1 = null;
    private NumberPicker _hourPicker2 = null;
    private NumberPicker _minPicker2 = null;
    private NumberPicker _secPicker2 = null;
    private Button _btnOK = null;
    private Button _btnCancel = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.lbl_title_dialog_duration));
        builder.setCancelable(true);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onActivityDurationSet(_hourPicker1.getValue(),
                        _minPicker1.getValue(), _minPicker2.getValue(),
                        _secPicker1.getValue(), _secPicker2.getValue());
                dismiss();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_activity_duration, null);
        builder.setView(view);

        _hourPicker1 = (NumberPicker)view.findViewById(R.id.act_duration_np_hour_1);
        _minPicker1 = (NumberPicker)view.findViewById(R.id.act_duration_np_min_1);
        _secPicker1 = (NumberPicker)view.findViewById(R.id.act_duration_np_sec_1);
        _minPicker2 = (NumberPicker)view.findViewById(R.id.act_duration_np_min_2);
        _secPicker2 = (NumberPicker)view.findViewById(R.id.act_duration_np_sec_2);

        setPickerWithMinMax(_hourPicker1, 0, 23);
        setPickerWithMinMax(_minPicker1, 0, 5);
        setPickerWithMinMax(_secPicker1, 0, 5);
        setPickerWithMinMax(_minPicker2, 0, 9);
        setPickerWithMinMax(_secPicker2, 0, 9);


        return builder.create();

    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        if (a instanceof ActivityDurationListener) {
            mListener = (ActivityDurationListener) a;
        } else {
            throw new RuntimeException(a.toString()
                    + " must implement ActivityDurationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setPickerWithMinMax(NumberPicker np, int min, int max) {
        if (np != null) {
            np.setMinValue(min);
            np.setMaxValue(max);
            np.setValue(0);
            np.setWrapSelectorWheel(false);
            np.setFormatter(FormatUtil.getDigitFormatter(1));
        }
    }

    public interface ActivityDurationListener {
        void onActivityDurationSet(int h1, int m1, int m2, int s1, int s2);
    }
}
