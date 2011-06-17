package org.whiskeysierra.impairedvision;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import com.google.common.collect.Ordering;

import java.util.List;

public class ImpairedVision extends Activity implements SurfaceHolder.Callback {

    private Camera camera;
    private boolean inPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final SurfaceView preview = (SurfaceView) findViewById(R.id.preview);
        final SurfaceHolder holder = preview.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void onResume() {
        super.onResume();
        camera = Camera.open();
        camera.setPreviewCallback(new FilterCallback());
    }

    @Override
    public void onPause() {
        if (inPreview) {
            camera.stopPreview();
        }

        camera.release();
        camera = null;
        inPreview = false;

        super.onPause();
    }

    private Camera.Size getBestPreviewSize(double width, double height, Camera.Parameters parameters) {
        final List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        final Ordering<Camera.Size> ordering = new AspectRatioOrdering(width / height);
        return ordering.min(sizes);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (camera == null) {
            return;
        } else {
            try {
                camera.setPreviewDisplay(holder);
            } catch (Throwable t) {
                Log.e("ImpairedVision-surfaceCallback", "Exception in setPreviewDisplay()", t);
                Toast.makeText(ImpairedVision.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera == null) {
            return;
        } else {
            final Camera.Parameters parameters = camera.getParameters();
            final Camera.Size size = getBestPreviewSize(width, height, parameters);

            if (size == null) {
                return;
            } else {
                parameters.setPreviewSize(size.width, size.height);
                camera.setParameters(parameters);
                camera.startPreview();
                inPreview = true;
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}
