package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.hardware.Camera;

final class AchromatopiaAndMyopia implements Vision {

    private final Achromatopia blindness = new Achromatopia();
    private final Myopia myopia = new Myopia();

    @Override
    public void configure(Camera camera) {
        myopia.configure(camera);
    }

    @Override
    public ColorFilter getFilter() {
        return blindness.getFilter();
    }

    @Override
    public String getName() {
        return "Achromatopia and Myopia";
    }

}
