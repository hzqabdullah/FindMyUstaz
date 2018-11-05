package com.example.macbook.eventapp.activity.Ustaz.Order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventAdd;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventConfig;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistory extends AppCompatActivity {

    TextView orderhistoryCount;

    private ListView orderhistorylistView;
    private String JSON_STRING, orderhistoryNo;
    private String orderid, ordername, orderaddress, ordercontact, orderemail, paydate, opid, pid, pname, pprice, pqty, pimage, ufirstname, ulastname, ucontact;
    String uid, uemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderhistory);

        Intent intent = getIntent();
        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.TAG_UEMAIL);

        orderhistoryCount = findViewById(R.id.orderhistoryCount);

        orderhistorylistView = (ListView) findViewById(R.id.orderhistorylistView);
        //orderhistorylistView.setOnItemClickListener(this);

        getOrder();
        getOrderHistoryCount();
    }

    private void showOrder(){


        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(EventConfig.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                orderid = jo.getString(OrderConfig.TAG_ORDERID);
                ordername = jo.getString(OrderConfig.TAG_ORDERNAME);
                orderaddress = jo.getString(OrderConfig.TAG_ORDERADDRESS);
                ordercontact = jo.getString(OrderConfig.TAG_ORDERCONTACT);
                orderemail = jo.getString(OrderConfig.TAG_ORDEREMAIL);
                paydate = jo.getString(OrderConfig.TAG_PAYDATE);
                opid = jo.getString(OrderConfig.TAG_OPID);
                pid = jo.getString(OrderConfig.TAG_PID);
                pname = jo.getString(OrderConfig.TAG_PNAME);
                pprice = jo.getString(OrderConfig.TAG_PPRICE);
                pqty = jo.getString(OrderConfig.TAG_PQTY);
                pimage = jo.getString(OrderConfig.TAG_PIMAGE);
                ufirstname = jo.getString(OrderConfig.TAG_UFIRSTNAME);
                ulastname = jo.getString(OrderConfig.TAG_ULASTNAME);
                ucontact = jo.getString(OrderConfig.TAG_UCONTACT);


                HashMap<String, String> order = new HashMap<>();
                order.put(OrderConfig.TAG_ORDERID, orderid);
                order.put(OrderConfig.TAG_ORDERNAME, ordername);
                order.put(OrderConfig.TAG_ORDERADDRESS, orderaddress);
                order.put(OrderConfig.TAG_ORDERCONTACT, ordercontact);
                order.put(OrderConfig.TAG_ORDEREMAIL, orderemail);
                order.put(OrderConfig.TAG_PAYDATE, paydate);
                order.put(OrderConfig.TAG_OPID, opid);
                order.put(OrderConfig.TAG_PID, pid);
                order.put(OrderConfig.TAG_PNAME, pname);
                order.put(OrderConfig.TAG_PPRICE, pprice);
                order.put(OrderConfig.TAG_PQTY, pqty);
                order.put(OrderConfig.TAG_PIMAGE, pimage);
                order.put(OrderConfig.TAG_UFIRSTNAME, ufirstname);
                order.put(OrderConfig.TAG_ULASTNAME, ulastname);
                order.put(OrderConfig.TAG_UEMAIL, uemail);
                order.put(OrderConfig.TAG_UCONTACT, ucontact);

                list.add(order);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        OrderAdapter orderAdapter = new OrderAdapter(this, list);
        orderhistorylistView.setAdapter(orderAdapter);

    }

    private void getOrder(){
        class GetOrder extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OrderHistory.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showOrder();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(OrderConfig.URL_ORDERHISTORY, uid);
                return s;
            }
        }
        GetOrder go = new GetOrder();
        go.execute();
    }

    private void getOrderHistoryCount(){
        class GetOrderCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OrderHistory.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showOrderCount(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_ORDERHISTORYCOUNT, uid);
                return s;
            }
        }
        GetOrderCount goc = new GetOrderCount();
        goc.execute();
    }

    private void showOrderCount(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_OCOUNT);
            JSONObject c = result.getJSONObject(0);
            orderhistoryNo = c.getString(Config.TAG_ORDERCOUNT);

            orderhistoryCount.setText(orderhistoryNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GoToOrderActivity(View view)
    {

        Intent intent = new Intent(this, OrderActivity.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
