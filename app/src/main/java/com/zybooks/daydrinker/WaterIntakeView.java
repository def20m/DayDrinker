package com.zybooks.daydrinker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

public class WaterIntakeView extends View {
    private Paint paintText;
    private Paint paintCircle;
    private Paint paintArc;
    private RectF oval;
    private float sweepAngle = 0;

    // Water intake variables
    private int goal = 8;
    private int currentIntake = 0;

    public WaterIntakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

        String percentage = String.format(Locale.getDefault(), "%.0f%%", (currentIntake / (float) goal) * 100);
        int xPos = getWidth() / 2;
        int yPos = (int) ((getHeight() / 2) - ((paintText.descent() + paintText.ascent()) / 2));
        canvas.drawText(percentage, xPos, yPos, paintText);
        setCurrentIntake(currentIntake);
    }

    public void setCurrentIntake(int intake) {
        this.currentIntake = intake;
        updateSweepAngle();
        invalidate();
    }

    private void updateSweepAngle() {
        if (currentIntake >= goal) {
            sweepAngle = 360;
        } else {
            sweepAngle = (currentIntake / (float) goal) * 360;
        }
    }
}
