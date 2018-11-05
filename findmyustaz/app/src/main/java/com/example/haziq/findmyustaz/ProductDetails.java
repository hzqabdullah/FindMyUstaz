package com.example.haziq.findmyustaz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {

    TextView txtpname, txtprice, txtquantity, txtdesc, txtustaz;
    String useremail, userid, JSON_STRING;
    String productid, uid, ustazname, pname, price, qty, desc, image, datetime;
    ImageView ivimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productdetails);

        txtpname = findViewById(R.id.txtpname);
        txtprice = findViewById(R.id.txtprice);
        txtquantity = findViewById(R.id.txtquantity);
        txtdesc = findViewById(R.id.txtdesc);

        txtustaz = findViewById(R.id.txtustaz);
        ivimage = findViewById(R.id.ivimage);

        Intent intent = getIntent();

        productid = intent.getStringExtra(UserConfig.P_ID);


        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);



        getUstazProduct();

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
                                datetime = dateString;
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

    private void getUstazProduct(){
        class getustazproduct extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProductDetails.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showUstazProduct(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_DETAILSPRODUCT, productid);
                return s;
            }
        }
        getustazproduct gup = new getustazproduct();
        gup.execute();
    }

    private void showUstazProduct(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);

            pname = c.getString(UserConfig.TAG_PNAME);
            price = c.getString(UserConfig.TAG_PPRICE);
            qty = c.getString(UserConfig.TAG_PQUANTITY);
            desc = c.getString(UserConfig.TAG_PDESC);
            ustazname = c.getString(UserConfig.TAG_UPROFILENAME);
            uid = c.getString(UserConfig.TAG_UID);
            image = c.getString(UserConfig.TAG_PIMAGE);

            if (image.isEmpty())
            {
                Picasso.with(this)
                        .load(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(ivimage);
            }
            else
            {
                Picasso.with(this)
                        .load(image)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(ivimage);
            }

            txtpname.setText("Name : "+pname);
            txtprice.setText("Price : RM"+price);
            txtquantity.setText("Quantity : "+qty);
            txtdesc.setText(desc);
            txtustaz.setText("From "+ustazname+" Shop");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public void GoToUstazShop(View view)
    {
        Intent intent = new Intent(this, UstazShop.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.TAG_UID, uid);
        bundle.putString(UserConfig.TAG_UPROFILENAME, ustazname);
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

    public void AddToCart(View view)
    {
        class addCart extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProductDetails.this, "Updating data...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);


                if (httpResponseMsg.equalsIgnoreCase("Product is added into your cart list")) {


                    Toast.makeText(ProductDetails.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(ProductDetails.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }

                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(UserConfig.TAG_ID, userid);
                hashMap.put(UserConfig.TAG_PID, productid);
                hashMap.put(UserConfig.TAG_PNAME, pname);
                hashMap.put(UserConfig.TAG_PPRICE, price);
                hashMap.put(UserConfig.TAG_PIMAGE, image);
                hashMap.put(UserConfig.TAG_UPROFILENAME, ustazname);
                hashMap.put(UserConfig.TAG_ADDED, datetime);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(UserConfig.URL_ADDCART, hashMap);

                return s;
            }

            ;
        }
        addCart ac = new addCart();
        ac.execute();
    }
}
