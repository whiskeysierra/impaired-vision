package org.whiskeysierra.impairedvision;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ImpairedVision extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private static final Logger LOG = LoggerFactory.getLogger(ImpairedVision.class);

    private final int diseaseChooser = 0;

    private Camera camera;
    private SurfaceView preview;
    private SurfaceHolder holder;

    private ImmutableList<Disorder> disorders = ImmutableList.of(
        new NormalVision(),
        new Myopia(),
        new RedGreenDeficiency()
    );
    private Iterable<String> disorderNames = Iterables.transform(disorders, Disorder.NAME);
    private Disorder current = disorders.get(0);

    private byte[] rgbbuffer = new byte[256 * 256];
    private int[] rgbints = new int[256 * 256];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main);

        preview = (SurfaceView) findViewById(R.id.preview);

        holder = preview.getHolder();
        holder.addCallback(this);
        //holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);

        preview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDialog(diseaseChooser);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {
            camera = Camera.open();
        }
    }

    @Override
    protected void onPause() {
        if (camera != null) {
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
//                try {
                    //camera.setPreviewDisplay(holder);
                    camera.startPreview();
                    current.applyTo(camera);
                    camera.setPreviewCallback(this);
//                } catch (IOException e) {
//                    camera.release();
//                    camera = null;
//                }
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case diseaseChooser: {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Choose disorder");

                final CharSequence[] items = Iterables.toArray(disorderNames, CharSequence.class);
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        final Disorder selected = disorders.get(i);

                        if (current == selected) {
                            LOG.debug("Selected disorder is already in use");
                        } else {
                            current = disorders.get(i);
                            LOG.debug("Switched to {}", current);
                            current.applyTo(camera);
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
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        Log.d("Camera", "Got a camera frame");

        Canvas c = null;

        if (holder == null) {
            return;
        }

        try {
            synchronized (holder) {
                c = holder.lockCanvas(null);

                // Do your drawing here
                // So this data value you're getting back is formatted in YUV format and you can't do much
                // with it until you convert it to rgb
                int bwCounter = 0;
                int yuvsCounter = 0;
                for (int y = 0; y < 160; y++) {
                    System.arraycopy(bytes, yuvsCounter, rgbbuffer, bwCounter, 240);
                    yuvsCounter = yuvsCounter + 240;
                    bwCounter = bwCounter + 256;
                }

                for (int i = 0; i < rgbints.length; i++) {
                    rgbints[i] = (int) rgbbuffer[i];
                }

                //decodeYUV(rgbbuffer, data, 100, 100);
                c.drawBitmap(rgbints, 0, 256, 0, 0, 256, 256, false, new Paint());

                Log.d("SOMETHING", "Got Bitmap");

            }
        } finally {
            // do this in a finally so that if an exception is thrown
            // during the above, we don't leave the Surface in an
            // inconsistent state
            if (c != null) {
                holder.unlockCanvasAndPost(c);
            }
        }
    }

}
