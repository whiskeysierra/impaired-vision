package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.hardware.Camera;

public class Myopia implements Vision {

    @Override
    public void configure(Camera camera) {
        camera.autoFocus(AutoFocusCallbacks.NOOP);
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        camera.setParameters(parameters);
    }

    @Override
    public ColorFilter getFilter() {
        return null;
    }

    @Override
    public String getName() {
        return "Myopia";
    }

}
