package org.whiskeysierra.impairedvision;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImpairedVision extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private static final Logger LOG = LoggerFactory.getLogger(ImpairedVision.class);

    private final int dialog = 0;

    private Camera camera;
    private SurfaceView preview;
    private SurfaceHolder holder;
    private Camera.Size size;

    private final ImmutableList<Vision> visions = ImmutableList.of(
            new NormalVision(),
            new Myopia(),
            new Protanopia(),
            new Deuteranopia(),
            new Tritanopia(),
            new Achromatopia(),
            new AchromatopiaAndMyopia()
    );

    private Vision current = visions.get(0);

    private int[] rgb;
    private final Paint paint = new Paint();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main);

        preview = (SurfaceView) findViewById(R.id.preview);

        holder = preview.getHolder();
        holder.addCallback(this);

        preview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDialog(dialog);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {
            camera = Camera.open();
            size = camera.getParameters().getPreviewSize();
        }
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (camera != null) {
            synchronized (this) {
                preview.setWillNotDraw(false);
                camera.startPreview();
                current.configure(camera);
                rgb = new int[size.width * size.height];
                camera.setPreviewCallback(this);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case dialog: {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Choose disorder");

                final CharSequence[] items = Iterables.toArray(Iterables.transform(visions, Vision.NAME), CharSequence.class);
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        final Vision selected = visions.get(i);

                        if (current != selected) {
                            current = visions.get(i);
                            LOG.debug("Switched to {}", current);
                            current.configure(camera);
                            paint.setColorFilter(current.getFilter());
                        }
                    }

                });

                return builder.create();
            }
            default: {
                return super.onCreateDialog(id);
            }
        }
    }

    @Override
    public void onPreviewFrame(byte[] yuv, Camera camera) {
        if (holder == null) return;

        Canvas canvas = null;

        try {
            canvas = holder.lockCanvas(null);
            Yuv420.decode(yuv, rgb, size.width, size.height);
            canvas.drawBitmap(rgb, 0, size.width, 0, 0, size.width, size.height, false, paint);
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

}
