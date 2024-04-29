package com.zybooks.daydrinker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.os.Parcelable;


import androidx.preference.PreferenceManager;

import com.zybooks.daydrinker.repo.DayRepository;

import java.util.Locale;

public class WaterIntakeView extends View {
    private Paint paintText;
    private Paint paintCircle;
    private Paint paintArc;
    private RectF oval;
    private float sweepAngle = 0;
    private DayRepository dayRepo;

    // Water intake variables
    private int goalValue;
    private int currentIntake;

    public WaterIntakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dayRepo = DayRepository.getInstance(this.getContext());
        init();
    }

    private void init() {
        // Circle paint
        paintCircle = new Paint();
        paintCircle.setColor(Color.LTGRAY);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setStrokeWidth(40);
        paintCircle.setAntiAlias(true);

        // Arc paint
        paintArc = new Paint();
        paintArc.setColor(Color.GREEN);
        paintArc.setStyle(Paint.Style.STROKE);
        paintArc.setStrokeWidth(40);
        paintArc.setAntiAlias(true);

        // Text paint
        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(80);
        paintText.setAntiAlias(true);
        oval = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String goalString = preferences.getString("daily_intake", "12"); // "0" is the default value if the preference is not found

        try {
            goalValue = Integer.parseInt(goalString);
        } catch (NumberFormatException e) {
            goalValue = 0;
        }
        Log.d("HomeFragment", "goalValue: " + goalValue);
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 20;

        // Set the bounds for the circle and arc
        oval.set((float) width / 2 - radius, (float) height / 2 - radius, (float) width / 2 + radius, (float) height / 2 + radius);

        // Draw the circle (background)
        canvas.drawCircle((float) width / 2, (float) height / 2, radius, paintCircle);

        // Draw the arc (water intake)
        canvas.drawArc(oval, -90, sweepAngle, false, paintArc);

        String percentage = String.format(Locale.getDefault(), "%.0f%%", (currentIntake / (float) goalValue) * 100);
        Log.d("HomeFragment", "CURRENT INTAKE " + currentIntake);
        Log.d("HomeFragment", "PERCENTAGE " + percentage);
        int xPos = getWidth() / 2;
        int yPos = (int) ((getHeight() / 2) - ((paintText.descent() + paintText.ascent()) / 2));
        canvas.drawText(percentage, xPos, yPos, paintText);
        setCurrentIntake();
    }

    public void setCurrentIntake() {
        int intake = dayRepo.getCurrentDay().getProgress();
        this.currentIntake = intake;
        updateSweepAngle();
        invalidate();
    }

    private void updateSweepAngle() {
        if (currentIntake >= goalValue) {
            sweepAngle = 360;
        } else {
            sweepAngle = (currentIntake / (float) goalValue) * 360;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentIntake = currentIntake;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setCurrentIntake();
    }

    static class SavedState extends BaseSavedState {
        int currentIntake;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentIntake = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentIntake);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}
