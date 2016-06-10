package de.sicher.sichersmarthome.objects;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import de.sicher.sichersmarthome.R;
import de.sicher.sichersmarthome.adapter.DeviceAdapter;
import de.sicher.sichersmarthome.handler.ObjectHandler;

/**
 * Created by sicher on 03.05.2016.
 */
public class Light extends Device{

    public Light(String name, Room room) {
        super(name, room);
    }

    @Override
    public int getImageResourceID(){
        if(isActive()){
            return R.drawable.light_on;
        }else{
            return R.drawable.light_off;
        }
    }

    @Override
    public void onClick(final DeviceAdapter adapter, Context context) {

        final Device current = this;
        final boolean oldActive = isActive();

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);

        builder.setTitle(current.getName());
        View dialog = LayoutInflater.from(context).inflate(R.layout.dialog_light, null);

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        text.setText(current.getName() + " " + context.getString(R.string.bearbeiten));
        final ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        image.setImageResource(getImageResourceID());
        final Switch status = (Switch) dialog.findViewById(R.id.active);
        status.setChecked(current.isActive());
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setActive(status.isChecked());
                image.setImageResource(current.getImageResourceID());
                adapter.notifyDataSetChanged();
            }
        });

        builder.setView(dialog);
        final AlertDialog alertDialog = builder.show();
        alertDialog.setCancelable(false);

        Button delete = (Button) dialog.findViewById(R.id.deleteButton);
        // if decline button is clicked, close the custom dialog
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                handler.removeDevice(current);
                adapter.remove(current);
                alertDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
        Button about = (Button) dialog.findViewById(R.id.aboutButton);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setActive(oldActive);
                alertDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
        Button okay = (Button) dialog.findViewById(R.id.okayButton);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setActive(status.isChecked());
                alertDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
    }
}