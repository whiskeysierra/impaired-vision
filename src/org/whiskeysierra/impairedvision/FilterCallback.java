package org.whiskeysierra.impairedvision;

import android.hardware.Camera;
import android.util.Log;

public class FilterCallback implements Camera.PreviewCallback {

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        Log.d("ImpairedVision", "We got a frame here!");
    }

}
