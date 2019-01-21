package com.example.admin.placepicker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.seismic.ShakeDetector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlacePickerActivity extends AppCompatActivity implements ShakeDetector.Listener{

    int PLACE_PICKER_REQUEST = 1;
    TextView tvPlace;

    private static String URL_REGIST = "https://nightingalenath2.000webhostapp.com/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        tvPlace = (TextView)findViewById(R.id.tvPlace);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        ShakeDetector shakeDetector = new ShakeDetector(this);

        shakeDetector.start(sensorManager);

    }

    public void goPlacePicker(View view)
    {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(PlacePickerActivity.this), PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesRepairableException e){
            e.printStackTrace();
        }catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(PlacePickerActivity.this, data);
                tvPlace.setText(place.getAddress());
                registerPlace();
            }
        }
    }

    @Override
    public void hearShake() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(PlacePickerActivity.this), PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesRepairableException e){
            e.printStackTrace();
        }catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }

    }

    private void registerPlace()
    {
        final String place = this.tvPlace.getText().toString().trim();


        addPlace(place);

    }

    private void addPlace(String place)
    {
        String urlSuffix="?place="+place;
        class RegisterUser extends AsyncTask<String,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PlacePickerActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Registered", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... params) {

                String s = params[0];
                BufferedReader bufferedReader = null;

                try {

                    URL url = new URL(URL_REGIST+s);
                    HttpURLConnection con=(HttpURLConnection)url.openConnection();
                    bufferedReader=new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String result;
                    result=bufferedReader.readLine();
                    return  result;

                }catch(Exception e){
                    return null;
                }
            }
        }

        RegisterUser ur=new RegisterUser();
        ur.execute(urlSuffix);

    }

}
