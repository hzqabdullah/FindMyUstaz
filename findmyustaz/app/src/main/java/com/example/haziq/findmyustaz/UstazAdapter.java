package com.example.haziq.findmyustaz;

/**
 * Created by Haziq on 3/4/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class UstazAdapter extends BaseAdapter {

    Context c;
    ArrayList<HashMap<String, String>> ustazlist;
    LayoutInflater inflater;

    public UstazAdapter(Context c, ArrayList<HashMap<String, String>> data) {
        this.c = c;
        this.ustazlist = data;
    }

    @Override
    public int getCount() {
        return ustazlist.size();
    }

    @Override
    public Object getItem(int position) {
        return ustazlist.get(position);
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
            convertView = inflater.inflate(R.layout.ustazlist, parent,false);

        }

        //BIND DATA
        UstazHolder holder = new UstazHolder(convertView);
        holder.lvprofilename.setText(ustazlist.get(position).get(UserConfig.TAG_UPROFILENAME));
        holder.lvfirstname.setText(ustazlist.get(position).get(UserConfig.TAG_UFIRSTNAME));
        holder.lvlastname.setText(ustazlist.get(position).get(UserConfig.TAG_ULASTNAME));
        holder.lvplace.setText(ustazlist.get(position).get(UserConfig.TAG_UPLACE));
        PicassoUstaz.downloadImage(c, ustazlist.get(position).get(UserConfig.TAG_UPHOTO), holder.ustazPhoto);

        return convertView;
    }
}