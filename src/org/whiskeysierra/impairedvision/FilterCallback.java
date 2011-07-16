package org.whiskeysierra.impairedvision;

import android.hardware.Camera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FilterCallback implements Camera.PreviewCallback {

    private static final Logger LOG = LoggerFactory.getLogger(FilterCallback.class);

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        LOG.debug("We got a frame here!");
    }

}
