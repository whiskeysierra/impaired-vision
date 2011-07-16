package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.hardware.Camera;

public class Protanopia implements Vision {

    private final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(new float[]{
            0.567f, 0.433f, 0.0f, 0.0f, 0.0f,
            0.558f, 0.442f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.242f, 0.758f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
    });

    @Override
    public void configure(Camera camera) {
        camera.autoFocus(AutoFocusCallbacks.NOOP);
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(parameters);
    }

    @Override
    public ColorFilter getFilter() {
        return filter;
    }

    @Override
    public String getName() {
        return "Protanopia";
    }
}
