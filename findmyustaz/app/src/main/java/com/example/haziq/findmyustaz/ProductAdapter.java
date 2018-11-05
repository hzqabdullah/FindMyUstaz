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

public class ProductAdapter extends BaseAdapter {

    Context c;
    ArrayList<HashMap<String, String>> productlist;
    LayoutInflater inflater;

    public ProductAdapter(Context c, ArrayList<HashMap<String, String>> data) {
        this.c = c;
        this.productlist = data;
    }

    @Override
    public int getCount() {
        return productlist.size();
    }

    @Override
    public Object getItem(int position) {
        return productlist.get(position);
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
            convertView = inflater.inflate(R.layout.shoplist, parent,false);

        }

        //BIND DATA
        ProductHolder holder = new ProductHolder(convertView);
        holder.lvname.setText(productlist.get(position).get(UserConfig.TAG_PNAME));
        holder.lvprice.setText(productlist.get(position).get(UserConfig.TAG_PPRICE));
        holder.lvqty.setText(productlist.get(position).get(UserConfig.TAG_PQUANTITY));
        PicassoEvent.downloadImage(c, productlist.get(position).get(UserConfig.TAG_PIMAGE), holder.productPhoto);

        return convertView;
    }
}
