package de.sicher.sichersmarthome.objects;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import de.sicher.sichersmarthome.R;
import de.sicher.sichersmarthome.adapter.DeviceAdapter;
import de.sicher.sichersmarthome.handler.ObjectHandler;

/**
 * Created by sicher on 03.05.2016.
 */
public abstract class Device{

    public final static int DELETE_VALUE = -1;
    public final static int STATUS_CHANGED = 0;

    private String name;
    private Room room;
    private boolean active;
    private boolean isInRoom;
    protected ObjectHandler handler;

    public Device(String name, Room room){
        this.name = name;
        this.room = room;
        active = false;
        isInRoom = false;
        handler = ObjectHandler.getObjectHandler();
    }


    public Room getRoom() {
        return room;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isInRoom() {
        return isInRoom;
    }

    public void setIsInRoom(boolean isInRoom) {
        this.isInRoom = isInRoom;
    }

    public void onLongClick(final DeviceAdapter adapter, Context context) {
        final Device current = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);

        builder.setTitle(current.getName());
        View dialog = LayoutInflater.from(context).inflate(R.layout.dialog_delete_device, null);
        Button okay = (Button) dialog.findViewById(R.id.okay_button);
        Button cancel = (Button) dialog.findViewById(R.id.cancel_button);
        builder.setView(dialog);
        final AlertDialog alertDialog = builder.show();

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                handler.removeDevice(current);
                adapter.remove(current);
                adapter.notifyDataSetChanged();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    public abstract int getImageResourceID();
    public abstract void onClick(DeviceAdapter adapter, Context context);
}
