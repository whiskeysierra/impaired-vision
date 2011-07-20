package org.whiskeysierra.impairedvision;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.TextView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class VisionActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(VisionActivity.class);

    private Camera camera;
    private SurfaceView preview;
    private SurfaceHolder holder;

    private int sizeIndex;
    private Camera.Size size;

    private int currentIndex;
    private final List<Vision> visions = new CyclingList<Vision>(
            new NormalVision(),
            new Myopia(),
            new Protanopia(),
            new Deuteranopia(),
            new Tritanopia(),
            new Achromatopia()
    );

    private TextView name;

    private int[] rgb;
    private final Paint paint = new Paint();

    private boolean skipFrames;
    private int skipBelow;
    private int skipUntil;

    private int frameCount;

    private float width;
    private float height;
    private float ratio;

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

        preview = (SurfaceView) findViewById(R.id.preview);
        name = (TextView) findViewById(R.id.name);

        configure(preferences);

        holder = preview.getHolder();
        holder.addCallback(this);

        final SwipeDetector detector = new SwipeDetector(new SwipeListener(this));
        preview.setOnTouchListener(detector);
        // this is a noop implementation, but somehow required for the touch events to fire
        preview.setOnClickListener(detector);
    }

    void switchToPrevious() {
        switchTo(currentIndex - 1);
    }

    void switchToNext() {
        switchTo(currentIndex + 1);
    }

    private void switchTo(int index) {
        currentIndex = index;
        final Vision vision = visions.get(currentIndex);
        if (camera != null) {
            vision.configure(camera);
        }
        paint.setColorFilter(vision.getFilter());
        name.setText(vision.getName());
        LOG.debug("Switched to {}", vision);
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
            final Camera.Parameters parameters = camera.getParameters();
            size = parameters.getSupportedPreviewSizes().get(sizeIndex);
            parameters.setPreviewSize(size.width, size.height);
            camera.setParameters(parameters);
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
            switchTo(0);
            camera.setPreviewCallback(this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.width = width;
        this.height = height;
        this.ratio = this.width / this.height;
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

                if (rgb == null) {
                    rgb = new int[size.width * size.height];
                }

                Yuv420.decode(yuv, rgb, size.width, size.height);

                final float x;
                final float y;

                if (size.width < width && size.height < height) {
                    final float bitmapRatio = (float) size.width / (float) size.height;

                    final float scale;

                    if (bitmapRatio < ratio) {
                        scale = height / (float) size.height;
                    } else {
                        scale = width / (float) size.width;
                    }

                    canvas.scale(scale, scale, canvas.getWidth() / 2f, canvas.getHeight() / 2f);
                    x = width / 2f - size.width / 2f;
                    y = height / 2f - size.height / 2f;
                } else {
                    x = 0;
                    y = 0;
                }

                canvas.drawBitmap(rgb, 0, size.width, x, y, size.width, size.height, false, paint);
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
        final String index = preferences.getString("sizeIndex", getString(R.string.defaultSizeIndex));

        if (index != null) {
            size = null;
            sizeIndex = Integer.parseInt(index);
            rgb = null;
        }

        skipFrames = preferences.getBoolean("skipFrames", Boolean.parseBoolean(getString(R.string.defaultSkipFrames)));

        final int rate = Integer.parseInt(preferences.getString("skipRate", getString(R.string.defaultSkipRate)));
        final int divisor = MoreMath.gcd(rate, 100);
        skipBelow = rate / divisor;
        skipUntil = 100 / divisor;

        if (preferences.getBoolean("displayName", Boolean.parseBoolean(getString(R.string.defaultDisplayName)))) {
            name.setVisibility(View.VISIBLE);
        } else {
            name.setVisibility(View.INVISIBLE);
        }
    }

}
