package com.example.macbook.eventapp.activity.Ustaz.Event;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class EventDisplay extends AppCompatActivity implements ListView.OnItemClickListener{

    TextView eventCount;

    private ListView listView;

    ImageView imageView;

    private String JSON_STRING, eventNo;
    String uid, uemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventdisplay);

        Intent intent = getIntent();
        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.TAG_UEMAIL);

        eventCount = findViewById(R.id.eventCount);
        imageView = (ImageView) findViewById(R.id.ivimage);

        listView = (ListView) findViewById(R.id.eventlistView);
        listView.setOnItemClickListener(this);

        getJSON();
        getEventCount();

    }

    public void getAutoDelete()
    {
        class AutoDelete extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(EventDisplay.this,s,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), EventDisplay.class);
                finish();

                Bundle bundle = new Bundle();
                bundle.putString(Config.TAG_UID, uid);
                bundle.putString(Config.UEMAIL, uemail);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(EventConfig.UID, uid);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(EventConfig.URL_EVENTAUTODELETE, params);
                return res;
            }
        }

        AutoDelete ad = new AutoDelete();
        ad.execute();
    }

    public void GoToAutoDelete(View view)
    {
        getAutoDelete();
    }

    private void showEvent(){


        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(EventConfig.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(EventConfig.TAG_EID);
                String title = jo.getString(EventConfig.TAG_ETITLE);
                String date = "Start : "+jo.getString(EventConfig.TAG_EDATE);
                String dateend = "End : "+jo.getString(EventConfig.TAG_EDATEEND);
                String day = jo.getString(EventConfig.TAG_EDAY);
                String time = jo.getString(EventConfig.TAG_ETIME);
                String venue = jo.getString(EventConfig.TAG_EVENUE);
                String update = "Last Update : "+jo.getString(EventConfig.TAG_EUPDATE);
                String image = jo.getString(EventConfig.TAG_EIMAGE);


                HashMap<String, String> event = new HashMap<>();
                event.put(EventConfig.TAG_EID, id);
                event.put(EventConfig.TAG_ETITLE, title);
                event.put(EventConfig.TAG_EDATEEND, dateend);
                event.put(EventConfig.TAG_EDATE, date);
                event.put(EventConfig.TAG_EDAY, day);
                event.put(EventConfig.TAG_ETIME, time);
                event.put(EventConfig.TAG_EVENUE, venue);
                event.put(EventConfig.TAG_EUPDATE, update);
                event.put(EventConfig.TAG_EIMAGE, image);

                list.add(event);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        EventAdapter eventAdapter = new EventAdapter(this, list);
        listView.setAdapter(eventAdapter);


    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventDisplay.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEvent();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(EventConfig.URL_EVENTDISPLAY, uid);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void getEventCount(){
        class GetEventCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventDisplay.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEventCount(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_EVENTCOUNT, uid);
                return s;
            }
        }
        GetEventCount gec = new GetEventCount();
        gec.execute();
    }

    private void showEventCount(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ECOUNT);
            JSONObject c = result.getJSONObject(0);
            eventNo = c.getString(Config.TAG_EVENTCOUNT);

            eventCount.setText(eventNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, EventDetails.class);
        HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
        String e_id = map.get(EventConfig.TAG_EID).toString();

        Bundle bundle = new Bundle();
        bundle.putString(EventConfig.E_ID,e_id);
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoToEventActivity(View view)
    {

        Intent intent = new Intent(this, EventActivity.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoToEventAdd(View view)
    {

        Intent intent = new Intent(this, EventAdd.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}