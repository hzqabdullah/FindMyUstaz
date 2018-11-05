package com.example.macbook.eventapp.activity.Ustaz.Event;

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

public class ArchiveAdapter extends BaseAdapter {

    Context c;
    ArrayList<HashMap<String, String>> eventlist;
    LayoutInflater inflater;

    public ArchiveAdapter(Context c, ArrayList<HashMap<String, String>> data) {
        this.c = c;
        this.eventlist = data;
    }

    @Override
    public int getCount() {
        return eventlist.size();
    }

    @Override
    public Object getItem(int position) {
        return eventlist.get(position);
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
            convertView = inflater.inflate(R.layout.eventlist, parent,false);

        }

        //BIND DATA
        EventHolder holder = new EventHolder(convertView);
        holder.lvtitle.setText(eventlist.get(position).get(EventConfig.TAG_ETITLE));
        holder.lvdate.setText(eventlist.get(position).get(EventConfig.TAG_EDATE));
        holder.lvdateend.setText(eventlist.get(position).get(EventConfig.TAG_EDATEEND));
        holder.lvday.setText(eventlist.get(position).get(EventConfig.TAG_EDAY));
        holder.lvtime.setText(eventlist.get(position).get(EventConfig.TAG_ETIME));
        holder.lvvenue.setText(eventlist.get(position).get(EventConfig.TAG_EVENUE));
        holder.lvupdate1.setText(eventlist.get(position).get(EventConfig.TAG_EINSERT));
        PicassoClient.downloadImage(c, eventlist.get(position).get(EventConfig.TAG_EIMAGE), holder.eventBanner);

        return convertView;
    }
}