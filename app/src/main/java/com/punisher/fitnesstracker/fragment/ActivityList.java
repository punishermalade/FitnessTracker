package com.punisher.fitnesstracker.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.adapter.FitnessActivityAdapterList;
import com.punisher.fitnesstracker.database.DatabaseManager;
import com.punisher.fitnesstracker.dto.FitnessActivity;
import com.punisher.fitnesstracker.task.DatabaseTask;
import com.punisher.fitnesstracker.util.Ternary;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *   Represents the list of fitness activity that are displayed.
 */
public class ActivityList extends Fragment  {

    /**
     * the list of all the Comparator this Fragment provide by default
     */
    public static Comparator<FitnessActivity> SORT_BY_DURATION_DESC = null;
    public static Comparator<FitnessActivity> SORT_BY_DURATION_ASC = null;
    public static Comparator<FitnessActivity> SORT_BY_DATE_DESC = null;
    public static Comparator<FitnessActivity> SORT_BY_DATE_ASC = null;
    public static Comparator<FitnessActivity> SORT_BY_DISTANCE_DESC = null;
    public static Comparator<FitnessActivity> SORT_BY_DISTANCE_ASC = null;
    public static Comparator<FitnessActivity> SORT_BY_AVERAGE_DESC = null;
    public static Comparator<FitnessActivity> SORT_BY_AVERAGE_ASC = null;

    /**
     * static block to create the different comparator provided by this fragment
     */
    static {
        buildStandardComparator();
        Log.i("fitness", "ActivityList static Comparator builder called");
    }

    private Map<Integer, Ternary> _sortStatus = null;

    private Drawable _sortAscendingIcon = null;
    private Drawable _sortDescendingIcon = null;

    private Button _sortDateBtn = null;
    private Button _sortDistanceBtn = null;
    private Button _sortDurationBtn = null;
    private Button _sortAverageBtn = null;

    private View _view = null;

    /**
     * a reference to the list view UI element
     */
    private ListView _listView = null;

    /**
     * a reference to the array adapter. This reference is used to update the order of the activity
     */
    private ArrayAdapter<FitnessActivity> _adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        Log.i("fitness", "ActivityList Fragment onCreateView");

        _view = inflater.inflate(R.layout.fragment_main_list, container);
        _listView = (ListView)_view.findViewById(R.id.list_activity);

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

        // setting the Map for the sort status
        _sortStatus = new HashMap<Integer, Ternary>();
        _sortStatus.put(R.id.sort_date_icon, new Ternary(Ternary.TRUE));
        _sortStatus.put(R.id.sort_distance_icon, new Ternary(Ternary.UNDEFINED));
        _sortStatus.put(R.id.sort_duration_icon, new Ternary(Ternary.UNDEFINED));
        _sortStatus.put(R.id.sort_average_icon, new Ternary(Ternary.UNDEFINED));

        // getting some reference for the Drawable and the ImageView's
        _sortAscendingIcon = ContextCompat.getDrawable(getActivity(), android.R.drawable.arrow_up_float);
        _sortDescendingIcon = ContextCompat.getDrawable(getActivity(), android.R.drawable.arrow_down_float);

