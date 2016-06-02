package de.sicher.sichersmarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import de.sicher.sichersmarthome.R;
import de.sicher.sichersmarthome.handler.ObjectHandler;
import de.sicher.sichersmarthome.objects.Device;

/**
 * Created by sicher on 03.05.2016.
 */
public class DeviceAdapter extends ArrayAdapter<Device> {

    private final Context context;
    private final ArrayList<Device> values;
    public ObjectHandler handler;

    public DeviceAdapter(Context context, ArrayList<Device> values) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;
        handler = ObjectHandler.getObjectHandler();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DeviceAdapter executer = this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowLayout = inflater.inflate(R.layout.list_item, parent, false);
        RelativeLayout layout = (RelativeLayout) rowLayout.findViewById(R.id.outer_layout);
        layout.setClickable(true);
        TextView nameText = (TextView) rowLayout.findViewById(R.id.name);
        TextView descriptionText = (TextView) rowLayout.findViewById(R.id.description);
        ImageView icon = (ImageView) rowLayout.findViewById(R.id.icon);
        Switch status = (Switch) rowLayout.findViewById(R.id.status);
        final Device current = values.get(position);
        nameText.setText(current.getName());
        status.setChecked(current.isActive());
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setActive(!current.isActive());
                executer.notifyDataSetChanged();
            }
        });
        if(current.isInRoom()){
            descriptionText.setText("");
        }else{
            descriptionText.setText(current.getRoom().getName());
        }
        rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.onClick(executer, context);
            }
        });
        rowLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                current.onLongClick(executer, context);
                return true;
            }
        });
        icon.setImageResource(current.getImageResourceID());
        return rowLayout;
    }
}
