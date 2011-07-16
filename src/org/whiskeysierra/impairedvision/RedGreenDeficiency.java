package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.hardware.Camera;

public class RedGreenDeficiency implements Vision {

    private final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(new float[]{
            2, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0,
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
        return "Red-Green-Deficiency";
    }
}
