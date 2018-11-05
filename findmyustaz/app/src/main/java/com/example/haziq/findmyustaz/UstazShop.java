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

public class UstazShop extends AppCompatActivity implements ListView.OnItemClickListener{

    private ListView listView;
    TextView ustazText, productCount;

    private String JSON_STRING;
    String uid, userid, useremail, ustazname, productNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ustazshop);

        Intent intent = getIntent();
        uid = intent.getStringExtra(UserConfig.TAG_UID);
        ustazname = intent.getStringExtra(UserConfig.TAG_UPROFILENAME);
        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        ustazText = (TextView) findViewById(R.id.ustazText);
        productCount = (TextView) findViewById(R.id.productCount);

        listView = (ListView) findViewById(R.id.productlistView);
        listView.setOnItemClickListener(this);

        ustazText.setText(ustazname+" Shop");

        getUstazProductList();
        getProductCount();
    }

    private void getUstazProductList(){
        class getustazproductList extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               JSON_STRING = s;
                showUstazProduct();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_USTAZSHOP, uid);
                return s;
            }
        }
        getustazproductList gupl = new getustazproductList();
        gupl.execute();
    }

    private void showUstazProduct(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(UserConfig.TAG_PID);
                String name = jo.getString(UserConfig.TAG_PNAME);
                String price = "Price : RM"+jo.getString(UserConfig.TAG_PPRICE);
                String qty = "Quantity : "+jo.getString(UserConfig.TAG_PQUANTITY);
                String image = jo.getString(UserConfig.TAG_PIMAGE);

                HashMap<String,String> product = new HashMap<>();
                product.put(UserConfig.TAG_PID, id);
                product.put(UserConfig.TAG_PNAME, name);
                product.put(UserConfig.TAG_PPRICE, price);
                product.put(UserConfig.TAG_PQUANTITY, qty);
                product.put(UserConfig.TAG_PIMAGE, image);

                list.add(product);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        ProductAdapter productAdapter = new ProductAdapter(this, list);
        listView.setAdapter(productAdapter);
    }

    private void getProductCount(){
        class GetProductCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                showProductCount(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UserConfig.URL_USTAZSHOPCOUNT, uid);
                return s;
            }
        }
        GetProductCount gpc = new GetProductCount();
        gpc.execute();
    }

    private void showProductCount(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_PCOUNT);
            JSONObject c = result.getJSONObject(0);
            productNo = c.getString(UserConfig.TAG_PRODUCTCOUNT);

            productCount.setText(productNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ProductDetails.class);
        HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
        String p_id = map.get(UserConfig.TAG_PID).toString();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.P_ID, p_id);
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        bundle.putString(UserConfig.TAG_UID, uid);
        bundle.putString(UserConfig.TAG_UPROFILENAME, ustazname);
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

    public void GoToUstazProfile(View view)
    {
        Intent intent = new Intent(this, UstazProfile.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(UserConfig.TAG_UID, uid);
        bundle.putString(UserConfig.TAG_ID, userid);
        bundle.putString(UserConfig.EMAIL, useremail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
