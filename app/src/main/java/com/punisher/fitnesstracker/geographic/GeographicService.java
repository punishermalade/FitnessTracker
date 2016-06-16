package com.punisher.fitnesstracker.geographic;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * This class is responsible to activate the Geographic API
 */
public class GeographicService implements GoogleApiClient.ConnectionCallbacks,
                                          GoogleApiClient.OnConnectionFailedListener {

    /**
     * Google API client
     */
    private GoogleApiClient mGoogleApiClient;

    public GeographicService() {

    }

    public void init(Context c) {
        init(c, false);
    }

    public void init(Context c, boolean autoConnect) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(c)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (autoConnect) {
            connect();
        }
    }

    public Location getLastKnownPosition() {
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    public void connect() {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    public void disconnect() {

        if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
