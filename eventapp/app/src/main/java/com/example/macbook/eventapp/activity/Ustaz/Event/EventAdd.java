package com.example.macbook.eventapp.activity.Ustaz.Event;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class EventAdd extends AppCompatActivity {


    String [] days = new String [] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"} ;


    private static final String TAG = "EventAdd";

    int CODE_GALLERY_REQUEST = 999;
    int PLACE_PICKER_REQUEST = 1;

    TextView txtDay, txtPlace, datetime;
    EditText txtCategory, txtTitle, txtDate, txtTime, txtVenue, txtDateEnd;
    ImageView clickPlace, clickDate, clickDate2, clickTime, eventBanner, btnUpload;
    Spinner spinnerDay;
    DatePickerDialog.OnDateSetListener DataSetListener, DataSetListener2;
    String uid, uemail, dateString;
    Bitmap bitmap;
    ArrayAdapter<String> adapter;
    List<Address> address;

    String elatitude, elongitude;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventadd);

        txtCategory = (EditText) findViewById(R.id.txtCategory);
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtDate = (EditText) findViewById(R.id.txtDate);
        txtDateEnd = (EditText) findViewById(R.id.txtDateEnd);
        txtDay = (TextView) findViewById(R.id.txtDay);
        txtTime = (EditText) findViewById(R.id.txtTime);
        txtVenue = (EditText) findViewById(R.id.txtVenue);
        txtPlace = (TextView) findViewById(R.id.txtPlace);
        clickDate = (ImageView) findViewById(R.id.clickDate);
        clickDate2 = (ImageView) findViewById(R.id.clickDate2);
        clickTime = (ImageView) findViewById(R.id.clickTime);
        clickPlace = (ImageView) findViewById(R.id.clickPlace);
        btnUpload = (ImageView) findViewById(R.id.btnUpload);
        eventBanner = (ImageView) findViewById(R.id.eventBanner);
        datetime = findViewById(R.id.datetime);

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

        Intent intent = getIntent();
        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.UEMAIL);


        //Upload Image
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(EventAdd.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
            }
        });

        //Button for Calendar
        clickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EventAdd.this,
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
                        EventAdd.this,
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


        //Button for Clock
        clickTime.setOnClickListener(new View.OnClickListener(){

            Calendar clock = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                new TimePickerDialog(EventAdd.this, onTimeSetListener, clock.get(Calendar.HOUR_OF_DAY), clock.get(Calendar.MINUTE), true).show();
            }
        });

        //Button for Maps
        clickPlace.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try
                {
                    startActivityForResult(builder.build(EventAdd.this), PLACE_PICKER_REQUEST);
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

    //Clock

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
                Place newplace = PlacePicker.getPlace(data, EventAdd.this);

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

        if(requestCode==CODE_GALLERY_REQUEST && resultCode==RESULT_OK && data != null)
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



    public void eventAdd(View view) {

        final String category = txtCategory.getText().toString();
        final String title = txtTitle.getText().toString();
        final String date = txtDate.getText().toString();
        final String dateend = txtDateEnd.getText().toString();
        final String day = txtDay.getText().toString();
        final String time = txtTime.getText().toString();
        final String venue = txtVenue.getText().toString();
        final String place = txtPlace.getText().toString();
        final String latitude = elatitude;
        final String longitude = elongitude;
        final String image = imageToString(bitmap);
        final String insert = datetime.getText().toString();
        final String update = datetime.getText().toString();

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

            class AddEvent extends AsyncTask<Void, Void, String> {


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if(s.equalsIgnoreCase("New event has been created")){

                        Toast.makeText(EventAdd.this, s, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), EventDisplay.class);
                        finish();

                        Bundle bundle = new Bundle();
                        bundle.putString(Config.TAG_UID, uid);
                        bundle.putString(Config.UEMAIL, uemail);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                    else{

                        Toast.makeText(EventAdd.this, s, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(EventConfig.ECATEGORY, category);
                    params.put(EventConfig.ETITLE, title);
                    params.put(EventConfig.EDATE, date);
                    params.put(EventConfig.EDATEEND, dateend);
                    params.put(EventConfig.EDAY, day);
                    params.put(EventConfig.ETIME, time);
                    params.put(EventConfig.EVENUE, venue);
                    params.put(EventConfig.EPLACE, place);
                    params.put(EventConfig.ELATITUDE, latitude);
                    params.put(EventConfig.ELONGITUDE, longitude);
                    params.put(EventConfig.EINSERT, insert);
                    params.put(EventConfig.EUPDATE, update);
                    params.put(EventConfig.UID, uid);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(EventConfig.URL_EVENTADD, params);
                    return res;
                }
            }

            AddEvent ae = new AddEvent();
            ae.execute();
        }
        else
        {
            class AddEvent extends AsyncTask<Void, Void, String> {


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if(s.equalsIgnoreCase("New event has been created")){

                        Toast.makeText(EventAdd.this, s, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), EventDisplay.class);
                        finish();

                        Bundle bundle = new Bundle();
                        bundle.putString(Config.TAG_UID, uid);
                        bundle.putString(Config.UEMAIL, uemail);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                    else
                    {

                        Toast.makeText(EventAdd.this, s, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(EventConfig.ECATEGORY, category);
                    params.put(EventConfig.ETITLE, title);
                    params.put(EventConfig.EDATE, date);
                    params.put(EventConfig.EDATEEND, dateend);
                    params.put(EventConfig.EDAY, day);
                    params.put(EventConfig.ETIME, time);
                    params.put(EventConfig.EVENUE, venue);
                    params.put(EventConfig.EPLACE, place);
                    params.put(EventConfig.ELATITUDE, latitude);
                    params.put(EventConfig.ELONGITUDE, longitude);
                    params.put(EventConfig.EIMAGE, image);
                    params.put(EventConfig.EINSERT, insert);
                    params.put(EventConfig.EUPDATE, update);
                    params.put(EventConfig.UID, uid);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(EventConfig.URL_EVENTADD, params);
                    return res;
                }
            }

            AddEvent ae = new AddEvent();
            ae.execute();
        }
    }

    public void Clear(View view)
    {
            txtTitle.setText("");
            txtDate.setText("");
            txtDateEnd.setText("");
            txtTime.setText("");
            txtPlace.setText("");
            txtDay.setText("");
            txtCategory.setText("");
            txtVenue.setText("");
    }

    public void GoToEventDisplay(View view)
    {

        Intent intent = new Intent(this, EventDisplay.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
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
}




