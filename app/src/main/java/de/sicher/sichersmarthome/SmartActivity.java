package de.sicher.sichersmarthome;

import android.support.v7.app.AppCompatActivity;

import de.sicher.sichersmarthome.handler.ObjectHandler;

/**
 * Created by sicher on 24.05.2016.
 */
public abstract class SmartActivity extends AppCompatActivity {


    @Override
    protected void onPause() {
        super.onPause();
        ObjectHandler handler = ObjectHandler.getObjectHandler();
        handler.generateFile(this);
    }
}
