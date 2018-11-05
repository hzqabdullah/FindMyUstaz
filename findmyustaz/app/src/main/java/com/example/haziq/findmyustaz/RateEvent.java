package com.example.haziq.findmyustaz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RateEvent extends AppCompatActivity {

    String useremail, userid, attendid, eid, etitle, edate, edateend, eday, etime, joinNo, rateNo;
    private TextView txtTimerDay, txtTimerHour, txtTimerMinute, txtTimerSecond, joinCount;
    private Handler handler;
    private Runnable runnable;
    Button btnRateEvent;
    EditText txtComment;
    RatingBar ratingBar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rateevent);

        txtTimerDay = (TextView) findViewById(R.id.txtTimerDay);
        txtTimerHour = (TextView) findViewById(R.id.txtTimerHour);
        txtTimerMinute = (TextView) findViewById(R.id.txtTimerMinute);
        txtTimerSecond = (TextView) findViewById(R.id.txtTimerSecond);
        joinCount = (TextView) findViewById(R.id.joinCount);
        txtComment = (EditText) findViewById(R.id.txtComment);
        btnRateEvent = (Button) findViewById(R.id.btnRateEvent);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        Intent intent = getIntent();

        attendid = intent.getStringExtra(UserConfig.A_ID);
        eid = intent.getStringExtra(UserConfig.TAG_EID);
        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);


        ratingBar.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            rateNo = String.valueOf(rating);
                    }
                }
        );

        getEnterEvent();
        getJoinCount();
    }

    public void RateEvent(View view)
    {
        final String comment = txtComment.getText().toString();

        if(ratingBar.getRating() <= 0)
        {
            Toast.makeText(RateEvent.this,"Please rate the event",Toast.LENGTH_LONG).show();
        }
        else
        {
            class rate extends AsyncTask<Void,Void,String> {

                @Override
                protected void onPreExecute() {

                    progressDialog = ProgressDialog.show(RateEvent.this, "Loading Data", null, true, true);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    progressDialog.dismiss();
                    Toast.makeText(RateEvent.this, s, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    finish();

                    Bundle bundle = new Bundle();
                    bundle.putString(UserConfig.TAG_ID, userid);
                    bundle.putString(UserConfig.EMAIL, useremail);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String,String> params = new HashMap<>();
                    params.put(UserConfig.A_ID, attendid);
                    params.put(UserConfig.TAG_EID, eid);
                    params.put(UserConfig.TAG_ID, userid);
                    params.put(UserConfig.TAG_RATE, rateNo);
                    params.put(UserConfig.TAG_COMMENT, comment);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(UserConfig.URL_RATEEVENT, params);
                    return res;
                }
            }

            rate rt = new rate();
            rt.execute();
        }
    }


    private void getEnterEvent(){
        class GetJoinDetails extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RateEvent.this,"Data fetching...","Please wait...",false,false);
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

            //edateend = ("4-5-2018 16:00");
            //tvday.setText(eday);
            //tvtime.setText(etime);

            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, 1000);
                    try {
                        SimpleDateFormat startdateFormat = new SimpleDateFormat(
                                "dd-MM-yyyy");
// Please here set your event date//YYYY-MM-DD
                        Date futureDate = startdateFormat.parse(edateend);
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
                            btnRateEvent.setText("EVENT EXPIRED");
                            btnRateEvent.setEnabled(false);
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

    private void getJoinCount(){
        class GetJoinCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RateEvent.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showJoinCount(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_JOINUSERTOTAL, eid);
                return s;
            }
        }
        GetJoinCount gjc = new GetJoinCount();
        gjc.execute();
    }

    private void showJoinCount(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_JUCOUNT);
            JSONObject c = result.getJSONObject(0);
            joinNo = c.getString(UserConfig.TAG_JOINUSERCOUNT);

            joinCount.setText(joinNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
