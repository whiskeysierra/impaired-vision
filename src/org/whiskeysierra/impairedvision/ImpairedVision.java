package org.whiskeysierra.impairedvision;


import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ImpairedVision extends Activity {
    
    private Preview preview;
    private Camera camera = Camera.open();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preview = new Preview(this);
        setContentView(preview);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (camera != null) {
            preview.setCamera(null);
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        camera = Camera.open();
        preview.setCamera(camera);
    }

}
