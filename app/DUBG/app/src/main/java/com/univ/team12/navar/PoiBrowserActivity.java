package com.univ.team12.navar;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.univ.team12.navar.ar.ArBeyondarGLSurfaceView;
import com.univ.team12.navar.ar.ArFragmentSupport;
import com.univ.team12.navar.ar.OnTouchBeyondarViewListenerMod;
import com.univ.team12.navar.network.DirectionsResponse;
import com.univ.team12.navar.network.MessageResponse;
import com.univ.team12.navar.network.PlaceResponse;
import com.univ.team12.navar.network.Poi2Response;
import com.univ.team12.navar.network.PoiResponse;
import com.univ.team12.navar.network.RetrofitInterface;
import com.univ.team12.navar.network.TypeResponse;
import com.univ.team12.navar.network.UsersResponse;
import com.univ.team12.navar.network.poi.Fields;
import com.univ.team12.navar.network.poi.Result;
import com.univ.team12.navar.network.poi.Status;
import com.univ.team12.navar.utils.UtilsCheck;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by Amal Krishnan on 10-04-2017.
 */

public class PoiBrowserActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks
    ,GoogleApiClient.OnConnectionFailedListener,OnClickBeyondarObjectListener,
        OnTouchBeyondarViewListenerMod{

    private final static String TAG="PoiBrowserActivity";

    private TextView textView;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LayoutInflater layoutInflater;
    private ArFragmentSupport arFragmentSupport;
    private World world;
    private SharedPreferences myPreferences;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private int selectedVisiblity;

    @BindView(R.id.poi_place_detail)
    CardView poi_cardview;
    @BindView(R.id.poi_place_close_btn)
    ImageButton poi_cardview_close_btn;
    @BindView(R.id.poi_place_name)
    TextView poi_place_name;
    @BindView(R.id.poi_place_address)
    TextView poi_place_addr;
    @BindView(R.id.poi_place_image)
    ImageView poi_place_image;
    @BindView(R.id.poi_place_ar_direction)
    Button poi_place_ar_btn;
    @BindView(R.id.poi_place_maps_direction)
    Button poi_place_maps_btn;
    @BindView(R.id.sendMessage)
    Button message;

    SharedPreferences.Editor myEditor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_browser);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
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
        ButterKnife.bind(this);
        myPreferences = PreferenceManager.getDefaultSharedPreferences(PoiBrowserActivity.this);
        myEditor = myPreferences.edit();
        poi_cardview.setVisibility(View.GONE);

        if(!UtilsCheck.isNetworkConnected(this)){
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.poi_layout),
                    "Turn Internet On", Snackbar.LENGTH_SHORT);
            mySnackbar.show();
        }

        arFragmentSupport = (ArFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.poi_cam_fragment);
        arFragmentSupport.setOnClickBeyondarObjectListener(this);
        arFragmentSupport.setOnTouchBeyondarViewListener(this);


        textView=(TextView) findViewById(R.id.loading_text);

        Set_googleApiClient(); //Sets the GoogleApiClient

        poi_cardview_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poi_cardview.setVisibility(View.GONE);
                poi_place_image.setImageResource(android.R.color.transparent);
                poi_place_name.setText(" ");
                poi_place_addr.setText(" ");
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LocationListener mLocationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(final Location location) {
                        //your code here
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(PoiBrowserActivity.this);
                // Get the layout inflater
                LayoutInflater inflater = PoiBrowserActivity.this.getLayoutInflater();
                final View v=inflater.inflate(R.layout.message,null);


                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(v)
                        // Add action buttons
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText message = (EditText) v.findViewById(R.id.message);
                                if(message.getText().toString().equals("near")){
                                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(getResources().getString(R.string.localhost_base_url))
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    RetrofitInterface apiService =
                                            retrofit.create(RetrofitInterface.class);

                                    final Call<PoiResponse> call = apiService.listPOI();

                                    Log.e("vivek","Retrofit done");

                                    call.enqueue(new Callback<PoiResponse>() {
                                        @Override
                                        public void onResponse(Call<PoiResponse> call, Response<PoiResponse> response) {
                                            Log.e("vivek","Retrofit recieved");



                                            List<Status> poiResult=response.body().getStatus();

                                            Double min=Double.MAX_VALUE;
                                            Double minLat=0d;
                                            Double minLong=0d;
                                            for(int i=0;i<poiResult.size();i++) {
                                                Double distance=(SphericalUtil.computeDistanceBetween(
                                                        new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()),
                                                        new LatLng(Double.parseDouble(poiResult.get(i).getFields().getLatitude()),
                                                                Double.parseDouble(poiResult.get(i).getFields().getLongitude()))));
                                                if(distance<min){
                                                    min=distance;
                                                    minLat=Double.parseDouble(poiResult.get(i).getFields().getLatitude());
                                                    minLong=Double.parseDouble(poiResult.get(i).getFields().getLongitude());
                                                }
                                            }

                                                Intent intent = new Intent(PoiBrowserActivity.this, ArCamActivity.class);

                                                try {

                                                    intent.putExtra("SRC", "Current Location");
                                                    intent.putExtra("DEST", minLat + "," +
                                                            minLong);
                                                    intent.putExtra("SRCLATLNG", mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
                                                    intent.putExtra("DESTLATLNG", minLat + "," +
                                                            minLong);
                                                    startActivity(intent);
                                                    finish();
                                                } catch (NullPointerException npe) {
                                                    Log.d(TAG, "onClick: The IntentExtras are Empty");
                                                }
                                        }

                                        @Override
                                        public void onFailure(Call<PoiResponse> call, Throwable t) {
                                        }
                                    });
                                    return;

                                }
                                else if(message.getText().toString().equals("visibility")) {
                                    AlertDialog.Builder alert_dialog_builder_course_selection = new AlertDialog.Builder(PoiBrowserActivity.this);
                                    String []choice={"Low","Medium","High"};
                                    alert_dialog_builder_course_selection.setTitle("Select visiblity").setSingleChoiceItems(choice, -1, new DialogInterface.OnClickListener() {
                                        @Override
                                        //when selected identify the course index selected
                                        public void onClick(DialogInterface dialog, int selected_index) {
                                            selectedVisiblity= selected_index;
                                        }
                                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        //on positive response allow session password taking
                                        public void onClick(DialogInterface dialog, int selected_index) {
//                                            Log.e("asjd",""+selectedMess);
                                            myEditor.putInt("visiblity",selectedVisiblity);
                                            myEditor.apply();
//                                            mess.setText(messList.get(selectedMess)+" Hostel");
                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        //on negative response end the dialog
                                        public void onClick(DialogInterface dialog, int selected_index) {
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog alert_dialog_course_selection = alert_dialog_builder_course_selection.create();

                                    // show it
                                    alert_dialog_course_selection.show();
                                    return;
                                }
                                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(getResources().getString(R.string.localhost_base_url))
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                RetrofitInterface apiService =
                                        retrofit.create(RetrofitInterface.class);

                                final Call<DirectionsResponse> call = apiService.createMessage(myPreferences.getString("id",""),String.valueOf(mLastLocation.getLatitude()),String.valueOf(mLastLocation.getLongitude()),message.getText().toString());
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
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });


                builder.setMessage("Enter message to be sent");
                builder.setTitle("Send Message");

                AlertDialog d = builder.create();
                d.show();
            }
        });

    }

    void Poi_list_call(int radius){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.localhost_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface apiService =
                retrofit.create(RetrofitInterface.class);

        final Call<PoiResponse> call = apiService.listPOI();

        Log.e("vivek","Retrofit done");

        call.enqueue(new Callback<PoiResponse>() {
            @Override
            public void onResponse(Call<PoiResponse> call, Response<PoiResponse> response) {
                Log.e("vivek","Retrofit recieved");



                List<Status> poiResult=response.body().getStatus();

                Configure_AR_Status(poiResult);
            }

            @Override
            public void onFailure(Call<PoiResponse> call, Throwable t) {
            }
        });


    }

    void Poi_list_call_message(int radius){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.localhost_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface apiService =
                retrofit.create(RetrofitInterface.class);

        final Call<MessageResponse> call = apiService.listMessage(myPreferences.getString("id",""));

        Log.e("vivek","Retrofit done");

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.e("vivek","Retrofit recieved");



                List<com.univ.team12.navar.network.message.Status> poiResult=response.body().getStatus();

                Configure_AR_Status_message(poiResult);
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
            }
        });


    }

    void Poi_list_call2(int radius){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.localhost_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface apiService =
                retrofit.create(RetrofitInterface.class);

        final Call<Poi2Response> call = apiService.listPOI2(myPreferences.getString("id",""));

        Log.e("vivek","Retrofit done");

        call.enqueue(new Callback<Poi2Response>() {
            @Override
            public void onResponse(Call<Poi2Response> call, Response<Poi2Response> response) {
                Log.e("vivek","Retrofit recieved");



                List<com.univ.team12.navar.network.poi2.Status> poiResult=response.body().getStatus();

                Configure_AR_Status2(poiResult);
            }

            @Override
            public void onFailure(Call<Poi2Response> call, Throwable t) {
            }
        });


    }

    void Poi_details_call(String placeid){
        Log.e("vivek",placeid );

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.localhost_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface apiService =
                retrofit.create(RetrofitInterface.class);

        final Call<PlaceResponse> call = apiService.getPlaceDetail(placeid);

        call.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {

                poi_cardview.setVisibility(View.VISIBLE);

                final com.univ.team12.navar.network.place.Fields fields=response.body().getS().get(0).getFields();

                poi_place_name.setText(fields.getName());
                poi_place_addr.setText(fields.getPeopleStuck()+" stuck here and "+fields.getPeopleInjured()+" are injured");

                try {
                    new PoiPhotoAsync().execute("https://cdn-images-1.medium.com/max/2000/1*C51FyB82wHdzRTgEIsYKPw.jpeg");

                }catch (Exception e){
                    Log.d(TAG, "onResponse: "+e.getMessage());
                    Toast.makeText(PoiBrowserActivity.this, "No image available", Toast.LENGTH_SHORT).show();
                }

                poi_place_maps_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        try{
                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("http")
                                    .authority("maps.google.com")
                                    .appendPath("maps")
                                    .appendQueryParameter("saddr", mLastLocation.getLatitude()+","+mLastLocation.getLongitude())
                                    .appendQueryParameter("daddr",fields.getLatitude()+","+
                                                    fields.getLongitude());

                            intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse( builder.build().toString()));
                            startActivity(intent);
                            finish();
                        }catch (Exception e){
                            Log.d(TAG, "onClick: mapNav Exception caught");
                            Toast.makeText(PoiBrowserActivity.this, "Unable to Open Maps Navigation", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                poi_place_ar_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(PoiBrowserActivity.this,ArCamActivity.class);

                        try {
                            intent.putExtra("visiblity",1);
                            intent.putExtra("SRC", "Current Location");
                            intent.putExtra("DEST",  fields.getLatitude()+","+
                                    fields.getLongitude());
                            intent.putExtra("SRCLATLNG",  mLastLocation.getLatitude()+","+mLastLocation.getLongitude());
                            intent.putExtra("DESTLATLNG", fields.getLatitude()+","+
                                    fields.getLongitude());
                            startActivity(intent);
                            finish();
                        }catch (NullPointerException npe){
                            Log.d(TAG, "onClick: The IntentExtras are Empty");
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
            }
        });

    }

    void Poi_details_call2(String placeid){
        Log.e("vivek",placeid );

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.localhost_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface apiService =
                retrofit.create(RetrofitInterface.class);

        final Call<TypeResponse> call = apiService.getTypeDetail(placeid);

        call.enqueue(new Callback<TypeResponse>() {
            @Override
            public void onResponse(Call<TypeResponse> call, Response<TypeResponse> response) {

                poi_cardview.setVisibility(View.VISIBLE);

                final com.univ.team12.navar.network.type.Fields fields=response.body().getS().get(0).getFields();


                poi_place_name.setText(response.body().getUsername());
                if(fields.getStatus().equals("v"))
                    poi_place_addr.setText("Volunteer");
                if(fields.getStatus().equals("r"))
                    poi_place_addr.setText("Rescue Team");

                poi_place_maps_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        try{
                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("http")
                                    .authority("maps.google.com")
                                    .appendPath("maps")
                                    .appendQueryParameter("saddr", mLastLocation.getLatitude()+","+mLastLocation.getLongitude())
                                    .appendQueryParameter("daddr",fields.getLatitude()+","+
                                            fields.getLongitude());

                            intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse( builder.build().toString()));
                            startActivity(intent);
                            finish();
                        }catch (Exception e){
                            Log.d(TAG, "onClick: mapNav Exception caught");
                            Toast.makeText(PoiBrowserActivity.this, "Unable to Open Maps Navigation", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                poi_place_ar_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(PoiBrowserActivity.this,ArCamActivity.class);

                        try {
                            intent.putExtra("visiblity",1);
                            intent.putExtra("SRC", "Current Location");
                            intent.putExtra("DEST",  fields.getLatitude()+","+
                                    fields.getLongitude());
                            intent.putExtra("SRCLATLNG",  mLastLocation.getLatitude()+","+mLastLocation.getLongitude());
                            intent.putExtra("DESTLATLNG", fields.getLatitude()+","+
                                    fields.getLongitude());
                            startActivity(intent);
                            finish();
                        }catch (NullPointerException npe){
                            Log.d(TAG, "onClick: The IntentExtras are Empty");
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<TypeResponse> call, Throwable t) {
            }
        });

    }

    void Poi_details_call_message(String placeid){
        Log.e("vivek",placeid );

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.localhost_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface apiService =
                retrofit.create(RetrofitInterface.class);

        final Call<MessageResponse> call = apiService.getMessageDetail(placeid);

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {

                poi_cardview.setVisibility(View.VISIBLE);

                final com.univ.team12.navar.network.message.Fields fields=response.body().getStatus().get(0).getFields();

                poi_place_name.setText(response.body().getUsername());
                poi_place_addr.setText(fields.getMessage());

                try {
                    new PoiPhotoAsync().execute("https://cdn-images-1.medium.com/max/2000/1*C51FyB82wHdzRTgEIsYKPw.jpeg");

                }catch (Exception e){
                    Log.d(TAG, "onResponse: "+e.getMessage());
                    Toast.makeText(PoiBrowserActivity.this, "No image available", Toast.LENGTH_SHORT).show();
                }

                poi_place_maps_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        try{
                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("http")
                                    .authority("maps.google.com")
                                    .appendPath("maps")
                                    .appendQueryParameter("saddr", mLastLocation.getLatitude()+","+mLastLocation.getLongitude())
                                    .appendQueryParameter("daddr",fields.getLatitude()+","+
                                            fields.getLongitude());

                            intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse( builder.build().toString()));
                            startActivity(intent);
                            finish();
                        }catch (Exception e){
                            Log.d(TAG, "onClick: mapNav Exception caught");
                            Toast.makeText(PoiBrowserActivity.this, "Unable to Open Maps Navigation", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                poi_place_ar_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(PoiBrowserActivity.this,ArCamActivity.class);

                        try {
                            intent.putExtra("visiblity",1);
                            intent.putExtra("SRC", "Current Location");
                            intent.putExtra("DEST",  fields.getLatitude()+","+
                                    fields.getLongitude());
                            intent.putExtra("SRCLATLNG",  mLastLocation.getLatitude()+","+mLastLocation.getLongitude());
                            intent.putExtra("DESTLATLNG", fields.getLatitude()+","+
                                    fields.getLongitude());
                            startActivity(intent);
                            finish();
                        }catch (NullPointerException npe){
                            Log.d(TAG, "onClick: The IntentExtras are Empty");
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
            }
        });

    }

    public class PoiPhotoAsync extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            poi_place_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            poi_place_image.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];

            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    private void Configure_AR_Status(List<Status> pois){


//        GeoObject geoObjects[]=new GeoObject[pois.size()];

        for(int i=0;i<pois.size();i++) {
            GeoObject poiGeoObj=new GeoObject(1000*(i+1));
            //ArObject2 poiGeoObj=new ArObject2(1000*(i+1));

//            poiGeoObj.setImageUri(getImageUri(this,textAsBitmap(pois.get(i).getName(),10.0f, Color.WHITE)));
            poiGeoObj.setGeoPosition(Double.parseDouble(pois.get(i).getFields().getLatitude()),
                    Double.parseDouble(pois.get(i).getFields().getLongitude()));
            poiGeoObj.setName("s"+pois.get(i).getPk().toString());
            Log.e("vivek",poiGeoObj.getName());
            //poiGeoObj.setPlaceId(pois.get(0).getPlaceId());

            //Bitmap bitmap=textAsBitmap(pois.get(i).getName(),30.0f,Color.WHITE);

            Bitmap snapshot=null;
            View view= getLayoutInflater().inflate(R.layout.poi_container,null);
            TextView name= (TextView)view.findViewById(R.id.poi_container_name);
            TextView dist= (TextView)view.findViewById(R.id.poi_container_dist);
            ImageView icon=(ImageView)view.findViewById(R.id.poi_container_icon);

            name.setText(pois.get(i).getFields().getName());
            String distance=String.valueOf((SphericalUtil.computeDistanceBetween(
                    new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()),
                    new LatLng(Double.parseDouble(pois.get(i).getFields().getLatitude()),
                            Double.parseDouble(pois.get(i).getFields().getLongitude()))))/1000);
            String d=distance+" KM";
            dist.setText(d);
            Log.e("vivek",distance);
            Log.e("vivek",mLastLocation.getLatitude()+","+
                    mLastLocation.getLongitude());


            icon.setImageResource(R.drawable.group);



            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

            try {

                view.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                snapshot = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight()
                        , Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(snapshot);
                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                view.draw(canvas);

                Thread.sleep(100);
                //canvas.drawBitmap(snapshot);
                //snapshot = Bitmap.createBitmap(view.getDrawingCache(),10,10,200,100); // You can tell how to crop the snapshot and whatever in this method
            }
            catch (Exception e){
                Log.e("vivek","Excepection occured");
            }
            finally {
                view.setDrawingCacheEnabled(false);
            }

            String uri=saveToInternalStorage(snapshot,i+"x.png");

            //icon.setImageURI(Uri.parse(uri));
            poiGeoObj.setImageUri(uri);

            world.addBeyondarObject(poiGeoObj);
        }


    }

    private void Configure_AR_Status2(List<com.univ.team12.navar.network.poi2.Status> pois){


        for(int i=0;i<pois.size();i++) {

            if(pois.get(i).getFields().getLatitude()==null)
                continue;

            GeoObject poiGeoObj=new GeoObject(1000*(i+1));
            //ArObject2 poiGeoObj=new ArObject2(1000*(i+1));

//            poiGeoObj.setImageUri(getImageUri(this,textAsBitmap(pois.get(i).getName(),10.0f, Color.WHITE)));
            poiGeoObj.setGeoPosition(Double.parseDouble(pois.get(i).getFields().getLatitude().toString()),
                    Double.parseDouble(pois.get(i).getFields().getLongitude().toString()));
            poiGeoObj.setName("t"+pois.get(i).getPk().toString());
            //poiGeoObj.setPlaceId(pois.get(0).getPlaceId());

            //Bitmap bitmap=textAsBitmap(pois.get(i).getName(),30.0f,Color.WHITE);

            Bitmap snapshot = null;
            View view= getLayoutInflater().inflate(R.layout.poi_container,null);
            TextView name= (TextView)view.findViewById(R.id.poi_container_name);
            TextView dist= (TextView)view.findViewById(R.id.poi_container_dist);
            ImageView icon=(ImageView)view.findViewById(R.id.poi_container_icon);
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.localhost_base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitInterface apiService =
                    retrofit.create(RetrofitInterface.class);

            final Call<UsersResponse> call = apiService.getUserDetail(pois.get(i).getFields().getUser().toString());

            Log.e("vivek","Retrofit done");

            call.enqueue(new Callback<UsersResponse>() {
                @Override
                public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                    Log.e("vivek","Retrofit recieved");



                }

                @Override
                public void onFailure(Call<UsersResponse> call, Throwable t) {
                }
            });

            name.setText(pois.get(i).getFields().getUser().toString());
            String distance=String.valueOf((SphericalUtil.computeDistanceBetween(
                    new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()),
                    new LatLng(Double.parseDouble(pois.get(i).getFields().getLatitude().toString()),
                            Double.parseDouble(pois.get(i).getFields().getLongitude().toString()))))/1000);
            String d=distance+" KM";
            dist.setText(d);
            Log.e("vivek",distance);
            Log.e("vivek",mLastLocation.getLatitude()+","+
                    mLastLocation.getLongitude());

            if(pois.get(i).getFields().getStatus().equals("v"))
                icon.setImageResource(R.drawable.rescue);
            else if(pois.get(i).getFields().getStatus().equals("r"))
                icon.setImageResource(R.drawable.rescue);


            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

            try {

                view.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                snapshot = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight()
                        , Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(snapshot);
                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                view.draw(canvas);
            } finally {
                view.setDrawingCacheEnabled(false);
            }


            String uri=saveToInternalStorage(snapshot,pois.get(i).getPk()+".png");

            //icon.setImageURI(Uri.parse(uri));
            poiGeoObj.setImageUri(uri);

            world.addBeyondarObject(poiGeoObj);
        }

    }

    private void Configure_AR_Status_message(List<com.univ.team12.navar.network.message.Status> pois){


        for(int i=0;i<pois.size();i++) {

//            if(pois.get(i).getFields().getLatitude()==null)
//                continue;

            GeoObject poiGeoObj=new GeoObject(1000*(i+1));
            //ArObject2 poiGeoObj=new ArObject2(1000*(i+1));

//            poiGeoObj.setImageUri(getImageUri(this,textAsBitmap(pois.get(i).getName(),10.0f, Color.WHITE)));
            poiGeoObj.setGeoPosition(Double.parseDouble(pois.get(i).getFields().getLatitude().toString()),
                    Double.parseDouble(pois.get(i).getFields().getLongitude().toString()));
            poiGeoObj.setName("m"+pois.get(i).getPk().toString());
            //poiGeoObj.setPlaceId(pois.get(0).getPlaceId());

            //Bitmap bitmap=textAsBitmap(pois.get(i).getName(),30.0f,Color.WHITE);

            Bitmap snapshot = null;
            View view= getLayoutInflater().inflate(R.layout.poi_container,null);
            TextView name= (TextView)view.findViewById(R.id.poi_container_name);
            TextView dist= (TextView)view.findViewById(R.id.poi_container_dist);
            ImageView icon=(ImageView)view.findViewById(R.id.poi_container_icon);

            name.setText(pois.get(i).getFields().getMessage());
            String distance=String.valueOf((SphericalUtil.computeDistanceBetween(
                    new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()),
                    new LatLng(Double.parseDouble(pois.get(i).getFields().getLatitude().toString()),
                            Double.parseDouble(pois.get(i).getFields().getLongitude().toString()))))/1000);
            String d=distance+" KM";
            dist.setText(d);
            Log.e("vivek",distance);
            Log.e("vivek",mLastLocation.getLatitude()+","+
                    mLastLocation.getLongitude());

            icon.setImageResource(R.drawable.message);


            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

            try {

                view.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                snapshot = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight()
                        , Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(snapshot);
                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                view.draw(canvas);
                Thread.sleep(100);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                view.setDrawingCacheEnabled(false);
            }


            String uri=saveToInternalStorage(snapshot,i+"message.png");

            //icon.setImageURI(Uri.parse(uri));
            poiGeoObj.setImageUri(uri);

            world.addBeyondarObject(poiGeoObj);
        }


    }
    private String saveToInternalStorage(Bitmap bitmapImage,String name){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name);
        Log.d(TAG, "saveToInternalStorage: PATH:"+mypath.toString());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.toString();
    }

    public String getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path).toString();


    }

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    private void Set_googleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                try {

                    layoutInflater=getLayoutInflater();

                    world=new World(getApplicationContext());
                    world.setGeoPosition(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                    world.setDefaultImage(R.drawable.ar_sphere_default);

                    arFragmentSupport.getGLSurfaceView().setPullCloserDistance(25);
                    Poi_list_call(900);
                    Poi_list_call2(900);
                    Poi_list_call_message(900);

                    textView.setVisibility(View.INVISIBLE);

                    // ... and send it to the fragment
                    arFragmentSupport.setWorld(world);
                }catch (Exception e){
                    Log.d(TAG, "onCreate: Intent Error");
                }
            }
        }

    }

    @Override
    public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
        if (beyondarObjects.size() > 0) {
            if(beyondarObjects.get(0).getName().charAt(0)=='s'){

                Poi_details_call(beyondarObjects.get(0).getName().substring(1));
            }
            else if(beyondarObjects.get(0).getName().charAt(0)=='t'){

                Poi_details_call2(beyondarObjects.get(0).getName().substring(1));
            }
            else if(beyondarObjects.get(0).getName().charAt(0)=='m'){

                Poi_details_call_message(beyondarObjects.get(0).getName().substring(1));
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onTouchBeyondarView(MotionEvent event, ArBeyondarGLSurfaceView var2) {

        float x = event.getX();
        float y = event.getY();

        ArrayList<BeyondarObject> geoObjects = new ArrayList<BeyondarObject>();

        // This method call is better to don't do it in the UI thread!
        // This method is also available in the BeyondarFragment
        var2.getBeyondarObjectsOnScreenCoordinates(x, y, geoObjects);

        String textEvent = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                textEvent = "Event type ACTION_DOWN: ";
                break;
            case MotionEvent.ACTION_UP:
                textEvent = "Event type ACTION_UP: ";
                break;
            case MotionEvent.ACTION_MOVE:
                textEvent = "Event type ACTION_MOVE: ";
                break;
            default:
                break;
        }

        Iterator<BeyondarObject> iterator = geoObjects.iterator();
        while (iterator.hasNext()) {
            BeyondarObject geoObject = iterator.next();
            textEvent = textEvent + " " + geoObject.getName();
            Log.d(TAG, "onTouchBeyondarView: ATTENTION !!! "+textEvent);

            // ...
            // Do something
            // ...
        }
    }
}
