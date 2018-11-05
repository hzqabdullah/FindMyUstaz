package com.example.macbook.eventapp.activity.Ustaz.Event;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EventArchive extends AppCompatActivity implements ListView.OnItemClickListener{

    TextView archiveCount;

    private ListView archivelistView;

    private String JSON_STRING, archiveNo;
    String uid, uemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventarchive);

        Intent intent = getIntent();
        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.TAG_UEMAIL);

        archiveCount = findViewById(R.id.archiveCount);

        archivelistView = (ListView) findViewById(R.id.archivelistView);
        archivelistView.setOnItemClickListener(this);

        getArchive();
        getArchiveCount();
    }

    private void showArchiveEvent(){
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
                String insert = "Created on : "+jo.getString(EventConfig.TAG_EINSERT);
                String image = jo.getString(EventConfig.TAG_EIMAGE);


                HashMap<String, String> event = new HashMap<>();
                event.put(EventConfig.TAG_EID, id);
                event.put(EventConfig.TAG_ETITLE, title);
                event.put(EventConfig.TAG_EDATE, date);
                event.put(EventConfig.TAG_EDATEEND, dateend);
                event.put(EventConfig.TAG_EDAY, day);
                event.put(EventConfig.TAG_ETIME, time);
                event.put(EventConfig.TAG_EVENUE, venue);
                event.put(EventConfig.TAG_EINSERT, insert);
                event.put(EventConfig.TAG_EIMAGE, image);

                list.add(event);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        ArchiveAdapter archiveAdapter = new ArchiveAdapter(this, list);
        archivelistView.setAdapter(archiveAdapter);
    }

    private void getArchive(){
        class GetArchive extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventArchive.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showArchiveEvent();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(EventConfig.URL_EVENTARCHIVE, uid);
                return s;
            }
        }
        GetArchive ga = new GetArchive();
        ga.execute();
    }

    private void getArchiveCount(){
        class GetArchiveCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventArchive.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEventArchiveCount(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_EVENTARCHIVECOUNT, uid);
                return s;
            }
        }
        GetArchiveCount gac = new GetArchiveCount();
        gac.execute();
    }

    private void showEventArchiveCount(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ACOUNT);
            JSONObject c = result.getJSONObject(0);
            archiveNo = c.getString(Config.TAG_ARCHIVECOUNT);

            archiveCount.setText(archiveNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, EventArchiveDetails.class);
        HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
        String e_id = map.get(EventConfig.TAG_EID).toString();

        Bundle bundle = new Bundle();
        bundle.putString(EventConfig.E_ID, e_id);
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
