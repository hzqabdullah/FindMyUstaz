package com.example.haziq.findmyustaz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Search extends AppCompatActivity implements ListView.OnItemClickListener{

    private String JSON_STRING, ustazNo;
    String userid, useremail;
    private ListView ustazlistView;
    EditText txtsearchustaz;
    TextView ustazCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Intent intent = getIntent();

        ustazlistView = (ListView) findViewById(R.id.ustazlistView);
        ustazlistView.setOnItemClickListener(this);

        txtsearchustaz = (EditText) findViewById(R.id.txtsearchustaz);
        ustazCount = (TextView) findViewById(R.id.ustazCount);

        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        getAllUstaz();
        getUstazCount();

    }

    private void getAllUstaz(){
        class getustazList extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Search.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showUstazList();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(UserConfig.URL_USTAZLIST);
                return s;
            }
        }
        getustazList gul = new getustazList();
        gul.execute();
    }

    private void showUstazList() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> ustazlist = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String uid = jo.getString(UserConfig.TAG_UID);
                String profilename = jo.getString(UserConfig.TAG_UPROFILENAME);
                String firstname = jo.getString(UserConfig.TAG_UFIRSTNAME);
                String lastname = jo.getString(UserConfig.TAG_ULASTNAME);
                String place = jo.getString(UserConfig.TAG_UPLACE);
                String photo = jo.getString(UserConfig.TAG_UPHOTO);

                HashMap<String, String> ustaz = new HashMap<>();
                ustaz.put(UserConfig.TAG_UID, uid);
                ustaz.put(UserConfig.TAG_UPROFILENAME, profilename);
                ustaz.put(UserConfig.TAG_UFIRSTNAME, firstname);
                ustaz.put(UserConfig.TAG_ULASTNAME, lastname);
                ustaz.put(UserConfig.TAG_UPLACE, place);
                ustaz.put(UserConfig.TAG_UPHOTO, photo);


                ustazlist.add(ustaz);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        UstazAdapter ustazadapter = new UstazAdapter(this, ustazlist);
        ustazlistView.setAdapter(ustazadapter);

    }


    //GET USTAZ COUNT

    private void getUstazCount(){
        class GetUstazCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Search.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showUstazCount(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(UserConfig.URL_USTAZTOTAL);
                return s;
            }
        }
        GetUstazCount guc = new GetUstazCount();
        guc.execute();
    }

    private void showUstazCount(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_UCOUNT);
            JSONObject c = result.getJSONObject(0);
            ustazNo = c.getString(UserConfig.TAG_USTAZCOUNT);

            ustazCount.setText(ustazNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void btnSearchUstaz(View view)
    {
        final String searchustaz = txtsearchustaz.getText().toString();

        class GetEvent extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Search.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showSearchUstaz();
            }

            @Override
            protected String doInBackground(Void... v) {

                HashMap<String,String> params = new HashMap<>();
                params.put(UserConfig.SEARCHUSTAZ, searchustaz);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(UserConfig.URL_SEARCHUSTAZ, params);
                return s;
            }
        }
        GetEvent ge = new GetEvent();
        ge.execute();
    }

    private void showSearchUstaz() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String uid = jo.getString(UserConfig.TAG_UID);
                String profilename = jo.getString(UserConfig.TAG_UPROFILENAME);
                String firstname = jo.getString(UserConfig.TAG_UFIRSTNAME);
                String lastname = jo.getString(UserConfig.TAG_ULASTNAME);
                String place = jo.getString(UserConfig.TAG_UPLACE);
                String photo = jo.getString(UserConfig.TAG_UPHOTO);


                HashMap<String, String> ustaz = new HashMap<>();
                ustaz.put(UserConfig.TAG_UID, uid);
                ustaz.put(UserConfig.TAG_UPROFILENAME, profilename);
                ustaz.put(UserConfig.TAG_UFIRSTNAME, firstname);
                ustaz.put(UserConfig.TAG_ULASTNAME, lastname);
                ustaz.put(UserConfig.TAG_UPLACE, place);
                ustaz.put(UserConfig.TAG_UPHOTO, photo);

                list.add(ustaz);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        UstazAdapter ustazadapter = new UstazAdapter(this, list);
        ustazlistView.setAdapter(ustazadapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, UstazProfile.class);
        HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
        String u_id = map.get(UserConfig.TAG_UID).toString();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_UID, u_id);
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

    public void SearchProductPage(View view)
    {
        Intent intent = new Intent(this, SearchProduct.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
