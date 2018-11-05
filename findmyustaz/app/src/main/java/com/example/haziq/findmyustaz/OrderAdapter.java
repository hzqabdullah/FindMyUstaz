package com.example.haziq.findmyustaz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Haziq on 3/4/2018.
 */

public class OrderAdapter extends BaseAdapter {

    Context c;
    ArrayList<HashMap<String, String>> cartlist;
    LayoutInflater inflater;

    public OrderAdapter(Context c, ArrayList<HashMap<String, String>> data) {
        this.c = c;
        this.cartlist = data;
    }

    @Override
    public int getCount() {
        return cartlist.size();
    }

    @Override
    public Object getItem(int position) {
        return cartlist.get(position);
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
        ProductHolder holder = new ProductHolder(convertView);
        holder.lvfullname.setText(cartlist.get(position).get(UserConfig.TAG_PAYFULLNAME));
        holder.lvemail.setText(cartlist.get(position).get(UserConfig.TAG_PAYEMAIL));
        holder.lvqty.setText(cartlist.get(position).get(UserConfig.TAG_PAYQTY));
        holder.lvprice.setText(cartlist.get(position).get(UserConfig.TAG_PAYPRICE));
        holder.lvcreated.setText(cartlist.get(position).get(UserConfig.TAG_PAYCREATED));

        return convertView;
    }
}
