package com.example.macbook.eventapp.activity.Ustaz.Event;

/**
 * Created by Haziq on 3/4/2018.
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macbook.eventapp.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentHolder {

    TextView txtuser, txtcomment;
    CircleImageView userPhoto;

    public CommentHolder(View v) {

        txtcomment = (TextView) v.findViewById(R.id.txtcomment);
        txtuser = (TextView) v.findViewById(R.id.txtuser);

        userPhoto = (CircleImageView) v.findViewById(R.id.userPhoto);

    }
}