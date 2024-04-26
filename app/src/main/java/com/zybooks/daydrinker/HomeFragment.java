package com.zybooks.daydrinker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private EditText editTextNumberCups;
    private Button buttonSubmit;
    private int totalDrank = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("HomeFragment", "Fragment is being created");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WaterIntakeView waterIntakeView = view.findViewById(R.id.waterIntakeView);
        editTextNumberCups = view.findViewById(R.id.editTextNumberCups);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("total water drank:", String.valueOf(totalDrank));
                    int amtDrank = Integer.parseInt(editTextNumberCups.getText().toString());
                    totalDrank = amtDrank + totalDrank;
                    waterIntakeView.setCurrentIntake(totalDrank);
                } catch (NumberFormatException e) {
                    editTextNumberCups.setError("Please enter a valid number");
                }
            }
        });
    }
}
