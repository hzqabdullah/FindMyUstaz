package com.example.macbook.eventapp.activity.Ustaz.Shop;

/**
 * Created by Haziq on 3/4/2018.
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macbook.eventapp.R;


public class ProductHolder {

    TextView lvname, lvprice, lvqty, lvupdate, lvstatus;
    ImageView productPhoto;

    public ProductHolder(View v) {

        lvname = (TextView) v.findViewById(R.id.lvname);
        lvprice = (TextView) v.findViewById(R.id.lvprice);
        lvqty = (TextView) v.findViewById(R.id.lvqty);
        lvupdate = (TextView) v.findViewById(R.id.lvupdate);
        lvstatus = (TextView) v.findViewById(R.id.lvstatus);

        productPhoto = (ImageView) v.findViewById(R.id.productPhoto);

    }
}