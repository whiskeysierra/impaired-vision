package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.hardware.Camera;

public class ColorBlindness implements Vision {

    private final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(new float[]{
            0.3f, 0.6f, 0.1f, 0.0f, 0.0f,
            0.3f, 0.6f, 0.1f, 0.0f, 0.0f,
            0.3f, 0.6f, 0.1f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
    });

    @Override
    public void configure(Camera camera) {
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(parameters);
        camera.autoFocus(AutoFocusCallbacks.NOOP);
    }

    @Override
    public ColorFilter getFilter() {
        return filter;
    }

    @Override
    public String getName() {
        return "Color Blindness";
    }
}
