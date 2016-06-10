package de.sicher.sichersmarthome;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import de.sicher.sichersmarthome.adapter.DeviceAdapter;
import de.sicher.sichersmarthome.adapter.RoomAdapter;
import de.sicher.sichersmarthome.handler.ObjectHandler;
import de.sicher.sichersmarthome.objects.Device;
import de.sicher.sichersmarthome.objects.Heating;
import de.sicher.sichersmarthome.objects.Light;
import de.sicher.sichersmarthome.objects.Room;

public class RoomActivity extends SmartActivity {

    private ObjectHandler handler;
    DeviceAdapter adapter;
    String title;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        final Context context = this;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        title = "Raumname";
        try {
            title = getIntent().getStringExtra(getString(R.string.room_name));
        } catch (NullPointerException e) {
        }

        getSupportActionBar().setTitle(title);

        handler = ObjectHandler.getObjectHandler();

        listView = (ListView) findViewById(R.id.items);

        //final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        adapter = new DeviceAdapter(this, handler.getAllDevices(title));

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Device device = (Device) parent.getItemAtPosition(position);
                System.out.println(device.getName());
                device.onClick(adapter, getApplicationContext());
                /*if(value == Device.DELETE_VALUE){
                    handler.removeDevice(device);
                }*/
            }
        });



        /**
         * FANCY FADING
         * listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override public void onItemClick(AdapterView<?> parent, final View view,
        int position, long id) {
        final Device item = (Device) parent.getItemAtPosition(position);
        view.animate().setDuration(2000).alpha(0)
        .withEndAction(new Runnable() {
        @Override public void run() {
        list.remove(item);
        adapter.notifyDataSetChanged();
        view.setAlpha(1);
        }
        });
        }

        });*/
    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Activity dieses = this;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.off_item:
                for(Device device : handler.getAllDevices()) {
                    device.setActive(false);
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Alle Geräte ausgeschaltet", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.add_item:

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
                builder.setTitle("Neues Gerät");
                final View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);

                final Spinner spinnerType = (Spinner) dialog.findViewById(R.id.spinner_type);
                ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this, R.array.spinner_type_available, android.R.layout.simple_spinner_item);
                adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(adapterType);

                //final ArrayList<Room> myrooms = handler.rooms;
                String[] names = new String[1];
                names[0] = title;

                final Spinner spinnerRoom = (Spinner) dialog.findViewById(R.id.spinner_room);
                spinnerRoom.setVisibility(View.GONE);

                builder.setView(dialog);
                final AlertDialog alertDialog = builder.show();
                alertDialog.setCancelable(false);

                Button save = (Button) dialog.findViewById(R.id.btn_save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.edit_name);
                        if (name.getText().toString().equals(""))
                            return;
                        if (spinnerType.getSelectedItem().toString().equals("Licht")) {
                            handler.addDevice(new Light(name.getText().toString(), handler.getRoom(title)));
                        } else if (spinnerType.getSelectedItem().toString().equals("Heizung")) {
                            handler.addDevice(new Heating(name.getText().toString(), handler.getRoom(title)));
                        }
                        adapter = new DeviceAdapter(dieses, handler.getAllDevices(title));
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        System.out.println("DONE");
                        alertDialog.dismiss();

                    }
                });

                Button chancel = (Button) dialog.findViewById(R.id.btn_cancel);
                chancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}




