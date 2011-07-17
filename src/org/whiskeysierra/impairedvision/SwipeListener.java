package org.whiskeysierra.impairedvision;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwipeListener extends GestureDetector.SimpleOnGestureListener {

    private static final Logger LOG = LoggerFactory.getLogger(SwipeListener.class);

    private final VisionActivity activity;
    private final int minimumDistance;
    private final int minimumVelocity;

    public SwipeListener(VisionActivity activity) {
        this.activity = activity;
        final ViewConfiguration configuration = ViewConfiguration.get(activity);
        this.minimumDistance = configuration.getScaledTouchSlop();
        this.minimumVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    @Override
    public boolean onFling(MotionEvent a, MotionEvent b, float velocityX, float velocityY) {
        if (a.getX() - b.getX() > minimumDistance && Math.abs(velocityX) > minimumVelocity) {
            activity.switchToNext();
            return true;
        } else if (b.getX() - a.getX() > minimumDistance && Math.abs(velocityX) > minimumVelocity) {
            activity.switchToPrevious();
            return true;
        } else {
            return false;
        }
    }

}
