package com.example.macbook.eventapp.activity.Ustaz.Event;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.picasso.Picasso;


public class EventManage extends AppCompatActivity implements View.OnClickListener{

    EditText txtTitle, txtDate, txtDateEnd, txtTime, txtCategory, txtVenue;
    TextView txtDay, txtPlace, datetime;
    Spinner spinnerDay;
    ArrayAdapter<String> adapter;

    private Button btnUpdate;
    private Button btnDelete;

    ImageView clickPlace, clickDate, clickDate2, clickTime, eventBanner, btnUpload;
    DatePickerDialog.OnDateSetListener DataSetListener, DataSetListener2;

    private String id, uid, uemail, dateString;
    private static final String TAG = "EventManage";
    int PLACE_PICKER_REQUEST = 1;

    String elatitude, elongitude;

    List<Address> address;

    Bitmap bitmap;

    int CODE_GALLERY_REQUEST = 999;


    String [] days = new String [] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventmanage);

        Intent intent = getIntent();

        id = intent.getStringExtra(EventConfig.E_ID);
        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.UEMAIL);

        txtCategory = (EditText) findViewById(R.id.txtCategory);
        txtDay = (TextView) findViewById(R.id.txtDay);
        txtVenue = (EditText) findViewById(R.id.txtVenue);
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtDate = (EditText) findViewById(R.id.txtDate);
        txtDateEnd = (EditText) findViewById(R.id.txtDateEnd);
        txtTime = (EditText) findViewById(R.id.txtTime);
        txtPlace = (TextView) findViewById(R.id.txtPlace);
        eventBanner = (ImageView) findViewById(R.id.eventBanner);
        clickDate = (ImageView) findViewById(R.id.clickDate);
        clickDate2 = (ImageView) findViewById(R.id.clickDate2);
        clickTime = (ImageView) findViewById(R.id.clickTime);
        clickPlace = (ImageView) findViewById(R.id.clickPlace);
        datetime = findViewById(R.id.datetime);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnUpload = (ImageView) findViewById(R.id.btnUpload);

        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        //txtID.setText(id);

        spinnerDay = (Spinner) findViewById(R.id.spinnerDay) ;

        final List<String> dayList = new ArrayList<>(Arrays.asList(days));

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, dayList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapter);
        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                String selectedItem = (String) parent.getItemAtPosition(i);
                txtDay.setText(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        getEvent();

        //Upload Image
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(EventManage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
            }
        });

        //Button for Calendar
        clickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EventManage.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DataSetListener,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        clickDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EventManage.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DataSetListener2,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //Calendar
        DataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "Date: dd,mm,yyyy" +day+ "-" +month+ "-" +year);
                String calendardate = +day+ "-" +month+ "-" +year;
                txtDate.setText(calendardate);
            }
        };

        //Button for Clock
        clickTime.setOnClickListener(new View.OnClickListener(){

            Calendar clock = Calendar.getInstance();

            @Override
            public void onClick(View v2) {
                new TimePickerDialog(EventManage.this, onTimeSetListener, clock.get(Calendar.HOUR_OF_DAY), clock.get(Calendar.MINUTE), true).show();
            }
        });

        //Calendar
        DataSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "Date: dd,mm,yyyy" +day+ "-" +month+ "-" +year);
                String calendardate2 = +day+ "-" +month+ "-" +year;
                txtDateEnd.setText(calendardate2);
            }
        };

        //Button for Maps
        clickPlace.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v3) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try
                {
                    startActivityForResult(builder.build(EventManage.this), PLACE_PICKER_REQUEST);
                }
                catch (GooglePlayServicesRepairableException e)
                {
                    e.printStackTrace();
                }
                catch (GooglePlayServicesNotAvailableException e)
                {
                    e.printStackTrace();
                }
            }
        });

        Thread thread = new Thread(){
            @Override
            public void run() {
                try
                {
                    while(!isInterrupted())
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh-mm-ss a");
                                dateString = sdf.format(date);
                                datetime.setText(dateString);
                            }
                        });
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };
        thread.start();


    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String newDate = +hourOfDay+":"+minute;
            txtTime.setText(newDate);
        }
    };

    //Maps
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==PLACE_PICKER_REQUEST)
        {
            if(resultCode==RESULT_OK)
            {
                Place newplace = PlacePicker.getPlace(data, EventManage.this);

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try
                {
                    address = geocoder.getFromLocation(newplace.getLatLng().latitude,newplace.getLatLng().longitude, 1);

                    elatitude = new Double(address.get(0).getLatitude()).toString();
                    elongitude = new Double(address.get(0).getLongitude()).toString();


                } catch (IOException e)
                {

                    e.printStackTrace();
                }
                txtPlace.setText(newplace.getAddress());
            }
        }

        if(requestCode==CODE_GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            Uri filePath = data.getData();

            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                eventBanner.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    private void getEvent(){
        class GetEvent extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventManage.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEvent(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(EventConfig.URL_EVENTDETAILS, id);
                return s;
            }
        }
        GetEvent ge = new GetEvent();
        ge.execute();
    }

    private void showEvent(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(EventConfig.TAG_JSON_ARRAY);
            JSONObject jo = result.getJSONObject(0);
            String id = jo.getString(EventConfig.TAG_EID);
            String category = jo.getString(EventConfig.TAG_ECATEGORY);
            String title = jo.getString(EventConfig.TAG_ETITLE);
            String date = jo.getString(EventConfig.TAG_EDATE);
            String dateend = jo.getString(EventConfig.TAG_EDATEEND);
            String day = jo.getString(EventConfig.TAG_EDAY);
            String time = jo.getString(EventConfig.TAG_ETIME);
            String place = jo.getString(EventConfig.TAG_EPLACE);
            String venue = jo.getString(EventConfig.TAG_EVENUE);
            String image = jo.getString(EventConfig.TAG_EIMAGE);

            elatitude = jo.getString(EventConfig.TAG_ELATITUDE);
            elongitude = jo.getString(EventConfig.TAG_ELONGITUDE);

            txtTitle.setText(title);
            txtDate.setText(date);
            txtDateEnd.setText(dateend);
            txtTime.setText(time);
            txtPlace.setText(place);
            txtDay.setText(day);
            txtCategory.setText(category);
            txtVenue.setText(venue);

            if (image.isEmpty())
            { //url.isEmpty()
                Picasso.with(this)
                        .load(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(eventBanner);

            }
            else
            {
                Picasso.with(this)
                        .load(image)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(eventBanner); //this is your ImageView
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateEvent(){
        final String title = txtTitle.getText().toString().trim();
        final String date = txtDate.getText().toString().trim();
        final String dateend = txtDateEnd.getText().toString().trim();
        final String time = txtTime.getText().toString().trim();
        final String place = txtPlace.getText().toString().trim();
        final String day = txtDay.getText().toString().trim();
        final String category = txtCategory.getText().toString().trim();
        final String venue = txtVenue.getText().toString().trim();
        final String update = datetime.getText().toString();
        final String image = imageToString(bitmap);

        final String latitude = elatitude;
        final String longitude = elongitude;

        if (TextUtils.isEmpty(date)) {
            txtDate.setError("Please insert start date");
            txtDate.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(dateend)) {
            txtDateEnd.setError("Please insert end date");
            txtDateEnd.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(day)) {
            txtDate.setError("Please insert the day of event");
            txtDate.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(place)) {
            txtPlace.setError("Please insert location");
            txtPlace.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(venue)) {
            txtPlace.setError("Please insert event venue");
            txtPlace.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(time)) {
            txtTime.setError("Please insert time");
            txtTime.requestFocus();
            return;
        }

        if(image.isEmpty() && image == null)
        {

            class UpdateEvent extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(EventManage.this, "Updating data...", "Please wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(EventManage.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(EventConfig.EID, id);
                    hashMap.put(EventConfig.ECATEGORY, category);
                    hashMap.put(EventConfig.EDAY, day);
                    hashMap.put(EventConfig.EVENUE, venue);
                    hashMap.put(EventConfig.ETITLE, title);
                    hashMap.put(EventConfig.EDATE, date);
                    hashMap.put(EventConfig.EDATEEND, dateend);
                    hashMap.put(EventConfig.ETIME, time);
                    hashMap.put(EventConfig.EPLACE, place);
                    hashMap.put(EventConfig.ELATITUDE, latitude);
                    hashMap.put(EventConfig.ELONGITUDE, longitude);
                    hashMap.put(EventConfig.EUPDATE, update);

                    RequestHandler rh = new RequestHandler();

                    String s = rh.sendPostRequest(EventConfig.URL_EVENTUPDATE, hashMap);

                    return s;
                }
            }

            UpdateEvent ue = new UpdateEvent();
            ue.execute();
        }

        else
        {
            class UpdateEvent extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(EventManage.this, "Updating data...", "Please wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(EventManage.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(EventConfig.EID, id);
                    hashMap.put(EventConfig.ECATEGORY, category);
                    hashMap.put(EventConfig.EDAY, day);
                    hashMap.put(EventConfig.EVENUE, venue);
                    hashMap.put(EventConfig.ETITLE, title);
                    hashMap.put(EventConfig.EDATE, date);
                    hashMap.put(EventConfig.EDATEEND, dateend);
                    hashMap.put(EventConfig.ETIME, time);
                    hashMap.put(EventConfig.EPLACE, place);
                    hashMap.put(EventConfig.ELATITUDE, latitude);
                    hashMap.put(EventConfig.ELONGITUDE, longitude);
                    hashMap.put(EventConfig.EUPDATE, update);
                    hashMap.put(EventConfig.EIMAGE, image);

                    RequestHandler rh = new RequestHandler();

                    String s = rh.sendPostRequest(EventConfig.URL_EVENTUPDATE, hashMap);

                    return s;
                }
            }

            UpdateEvent ue = new UpdateEvent();
            ue.execute();
        }
    }

    private String imageToString (Bitmap bmp)
    {
        final String encodedImage;

        if (bmp != null)
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte [] imageBytes = outputStream.toByteArray();

            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        }
        else
        {
            encodedImage = "";
        }

        return encodedImage;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int [] grantResults) {

        if(requestCode == CODE_GALLERY_REQUEST)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "You dont have permission to access the gallery", Toast.LENGTH_LONG).show();
            }

            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void deleteEvent(){
        class DeleteEvent extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventManage.this, "Updating data...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(EventManage.this, s, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), EventDisplay.class);
                finish();

                Bundle bundle = new Bundle();
                bundle.putString(Config.TAG_UID, uid);
                bundle.putString(Config.UEMAIL, uemail);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(EventConfig.URL_EVENTDELETE, id);
                return s;
            }
        }

        DeleteEvent de = new DeleteEvent();
        de.execute();
    }

    private void confirmDeleteEvent(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteEvent();

                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == btnUpdate){
            updateEvent();
            Intent intent = new Intent(this, EventDetails.class);
            finish();

            Bundle bundle = new Bundle();
            bundle.putString(EventConfig.E_ID, id);
            bundle.putString(Config.TAG_UID, uid);
            bundle.putString(Config.UEMAIL, uemail);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        if(v == btnDelete){
            confirmDeleteEvent();

        }
    }

    public void GoToEventDetails(View view)
    {

        Intent intent = new Intent(this, EventDetails.class);
        finish();

        Bundle bundle = new Bundle();

        bundle.putString(EventConfig.E_ID, id);
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
