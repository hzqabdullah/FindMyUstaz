package com.example.macbook.eventapp.activity.Ustaz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventActivity;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventDisplay;
import com.example.macbook.eventapp.activity.Ustaz.Profile.ProfileActivity;
import com.example.macbook.eventapp.activity.Ustaz.Shop.ShopActivity;
import com.example.macbook.eventapp.activity.Ustaz.Order.OrderActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class Dashboard extends AppCompatActivity
{
    TextView UstazProfile, UstazPlace, UstazEmail, datetime;
    String uemail, profilename, uid, photo, place;
    ImageView ivprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);


        datetime = findViewById(R.id.datetime);

        Intent intent = getIntent();
        uemail = intent.getStringExtra(Config.UEMAIL);

        getUstazID();

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
                                SimpleDateFormat sdf = new SimpleDateFormat("hh-mm-ss a  dd MMM yyyy");
                                String dateString = sdf.format(date);
                                datetime.setText(dateString);
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

    private void getUstazID(){
        class GetUstazID extends AsyncTask<Void,Void,String> {
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
                showUstaz(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_DASHBOARD, uemail);
                return s;
            }
        }
        GetUstazID gu = new GetUstazID();
        gu.execute();
    }

    private void showUstaz(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            profilename = c.getString(Config.TAG_UPROFILENAME);
            place = c.getString(Config.TAG_UPLACE);
            photo = c.getString(Config.TAG_UPHOTO);
            uid = c.getString(Config.TAG_UID);



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
        bundle.putString(Config.TAG_UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoToProfileActivity(View view)
    {
        Intent intent = new Intent(this, ProfileActivity.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.TAG_UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoToShopActivity(View view)
    {
        Intent intent = new Intent(this, ShopActivity.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.TAG_UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoToOrderActivity(View view)
    {
        Intent intent = new Intent(this, OrderActivity.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.TAG_UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void Logout(View view)
    {
        confirmLogout();
    }

    private void confirmLogout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to log out?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Toast.makeText(getApplicationContext(), "You've been logged out successfully!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        finish();
                        startActivity(intent);

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
}
