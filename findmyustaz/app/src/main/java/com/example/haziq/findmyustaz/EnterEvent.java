package com.example.haziq.findmyustaz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class EnterEvent extends AppCompatActivity implements View.OnClickListener{

    String useremail, userid, attendid, eid, etitle, edate, edateend, eday, etime;
    private TextView txtTimerDay, txtTimerHour, txtTimerMinute, txtTimerSecond, tvdate, tvday, tvtime;
    private Handler handler;
    private Runnable runnable;
    Button btnRate;

    String starteventdate, endeventdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterevent);

        txtTimerDay = (TextView) findViewById(R.id.txtTimerDay);
        txtTimerHour = (TextView) findViewById(R.id.txtTimerHour);
        txtTimerMinute = (TextView) findViewById(R.id.txtTimerMinute);
        txtTimerSecond = (TextView) findViewById(R.id.txtTimerSecond);
        tvdate = (TextView) findViewById(R.id.tvdate);
        tvday = (TextView) findViewById(R.id.tvday);
        tvtime = (TextView) findViewById(R.id.tvtime);

        btnRate = (Button) findViewById(R.id.btnRate);
        btnRate.setOnClickListener(this);

        Intent intent = getIntent();

        attendid = intent.getStringExtra(UserConfig.A_ID);
        eid = intent.getStringExtra(UserConfig.TAG_EID);
        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        getEnterEvent();
    }

    private void getEnterEvent(){
        class GetJoinDetails extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EnterEvent.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEnterEvent(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_DETAILSJOINEVENT, attendid);
                return s;
            }
        }
        GetJoinDetails gjd = new GetJoinDetails();
        gjd.execute();
    }

    private void showEnterEvent(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);

            etitle = c.getString(UserConfig.TAG_ETITLE);
            edate = c.getString(UserConfig.TAG_EDATE);
            edateend = c.getString(UserConfig.TAG_EDATEEND);
            eday = c.getString(UserConfig.TAG_EDAY);
            etime = c.getString(UserConfig.TAG_ETIME);

            tvdate.setText(edate+" - "+edateend);
            tvday.setText(eday);
            tvtime.setText(etime);

            //starteventdate = (edate+" "+etime);
            starteventdate = ("22-3-2018 15:44");

            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, 1000);
                    try {
                        SimpleDateFormat startdateFormat = new SimpleDateFormat(
                                "dd-MM-yyyy hh:mm");
// Please here set your event date//YYYY-MM-DD
                        Date futureDate = startdateFormat.parse(starteventdate);
                        Date currentDate = new Date();
                        if (!currentDate.after(futureDate)) {
                            long diff = futureDate.getTime()
                                    - currentDate.getTime();
                            long days = diff / (24 * 60 * 60 * 1000);
                            diff -= days * (24 * 60 * 60 * 1000);
                            long hours = diff / (60 * 60 * 1000);
                            diff -= hours * (60 * 60 * 1000);
                            long minutes = diff / (60 * 1000);
                            diff -= minutes * (60 * 1000);
                            long seconds = diff / 1000;
                            txtTimerDay.setText("" + String.format("%02d", days));
                            txtTimerHour.setText("" + String.format("%02d", hours));
                            txtTimerMinute.setText(""
                                    + String.format("%02d", minutes));
                            txtTimerSecond.setText(""
                                    + String.format("%02d", seconds));
                        }
                        else
                        {
                            btnRate.setText("RATE NOW");
                            btnRate.setEnabled(true);
                        }
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            handler.postDelayed(runnable, 1 * 1000);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnRate){
            Intent intent = new Intent(this, RateEvent.class);
            finish();

            Bundle bundle = new Bundle();
            bundle.putString(UserConfig.TAG_ID, userid);
            bundle.putString(UserConfig.A_ID, attendid);
            bundle.putString(UserConfig.TAG_EID, eid);
            bundle.putString(UserConfig.EMAIL, useremail);

            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    public void GoToJoinEventDetails(View view)
    {
        Intent intent = new Intent(this, JoinEventDetails.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.A_ID, attendid);
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
