package org.whiskeysierra.impairedvision;

import android.graphics.ColorFilter;
import android.hardware.Camera;
import com.google.common.base.Function;

public interface Vision {

    public static final Function<Vision, String> NAME = new Function<Vision, String>() {

        @Override
        public String apply(Vision vision) {
            return vision.getName();
        }

    };

    void configure(Camera camera);

    ColorFilter getFilter();

    String getName();

}
