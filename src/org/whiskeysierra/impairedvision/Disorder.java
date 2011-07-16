package org.whiskeysierra.impairedvision;

import android.hardware.Camera;
import com.google.common.base.Function;

public interface Disorder {

    public static final Function<Disorder, String> NAME = new Function<Disorder, String>() {

        @Override
        public String apply(Disorder disorder) {
            return disorder.getName();
        }

    };

    void applyTo(Camera camera);

    String getName();

}
