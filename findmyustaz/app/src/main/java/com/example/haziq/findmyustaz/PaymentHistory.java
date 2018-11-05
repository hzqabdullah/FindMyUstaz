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

public class PaymentHistory extends AppCompatActivity implements ListView.OnItemClickListener{

    String useremail, userid, profilename, JSON_STRING;
    ListView historylistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymenthistory);

        historylistView = findViewById(R.id.historylistView);
        historylistView.setOnItemClickListener(this);

        Intent intent = getIntent();

        userid = intent.getStringExtra(UserConfig.TAG_ID);
        profilename = intent.getStringExtra(UserConfig.TAG_PROFILENAME);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        getDisplayHistory();
    }

    private void getDisplayHistory(){
        class getDisplayHistory extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PaymentHistory.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showHistoryList();
            }

            @Override
            protected String doInBackground(Void... v) {

                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_PAYMENTHISTORY, userid);
                return s;
            }
        }
        getDisplayHistory gdh = new getDisplayHistory();
        gdh.execute();
    }

    private void showHistoryList() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
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

                HashMap<String, String> history = new HashMap<>();
                history.put(UserConfig.TAG_PAYID, payid);
                history.put(UserConfig.TAG_PAYPRICE, payprice);
                history.put(UserConfig.TAG_PAYQTY, payqty);
                history.put(UserConfig.TAG_PAYCREATED, paycreated);
                history.put(UserConfig.TAG_PAYFULLNAME, payfullname);
                history.put(UserConfig.TAG_PAYEMAIL, payemail);

                list.add(history);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        OrderAdapter orderadapter = new OrderAdapter(this, list);
        historylistView.setAdapter(orderadapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PaymentHistoryDetails.class);
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
