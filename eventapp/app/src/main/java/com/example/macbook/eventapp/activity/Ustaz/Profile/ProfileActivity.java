package com.example.macbook.eventapp.activity.Ustaz.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.Dashboard;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventActivity;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;
import com.example.macbook.eventapp.activity.Ustaz.Shop.ShopActivity;
import com.google.android.gms.maps.model.Circle;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView ivprofile;
    TextView txtname, txtplace, txtdob, txtprofilename, txtemail, txtcontact, eventCount, productCount, followerCount;
    String uid, uemail, firstname, lastname, place, dob, contact, email, profilename, image, eventNo, productNo, followerNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);

        txtname = findViewById(R.id.txtname);
        txtplace = findViewById(R.id.txtplace);
        txtdob = findViewById(R.id.txtdob);
        txtprofilename = findViewById(R.id.txtprofilename);
        txtemail = findViewById(R.id.txtemail);
        txtcontact = findViewById(R.id.txtcontact);
        eventCount = findViewById(R.id.eventCount);
        productCount = findViewById(R.id.productCount);
        followerCount = findViewById(R.id.followerCount);

        ivprofile = findViewById(R.id.ivprofile);

        Intent intent = getIntent();

        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.UEMAIL);
        getUstazInfo();
        getEventCount();
        getProductCount();
        getFollowerCount();
    }

    private void getUstazInfo(){
        class GetUstaz extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileActivity.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showUstaz(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_USTAZPROFILE, uid);
                return s;
            }
        }
        GetUstaz gu = new GetUstaz();
        gu.execute();
    }



    private void getProductCount(){
        class GetProductCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileActivity.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showProductCount(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_PRODUCTCOUNT, uid);
                return s;
            }
        }
        GetProductCount gpc = new GetProductCount();
        gpc.execute();
    }

    private void getEventCount(){
        class GetEventCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileActivity.this,"Data fetching...","Please wait...",false,false);
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

    private void showUstaz(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            firstname = c.getString(Config.TAG_UFIRSTNAME);
            lastname = c.getString(Config.TAG_ULASTNAME);
            profilename = c.getString(Config.TAG_UPROFILENAME);
            place = c.getString(Config.TAG_UPLACE);
            dob = c.getString(Config.TAG_UDOB);
            email = c.getString(Config.TAG_UEMAIL);
            contact = c.getString(Config.TAG_UCONTACT);
            image = c.getString(Config.TAG_UPHOTO);

            if(image.isEmpty())
            { //url.isEmpty()
                Picasso.with(this)
                        .load(R.drawable.ustaz)
                        .placeholder(R.drawable.ustaz)
                        .error(R.drawable.ustaz)
                        .into(ivprofile);

            }
            else
            {
                Picasso.with(this)
                        .load(image)
                        .placeholder(R.drawable.ustaz)
                        .error(R.drawable.ustaz)
                        .into(ivprofile); //this is your ImageView
            }

            txtprofilename.setText(profilename);
            txtname.setText(firstname+" "+lastname);
            txtplace.setText(place);
            txtdob.setText(dob);
            txtemail.setText(email);
            txtcontact.setText(contact);
            txtdob.setText(dob);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showFollowersCount(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_FCOUNT);
            JSONObject c = result.getJSONObject(0);
            followerNo = c.getString(Config.TAG_FOLLOWERCOUNT);

            followerCount.setText(followerNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFollowerCount(){
        class GetFollowersCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileActivity.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showFollowersCount(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_FOLLOWERCOUNT, uid);
                return s;
            }
        }
        GetFollowersCount gfc = new GetFollowersCount();
        gfc.execute();
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

    private void showProductCount(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_PCOUNT);
            JSONObject c = result.getJSONObject(0);
            productNo = c.getString(Config.TAG_PRODUCTCOUNT);

            productCount.setText(productNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GoToProfileUpdate(View view)
    {
        Intent intent = new Intent(this, ProfileUpdate.class);
        finish();

        Bundle bundle = new Bundle();
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

    public void GoToShopActivity(View view)
    {

        Intent intent = new Intent(this, ShopActivity.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
