package com.zybooks.daydrinker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.zybooks.daydrinker.repo.DayRepository;

import java.util.List;

public class CalendarFragment extends Fragment {

    DayRepository dayRepo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dayRepo = DayRepository.getInstance(this.getContext());
        View contentView = inflater.inflate(R.layout.activity_calendar, container, false);

        TextView columnNamesTextView = contentView.findViewById(R.id.columnNamesTextView);

        String columnNames = DayRepository.COLUMN_DATE + "                  " + DayRepository.COLUMN_GOAL + "     " + DayRepository.COLUMN_PROGRESS;
        columnNamesTextView.setText(columnNames);

        ListView listView = contentView.findViewById(R.id.currentWeek);

        Adapter listAdapter = new Adapter(dayRepo.getCurrentWeek());
        listView.setAdapter(listAdapter);

        int streak = dayRepo.getCurrentStreak();
        TextView streakTextView = contentView.findViewById(R.id.streak);
        streakTextView.setText(String.valueOf(streak));

        int maxStreak = dayRepo.getMaxStreak();
        TextView maxStreakTextView = contentView.findViewById(R.id.maxStreak);
        maxStreakTextView.setText(String.valueOf(maxStreak));

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
