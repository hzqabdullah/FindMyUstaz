package com.example.macbook.eventapp.activity.Ustaz.Event;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macbook.eventapp.R;

/**
 * Created by Haziq on 3/4/2018.
 */

public class EventHolder {

    TextView lvtitle, lvdate, lvdateend, lvday, lvtime, lvvenue, lvupdate1;
    ImageView eventBanner;

    public EventHolder(View v) {

        lvtitle = (TextView) v.findViewById(R.id.lvtitle);
        lvdate = (TextView) v.findViewById(R.id.lvdate);
        lvdateend = (TextView) v.findViewById(R.id.lvdateend);
        lvday = (TextView) v.findViewById(R.id.lvday);
        lvtime = (TextView) v.findViewById(R.id.lvtime);
        lvvenue = (TextView) v.findViewById(R.id.lvvenue);
        lvupdate1 = (TextView) v.findViewById(R.id.lvupdate1);

        eventBanner = (ImageView) v.findViewById(R.id.eventBanner);

    }
}
