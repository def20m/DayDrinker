package com.zybooks.daydrinker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.zybooks.daydrinker.model.Day;
import com.zybooks.daydrinker.repo.DayRepository;

import java.util.List;

public class CalendarFragment extends Fragment {

    DayRepository dayRepo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CalendarFragment", "Fragment is being created");

        dayRepo = DayRepository.getInstance(this.getContext());

        View contentView = inflater.inflate(R.layout.activity_calendar, container, false);
        ListView listView = contentView.findViewById(R.id.currentWeek);

        Adapter listAdapter = new Adapter(dayRepo.getCurrentWeek());

        listView.setAdapter(listAdapter);

        return contentView;
    }

    class Adapter extends BaseAdapter {
        List<String> items;

        public Adapter(List<String> items) {
            super();
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return items.get(i).hashCode();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(getContext());
            textView.setText(items.get(i));
            return textView;
        }
    }
}
