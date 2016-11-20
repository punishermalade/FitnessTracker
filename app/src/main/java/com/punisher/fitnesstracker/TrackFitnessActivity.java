package com.punisher.fitnesstracker;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.punisher.fitnesstracker.dto.FitnessActivity;
import com.punisher.fitnesstracker.monitor.ClockManager;
import com.punisher.fitnesstracker.util.DistanceUtil;
import com.punisher.fitnesstracker.util.FormatUtil;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Track the movement of the device and display the progress. This activity permits the tracking
 * to be started and stopped.
 */
public class TrackFitnessActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
                                                    GoogleApiClient.OnConnectionFailedListener,
                                                    LocationListener {

    private static final long UPDATE_INTERVAL = 1000;
    private static final long FASTEST_UPDATE_INTERVAL = 5000;
    private static final float MINIMUM_DISTANCE_TOLERANCE = 10f; // in meters

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean mIsStarted = false;

    private ArrayList<Location> mLocations;
    private float mTotalDistance = 0.0f;

    private long mTotalTime = 0;

    private ClockManager mClockManager;
    private TextView mTxtDistance;
    private TextView mTxtTime;
    private Button mStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("fitness", "TrackFitnessActivity.onCreate()");
        setContentView(R.layout.activity_track_fitness);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        }

        if (mLocations == null) {
            mLocations = new ArrayList<>();
        }

        mTxtDistance = (TextView)findViewById(R.id.track_fitness_distance);
        mTxtTime = (TextView)findViewById(R.id.track_fitness_clock);
        mStartBtn = (Button)findViewById(R.id.track_fitness_start_btn);

        if (mClockManager == null) {
            mClockManager = new ClockManager();
            mClockManager.attachUI(mTxtTime);
        }


        restoreActivity(savedInstanceState);
        updateUI();
    }

    private void restoreActivity(Bundle b) {

        if (b != null) {
            if (b.containsKey("distanceText")) {
                mTxtDistance.setText(b.getString("distanceText"));
            }

            if (b.containsKey("timeText")) {
                mTxtTime.setText(b.getString("timeText"));
            }

            if (b.containsKey("started")) {
                mIsStarted = b.getBoolean("started");
            }

            if (b.containsKey("locations")) {
                mLocations = b.getParcelableArrayList("locations");
            }

            if (b.containsKey("totalDistance")) {
                mTotalDistance = b.getFloat("totalDistance");
            }

            if (b.containsKey("totalTime")) {
                mTotalTime = b.getLong("totalTime");
            }

            if (b.containsKey("elapsedTime")) {
                mClockManager.getGameClock().setElapsedTime(b.getLong("elapsedTime"));
                mClockManager.startClock();
            }
        }

        updateUI();
    }

    public void startTrackingClick(View v) {
        if (!mIsStarted) {
            Log.i("fitness", "Start fitness click view");
            stopLocationRequestSilently();
            mGoogleApiClient.connect();

            mLocations.clear();
            mTotalDistance = 0.0f;

            mIsStarted = true;

            mClockManager.getGameClock().setElapsedTime(0);
            mClockManager.startClock();
        }
        else {
            Log.i("fitness", "Stop fitness click view");
            stopLocationRequestSilently();
            mGoogleApiClient.disconnect();

            mIsStarted = false;

            mClockManager.stopClock();
            mTotalTime = mClockManager.getGameClock().getElapsed();

            Log.i("fitness", "Activity stopped - dist: " + mTotalDistance + " time: " + mTotalTime);

            // sending the time to the record activity
            Intent i = new Intent(this, AddNewFitnessActivity.class);
            i.putExtra(AddNewFitnessActivity.DISTANCE_KEY, mTotalDistance);
            i.putExtra(AddNewFitnessActivity.DURATION_KEY, mTotalTime);
            i.putExtra(AddNewFitnessActivity.FITNESS_TYPE, FitnessActivity.FitnessType.RUNNING.toString());
            i.putExtra(AddNewFitnessActivity.FROZEN_DATA_KEY, true);
            startActivity(i);
            finish();
        }

        updateUI();
    }

    private void stopLocationRequestSilently() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        catch (Exception ex) {
            Log.e("fitness", "Error while removing location update: " + ex.getMessage());
        }
    }

    private void updateUI() {

        mTxtDistance.setText(String.format("%s %s", FormatUtil.getDistance(Math.round(mTotalDistance)),
                getString(R.string.track_fitness_km_short_lbl)));

        if (mIsStarted) {
            mStartBtn.setText(R.string.track_fitness_stop_btn_lbl);
        }
        else {
            mStartBtn.setText(R.string.track_fitness_start_btn_lbl);
        }
    }

    private void calculateDistance() {
        Log.d("fitness", mLocations.size() + " locations to process");
        float distance;

        if (mLocations.size() > 1) {
            Location last = mLocations.get(mLocations.size() - 2);
            Location next = mLocations.get(mLocations.size() - 1);
            distance = last.distanceTo(next);

            if (distance >= MINIMUM_DISTANCE_TOLERANCE) {
                mTotalDistance += distance;
            }
            else {
                Log.d("fitness", "Removing last entry, tolerance not reached");
                mLocations.remove(mLocations.size() - 1);
            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
        Log.d("fitness", "TrackFitnessActivity.onSaveInstanceState()");
        b.putString("distanceText", mTxtDistance.getText().toString());
        b.putString("timeText", mTxtTime.getText().toString());
        b.putBoolean("started", mIsStarted);
        b.putParcelableArrayList("locations", mLocations);
        b.putFloat("totalDistance", mTotalDistance);
        b.putLong("totalTime", mTotalTime);
        b.putLong("elapsedTime", mClockManager.getGameClock().getElapsed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("fitness", "TrackFitnessActivity.onResume()");

        if (mIsStarted && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("fitness", "TrackFitnessActivity.onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("fitness", "TrackFitnessActivity.onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("fitness", "TrackFitnessActivity.onDestroy()");
        stopLocationRequestSilently();
        mClockManager.stopClock();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (mIsStarted) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("fitness", "new location: " + location.toString());
        mLocations.add(location);

        calculateDistance();
        updateUI();
    }
}
