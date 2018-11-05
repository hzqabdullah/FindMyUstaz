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

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    TextView txtprofilename, txtdetails, txtlocation, txtemail, txtcontact, favCount, joinCount;
    String userid, useremail, location, details, contact, photo, email, profilename, favNo, joinNo;
    CircleImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        txtdetails = findViewById(R.id.txtdetails);
        txtlocation = findViewById(R.id.txtlocation);
        txtprofilename = findViewById(R.id.txtprofilename);
        txtemail = findViewById(R.id.txtemail);
        txtcontact = findViewById(R.id.txtcontact);
        favCount = findViewById(R.id.favCount);
        joinCount = findViewById(R.id.joinCount);

        userPhoto = findViewById(R.id.userPhoto);

        Intent intent = getIntent();

        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);
        getUserInfo();
        getFavCount();
        getJoinCount();
    }

    //Profile
    private void getUserInfo(){
        class GetUserID extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Profile.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showUser(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_USERINFO, useremail);
                return s;
            }
        }
        GetUserID gu = new GetUserID();
        gu.execute();
    }

    //Profile
    private void showUser(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            profilename = c.getString(UserConfig.TAG_PROFILENAME);
            details = c.getString(UserConfig.TAG_DETAILS);
            location = c.getString(UserConfig.TAG_LOCATION);
            email = c.getString(UserConfig.TAG_EMAIL);
            contact = c.getString(UserConfig.TAG_CONTACT);
            photo = c.getString(UserConfig.TAG_PHOTO);

            if(photo.isEmpty())
            {
                Picasso.with(this)
                        .load(R.drawable.man)
                        .placeholder(R.drawable.man)
                        .error(R.drawable.man)
                        .into(userPhoto);

            }
            else
            {
                Picasso.with(this)
                        .load(photo)
                        .placeholder(R.drawable.man)
                        .error(R.drawable.man)
                        .into(userPhoto);
            }

            txtprofilename.setText(profilename);
            txtlocation.setText(location);
            txtdetails.setText(details);
            txtemail.setText(email);
            txtcontact.setText(contact);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Fav Ustaz Count
    private void getFavCount(){
        class GetFavCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Profile.this,"Data fetching...","Please wait...",false,false);
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

    //Fav Ustaz Count
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

    //Join Event Count
    private void getJoinCount(){
        class GetJoinCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Profile.this,"Data fetching...","Please wait...",false,false);
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
                String s = rh.sendGetRequestParam(UserConfig.URL_JOINEVENTTOTAL, userid);
                return s;
            }
        }
        GetJoinCount gjc = new GetJoinCount();
        gjc.execute();
    }

    //Join Event Count
    private void showJoinCount(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_JCOUNT);
            JSONObject c = result.getJSONObject(0);
            joinNo = c.getString(UserConfig.TAG_JOINEVENTCOUNT);

            joinCount.setText(joinNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //GoToListJoinEvent
    public void GoToListJoinEvent(View view)
    {
        Intent intent = new Intent(this, JoinEvent.class);
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

    public void ProfileUpdatePage(View view)
    {
        Intent intent = new Intent(this, ProfileUpdate.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void Logout(View view)
    {
        confirmLogout();
    }

    private void confirmLogout()
    {
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

    public void DisplayCart(View view)
    {
        Intent intent = new Intent(this, Cart.class);
        finish();

        Bundle bundle = new Bundle();

        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.TAG_PROFILENAME, profilename);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GotoDisplayOrder(View view)
    {
        Intent intent = new Intent(this, DisplayOrder.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
