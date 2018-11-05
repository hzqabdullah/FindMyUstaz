package com.example.haziq.findmyustaz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class PaymentInfo extends AppCompatActivity {

    String useremail, profilename, userid, userfullname, useraddress, usercontact, totalprice, datetime, email, JSON_STRING;
    TextView txtTotalPrice;
    ListView paylistView;
    EditText txtFullname, txtAddress, txtContact, txtEmail;


    PayPalConfiguration payPalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(UserConfig.PAYPAL_CLIENTID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentinfo);

        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        paylistView = findViewById(R.id.paylistView);

        txtEmail = findViewById(R.id.txtEmail);
        txtFullname = findViewById(R.id.txtFullname);
        txtAddress = findViewById(R.id.txtAddress);
        txtContact = findViewById(R.id.txtContact);

        Intent intent = getIntent();

        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);
        profilename = intent.getStringExtra(UserConfig.TAG_PROFILENAME);
        totalprice = intent.getStringExtra(UserConfig.TAG_CARTTOTAL);

        txtEmail.setText(useremail);
        txtFullname.setText(profilename);
        txtTotalPrice.setText("RM "+totalprice);

        getUserCart();

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
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a  dd MMM yyyy");
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

    private void getUserCart(){
        class GetUserCart extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PaymentInfo.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showCartList();
            }

            @Override
            protected String doInBackground(Void... v) {

                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_DISPLAYCART, userid);
                return s;
            }
        }
        GetUserCart guc = new GetUserCart();
        guc.execute();
    }

    private void showCartList() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> cartlist = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String cid = jo.getString(UserConfig.TAG_CID);
                String price = "Price : RM"+jo.getString(UserConfig.TAG_PPRICE);
                String qty = "("+jo.getString(UserConfig.TAG_PQUANTITY)+")";
                String name = jo.getString(UserConfig.TAG_PNAME);
                String totalprice = "Total : RM"+jo.getString(UserConfig.TAG_TOTALPRICE);

                HashMap<String, String> cart = new HashMap<>();
                cart.put(UserConfig.TAG_CID, cid);
                cart.put(UserConfig.TAG_PPRICE, price);
                cart.put(UserConfig.TAG_PQUANTITY, qty);
                cart.put(UserConfig.TAG_PNAME, name);
                cart.put(UserConfig.TAG_TOTALPRICE, totalprice);


                cartlist.add(cart);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        PaymentAdapter payadapter = new PaymentAdapter(this, cartlist);
        paylistView.setAdapter(payadapter);

    }


    public void GoPayNow(View view)
    {
        confirmPay();
    }
    private void confirmPay()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("All products will be removed from your cart. Are you sure you want to make an order?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        PayNow();

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

    public void PayNow()
    {
        final Double price = Double.valueOf(totalprice);

        userfullname = txtFullname.getText().toString();
        useraddress = txtAddress.getText().toString();
        email = txtEmail.getText().toString();
        usercontact = txtContact.getText().toString();

        if(price == 0 || price == null)
        {
            Toast.makeText(PaymentInfo.this, "Total price cannot be zero. Please insert product into your cart", Toast.LENGTH_LONG).show();
        }

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Email required");
            txtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(userfullname)) {
            txtFullname.setError("Please insert your full name");
            txtFullname.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(useraddress)) {
            txtAddress.setError("Please insert your address");
            txtAddress.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(usercontact)) {
            txtContact.setError("Profile name required");
            txtContact.requestFocus();
            return;
        }

        class payNow extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PaymentInfo.this, "Updating data...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(PaymentInfo.this, s, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                finish();

                Bundle bundle = new Bundle();
                bundle.putString(UserConfig.TAG_ID, userid);
                bundle.putString(UserConfig.EMAIL, useremail);
                bundle.putString(UserConfig.TAG_PROFILENAME, profilename);

                intent.putExtras(bundle);
                startActivity(intent);

            }

            @Override
            protected String doInBackground(Void... v) {

                    HashMap<String, String> params = new HashMap<>();
                    params.put(UserConfig.TAG_ID, userid);
                    params.put(UserConfig.PAYFULLNAME, userfullname);
                    params.put(UserConfig.PAYADDRESS, useraddress);
                    params.put(UserConfig.PAYEMAIL, email);
                    params.put(UserConfig.PAYCONTACT, usercontact);
                    params.put(UserConfig.PAYTOTALPRICE, totalprice);
                    params.put(UserConfig.PAYCREATED, datetime);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(UserConfig.URL_PAYMENTINFO, params);
                    return res;
            }
        }
        payNow pn = new payNow();
        pn.execute();

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
