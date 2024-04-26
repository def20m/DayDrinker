package com.zybooks.daydrinker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class CalendarFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CalendarFragment", "Fragment is being created");
        return inflater.inflate(R.layout.activity_calendar, container, false);
    }
}
