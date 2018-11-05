package com.example.haziq.findmyustaz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class EventDetails extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {


    TextView txtcategory, txtustaz, txttitle, txtdate, txtplace, txtvenue, txtlatitude, txtlongitude;
    String useremail, userid, eventid;
    String category, uid, ustaz, title, date, dateend, day, time, place, venue, image, latitude, longitude;
    Double Latitude , Longitude ;
    Button btnJoin;
    ImageView ivimage;

    GoogleMap eventmap;
    View v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventdetails);

        txtcategory = findViewById(R.id.txtcategory);
        txtustaz = findViewById(R.id.txtustaz);
        txttitle = findViewById(R.id.txttitle);
        txtdate = findViewById(R.id.txtdate);
        txtplace = findViewById(R.id.txtplace);
        txtvenue = findViewById(R.id.txtvenue);
        txtlatitude = findViewById(R.id.txtlatitude);
        txtlongitude = findViewById(R.id.txtlongitude);
        btnJoin = findViewById(R.id.btnJoin);

        ivimage = findViewById(R.id.ivimage);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.eventmap);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        eventid = intent.getStringExtra(UserConfig.E_ID);

        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        getEventDetails();
        getAttend();
    }

    private void getEventDetails(){
        class GetUserInfo extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventDetails.this,"Data fetching...","Please wait...",false,false);
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
                String s = rh.sendGetRequestParam(UserConfig.URL_DETAILSEVENT, eventid);
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
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            category = c.getString(UserConfig.TAG_ECATEGORY);
            uid = c.getString(UserConfig.TAG_UID);
            ustaz = c.getString(UserConfig.TAG_UPROFILENAME);
            title = c.getString(UserConfig.TAG_ETITLE);
            date = c.getString(UserConfig.TAG_EDATE);
            dateend = c.getString(UserConfig.TAG_EDATEEND);
            day = c.getString(UserConfig.TAG_EDAY);
            time = c.getString(UserConfig.TAG_ETIME);
            place = c.getString(UserConfig.TAG_EPLACE);
            venue = c.getString(UserConfig.TAG_EVENUE);
            latitude = c.getString(UserConfig.TAG_ELATITUDE);
            longitude = c.getString(UserConfig.TAG_ELONGITUDE);
            image = c.getString(UserConfig.TAG_EIMAGE);

            if (image.isEmpty())
            {
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
                        .into(ivimage);
            }

            txtcategory.setText(category);
            txtustaz.setText("By : "+ustaz);
            txttitle.setText(title);
            txtdate.setText("Start : "+date+" \t\t\t\tEnd : " + dateend +"\nDay : "+day+" \nTime : "+time);
            txtplace.setText(place);
            txtvenue.setText(venue);
            txtlatitude.setText("Latitude : "+latitude);
            txtlongitude.setText("Longitude : "+longitude);

            Latitude = Double.valueOf(latitude);
            Longitude = Double.valueOf(longitude);


            LatLng coordinate = new LatLng(Latitude, Longitude);
            eventmap.addMarker(new MarkerOptions().position(coordinate).title("Your event is here"));
            eventmap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));


            txtustaz.setOnClickListener(this);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAttend(){

        class Attend extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(EventDetails.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                loading.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("You already joined the event")){

                    //Toast.makeText(UstazProfile.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                    btnJoin.setText("Joined");
                    btnJoin.setEnabled(false);
                }
                else
                {

                    //Toast.makeText(UstazProfile.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(Void... v) {

                HashMap<String,String> params = new HashMap<>();
                params.put(UserConfig.TAG_ID, userid);
                params.put(UserConfig.TAG_EID, eventid);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(UserConfig.URL_CHECKJOINEVENT, params);
                return res;
            }
        }

        Attend attend = new Attend();
        attend.execute();
    }

    public void GoToUstazEvent(View view)
    {
        Intent intent = new Intent(this, UstazEvent.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.TAG_UID, uid);
        bundle.putString(UserConfig.TAG_UPROFILENAME, ustaz);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void JoinEvent(View view)
    {
        class joinEvent extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventDetails.this, "Updating data...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);


                if(httpResponseMsg.equalsIgnoreCase("Thank you for joining the event. Please check your profile")){


                    Toast.makeText(EventDetails.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                    btnJoin.setText("Joined");
                    btnJoin.setEnabled(false);
                }
                else
                {

                    Toast.makeText(EventDetails.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                    btnJoin.setText("Join Event");
                }

                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(UserConfig.ID, userid);
                hashMap.put(UserConfig.TAG_EID, eventid);


                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(UserConfig.URL_JOINEVENT, hashMap);

                return s;
            }
        }

        joinEvent je = new joinEvent();
        je.execute();

        btnJoin.setEnabled(false);
    }

    public void AddCalendar(View view)
    {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-M-yyyy hh:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-M-yyyy");
        String dateStart = date+" "+time;
        String dateEnd = dateend;
        Date eventStartDate, eventEndDate;
        try
        {
            eventStartDate = sdf1.parse(dateStart);
            eventEndDate = sdf2.parse(dateEnd);

            startDate.setTime(eventStartDate);
            endDate.setTime(eventEndDate);

            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDate.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDate.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, category+" : "+title)
                    .putExtra(CalendarContract.Events.DESCRIPTION, "By "+ustaz)
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, place)
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
            startActivity(intent);

        }
        catch (ParseException e)
        {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        eventmap = googleMap;

        if(eventmap != null) {
            eventmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    @Override
    public void onClick(View v) {

        v1 = v;

        if(v1 != null) {
            Intent intent = new Intent(this, UstazProfile.class);
            finish();

            Bundle bundle = new Bundle();
            bundle.putString(UserConfig.TAG_ID, userid);
            bundle.putString(UserConfig.TAG_UID, uid);
            bundle.putString(UserConfig.TAG_UPROFILENAME, ustaz);
            bundle.putString(UserConfig.EMAIL, useremail);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
