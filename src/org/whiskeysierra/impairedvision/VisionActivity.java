package org.whiskeysierra.impairedvision;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VisionActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(VisionActivity.class);

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

    private boolean skipFrames;
    private int skipBelow;
    private int skipUntil;

    private int frameCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.main);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        configure(preferences);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        OptionsMenu.createMenu(this, menu);
        return super.onCreateOptionsMenu(menu);
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
            preview.setWillNotDraw(false);
            camera.startPreview();
            current.configure(camera);
            rgb = new int[size.width * size.height];
            camera.setPreviewCallback(this);
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

                builder.setTitle("Choose vision");

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
        if (holder != null) {
            if (skipFrames) {
                if (frameCount < skipBelow) {
                    frameCount++;
                    return;
                } else if (frameCount >= skipUntil) {
                    frameCount = 0;
                    return;
                }
            }

            frameCount++;

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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String s) {
        configure(preferences);
    }

    private void configure(SharedPreferences preferences) {
        skipFrames = preferences.getBoolean("skipFrames", false);

        final int rate = Integer.parseInt(preferences.getString("skipRate", "25"));
        final int divisor = MoreMath.gcd(rate, 100);
        skipBelow = rate / divisor;
        skipUntil = 100 / divisor;
    }

}
