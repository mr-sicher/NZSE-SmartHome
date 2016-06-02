package de.sicher.sichersmarthome;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;

import de.sicher.sichersmarthome.adapter.DeviceAdapter;
import de.sicher.sichersmarthome.handler.ObjectHandler;
import de.sicher.sichersmarthome.objects.Device;
import de.sicher.sichersmarthome.objects.Heating;
import de.sicher.sichersmarthome.objects.Light;
import de.sicher.sichersmarthome.objects.Room;

public class ShowHeatingActivity extends SmartActivity {
    private ObjectHandler handler;
    DeviceAdapter adapter;

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

        String title = "";
        try{
            title = getIntent().getStringExtra(getString(R.string.app_name));
        }catch(NullPointerException e){
        }

        getSupportActionBar().setTitle(title);

        handler = ObjectHandler.getObjectHandler();

        listView = (ListView) findViewById(R.id.items);

        //final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        adapter = new DeviceAdapter(this, handler.getHeatings());
        listView.setAdapter(adapter);

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
                builder.setTitle("Neue Heizung");
                final View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);

                final Spinner spinnerType = (Spinner) dialog.findViewById(R.id.spinner_type);
                spinnerType.setVisibility(View.GONE);

                ArrayList<Room> myrooms = handler.getRooms();
                String[] names = new String[myrooms.size()];

                for(int i = 0; i < myrooms.size(); i++){
                    names[i] = myrooms.get(i).getName();
                }

                final Spinner spinnerRoom = (Spinner) dialog.findViewById(R.id.spinner_room);
                ArrayAdapter<String> adapterRoom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);
                adapterRoom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRoom.setAdapter(adapterRoom);

                builder.setView(dialog);
                final AlertDialog alertDialog = builder.show();

                Button save = (Button) dialog.findViewById(R.id.btn_save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.edit_name);
                        if (name.getText().toString().equals(""))
                            return;

                        handler.addDevice(new Heating(name.getText().toString(), handler.getRoom(spinnerRoom.getSelectedItem().toString())));

                        adapter = new DeviceAdapter(dieses, handler.getHeatings());//für die heizung in getHeatings ändern
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