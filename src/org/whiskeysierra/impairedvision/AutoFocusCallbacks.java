package org.whiskeysierra.impairedvision;

import android.hardware.Camera;

public class AutoFocusCallbacks {

    public static final Camera.AutoFocusCallback NOOP = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean b, Camera camera) {

        }

    };

}
