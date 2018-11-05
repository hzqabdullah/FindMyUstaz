package com.example.macbook.eventapp.activity.Ustaz.Order;

import android.view.View;
import android.widget.TextView;

import com.example.macbook.eventapp.R;


/**
 * Created by Haziq on 29/4/2018.
 */

public class OrderHolder {

    TextView lvfullname, lvaddress, lvemail, lvcontact, lvproduct, lvqty, lvprice, lvcreated;

    public OrderHolder(View v) {

        lvfullname = (TextView) v.findViewById(R.id.lvfullname);
        lvaddress = (TextView) v.findViewById(R.id.lvaddress);
        lvemail = (TextView) v.findViewById(R.id.lvemail);
        lvcontact = (TextView) v.findViewById(R.id.lvcontact);
        lvproduct = (TextView) v.findViewById(R.id.lvproduct);
        lvqty = (TextView) v.findViewById(R.id.lvqty);
        lvcreated = (TextView) v.findViewById(R.id.lvcreated);
        lvprice = (TextView) v.findViewById(R.id.lvprice);

    }
}
