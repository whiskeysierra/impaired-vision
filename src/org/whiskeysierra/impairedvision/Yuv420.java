package org.whiskeysierra.impairedvision;

import android.graphics.BitmapRegionDecoder;
import com.google.common.base.Preconditions;

public class Yuv420 {

    public static void decode(byte[] yuv, int[] rgb, int width, int height) {
        final int size = width * height;

        Preconditions.checkNotNull(yuv, "yuv");
        Preconditions.checkArgument(yuv.length >= size, "buffer 'yuv' size < minimum");
        Preconditions.checkNotNull(rgb, "rgb");
        Preconditions.checkArgument(rgb.length >= size, "Buffer 'rgb' size < minimum");

        int cr = 0;
        int cb = 0;

        for (int j = 0; j < height; j++) {
            int index = j * width;
            final int halfJ = j >> 1;

            for (int i = 0; i < width; i++) {
                int y = yuv[index];

                if (y < 0) {
                    y += 255;
                }

                if ((i & 0x1) != 1) {
                    final int offset = size + halfJ * width + (i >> 1) * 2;

                    cb = yuv[offset];
                    if (cb < 0) {
                        cb += 127;
                    } else {
                        cb -= 128;
                    }

                    cr = yuv[offset + 1];
                    if (cr < 0) {
                        cr += 127;
                    } else {
                        cr -= 128;
                    }
                }

                int r = y + cr + (cr >> 2) + (cr >> 3) + (cr >> 5);
                if (r < 0) {
                    r = 0;
                } else if (r > 255) {
                    r = 255;
                }

                int g = y - (cb >> 2) + (cb >> 4) + (cb >> 5) - (cr >> 1) + (cr >> 3) + (cr >> 4) + (cr >> 5);
                if (g < 0) {
                    g = 0;
                } else if (g > 255) {
                    g = 255;
                }

                int b = y + cb + (cb >> 1) + (cb >> 2) + (cb >> 6);
                if (b < 0) {
                    b = 0;
                } else if (b > 255) {
                    b = 255;
                }

                rgb[index++] = 0xff000000 + (b << 16) + (g << 8) + r;
            }
        }
    }

}
