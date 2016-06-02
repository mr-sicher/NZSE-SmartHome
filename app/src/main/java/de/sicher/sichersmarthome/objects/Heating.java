package de.sicher.sichersmarthome.objects;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import de.sicher.sichersmarthome.R;
import de.sicher.sichersmarthome.adapter.DeviceAdapter;

/**
 * Created by sicher on 03.05.2016.
 */
public class Heating extends Device{

    public final static int MAX_TEMPERATURE = 25;
    public final static int MIN_TEMPERATURE = 14;


    private int temperature;

    public Heating(String name, Room room){
        super(name, room);
        temperature = MIN_TEMPERATURE;
    }

    public int getTemperature(){
        return this.temperature;
    }

    public void setTemperature(int temperature){

        if(temperature < MIN_TEMPERATURE)
            temperature = MIN_TEMPERATURE;
        if(temperature > MAX_TEMPERATURE)
            temperature = MAX_TEMPERATURE;
        this.temperature = temperature;
    }

    @Override
    public int getImageResourceID(){
        if(!isActive()){
            return R.drawable.heating_off;
        }
        if(temperature > ((MAX_TEMPERATURE + MIN_TEMPERATURE) / 2)){
            return R.drawable.heating_hot;
        }else {
            return R.drawable.heating_cold;
        }
    }

    @Override
    public void onClick(final DeviceAdapter adapter, Context context) {

        final Heating current = this;
        final Heating oldDevice = new Heating(this.getName(), this.getRoom());
        oldDevice.setTemperature(this.getTemperature());
        oldDevice.setActive(this.isActive());

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);

        builder.setTitle(current.getName());
        View dialog = LayoutInflater.from(context).inflate(R.layout.dialog_heating, null);

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

        final TextView temperature_status = (TextView) dialog.findViewById(R.id.temperature_status_text);
        temperature_status.setText(current.getTemperature() + "°C");
        final SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.temperature);
        seekBar.setProgress(temperature - MIN_TEMPERATURE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temperature_status.setText(progress + MIN_TEMPERATURE + "°C");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current.setTemperature(seekBar.getProgress() + MIN_TEMPERATURE);
                current.setActive(true);
                adapter.notifyDataSetChanged();
                status.setChecked(true);
                image.setImageResource(current.getImageResourceID());
            }
        });

        builder.setView(dialog);
        final AlertDialog alertDialog = builder.show();
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
                current.setTemperature(oldDevice.getTemperature());
                current.setActive(oldDevice.isActive());
                alertDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
        Button okay = (Button) dialog.findViewById(R.id.okayButton);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setActive(status.isChecked());

                if(temperature != (MIN_TEMPERATURE + seekBar.getProgress())){
                    temperature = MIN_TEMPERATURE + seekBar.getProgress();
                    current.setTemperature(temperature);
                    current.setActive(true);
                }

                alertDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
    }

}
