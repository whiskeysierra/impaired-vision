package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.hardware.Camera;

final class Achromatopia implements Vision {

    private final Myopia myopia = new Myopia();

    private final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(new float[]{
            0.299f, 0.587f, 0.114f, 0.0f, 0.0f,
            0.299f, 0.587f, 0.114f, 0.0f, 0.0f,
            0.299f, 0.587f, 0.114f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
    });

    @Override
    public void configure(Camera camera) {
        myopia.configure(camera);
    }

    @Override
    public ColorFilter getFilter() {
        return filter;
    }

    @Override
    public String getName() {
        return "Achromatopia";
    }

}