        // getting reference on the button
        _sortDateBtn = (Button)_view.findViewById(R.id.sort_date);
        _sortDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSortChange(R.id.sort_date_icon);
            }
        });

        _sortDistanceBtn = (Button)_view.findViewById(R.id.sort_distance);
        _sortDistanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSortChange(R.id.sort_distance_icon);
            }
        });

        _sortDurationBtn = (Button)_view.findViewById(R.id.sort_duration);
        _sortDurationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSortChange(R.id.sort_duration_icon);
            }
        });

        _sortAverageBtn = (Button)_view.findViewById(R.id.sort_average);
        _sortAverageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSortChange(R.id.sort_average_icon);
            }
        });

        handleSortChange(R.id.sort_date_icon);

        return _view;
    }

    /**
     * sort the underlying list of FitnessActivity with the given comparator
     * @param comparator the comparator to be used to sort
     */
    public void sortFitnessActivity(Comparator<FitnessActivity> comparator) {
        if (_adapter != null && comparator != null) {
            _adapter.sort(comparator);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fitness", "ActivityList Fragment onResume");
        loadList(getView());
    }

    private void handleSortChange(int id) {
        Log.i("fitness", "Handling a sort change from button id: " + id);
        switchSortStatus(id);
        applySort(id);
        for (int key : _sortStatus.keySet()) {
            updateUISortIndicator(key, _sortStatus.get(key));
        }
    }

    private void switchSortStatus(int i) {
        Ternary t = _sortStatus.get(i);

        if (t.getValue() == Ternary.UNDEFINED || t.getValue() == Ternary.FALSE) {
            _sortStatus.put(i, t.setValue(Ternary.TRUE));
        }
        else if (t.getValue() == Ternary.TRUE) {
            _sortStatus.put(i, t.setValue(Ternary.FALSE));
        }
        else {
            _sortStatus.put(i, t.setValue(Ternary.UNDEFINED));
        }

        for (int key : _sortStatus.keySet()) {
            if (key != i) {
                _sortStatus.get(key).setValue(Ternary.UNDEFINED);
            }
        }
    }

    private void applySort(int i) {
        Ternary t = _sortStatus.get(i);

        if (R.id.sort_date_icon == i && t.getValue() == Ternary.FALSE) {
            sortFitnessActivity(SORT_BY_DATE_DESC);
        }
        else if (R.id.sort_date_icon == i && t.getValue() == Ternary.TRUE) {
            sortFitnessActivity(SORT_BY_DATE_ASC);
        }
        else if (R.id.sort_distance_icon == i && t.getValue() == Ternary.FALSE) {
            sortFitnessActivity(SORT_BY_DISTANCE_DESC);
        }
        else if (R.id.sort_distance_icon == i && t.getValue() == Ternary.TRUE) {
            sortFitnessActivity(SORT_BY_DISTANCE_ASC);
        }
        else if (R.id.sort_duration_icon == i && t.getValue() == Ternary.FALSE) {
            sortFitnessActivity(SORT_BY_DURATION_DESC);
        }
        else if (R.id.sort_duration_icon == i && t.getValue() == Ternary.TRUE) {
            sortFitnessActivity(SORT_BY_DURATION_ASC);
        }
        else if (R.id.sort_average_icon == i && t.getValue() == Ternary.FALSE) {
            sortFitnessActivity(SORT_BY_AVERAGE_DESC);
        }
        else if (R.id.sort_average_icon == i && t.getValue() == Ternary.TRUE) {
            sortFitnessActivity(SORT_BY_AVERAGE_ASC);
        }
    }

    private void updateUISortIndicator(int ressId, Ternary t) {

        ImageView v = (ImageView)_view.findViewById(ressId);

        if (t.getValue() == Ternary.UNDEFINED) {
            v.setVisibility(View.INVISIBLE);
        }
        else {
            v.setVisibility(View.VISIBLE);

            if (t.getValue() == Ternary.TRUE) {
                v.setImageDrawable(_sortAscendingIcon);
            }
            else if (t.getValue() == Ternary.FALSE) {
                v.setImageDrawable(_sortDescendingIcon);
            }
        }
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

    private static void buildStandardComparator() {

        SORT_BY_DURATION_DESC = new Comparator<FitnessActivity>() {
            @Override
            public int compare(FitnessActivity lhs, FitnessActivity rhs) {
                if (lhs.getDuration() < rhs.getDuration()) {
                    return 1;
                }
                else if (lhs.getDuration() > rhs.getDuration()) {
                    return -1;
                }
                return 0;
            }
        };

        SORT_BY_DURATION_ASC = new Comparator<FitnessActivity>() {
            @Override
            public int compare(FitnessActivity lhs, FitnessActivity rhs) {
                if (lhs.getDuration() < rhs.getDuration()) {
                    return -1;
                }
                else if (lhs.getDuration() > rhs.getDuration()) {
                    return 1;
                }
                return 0;
            }
        };

        SORT_BY_DATE_ASC = new Comparator<FitnessActivity>() {
            @Override
            public int compare(FitnessActivity lhs, FitnessActivity rhs) {
                if (lhs.getDayOfActivity().before(rhs.getDayOfActivity())) {
                    return -1;
                }
                else if (!lhs.getDayOfActivity().before(rhs.getDayOfActivity())) {
                    return 1;
                }
                return 0;
            }
        };

        SORT_BY_DATE_DESC = new Comparator<FitnessActivity>() {
            @Override
            public int compare(FitnessActivity lhs, FitnessActivity rhs) {
                if (lhs.getDayOfActivity().before(rhs.getDayOfActivity())) {
                    return 1;
                }
                else if (!lhs.getDayOfActivity().before(rhs.getDayOfActivity())){
                    return -1;
                }
                return 0;
            }
        };

        SORT_BY_DISTANCE_DESC = new Comparator<FitnessActivity>() {
            @Override
            public int compare(FitnessActivity lhs, FitnessActivity rhs) {
                if (lhs.getDistance() < rhs.getDistance()) {
                    return 1;
                }
                else if (lhs.getDistance() > rhs.getDistance()){
                    return -1;
                }
                return 0;
            }
        };

        SORT_BY_DISTANCE_ASC = new Comparator<FitnessActivity>() {
            @Override
            public int compare(FitnessActivity lhs, FitnessActivity rhs) {
                if (lhs.getDistance() < rhs.getDistance()) {
                    return -1;
                }
                else if (lhs.getDistance() > rhs.getDistance()){
                    return 1;
                }
                return 0;
            }
        };

        SORT_BY_AVERAGE_ASC = new Comparator<FitnessActivity>() {
            @Override
            public int compare(FitnessActivity lhs, FitnessActivity rhs) {
                if (lhs.getAverage() < rhs.getAverage()) {
                    return -1;
                }
                else if (lhs.getAverage() > rhs.getAverage()){
                    return 1;
                }
                return 0;
            }
        };

        SORT_BY_AVERAGE_DESC = new Comparator<FitnessActivity>() {
            @Override
            public int compare(FitnessActivity lhs, FitnessActivity rhs) {
                if (lhs.getAverage() < rhs.getAverage()) {
                    return 1;
                }
                else if (lhs.getAverage() > rhs.getAverage()){
                    return -1;
                }
                return 0;
            }
        };

    }
}
