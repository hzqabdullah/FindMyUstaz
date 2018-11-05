package com.example.macbook.eventapp.activity.Ustaz.Shop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventAdd;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventConfig;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ShopAdd extends AppCompatActivity {

    EditText txtPName, txtPPrice, txtPDesc, txtPQty;
    TextView txtPStatus, datetime;
    String uid, uemail, dateString;
    int CODE_GALLERY_REQUEST = 999;
    ImageView productBanner, btnUpload;

    Bitmap bitmap;

    String [] status = new String [] {"Available", "Not Available"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopadd);

        txtPName = (EditText) findViewById(R.id.txtPName);
        txtPPrice = (EditText) findViewById(R.id.txtPPrice);
        txtPDesc = (EditText) findViewById(R.id.txtPDesc);
        txtPQty = (EditText) findViewById(R.id.txtPQty);
        txtPStatus = (TextView) findViewById(R.id.txtPStatus);
        productBanner = (ImageView) findViewById(R.id.productBanner);
        btnUpload = (ImageView) findViewById(R.id.btnUpload);

        datetime = findViewById(R.id.datetime);

        Spinner spinnerStatus;
        ArrayAdapter<String> adapter;

        Intent intent = getIntent();
        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.UEMAIL);

        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus) ;

        final List<String> StatusList = new ArrayList<>(Arrays.asList(status));

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, StatusList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                String selectedItem = (String) parent.getItemAtPosition(i);
                txtPStatus.setText(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        //Upload Image
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ShopAdd.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if(requestCode==CODE_GALLERY_REQUEST && resultCode==RESULT_OK && data != null)
        {
            Uri filePath = data.getData();

            try
            {
                //InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                productBanner.setImageBitmap(bitmap);
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


    public void shopAdd (View view)
    {
        final String name = txtPName.getText().toString();
        final String price = txtPPrice.getText().toString();
        final String desc = txtPDesc.getText().toString();
        final String qty = txtPQty.getText().toString();
        final String status = txtPStatus.getText().toString();
        final String insert = datetime.getText().toString();
        final String update = datetime.getText().toString();
        final String image = imageToString(bitmap);

        if (TextUtils.isEmpty(name)) {
            txtPName.setError("Please insert product name");
            txtPName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(price)) {
            txtPPrice.setError("Please insert product price");
            txtPPrice.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(qty)) {
            txtPQty.setError("Please insert product quantity");
            txtPQty.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(status)) {
            txtPStatus.setError("Please insert product status");
            txtPStatus.requestFocus();
            return;
        }

        if(image.isEmpty() && image == null) {

            class AddProduct extends AsyncTask<Void, Void, String> {


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(ShopAdd.this, s, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), ShopDisplay.class);
                    finish();

                    Bundle bundle = new Bundle();
                    bundle.putString(Config.TAG_UID, uid);
                    bundle.putString(Config.UEMAIL, uemail);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(ShopConfig.PNAME, name);
                    params.put(ShopConfig.PPRICE, price);
                    params.put(ShopConfig.PDESC, desc);
                    params.put(ShopConfig.PQUANTITY, qty);
                    params.put(ShopConfig.PSTATUS, status);
                    params.put(ShopConfig.PINSERT, insert);
                    params.put(ShopConfig.PUPDATE, update);
                    params.put(ShopConfig.UID, uid);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(ShopConfig.URL_PRODUCTADD, params);
                    return res;
                }
            }

            AddProduct ap = new AddProduct();
            ap.execute();

        }

        else
        {
            class AddProduct extends AsyncTask<Void, Void, String> {


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(ShopAdd.this, s, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), ShopDisplay.class);
                    finish();

                    Bundle bundle = new Bundle();
                    bundle.putString(Config.TAG_UID, uid);
                    bundle.putString(Config.UEMAIL, uemail);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(ShopConfig.PNAME, name);
                    params.put(ShopConfig.PPRICE, price);
                    params.put(ShopConfig.PDESC, desc);
                    params.put(ShopConfig.PQUANTITY, qty);
                    params.put(ShopConfig.PSTATUS, status);
                    params.put(ShopConfig.PIMAGE, image);
                    params.put(ShopConfig.PINSERT, insert);
                    params.put(ShopConfig.PUPDATE, update);
                    params.put(ShopConfig.UID, uid);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(ShopConfig.URL_PRODUCTADD, params);
                    return res;
                }
            }

            AddProduct ap = new AddProduct();
            ap.execute();
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

    public void Clear(View view)
    {
        txtPName.setText("");
        txtPPrice.setText("");
        txtPDesc.setText("");
        txtPQty.setText("");
        txtPStatus.setText("");

    }

    public void GoToShopActivity(View view)
    {

        Intent intent = new Intent(this, ShopActivity.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
