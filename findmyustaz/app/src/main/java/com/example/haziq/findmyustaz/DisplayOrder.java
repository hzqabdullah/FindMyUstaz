package com.example.haziq.findmyustaz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayOrder extends AppCompatActivity implements ListView.OnItemClickListener{

    String useremail, userid, profilename, JSON_STRING;
    TextView txtProfilename, txtTotalPrice;
    ListView pendinglistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displayorder);

        txtProfilename = findViewById(R.id.txtProfilename);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        pendinglistView = findViewById(R.id.pendinglistView);
        pendinglistView.setOnItemClickListener(this);

        Intent intent = getIntent();

        userid = intent.getStringExtra(UserConfig.TAG_ID);
        profilename = intent.getStringExtra(UserConfig.TAG_PROFILENAME);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        getDisplayOrder();
    }

    private void getDisplayOrder(){
        class getDisplayOrder extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DisplayOrder.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showOrderList();
            }

            @Override
            protected String doInBackground(Void... v) {

                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_DISPLAYORDER, userid);
                return s;
            }
        }
        getDisplayOrder gdo = new getDisplayOrder();
        gdo.execute();
    }

    private void showOrderList() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> cartlist = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String payid = jo.getString(UserConfig.TAG_PAYID);
                String payprice = "RM "+jo.getString(UserConfig.TAG_PAYPRICE);
                String payqty = jo.getString(UserConfig.TAG_PAYQTY);
                String paycreated = jo.getString(UserConfig.TAG_PAYCREATED);
                String payfullname = jo.getString(UserConfig.TAG_PAYFULLNAME);
                String payemail = jo.getString(UserConfig.TAG_PAYEMAIL);

                HashMap<String, String> order = new HashMap<>();
                order.put(UserConfig.TAG_PAYID, payid);
                order.put(UserConfig.TAG_PAYPRICE, payprice);
                order.put(UserConfig.TAG_PAYQTY, payqty);
                order.put(UserConfig.TAG_PAYCREATED, paycreated);
                order.put(UserConfig.TAG_PAYFULLNAME, payfullname);
                order.put(UserConfig.TAG_PAYEMAIL, payemail);

                cartlist.add(order);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        OrderAdapter orderadapter = new OrderAdapter(this, cartlist);
        pendinglistView.setAdapter(orderadapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, OrderDetails.class);
        HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
        String pay_id = map.get(UserConfig.TAG_PAYID).toString();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.PAY_ID, pay_id);
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        bundle.putString(UserConfig.TAG_PROFILENAME, profilename);
        intent.putExtras(bundle);
        startActivity(intent);
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
