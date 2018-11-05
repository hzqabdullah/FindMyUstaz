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

public class CartAdapter extends BaseAdapter {

    Context c;
    ArrayList<HashMap<String, String>> cartlist;
    LayoutInflater inflater;

    public CartAdapter(Context c, ArrayList<HashMap<String, String>> data) {
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
            convertView = inflater.inflate(R.layout.cartlist, parent,false);

        }

        //BIND DATA
        ProductHolder holder = new ProductHolder(convertView);
        holder.lvname.setText(cartlist.get(position).get(UserConfig.TAG_PNAME));
        holder.lvprice.setText(cartlist.get(position).get(UserConfig.TAG_PPRICE));
        holder.lvqty.setText(cartlist.get(position).get(UserConfig.TAG_PQUANTITY));
        holder.lvtotal.setText(cartlist.get(position).get(UserConfig.TAG_TOTALPRICE));
        holder.lvustaz.setText(cartlist.get(position).get(UserConfig.TAG_UPROFILENAME));
        holder.lvadded.setText(cartlist.get(position).get(UserConfig.TAG_ADDED));
        PicassoEvent.downloadImage(c, cartlist.get(position).get(UserConfig.TAG_PIMAGE), holder.productPhoto);

        return convertView;
    }
}
