package com.example.macbook.eventapp.activity.Ustaz.Shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventConfig;
import com.example.macbook.eventapp.activity.Ustaz.Event.EventManage;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopDetails extends AppCompatActivity {


    String id, uid, uemail, JSON_STRING;
    TextView txtstatus, txtpname, txtprice, txtdesc, txtquantity, txtupdate, txtinsert;
    ImageView ivimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopdetails);

        txtpname = findViewById(R.id.txtpname);
        txtprice = findViewById(R.id.txtprice);
        txtquantity = findViewById(R.id.txtquantity);
        txtdesc = findViewById(R.id.txtdesc);
        txtstatus = findViewById(R.id.txtstatus);
        txtupdate = findViewById(R.id.txtupdate);
        txtinsert = findViewById(R.id.txtinsert);

        ivimage = (ImageView) findViewById(R.id.ivimage);

        Intent intent = getIntent();
        id = intent.getStringExtra(ShopConfig.P_ID);
        uid = intent.getStringExtra(Config.TAG_UID);
        uemail = intent.getStringExtra(Config.UEMAIL);

        getProduct();
    }

    private void getProduct(){
        class GetProduct extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShopDetails.this,"Data fetching...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showProduct(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(ShopConfig.URL_PRODUCTDETAILS, id);
                return s;
            }
        }
        GetProduct gp = new GetProduct();
        gp.execute();
    }

    private void showProduct(String json){
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(ShopConfig.TAG_JSON_ARRAY);
            JSONObject jo = result.getJSONObject(0);
            String id = jo.getString(ShopConfig.TAG_PID);
            String name = jo.getString(ShopConfig.TAG_PNAME);
            String price = jo.getString(ShopConfig.TAG_PPRICE);
            String desc = jo.getString(ShopConfig.TAG_PDESC);
            String quantity = jo.getString(ShopConfig.TAG_PQUANTITY);
            String status = jo.getString(ShopConfig.TAG_PSTATUS);
            String image = jo.getString(ShopConfig.TAG_PIMAGE);
            String insert = jo.getString(ShopConfig.TAG_PINSERT);
            String update = jo.getString(ShopConfig.TAG_PUPDATE);

            txtpname.setText("Product : "+name);
            txtprice.setText("Price : RM"+price);
            txtdesc.setText(desc);
            txtquantity.setText("Quantity : "+quantity);
            txtstatus.setText("Status : "+status);
            txtinsert.setText("Added on : "+insert);
            txtupdate.setText("Last Update : "+update);

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


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GoToShopDisplay(View view)
    {

        Intent intent = new Intent(this, ShopDisplay.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoToShopManage(View view)
    {

        Intent intent = new Intent(this, ShopManage.class);
        finish();

        Bundle bundle = new Bundle();

        bundle.putString(ShopConfig.P_ID, id);
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

