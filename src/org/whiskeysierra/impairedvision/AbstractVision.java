package org.whiskeysierra.impairedvision;

import android.hardware.Camera;

abstract class AbstractVision implements Vision{

    @Override
    public void configure(Camera camera) {
        camera.autoFocus(AutoFocusCallbacks.NOOP);
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(parameters);
    }

}
