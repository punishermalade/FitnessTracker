package com.punisher.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.punisher.fitnesstracker.database.DatabaseManager;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient = null;
    private static int RC_SIGN_IN = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("fitness", "app shut down at: " + new Date(System.currentTimeMillis()));

        Log.i("fitness", "MainActivity.onCreate()");
        setContentView(R.layout.activity_main);

        // Configure sign-in to request the user's ID, email address, and basic profile. ID and
        // basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("fitness", "MainActivity.onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("fitness", "MainActivity.onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("fitness", "MainActivity.onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("fitness", "MainActivity.onDestroy()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fitness", "MainActivity is resuming...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_signin_google) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        if (id == R.id.action_export_db) {
            DatabaseManager dbManager = new DatabaseManager(this);
            dbManager.exportDatabaseToStorage();
        }

        if (id == R.id.action_import_db) {
            DatabaseManager dbManager = new DatabaseManager(this);
            dbManager.importDatabaseFromStorage();

            // restart the view
            this.recreate();
        }

        if (id == R.id.action_clear_db) {
            DatabaseManager dbManager = new DatabaseManager(this);
            dbManager.clearDatabase();

            // restart the view
            this.recreate();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from
        //   GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                // Get account information
                Log.i("fitness", "display name: " + acct.getDisplayName());
                /*
                mFullName = acct.getDisplayName();
                mEmail = acct.getEmail();
                */
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();


        Log.i("fitness", "MainAcitivty.onStop()");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
