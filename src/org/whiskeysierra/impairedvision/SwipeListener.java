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
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > minimumDistance && Math.abs(velocityX) > minimumVelocity) {
            activity.switchToNext();
        } else if (e2.getX() - e1.getX() > minimumDistance && Math.abs(velocityX) > minimumVelocity) {
            activity.switchToPrevious();
        }

        return false;
    }

}
