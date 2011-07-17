package org.whiskeysierra.impairedvision;

import android.hardware.Camera;

final class AutoFocusCallbacks {

    public static final Camera.AutoFocusCallback NOOP = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean b, Camera camera) {

        }

    };

}
