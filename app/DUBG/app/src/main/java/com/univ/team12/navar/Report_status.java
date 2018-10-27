package com.univ.team12.navar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beyondar.android.world.World;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.univ.team12.navar.network.DirectionsResponse;
import com.univ.team12.navar.network.RetrofitInterface;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Report_status extends AppCompatActivity implements View.OnClickListener {
    private EditText peopleStuck;
    private EditText peopleInjured;
    private EditText comments;
    private Button submit;
    private LocationManager mLocationManager;
    private android.location.LocationListener mLocationListener;
    double lat;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationListener = new android.location.LocationListener() {
            // @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO locationListenerGPS onStatusChanged

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

            // @Override
            public void onLocationChanged(Location location) {
                lat=location.getLatitude();
                longitude=location.getLongitude();
                Log.e("ekagra","loc changed");

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100,
                    10, mLocationListener);
        }
        setContentView(R.layout.activity_report_status);
        peopleStuck = (EditText) findViewById(R.id.people_stuck);
        peopleInjured = (EditText) findViewById(R.id.people_injured);
        comments = (EditText) findViewById(R.id.comments);
        submit = (Button) findViewById(R.id.submit_report);

        submit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.localhost_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface apiService =
                retrofit.create(RetrofitInterface.class);

        final Call<DirectionsResponse> call = apiService.createStatus(comments.getText().toString()
                ,String.valueOf(lat),String.valueOf(longitude)
                ,peopleStuck.getText().toString(),peopleInjured.getText().toString());
        call.enqueue(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                Log.e("vivek","Message chala gaya");
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
            }
        });
    }

//    private void Set_googleApiClient() {
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }
//    }
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
//
//        }
//        else {
//
//            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                    mGoogleApiClient);
//
//        }
////        startLocationUpdates();
//
//    }
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
}
