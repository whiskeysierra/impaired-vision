package org.whiskeysierra.impairedvision;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

final class SwipeDetector extends GestureDetector implements View.OnClickListener, View.OnTouchListener {

    public SwipeDetector(OnGestureListener listener) {
        super(listener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {

    }

}
