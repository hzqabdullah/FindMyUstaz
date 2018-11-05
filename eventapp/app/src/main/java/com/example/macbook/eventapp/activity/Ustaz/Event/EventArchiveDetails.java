package com.example.macbook.eventapp.activity.Ustaz.Event;

import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventArchiveDetails extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap eventmap;
    TextView txtcategory, txttitle, txtdate, txtplace, txtvenue, txtlatitude, txtlongitude, txtinsert, txtupdate, joinCount;
    String uemail, uid, e_id;
    String category, title, date, dateend, day, time, place, venue, image, latitude, longitude, insert, update, joinNo,rateNo;
    Double Latitude , Longitude ;
    ImageView ivimage;
    Button deleteEventArchive;
    RatingBar ratingBar;
    Float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventarchivedetails);

        txtcategory = findViewById(R.id.txtcategory);
        txttitle = findViewById(R.id.txttitle);
        txtdate = findViewById(R.id.txtdate);
        txtplace = findViewById(R.id.txtplace);
        txtvenue = findViewById(R.id.txtvenue);
        txtlatitude = findViewById(R.id.txtlatitude);
        txtlongitude = findViewById(R.id.txtlongitude);
        txtinsert = findViewById(R.id.txtinsert);
        txtupdate = findViewById(R.id.txtupdate);
        joinCount = findViewById(R.id.joinCount);

        ratingBar = findViewById(R.id.ratingBar);

        deleteEventArchive = findViewById(R.id.deleteEventArchive);
        deleteEventArchive.setOnClickListener(this);

        ivimage = findViewById(R.id.ivimage);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.eventmap);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        e_id = intent.getStringExtra(EventConfig.E_ID);
        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.UEMAIL);

        getEventDetails();
        getJoinCount();
        getRating();
    }

    private void getEventDetails(){
        class GetUserInfo extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventArchiveDetails.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEventDetails(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(EventConfig.URL_EVENTARCHIVEDETAILS, e_id);
                return s;
            }
        }
        GetUserInfo gu = new GetUserInfo();
        gu.execute();
    }

    private void showEventDetails(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(EventConfig.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            category = c.getString(EventConfig.TAG_ECATEGORY);
            title = c.getString(EventConfig.TAG_ETITLE);
            date = c.getString(EventConfig.TAG_EDATE);
            dateend = c.getString(EventConfig.TAG_EDATEEND);
            day = c.getString(EventConfig.TAG_EDAY);
            time = c.getString(EventConfig.TAG_ETIME);
            place = c.getString(EventConfig.TAG_EPLACE);
            venue = c.getString(EventConfig.TAG_EVENUE);
            image = c.getString(EventConfig.TAG_EIMAGE);
            latitude = c.getString(EventConfig.TAG_ELATITUDE);
            longitude = c.getString(EventConfig.TAG_ELONGITUDE);
            insert = c.getString(EventConfig.TAG_EINSERT);
            update = c.getString(EventConfig.TAG_EUPDATE);

            if (image.isEmpty())
            { //url.isEmpty()
                Picasso.with(this)
                        .load(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(ivimage);

            }
            else
            {
                Picasso.with(this)
                        .load(image)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(ivimage); //this is your ImageView
            }

            txtcategory.setText(category);
            txttitle.setText(title);
            txtdate.setText("Start : "+date+" \t\t\t\tEnd : "+dateend+" \nDay : "+day+" \nTime : "+time);
            txtplace.setText(place);
            txtvenue.setText(venue);
            txtlatitude.setText("Latitude : "+latitude);
            txtlongitude.setText("Longitude : "+longitude);
            txtinsert.setText("Added on : "+insert);
            txtupdate.setText("Last Update : "+update);

            Latitude = Double.valueOf(latitude);
            Longitude = Double.valueOf(longitude);


            LatLng coordinate = new LatLng(Latitude, Longitude);
            eventmap.addMarker(new MarkerOptions().position(coordinate).title("Your event is here"));
            eventmap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));



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
                loading = ProgressDialog.show(EventArchiveDetails.this,"Data fetching...","Please wait...",false,false);
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
                String s = rh.sendGetRequestParam(Config.URL_JOINUSERTOTAL, e_id);
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
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_JUCOUNT);
            JSONObject c = result.getJSONObject(0);
            joinNo = c.getString(Config.TAG_JOINUSERCOUNT);

            joinCount.setText(joinNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getRating(){
        class GetEventCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventArchiveDetails.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showRating(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_RATECOUNT, e_id);
                return s;
            }
        }
        GetEventCount gec = new GetEventCount();
        gec.execute();
    }

    private void showRating(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_RCOUNT);
            JSONObject c = result.getJSONObject(0);
            rateNo = c.getString(Config.TAG_RATECOUNT);

            rate = Float.valueOf(rateNo);

            ratingBar.setRating(rate);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        eventmap = googleMap;

        if(eventmap != null) {
            eventmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void GoToEventArchive(View view)
    {

        Intent intent = new Intent(this, EventArchive.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void ViewComment(View view)
    {

        Intent intent = new Intent(this, ViewComment.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(EventConfig.E_ID, e_id);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void deleteEvent(){
        class DeleteEvent extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventArchiveDetails.this, "Updating data...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(EventArchiveDetails.this, s, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), EventArchive.class);
                finish();

                Bundle bundle = new Bundle();
                bundle.putString(Config.TAG_UID, uid);
                bundle.putString(Config.UEMAIL, uemail);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(EventConfig.URL_EVENTDELETE, e_id);
                return s;
            }
        }

        DeleteEvent de = new DeleteEvent();
        de.execute();
    }

    private void confirmDeleteEvent(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete the event?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteEvent();

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

    @Override
    public void onClick(View v) {
        if(v == deleteEventArchive){
            confirmDeleteEvent();

        }
    }

}
