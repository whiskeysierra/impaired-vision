package org.whiskeysierra.impairedvision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

public class CustomSurfaceView extends SurfaceView {

    protected final Paint rectanglePaint = new Paint();

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rectanglePaint.setARGB(100, 200, 0, 0);
        rectanglePaint.setStyle(Paint.Style.FILL);
        rectanglePaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(new Rect((int) Math.random() * 100,
                (int) Math.random() * 100, 200, 200), rectanglePaint);
        Log.w(this.getClass().getName(), "On Draw Called");
    }

}
