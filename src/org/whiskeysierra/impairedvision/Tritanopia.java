package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;

final class Tritanopia extends AbstractVision {

    private final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(new float[]{
            0.95f, 0.05f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.433f, 0.567f, 0.0f, 0.0f,
            0.0f, 0.475f, 0.525f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
    });

    @Override
    public ColorFilter getFilter() {
        return filter;
    }

    @Override
    public String getName() {
        return "Tritanopia";
    }

}
