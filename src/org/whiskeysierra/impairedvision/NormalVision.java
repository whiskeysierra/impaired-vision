package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;

final class NormalVision extends AbstractVision {

    @Override
    public ColorFilter getFilter() {
        return null;
    }

    @Override
    public String getName() {
        return "Normal Vision";
    }

}
