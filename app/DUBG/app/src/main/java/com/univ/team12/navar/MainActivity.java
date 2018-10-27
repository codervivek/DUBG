package com.univ.team12.navar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.univ.team12.navar.network.PlaceResponse;
import com.univ.team12.navar.network.PoiResponse;
import com.univ.team12.navar.network.RetrofitInterface;
import com.univ.team12.navar.network.UsersResponse;
import com.univ.team12.navar.network.poi.Status;
import com.univ.team12.navar.network.users.User;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button ar;
    private Button login;
    private Button report;
    private SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;
    int selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ar = (Button)findViewById(R.id.ar);
        login = (Button)findViewById(R.id.login);
        report = (Button)findViewById(R.id.SOS);
        ar.setOnClickListener(this);
        login.setOnClickListener(this);
        report.setOnClickListener(this);
        myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        myEditor = myPreferences.edit();

    }

    @Override
    public void onClick(View v) {             //If sign in button is clicked call sign in function
        int i = v.getId();
        if (i == R.id.ar) {
            Intent intent = new Intent(MainActivity.this, PoiBrowserActivity.class);
            startActivity(intent);
        }
        else if (i == R.id.login) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.localhost_base_url))
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitInterface apiService =
                    retrofit.create(RetrofitInterface.class);

            final Call<UsersResponse> call = apiService.listUsers();
            call.enqueue(new Callback<UsersResponse>() {
                @Override
                public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {


                    List<User> users=response.body().getUser();



                    final String id[] =new String [users.size()];
                    final String items[] =new String [users.size()];
                    for (int i = 0; i< users.size(); i++){
                        items[i]= users.get(i).getFields().getUsername();
                        id[i]= users.get(i).getPk().toString();
                    }
                    // build dialog to display course option to choose from
                    AlertDialog.Builder alert_dialog_builder_course_selection = new AlertDialog.Builder(MainActivity.this);
                    alert_dialog_builder_course_selection.setTitle("Select Users").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        @Override
                        //when selected identify the course index selected
                        public void onClick(DialogInterface dialog, int selected_index) {
                            selectedUser= selected_index;
                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        //on positive response allow session password taking
                        public void onClick(DialogInterface dialog, int selected_index) {
                            myEditor.putString("user",items[selectedUser]);
                            myEditor.putString("id",id[selectedUser]);
                            myEditor.commit();
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
                }

                @Override
                public void onFailure(Call<UsersResponse> call, Throwable t) {
                }
            });



        }
        else if (i == R.id.SOS) {
            Intent intent = new Intent(MainActivity.this, Report_status.class);
            startActivity(intent);
        }
    }
}
