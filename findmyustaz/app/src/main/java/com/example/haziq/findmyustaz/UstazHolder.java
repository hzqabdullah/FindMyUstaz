package com.example.haziq.findmyustaz;

/**
 * Created by Haziq on 3/4/2018.
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;


public class UstazHolder {

    TextView lvprofilename, lvfirstname, lvlastname, lvplace;
    CircleImageView ustazPhoto;

    public UstazHolder(View v) {

        lvprofilename = (TextView) v.findViewById(R.id.lvprofilename);
        lvfirstname = (TextView) v.findViewById(R.id.lvfirstname);
        lvlastname = (TextView) v.findViewById(R.id.lvlastname);
        lvplace = (TextView) v.findViewById(R.id.lvplace);
        ustazPhoto = (CircleImageView) v.findViewById(R.id.ustazPhoto);

    }
}