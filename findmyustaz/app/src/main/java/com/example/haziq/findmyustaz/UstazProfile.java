package com.example.haziq.findmyustaz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UstazProfile extends AppCompatActivity {

    String useremail, userid, uid, firstname, lastname, place, dob, contact, email, profilename, photo;
    TextView txtname, txtplace, txtdob, txtprofilename, txtemail, txtcontact;
    Button btnFollow;
    CircleImageView ustazPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ustazprofile);

        txtname = findViewById(R.id.txtname);
        txtplace = findViewById(R.id.txtplace);
        txtdob = findViewById(R.id.txtdob);
        txtprofilename = findViewById(R.id.txtprofilename);
        txtemail = findViewById(R.id.txtemail);
        txtcontact = findViewById(R.id.txtcontact);
        ustazPhoto = findViewById(R.id.ustazPhoto);

        btnFollow = (Button)findViewById(R.id.btnFollow);

        Intent intent = getIntent();

        uid = intent.getStringExtra(UserConfig.TAG_UID);
        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        getUstazInfo();
        getFavourite();
    }

    private void getUstazInfo(){
        class GetUstaz extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UstazProfile.this,"Data fetching...","Please wait...",false,false);
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
                String s = rh.sendGetRequestParam(UserConfig.URL_USTAZPROFILE, uid);
                return s;
            }
        }
        GetUstaz gu = new GetUstaz();
        gu.execute();
    }

    private void showUstaz(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            firstname = c.getString(UserConfig.TAG_UFIRSTNAME);
            lastname = c.getString(UserConfig.TAG_ULASTNAME);
            profilename = c.getString(UserConfig.TAG_UPROFILENAME);
            place = c.getString(UserConfig.TAG_UPLACE);
            dob = c.getString(UserConfig.TAG_UDOB);
            email = c.getString(UserConfig.TAG_UEMAIL);
            contact = c.getString(UserConfig.TAG_UCONTACT);
            photo = c.getString(UserConfig.TAG_UPHOTO);

            if (photo.isEmpty())
            {
                Picasso.with(this)
                        .load(R.drawable.ustaz)
                        .placeholder(R.drawable.ustaz)
                        .error(R.drawable.ustaz)
                        .into(ustazPhoto);
            }
            else
            {
                Picasso.with(this)
                        .load(photo)
                        .placeholder(R.drawable.ustaz)
                        .error(R.drawable.ustaz)
                        .into(ustazPhoto);
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


    public void getFavourite(){

        class Favourite extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(UstazProfile.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                loading.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("You already followed")){

                    //Toast.makeText(UstazProfile.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                    btnFollow.setText("Unfollow");
                }
                else
                {

                    //Toast.makeText(UstazProfile.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                    btnFollow.setText("Follow");
                }

            }

            @Override
            protected String doInBackground(Void... v) {

                HashMap<String,String> params = new HashMap<>();
                params.put(UserConfig.TAG_ID, userid);
                params.put(UserConfig.TAG_UID, uid);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(UserConfig.URL_CHECKFAVOURITE, params);
                return res;
            }
        }

        Favourite favourite = new Favourite();
        favourite.execute();
    }

    public void AddFavourite(View view)
    {
        class addFavourite extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UstazProfile.this, "Updating data...", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);


                if(httpResponseMsg.equalsIgnoreCase("Unfollowed")){


                    Toast.makeText(UstazProfile.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                    btnFollow.setText("Follow");
                }
                else
                {

                    Toast.makeText(UstazProfile.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                    btnFollow.setText("Unfollow");
                }

                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(UserConfig.ID, userid);
                hashMap.put(UserConfig.TAG_UID, uid);


                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(UserConfig.URL_ADDFAVOURITE, hashMap);

                return s;
            }
        }

        addFavourite af = new addFavourite();
        af.execute();

        btnFollow.setText("Follow");

    }

    public void GoToUstazEvent(View view)
    {
        Intent intent = new Intent(this, UstazEvent.class);
        finish();

        Bundle bundle = new Bundle();

        bundle.putString(UserConfig.TAG_UID, uid);
        bundle.putString(UserConfig.TAG_UPROFILENAME, profilename);
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoToUstazShop(View view)
    {
        Intent intent = new Intent(this, UstazShop.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_UID, uid);
        bundle.putString(UserConfig.TAG_UPROFILENAME, profilename);
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
