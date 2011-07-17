package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;

final class Protanopia extends AbstractVision {

    private final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(new float[]{
            0.567f, 0.433f, 0.0f, 0.0f, 0.0f,
            0.558f, 0.442f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.242f, 0.758f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
    });

    @Override
    public ColorFilter getFilter() {
        return filter;
    }

    @Override
    public String getName() {
        return "Protanopia";
    }

}
