package com.example.haziq.findmyustaz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Cart extends AppCompatActivity implements ListView.OnItemClickListener{

    String useremail, userid, profilename, totalprice, JSON_STRING;
    TextView txtProfilename, txtTotalPrice;
    ListView cartlistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        txtProfilename = findViewById(R.id.txtProfilename);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        cartlistView = findViewById(R.id.cartlistView);
        cartlistView.setOnItemClickListener(this);

        Intent intent = getIntent();

        userid = intent.getStringExtra(UserConfig.TAG_ID);
        profilename = intent.getStringExtra(UserConfig.TAG_PROFILENAME);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        txtProfilename.setText("Profile name : "+profilename);


        getUserCart();
        getTotalPrice();
    }

    private void getUserCart(){
        class GetUserCart extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Cart.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showCartList();
            }

            @Override
            protected String doInBackground(Void... v) {

                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_DISPLAYCART, userid);
                return s;
            }
        }
        GetUserCart guc = new GetUserCart();
        guc.execute();
    }

    private void showCartList() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> cartlist = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String cid = jo.getString(UserConfig.TAG_CID);
                String price = "Price : RM"+jo.getString(UserConfig.TAG_PPRICE);
                String qty = "Quantity : "+jo.getString(UserConfig.TAG_PQUANTITY);
                String added = "Added on : "+jo.getString(UserConfig.TAG_ADDED);
                String image = jo.getString(UserConfig.TAG_PIMAGE);
                String name = jo.getString(UserConfig.TAG_PNAME);
                String totalprice = "Total : RM"+jo.getString(UserConfig.TAG_TOTALPRICE);
                String ustaz = "From "+jo.getString(UserConfig.TAG_UPROFILENAME)+" Shop";

                HashMap<String, String> cart = new HashMap<>();
                cart.put(UserConfig.TAG_CID, cid);
                cart.put(UserConfig.TAG_PPRICE, price);
                cart.put(UserConfig.TAG_PQUANTITY, qty);
                cart.put(UserConfig.TAG_PIMAGE, image);
                cart.put(UserConfig.TAG_ADDED, added);
                cart.put(UserConfig.TAG_PNAME, name);
                cart.put(UserConfig.TAG_TOTALPRICE, totalprice);
                cart.put(UserConfig.TAG_UPROFILENAME, ustaz);

                cartlist.add(cart);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        CartAdapter cartadapter = new CartAdapter(this, cartlist);
        cartlistView.setAdapter(cartadapter);

    }

    private void getTotalPrice(){
        class GetTotal extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Cart.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showTotal(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_TOTALCART, userid);
                return s;
            }
        }
        GetTotal gt = new GetTotal();
        gt.execute();
    }

    private void showTotal(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_CARTCOUNT);
            JSONObject c = result.getJSONObject(0);
            totalprice = c.getString(UserConfig.TAG_CARTTOTAL);

            txtTotalPrice.setText("RM "+totalprice);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CartUpdate.class);
        HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
        String c_id = map.get(UserConfig.TAG_CID).toString();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.C_ID, c_id);
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        bundle.putString(UserConfig.TAG_PROFILENAME, profilename);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GotoPaymentInfo(View view)
    {
        Intent intent = new Intent(this, PaymentInfo.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        bundle.putString(UserConfig.TAG_PROFILENAME, profilename);
        bundle.putString(UserConfig.TAG_CARTTOTAL, totalprice);
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
