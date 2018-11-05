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

public class PaymentListAdapter extends BaseAdapter {

    Context c;
    ArrayList<HashMap<String, String>> list;
    LayoutInflater inflater;

    public PaymentListAdapter(Context c, ArrayList<HashMap<String, String>> data) {
        this.c = c;
        this.list = data;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = inflater.inflate(R.layout.paymenthistorylist, parent,false);

        }

        //BIND DATA
        ProductHolder holder = new ProductHolder(convertView);
        holder.lvname.setText(list.get(position).get(UserConfig.TAG_PNAME));
        holder.lvprice.setText(list.get(position).get(UserConfig.TAG_PPRICE));
        holder.lvqty.setText(list.get(position).get(UserConfig.TAG_PQUANTITY));
        holder.lvtotal.setText(list.get(position).get(UserConfig.TAG_TOTALPRICE));
        holder.lvustaz.setText(list.get(position).get(UserConfig.TAG_UPROFILENAME));
        holder.lvstatus.setText(list.get(position).get(UserConfig.TAG_PSTATUS));

        return convertView;
    }
}
