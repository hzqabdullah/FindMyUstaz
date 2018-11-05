package com.example.macbook.eventapp.activity.Ustaz.Profile;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventAdd;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.picasso.Picasso;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUpdate extends AppCompatActivity implements View.OnClickListener{

    String [] hometown = new String [] {"Johor", "Kedah", "Kelantan", "Kuala Lumpur", "Labuan", "Melaka", "Perak", "Putrajaya",
            "Negeri Sembilan", "Pahang", "Perlis", "Pulau Pinang", "Sabah", "Sarawak", "Selangor", "Terengganu", "Lain-lain"} ;

    EditText txtFirstname, txtLastname, txtDob, txtProfilename, txtContact, txtPassword;
    TextView txtPlace, datetime;
    String uid, uemail, firstname, lastname, place, dob, contact, profilename, image, password;
    ImageView clickDate, btnUpload;
    private Button btnUpdate, btnReset;
    Spinner spinner;
    ArrayAdapter<String> adapter;
    Bitmap bitmap;

    CircleImageView ustazPhoto;

    int CODE_GALLERY_REQUEST = 999;

    DatePickerDialog.OnDateSetListener DataSetListener;
    private static final String TAG = "ProfileUpdate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileupdate);

        txtFirstname = (EditText) findViewById(R.id.txtFirstname);
        txtLastname = (EditText) findViewById(R.id.txtLastname);
        txtProfilename = (EditText) findViewById(R.id.txtProfilename);
        txtDob = (EditText) findViewById(R.id.txtDob);
        txtPlace = (TextView) findViewById(R.id.txtPlace);
        txtContact = (EditText) findViewById(R.id.txtContact);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        ustazPhoto = (CircleImageView) findViewById(R.id.ustazPhoto);
        btnUpload = (ImageView) findViewById(R.id.btnUpload);

        datetime = findViewById(R.id.datetime);

        spinner = (Spinner) findViewById(R.id.spinner) ;

        final List<String> hometownList = new ArrayList<>(Arrays.asList(hometown));

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, hometownList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

               String selectedItem = (String) parent.getItemAtPosition(i);
               txtPlace.setText(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        clickDate = (ImageView) findViewById(R.id.clickDate);
        btnUpdate = (Button) findViewById(R.id.btnProfileUpdate);

        btnUpdate.setOnClickListener(this);

        Intent intent = getIntent();
        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.UEMAIL);

        getUstazProfile();

        //Button for Calendar
        clickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ProfileUpdate.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DataSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //Calendar
        DataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "Date: dd,mm,yyyy" + day + "/" + month + "/" + year);
                String calendardate = +day + "/" + month + "/" + year;
                txtDob.setText(calendardate);
            }
        };

        //Upload Image
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ProfileUpdate.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
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
                                SimpleDateFormat sdf = new SimpleDateFormat("hh-mm-ss a  dd MMM yyyy");
                                String dateString = sdf.format(date);
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

    private void getUstazProfile() {
        class GetUstazInfo extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileUpdate.this, "Data fetching...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showProfileUstaz(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_USTAZPROFILE, uid);
                return s;
            }
        }
        GetUstazInfo gui = new GetUstazInfo();
        gui.execute();
    }

    private void showProfileUstaz(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            firstname = c.getString(Config.TAG_UFIRSTNAME);
            lastname = c.getString(Config.TAG_ULASTNAME);
            profilename = c.getString(Config.TAG_UPROFILENAME);
            place = c.getString(Config.TAG_UPLACE);
            dob = c.getString(Config.TAG_UDOB);
            contact = c.getString(Config.TAG_UCONTACT);
            image = c.getString(Config.TAG_UPHOTO);
            password = c.getString(Config.TAG_UPASSWORD);
            uid = c.getString(Config.TAG_UID);

            txtFirstname.setText(firstname);
            txtLastname.setText(lastname);
            txtProfilename.setText(profilename);

            if (image.isEmpty())
            { //url.isEmpty()
                Picasso.with(this)
                        .load(R.drawable.ustaz)
                        .placeholder(R.drawable.ustaz)
                        .error(R.drawable.ustaz)
                        .into(ustazPhoto);

            }
            else
            {
                Picasso.with(this)
                        .load(image)
                        .placeholder(R.drawable.ustaz)
                        .error(R.drawable.ustaz)
                        .into(ustazPhoto); //this is your ImageView
            }


            txtPlace.setText(place);
            txtDob.setText(dob);
            txtContact.setText(contact);
            txtPassword.setText(password);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateProfile() {
        final String firstname = txtFirstname.getText().toString().trim();
        final String lastname = txtLastname.getText().toString().trim();
        final String profilename = txtProfilename.getText().toString().trim();
        final String place = txtPlace.getText().toString().trim();
        final String dob = txtDob.getText().toString().trim();
        final String contact = txtContact.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();
        final String image = imageToString(bitmap);


        if (TextUtils.isEmpty(place)) {
            txtPlace.setError("Hometown required");
            txtPlace.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("Password required");
            txtPassword.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(firstname)) {
            txtFirstname.setError("First name required");
            txtFirstname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(profilename)) {
            txtProfilename.setError("Profile name required");
            txtProfilename.requestFocus();
            return;
        }

        if(image.isEmpty() && image == null)
        {
            class UpdateProfile extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(ProfileUpdate.this, "Updating data...", "Please wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(ProfileUpdate.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(Config.UID, uid);
                    hashMap.put(Config.UFIRSTNAME, firstname);
                    hashMap.put(Config.ULASTNAME, lastname);
                    hashMap.put(Config.UPROFILENAME, profilename);
                    hashMap.put(Config.UDOB, dob);
                    hashMap.put(Config.UPLACE, place);
                    hashMap.put(Config.UCONTACT, contact);
                    hashMap.put(Config.UPASSWORD, password);

                    RequestHandler rh = new RequestHandler();

                    String s = rh.sendPostRequest(Config.URL_PROFILEUPDATE, hashMap);

                    return s;
                }
            }

            UpdateProfile up = new UpdateProfile();
            up.execute();
        }

        else
        {
            class UpdateProfile1 extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(ProfileUpdate.this, "Updating data...", "Please wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(ProfileUpdate.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(Config.UID, uid);
                    hashMap.put(Config.UFIRSTNAME, firstname);
                    hashMap.put(Config.ULASTNAME, lastname);
                    hashMap.put(Config.UPROFILENAME, profilename);
                    hashMap.put(Config.UDOB, dob);
                    hashMap.put(Config.UPLACE, place);
                    hashMap.put(Config.UCONTACT, contact);
                    hashMap.put(Config.UPASSWORD, password);
                    hashMap.put(Config.UPHOTO, image);

                    RequestHandler rh = new RequestHandler();

                    String s = rh.sendPostRequest(Config.URL_PROFILEUPDATE, hashMap);

                    return s;
                }
            }

            UpdateProfile1 up = new UpdateProfile1();
            up.execute();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==CODE_GALLERY_REQUEST && resultCode==RESULT_OK && data != null)
        {
            Uri filePath = data.getData();

            try
            {
                //InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ustazPhoto.setImageBitmap(bitmap);
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

    @Override
    public void onClick(View v) {
        if(v == btnUpdate){
            updateProfile();
            Intent intent = new Intent(this, ProfileActivity.class);
            finish();

            Bundle bundle = new Bundle();
            bundle.putString(Config.TAG_UID, uid);
            bundle.putString(Config.UEMAIL, uemail);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void btnProfileReset (View view)
    {
            txtFirstname.setText("");
            txtLastname.setText("");
            txtProfilename.setText("");
            txtDob.setText("");
            txtPlace.setText("");
            txtContact.setText("");
    }

    public void GoToProfileActivity(View view)
    {

        Intent intent = new Intent(this, ProfileActivity.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
