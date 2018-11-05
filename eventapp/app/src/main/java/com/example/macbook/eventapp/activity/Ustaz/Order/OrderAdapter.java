package com.example.macbook.eventapp.activity.Ustaz.Order;

/**
 * Created by Haziq on 3/4/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.PicassoClient;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderAdapter extends BaseAdapter {

    Context c;
    ArrayList<HashMap<String, String>> orderlist;
    LayoutInflater inflater;

    public OrderAdapter(Context c, ArrayList<HashMap<String, String>> data) {
        this.c = c;
        this.orderlist = data;
    }

    @Override
    public int getCount() {
        return orderlist.size();
    }

    @Override
    public Object getItem(int position) {
        return orderlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
        {
            inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.orderlist, parent,false);

        }

        //BIND DATA
        OrderHolder holder = new OrderHolder(convertView);
        holder.lvfullname.setText(orderlist.get(position).get(OrderConfig.TAG_ORDERNAME));
        holder.lvaddress.setText(orderlist.get(position).get(OrderConfig.TAG_ORDERADDRESS));
        holder.lvemail.setText(orderlist.get(position).get(OrderConfig.TAG_ORDEREMAIL));
        holder.lvcontact.setText(orderlist.get(position).get(OrderConfig.TAG_ORDERCONTACT));
        holder.lvproduct.setText(orderlist.get(position).get(OrderConfig.TAG_PNAME));
        holder.lvqty.setText(orderlist.get(position).get(OrderConfig.TAG_PQTY));
        holder.lvprice.setText(orderlist.get(position).get(OrderConfig.TAG_PPRICE));
        holder.lvcreated.setText(orderlist.get(position).get(OrderConfig.TAG_PAYDATE));


        return convertView;
    }
}