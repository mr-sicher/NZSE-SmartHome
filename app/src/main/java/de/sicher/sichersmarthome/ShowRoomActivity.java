package de.sicher.sichersmarthome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.sicher.sichersmarthome.adapter.DeviceAdapter;
import de.sicher.sichersmarthome.adapter.RoomAdapter;
import de.sicher.sichersmarthome.handler.ObjectHandler;
import de.sicher.sichersmarthome.objects.Device;
import de.sicher.sichersmarthome.objects.Heating;
import de.sicher.sichersmarthome.objects.Light;
import de.sicher.sichersmarthome.objects.Room;

public class ShowRoomActivity extends SmartActivity {

    private ObjectHandler handler;
    RoomAdapter adapter;

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

        getSupportActionBar().setTitle(getString(R.string.rooms));

        handler = ObjectHandler.getObjectHandler();

        listView = (ListView) findViewById(R.id.items);

        //final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        adapter = new RoomAdapter(this, handler.getRooms());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ShowRoomActivity.this, "HELP HELP", Toast.LENGTH_SHORT).show();
                Room room = (Room) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                intent.putExtra(getString(R.string.room_name), room.getName());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Room room = (Room) parent.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);

                builder.setTitle(room.getName());
                View dialog = LayoutInflater.from(context).inflate(R.layout.dialog_delete_room, null);
                Button okay = (Button) dialog.findViewById(R.id.okay_button);
                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);
                builder.setView(dialog);
                final AlertDialog alertDialog = builder.show();

                okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        for (Device d : handler.getAllDevices(room.getName())) {
                            handler.removeDevice(d);
                        }
                        handler.removeRoom(room);
                        adapter.remove(room);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                return true;
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle(getString(R.string.rooms));
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
                Toast.makeText(getApplicationContext(), "Alle Geräte ausgeschaltet", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.add_item:

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
                builder.setTitle("Neuer Raum");
                final View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_add_room, null);

                builder.setView(dialog);
                final AlertDialog alertDialog = builder.show();

                Button save = (Button) dialog.findViewById(R.id.btn_save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.edit_name);
                        if (name.getText().toString().equals(""))
                            return;

                        Room newRoom = new Room(name.getText().toString());
                        handler.addRoom(newRoom);

                        adapter = new RoomAdapter(dieses, handler.getRooms());
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        //System.out.println("DONE");
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
