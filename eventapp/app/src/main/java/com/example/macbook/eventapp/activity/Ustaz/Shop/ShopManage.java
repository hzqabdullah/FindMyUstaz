package com.example.macbook.eventapp.activity.Ustaz.Shop;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventConfig;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ShopManage extends AppCompatActivity implements View.OnClickListener {

    EditText txtName, txtPrice, txtDesc, txtQty;
    String id, uid, uemail, dateString;
    TextView txtStatus, datetime;
    ImageView productBanner, btnUpload;

    int CODE_GALLERY_REQUEST = 999;

    Bitmap bitmap;

    private Button btnUpdateProduct;
    private Button btnDeleteProduct;

    Spinner spinnerStatus;
    ArrayAdapter<String> adapter;

    String [] status = new String [] {"Available", "Not Available"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopmanage);

        txtName = (EditText) findViewById(R.id.txtName);
        txtPrice = (EditText) findViewById(R.id.txtPrice);
        txtDesc = (EditText) findViewById(R.id.txtDesc);
        txtQty = (EditText) findViewById(R.id.txtQty);
        txtStatus = (TextView) findViewById(R.id.txtStatus);

        datetime = findViewById(R.id.datetime);

        btnUpdateProduct = (Button) findViewById(R.id.btnUpdateProduct);
        btnDeleteProduct = (Button) findViewById(R.id.btnDeleteProduct);
        productBanner = (ImageView) findViewById(R.id.productBanner);
        btnUpload = (ImageView) findViewById(R.id.btnUpload);

        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus) ;

        final List<String> StatusList = new ArrayList<>(Arrays.asList(status));

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, StatusList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                String selectedItem = (String) parent.getItemAtPosition(i);
                txtStatus.setText(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        Intent intent = getIntent();
        id = intent.getStringExtra(ShopConfig.P_ID);
        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.UEMAIL);

        btnUpdateProduct.setOnClickListener(this);
        btnDeleteProduct.setOnClickListener(this);

        getProduct();

        //Upload Image
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ShopManage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
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

    private void getProduct(){
        class GetProduct extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShopManage.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showProduct(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(ShopConfig.URL_PRODUCTDETAILS, id);
                return s;
            }
        }
        GetProduct gp = new GetProduct();
        gp.execute();
    }

    private void showProduct(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(ShopConfig.TAG_JSON_ARRAY);
            JSONObject jo = result.getJSONObject(0);
            String id = jo.getString(ShopConfig.TAG_PID);
            String name = jo.getString(ShopConfig.TAG_PNAME);
            String price = jo.getString(ShopConfig.TAG_PPRICE);
            String desc = jo.getString(ShopConfig.TAG_PDESC);
            String quantity = jo.getString(ShopConfig.TAG_PQUANTITY);
            String status = jo.getString(ShopConfig.TAG_PSTATUS);
            String image = jo.getString(ShopConfig.TAG_PIMAGE);

            txtName.setText(name);
            txtPrice.setText(price);
            txtDesc.setText(desc);
            txtQty.setText(quantity);
            txtStatus.setText(status);

            if (image.isEmpty())
            { //url.isEmpty()
                Picasso.with(this)
                        .load(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(productBanner);

            }
            else
            {
                Picasso.with(this)
                        .load(image)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(productBanner); //this is your ImageView
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateProduct(){
        final String name = txtName.getText().toString().trim();
        final String price = txtPrice.getText().toString().trim();
        final String desc = txtDesc.getText().toString().trim();
        final String quantity = txtQty.getText().toString().trim();
        final String status = txtStatus.getText().toString().trim();
        final String update = datetime.getText().toString();
        final String image = imageToString(bitmap);

        if (TextUtils.isEmpty(name)) {
            txtName.setError("Please insert product name");
            txtName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(price)) {
            txtPrice.setError("Please insert product price");
            txtPrice.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(quantity)) {
            txtQty.setError("Please insert product quantity");
            txtQty.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(status)) {
            txtStatus.setError("Please insert product status");
            txtStatus.requestFocus();
            return;
        }

        if(image.isEmpty() && image == null) {

            class UpdateProduct1 extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(ShopManage.this, "Updating data...", "Please wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(ShopManage.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(ShopConfig.PID, id);
                    hashMap.put(ShopConfig.PNAME, name);
                    hashMap.put(ShopConfig.PPRICE, price);
                    hashMap.put(ShopConfig.PDESC, desc);
                    hashMap.put(ShopConfig.PQUANTITY, quantity);
                    hashMap.put(ShopConfig.PSTATUS, status);
                    hashMap.put(ShopConfig.PUPDATE, update);

                    RequestHandler rh = new RequestHandler();

                    String s = rh.sendPostRequest(ShopConfig.URL_PRODUCTUPDATE, hashMap);

                    return s;
                }
            }

            UpdateProduct1 up = new UpdateProduct1();
            up.execute();
        }
        else
        {
            class UpdateProduct2 extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(ShopManage.this, "Updating data...", "Please wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(ShopManage.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(ShopConfig.PID, id);
                    hashMap.put(ShopConfig.PNAME, name);
                    hashMap.put(ShopConfig.PPRICE, price);
                    hashMap.put(ShopConfig.PDESC, desc);
                    hashMap.put(ShopConfig.PIMAGE, image);
                    hashMap.put(ShopConfig.PQUANTITY, quantity);
                    hashMap.put(ShopConfig.PSTATUS, status);
                    hashMap.put(ShopConfig.PUPDATE, update);

                    RequestHandler rh = new RequestHandler();

                    String s = rh.sendPostRequest(ShopConfig.URL_PRODUCTUPDATE, hashMap);

                    return s;
                }
            }

            UpdateProduct2 up = new UpdateProduct2();
            up.execute();
        }
    }

    private void deleteProduct(){
        class DeleteEvent extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShopManage.this, "Updating data...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ShopManage.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(ShopConfig.URL_PRODUCTDELETE, id);
                return s;
            }
        }

        DeleteEvent de = new DeleteEvent();
        de.execute();
    }

    private void confirmDeleteProduct(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteProduct();

                        Intent intent = new Intent(getApplicationContext(), ShopDisplay.class);
                        finish();

                        Bundle bundle = new Bundle();
                        bundle.putString(Config.TAG_UID, uid);
                        bundle.putString(Config.UEMAIL, uemail);
                        intent.putExtras(bundle);
                        startActivity(intent);

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
        if(v == btnUpdateProduct){
            updateProduct();
            Intent intent = new Intent(this, ShopDetails.class);
            finish();

            Bundle bundle = new Bundle();
            bundle.putString(ShopConfig.P_ID, id);
            bundle.putString(Config.TAG_UID, uid);
            bundle.putString(Config.UEMAIL, uemail);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        if(v == btnDeleteProduct){
            confirmDeleteProduct();

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

    public void GoToShopDetails(View view)
    {

        Intent intent = new Intent(this, ShopDetails.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(ShopConfig.P_ID, id);
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
