package com.example.haziq.findmyustaz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetails extends AppCompatActivity {

    String useremail, userid, profilename, pay_id, JSON_STRING, datetime;
    String fullname, address, contact, email, created, price, qty;
    TextView lvfullname, lvaddress, lvcontact, lvemail, lvprice, lvqty, lvcreated;
    ListView orderdetailslistView;

    public static final int PAYPAL_REQUEST_CODE = 7171;

    PayPalConfiguration payPalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(UserConfig.PAYPAL_CLIENTID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetails);

        lvfullname = findViewById(R.id.lvfullname);
        lvaddress = findViewById(R.id.lvaddress);
        lvcontact = findViewById(R.id.lvcontact);
        lvemail = findViewById(R.id.lvemail);
        lvprice = findViewById(R.id.lvprice);
        lvqty = findViewById(R.id.lvqty);
        lvcreated = findViewById(R.id.lvcreated);
        orderdetailslistView = findViewById(R.id.orderdetailslistView);

        Intent intent = getIntent();

        pay_id = intent.getStringExtra(UserConfig.PAY_ID);
        userid = intent.getStringExtra(UserConfig.TAG_ID);
        profilename = intent.getStringExtra(UserConfig.TAG_PROFILENAME);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        getOrderDetails();
        getUserOrder();

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

    private void getUserOrder(){
        class GetOrder extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OrderDetails.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showOrder();
            }

            @Override
            protected String doInBackground(Void... v) {

                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_DETAILSORDERLIST, pay_id);
                return s;
            }
        }
        GetOrder go = new GetOrder();
        go.execute();
    }

    private void showOrder() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> cartlist = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                String pprice = "Price : RM"+jo.getString(UserConfig.TAG_PPRICE);
                String pqty = "("+jo.getString(UserConfig.TAG_PQUANTITY)+")";
                String pname = jo.getString(UserConfig.TAG_PNAME);
                String ptotalprice = "Total : RM"+jo.getString(UserConfig.TAG_TOTALPRICE);
                String ustaz = "From "+jo.getString(UserConfig.TAG_UPROFILENAME)+" Shop";

                HashMap<String, String> cart = new HashMap<>();

                cart.put(UserConfig.TAG_PPRICE, pprice);
                cart.put(UserConfig.TAG_PQUANTITY, pqty);
                cart.put(UserConfig.TAG_PNAME, pname);
                cart.put(UserConfig.TAG_TOTALPRICE, ptotalprice);
                cart.put(UserConfig.TAG_UPROFILENAME, ustaz);


                cartlist.add(cart);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        OrderListAdapter orderadapter = new OrderListAdapter(this, cartlist);
        orderdetailslistView.setAdapter(orderadapter);

    }

    private void getOrderDetails(){
        class GetUserCart extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OrderDetails.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showOrderDetails(s);
            }

            @Override
            protected String doInBackground(Void... v) {

                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_DETAILSORDER, pay_id);
                return s;
            }
        }
        GetUserCart guc = new GetUserCart();
        guc.execute();
    }

    private void showOrderDetails(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);

            fullname = c.getString(UserConfig.TAG_PAYFULLNAME);
            address = c.getString(UserConfig.TAG_PAYADDRESS);
            contact = c.getString(UserConfig.TAG_PAYCONTACT);
            email = c.getString(UserConfig.TAG_PAYEMAIL);
            price = c.getString(UserConfig.TAG_PAYPRICE);
            qty = c.getString(UserConfig.TAG_PAYQTY);
            created = c.getString(UserConfig.TAG_PAYCREATED);

            lvfullname.setText(fullname);
            lvprice.setText("RM "+price);
            lvqty.setText(qty);
            lvaddress.setText(address);
            lvemail.setText(email);
            lvcontact.setText(contact);
            lvcreated.setText(created);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private void payProcess() {

        class paymentProcess extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OrderDetails.this, "Updating data...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(OrderDetails.this, s, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), DisplayOrder.class);
                finish();


                Bundle bundle = new Bundle();
                bundle.putString(UserConfig.PAY_ID, pay_id);
                bundle.putString(UserConfig.TAG_ID, userid);
                bundle.putString(UserConfig.EMAIL, useremail);
                bundle.putString(UserConfig.TAG_PROFILENAME, profilename);

                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(UserConfig.PAY_ID, pay_id);
                hashMap.put(UserConfig.TAG_PAYDATE, datetime);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(UserConfig.URL_PAYMENTPROCESS, hashMap);

                return s;
            }
        }

        paymentProcess pp = new paymentProcess();
        pp.execute();
    }

    public void btnPayPal(View view)
    {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(price), "USD", "Pay to findmyustaz.com account", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PAYPAL_REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null)
                {
                    String paymentDetails = confirmation.getProofOfPayment().getState();

                    if(paymentDetails.equals("approved"))
                    {
                        Toast.makeText(this, "Payment approved", Toast.LENGTH_LONG).show();
                        payProcess();

                    }
                    else
                    {
                        Toast.makeText(this, "Error in payment", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "Confirmation is null", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void DeleteOrder(View view)
    {
        confirmDeleteOrder();
    }

    public void deleteOrder()
    {        class DeleteOrder extends AsyncTask<Void,Void,String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(OrderDetails.this, "Updating data...", "Please wait...", false, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(OrderDetails.this, s, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), DisplayOrder.class);
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
            String s = rh.sendGetRequestParam(UserConfig.URL_DELETEORDER, pay_id);
            return s;
        }
    }

        DeleteOrder dor = new DeleteOrder();
        dor.execute();

    }

    private void confirmDeleteOrder(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to cancel the order?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteOrder();

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

    public void GotoDisplayOrder(View view)
    {
        Intent intent = new Intent(this, DisplayOrder.class);
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
