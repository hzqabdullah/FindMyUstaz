package com.example.haziq.findmyustaz;

/**
 * Created by Haziq on 3/4/2018.
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ProductHolder {

    TextView lvname, lvprice, lvqty, lvtotal, lvadded, lvustaz;
    TextView lvfullname, lvemail, lvcreated, lvstatus;
    ImageView productPhoto;

    public ProductHolder(View v) {

        lvname = (TextView) v.findViewById(R.id.lvname);
        lvprice = (TextView) v.findViewById(R.id.lvprice);
        lvqty = (TextView) v.findViewById(R.id.lvqty);
        lvtotal = (TextView) v.findViewById(R.id.lvtotal);
        lvadded = (TextView) v.findViewById(R.id.lvadded);
        lvustaz = (TextView) v.findViewById(R.id.lvustaz);
        lvfullname = (TextView) v.findViewById(R.id.lvfullname);
        lvemail = (TextView) v.findViewById(R.id.lvemail);
        lvcreated = (TextView) v.findViewById(R.id.lvcreated);
        lvstatus = (TextView) v.findViewById(R.id.lvstatus);

        productPhoto = (ImageView) v.findViewById(R.id.productPhoto);

    }
}