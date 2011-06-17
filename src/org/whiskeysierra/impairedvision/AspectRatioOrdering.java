package org.whiskeysierra.impairedvision;

import android.hardware.Camera;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;

public class AspectRatioOrdering extends Ordering<Camera.Size> {

    private final double ratio;

    public AspectRatioOrdering(double ratio) {
        this.ratio = ratio;
    }

    private double ratio(Camera.Size size) {
        return (double) size.width / (double) size.height;
    }

    @Override
    public int compare(Camera.Size left, Camera.Size right) {
        return Doubles.compare(Math.abs(ratio - ratio(left)), Math.abs(ratio - ratio(right)));
    }

}
