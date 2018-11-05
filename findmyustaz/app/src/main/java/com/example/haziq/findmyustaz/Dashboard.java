package com.example.haziq.findmyustaz;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity implements ListView.OnItemClickListener{


    private ListView listView;

    private String JSON_STRING;

    TextView UserProfile, UserLocation;
    String useremail, profilename, userid, location, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        UserProfile = findViewById(R.id.UserProfile);
        UserLocation = findViewById(R.id.UserLocation);

        listView = (ListView) findViewById(R.id.displayeventlv);
        listView.setOnItemClickListener(this);

        Intent intent = getIntent();
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        //User Information
        {
            class GetUserID extends AsyncTask<Void,Void,String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(Dashboard.this,"Data fetching...","Please wait...",false,false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    showUser(s);
                }

                @Override
                protected String doInBackground(Void... params) {
                    RequestHandler rh = new RequestHandler();
                    String s = rh.sendGetRequestParam(UserConfig.URL_USERINFO, useremail);
                    return s;
                }
            }
            GetUserID gu = new GetUserID();
            gu.execute();
        }

    }

    //User Information
    private void showUser(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            profilename = c.getString(UserConfig.TAG_PROFILENAME);
            location = c.getString(UserConfig.TAG_LOCATION);
            latitude = c.getString(UserConfig.TAG_LATITUDE);
            longitude = c.getString(UserConfig.TAG_LONGITUDE);
            userid = c.getString(UserConfig.TAG_ID);

            UserProfile.setText("Welcome " +profilename);
            UserLocation.setText(location);

            class GetEvent extends AsyncTask<Void,Void,String> {

                ProgressDialog loading;
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(Dashboard.this,"Loading Data","Please wait for a moment...",false,false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    JSON_STRING = s;
                    showEvent();
                }

                @Override
                protected String doInBackground(Void... v) {

                    HashMap<String,String> params = new HashMap<>();
                    params.put(UserConfig.TAG_LOCATION, location);
                    params.put(UserConfig.TAG_LATITUDE, latitude);
                    params.put(UserConfig.TAG_LONGITUDE, longitude);

                    RequestHandler rh = new RequestHandler();
                    String s = rh.sendPostRequest(UserConfig.URL_DISPLAYEVENT, params);
                    return s;


                }
            }
            GetEvent ge = new GetEvent();
            ge.execute();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //Event information
    private void showEvent(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> eventlist = new ArrayList<HashMap<String, String>>();
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
                String venue = ("Venue : "+jo.getString(UserConfig.TAG_EVENUE));
                String ustaz = ("By : "+jo.getString(UserConfig.TAG_UPROFILENAME));
                String distance = (jo.getString(UserConfig.TAG_DISTANCE)+" km from here");
                String image = jo.getString(UserConfig.TAG_EIMAGE);

                HashMap<String, String> event = new HashMap<>();
                event.put(UserConfig.TAG_EID, id);
                event.put(UserConfig.TAG_ETITLE, title);
                event.put(UserConfig.TAG_EDATE, date);
                event.put(UserConfig.TAG_EDATEEND, dateend);
                event.put(UserConfig.TAG_ETIME, time);
                event.put(UserConfig.TAG_EDAY, day);
                event.put(UserConfig.TAG_EVENUE, venue);
                event.put(UserConfig.TAG_UPROFILENAME, ustaz);
                event.put(UserConfig.TAG_DISTANCE, distance);
                event.put(UserConfig.TAG_EIMAGE, image);

                eventlist.add(event);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        EventAdapter eventadapter = new EventAdapter(this, eventlist);
        listView.setAdapter(eventadapter);
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
