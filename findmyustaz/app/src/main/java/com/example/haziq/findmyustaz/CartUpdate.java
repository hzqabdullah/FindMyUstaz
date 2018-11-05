package com.example.haziq.findmyustaz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CartUpdate extends AppCompatActivity implements View.OnClickListener {

    String useremail, userid, profilename, cid, JSON_STRING;
    TextView txtpname, txtprice, txtdesc, txttotal, txtadded, txtustaz;
    EditText txtqty;
    String pname, price, desc, qty, image, total, added, ustaz, datetime;
    Button btnCartUpdate;
    ImageView ivimage;
    ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cartupdate);

        txtpname = findViewById(R.id.txtpname);
        txtprice = findViewById(R.id.txtprice);
        txtqty = findViewById(R.id.txtqty);
        txtdesc = findViewById(R.id.txtdesc);
        txttotal = findViewById(R.id.txttotal);
        txtadded = findViewById(R.id.txtadded);
        txtustaz = findViewById(R.id.txtustaz);
        ivimage = findViewById(R.id.ivimage);

        btnCartUpdate = (Button) findViewById(R.id.btnCartUpdate);

        btnCartUpdate.setOnClickListener(this);


        Intent intent = getIntent();

        userid = intent.getStringExtra(UserConfig.TAG_ID);
        profilename = intent.getStringExtra(UserConfig.TAG_PROFILENAME);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        cid = intent.getStringExtra(UserConfig.C_ID);

        getCartDetails();

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

    private void getCartDetails(){
        class getustazproduct extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CartUpdate.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showCartList(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_DETAILSCART, cid);
                return s;
            }
        }
        getustazproduct gup = new getustazproduct();
        gup.execute();
    }

    private void showCartList(String json) {
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);

            pname = c.getString(UserConfig.TAG_PNAME);
            price = c.getString(UserConfig.TAG_PPRICE);
            qty = c.getString(UserConfig.TAG_PQUANTITY);
            desc = c.getString(UserConfig.TAG_PDESC);
            total = c.getString(UserConfig.TAG_TOTALPRICE);
            added = c.getString(UserConfig.TAG_ADDED);
            ustaz = c.getString(UserConfig.TAG_UPROFILENAME);
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
            txtqty.setText(qty);
            txtdesc.setText(desc);
            txttotal.setText("Total Price : RM"+total);
            txtustaz.setText("From "+ustaz+" Shop");
            txtadded.setText("Added on : "+added);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void updateCart() {

        final String qty = txtqty.getText().toString().trim();

        if (TextUtils.isEmpty(qty)) {
            txtqty.setError("Please insert quantity");
            txtqty.requestFocus();
            return;
        }


        class UpdateCart extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CartUpdate.this, "Updating data...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(CartUpdate.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(UserConfig.TAG_CID, cid);
                hashMap.put(UserConfig.TAG_PQUANTITY, qty);
                hashMap.put(UserConfig.TAG_ADDED, datetime);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(UserConfig.URL_UPDATECART, hashMap);

                return s;
            }
        }

        UpdateCart uc = new UpdateCart();
        uc.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == btnCartUpdate){
            updateCart();
            Intent intent = new Intent(this, Cart.class);
            finish();

            Bundle bundle = new Bundle();
            bundle.putString(UserConfig.TAG_ID, userid);
            bundle.putString(UserConfig.EMAIL, useremail);
            bundle.putString(UserConfig.TAG_PROFILENAME, profilename);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void RemoveCart(View view) {
        confirmDeleteCart();
    }

    public void deleteCart()
    {        class DeleteCart extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CartUpdate.this, "Updating data...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(CartUpdate.this, s, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), Cart.class);
                finish();


                Bundle bundle = new Bundle();
                bundle.putString(UserConfig.TAG_ID, userid);
                bundle.putString(UserConfig.EMAIL, useremail);
                bundle.putString(UserConfig.TAG_PROFILENAME, profilename);

                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_DELETECART, cid);
                return s;
            }
        }

        DeleteCart dc = new DeleteCart();
        dc.execute();

    }

    private void confirmDeleteCart(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to remove this product from your cart?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteCart();

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

    public void GoToCart(View view)
    {
        Intent intent = new Intent(this, Cart.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        bundle.putString(UserConfig.TAG_PROFILENAME, profilename);

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

}
