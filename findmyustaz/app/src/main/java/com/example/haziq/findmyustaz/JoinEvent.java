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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JoinEvent extends AppCompatActivity implements ListView.OnItemClickListener{

    String useremail, userid, JSON_STRING;
    ListView joinlistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinevent);

        joinlistView = (ListView) findViewById(R.id.joinlistView);
        joinlistView.setOnItemClickListener(this);

        Intent intent = getIntent();

        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        getJEvent();
    }

    private void getJEvent(){
        class getjoinEvent extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(JoinEvent.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showJoinEvent();
            }

            @Override
            protected String doInBackground(Void... v) {

                HashMap<String,String> params = new HashMap<>();
                params.put(UserConfig.TAG_ID, userid);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(UserConfig.URL_DISPLAYJOINEVENT, params);
                return s;
            }
        }
        getjoinEvent gje = new getjoinEvent();
        gje.execute();
    }

    //Event information
    private void showJoinEvent(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String aid = jo.getString(UserConfig.TAG_AID);
                String title = jo.getString(UserConfig.TAG_ETITLE);
                String date = "Start : "+jo.getString(UserConfig.TAG_EDATE);
                String dateend = "End : "+jo.getString(UserConfig.TAG_EDATEEND);
                String day = jo.getString(UserConfig.TAG_EDAY);
                String time = jo.getString(UserConfig.TAG_ETIME);
                String image = jo.getString(UserConfig.TAG_EIMAGE);
                String venue = ("Venue : "+jo.getString(UserConfig.TAG_EVENUE));
                String ustaz = ("By : "+jo.getString(UserConfig.TAG_UPROFILENAME));

                HashMap<String, String> event = new HashMap<>();
                event.put(UserConfig.TAG_AID, aid);
                event.put(UserConfig.TAG_ETITLE, title);
                event.put(UserConfig.TAG_EDATE, date);
                event.put(UserConfig.TAG_EDATEEND, dateend);
                event.put(UserConfig.TAG_ETIME, time);
                event.put(UserConfig.TAG_EDAY, day);
                event.put(UserConfig.TAG_EVENUE, venue);
                event.put(UserConfig.TAG_EIMAGE, image);
                event.put(UserConfig.TAG_UPROFILENAME, ustaz);

                list.add(event);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        EventListAdapter eventlistadapter = new EventListAdapter(this, list);
        joinlistView.setAdapter(eventlistadapter);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, JoinEventDetails.class);
        HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
        String a_id = map.get(UserConfig.TAG_AID).toString();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.A_ID, a_id);
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
