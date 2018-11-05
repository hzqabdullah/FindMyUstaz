package com.example.macbook.eventapp.activity.Ustaz.Order;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.Dashboard;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventActivity;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventAdapter;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventAdd;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventConfig;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventDetails;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventDisplay;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventManage;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    TextView productCount;

    private ListView orderlistView;
    private String orderid, ordername, orderaddress, ordercontact, orderemail, paydate, deliverdate, opid, pid, pname, pprice, pqty, pimage, ufirstname, ulastname, ucontact;

    private String JSON_STRING, productNo;
    String uid, uemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderactivity);

        Intent intent = getIntent();
        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.TAG_UEMAIL);

        productCount = findViewById(R.id.productCount);

        orderlistView = (ListView) findViewById(R.id.orderlistView);
        orderlistView.setOnItemClickListener(this);

        getOrder();
        getOrderCount();

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
                                deliverdate = sdf.format(date);
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
        orderlistView.setAdapter(orderAdapter);

    }

    private void getOrder(){
        class GetOrder extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OrderActivity.this,"Loading Data","Please wait for a moment...",false,false);
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
                String s = rh.sendGetRequestParam(OrderConfig.URL_ORDERDISPLAY, uid);
                return s;
            }
        }
        GetOrder go = new GetOrder();
        go.execute();
    }

    private void getOrderCount(){
        class GetOrderCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OrderActivity.this,"Data fetching...","Please wait...",false,false);
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
                String s = rh.sendGetRequestParam(Config.URL_ORDERCOUNT, uid);
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
            productNo = c.getString(Config.TAG_ORDERCOUNT);

            productCount.setText(productNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        confirmApproveOrder();
    }

    private void confirmApproveOrder(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to deliver the product ordered?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        approveOrder();

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

    private void approveOrder(){
        class ApproveOrder extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OrderActivity.this, "Updating data...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(OrderActivity.this, s, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                finish();

                Bundle bundle = new Bundle();
                bundle.putString(Config.TAG_UID, uid);
                bundle.putString(Config.UEMAIL, uemail);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> orderMaps = new HashMap<>();

                orderMaps.put(OrderConfig.TAG_ORDERID, orderid);
                orderMaps.put(OrderConfig.TAG_ORDERNAME, ordername);
                orderMaps.put(OrderConfig.TAG_ORDERADDRESS, orderaddress);
                orderMaps.put(OrderConfig.TAG_ORDERCONTACT, ordercontact);
                orderMaps.put(OrderConfig.TAG_ORDEREMAIL, orderemail);
                orderMaps.put(OrderConfig.TAG_PAYDATE, paydate);
                orderMaps.put(OrderConfig.TAG_DELIVERDATE, deliverdate);
                orderMaps.put(OrderConfig.TAG_OPID, opid);
                orderMaps.put(OrderConfig.TAG_PID, pid);
                orderMaps.put(OrderConfig.TAG_PNAME, pname);
                orderMaps.put(OrderConfig.TAG_PPRICE, pprice);
                orderMaps.put(OrderConfig.TAG_PQTY, pqty);
                orderMaps.put(OrderConfig.TAG_PIMAGE, pimage);
                orderMaps.put(OrderConfig.TAG_UFIRSTNAME, ufirstname);
                orderMaps.put(OrderConfig.TAG_ULASTNAME, ulastname);
                orderMaps.put(OrderConfig.TAG_UEMAIL, uemail);
                orderMaps.put(OrderConfig.TAG_UCONTACT, ucontact);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(OrderConfig.URL_DELIVERORDER, orderMaps);

                return s;
            }
        }

        ApproveOrder ao = new ApproveOrder();
        ao.execute();
    }

    public void GoToDashboard(View view)
    {

        Intent intent = new Intent(this, Dashboard.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoToOrderHistory(View view)
    {

        Intent intent = new Intent(this, OrderHistory.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
