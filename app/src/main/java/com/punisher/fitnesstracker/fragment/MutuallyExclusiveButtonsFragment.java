package com.punisher.fitnesstracker.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.punisher.fitnesstracker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * a reusable fragment to have mutually exclusive button selection. An interface provides callback
 * method that can be implemented by an external caller.
 */
public class MutuallyExclusiveButtonsFragment extends Fragment {

    private View _view = null;
    private List<Button> _listButton = null;
    private OnChangeActiveButtonListener _changeActiveListener = null;
    private int _layoutResource = -1;
    private boolean _activateFirstButton = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflating the view
        _view = inflater.inflate(_layoutResource, container);

        // attaching click listener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // find the button
                for (Button b : _listButton) {
                    if (v.equals(b)) {
                        // activate the button
                        b.setBackgroundColor(getResources().getColor(R.color.colorActiveFilter));
                        _changeActiveListener.onChangeActiveButton(v);
                    }
                    else {
                        b.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }
                }
            }
        };

        // traversing the view as a view group
        ViewGroup parent = (ViewGroup)_view;
        _listButton = new ArrayList<>();

        // checking every child and adding only button to the list
        int totalChild = parent.getChildCount();
        for (int i = 0; i < totalChild; i++) {

            View child = parent.getChildAt(i);
            if (child instanceof Button) {
                child.setOnClickListener(listener);
                _listButton.add((Button)child);
            }
        }

        return _view;
    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        Log.i("fitness", "MutuallyExclusiveButtonsFragment.onAttach called");
        if (c instanceof OnChangeActiveButtonListener) {
            _changeActiveListener = (OnChangeActiveButtonListener)c;
            _layoutResource = _changeActiveListener.getLayoutResource();
        }
        else {
            throw new RuntimeException("Context is not instance of OnChangeActiveButtonLister");
        }
    }

    @Override
    public void onActivityCreated(Bundle s) {
        super.onActivityCreated(s);

        if (_listButton.size() > 0 && _changeActiveListener.mustActivateFirstButton()) {
            _listButton.get(0).callOnClick();
        }
    }

    public interface OnChangeActiveButtonListener {
        void onChangeActiveButton(View v);
        int getLayoutResource();
        boolean mustActivateFirstButton();
    }
}
