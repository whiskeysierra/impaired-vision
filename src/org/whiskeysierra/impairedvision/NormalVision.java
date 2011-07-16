package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.hardware.Camera;

public class NormalVision implements Vision {

    @Override
    public void configure(Camera camera) {
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(parameters);
        camera.autoFocus(AutoFocusCallbacks.NOOP);
    }

    @Override
    public ColorFilter getFilter() {
        return null;
    }

    @Override
    public String getName() {
        return "Normal Vision";
    }
}
