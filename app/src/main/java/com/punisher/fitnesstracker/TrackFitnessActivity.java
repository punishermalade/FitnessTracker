package com.punisher.fitnesstracker;

import android.location.Location;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Track the movement of the device and display the progress. This activity permits the tracking
 * to be started and stopped.
 */
public class TrackFitnessActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
                                                    GoogleApiClient.OnConnectionFailedListener,
                                                    LocationListener {

    private static final long UPDATE_INTERVAL = 1000;
    private static final long FASTEST_UPDATE_INTERVAL = 5000;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean mIsStarted = false;

    private SparseArray<Location> mLocations;
    private int mIndex = 0;
    private float mTotalDistance = 0.0f;

    private long mStartTime = 0;
    private long mEndTime = 0;
    private long mTotalTime = 0;

    private TextView mTxtDistance;
    private TextView mTxtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("fitness", "TrackFitnessActivity.onCreate()");
        setContentView(R.layout.activity_track_fitness);

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
            mLocations = new SparseArray<>();
        }

        mTxtDistance = (TextView)findViewById(R.id.start_tracking_distance_txt);
        mTxtTime = (TextView)findViewById(R.id.start_tracking_time_txt);

        restoreActivity(savedInstanceState);
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

            if (b.containsKey("startTime")) {
                mStartTime = b.getLong("startTime");
            }

            if (b.containsKey("locations")) {
                mLocations = b.getSparseParcelableArray("locations");
            }

            if (b.containsKey("locationsIndex")) {
                mIndex = b.getInt("locationsIndex");
            }

            if (b.containsKey("totalDistance")) {
                mTotalDistance = b.getFloat("totalDistance");
            }

            if (b.containsKey("totalTime")) {
                mTotalTime = b.getLong("totalTime");
            }
        }

        updateUI(mTotalDistance, mTotalTime);
    }

    public void startTrackingClick(View v) {
        if (!mIsStarted) {
            Log.i("fitness", "Start fitness click view");
            stopLocationRequestSilently();
            mGoogleApiClient.connect();

            mIndex = 0;
            mLocations.clear();
            mTotalDistance = 0.0f;

            mIsStarted = true;
            mStartTime = System.currentTimeMillis();
            mTotalTime = 0;

        }
        else {
            Log.i("fitness", "Stop fitness click view");
            stopLocationRequestSilently();
            mGoogleApiClient.disconnect();

            mIsStarted = false;
            mEndTime = System.currentTimeMillis();

            mTotalDistance = calculateDistance();
            mTotalTime = calculateTime();

        }

        updateUI(mTotalDistance, mTotalTime);
    }

    private void stopLocationRequestSilently() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        catch (Exception ex) {
            Log.e("fitness", "Error while removing location update: " + ex.getMessage());
        }
    }

    private void updateUI(float dist, long time) {
        if (dist == 0.0f) {
            mTxtDistance.setText("Activity in progress");
        }
        else {
            mTxtDistance.setText(mTotalDistance + " meters");
        }

        if (time == 0) {
            mTxtTime.setText("--:--");
        }
        else {
            mTxtTime.setText(time + " ms");
        }
    }

    private long calculateTime() {
        mEndTime = System.currentTimeMillis();
        return mEndTime - mStartTime;
    }

    private float calculateDistance() {
        float distance = 0.0f;
        Log.d("fitness", mLocations.size() + " locations to process");

        for (int i = 0; i < mLocations.size() - 1; i++) {
            Location current = mLocations.get(i);
            Location next = mLocations.get(i + 1);
            float meter = current.distanceTo(next);
            distance += meter;
        }
        return distance;
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
        b.putLong("startTime", mStartTime);

        b.putInt("locationsIndex", mIndex);
        b.putSparseParcelableArray("locations", mLocations);

        b.putFloat("totalDistance", mTotalDistance);
        b.putLong("totalTime", mTotalTime);
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
        mLocations.append(mIndex++, location);
    }
}
