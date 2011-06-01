package org.whiskeysierra.impairedvision;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ImpairedVision extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TextView view = new TextView(this);
        view.setText("Hello, Android");
        setContentView(view);
    }
    
}
