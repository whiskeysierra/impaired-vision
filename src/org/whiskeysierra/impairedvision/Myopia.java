package org.whiskeysierra.impairedvision;

import android.hardware.Camera;

public class Myopia implements Disorder {

    @Override
    public void applyTo(Camera camera) {
        camera.autoFocus(AutoFocusCallbacks.NOOP);
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        camera.setParameters(parameters);
    }

    @Override
    public String getName() {
        return "Myopia";
    }

}