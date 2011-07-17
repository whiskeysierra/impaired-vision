package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;

final class Deuteranopia extends AbstractVision {

    private final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(new float[]{
            0.625f, 0.375f, 0.0f, 0.0f, 0.0f,
            0.7f, 0.3f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.3f, 0.7f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
    });

    @Override
    public ColorFilter getFilter() {
        return filter;
    }

    @Override
    public String getName() {
        return "Deuteranopia";
    }

}
