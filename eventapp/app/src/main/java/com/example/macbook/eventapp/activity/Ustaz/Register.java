package com.example.macbook.eventapp.activity.Ustaz;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.macbook.eventapp.activity.Ustaz.Event.EventAdd;
import com.example.macbook.eventapp.activity.Ustaz.Profile.ProfileUpdate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    String [] hometown = new String [] {"Johor", "Kedah", "Kelantan", "Kuala Lumpur", "Labuan", "Melaka", "Perak", "Putrajaya",
            "Negeri Sembilan", "Pahang", "Perlis", "Pulau Pinang", "Sabah", "Sarawak", "Selangor", "Terengganu", "Lain-lain"} ;

    EditText txtFirstname, txtLastname, txtProfilename, txtContact, txtEmail, txtPassword, txtDob, txtICNo;
    TextView txtPlace;
    ImageView clickDate, btnUpload;
    Spinner spinner;
    ArrayAdapter<String> adapter;
    ProgressDialog progressDialog;
    CircleImageView ustazPhoto;

    Bitmap bitmap;

    int CODE_GALLERY_REQUEST = 999;

    DatePickerDialog.OnDateSetListener DataSetListener;
    private static final String TAG = "ProfileUpdate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        txtFirstname = (EditText)findViewById(R.id.txtFirstname);
        txtLastname = (EditText)findViewById(R.id.txtLastname);
        txtProfilename = (EditText)findViewById(R.id.txtProfilename);
        txtContact = (EditText)findViewById(R.id.txtContact);
        txtICNo = (EditText)findViewById(R.id.txtICNo);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtDob = (EditText)findViewById(R.id.txtDob);
        txtPlace = (TextView) findViewById(R.id.txtPlace);
        spinner = (Spinner) findViewById(R.id.spinner) ;

        ustazPhoto = findViewById(R.id.ustazPhoto);
        btnUpload = (ImageView) findViewById(R.id.btnUpload);

        clickDate = (ImageView) findViewById(R.id.clickDate);

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

        //Button for Calendar
        clickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Register.this,
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
                ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
            }
        });
    }

    public void OnRegister(View view){

        final String ufirstname = txtFirstname.getText().toString();
        final String ulastname = txtLastname.getText().toString();
        final String uprofilename = txtProfilename.getText().toString();
        final String udob = txtDob.getText().toString();
        final String uplace = txtPlace.getText().toString();
        final String ucontact = txtContact.getText().toString();
        final String uicno = txtICNo.getText().toString();
        final String uemail = txtEmail.getText().toString();
        final String upassword = txtPassword.getText().toString();
        final String uphoto = imageToString(bitmap);

        if (TextUtils.isEmpty(uicno)) {
            txtICNo.setError("IC No required");
            txtICNo.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(uemail)) {
            txtEmail.setError("Email required");
            txtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(uplace)) {
            txtPlace.setError("Hometown required");
            txtPlace.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(upassword)) {
            txtPassword.setError("Password required");
            txtPassword.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(ufirstname)) {
            txtFirstname.setError("First name required");
            txtFirstname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(uprofilename)) {
            txtProfilename.setError("Profile name required");
            txtProfilename.requestFocus();
            return;
        }

        if(uphoto.isEmpty() && uphoto == null)
        {
            class register extends AsyncTask<Void,Void,String> {


                @Override
                protected void onPreExecute() {

                    progressDialog = ProgressDialog.show(Register.this,"Loading Data",null,true,true);
                }

                @Override
                protected void onPostExecute(String httpResponseMsg) {
                    super.onPostExecute(httpResponseMsg);

                    progressDialog.dismiss();

                    if(httpResponseMsg.equalsIgnoreCase("Successfully registered. Account approval required before login. Please check your email")){

                        Toast.makeText(Register.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this, Login.class);
                        finish();
                        startActivity(intent);

                    }
                    else{

                        Toast.makeText(Register.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String,String> params = new HashMap<>();
                    params.put(Config.UFIRSTNAME, ufirstname);
                    params.put(Config.ULASTNAME, ulastname);
                    params.put(Config.UPROFILENAME, uprofilename);
                    params.put(Config.UICNO, uicno);
                    params.put(Config.UDOB, udob);
                    params.put(Config.UPLACE, uplace);
                    params.put(Config.UCONTACT, ucontact);
                    params.put(Config.UEMAIL, uemail);
                    params.put(Config.UPASSWORD, upassword);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.URL_SIGNUP, params);
                    return res;
                }
            }

            register reg = new register();
            reg.execute();
        }

        else
        {
            class register1 extends AsyncTask<Void,Void,String> {


                @Override
                protected void onPreExecute() {

                    progressDialog = ProgressDialog.show(Register.this,"Loading Data",null,true,true);
                }

                @Override
                protected void onPostExecute(String httpResponseMsg) {
                    super.onPostExecute(httpResponseMsg);

                    progressDialog.dismiss();

                    if(httpResponseMsg.equalsIgnoreCase("Successfully registered. Account approval required before login. Please check your email")){

                        Toast.makeText(Register.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this, Login.class);
                        finish();
                        startActivity(intent);

                    }
                    else{

                        Toast.makeText(Register.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String,String> params = new HashMap<>();
                    params.put(Config.UFIRSTNAME, ufirstname);
                    params.put(Config.ULASTNAME, ulastname);
                    params.put(Config.UPROFILENAME, uprofilename);
                    params.put(Config.UICNO, uicno);
                    params.put(Config.UDOB, udob);
                    params.put(Config.UPLACE, uplace);
                    params.put(Config.UCONTACT, ucontact);
                    params.put(Config.UEMAIL, uemail);
                    params.put(Config.UPASSWORD, upassword);
                    params.put(Config.UPHOTO, uphoto);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.URL_SIGNUP, params);
                    return res;
                }
            }

            register1 reg = new register1();
            reg.execute();
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

    public void OnReset(View view)
    {
        txtFirstname.setText("");
        txtLastname.setText("");
        txtProfilename.setText("");
        txtICNo.setText("");
        txtDob.setText("");
        txtPlace.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }
}
