package de.sicher.sichersmarthome;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.sicher.sichersmarthome.adapter.GridAdapter;
import de.sicher.sichersmarthome.handler.ObjectHandler;
import de.sicher.sichersmarthome.objects.Device;
import de.sicher.sichersmarthome.objects.StartMenuItem;

public class StartActivity extends SmartActivity {
    private static final String TAG = "StartActivity";

    private ObjectHandler handler;

    private GridView gridView;
    private Button heatingOff, lightOff;
    private TextView numberDevices, numberHeatings, numberLights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ArrayList<StartMenuItem> data = new ArrayList<>();
        data.add(new StartMenuItem("Räume", R.drawable.ic_home) {
            @Override
            public void onClick() {
                Intent intent = new Intent(getApplicationContext(), ShowRoomActivity.class);
                startActivity(intent);
            }
        });
        data.add(new StartMenuItem("Lichter", R.drawable.light_on) {
            @Override
            public void onClick() {
                Intent intent = new Intent(getApplicationContext(), ShowLightActivity.class);
                intent.putExtra(getString(R.string.app_name), "Lichter");
                startActivity(intent);
            }
        });
        data.add(new StartMenuItem("Heizungen", R.drawable.heating_cold) {
            @Override
            public void onClick() {
                Intent intent = new Intent(getApplicationContext(), ShowHeatingActivity.class);
                intent.putExtra(getString(R.string.app_name), "Heizungen");
                startActivity(intent);
            }
        });

        handler = ObjectHandler.getObjectHandler();
        if(!handler.generateObjects(this)) {
            handler.generate();
            Log.e(TAG, "Something went wrong");
            Toast.makeText(StartActivity.this, "Load default", Toast.LENGTH_SHORT).show();
        }

        numberDevices = (TextView)findViewById(R.id.number_all_devices);
        numberDevices.setText(handler.getActiveDevices() + "");

        numberHeatings = (TextView)findViewById(R.id.number_all_heatings);
        numberHeatings.setText(handler.getActiveHeatings() + "");

        numberLights = (TextView)findViewById(R.id.number_all_light);
        numberLights.setText(handler.getActiveLights() + "");

        gridView = (GridView) findViewById(R.id.overview);
        gridView.setAdapter(new GridAdapter(this, data));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((StartMenuItem) parent.getItemAtPosition(position)).onClick();
            }
        });

        heatingOff = (Button) findViewById(R.id.heating_off);
        heatingOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Device heating : handler.getHeatings()) {
                    heating.setActive(false);
                }
                heatingOff.setEnabled(false);

                numberDevices.setText(handler.getActiveDevices() + "");
                numberHeatings.setText(handler.getActiveHeatings() + "");
            }
        });
        heatingOff.setEnabled(handler.getActiveHeatings() > 0);
        lightOff = (Button) findViewById(R.id.light_off);
        lightOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Device light : handler.getLights()){
                    light.setActive(false);
                }
                lightOff.setEnabled(false);

                numberDevices.setText(handler.getActiveDevices() + "");
                numberLights.setText(handler.getActiveLights() + "");
            }
        });
        lightOff.setEnabled(handler.getActiveLights() > 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_start_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Activity dieses = this;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.off_item:
                for (Device device : handler.getAllDevices()) {
                    device.setActive(false);
                }
                refresh();
                Toast.makeText(getApplicationContext(), "Alle Geräte ausgeschaltet", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
    private void refresh(){

        numberDevices.setText(handler.getActiveDevices() + "");
        numberHeatings.setText(handler.getActiveHeatings() + "");
        numberLights.setText(handler.getActiveLights() + "");

        heatingOff.setEnabled(handler.getActiveHeatings() > 0);
        lightOff.setEnabled(handler.getActiveLights() > 0);
    }

}
