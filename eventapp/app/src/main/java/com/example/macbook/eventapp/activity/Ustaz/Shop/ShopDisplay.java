package com.example.macbook.eventapp.activity.Ustaz.Shop;

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

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopDisplay extends AppCompatActivity implements ListView.OnItemClickListener{

    private ListView listView;
    TextView productCount;

    private String JSON_STRING;
    String uid, uemail, productNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopdisplay);

        Intent intent = getIntent();
        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.UEMAIL);

        productCount = findViewById(R.id.productCount);

        listView = (ListView) findViewById(R.id.productlistView);
        listView.setOnItemClickListener(this);

        getJSON();
        getProductCount();
    }

    private void showProduct(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(ShopConfig.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(ShopConfig.TAG_PID);
                String name = "Product : "+jo.getString(ShopConfig.TAG_PNAME);
                String price = "Price : RM"+jo.getString(ShopConfig.TAG_PPRICE);
                String qty = "Quantity : "+jo.getString(ShopConfig.TAG_PQUANTITY);
                String status = "Status : "+jo.getString(ShopConfig.TAG_PSTATUS);
                String update = "Last Update : "+jo.getString(ShopConfig.TAG_PUPDATE);
                String image = jo.getString(ShopConfig.TAG_PIMAGE);

                HashMap<String,String> product = new HashMap<>();
                product.put(ShopConfig.TAG_PID, id);
                product.put(ShopConfig.TAG_PNAME, name);
                product.put(ShopConfig.TAG_PPRICE, price);
                product.put(ShopConfig.TAG_PQUANTITY, qty);
                product.put(ShopConfig.TAG_PSTATUS, status);
                product.put(ShopConfig.TAG_PUPDATE, update);
                product.put(ShopConfig.TAG_PIMAGE, image);

                list.add(product);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ProductAdapter productAdapter = new ProductAdapter(this, list);
        listView.setAdapter(productAdapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShopDisplay.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showProduct();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(ShopConfig.URL_PRODUCTDISPLAY, uid);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void getProductCount(){
        class GetProductCount extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShopDisplay.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showProductCount(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_PRODUCTCOUNT, uid);
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
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_PCOUNT);
            JSONObject c = result.getJSONObject(0);
            productNo = c.getString(Config.TAG_PRODUCTCOUNT);

            productCount.setText(productNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ShopDetails.class);
        HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
        String p_id = map.get(ShopConfig.TAG_PID).toString();

        Bundle bundle = new Bundle();
        bundle.putString(ShopConfig.P_ID, p_id);
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoToShopActivity(View view)
    {

        Intent intent = new Intent(this, ShopActivity.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoToShopAdd(View view)
    {

        Intent intent = new Intent(this, ShopAdd.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
