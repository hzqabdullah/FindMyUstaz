package com.example.macbook.eventapp.activity.Ustaz;

import android.content.Context;
import android.widget.ImageView;

import com.example.macbook.eventapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Haziq on 3/4/2018.
 */

public class PicassoClient {

    public static void downloadImage(Context c, String url, ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Picasso.with(c).load(url).placeholder(R.drawable.placeholder).into(img);
        }
        else
        {
            Picasso.with(c).load(R.drawable.placeholder).into(img);
        }
    }
}
