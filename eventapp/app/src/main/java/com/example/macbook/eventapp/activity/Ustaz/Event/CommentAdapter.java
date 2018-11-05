package com.example.macbook.eventapp.activity.Ustaz.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.PicassoClient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Haziq on 9/4/2018.
 */

public class CommentAdapter extends BaseAdapter {


        Context c;
        ArrayList<HashMap<String, String>> list;
        LayoutInflater inflater;

        public CommentAdapter(Context c, ArrayList<HashMap<String, String>> data) {
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
                convertView = inflater.inflate(R.layout.commentlist, parent,false);

            }

            //BIND DATA
            CommentHolder holder = new CommentHolder(convertView);
            holder.txtcomment.setText(list.get(position).get(Config.TAG_COMMENT));
            holder.txtuser.setText(list.get(position).get(Config.TAG_USER));

            PicassoClient.downloadImage(c, list.get(position).get(Config.TAG_USERPHOTO), holder.userPhoto);

            return convertView;
        }

}
