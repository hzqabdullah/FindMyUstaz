package com.example.haziq.findmyustaz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SearchProduct extends AppCompatActivity implements ListView.OnItemClickListener{

    private String JSON_STRING;
    String userid, useremail;

    ListView productsearchlistView;
    EditText txtsearchproduct, txtproductRange1;

    Spinner spinnerPrice1;

    String [] range1 = new String [] { "0", "10", "50", "100", "150", "200", "500" } ;

    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchproduct);

        txtsearchproduct = findViewById(R.id.txtsearchproduct);
        txtproductRange1 = findViewById(R.id.txtproductRange1);

        spinnerPrice1 = findViewById(R.id.spinnerPrice1);

        Intent intent = getIntent();

        userid = intent.getStringExtra(UserConfig.TAG_ID);
        useremail = intent.getStringExtra(UserConfig.EMAIL);

        productsearchlistView = findViewById(R.id.productsearchlistView);
        productsearchlistView.setOnItemClickListener(this);


        final List<String> priceList1 = new ArrayList<>(Arrays.asList(range1));


        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, priceList1);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerPrice1.setAdapter(adapter);

        spinnerPrice1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                String selectedItem = (String) parent.getItemAtPosition(i);
                txtproductRange1.setText(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        getLatestProduct();
    }

    private void getLatestProduct(){
        class getlatestProduct extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SearchProduct.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showLatestProduct();
            }

            @Override
            protected String doInBackground(Void... v) {


                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(UserConfig.URL_LATESTPRODUCT);
                return s;
            }
        }
        getlatestProduct glp = new getlatestProduct();
        glp.execute();
    }

    private void showLatestProduct(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(UserConfig.TAG_PID);
                String name = "Name : "+jo.getString(UserConfig.TAG_PNAME);
                String price = "Price : RM"+jo.getString(UserConfig.TAG_PPRICE);
                String qty = "Quantity : "+jo.getString(UserConfig.TAG_PQUANTITY);
                String ustaz = "From "+jo.getString(UserConfig.TAG_UPROFILENAME)+" Shop";
                String image = jo.getString(UserConfig.TAG_PIMAGE);

                HashMap<String,String> product = new HashMap<>();
                product.put(UserConfig.TAG_PID, id);
                product.put(UserConfig.TAG_PNAME, name);
                product.put(UserConfig.TAG_PPRICE, price);
                product.put(UserConfig.TAG_PQUANTITY, qty);
                product.put(UserConfig.TAG_UPROFILENAME, ustaz);
                product.put(UserConfig.TAG_PIMAGE, image);

                list.add(product);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        SearchProductAdapter productAdapter = new SearchProductAdapter(this, list);
        productsearchlistView.setAdapter(productAdapter);
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

        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void btnSearchProduct(View view)
    {
        final String searchproduct = txtsearchproduct.getText().toString();

        class GetEvent extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SearchProduct.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showSearchProduct();
            }

            @Override
            protected String doInBackground(Void... v) {

                HashMap<String,String> params = new HashMap<>();
                params.put(UserConfig.SEARCHPRODUCT, searchproduct);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(UserConfig.URL_SEARCHPRODUCT, params);
                return s;
            }
        }
        GetEvent ge = new GetEvent();
        ge.execute();
    }

    private void showSearchProduct() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(UserConfig.TAG_PID);
                String name = "Name : "+jo.getString(UserConfig.TAG_PNAME);
                String price = "Price : RM"+jo.getString(UserConfig.TAG_PPRICE);
                String qty = "Quantity : "+jo.getString(UserConfig.TAG_PQUANTITY);
                String ustaz = "From "+jo.getString(UserConfig.TAG_UPROFILENAME)+" Shop";
                String image = jo.getString(UserConfig.TAG_PIMAGE);

                HashMap<String,String> product = new HashMap<>();
                product.put(UserConfig.TAG_PID, id);
                product.put(UserConfig.TAG_PNAME, name);
                product.put(UserConfig.TAG_PPRICE, price);
                product.put(UserConfig.TAG_PQUANTITY, qty);
                product.put(UserConfig.TAG_UPROFILENAME, ustaz);
                product.put(UserConfig.TAG_PIMAGE, image);

                list.add(product);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        SearchProductAdapter productAdapter = new SearchProductAdapter(this, list);
        productsearchlistView.setAdapter(productAdapter);

    }

    public void SearchProductRange(View view)
    {
        final String priceRange1 = txtproductRange1.getText().toString();

        class GetProduct extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SearchProduct.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showSearchProductRange();
            }

            @Override
            protected String doInBackground(Void... v) {

                HashMap<String,String> params = new HashMap<>();
                params.put(UserConfig.SEARCHRANGE1, priceRange1);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(UserConfig.URL_SEARCHPRODUCTRANGE, params);
                return s;
            }
        }
        GetProduct gp = new GetProduct();
        gp.execute();
    }

    private void showSearchProductRange() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(UserConfig.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(UserConfig.TAG_PID);
                String name = "Name : "+jo.getString(UserConfig.TAG_PNAME);
                String price = "Price : RM"+jo.getString(UserConfig.TAG_PPRICE);
                String qty = "Quantity : "+jo.getString(UserConfig.TAG_PQUANTITY);
                String ustaz = "From "+jo.getString(UserConfig.TAG_UPROFILENAME)+" Shop";
                String image = jo.getString(UserConfig.TAG_PIMAGE);

                HashMap<String,String> product = new HashMap<>();
                product.put(UserConfig.TAG_PID, id);
                product.put(UserConfig.TAG_PNAME, name);
                product.put(UserConfig.TAG_PPRICE, price);
                product.put(UserConfig.TAG_PQUANTITY, qty);
                product.put(UserConfig.TAG_UPROFILENAME, ustaz);
                product.put(UserConfig.TAG_PIMAGE, image);

                list.add(product);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        SearchProductAdapter productAdapter = new SearchProductAdapter(this, list);
        productsearchlistView.setAdapter(productAdapter);

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
