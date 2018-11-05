package com.example.haziq.findmyustaz;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUpdate extends AppCompatActivity implements View.OnClickListener {

    Button btnUpdate;
    TextView txtLocation;
    EditText txtProfilename, txtDetails, txtContact, txtPassword;
    String userid, useremail, location, details, contact, profilename, password, photo;
    ImageView clickLocation, btnUpload;
    CircleImageView userPhoto1;
    List<Address> address;
    ProgressDialog loading, loading1;

    Bitmap bitmap;

    String latitude, longitude;

    int PLACE_PICKER_REQUEST = 1, CODE_GALLERY_REQUEST = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileupdate);

        txtProfilename = (EditText)findViewById(R.id.txtProfilename);
        txtContact = (EditText)findViewById(R.id.txtContact);
        txtDetails = (EditText)findViewById(R.id.txtDetails);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtLocation = (TextView)findViewById(R.id.txtLocation);

        userPhoto1 = findViewById(R.id.userPhoto1);
        btnUpload = (ImageView) findViewById(R.id.btnUpload);

        clickLocation = (ImageView) findViewById(R.id.clickLocation);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(this);

        //Button for Maps
        clickLocation.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try
                {
                    startActivityForResult(builder.build(ProfileUpdate.this), PLACE_PICKER_REQUEST);
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

        Intent intent = getIntent();

        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        getUserInfo();

        //Upload Image
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ProfileUpdate.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
            }
        });
    }

    //Maps
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, ProfileUpdate.this);

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
                userPhoto1.setImageBitmap(bitmap);
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


    private void getUserInfo(){
        class GetUserID extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileUpdate.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showUser(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_USERINFO, useremail);
                return s;
            }
        }
        GetUserID gu = new GetUserID();
        gu.execute();
    }

    private void showUser(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            profilename = c.getString(UserConfig.TAG_PROFILENAME);
            details = c.getString(UserConfig.TAG_DETAILS);
            location = c.getString(UserConfig.TAG_LOCATION);
            latitude = c.getString(UserConfig.TAG_LATITUDE);
            longitude = c.getString(UserConfig.TAG_LONGITUDE);
            password = c.getString(UserConfig.TAG_PASSWORD);
            contact = c.getString(UserConfig.TAG_CONTACT);
            photo = c.getString(UserConfig.TAG_PHOTO);

            if (photo.isEmpty())
            {
                Picasso.with(this)
                        .load(R.drawable.man)
                        .placeholder(R.drawable.man)
                        .error(R.drawable.man)
                        .into(userPhoto1);

            }
            else
            {
                Picasso.with(this)
                        .load(photo)
                        .placeholder(R.drawable.man)
                        .error(R.drawable.man)
                        .into(userPhoto1);
            }

            txtProfilename.setText(profilename);
            txtLocation.setText(location);
            txtDetails.setText(details);
            txtContact.setText(contact);
            txtPassword.setText(password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateProfile() {

        final String profilename = txtProfilename.getText().toString().trim();
        final String details = txtDetails.getText().toString().trim();
        final String location = txtLocation.getText().toString().trim();
        final String contact = txtContact.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();
        final String photo = imageToString(bitmap);

        final String slatitude = latitude;
        final String slongitude = longitude;

        if (TextUtils.isEmpty(profilename)) {
            txtProfilename.setError("Please insert profile name");
            txtProfilename.requestFocus();
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

        if(photo.isEmpty() && photo == null)
        {
            class UpdateProfile1 extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading1 = ProgressDialog.show(ProfileUpdate.this, "Updating data...", "Please wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(ProfileUpdate.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(UserConfig.ID, userid);
                    hashMap.put(UserConfig.PROFILENAME, profilename);
                    hashMap.put(UserConfig.DETAILS, details);
                    hashMap.put(UserConfig.LOCATION, location);
                    hashMap.put(UserConfig.LATITUDE, slatitude);
                    hashMap.put(UserConfig.LONGITUDE, slongitude);
                    hashMap.put(UserConfig.CONTACT, contact);
                    hashMap.put(UserConfig.PASSWORD, password);

                    RequestHandler rh = new RequestHandler();

                    String s = rh.sendPostRequest(UserConfig.URL_USERUPDATE, hashMap);

                    return s;
                }
            }

            UpdateProfile1 up1 = new UpdateProfile1();
            up1.execute();
        }
        else
        {
             class UpdateProfile2 extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading1 = ProgressDialog.show(ProfileUpdate.this, "Updating data...", "Please wait...", false, false);
                }

                @Override
                protected void onPostExecute(String string) {
                    super.onPostExecute(string);
                    Toast.makeText(ProfileUpdate.this, string, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(UserConfig.ID, userid);
                    hashMap.put(UserConfig.PROFILENAME, profilename);
                    hashMap.put(UserConfig.DETAILS, details);
                    hashMap.put(UserConfig.LOCATION, location);
                    hashMap.put(UserConfig.LATITUDE, slatitude);
                    hashMap.put(UserConfig.LONGITUDE, slongitude);
                    hashMap.put(UserConfig.CONTACT, contact);
                    hashMap.put(UserConfig.PASSWORD, password);
                    hashMap.put(UserConfig.PHOTO, photo);

                    RequestHandler rh = new RequestHandler();

                    String s = rh.sendPostRequest(UserConfig.URL_USERUPDATE, hashMap);

                    return s;
                }
            }

            UpdateProfile2 up2 = new UpdateProfile2();
            up2.execute();
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

    @Override
    public void onClick(View v) {
        if(v == btnUpdate){
            updateProfile();
            Intent intent = new Intent(this, Profile.class);
            finish();

            Bundle bundle = new Bundle();
            bundle.putString(UserConfig.TAG_ID, userid);
            bundle.putString(UserConfig.EMAIL, useremail);
            bundle.putString(UserConfig.TAG_PROFILENAME, profilename);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    public void OnReset (View view)
    {
        txtProfilename.setText("");
        txtDetails.setText("");
        txtLocation.setText("");
        txtContact.setText("");
        txtPassword.setText("");
    }

    public void SearchPage(View view)
    {
        Intent intent = new Intent(this, Search.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void NotificationPage(View view)
    {
        Intent intent = new Intent(this, Favourite.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void HomePage(View view)
    {
        Intent intent = new Intent(this, Dashboard.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void ProfilePage(View view)
    {
        Intent intent = new Intent(this, Profile.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
