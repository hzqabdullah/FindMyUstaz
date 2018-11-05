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

public class Favourite extends AppCompatActivity implements ListView.OnItemClickListener {

    String userid, useremail, JSON_STRING, favNo;
    private ListView favouritelistView;
    TextView favCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite);


        favouritelistView = (ListView) findViewById(R.id.favouritelistView);
        favouritelistView.setOnItemClickListener(this);

        favCount = (TextView) findViewById(R.id.favCount);

        Intent intent = getIntent();
        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        getFavUstaz();
        getFavCount();
    }

    private void getFavUstaz(){
        class getfavList extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Favourite.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showFavList();
            }

            @Override
            protected String doInBackground(Void... v) {

                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_FAVOURITELIST, userid);
                return s;
            }
        }
        getfavList gul = new getfavList();
        gul.execute();
    }

    private void showFavList() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> favlist = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String uid = jo.getString(UserConfig.TAG_UID);
                String uprofilename = jo.getString(UserConfig.TAG_UPROFILENAME);
                String ufirstname = jo.getString(UserConfig.TAG_UFIRSTNAME);
                String ulastname = jo.getString(UserConfig.TAG_ULASTNAME);
                String uplace = jo.getString(UserConfig.TAG_UPLACE);
                String uphoto = jo.getString(UserConfig.TAG_UPHOTO);

                HashMap<String, String> fav = new HashMap<>();
                fav.put(UserConfig.TAG_UID, uid);
                fav.put(UserConfig.TAG_UPROFILENAME, uprofilename);
                fav.put(UserConfig.TAG_UFIRSTNAME, ufirstname);
                fav.put(UserConfig.TAG_ULASTNAME, ulastname);
                fav.put(UserConfig.TAG_UPLACE, uplace);
                fav.put(UserConfig.TAG_UPHOTO, uphoto);

                favlist.add(fav);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        UstazAdapter ustazadapter = new UstazAdapter(this, favlist);
        favouritelistView.setAdapter(ustazadapter);
    }

    private void getFavCount(){
        class GetFavCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Favourite.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showFavCount(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_FAVTOTAL, userid);
                return s;
            }
        }
        GetFavCount gfc = new GetFavCount();
        gfc.execute();
    }

    private void showFavCount(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_FCOUNT);
            JSONObject c = result.getJSONObject(0);
            favNo = c.getString(UserConfig.TAG_FAVUSTAZCOUNT);

            favCount.setText(favNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
