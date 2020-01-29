package com.business.order_trip.helpers;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.business.order_trip.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class AdressFromActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //_______create object
    private GoogleApiClient mGoogleApiClient;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 11;
    private String TAG = "place";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress);

        //________initialize google client api
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        //________initialize auto complete place
        autocompletePlace();
    }
    private void autocompletePlace() {

        //______________create object of PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        //______________add listener to PlaceAutocompleteFragment.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            //______________Method will be auto call on selection of any place.
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                //______________format place name.
                String toastMsgAuto = String.format("%s", place.getName());

                Intent intent = new Intent();
                intent.putExtra("city_from", toastMsgAuto);
                setResult(RESULT_CANCELED, intent);
                finish();

                //______________show selected place in massage.
//                Toast.makeText(AdressFromActivity.this, toastMsgAuto, Toast.LENGTH_LONG).show();
            }

            //______________Method will be auto call, if error occur on selection of any place.
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    //______________Method will be auto call, if connection fail from google server.
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {        // to prevent irritating accidental logouts
        Intent intent = new Intent();
        intent.putExtra("city_from", "null");
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}