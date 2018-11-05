package com.example.haziq.findmyustaz;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentHistoryDetails extends AppCompatActivity {

    String useremail, userid, profilename, pay_id, JSON_STRING;
    String fullname, address, contact, email, created, price, qty, status, paid;
    TextView lvfullname, lvaddress, lvcontact, lvemail, lvprice, lvqty, lvcreated, lvstatus, lvpaid;
    ListView historydetailslistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymenthistorydetails);

        lvfullname = findViewById(R.id.lvfullname);
        lvaddress = findViewById(R.id.lvaddress);
        lvcontact = findViewById(R.id.lvcontact);
        lvemail = findViewById(R.id.lvemail);
        lvprice = findViewById(R.id.lvprice);
        lvqty = findViewById(R.id.lvqty);
        lvcreated = findViewById(R.id.lvcreated);
        lvstatus = findViewById(R.id.lvstatus);
        lvpaid = findViewById(R.id.lvpaid);
        historydetailslistView = findViewById(R.id.historydetailslistView);

        Intent intent = getIntent();

        pay_id = intent.getStringExtra(UserConfig.PAY_ID);
        userid = intent.getStringExtra(UserConfig.TAG_ID);
        profilename = intent.getStringExtra(UserConfig.TAG_PROFILENAME);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        getOrderDetails();
        getUserOrder();
    }

    private void getUserOrder(){
        class GetOrder extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PaymentHistoryDetails.this,"Loading Data","Please wait for a moment...",false,false);
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
                String s = rh.sendGetRequestParam(UserConfig.URL_PAYMENTHISTORYDETAILSLIST, pay_id);
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
                String status = jo.getString(UserConfig.TAG_PSTATUS);

                HashMap<String, String> cart = new HashMap<>();

                cart.put(UserConfig.TAG_PPRICE, pprice);
                cart.put(UserConfig.TAG_PQUANTITY, pqty);
                cart.put(UserConfig.TAG_PNAME, pname);
                cart.put(UserConfig.TAG_TOTALPRICE, ptotalprice);
                cart.put(UserConfig.TAG_UPROFILENAME, ustaz);
                cart.put(UserConfig.TAG_PSTATUS, status);

                cartlist.add(cart);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        PaymentListAdapter orderadapter = new PaymentListAdapter(this, cartlist);
        historydetailslistView.setAdapter(orderadapter);

    }

    private void getOrderDetails(){
        class GetUserCart extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PaymentHistoryDetails.this,"Loading Data","Please wait for a moment...",false,false);
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
                String s = rh.sendGetRequestParam(UserConfig.URL_PAYMENTHISTORYDETAILS, pay_id);
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
            status = c.getString(UserConfig.TAG_PAYSTATUS);
            paid = c.getString(UserConfig.TAG_PAYDATE);

            lvfullname.setText(fullname);
            lvprice.setText("RM "+price);
            lvqty.setText(qty);
            lvaddress.setText(address);
            lvemail.setText(email);
            lvcontact.setText(contact);
            lvcreated.setText(created);
            lvstatus.setText(status);
            lvpaid.setText(paid);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public void DeleteHistory(View view)
    {
        confirmDeleteOrder();
    }

    public void deletePayment()
    {        class DeletePayment extends AsyncTask<Void,Void,String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(PaymentHistoryDetails.this, "Updating data...", "Please wait...", false, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(PaymentHistoryDetails.this, s, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), PaymentHistory.class);
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
            String s = rh.sendGetRequestParam(UserConfig.URL_DELETEPAYMENTHISTORY, pay_id);
            return s;
        }
    }

        DeletePayment dp = new DeletePayment();
        dp.execute();

    }

    private void confirmDeleteOrder(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete your previous payment?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletePayment();

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

    public void GotoPaymentHistory(View view)
    {
        Intent intent = new Intent(this, PaymentHistory.class);
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
