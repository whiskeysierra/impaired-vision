package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.hardware.Camera;

public class NormalVision extends AbstractVision {

    @Override
    public ColorFilter getFilter() {
        return null;
    }

    @Override
    public String getName() {
        return "Normal Vision";
    }

}
