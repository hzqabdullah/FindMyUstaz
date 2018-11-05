package com.example.haziq.findmyustaz;

/**
 * Created by Haziq on 3/4/2018.
 */

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoUstaz {

    public static void downloadImage(Context c, String url, ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Picasso.with(c).load(url).placeholder(R.drawable.ustaz).into(img);
        }else
        {
            Picasso.with(c).load(R.drawable.ustaz).into(img);
        }
    }

}
