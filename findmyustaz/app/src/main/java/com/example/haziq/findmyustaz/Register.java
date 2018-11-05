package com.example.haziq.findmyustaz;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class Register extends AppCompatActivity {

    String location, latitude, longitude;;

    TextView txtLocation;
    EditText txtProfilename, txtContact, txtEmail, txtPassword, txtDetails;
    ProgressDialog progressDialog;
    ImageView clickLocation, btnUpload;
    List<Address> address;
    CircleImageView userPhoto2;

    int CODE_GALLERY_REQUEST = 999;
    int PLACE_PICKER_REQUEST = 1;

    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        txtProfilename = (EditText)findViewById(R.id.txtProfilename);
        txtContact = (EditText)findViewById(R.id.txtContact);
        txtDetails = (EditText)findViewById(R.id.txtDetails);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtLocation = (TextView)findViewById(R.id.txtLocation);

        userPhoto2 = findViewById(R.id.userPhoto2);

        btnUpload = (ImageView) findViewById(R.id.btnUpload);

        clickLocation = (ImageView) findViewById(R.id.clickLocation);

        //Button for Maps
        clickLocation.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try
                {
                    startActivityForResult(builder.build(Register.this), PLACE_PICKER_REQUEST);
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

        //Upload Image
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
            }
        });

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

    //Maps
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, Register.this);

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try
                {
                    address = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude, 1);

                    String area = address.get(0).getLocality();
                    String city = address.get(0).getAdminArea();

                    latitude = new Double(address.get(0).getLatitude()).toString();
                    longitude = new Double(address.get(0).getLongitude()).toString();

                    location = area+", "+city;

                } catch (IOException e)
                {

                    e.printStackTrace();
                }

                txtLocation.setText(location);
            }
        }

        if(requestCode==CODE_GALLERY_REQUEST && resultCode==RESULT_OK && data != null)
        {
            Uri filePath = data.getData();

            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                userPhoto2.setImageBitmap(bitmap);
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

    public void OnRegister(View view){


        final String profilename = txtProfilename.getText().toString();
        final String details = txtDetails.getText().toString();
        final String contact = txtContact.getText().toString();
        final String email = txtEmail.getText().toString();
        final String password = txtPassword.getText().toString();
        final String place = txtLocation.getText().toString();
        final String slatitude = latitude;
        final String slongitude = longitude;
        final String sphoto = imageToString(bitmap);

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Email required");
            txtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(location)) {
            txtLocation.setError("Please insert your location");
            txtLocation.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("Password required");
            txtPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(profilename)) {
            txtProfilename.setError("Profile name required");
            txtProfilename.requestFocus();
            return;
        }

        if(sphoto.isEmpty() && sphoto == null) {
            class register extends AsyncTask<Void, Void, String> {


                @Override
                protected void onPreExecute() {

                    progressDialog = ProgressDialog.show(Register.this, "Loading Data", null, true, true);
                }

                @Override
                protected void onPostExecute(String httpResponseMsg) {
                    super.onPostExecute(httpResponseMsg);

                    progressDialog.dismiss();

                    if (httpResponseMsg.equalsIgnoreCase("You have been successfully registered. Please check your email for verification")) {

                        Toast.makeText(Register.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this, Login.class);
                        finish();
                        startActivity(intent);

                    } else {

                        Toast.makeText(Register.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(UserConfig.PROFILENAME, profilename);
                    params.put(UserConfig.DETAILS, details);
                    params.put(UserConfig.LOCATION, place);
                    params.put(UserConfig.LATITUDE, slatitude);
                    params.put(UserConfig.LONGITUDE, slongitude);
                    params.put(UserConfig.CONTACT, contact);
                    params.put(UserConfig.EMAIL, email);
                    params.put(UserConfig.PASSWORD, password);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(UserConfig.URL_SIGNUP, params);
                    return res;
                }
            }

            register reg = new register();
            reg.execute();
        }

        else
        {
            class register extends AsyncTask<Void, Void, String> {


                @Override
                protected void onPreExecute() {

                    progressDialog = ProgressDialog.show(Register.this, "Loading Data", null, true, true);
                }

                @Override
                protected void onPostExecute(String httpResponseMsg) {
                    super.onPostExecute(httpResponseMsg);

                    progressDialog.dismiss();

                    if (httpResponseMsg.equalsIgnoreCase("You have been successfully registered. Please check your email for verification")) {

                        Toast.makeText(Register.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this, Login.class);
                        finish();
                        startActivity(intent);

                    } else {

                        Toast.makeText(Register.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(UserConfig.PROFILENAME, profilename);
                    params.put(UserConfig.DETAILS, details);
                    params.put(UserConfig.LOCATION, place);
                    params.put(UserConfig.LATITUDE, slatitude);
                    params.put(UserConfig.LONGITUDE, slongitude);
                    params.put(UserConfig.CONTACT, contact);
                    params.put(UserConfig.EMAIL, email);
                    params.put(UserConfig.PASSWORD, password);
                    params.put(UserConfig.PHOTO, sphoto);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(UserConfig.URL_SIGNUP, params);
                    return res;
                }
            }

            register reg = new register();
            reg.execute();
        }
    }

    public void OnReset(View view)
    {

        txtProfilename.setText("");
        txtDetails.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtLocation.setText("");
    }
}
