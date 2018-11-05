package com.example.haziq.findmyustaz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UstazEvent extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listView;
    TextView ustazText, eventCount;

    private String JSON_STRING;
    String uid, userid, useremail, ustazname, eventNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ustazevent);

        Intent intent = getIntent();
        uid = intent.getStringExtra(UserConfig.TAG_UID);
        ustazname = intent.getStringExtra(UserConfig.TAG_UPROFILENAME);
        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        ustazText = (TextView) findViewById(R.id.ustazText);
        eventCount = (TextView) findViewById(R.id.eventCount);

        listView = (ListView) findViewById(R.id.eventlistView);
        listView.setOnItemClickListener(this);

        ustazText.setText(ustazname+" Event");

        getUstazEventList();
        getEventCount();
    }

    private void getUstazEventList(){
        class getustazeventList extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showUstazEvent();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_USTAZEVENT, uid);
                return s;
            }
        }
        getustazeventList guel = new getustazeventList();
        guel.execute();
    }

    private void showUstazEvent(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(UserConfig.TAG_EID);
                String title = jo.getString(UserConfig.TAG_ETITLE);
                String date = "Start : "+jo.getString(UserConfig.TAG_EDATE);
                String dateend = "End : "+jo.getString(UserConfig.TAG_EDATEEND);
                String day = jo.getString(UserConfig.TAG_EDAY);
                String time = jo.getString(UserConfig.TAG_ETIME);
                String venue = jo.getString(UserConfig.TAG_EVENUE);
                String image = jo.getString(UserConfig.TAG_EIMAGE);

                HashMap<String, String> event = new HashMap<>();
                event.put(UserConfig.TAG_EID, id);
                event.put(UserConfig.TAG_ETITLE, title);
                event.put(UserConfig.TAG_EDATE, date);
                event.put(UserConfig.TAG_EDATEEND, dateend);
                event.put(UserConfig.TAG_EDAY, day);
                event.put(UserConfig.TAG_ETIME, time);
                event.put(UserConfig.TAG_EVENUE, venue);
                event.put(UserConfig.TAG_EIMAGE, image);


                list.add(event);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        EventListAdapter eventlistadapter = new EventListAdapter(this, list);
        listView.setAdapter(eventlistadapter);
    }

    private void getEventCount(){
        class GetEventCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                showEventCount(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_USTAZEVENTCOUNT, uid);
                return s;
            }
        }
        GetEventCount goc = new GetEventCount();
        goc.execute();
    }

    private void showEventCount(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ECOUNT);
            JSONObject c = result.getJSONObject(0);
            eventNo = c.getString(UserConfig.TAG_EVENTCOUNT);

            eventCount.setText(eventNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, EventDetails.class);
        HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
        String e_id = map.get(UserConfig.TAG_EID).toString();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.E_ID,e_id);
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
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

    public void GoToUstazProfile(View view)
    {
        Intent intent = new Intent(this, UstazProfile.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_UID, uid);
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
