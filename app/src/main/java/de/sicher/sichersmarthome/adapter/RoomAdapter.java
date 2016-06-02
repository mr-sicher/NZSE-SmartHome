package de.sicher.sichersmarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import de.sicher.sichersmarthome.R;
import de.sicher.sichersmarthome.objects.Device;
import de.sicher.sichersmarthome.objects.Room;

/**
 * Created by sicher on 04.05.2016.
 */
public class RoomAdapter extends ArrayAdapter<Room> {

    private final Context context;
    private final ArrayList<Room> values;

    public RoomAdapter(Context context, ArrayList<Room> values) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RoomAdapter executer = this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowLayout = inflater.inflate(R.layout.list_item, parent, false);
        TextView nameText = (TextView) rowLayout.findViewById(R.id.name);
        TextView descriptionText = (TextView) rowLayout.findViewById(R.id.description);
        ImageView icon = (ImageView) rowLayout.findViewById(R.id.icon);
        Switch status = (Switch) rowLayout.findViewById(R.id.status);
        final Room current = values.get(position);
        nameText.setText(current.getName());
        descriptionText.setVisibility(View.GONE);
        status.setVisibility(View.GONE);
        /*status.setOnClickListener(new View.OnClickListener() {
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
        icon.setImageResource(current.getImageResourceID());*/
        return rowLayout;
    }
}
