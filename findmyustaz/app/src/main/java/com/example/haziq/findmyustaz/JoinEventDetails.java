package com.example.haziq.findmyustaz;

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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoinEventDetails extends AppCompatActivity implements OnMapReadyCallback{

    TextView lvprofilename, lvfirstname, lvlastname, lvplace, lvtitle, txtdate, lvvenue, lvaddress;
    String useremail, userid, attendid;
    String uprofilename, ufirstname, ulastname, uplace, uphoto, eid, etitle, edate, edateend, eday, etime, evenue, eplace, eimage, elatitude, elongitude;
    GoogleMap eventmap;

    ImageView eventBanner;
    CircleImageView ustazPhoto2;

    Double Latitude , Longitude ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joineventdetails);

        lvprofilename = findViewById(R.id.lvprofilename);
        lvfirstname = findViewById(R.id.lvfirstname);
        lvlastname = findViewById(R.id.lvlastname);
        lvplace = findViewById(R.id.lvplace);
        lvtitle = findViewById(R.id.lvtitle);
        txtdate = findViewById(R.id.txtdate);
        lvaddress = findViewById(R.id.lvaddress);
        lvvenue = findViewById(R.id.lvvenue);

        ustazPhoto2 = findViewById(R.id.ustazPhoto2);
        eventBanner = findViewById(R.id.eventBanner);

        Intent intent = getIntent();

        attendid = intent.getStringExtra(UserConfig.A_ID);

        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.eventmap);
        mapFragment.getMapAsync(this);

        getJoinEventDetails();
    }

    private void getJoinEventDetails(){
        class GetJoinDetails extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(JoinEventDetails.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showJoinEventDetails(s);
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

    private void showJoinEventDetails(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);


            uprofilename = c.getString(UserConfig.TAG_UPROFILENAME);
            ufirstname = c.getString(UserConfig.TAG_UFIRSTNAME);
            ulastname = c.getString(UserConfig.TAG_ULASTNAME);
            uplace = c.getString(UserConfig.TAG_UPLACE);
            uphoto = c.getString(UserConfig.TAG_UPHOTO);

            eid = c.getString(UserConfig.TAG_EID);
            etitle = c.getString(UserConfig.TAG_ETITLE);
            edate = c.getString(UserConfig.TAG_EDATE);
            edateend = c.getString(UserConfig.TAG_EDATEEND);
            eday = c.getString(UserConfig.TAG_EDAY);
            etime = c.getString(UserConfig.TAG_ETIME);
            evenue = c.getString(UserConfig.TAG_EVENUE);
            eplace = c.getString(UserConfig.TAG_EPLACE);
            elatitude = c.getString(UserConfig.TAG_ELATITUDE);
            elongitude = c.getString(UserConfig.TAG_ELONGITUDE);
            eimage = c.getString(UserConfig.TAG_EIMAGE);

            //Event Banner
            if (eimage.isEmpty())
            {
                Picasso.with(this)
                        .load(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(eventBanner);
            }
            else
            {
                Picasso.with(this)
                        .load(eimage)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(eventBanner);
            }

            //Ustaz Photo
            if (uphoto.isEmpty())
            {
                Picasso.with(this)
                        .load(R.drawable.ustaz)
                        .placeholder(R.drawable.ustaz)
                        .error(R.drawable.ustaz)
                        .into(ustazPhoto2);
            }
            else
            {
                Picasso.with(this)
                        .load(uphoto)
                        .placeholder(R.drawable.ustaz)
                        .error(R.drawable.ustaz)
                        .into(ustazPhoto2);
            }

            Latitude = Double.valueOf(elatitude);
            Longitude = Double.valueOf(elongitude);

            lvprofilename.setText(uprofilename);
            lvfirstname.setText(ufirstname);
            lvlastname.setText(ulastname);
            lvplace.setText(uplace);
            lvtitle.setText(etitle);
            txtdate.setText("Start : "+edate+" \t\t\t\tEnd : " + edateend +"\nDay : "+eday+" \nTime : "+etime);

            lvvenue.setText(evenue);
            lvaddress.setText(eplace);


            LatLng coordinate = new LatLng(Latitude, Longitude);
            eventmap.addMarker(new MarkerOptions().position(coordinate).title("Your event is here"));
            eventmap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void RemoveJoinEvent(View view) {
        confirmDeleteJoinEvent();
    }

    public void deleteJoin()
    {
        class DeleteJoin extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(JoinEventDetails.this, "Updating data...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(JoinEventDetails.this, s, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), JoinEvent.class);
                finish();


                Bundle bundle = new Bundle();
                bundle.putString(UserConfig.TAG_ID, userid);
                bundle.putString(UserConfig.EMAIL, useremail);

                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(Void... params) {

                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_DELETEJOINEVENT, attendid);
                return s;
            }
        }
        DeleteJoin dc = new DeleteJoin();
        dc.execute();
    }

    private void confirmDeleteJoinEvent(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to remove the join event?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteJoin();

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
    public void onMapReady(GoogleMap googleMap) {
        eventmap = googleMap;

        if(eventmap != null) {
            eventmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void GoToJoinEvent(View view)
    {
        Intent intent = new Intent(this, JoinEvent.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoToEnterEvent(View view)
    {
        Intent intent = new Intent(this, EnterEvent.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.A_ID, attendid);
        bundle.putString(UserConfig.TAG_EID, eid);
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
