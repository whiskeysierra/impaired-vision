package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;

final class Achromatopia extends AbstractVision {

    private final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(new float[]{
            0.299f, 0.587f, 0.114f, 0.0f, 0.0f,
            0.299f, 0.587f, 0.114f, 0.0f, 0.0f,
            0.299f, 0.587f, 0.114f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
    });

    @Override
    public ColorFilter getFilter() {
        return filter;
    }

    @Override
    public String getName() {
        return "Achromatopia";
    }

}
