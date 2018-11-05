package com.example.macbook.eventapp.activity.Ustaz.Event;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.macbook.eventapp.R;

/**
 * Created by Haziq on 1/3/2018.
 */

public class MapsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.mapfragment, container, false);
        return v;
    }
}
