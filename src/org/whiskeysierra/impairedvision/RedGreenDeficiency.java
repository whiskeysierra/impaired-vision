package org.whiskeysierra.impairedvision;

import android.hardware.Camera;

public class RedGreenDeficiency implements Disorder {

    @Override
    public void applyTo(Camera camera) {
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(parameters);
        camera.autoFocus(AutoFocusCallbacks.NOOP);
    }

    @Override
    public String getName() {
        return "Red-Green-Deficiency";
    }
}
