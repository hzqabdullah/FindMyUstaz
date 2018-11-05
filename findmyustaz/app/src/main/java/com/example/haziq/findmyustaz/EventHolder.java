package com.example.haziq.findmyustaz;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Haziq on 3/4/2018.
 */

public class EventHolder {

    TextView lvtitle, lvdate, lvdateend, lvday, lvtime, lvvenue, lvustaz, lvdistance;
    ImageView eventBanner;

    public EventHolder(View v) {

        lvtitle = (TextView) v.findViewById(R.id.lvtitle);
        lvdate = (TextView) v.findViewById(R.id.lvdate);
        lvdateend = (TextView) v.findViewById(R.id.lvdateend);
        lvday = (TextView) v.findViewById(R.id.lvday);
        lvtime = (TextView) v.findViewById(R.id.lvtime);
        lvvenue = (TextView) v.findViewById(R.id.lvvenue);
        lvustaz = (TextView) v.findViewById(R.id.lvustaz);
        lvdistance = (TextView) v.findViewById(R.id.lvdistance);
        eventBanner = (ImageView) v.findViewById(R.id.eventBanner);

    }
}
